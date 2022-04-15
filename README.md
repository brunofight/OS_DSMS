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

Danach lässt sich das Projekt folgendermaßen in IntelliJ importieren

1. File --> New --> Project from Existing Sources...

![Project_Setup1](https://user-images.githubusercontent.com/55789698/163538858-c0d3ad6b-ee16-4a4d-b60f-2f7430e86b70.PNG)

2. Zum Dateipfad navigieren, wo das Projekt mit Maven erstellt wurde.
3. Import project from external model --> Maven --> Finish

![Project_Setup2](https://user-images.githubusercontent.com/55789698/163538887-0dd853d4-193b-4eb9-bdc2-35b2ed2536ea.PNG)

### Test-Projekt

Normalerweise werden Streaming Jobs von einem Flink-Client auf den Flink Jobmanager hochgeladen, wo die Laufzeit-Dependencies für Flink bereits vorliegen. Deswegen wurden diese in der pom.xml als *provided* vorgegeben. Allerdings werden dadurch die Dependencies bei einem lokalen Test in der IDE so ohne weiteres auch nicht berücksichtigt. Aus diesem Grund wirft die StreamingJob-Klasse zunächst einen ``NoClassFoundError``. Zur Behebung (dieses Fehlers und einer möglicherweise auftretenden ``InaccessibleObjectException``) sind zwei zusätzliche Einstellungen in den *Run Configurations* notwendig:

- Add dependencies with "provided" scope to classpath
- Add VM Options ``--add-opens java.base/java.lang=ALL-UNNAMED``

![Project_Setup3](https://user-images.githubusercontent.com/55789698/163538902-645f34d7-9289-49ea-bfbe-3bfa360f092a.PNG)
![Project_Setup4](https://user-images.githubusercontent.com/55789698/163538905-3237ff7e-e0f5-4520-b852-58162d378023.PNG)
![Project_Setup5](https://user-images.githubusercontent.com/55789698/163538909-03b7adcf-4ea6-4842-b547-1cab05f9f505.PNG)



### Basic Project Setup

See: https://www.galiglobal.com/blog/2021/20210130-Flink-setup.html

```
mvn archetype:generate -DarchetypeGroupId=org.apache.flink -DarchetypeArtifactId=flink-quickstart-java -DarchetypeVersion=1.12.0 -DgroupId=org.apache.flink -DartifactId=flink-quickstart-java -Dversion=0.1 -Dpackage=demo.rollingSum -DinteractiveMode=false
```

Open Project with IntelliJ (New --> Project from Existing Sources --> Maven)



### Bundle for Flink Cluster in Docker

https://nightlies.apache.org/flink/flink-docs-master/docs/deployment/resource-providers/standalone/docker/#flink-with-docker-compose






