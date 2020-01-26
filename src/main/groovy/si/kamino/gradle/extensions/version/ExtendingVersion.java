package si.kamino.gradle.extensions.version;

import org.gradle.api.Action;
import org.gradle.api.model.ObjectFactory;
import si.kamino.gradle.extensions.version.code.VersionCode;

import javax.inject.Inject;

public class ExtendingVersion extends BaseVersion {

    private final ObjectFactory objectFactory;
    private final String name;

    private VersionCode versionCode;

    @Inject
    public ExtendingVersion(String name, ObjectFactory objectFactory) {
        this.name = name;
        this.objectFactory = objectFactory;
    }

    public String getName() {
        return name;
    }

    public VersionCode getVersionCode() {
        return versionCode;
    }

    public <T extends VersionCode> void versionCode(Class<T> aClass, Action<T> versionCode) {
        T instance = objectFactory.newInstance(aClass);
        versionCode.execute(instance);
        this.versionCode = instance;
    }
}
