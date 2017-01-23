package si.kamino.gradle.extensions.version.code

import org.gradle.api.tasks.Input
import si.kamino.gradle.extensions.version.StaticVersion

class IncreaseVersionCode implements VersionCode {

    private int by

    @Input
    add(int add) {
        this.by = add
    }

    @Override
    int buildVersionCode(StaticVersion version) {
        return version.versionCode + by
    }
}
