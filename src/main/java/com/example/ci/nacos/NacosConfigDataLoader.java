package com.example.ci.nacos;

import java.io.IOException;

import org.springframework.boot.context.config.ConfigData;
import org.springframework.boot.context.config.ConfigDataLoader;
import org.springframework.boot.context.config.ConfigDataLoaderContext;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.boot.env.YamlPropertySourceLoader;

public class NacosConfigDataLoader implements ConfigDataLoader<NacosConfigDataResource> {
    @Override
    public ConfigData load(ConfigDataLoaderContext context, NacosConfigDataResource resource) throws IOException, ConfigDataResourceNotFoundException {
        YamlPropertySourceLoader yamlPropertySourceLoader = new YamlPropertySourceLoader();
        return new ConfigData(yamlPropertySourceLoader.load("nacos-config", resource.getResource()));
    }
}
