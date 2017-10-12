package si.kamino.gradle.extensions.version.code

import si.kamino.gradle.extensions.version.StaticVersion

class IncreaseVersionCode implements VersionCode {

    private int by

    void add(int add) {
        this.by = add
    }

    @Override
    int buildVersionCode(StaticVersion version) {
        return version.versionCode + by
    }
}
