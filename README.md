# Demo

### Install application

* use command mvn install -DskipTests
* * *
#### You have to build docker image from dockerfile 
* * docker build -f Dockerfile -t <name_of_image> .
* * run application : docker run -p 8080:8080 <name_of_image>
* * inspect running application : docker run -ti --entrypoint /bin/sh <name_of_image>
* * check for running containers: docker ps
* * inspect docker logs : docker logs -f container_id
* * *
* u need to skip tests because you are loading spring context and application will try to reach to kafka 

### Run everything 

* chmod +x check-config-server-started.sh 
* cd docker-compose 
* docker-compose -f common.yml -f kafka_cluster.yml -f services.yml up 
* docker-compose up