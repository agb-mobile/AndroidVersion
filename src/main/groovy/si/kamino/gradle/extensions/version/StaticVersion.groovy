package si.kamino.gradle.extensions.version

class StaticVersion extends BaseVersion implements Cloneable {

    private int versionCode

    StaticVersion(Integer major, Integer minor, Integer build) {
        this(major, minor, build, 0)
    }

    protected StaticVersion(Integer major, Integer minor, Integer build, int versionCode) {
        super(major, minor, build)
        this.versionCode = versionCode
    }

    int getVersionCode() {
        return versionCode
    }

    void versionCode(int versionCode) {
        this.versionCode = versionCode
    }

    void apply(ExtendingVersion version) {
        if (version.major != null) {
            major = version.major
        }

        if (version.minor != null) {
            minor = version.minor
        }

        if (version.build != null) {
            build = version.build
        }

    }

    String getVersionName() {
        return "$major.${minor}.${build}"
    }

    @Override StaticVersion clone() {
        return new StaticVersion(major, minor, build, versionCode)
    }

}
