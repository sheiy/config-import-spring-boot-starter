package com.example.ci.git;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.BootstrapContext;
import org.springframework.boot.context.config.ConfigDataLocation;
import org.springframework.boot.context.config.ConfigDataLocationBindHandler;
import org.springframework.boot.context.config.ConfigDataLocationNotFoundException;
import org.springframework.boot.context.config.ConfigDataLocationResolver;
import org.springframework.boot.context.config.ConfigDataLocationResolverContext;
import org.springframework.boot.context.config.ConfigDataProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.ObjectUtils;

@EnableConfigurationProperties({GitConfigProperties.class})
public class GitConfigDataLocationResolver implements ConfigDataLocationResolver<GitConfigDataResource> {

    private static final String PREFIX = "git:";
    private final GitConfigProperties properties;

    public GitConfigDataLocationResolver( Binder binder) {
        this.properties = new GitConfigProperties();
        ConfigDataProperties.LegacyProfilesBindHandler legacyProfilesBindHandler = new ConfigDataProperties.LegacyProfilesBindHandler();
        String[] legacyProfiles = binder.bind(LEGACY_PROFILES_NAME, BINDABLE_STRING_ARRAY, legacyProfilesBindHandler)
                .orElse(null);
        ConfigDataProperties properties = binder.bind(NAME, BINDABLE_PROPERTIES, new ConfigDataLocationBindHandler())
                .orElse(null);
        if (!ObjectUtils.isEmpty(legacyProfiles)) {
            properties = (properties != null)
                    ? properties.withLegacyProfiles(legacyProfiles, legacyProfilesBindHandler.getProperty())
                    : new ConfigDataProperties(null, new ConfigDataProperties.Activate(null, legacyProfiles));
        }
        return properties;
    }

    @Override
    public boolean isResolvable(ConfigDataLocationResolverContext context, ConfigDataLocation location) {
        return location.hasPrefix(PREFIX);
    }

    @Override
    public List<GitConfigDataResource> resolve(ConfigDataLocationResolverContext context,
                                               ConfigDataLocation location) {
        try {
            return resolve(location.getNonPrefixedValue(PREFIX));
        } catch (IOException ex) {
            throw new ConfigDataLocationNotFoundException(location, ex);
        }
    }

    private List<GitConfigDataResource> resolve(String location) throws IOException {
        return Collections.singletonList(new GitConfigDataResource(location, properties));
    }
}
