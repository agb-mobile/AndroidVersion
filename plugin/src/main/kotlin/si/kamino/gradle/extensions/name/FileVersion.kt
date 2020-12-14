package si.kamino.gradle.extensions.name

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.InputFile
import javax.inject.Inject

abstract class FileVersion @Inject constructor(objects: ObjectFactory) : AbsVersionName() {

    @get:InputFile
    val file: RegularFileProperty = objects.fileProperty()

    private val properties = lazy {
        // Make it nicer!
        file.get().asFile.readLines()
            .map { it.split("=") }
            .map { it.first() to it.last() }
            .toMap(LinkedHashMap())
    }

    override fun getKeys(): Set<String> {
        return properties.value.keys
    }

    override fun getValue(key: String): Int {
        return (properties.value[key] as String).toInt()
    }

}