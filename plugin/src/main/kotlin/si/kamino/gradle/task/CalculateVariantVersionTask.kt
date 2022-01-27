package si.kamino.gradle.task

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import si.kamino.gradle.extensions.VersionExtension
import si.kamino.gradle.extensions.name.internal.StaticVersion

abstract class CalculateVariantVersionTask : DefaultTask() {

    @get:Input
    abstract val productFlavors: ListProperty<Pair<String, String>>

    @get:Input
    abstract val buildType: Property<String?>

    @get:Nested
    abstract val versionExtension: Property<VersionExtension>

    @get:OutputFile
    abstract val versionOutputFile: RegularFileProperty

    @TaskAction
    fun taskAction() {
        val versionName = versionExtension.get().versionName

        val variantVersion = StaticVersion(
            versionName.getKeys().map { it to versionName.getValue(it) }
                .toMap(LinkedHashMap())
        )

        // Build version names
        applyFlavorVersion(versionExtension.get(), variantVersion)
        applyBuildTypeVersion(versionExtension.get(), variantVersion)
        applyVariantCombinationVersion(versionExtension.get(), variantVersion)

        // TODO splits!! How do we get manifest file transformation for splits.
//        applyOutputVersions(versionExtension, variantVersion)

        versionOutputFile.get().asFile.writeText(
            variantVersion.versionName()
                    + "\n"
                    + versionExtension.get().versionCode.getVersionCode(variantVersion).toString()
        )
    }

    private fun applyFlavorVersion(versionExtension: VersionExtension, variantVersion: StaticVersion) {
        productFlavors.get().forEach { (name, _) ->
            applyVariantVersion(versionExtension, variantVersion, name)
        }
    }

    //
    private fun applyBuildTypeVersion(versionExtension: VersionExtension, variantVersion: StaticVersion) {
        // TODO why could this be null?
        applyVariantVersion(versionExtension, variantVersion, buildType.get()!!)
    }

    private fun applyVariantCombinationVersion(versionExtension: VersionExtension, variantVersion: StaticVersion) {
        if (productFlavors.get().isNotEmpty()) {
            var variantName = ""
            productFlavors.get().forEachIndexed { index, (flavorName, _) ->
                // TODO is name first or second parameter in pair?
                if (index == 0) {
                    variantName = flavorName
                } else {
                    variantName = "$variantName${flavorName.capitalize()}"
                    applyVariantVersion(versionExtension, variantVersion, variantName)
                }
            }
            // TODO when could build type be null?
            variantName = "$variantName${buildType.get()!!.capitalize()}"
            applyVariantVersion(versionExtension, variantVersion, variantName)
        }

    }

    private fun applyVariantVersion(
        versionExtension: VersionExtension,
        variantVersion: StaticVersion,
        variantName: String
    ) {
        versionExtension.variants.findByName(variantName)?.let { extending ->
            variantVersion.version.keys.forEach { key ->
                extending.version[key]?.let { variantVersion.version[key] = it }
            }
            extending.suffix?.let { variantVersion.appendSuffix(it) }
        }
    }

}
