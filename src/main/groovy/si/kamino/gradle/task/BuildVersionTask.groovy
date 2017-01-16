package si.kamino.gradle.task

import com.android.build.OutputFile
import com.android.build.gradle.api.BaseVariant
import org.gradle.api.DefaultTask
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.tasks.TaskAction
import si.kamino.gradle.extensions.ConstantVersion
import si.kamino.gradle.extensions.ExtendingVersion
import si.kamino.gradle.extensions.VersionExtension

class BuildVersionTask extends DefaultTask {

    private BaseVariant variant

    void setVariant(BaseVariant variant) {
        this.variant = variant
    }

    @TaskAction void buildVersion() {

        VersionExtension extension = project.androidVersion
        final ConstantVersion variantVersion = extension.appVersion.clone()

        variant.getProductFlavors().each { flavor ->
            def version = extension.variants.findByName(flavor.name)

            if (version != null) {
                variantVersion.apply(version)
            }
        }

        variant.outputs.each { output ->

            def filters = output.getMainOutputFile().getFilterTypes()

            if (filters.isEmpty()) {
                output.versionNameOverride = variantVersion.versionName
                output.versionCodeOverride = variantVersion.versionCode
            } else {

                ConstantVersion splitVersion = variantVersion.clone()

                filters.each { split ->

                    NamedDomainObjectContainer<ExtendingVersion> splitContainer

                    if (OutputFile.DENSITY.equals(split)) {
                        splitContainer = extension.splits.density
                    } else if (OutputFile.ABI.equals(split)) {
                        splitContainer = extension.splits.abi
                    } else {
                        throw new IllegalStateException("Unknown split type: " + split)
                    }

                    def filter = output.getFilter(split)
                    ExtendingVersion version = splitContainer.findByName(filter)

                    if (version != null) {
                        splitVersion.apply(version)
                    }

                }

                output.versionNameOverride = splitVersion.versionName
                output.versionCodeOverride = splitVersion.versionCode

            }
        }

//        Change only output for the time being
//        variant.getMergedFlavor().versionName = variantVersion.versionName
//        variant.getMergedFlavor().versionCode = variantVersion.versionCode
    }

}
