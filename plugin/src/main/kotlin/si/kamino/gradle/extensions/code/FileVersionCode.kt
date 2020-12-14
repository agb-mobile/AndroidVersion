package si.kamino.gradle.extensions.code

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.InputFile
import si.kamino.gradle.extensions.name.internal.StaticVersion
import javax.inject.Inject

abstract class FileVersionCode @Inject constructor(objects: ObjectFactory) : BaseVersionCode() {

    @get:InputFile
    val file: RegularFileProperty = objects.fileProperty()

    private val versionCode = lazy {
        // Make it nicer!
        file.get().asFile.readLines()
            .map { it.split("=") }
            .map { it.first() to it.last() }
            .filter { it.first == "version.code" }
            .first().second
            .toInt()
    }

    override fun getVersionCode(versionName: StaticVersion): Int {
        return versionCode.value
    }

}