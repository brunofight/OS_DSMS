# OS_DSMS

## Setup for Apache Flink

### Software

- IntelliJ IDEA
- Maven
- JDK 18

### Basic Project Setup

See: https://www.galiglobal.com/blog/2021/20210130-Flink-setup.html

```
mvn archetype:generate -DarchetypeGroupId=org.apache.flink -DarchetypeArtifactId=flink-quickstart-java -DarchetypeVersion=1.12.0 -DgroupId=<group> -DartifactId=<artifact> -Dversion=0.1 -Dpackage=galiglobal.flink -DinteractiveMode=false
```

Open Project with IntelliJ (New --> Project from Existing Sources --> Maven)

Edit run configurations --> Modify Options: checkmark *Add VM Options*, *Add "Dependencies" with provided scope to class*

VM Options
```
--add-opens java.base/java.lang=ALL-UNNAMED
```

... this avoids InaccessibleObjectException

### Bundle for Flink Cluster in Docker

https://nightlies.apache.org/flink/flink-docs-master/docs/deployment/resource-providers/standalone/docker/#flink-with-docker-compose






