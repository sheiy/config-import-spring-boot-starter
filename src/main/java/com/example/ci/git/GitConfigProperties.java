package com.example.ci.git;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = GitConfigProperties.PREFIX)
public class GitConfigProperties {

    public static final String PREFIX = "git.config";

    private String username;

    private String password;

    private String branch;

    private String path;

    public GitConfigProperties() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
