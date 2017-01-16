package si.kamino.gradle.extensions

class ConstantVersion extends BaseVersion {

    public int versionCode;

    ConstantVersion() {
        super(0, 0, 0)
    }

    protected ConstantVersion(Integer major, Integer minor, Integer build, int versionCode) {
        super(major, minor, build)
        this.versionCode = versionCode
    }

    int getVersionCode() {
        return versionCode
    }

    void versionCode(int versionCode) {
        this.versionCode = versionCode
    }

    @Override ConstantVersion clone() {
        return new ConstantVersion(major, minor, build, versionCode)
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

        def code = version.versionCode
        if (code) {
            versionCode = code.call(versionCode)
        }

    }

    String getVersionName() {
        return "$major.$minor.$build"
    }

}
