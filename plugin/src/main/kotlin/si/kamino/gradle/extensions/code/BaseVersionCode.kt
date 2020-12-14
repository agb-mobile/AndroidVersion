package si.kamino.gradle.extensions.code

import si.kamino.gradle.extensions.name.internal.StaticVersion

abstract class BaseVersionCode {

    abstract fun getVersionCode(versionName: StaticVersion): Int

}