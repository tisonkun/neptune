# Neptune

A dashboard to show GitHub events insight.

## Usage

1. Star backend:

```shell
cd neptune-backend
./gradlew bootRun
```

2. Query stargazers cross:

```shell
curl -H "Content-Type: application/json" -X GET http://127.0.0.1:8080/api/stargazers_cross -d '{"origins": ["apache/pulsar", "apache/kafka"]}' | jq
```
