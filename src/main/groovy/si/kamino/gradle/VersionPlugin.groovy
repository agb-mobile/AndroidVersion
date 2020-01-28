package si.kamino.gradle

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.api.BaseVariant
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.execution.TaskExecutionGraph
import org.gradle.api.execution.TaskExecutionGraphListener
import org.gradle.api.tasks.TaskProvider
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

        if (!VersionUtils.isGradle4_2orAbove(project.gradle)) {
            throw new IllegalStateException("VersionPlugin requires Gradle 4.2 or above. To use plugin with older versions of gradle downgrade it to 1.2.4.")
        }

        this.project = project

        def hasAndroidPlugin = false;
        project.plugins.all {
            if (it instanceof AppPlugin) {
                createExtensions()
                createTasks()
                hasAndroidPlugin = true
            }
        }

        project.afterEvaluate {
            if (!hasAndroidPlugin) {
                project.logger.warn("Android project not present!")
            }
        }
    }

    private void createExtensions() {
        extension = project.extensions.create('androidVersion', VersionExtension, project)
    }

    private void createTasks() {

        project.android.applicationVariants.all { variant ->

            String taskName = "version${variant.name.capitalize()}"

            final versionTaskProvider = project.tasks.register(taskName, BuildVersionTask) {
                setVariant(variant)
            }

            variant.getPreBuildProvider().configure {
                dependsOn versionTaskProvider
            }

            skipInstantRunIfNeeded(variant, versionTaskProvider)
        }

    }

    static isAndroidProject(Project project) {
        project.pluginManager.hasPlugin('com.android.application') || project.pluginManager.hasPlugin('com.android.library') || project.pluginManager.hasPlugin('com.android.test')
    }

    /**
     * Workaround Android Gradle plugin that was incorrectly handling overrideVersionCode when running in instant
     * run mode.
     *
     * @link https://code.google.com/p/android/issues/detail?id=227610
     */
    private void skipInstantRunIfNeeded(BaseVariant variant, TaskProvider<BuildVersionTask> versionTaskProvider) {
        if (!VersionUtils.is330orAbove()) {
            taskExecutionGraph.addTaskExecutionGraphListener(new TaskExecutionGraphListener() {
                @Override
                void graphPopulated(TaskExecutionGraph taskExecutionGraph) {
                    versionTaskProvider.configure {
                        enabled = !taskExecutionGraph.hasTask("${project.path}:incremental${variant.name.capitalize()}Tasks")
                    }
                }
            })

        }
    }

}
