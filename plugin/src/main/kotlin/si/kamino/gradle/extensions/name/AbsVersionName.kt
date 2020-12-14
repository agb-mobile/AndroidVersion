package si.kamino.gradle.extensions.name

abstract class AbsVersionName {

    abstract fun getKeys(): Set<String>

    abstract fun getValue(key: String): Int

}