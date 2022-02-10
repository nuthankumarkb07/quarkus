# quarkus
Quarkus installed.
JDK 11
MVN 3.8.4
docker network create client_server_network


## How to run TCP server on 9999
cd trireme-connector

./mvnw compile quarkus:dev

#### PREREQ

docker network create client_server_network

## build docker 

cd trireme-connector

./mvnw package

docker build -f src/main/docker/Dockerfile.jvm -t quarkus/trireme-connector-jvm .

docker run -d --name connector --network-alias connector --network client_server_network -it quarkus/trireme-connector-jvm

## Simulator

cd simulator/tcpsimulator

./mvnw package

docker build -f src/main/docker/Dockerfile.jvm -t quarkus/tcpsimulator-jvm .

docker run -d --name simulator --network-alias simulator --network client_server_network -it quarkus/tcpsimulator-jvm 




