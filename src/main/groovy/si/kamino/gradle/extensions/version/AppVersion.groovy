package si.kamino.gradle.extensions.version

import org.gradle.api.Action
import org.gradle.internal.reflect.Instantiator
import si.kamino.gradle.extensions.version.code.SimpleVersionCode
import si.kamino.gradle.extensions.version.code.VersionCode

class AppVersion extends BaseVersion {

    private VersionCode versionCode

    private final Instantiator instantiator;

    AppVersion(Instantiator instantiator) {
        this.instantiator = instantiator
        this.versionCode = instantiator.newInstance(SimpleVersionCode)
    }

    VersionCode getVersionCode() {
        return versionCode
    }

    void versionCode(Class<? super VersionCode> aClass, Action<? extends VersionCode> versionCode) {
        this.versionCode = instantiator.newInstance(aClass)
        versionCode(versionCode)
    }

    void versionCode(Action<? extends VersionCode> versionCode) {
        versionCode.execute(this.versionCode)
    }

}
