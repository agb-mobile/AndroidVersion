package si.kamino.gradle.extensions.version.code

import org.gradle.api.Project
import si.kamino.gradle.extensions.version.StaticVersion

import javax.inject.Inject

class FileVersionCode implements VersionCode {

    private final Project project
    private final Properties properties = new Properties()

    private boolean loaded = false

    private File sourceFile

    @Inject
    FileVersionCode(Project project) {
        this.project = project
    }

    void sourceFile(Object file) {
        sourceFile = project.file(file)
    }

    @Override
    int buildVersionCode(StaticVersion version) {
        load()
        return properties.getProperty("versionCode") as Integer
    }

    private void load() {
        if (!loaded) {
            synchronized (this) {
                if (!loaded) {
                    InputStream is = new FileInputStream(sourceFile)
                    properties.load(is)
                    loaded = true
                }
            }
        }
    }
}
