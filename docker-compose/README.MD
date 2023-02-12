### RUN COMPOSE FILE
* docker-compose -f FILE_NAME up
* docker ps - to check if containers are running

### Zookeeper

*  hold metadata for cluster and play role on determining leader election

### Schema registry

*  is used to register a schema for a kafka topic, and
*  it will check the producers and consumer each time to force them to use register schema,
*  schema can evolve without breaking changes

### Broker
*  broker stores data on server disk they run on
*  3 containers - concept of Quorum , minimum number of brokers to create a network

###  Kafkacat

https://docs.confluent.io/platform/current/app-development/kafkacat-usage.html

#### Run kcat locally in docker
* docker run -it --network=host confluentinc/cp-kafkacat kafkacat -L -b localhost:19092