package io.quarkus.workshop.superheroes.version.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.ApplicationInfoBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.workshop.superheroes.version.runtime.VersionRecorder;

// core of the extension
public class ExtensionVersionProcessor {

    private static final String FEATURE = "extension-version";

    // "FeatureBuildItem", allows you to declare the feature/extension to Quarkus and have it listed at application startup
    //  10:17:00 INFO  [io.quarkus] (Quarkus Main Thread) Installed features: [agroal, cdi, extension-version, ...]
    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    void version(ApplicationInfoBuildItem app, VersionConfig versionConfig, VersionRecorder versionRecorder) {
        if (versionConfig.enabled) {
            versionRecorder.printVersion(app.getVersion());
        }
    }
}
