                                                  Cloner Projet :

git clone https://github.com/bougha1/garage-service.git

cd garage-service

---------------------------------------------------------------------------------------------------------------------------------------------
                                                  Build Projet :

mvn clean install

mvn test

---------------------------------------------------------------------------------------------------------------------------------------------
                                             Démarrage de l'application

mvn spring-boot:run

ou lancer la classe GarageServiceApplication.java

---------------------------------------------------------------------------------------------------------------------------------------------
                                                      API :

- Swagger UI : http://localhost:8080/swagger-ui/index.html

- OpenAPI JSON : http://localhost:8080/v3/api-docs

---------------------------------------------------------------------------------------------------------------------------------------------
                                Commandes Kafka & Zookeeper (Windows PowerShell)

Dans le repertoire : C:\kafka_2.13-3.6.0\ lance les commandes suivant :

- Zookeeper : .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties

- Kafka : 
              $env:KAFKA_HEAP_OPTS = "-Xms512m -Xmx512m"
              $env:KAFKA_JVM_PERFORMANCE_OPTS = "-server -XX:+UseG1GC -XX:MaxGCPauseMillis=20 -XX:InitiatingHeapOccupancyPercent=35 -XX:           +ExplicitGCInvokesConcurrent -Djava.awt.headless=true"

              .\bin\windows\kafka-server-start.bat .\config\server.propertie


     - Créer un topic: .\bin\windows\kafka-topics.bat --create --topic vehicle-created-topic --bootstrap-server localhost:9092
     - Lister les topics: .\bin\windows\kafka-console-consumer.bat --topic vehicle-created-topic --bootstrap-server localhost:9092 

---------------------------------------------------------------------------------------------------------------------------------------------


