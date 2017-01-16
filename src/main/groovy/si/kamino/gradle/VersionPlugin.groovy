package si.kamino.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.execution.TaskExecutionGraph
import org.gradle.api.execution.TaskExecutionGraphListener
import org.gradle.internal.reflect.Instantiator
import si.kamino.gradle.extensions.ConstantVersion
import si.kamino.gradle.extensions.ExtendingVersion
import si.kamino.gradle.extensions.Splits
import si.kamino.gradle.extensions.VersionExtension
import si.kamino.gradle.task.BuildVersionTask

import javax.inject.Inject

class VersionPlugin implements Plugin<Project> {

    private final Instantiator instantiator
    private final TaskExecutionGraph taskExecutionGraph;

    private Project project

    private VersionExtension extension

    @Inject
    VersionPlugin(Instantiator instantiator, TaskExecutionGraph taskExecutionGraph) {
        this.instantiator = instantiator
        this.taskExecutionGraph = taskExecutionGraph
    }

    @Override
    void apply(Project project) {
        if (!isAndroidProject(project)) {
            throw new IllegalStateException("VersionPlugin only works with Android projects but \"${project.name}\" is none")
        }

        this.project = project;

        createExtensions()
        createTasks()
    }

    private void createExtensions() {

        Splits splits = instantiator.newInstance(Splits, project.container(ExtendingVersion),
                project.container(ExtendingVersion))

        extension = project.extensions.create('androidVersion', VersionExtension, instantiator.newInstance(ConstantVersion),
                project.container(ExtendingVersion), splits)
    }

    private void createTasks() {

        project.android.applicationVariants.all { variant ->

            String taskName = "version${variant.name.capitalize()}"

            final versionTask = project.tasks.create(taskName, BuildVersionTask)
            versionTask.setVariant(variant)

            variant.getPreBuild().dependsOn versionTask

//            Workaround for instant run issues
//            https://code.google.com/p/android/issues/detail?id=227610
            taskExecutionGraph.addTaskExecutionGraphListener(new TaskExecutionGraphListener() {
                @Override
                void graphPopulated(TaskExecutionGraph taskExecutionGraph) {
                    versionTask.enabled = !taskExecutionGraph.hasTask("${project.path}:incremental${variant.name.capitalize()}Tasks")
                }
            })

        }

    }

    static isAndroidProject(Project project) {
        project.plugins.hasPlugin('com.android.application') || project.plugins.hasPlugin('com.android.library') || project.plugins.hasPlugin('com.android.test')
    }

}
