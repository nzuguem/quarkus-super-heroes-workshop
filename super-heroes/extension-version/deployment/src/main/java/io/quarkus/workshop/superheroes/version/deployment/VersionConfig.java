package io.quarkus.workshop.superheroes.version.deployment;


import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;

// default prefix is "quarkus"
@ConfigRoot(name = "version")
public class VersionConfig {

    /**
     * Enables or disables the version printing at startup.
     */
    // conf -> quarkus.version.enabled
    @ConfigItem(defaultValue = "true")
    public boolean enabled;
}
