# Bus Route

Swagger URL: http://localhost:8080/swagger-ui/index.html

```
git clone https://github.com/AxelrodAdil/Bus-Route.git
```

```shell
docker-compose -f docker-compose-local.yml up -d
```
<img alt="postMethodFalse" src="/imgs/Screenshot 2023-02-06 at 16.39.38.png" height="400" />
<img alt="postMethodTrue" src="/imgs/Screenshot 2023-02-06 at 16.40.12.png" height="200" />


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
