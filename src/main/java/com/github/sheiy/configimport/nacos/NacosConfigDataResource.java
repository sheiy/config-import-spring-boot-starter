package com.github.sheiy.configimport.nacos;

import java.nio.charset.StandardCharsets;

import org.springframework.boot.context.config.ConfigDataResource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

public class NacosConfigDataResource extends ConfigDataResource {

    private final ByteArrayResource resource;

    public NacosConfigDataResource(String content) {
        this.resource = new ByteArrayResource(content.getBytes(StandardCharsets.UTF_8));
    }

    public Resource getResource() {
        return resource;
    }
}
