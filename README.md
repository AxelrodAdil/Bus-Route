# Getting Started

Swagger URL: http://localhost:8080/swagger-ui/index.html

```shell
docker-compose -f docker-compose-local.yml up -d
```

```shell
docker stop $(docker ps -aq)
```

```shell
docker rmi -f $(docker images -q)
```

========================

```shell
redis-cli KEYS '*'
```

```shell
redis-cli flushall
```
