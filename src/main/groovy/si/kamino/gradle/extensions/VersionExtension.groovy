package si.kamino.gradle.extensions

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import si.kamino.gradle.extensions.version.AppVersion
import si.kamino.gradle.extensions.version.ExtendingVersion

import javax.inject.Inject

class VersionExtension {

    private final AppVersion appVersion

    private final NamedDomainObjectContainer<ExtendingVersion> variants

    private final Splits splits

    private String fileNamePattern

    @Inject
    VersionExtension(Project project) {
        ObjectFactory objectFactory = project.objects

        this.appVersion = objectFactory.newInstance(AppVersion, objectFactory)

        def producer = { name -> objectFactory.newInstance(ExtendingVersion, name, objectFactory) }
        this.variants = project.container(ExtendingVersion, producer)

        this.splits = objectFactory.newInstance(Splits,
                project.container(ExtendingVersion, producer),
                project.container(ExtendingVersion, producer))
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
