package si.kamino.gradle.extensions

class BaseVersion implements Cloneable {

    public Integer major
    public Integer minor
    public Integer build

    BaseVersion() {
    }

    protected BaseVersion(Integer major, Integer minor, Integer build) {
        this.major = major
        this.minor = minor
        this.build = build
    }

    void major(Integer major) {
        this.major = major;
    }

    void minor(Integer minor) {
        this.minor = minor;
    }

    void build(Integer build) {
        this.build = build;
    }

    Integer getMajor() {
        return major;
    }

    Integer getMinor() {
        return minor;
    }

    Integer getBuild() {
        return build;
    }

    BaseVersion clone() {
        return new BaseVersion(major, minor, build)
    }

}
