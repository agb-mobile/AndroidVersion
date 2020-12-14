package si.kamino.gradle

import com.android.build.api.artifact.ArtifactType
import com.android.build.api.artifact.Artifacts
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import si.kamino.gradle.common.VersionUtil
import si.kamino.gradle.extensions.VersionExtension
import si.kamino.gradle.task.CalculateVariantVersionTask
import si.kamino.gradle.task.ManifestVersionTransformationTask
import java.io.File

class VersionPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        if (!VersionUtil.is410OrAbove()) {
            throw IllegalStateException("VersionPlugin requires Android Gradle Plugin 4.1 or above. To use plugin with older versions of gradle downgrade it to version 1.x.x of this plugins.")
        }

        var hasAndroidAppPlugin = false
        project.plugins.all {
            if (it is AppPlugin) {
                createExtensions(project)
                createTasks(project)
                hasAndroidAppPlugin = true
            }
        }

        project.afterEvaluate {
            if (!hasAndroidAppPlugin) {
                project.logger.warn("Android version plugin not applied as android gradle plugin was not applied!")
            }
        }

    }

    private fun createExtensions(project: Project): VersionExtension {
        return project.extensions.create("androidVersion", VersionExtension::class.java, project)
    }

    private fun createTasks(project: Project) {
        val androidExtension = project.extensions.findByName("android") as ApplicationExtension<*, *, *, *, *>

        androidExtension.onVariantProperties {
            val taskName = "version${name.capitalize()}"
            val versionTask = project.tasks.register(taskName, CalculateVariantVersionTask::class.java) {
                it.productFlavors.set(productFlavors)
                it.buildType.set(buildType)
                it.versionExtension.set(project.extensions.findByType(VersionExtension::class.java)!!)
                it.versionOutputFile.set(File(project.buildDir, "intermediates/version/$name"))
            }

            val manifestUpdater = project.tasks.register("${taskName}ManifestUpdater", ManifestVersionTransformationTask::class.java) {
                    it.versionFile.set(versionTask.flatMap {
                        it.versionOutputFile
                    })
                }

            setupMerger(artifacts, manifestUpdater)

        }

    }

    private fun setupMerger(artifacts: Artifacts, manifestUpdater: TaskProvider<ManifestVersionTransformationTask>) {
        // and wire things up with the Android Gradle Plugin
        artifacts.use(manifestUpdater)
            .wiredWithFiles({
                it.mergedManifestFile
            }, {
                it.updatedManifestFile
            })
            .toTransform(ArtifactType.MERGED_MANIFEST)
    }

}