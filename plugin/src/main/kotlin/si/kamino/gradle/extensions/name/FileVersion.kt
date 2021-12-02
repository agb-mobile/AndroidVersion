package si.kamino.gradle.extensions.name

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.InputFile
import javax.inject.Inject

abstract class FileVersion @Inject constructor(objects: ObjectFactory) : AbsVersionName() {

    @get:InputFile
    val file: RegularFileProperty = objects.fileProperty()

    // Note: can't use lazy {}, as it still gets evaluated in configuration time because of configuration cache / inputs change detection.
    private var properties: LinkedHashMap<String, String>? = null

    private fun readProperties(): LinkedHashMap<String, String> {
        // Make it nicer!
        return file.get().asFile.readLines()
            .map { it.split("=") }
            .map { it.first() to it.last() }
            .toMap(LinkedHashMap())
            .also {
                println("FileVersion read properties: $it")
            }
    }

    private fun requireProperties(): LinkedHashMap<String, String> {
        if (properties == null) {
            properties = readProperties()
        }
        return properties!!
    }

    override fun getKeys(): Set<String> {
        return requireProperties().keys
    }

    override fun getValue(key: String): Int {
        return (requireProperties()[key] as String).toInt()
    }

}
