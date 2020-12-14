package si.kamino.gradle.extensions.name

import org.gradle.api.tasks.Input

abstract class ExtendingVersion(@get:Input val name: String): BaseVersionName() {

    @get:Input
    abstract var suffix: String?

}