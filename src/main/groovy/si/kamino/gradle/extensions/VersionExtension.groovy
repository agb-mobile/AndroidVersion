package si.kamino.gradle.extensions

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
/**
 * Created by blazsolar on 02/09/14.
 */
class VersionExtension {

    private final ConstantVersion appVersion

    private final NamedDomainObjectContainer<ExtendingVersion> variants

    private final Splits splits

    private boolean renameOutputs;

    VersionExtension(ConstantVersion appVersion, NamedDomainObjectContainer<ExtendingVersion> variants,
                     Splits splits) {
        this.appVersion = appVersion;
        this.variants = variants
        this.splits = splits
    }

    def appVersion(Action<ConstantVersion> appVersion) {
        appVersion.execute(this.appVersion)
    }

    def variants(Closure closure) {
        variants.configure(closure)
    }

    def splits(Action<Splits> splits) {
        splits.execute(this.splits)
    }

    def renameOutputs(boolean rename) {
        this.renameOutputs = rename;
    }

    ConstantVersion getAppVersion() {
        return appVersion
    }

    NamedDomainObjectContainer<ExtendingVersion> getVariants() {
        return variants
    }

    Splits getSplits() {
        return splits
    }

    boolean renameOutputs() {
        return renameOutputs
    }
}
