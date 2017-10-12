package si.kamino.gradle.extensions.version.code

import si.kamino.gradle.extensions.version.StaticVersion

class DigitsVersionCode implements VersionCode {

    private int digitsMinor = 2
    private int digitsBuild = 2

    void digitsMinor(int digits) {
        this.digitsMinor = digits
    }

    void digitsBuild(int digits) {
        this.digitsBuild = digits
    }

    @Override
    int buildVersionCode(StaticVersion version) {
        return version.major * Math.pow(10, digitsMinor + digitsBuild) +
                version.minor * Math.pow(10, digitsBuild) +
                version.build
    }
}
