package si.kamino.gradle.extensions

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import si.kamino.gradle.extensions.version.AppVersion
import si.kamino.gradle.extensions.version.ExtendingVersion

/**
 * Created by blazsolar on 02/09/14.
 */
class VersionExtension {

    private final AppVersion appVersion

    private final NamedDomainObjectContainer<ExtendingVersion> variants

    private final Splits splits

    private String fileNamePattern

    VersionExtension(AppVersion appVersion, NamedDomainObjectContainer<ExtendingVersion> variants,
                     Splits splits) {
        this.appVersion = appVersion
        this.variants = variants
        this.splits = splits
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
