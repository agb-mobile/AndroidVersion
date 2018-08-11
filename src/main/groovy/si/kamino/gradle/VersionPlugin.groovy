package si.kamino.gradle

import com.android.build.gradle.api.BaseVariant
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.execution.TaskExecutionGraph
import org.gradle.api.execution.TaskExecutionGraphListener
import si.kamino.gradle.commons.VersionUtils
import si.kamino.gradle.extensions.VersionExtension
import si.kamino.gradle.task.BuildVersionTask

import javax.inject.Inject

class VersionPlugin implements Plugin<Project> {

    private final TaskExecutionGraph taskExecutionGraph;

    private Project project

    private VersionExtension extension

    @Inject
    VersionPlugin(TaskExecutionGraph taskExecutionGraph) {
        this.taskExecutionGraph = taskExecutionGraph
    }

    @Override
    void apply(Project project) {

        if (!isAndroidProject(project)) {
            throw new IllegalStateException("VersionPlugin only works with Android projects but \"${project.name}\" is none")
        }

        if (!VersionUtils.isGradle4_2orAbove(project.gradle)) {
            throw new IllegalStateException("VersionPlugin requires Gradle 4.2 or above. To use plugin with older versions of gradle downgrade it to 1.2.4.")
        }

        this.project = project

        createExtensions()
        createTasks()
    }

    private void createExtensions() {
        extension = project.extensions.create('androidVersion', VersionExtension, project)
    }

    private void createTasks() {

        project.android.applicationVariants.all { variant ->

            String taskName = "version${variant.name.capitalize()}"

            final versionTask = project.tasks.create(taskName, BuildVersionTask)
            versionTask.setVariant(variant)

            variant.getPreBuild().dependsOn versionTask

            skipInstantRunIfNeeded(variant, versionTask)
        }

    }

    static isAndroidProject(Project project) {
        project.plugins.hasPlugin('com.android.application') || project.plugins.hasPlugin('com.android.library') || project.plugins.hasPlugin('com.android.test')
    }

    /**
     * Workaround Android Gradle plugin that was incorrectly handling overrideVersionCode when running in instant
     * run mode.
     * 
     * @link https://code.google.com/p/android/issues/detail?id=227610
     */
    private void skipInstantRunIfNeeded(BaseVariant variant, BuildVersionTask versionTask) {
        if (VersionUtils.is330orAbove()) {
            taskExecutionGraph.addTaskExecutionGraphListener(new TaskExecutionGraphListener() {
                @Override
                void graphPopulated(TaskExecutionGraph taskExecutionGraph) {
                    versionTask.enabled = !taskExecutionGraph.hasTask("${project.path}:incremental${variant.name.capitalize()}Tasks")
                }
            })

        }
    }

}
