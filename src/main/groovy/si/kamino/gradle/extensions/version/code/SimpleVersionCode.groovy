package si.kamino.gradle.extensions.version.code

import org.gradle.api.tasks.Input
import si.kamino.gradle.extensions.version.StaticVersion

class SimpleVersionCode implements VersionCode {

    private int digits = 2

    @Input void digits(int digits) {
        this.digits = digits
    }

    @Override
    int buildVersionCode(StaticVersion version) {
        return version.major * Math.pow(10, digits * 2) + version.minor * Math.pow(10, digits) + version.build
    }
}
