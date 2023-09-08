# How to Run This Project?

## Locally (Development)

Configuration in docker-compose will be made later. For now, let's do everything manually, one by one.

* Before starting any service, run Docker Desktop first.


* Run existing container of Elasticsearch (used by `search-service`)
    * `docker start elasticsearch`
    * in case you don't have the container, run `docker run --name elasticsearch --net elastic -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" -e "xpack.security.enabled=false" -t docker.elastic.co/elasticsearch/elasticsearch:8.9.2`


* (If needed) run existing container of Kibana
    * `docker start kibana`
    * in case you don't have the container, run `docker run --name kibana --net elastic -p 5601:5601 docker.elastic.co/kibana/kibana:8.9.2`


* Run docker-compose of kafka in the folder `kafka-stack-docker-compose` (used by `product-service` and `search-service`)
    * `docker-compose -f zk-single-kafka-single.yml up -d`
    * to stop the containers, run `docker-compose -f zk-single-kafka-single.yml down`


* Make sure your MongoDB `cluster-product` located in cloud is running. Do this by checking through the web or MongoDB compass. (used by `product-service`)


* Make sure your PostgresSQL server is running. Do this by checking through pgAdmin. (used by `auth-service`)


* Run the services, with the following order (the order matters):
    * `service-registry` at port `8761`
    * `auth-service` at port `8100`
    * `product-service` at port `8090`
    * `search-service` at port `8110`
    * `api-gateway` at port `8080`


* Here are the URLs to access the services:
    * `http://localhost:8761/` for `service-registry` and access the Eureka dashboard
    * `http://localhost:8100/` for `auth-service`
    * `http://localhost:8090/` for `product-service`
    * `http://localhost:8110/` for `search-service`
    * `http://localhost:8080/` for `api-gateway`