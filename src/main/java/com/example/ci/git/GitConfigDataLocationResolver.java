package com.example.ci.git;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.boot.context.config.ConfigDataLocation;
import org.springframework.boot.context.config.ConfigDataLocationNotFoundException;
import org.springframework.boot.context.config.ConfigDataLocationResolver;
import org.springframework.boot.context.config.ConfigDataLocationResolverContext;

public class GitConfigDataLocationResolver implements ConfigDataLocationResolver<GitConfigDataResource> {

    private static final String PREFIX = "nacos:";

    @Override
    public boolean isResolvable(ConfigDataLocationResolverContext context, ConfigDataLocation location) {
        return location.hasPrefix(PREFIX);
    }

    @Override
    public List<GitConfigDataResource> resolve(ConfigDataLocationResolverContext context,
                                               ConfigDataLocation location) {
        try {
            return resolve(location.getNonPrefixedValue(PREFIX));
        } catch (IOException | NacosException ex) {
            throw new ConfigDataLocationNotFoundException(location, ex);
        }
    }

    private List<GitConfigDataResource> resolve(String location) throws IOException, NacosException {
        URL url = new URL(location);
        //参考nacos F12 获取配置
        //登陆接口：http://192.168.100.1:8848/nacos/v1/auth/users/login
        //获取配置接口：http://192.168.100.1:8848/nacos/v1/cs/configs?dataId=extenddata.yml&group=DEFAULT_GROUP&namespaceId=21e60c4d-9fb5-4d46-8359-b9cd94a138e7&tenant=21e60c4d-9fb5-4d46-8359-b9cd94a138e7&show=all&accessToken=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYWNvcyIsImV4cCI6MTYyNDkxNjU1MX0.cfyFVgKRaEYL6icF_JRO2J1oI-eoP3Xw_nZ5GbdFbcU
        return Collections.singletonList(new GitConfigDataResource(location, new GitConfigProperties()));
    }
}
