# Vortrag

## 1. Vergleich Batch und Stream Processing

Die **Kardinalität** von Datensätzen fällt in eine von zwei Kategorien: *Bounded* oder *Unbounded*. 
*Bounded* beschreibt einen Datensatz endlicher Größe.
*Unbounded* beschreibt einen Datensatz (theoretisch) unendlicher Größe.

Unter der Ausprägung (Konstitution) eines Datensatzes versteht man dessen physische Manifestation. Im Fall einer *Tabelle* werden Betrachtungen in Bezug auf eine vollständige Menge von Daten vorgenommen. *Streams* erlauben eine elementweise Sicht auf die Entwicklung eines Datensatzes über einen beliebigen Zeitraum hinweg.

Es ist durchaus möglich *Unbounded Datasets* als Tabelle aufzufassen, wenn man die Gesamtheit der Daten in einem bestimmten (abgeschlossenen) Zeitabschnitt vornimmt. Genau nach diesem Prinzip arbeitet das *Batch Processing*. Da nun aber erst alle Datensätze in einem Zeitintervall gesammelt werden bevor eine Transformation bzw. Analyse stattfinden kann, besitzt das *Batch Processing* keine Echtzeitfähigkeit. Des Weiteren arbeitet *Batch Processing* ausschließlich auf Basis der Prozess-Zeit und unter der Annahme von korrekt geordneten Daten.

Diese Defizite überkommt das *Stateful Stream Processing*, als für die Verarbeitung von endlosen Datenströmen optimiertes Anwendungs-Entwurfsmuster. Zentrales Element ist der Zustand, der durch eingehende Events verändert wird oder Einfluss auf deren Transformation hat.


## 2. Use Case - Log Analysis

Ein prägnanter Anwendungsfall für *Stateful Stream Processing* ist die kontinuierliche Log-Analyse. Logs in einem Unternehmensnetzwerk sind als *Unbounded Data* aufzufassen.

Die Log-Analyse bedarf einer Informationsbereitstellung in Echtzeit. Nur so können frühzeitig Anomalien und Fehler erkannt (und behoben) werden. Außerdem ist eine zentrale Verarbeitung aller System-Logs, zwecks Log-Korrelation, zwingend erforderlich.

Das SIEM (Security Information and Event Management) ist ein wichtiges Teilgebiet der Log-Analyse in der IT-Sicherheit. Aus Log-Events werden Informationen zur Erkennung von Cyberangriffsmustern gewonnen und in Use-Cases festgehalten. Typische Use-Case-Definitionen basieren auf Schwellwerten für bspw. fehlgeschlagene Anmeldungen, Web-Requests (DDoS-Angriff), ausgehender DNS-Traffic (Data Exfiltration).
Bekannte kommerzielle SIEM-Systeme sind Splunk, ArcSight und QRadar.

Die Erläuterung der Demo befindet sich im Abschnitt *Demo 1*.

## 3. Windowing

Zeitabhängige Streams werden zur weiteren Transformation in Zeitabschnitte (Windows) aufgeteilt. Man unterscheidet dabei zwischen *Fixed (bzw. Tumbling) Windows*, *Sliding Windows* und *Session Windows*. Zudem ist beim *Windowing* eine Wahl zu treffen, ob entlang der *Processing Time* oder *Event Time* *Windows* definiert werden sollen. 

*Processing Time Windows* lassen sich einfach implementieren, sind garantiert vollständig und für die Verarbeitung von zeit-agnostischen Daten (z.B. Filtern von bestimmten Web-Requests) durchaus sinnvoll. Es gibt dabei aber keinen Weg ungeordnete (*out-of-order*) Events und/oder hohen *Event Time Skew* zu kompensieren. Diese Defizite lassen sich mit *Event Time Windows* überwinden (allerdings möglicherweise zu Kosten der Vollständigkeit).

## 4. Trigger

*Trigger* definieren Zeitpunkte, zu denen aktualisierte Versionen eines Windows (*Panes*) ausgegeben werden. 

*Repeated Update Trigger* geben periodisch beim Eingang neuer Events (*Per-Record*) oder basierend auf der Prozess-Zeit (*Aligned Delay, Unaligned Delay*) neue Panes aus. *Unaligned Delay Repeated Update Trigger* sorgen für eine gleichmäßigere Lastverteilung als mit *Aligned Delay*.

*Completeness Trigger* werden bei einer bestimmten Vollständigkeit eines Windows ausgelöst. Voraussetzung für eine Abschätzung, ob ein Window (in *Event Time*) vollständig ist, sind *Watermarks*.

## 5. Watermarks

Der Grundgedanke von *Watermarks* ist die (heuristische) Bestimmung einer Event-Zeit E, bis zu welcher alle Events erhalten wurden.

*Watermarks* liefern entweder eine konkrete Aussage (*Perfect Watermark*) oder eine Abschätzung (*Heuristic Watermark*) über die Vollständigkeit der eingegangenen Events. 

*Perfect Watermarks* setzen vollständiges Wissen über die Gesamtheit der Eingangsdaten voraus und sind somit oftmals nicht umsetzbar.

## 6. Demo - Windowing in Event and Processing Time

Die Erläuterung der Demo befindet sich im Abschnitt *Demo 2*.

## 7. Accumulation Modes

Die Bestimmung der Zustände fortlaufender Panes eines Windows erfolgt in einem von drei Accumulation Modes: *Discarding* , *Accumulating* oder *Accumulating and Retracting*.





## 8. Zusammenfassung



## Quellen

- Streaming Systems ( O‘Reilly 2018), Tyler Akidau, Slava Chernyak, Reuven Lax
- Stream Processing with Flink (O‘Reilly 2019), Fabian Hueske, Vasiliki Kalavri

- https://www.upsolver.com/blog/6-most-common-streaming-data-use-cases
- https://www.onrec.com/sites/onrec/directory/files/R22e4774671ca419919bfa021bf7b7198.jpg
- https://blog.cloudera.com/real-time-log-aggregation-with-flink-part-1/
- https://www.youtube.com/watch?v=PzPXRmVHMxI&t=16s


