package si.kamino.gradle.extensions.version.code

import org.gradle.api.tasks.Input
import si.kamino.gradle.extensions.version.StaticVersion

class StaticVersionCode implements VersionCode {

    private int versionCode

    @Input void versionCode(int versionCode) {
        this.versionCode = versionCode
    }

    @Override
    int buildVersionCode(StaticVersion version) {
        return versionCode
    }
}
