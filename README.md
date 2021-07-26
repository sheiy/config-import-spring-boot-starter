# 用法示例
```yaml
spring:
    config:
        import:
          - nacos:{protocol}://{usernmae}:{password}@{nacos_ip}:{nacos_port}/{namespace}/{config_file_name}
          - nacos:http://nacos:nacos@192.168.100.1:8848/21e60c4d-9fb5-4d46-8359-b9cd94a138e7/common.yml
```
