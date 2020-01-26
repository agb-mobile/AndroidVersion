package si.kamino.gradle.extensions.version;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.model.ObjectFactory;
import si.kamino.gradle.extensions.version.code.SimpleVersionCode;
import si.kamino.gradle.extensions.version.code.VersionCode;

public abstract class AppVersion extends AbsVersion {

    private final Project project;
    private final ObjectFactory objectFactory;

    private VersionCode versionCode;

    public AppVersion(Project project) {
        this.project = project;
        this.objectFactory = project.getObjects();
        this.versionCode = objectFactory.newInstance(SimpleVersionCode.class);
    }

    public VersionCode getVersionCode() {
        return versionCode;
    }

    public <T extends VersionCode> void versionCode(Class<T> aClass, Action<T> versionCodeAction) {
        T instance = objectFactory.newInstance(aClass, project);
        versionCodeAction.execute(instance);
        this.versionCode = instance;
    }

//    public void versionCode(Action<? extends VersionCode> versionCode) {
//        versionCode.execute(this.versionCode);
//    }
}
