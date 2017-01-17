package si.kamino.gradle.extensions.version.code

import org.gradle.api.tasks.Input
import si.kamino.gradle.extensions.version.StaticVersion

class AddVersionCode implements VersionCode {

    private int add

    @Input
    add(int add) {
        this.add = add
    }

    @Override
    int buildVersionCode(StaticVersion version) {
        return version.versionCode + add
    }
}
