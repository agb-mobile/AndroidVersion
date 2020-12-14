package si.kamino.gradle.extensions.code

import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import si.kamino.gradle.extensions.name.internal.StaticVersion

abstract class StaticVersionCode : BaseVersionCode() {

    @get:Input
    abstract val versionCode: Property<Int>

    override fun getVersionCode(versionName: StaticVersion): Int {
        return versionCode.get()
    }
}