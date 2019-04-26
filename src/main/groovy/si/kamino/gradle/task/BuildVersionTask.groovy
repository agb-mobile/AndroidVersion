package si.kamino.gradle.task

import com.android.build.OutputFile
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.api.BaseVariantOutput
import com.android.builder.model.ProductFlavor
import groovy.text.SimpleTemplateEngine
import groovy.text.Template
import org.gradle.api.DefaultTask
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.tasks.TaskAction
import si.kamino.gradle.commons.VersionUtils
import si.kamino.gradle.extensions.VersionExtension
import si.kamino.gradle.extensions.version.ExtendingVersion
import si.kamino.gradle.extensions.version.StaticVersion

class BuildVersionTask extends DefaultTask {

    private BaseVariant variant
    private Template fileTemplate

    void setVariant(BaseVariant variant) {
        this.variant = variant
    }

    @TaskAction void buildVersion() {

        VersionExtension extension = project.androidVersion

        if (extension.fileNamePattern) {
            fileTemplate = new SimpleTemplateEngine().createTemplate(extension.fileNamePattern)
        }

        def appVersion = extension.appVersion
        final StaticVersion variantVersion = new StaticVersion(appVersion.major, appVersion.minor, appVersion.build)

        applyFlavorVersion(extension, variantVersion)
        applyBuildTypeVersion(extension, variantVersion)
        applyVariantCombinationVersion(extension, variantVersion)

        variantVersion.versionCode(appVersion.getVersionCode().buildVersionCode(variantVersion))

        variant.productFlavors.each { flavor ->
            def version = extension.variants.findByName(flavor.name)

            if (version != null) {
                def code = version.getVersionCode()
                if (code) {
                    variantVersion.versionCode(code.buildVersionCode(variantVersion))
                }
            }
        }

        applyOutputVersions(extension, variantVersion)

//        if (!VersionUtils.is320orAbove()) {
//            variant.getMergedFlavor().versionName = variantVersion.versionName
//            variant.getMergedFlavor().versionCode = variantVersion.versionCode
//        }
    }

    private void applyFlavorVersion(VersionExtension extension, final StaticVersion variantVersion) {

        variant.productFlavors.each { flavor ->
            applyVariantVersion(extension, variantVersion, flavor.name)
        }

    }

    private void applyBuildTypeVersion(VersionExtension extension, final StaticVersion variantVersion) {
        applyVariantVersion(extension, variantVersion, variant.buildType.name)
    }

    private void applyVariantCombinationVersion(VersionExtension extension, final StaticVersion variantVersion) {
        if (variant.productFlavors.size() > 0) {
            def variantName
            variant.productFlavors.eachWithIndex { ProductFlavor flavor, int index ->
                if (index == 0) {
                    variantName = flavor.name
                } else {
                    variantName = "$variantName${flavor.name.capitalize()}"
                    applyVariantVersion(extension, variantVersion, variantName)
                }
            }

            variantName = "$variantName${variant.buildType.name.capitalize()}"
            applyVariantVersion(extension, variantVersion, variantName)
        }

    }

    static void applyVariantVersion(VersionExtension extension, final StaticVersion variantVersion,
                                    final String variantName) {
        def version = extension.variants.findByName(variantName)

        if (version != null) {
            variantVersion.apply(version)
        }
    }

    private void applyOutputVersions(VersionExtension extension, StaticVersion variantVersion) {
        variant.outputs.each { output ->

            def filters
            if (VersionUtils.is300orAbove()) {
                filters = output.getFilterTypes()
            } else {
                filters = output.getMainOutputFile().getFilterTypes()
            }
            StaticVersion splitVersion = variantVersion.clone()

            if (!filters.isEmpty()) {

                filters.each { split ->

                    ExtendingVersion version = BuildVersionTask.getVersionForSplit(extension, output, split)

                    if (version != null) {
                        splitVersion.apply(version)
                    }

                }

                filters.each { split ->

                    ExtendingVersion version = BuildVersionTask.getVersionForSplit(extension, output, split)

                    if (version != null) {
                        def code = version.getVersionCode()
                        if (code) {
                            splitVersion.versionCode(code.buildVersionCode(splitVersion))
                        }
                    }

                }

            }

            def variantSuffix = variant.getMergedFlavor().getVersionNameSuffix()
            def buildTypeSuffix = variant.buildType.getVersionNameSuffix()
            def splitVersionName = splitVersion.versionName + (variantSuffix ? variantSuffix : "") + (buildTypeSuffix ? buildTypeSuffix : "")

            output.versionNameOverride = splitVersionName
            output.versionCodeOverride = splitVersion.versionCode

            if (fileTemplate) {
                def makeMap = [
                        "project"    : project.name,
                        "variantName": output.name,
                        "buildType"  : variant.buildType.name,
                        "flavorName" : variant.flavorName,
                        "versionCode": splitVersion.versionCode,
                        "versionName": splitVersionName,
                        "flavors"   : variant.productFlavors.collectEntries {
                            [it.dimension, it.name]
                        }
                ]

                def apkName = fileTemplate.make(makeMap)

                if (apkName) {
                    output.outputFile = new File(output.outputFile.parent, "${apkName}.apk")
                }
            }

        }

    }

    private static ExtendingVersion getVersionForSplit(VersionExtension extension, BaseVariantOutput output, String split) {

        NamedDomainObjectContainer<ExtendingVersion> splitContainer

        if (OutputFile.DENSITY == split) {
            splitContainer = extension.splits.density
        } else if (OutputFile.ABI == split) {
            splitContainer = extension.splits.abi
        } else {
            throw new IllegalStateException("Unknown split type: " + split)
        }

        def filter = output.getFilter(split)
        splitContainer.findByName(filter)

    }

    Template getFileTemplate() {
        return fileTemplate
    }

    BaseVariant getVariant() {
        return variant
    }
}
