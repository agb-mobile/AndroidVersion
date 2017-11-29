package si.kamino.gradle.extensions.version

import org.gradle.api.Project

import javax.inject.Inject

class StaticAppVersion extends AppVersion {

    public Integer major
    public Integer minor
    public Integer build

    @Inject
    StaticAppVersion(Project project) {
        super(project)
    }

    void major(Integer major) {
        this.major = major
    }

    void minor(Integer minor) {
        this.minor = minor
    }

    void build(Integer build) {
        this.build = build
    }

    @Override
    Integer getMajor() {
        return major
    }

    @Override
    Integer getMinor() {
        return minor
    }

    @Override
    Integer getBuild() {
        return build
    }
}
