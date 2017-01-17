package si.kamino.gradle.extensions.version.code

import si.kamino.gradle.extensions.version.StaticVersion

interface VersionCode {

    int buildVersionCode(StaticVersion version)

}
