package si.kamino.gradle.extensions;

import groovy.lang.Closure;
import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.NamedDomainObjectFactory;
import org.gradle.api.Project;
import si.kamino.gradle.extensions.version.AppVersion;
import si.kamino.gradle.extensions.version.ExtendingVersion;
import si.kamino.gradle.extensions.version.StaticAppVersion;

import javax.inject.Inject;

public class VersionExtension {

    private AppVersion appVersion;

    private final NamedDomainObjectContainer<ExtendingVersion> variants;

    private final Splits splits;

    private Project project;

    private String fileNamePattern;

    @Inject
    public VersionExtension(final Project project) {
        this.project = project;

        this.appVersion = project.getObjects().newInstance(StaticAppVersion.class, project);
        NamedDomainObjectFactory<ExtendingVersion> producer = new NamedDomainObjectFactory<ExtendingVersion>() {
            @Override
            public ExtendingVersion create(String name) {
                return project.getObjects().newInstance(ExtendingVersion.class, name, project.getObjects());
            }
        };
        this.variants = project.container(ExtendingVersion.class, producer);

        this.splits = project.getObjects().newInstance(Splits.class,
                project.container(ExtendingVersion.class, producer),
                project.container(ExtendingVersion.class, producer));
    }

    public <T extends AppVersion> void  appVersion(Class<T> aClass, Action<T> provider) {
        T instance = project.getObjects().newInstance(aClass, project);
        provider.execute(instance);
        appVersion = instance;
    }

    public void variants(Closure closure) {
        variants.configure(closure);
    }

    public void splits(Action<Splits> splits) {
        splits.execute(this.splits);
    }

    public void fileNamePattern(String pattern) {
        this.fileNamePattern = pattern;
    }

    public AppVersion getAppVersion() {
        return appVersion;
    }

    public NamedDomainObjectContainer<ExtendingVersion> getVariants() {
        return variants;
    }

    public Splits getSplits() {
        return splits;
    }

    public String getFileNamePattern() {
        return fileNamePattern;
    }

}
