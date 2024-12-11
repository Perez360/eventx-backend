# Codex micro service

### How to run ?

after compiled the executable jars ends with "-fat.jar"

To execute in single mode, run the following:
> java -jar service-name-fat.jar

The service name uses `MongoDB` as its datastore

### Configuration

The service is configured by either :

1. passing a .properties
   e.g:

> java -jar -Dservice.config=/your/config/file.properties service-name-fat.jar

2. modifying a specific configuration using `-Dservice.[config][.attribute]`
   e.g:

> java -jar -Dservice.http.port=8088 service-name-fat.jar

The example above will run the service on http port 8088

Below is the full list of all attributes and their default when undefined

```properties
service.http.port=8000
service.datasource.name=test
service.datasource.url=mongodb://127.0.0.1:27017
```

### Cluster mode

For the service to communicate with others over the eventbus it requires to run in cluster mode.
It uses `Zookeeper` to run in cluster mode.

To execute in cluster mode, run the following:
> java -jar service-name-fat.jar -cluster

By default, the service will try to connect the Zookeeper host on `127.0.0.1:2181`

This can be changed either by providing the JVM argument or providing `JSON` file as configuration

> java -jar service-name-fat.jar -cluster -Dvertx.zookeeper.hosts=127.0.0.1
>
> java -jar service-name-fat.jar -cluster -Dvertx.zookeeper.config=/your/folder/zookeeper.json

Below is the full configuration sample for Zookeeper:

```json
{
  "zookeeperHosts":"127.0.0.1",
  "sessionTimeout":20000,
  "connectTimeout":3000,
  "rootPath":"io.vertx",
  "retry": {
    "policy": "exponential_backoff",
    "initialSleepTime":100,
    "intervalTimes":10000,
    "maxTimes":5
  }
}
```

_Note: the service configuration generally doesn't need change other configuration aside the host and the root path_

### Troubleshooting

When starting the service will:

1. exit immediately if it cannot connect to the database
2. Attempt to connect to zookeeper after retrying base on its retry policy (default is 5 times)

When the service is running:

1. It will re-attempt connection in case it cannot connect to zookeeper
2. It will not exit if it cannot connect to zookeeper anymore.