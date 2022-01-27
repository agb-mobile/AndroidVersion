package si.kamino.gradle

import com.android.build.api.artifact.Artifacts
import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import si.kamino.gradle.extensions.VersionExtension
import si.kamino.gradle.task.CalculateVariantVersionTask
import si.kamino.gradle.task.ManifestVersionTransformationTask
import java.io.File

class VersionPlugin : Plugin<Project> {

    override fun apply(project: Project) {
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
        return project.extensions.create("androidVersion", VersionExtension::class.java, project.objects)
    }

    private fun createTasks(project: Project) {
        val components = project.extensions.findByName("androidComponents") as ApplicationAndroidComponentsExtension

        components.onVariants { variant ->
            val taskName = "version${variant.name.capitalize()}"
            val versionTask = project.tasks.register(taskName, CalculateVariantVersionTask::class.java) {
                it.productFlavors.set(variant.productFlavors)
                it.buildType.set(variant.buildType!!)
                it.versionExtension.set(project.extensions.findByType(VersionExtension::class.java)!!)
                it.versionOutputFile.set(File(project.buildDir, "intermediates/version/${variant.name}"))
            }

            val manifestUpdater = project.tasks.register(
                "${taskName}ManifestUpdater",
                ManifestVersionTransformationTask::class.java
            ) {
                it.versionFile.set(versionTask.flatMap {
                    it.versionOutputFile
                })
            }

            setupMerger(variant.artifacts, manifestUpdater)
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
            .toTransform(SingleArtifact.MERGED_MANIFEST)
    }

}
