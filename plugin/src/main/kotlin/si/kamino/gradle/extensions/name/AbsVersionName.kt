package si.kamino.gradle.extensions.name

import org.gradle.api.tasks.Input

abstract class AbsVersionName {

    @Input
    abstract fun getKeys(): Set<String>

    @Input
    abstract fun getValue(key: String): Int

}