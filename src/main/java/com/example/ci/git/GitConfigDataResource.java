package com.example.ci.git;

import java.net.MalformedURLException;

import org.springframework.boot.context.config.ConfigDataResource;
import org.springframework.core.io.UrlResource;

public class GitConfigDataResource extends ConfigDataResource {

    private final UrlResource resource;

    private final GitConfigProperties properties;

    public GitConfigDataResource(String url, GitConfigProperties properties) throws MalformedURLException {
        this.resource = new UrlResource(url);
        this.properties = properties;
    }

    public UrlResource getResource() {
        return resource;
    }

    public GitConfigProperties getProperties() {
        return properties;
    }
}
