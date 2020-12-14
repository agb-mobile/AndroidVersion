package si.kamino.gradle.extensions

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.tasks.Nested
import si.kamino.gradle.extensions.code.BaseVersionCode
import si.kamino.gradle.extensions.code.StaticVersionCode
import si.kamino.gradle.extensions.name.AbsVersionName
import si.kamino.gradle.extensions.name.BaseVersionName
import si.kamino.gradle.extensions.name.ExtendingVersion
import java.io.Serializable
import javax.inject.Inject

abstract class VersionExtension @Inject constructor(private val project: Project) : Serializable {

    @get:Nested
    var versionName: AbsVersionName = project.objects.newInstance(BaseVersionName::class.java)

    @get:Nested
    var versionCode: BaseVersionCode = project.objects.newInstance(StaticVersionCode::class.java)

    @get:Nested
    val variants: NamedDomainObjectContainer<ExtendingVersion> = project.container(ExtendingVersion::class.java)

    fun versionName(action: Action<BaseVersionName>) {
        val instance = versionName
        if (instance is BaseVersionName) {
            action.execute(instance)
        } else {
            error("Incorrect version name type")
        }
    }

    fun <T : AbsVersionName> versionName(clazz: Class<T>, action: Action<T>) {
        val instance = project.objects.newInstance(clazz)
        action.execute(instance)
        versionName = instance
    }

    fun versionCode(action: Action<StaticVersionCode>) {
        val instance = versionCode
        if (instance is StaticVersionCode) {
            action.execute(instance)
        } else {
            error("Incorrect version code type")
        }
    }

    fun <T : BaseVersionCode> versionCode(clazz: Class<T>, action: Action<T>) {
        val instance = project.objects.newInstance(clazz)
        action.execute(instance)
        versionCode = instance
    }


}
