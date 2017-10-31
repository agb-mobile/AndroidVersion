package si.kamino.gradle.extensions.version

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import si.kamino.gradle.extensions.version.code.VersionCode

import javax.inject.Inject

class ExtendingVersion extends BaseVersion {

    private final ObjectFactory objectFactory;
    private final String name

    private VersionCode versionCode

    @Inject
    ExtendingVersion(String name, ObjectFactory objectFactory) {
        this.name = name
        this.objectFactory = objectFactory
    }

    String getName() {
        return name
    }

    VersionCode getVersionCode() {
        return versionCode
    }

    void versionCode(Class<? super VersionCode> aClass, Action<? extends VersionCode> versionCode) {
        this.versionCode = objectFactory.newInstance(aClass)
        versionCode.execute(this.versionCode)
    }

}
