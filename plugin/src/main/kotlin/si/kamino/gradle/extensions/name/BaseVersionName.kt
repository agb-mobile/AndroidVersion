package si.kamino.gradle.extensions.name

import org.gradle.api.tasks.Input
import javax.inject.Inject

abstract class BaseVersionName(@get:Input val version: MutableMap<String, Int>) : AbsVersionName() {

    @Inject constructor(): this(LinkedHashMap())

    override fun getKeys() = version.keys

    override fun getValue(key: String) = version[key]!!

}