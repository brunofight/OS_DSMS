# Oberseminar - Grundbegriffe des Data Stream Management

Begleitend zum 2. Vortrag im Oberseminar *Data Stream Management Systeme* (02.05.2022) werden zwei Demonstrationen mit *Apache Flink* präsentiert. Es folgt eine Anleitung zur Replikation und Nachvollziehen dieses grundlegenden Projektes. Dabei liegt der Fokus insbesondere auf:

- Einrichtung einer lokalen Entwicklungsumgebung für *Apache Flink*
- Erstellung eines lauffähigen Flink-Projektes in Java (StreamJob)
- Aufbau einer einfachen *Apache Kafka* Umgebung in *Docker* 
- Beispiel einer *Stateful Stream Processing* Anwendung für die Verarbeitung von Syslogs (Demo 1)
- Demonstration von Basiskonzepten des *Data Streamings* wie Windowing, Event/Processing Time, Watermarks und Aggregation

## Nutzung

```console
git clone https://github.com/brunofight/OS_DSMS.git
```

- [Entwicklungsumgebung](#apache-flink-entwicklungsumgebung) s. unten aufsetzen
- *Run Configurations* analog zum [Test-Projekt](#test-projekt) anpassen


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
- Docker (nur für Demo 1)
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

```console
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

Für ein schnelles Test-Projekt reicht es lokal ein paar Daten als Stream bereitzustellen. Dazu wird die main-Methode im StreamingJob entsprechend angepasst:

```java
public static void main(String[] args) throws Exception {
		
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		DataStream<Integer> stream = env.fromElements(1, 5, 8, 10, 4, 28, 9)
				.filter(i -> i > 8);
		stream.print();

		env.execute("Test");
	}
```

In der Ausgabe erscheinen nun alle Zahlen > 8.

### Logging

Im Hintergrund wird Log4J zum Logging verwendet. Das kann sofern keine Fehler auftreten bei der Demonstration etwas störend sein. Um das Logging auszuschalten, muss in der Datei ``./src/main/resources/log4j2.properties`` die Konfiguration ``rootLogger.level = OFF`` vorgenommen werden.

## Demo 1 - Syslog-Verarbeitung mit Apache Kafka und Flink

Im Sinne des Aufbaus eines kleinen SIEM (Security Information and Event Management) wird gezeigt, wie eine zentrale, stream-basierte Log-Verarbeitung realisiert werden kann. Nachgelagert können hierauf aufbauend Stream-Transformationen zur Aufbereitung der Logs implementiert werden.

### Aufbau

1. Docker Netzwerk bestehend aus:
  - Kafka Zookeeper
  - Kafka Broker (localhost:9092 tcp)
  - Syslog Producer und Topic (localhost:5140 tcp)
2. Apache Flink Streaming-Applikation (lokal in IntelliJ)
3. Test-Client (z.B. Ubuntu for Windows mit WSL)

Die Komponenten müssen in genau dieser Reihenfolge gestartet werden.
Für das Docker-Netzwerk wurde eine docker-compose-Datei bereitgestellt. 

Images:

- [Syslog Producer for Apache Kafka](https://github.com/elodina/syslog-kafka)
- [Apache Kafka Docker](https://hub.docker.com/r/bitnami/kafka/)

```
docker-compose up
```

### Anpassung Hosts-Datei

Außerhalb des Docker-Netzwerks ist der FQDN *kafka-server* nicht bekannt. Als Workaround für die Testumgebung kann man der Host-Datei ``C:\windows\System32\drivers\etc`` einen DNS-Eintrag hinzufügen:

```console
127.0.0.1 kafka-server
```

(Hinweis: Notepad als Administrator ausführen)

### RSyslog-Konfiguration

(s. https://www.thegeekdiary.com/how-to-log-every-shell-command-in-linux/)

```console
sudo echo '*.* @@localhost:5140' >> /etc/rsyslog.conf
sudo service rsyslog restart
```

Um das Logging von allen Nutzer-Befehlen zu aktivieren:

```console
sudo vi /etc/bash.bashrc
# folgende Zeile hinzufügen:
 export PROMPT_COMMAND='RETRN_VAL=$?;logger -p local6.debug "$(whoami) [$$]: $(history 1 | sed "s/^[ ]*[0-9]\+[ ]*//" ) [$RETRN_VAL]"'

sudo echo 'local6.* /var/log/secure' >> /etc/rsyslog.d/bash.conf
sudo service rsyslog restart

# Beim Start der nächsten Nutzer-Session ist das Logging von User-Befehlen aktiv
```

### Mögliche Fehlerquellen

- gelegentlich startet der Kafka-Broker zu schnell (bevor der Kafka-Zookeeper initialisiert wurde)
  - Lösung: Netzwerk neustarten oder alle drei Container manuell in Reihenfolge per Kommandozeile starten
- falls wieder InaccessibleObjectExceptions auftreten sollten, muss ein neuer JVM-Parameter hinzugefügt werden:
  - ``--add-opens java.base/java.util=ALL-UNNAMED``

## Demo 2 - Event und Processing Time basiertes Windowing

Dieses Beispiel soll den Unterschied zwischen *Event Time* und *Processing Time* verdeutlichen. Besonders stark wird das bei der Verarbeitung von out-of-order Events mit hohem *Event Time Skew* deutlich. Dazu werden in einer lokalen Datenquelle Event-Zeiten ohne Reihenfolge simuliert (s. [SiemSource](https://github.com/brunofight/OS_DSMS/blob/main/src/main/java/demo/windowing/SiemSource.java)):

```java
long[][] outOfOrderEventTimes = { {534, 2}, {332, 2}, {965, 2}, {1345, 2}, {1234, 2}, {876, 2}, {954, 2},
            {1111, 2}, {1876, 2}, {667, 2}, {3223, 2}, {3110, 2}, {3230, 2}, {78, 2}};
```

Das zweidimensionale Array stellt eine Menge von Tupeln aus Event-Zeiten und einem Wert für eine spätere Summenberechnung (der Einfachheit halber überall 2).

In der *run*-Methode wird die Übertragungsverzögerung der Events vorgetäuscht. Die Prozesszeit für das erste Event liegt bei 3 Sekunden. Alle folgenden Events haben konstant eine weitere Prozesszeit-Verzögerung von 100 Millisekunden. Mit ``ctx.collectWithTimeStamp(SiemEvent event, long eventTime)`` wird das zuvor erzeugte Event an den StreamJob weitergegeben. 

```java
public void run(SourceContext<SiemEvent> ctx) throws Exception {

    startTime = System.currentTimeMillis();
    Thread.sleep(3000);

    while (isRunning) {
        Tuple2<SiemEvent,Long> nextEvent = getNextEvent();
        ctx.collectWithTimestamp(nextEvent.f0, startTime + nextEvent.f1);
        Thread.sleep(100);
    }

    Thread.sleep(2000);
}
```

Dabei berechnet sich die Event-Zeit aus dem Startzeitpunkt der StreamSource plus der vorgegebenen Event-Zeit. So ergeben sich für die Elemente des Arrays folgende relative Event- und Prozesszeiten:

> {534, 3000} ; {332, 3100} ; {965, 3200} ; {1345, 3300} usw.

Für den Vergleich wird diese Datenquelle auf zwei StreamJobs simuliert. In beiden Beispielen werden Fixed Windows (bzw. Tumbling Windows) mit einer Zeit von 1 Sekunde verwendet. In den jeweiligen Windows wird die Summe der darin vorliegenden Werte und eine Liste der zugeordneten Event-Zeiten aggregiert.

Der Startzeitpunkt des ersten Windows kann jenachdem wann dem Thread genau wieder Rechenzeit gegeben wird leicht abweichen. Dadurch ist die Zuordnung versuchsweise immer leicht zueinander verschoben. Für das Nachvollziehen der generellen Wirkungsweise von Prozess- und Event-Zeit ist das jedoch irrelevant.

### Processing Time Windows

Die Klasse [ProcessingTimeWindowing](https://github.com/brunofight/OS_DSMS/blob/main/src/main/java/demo/windowing/ProcessingTimeWindowing.java) verarbeitet den Stream mit Prozess-Zeit basierten Windows.

```java
DataStream<Tuple3<Integer, Integer, String>> stream = env.addSource(new SiemSource())
                .flatMap(new Splitter())
                .keyBy(t -> t.f0)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(1)))
                .reduce((t1,t2) -> {t1.f1 += t2.f1; t1.f2 += t2.f2; return t1;})
                ;

stream.print();
```

> 6> (1,*16*,**534, 332, 965, 1345, 1234, 876, 954, 1111,** )\
> 6> (1,*12*,**1876, 667, 3223, 3110, 3230, 78,** )

Die Ausgabe ist folgendermaßen zu interpretieren:
- erster Wert ist nur ein Identifikator für ``keyBy`` und hat sonst keine Bedeutung
- zweiter Wert (kursiv) ist die Summenbildung der einzelnen Element-Werte
- Rest (fett) sind die zugeordneten Event-Zeiten

Beim Ergebnis fällt auf, dass:
- die Events in der Reihenfolge ihrer Verarbeitung zu Prozess-Zeit vorliegen
- es nur 2 Fixed Windows erzeugt wurden, denn die Prozess-Zeiten bewegen sich nur in einem Intervall von [3000;3000 + 14 x 100] = [3000; 4400] Millisekunden

### Event Time Windows

Die Klasse [EventTimeWindowing](https://github.com/brunofight/OS_DSMS/blob/main/src/main/java/demo/windowing/EventTimeWindowing.java) verarbeitet den Stream mit Event-Zeit basierten Windows. Hierfür werden zusätzlich Watermarks benötigt, da sonst Windows unendlich lange erhalten bleiben.

```java
DataStream<Tuple3<Integer, Integer, String>> stream = env.addSource(new SiemSource())
		.assignTimestampsAndWatermarks(WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(3)))
		.flatMap(new Splitter())
		.keyBy(t -> t.f0)
		.window(TumblingEventTimeWindows.of(Time.seconds(1)))
		.reduce((t1,t2) -> {t1.f1 += t2.f1; t1.f2 += t2.f2; return t1;})
		;

stream.print();

```

> 6> (1,*14*,**534, 332, 965, 876, 954, 1111, 667,**)\
> 6> (1,*6*,**1345, 1234, 1876,** )\
> 6> (1,*2*,**3110,** )\
> 6> (1,*4*,**3223, 3230,** )

Im Vergleich zu den *Processing Time Windows*:
- insgesamt 4 *Fixed Windows*, da die Event-Zeiten sich in einem Intervall zwischen 78 und 3230 Millisekunden befinden
- die Events sind jetzt entsprechend ihrer Event-Zeit den Windows zugeordnet; die Summen entsprechen somit den korrekten Werten
- das letzte Event (Event-Zeit: 78) fehlt; das liegt an der Heuristik der Watermarks 

Durch Herumspielen mit dem *Duration.ofSeconds(int t)*-Parameter kann das letzte Event wahlweise mit erfasst werden oder auch nicht.
```java
env.assignTimestampsAndWatermarks(WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(3)))
```

Bei niedrigen Werten z.B. 1 Sekunde fehlt das letzte Event, denn zuvor erscheinen Events mit einer Event-Zeit von über 3000 ms. Das heißt nach der Watermark-Heuristik wird nicht von einer größeren "Unordnung" als einer Sekunde ausgeganen. Zu dem Zeitpunkt (in Prozess-Zeit), wenn das Event auftritt, wurde das zugehörige erste Window wieder geschlossen; die 78 fällt weg.
Hingegen bei einem höheren Watermark-Wert von bspw. 5 Sekunden wird die 78 erfasst.







