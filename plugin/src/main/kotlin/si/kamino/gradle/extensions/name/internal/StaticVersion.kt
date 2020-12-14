package si.kamino.gradle.extensions.name.internal

import si.kamino.gradle.extensions.name.BaseVersionName

class StaticVersion(version: MutableMap<String, Int> = LinkedHashMap()) : BaseVersionName(version) {

    private val suffix = StringBuilder()

    fun appendSuffix(suffix: String) {
        this.suffix.append(suffix)
    }

    fun versionName(): String {
        return version.values.joinToString(".")
            .plus(suffix)
    }

}