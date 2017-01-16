package si.kamino.gradle.extensions

import org.gradle.api.NamedDomainObjectContainer

class Splits {

    private NamedDomainObjectContainer<ExtendingVersion> density
    private NamedDomainObjectContainer<ExtendingVersion> abi

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
