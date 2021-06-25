package com.example.ci.git;

import java.io.IOException;

import org.springframework.boot.context.config.ConfigData;
import org.springframework.boot.context.config.ConfigDataLoader;
import org.springframework.boot.context.config.ConfigDataLoaderContext;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.boot.env.YamlPropertySourceLoader;

public class GitConfigDataLoader implements ConfigDataLoader<GitConfigDataResource> {
    @Override
    public ConfigData load(ConfigDataLoaderContext context, GitConfigDataResource resource) throws IOException, ConfigDataResourceNotFoundException {
        YamlPropertySourceLoader yamlPropertySourceLoader = new YamlPropertySourceLoader();
        return new ConfigData(yamlPropertySourceLoader.load("git-config", resource.getResource()));
    }
}
