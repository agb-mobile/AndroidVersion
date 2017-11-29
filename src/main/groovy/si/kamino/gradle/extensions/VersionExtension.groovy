package si.kamino.gradle.extensions

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import si.kamino.gradle.extensions.version.AppVersion
import si.kamino.gradle.extensions.version.ExtendingVersion
import si.kamino.gradle.extensions.version.StaticAppVersion

import javax.inject.Inject

class VersionExtension {

    private AppVersion appVersion

    private final NamedDomainObjectContainer<ExtendingVersion> variants

    private final Splits splits

    private Project project
    private ObjectFactory objectFactory

    private String fileNamePattern

    @Inject
    VersionExtension(Project project) {
        this.project = project
        objectFactory = project.objects

        this.appVersion = objectFactory.newInstance(StaticAppVersion, project)

        def producer = { name -> objectFactory.newInstance(ExtendingVersion, name, objectFactory) }
        this.variants = project.container(ExtendingVersion, producer)

        this.splits = objectFactory.newInstance(Splits,
                project.container(ExtendingVersion, producer),
                project.container(ExtendingVersion, producer))
    }

    void appVersion(Class<? super AppVersion> aClass, Action<? extends AppVersion> versionCodeAction) {
        this.appVersion = objectFactory.newInstance(aClass, project)
        appVersion(versionCodeAction)
    }

    def appVersion(Action<AppVersion> appVersion) {
        appVersion.execute(this.appVersion)
    }

    def variants(Closure closure) {
        variants.configure(closure)
    }

    def splits(Action<Splits> splits) {
        splits.execute(this.splits)
    }

    def fileNamePattern(String pattern) {
        this.fileNamePattern = pattern
    }

    AppVersion getAppVersion() {
        return appVersion
    }

    NamedDomainObjectContainer<ExtendingVersion> getVariants() {
        return variants
    }

    Splits getSplits() {
        return splits
    }

    String getFileNamePattern() {
        return fileNamePattern
    }
}
