package si.kamino.gradle.extensions.code

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.InputFile
import si.kamino.gradle.extensions.name.internal.StaticVersion
import javax.inject.Inject

abstract class FileVersionCode @Inject constructor(objects: ObjectFactory) : BaseVersionCode() {

    @get:InputFile
    val file: RegularFileProperty = objects.fileProperty()

    // Note: can't use lazy {}, as it still gets evaluated in configuration time because of configuration cache / inputs change detection.
    private var versionCode: Int? = null

    private fun readVersionCode(): Int {
        // Make it nicer!
        return file.get().asFile.readLines()
            .map { it.split("=") }
            .map { it.first() to it.last() }
            .filter { it.first == "version.code" }
            .first().second
            .toInt()
    }

    override fun getVersionCode(versionName: StaticVersion): Int {
        if (versionCode == null) {
            versionCode = readVersionCode()
        }
        return versionCode!!
    }

}
