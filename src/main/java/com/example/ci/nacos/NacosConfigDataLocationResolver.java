package com.example.ci.nacos;

import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import org.springframework.boot.context.config.ConfigDataLocation;
import org.springframework.boot.context.config.ConfigDataLocationNotFoundException;
import org.springframework.boot.context.config.ConfigDataLocationResolver;
import org.springframework.boot.context.config.ConfigDataLocationResolverContext;

public class NacosConfigDataLocationResolver implements ConfigDataLocationResolver<NacosConfigDataResource> {

    private static final String PREFIX = "nacos:";
    private static final String LOGIN_PATH = "/nacos/v1/auth/users/login";
    private static final String CONFIG_PATH = "/nacos/v1/cs/configs";
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().build();
    private static final Gson GSON = new Gson();

    @Override
    public boolean isResolvable(ConfigDataLocationResolverContext context, ConfigDataLocation location) {
        return location.hasPrefix(PREFIX);
    }

    @Override
    public List<NacosConfigDataResource> resolve(ConfigDataLocationResolverContext context,
                                                 ConfigDataLocation location) {
        try {
            return resolve(location.getNonPrefixedValue(PREFIX));
        } catch (Exception ex) {
            throw new ConfigDataLocationNotFoundException(location, ex);
        }
    }

    private List<NacosConfigDataResource> resolve(String location) throws Exception {
        URL url = new URL(location);
        String userInfo = url.getUserInfo();
        if (userInfo == null || userInfo.isBlank() || userInfo.split(":").length != 2) {
            throw new RuntimeException("用户名或密码未配置，参考格式：【nacos:http://nacos:nacos@192.168.100.1:8848/21e60c4d-9fb5-4d46-8359-b9cd94a138e7/common.yml】");
        }
        String username = userInfo.split(":")[0];
        String password = userInfo.split(":")[1];
        userInfo = "username=" + username + "&" + "password=" + password;
        String urlPrefix = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort();
        URI loginUri = URI.create(urlPrefix + LOGIN_PATH);
        HttpRequest request = HttpRequest.newBuilder().uri(loginUri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(userInfo))
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() != 200) {
            throw new RuntimeException(response.body());
        }
        //noinspection unchecked
        Map<String, String> map = GSON.fromJson(response.body(), Map.class);
        if (!map.containsKey("accessToken")) {
            throw new RuntimeException("未获取到Nacos Token");
        }
        String nameSpace = url.getPath().substring(1).split("/")[0];
        String dataId = url.getPath().substring(1).split("/")[1];
        String query = "dataId=" + dataId + "&group=DEFAULT_GROUP" + "&namespaceId=" + nameSpace + "&tenant=" + nameSpace + "&show=all&accessToken=" + map.get("token") + "&username=" + username;
        request = HttpRequest.newBuilder().uri(URI.create(urlPrefix + CONFIG_PATH + "?" + query)).GET().build();

        response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() != 200) {
            throw new RuntimeException(response.body());
        }
        //noinspection unchecked
        map = GSON.fromJson(response.body(), Map.class);
        return Collections.singletonList(new NacosConfigDataResource(map.get("content")));
    }
}
