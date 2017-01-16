package si.kamino.gradle.extensions

class ExtendingVersion extends BaseVersion {

    private final String name;

    private Closure<Integer> versionCode;

    ExtendingVersion(String name) {
        this.name = name;
    }

    protected ExtendingVersion(Integer major, Integer minor, Integer build, String name, Closure<Integer> versionCode) {
        super(major, minor, build)
        this.name = name
        this.versionCode = versionCode
    }

    Closure<Integer> getVersionCode() {
        return versionCode
    }

    void versionCode(Closure<Integer> versionCode) {
        this.versionCode = versionCode
    }

    @Override ExtendingVersion clone() {
        return new ExtendingVersion(major, minor, build, name, versionCode);
    }

}
