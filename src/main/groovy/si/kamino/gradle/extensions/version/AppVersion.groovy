package si.kamino.gradle.extensions.version

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import si.kamino.gradle.extensions.version.code.SimpleVersionCode
import si.kamino.gradle.extensions.version.code.VersionCode

abstract class AppVersion extends AbsVersion {

    private final Project project;
    private final ObjectFactory objectFactory

    private VersionCode versionCode

    AppVersion(Project project) {
        this.project = project
        this.objectFactory = project.objects
        this.versionCode = objectFactory.newInstance(SimpleVersionCode)
    }

    VersionCode getVersionCode() {
        return versionCode
    }

    void versionCode(Class<? super VersionCode> aClass, Action<? extends VersionCode> versionCodeAction) {
        this.versionCode = objectFactory.newInstance(aClass, project)
        versionCode(versionCodeAction)
    }

    void versionCode(Action<? extends VersionCode> versionCode) {
        versionCode.execute(this.versionCode)
    }

}
