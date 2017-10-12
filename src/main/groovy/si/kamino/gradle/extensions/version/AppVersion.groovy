package si.kamino.gradle.extensions.version

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import si.kamino.gradle.extensions.version.code.SimpleVersionCode
import si.kamino.gradle.extensions.version.code.VersionCode

import javax.inject.Inject

class AppVersion extends BaseVersion {

    private VersionCode versionCode

    private final ObjectFactory objectFactory

    @Inject
    AppVersion(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory
        this.versionCode = objectFactory.newInstance(SimpleVersionCode)
    }

    VersionCode getVersionCode() {
        return versionCode
    }

    void versionCode(Class<? super VersionCode> aClass, Action<? extends VersionCode> versionCodeAction) {
        this.versionCode = objectFactory.newInstance(aClass)
        versionCode(versionCodeAction)
    }

    void versionCode(Action<? extends VersionCode> versionCode) {
        versionCode.execute(this.versionCode)
    }

}
