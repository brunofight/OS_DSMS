# Oberseminar - Grundbegriffe des Data Stream Management

Begleitend zum 2. Vortrag im Oberseminar *Data Stream Management Systeme* (02.05.2022) werden zwei Demonstrationen mit *Apache Flink* präsentiert. Es folgt eine Anleitung zur Replikation und Nachvollziehen dieses grundlegenden Projektes. Dabei liegt der Fokus insbesondere auf:

- Einrichtung einer lokalen Entwicklungsumgebung für *Apache Flink*
- Erstellung eines lauffähigen Flink-Projektes in Java (StreamJob)
- Aufbau einer einfachen *Apache Kafka* Umgebung in *Docker* 
- Beispiel einer *Stateful Stream Processing* Anwendung für die Verarbeitung von Syslogs (Demo 1)
- Demonstration von Basiskonzepten des *Data Streamings* wie Windowing, Event/Processing Time, Watermarks und Aggregation

## Apache Flink Entwicklungsumgebung

Die Anleitung basiert auf einem Windows-System (10/11). Für Linux-Derivate oder Mac können die Anforderungen leicht abweichen.

### Software-Voraussetzung

- Maven
  - [Download](https://maven.apache.org/download.cgi)
  - [Installationsanleitung](https://maven.apache.org/install.html) - PATH-Variable anpassen
  - Test: ``mvn -v``
- JDK 18 (mindestens JDK 11) 
- IntelliJ IDEA
  - [Kostenlose Studentenlizenz](https://www.jetbrains.com/de-de/community/education/#students)
- Docker
  - [WSL-Aktivierung für Windows](https://docs.microsoft.com/de-de/windows/wsl/install)
- Docker Desktop (nicht zwingend notwendig)
  - [Installationsanleitung](https://docs.docker.com/desktop/windows/install/)

### Weiterführende Links und Quellen

Auch wenn das Apache Flink Dashboard für die einfachen Beispiele nicht notwendig ist, sind hier informativ noch einige hilfreiche Links dazu angegeben:

- [Flink-Cluster mit Docker](https://nightlies.apache.org/flink/flink-docs-master/docs/deployment/resource-providers/standalone/docker/)
- [Flink Setup for Development](https://www.galiglobal.com/blog/2021/20210130-Flink-setup.html)

Im Buch *Stream Processing with Apache Flink* - Kapitel 4 (S. 71 ff) ist ebenfalls ein Leitfaden für den Aufbau einer Entwicklungsumgebung gegeben (zzgl. einiger Beispiel-Projekte).

## Flink Quickstart-Projekt

Als Einstiegspunkt für die Entwicklung einer neuen Flink-Anwendung eignet sich der Maven-Archetype Flink-Quickstart-Java:

```
mvn archetype:generate -DarchetypeGroupId=org.apache.flink -DarchetypeArtifactId=flink-quickstart-java -DarchetypeVersion=1.12.0 -DgroupId=org.apache.flink -DartifactId=<Artifact> -Dversion=0.1 -Dpackage=<Package> -DinteractiveMode=false
```

![Schritt 1](Images/Setup_Process1.PNG)

### Basic Project Setup

See: https://www.galiglobal.com/blog/2021/20210130-Flink-setup.html

```
mvn archetype:generate -DarchetypeGroupId=org.apache.flink -DarchetypeArtifactId=flink-quickstart-java -DarchetypeVersion=1.12.0 -DgroupId=org.apache.flink -DartifactId=flink-quickstart-java -Dversion=0.1 -Dpackage=demo.rollingSum -DinteractiveMode=false
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






