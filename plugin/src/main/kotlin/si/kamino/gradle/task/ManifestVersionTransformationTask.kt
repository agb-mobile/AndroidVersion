package si.kamino.gradle.task

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.w3c.dom.Element
import java.io.FileWriter
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

abstract class ManifestVersionTransformationTask : DefaultTask() {

    @get:InputFile
    abstract val versionFile: RegularFileProperty

    @get:InputFile
    abstract val mergedManifestFile: RegularFileProperty

    @get:OutputFile
    abstract val updatedManifestFile: RegularFileProperty

    @TaskAction
    fun taskAction() {
        val lines = versionFile.get().asFile.readLines()
        val versionName = lines.first()
        val versionCode = lines.last()

        val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        val builder: DocumentBuilder = factory.newDocumentBuilder()
        val parse = builder.parse(mergedManifestFile.get().asFile)

        parse.getElementsByTagName("manifest")
            .let {
                assert(it.length == 1)

                val node = it.item(0) as Element

                node.setAttribute("android:versionCode", versionCode)
                node.setAttribute("android:versionName", versionName)
            }

        val writer = FileWriter(updatedManifestFile.get().asFile)
        val result = StreamResult(writer)
        val tf = TransformerFactory.newInstance()
        val transformer = tf.newTransformer()
        transformer.transform(DOMSource(parse), result)
    }

}