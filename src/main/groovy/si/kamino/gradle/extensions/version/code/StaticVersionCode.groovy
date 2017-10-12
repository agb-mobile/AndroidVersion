package si.kamino.gradle.extensions.version.code

import si.kamino.gradle.extensions.version.StaticVersion

class StaticVersionCode implements VersionCode {

    private int versionCode

    void versionCode(int versionCode) {
        this.versionCode = versionCode
    }

    @Override
    int buildVersionCode(StaticVersion version) {
        return versionCode
    }
}
