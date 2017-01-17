package si.kamino.gradle.task

import com.android.build.OutputFile
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.api.BaseVariantOutput
import org.gradle.api.DefaultTask
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.tasks.TaskAction
import si.kamino.gradle.extensions.version.ExtendingVersion
import si.kamino.gradle.extensions.version.StaticVersion
import si.kamino.gradle.extensions.VersionExtension

class BuildVersionTask extends DefaultTask {

    private BaseVariant variant

    void setVariant(BaseVariant variant) {
        this.variant = variant
    }

    @TaskAction void buildVersion() {

        VersionExtension extension = project.androidVersion
        def appVersion = extension.appVersion
        final StaticVersion variantVersion = new StaticVersion(appVersion.major, appVersion.minor, appVersion.build)

        variant.getProductFlavors().each { flavor ->
            def version = extension.variants.findByName(flavor.name)

            if (version != null) {
                variantVersion.apply(version)
            }
        }

        variantVersion.versionCode(appVersion.getVersionCode().buildVersionCode(variantVersion))

        variant.getProductFlavors().each { flavor ->
            def version = extension.variants.findByName(flavor.name)

            if (version != null) {
                def code = version.getVersionCode()
                if (code) {
                    variantVersion.versionCode(code.buildVersionCode(variantVersion))
                }
            }
        }

        println variant.name
        applyOutputVersions(extension, variantVersion)

//        Change only output for the time being
//        variant.getMergedFlavor().versionName = variantVersion.versionName
//        variant.getMergedFlavor().versionCode = variantVersion.versionCode
    }

    private void applyOutputVersions(VersionExtension extension, StaticVersion variantVersion) {
        variant.outputs.each { output ->

            def filters = output.getMainOutputFile().getFilterTypes()

            if (filters.isEmpty()) {
                output.versionNameOverride = variantVersion.versionName
                output.versionCodeOverride = variantVersion.versionCode
            } else {

                StaticVersion splitVersion = variantVersion.clone()

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
                            splitVersion.versionCode = code.buildVersionCode(splitVersion)
                        }
                    }

                }

                output.versionNameOverride = splitVersion.versionName
                output.versionCodeOverride = splitVersion.versionCode

            }
        }

    }

    private static ExtendingVersion getVersionForSplit(VersionExtension extension, BaseVariantOutput output, String split) {

        NamedDomainObjectContainer<ExtendingVersion> splitContainer

        if (OutputFile.DENSITY.equals(split)) {
            splitContainer = extension.splits.density
        } else if (OutputFile.ABI.equals(split)) {
            splitContainer = extension.splits.abi
        } else {
            throw new IllegalStateException("Unknown split type: " + split)
        }

        def filter = output.getFilter(split)
        splitContainer.findByName(filter)

    }

}
