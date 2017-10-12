package si.kamino.gradle.extensions

import org.gradle.api.NamedDomainObjectContainer
import si.kamino.gradle.extensions.version.ExtendingVersion

import javax.inject.Inject

class Splits {

    private NamedDomainObjectContainer<ExtendingVersion> density
    private NamedDomainObjectContainer<ExtendingVersion> abi

    @Inject
    Splits(NamedDomainObjectContainer<ExtendingVersion> density, NamedDomainObjectContainer<ExtendingVersion> abi) {
        this.density = density
        this.abi = abi
    }

    NamedDomainObjectContainer<ExtendingVersion> getDensity() {
        return density
    }

    NamedDomainObjectContainer<ExtendingVersion> getAbi() {
        return abi
    }

    def density(Closure closure) {
        density.configure(closure)
    }

    def abi(Closure closure) {
        abi.configure(closure)
    }

}
