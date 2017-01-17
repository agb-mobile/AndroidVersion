package si.kamino.gradle.extensions.version

import org.gradle.api.Action
import org.gradle.internal.reflect.Instantiator
import si.kamino.gradle.extensions.version.code.VersionCode

class ExtendingVersion extends BaseVersion {

    private final Instantiator instantiator;

    private final String name

    private VersionCode versionCode

    ExtendingVersion(String name, Instantiator instantiator) {
        this.name = name
        this.instantiator = instantiator
    }

    String getName() {
        return name
    }

    VersionCode getVersionCode() {
        return versionCode
    }

    void versionCode(Class<? super VersionCode> aClass, Action<? extends VersionCode> versionCode) {
        this.versionCode = instantiator.newInstance(aClass)
        versionCode.execute(this.versionCode)
    }

}
