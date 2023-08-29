package io.quarkus.workshop.superheroes.version.runtime;

import io.quarkus.runtime.annotations.Recorder;
import org.slf4j.LoggerFactory;

// registration of the class to be executed in the Runtime
@Recorder
public class VersionRecorder {
    public void printVersion(String version) {
        LoggerFactory.getLogger(VersionRecorder.class).info("Version: {}", version);
    }
}
