package si.kamino.gradle.extensions.version

import org.gradle.api.Project

import javax.inject.Inject

class FileAppVersion extends AppVersion {

    private final Project project
    private final Properties properties = new Properties()

    private boolean loaded = false

    private File sourceFile

    @Inject
    FileAppVersion(Project project) {
        super(project)
        this.project = project
    }

    void sourceFile(Object file) {
        sourceFile = project.file(file)
    }

    @Override
    Integer getMajor() {
        load()
        return properties.getProperty("versionMajor") as Integer
    }

    @Override
    Integer getMinor() {
        load()
        return properties.getProperty("versionMinor") as Integer
    }

    @Override
    Integer getBuild() {
        load()
        return properties.getProperty("versionBuild") as Integer
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
