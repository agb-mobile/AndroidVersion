package si.kamino.gradle.extensions.name

import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

abstract class SemanticFileVersion @Inject constructor(objects: ObjectFactory) : FileVersion(objects) {

    override fun getKeys(): Set<String> {
        return setOf("version.major", "version.minor", "version.patch")
    }

}