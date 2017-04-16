OpenLoco
========
OpenLoco ist ein von [Tim Moran](https://github.com/mr-tim/openloco) mit Javascript entwickeltes Open-Source Remake von [Chris Sawyers Locomotion](https://de.wikipedia.org/wiki/Chris_Sawyer%E2%80%99s_Locomotion). Die hier dargestellte Seite, so wie auch der dargestellte und angebotene Quellcode, ist ein geklontes auf Deutsch übersetztes GitHub Projekt des o.g. Entwicklers Tim Moran. 


----------


Erstellen
--------
Wenn Sie den Code erstellen möchte, so ist dieses relativ einfach. Das einzigste was Sie dazu benötigen und installiert haben müssen ist:

 - [Maven](https://maven.apache.org/download.cgi)
 - [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

Nach dem Sie Maven und JDK 8 installiert haben, sind Sie bereit das Projekt zu erstellen. Dazu wechseln Sie bitte in Ihrer Befehlseingabeauforderung bzw. den Terminal zum Pfad in welchen Sie OpenLoco heruntergeladen und entpackt haben und führen folgenden Code aus:

    mvn package

Mit diesen Befehl erstellen Sie eine einzelne ausführbare .jar Datei mit allen benötigten Abhängigkeiten im Zielverzeichnis, welche zum ausführen notwendig sind. Die an der .jar Datei angehangenen Ziffern stellen die aktuelle Versionsnummer von OpenLoco dar.

Ausführen
-------

OpenLoco benötigt zum ausführen die Originalen Locomotion Grafiken aus dem "ObjData" Ordner. Sollten Sie "Chris Sawyers Locomotion" noch nicht besitzen, so erwerben Sie bitte das Spiel. Chris Sawyers Locomotion können Sie u.a. bei Amazon, GOG, Steam oder einen anderen Anbieter Ihrer Wahl erwerben. Sobald Sie Locomotion installiert haben, ist es erforderlich das Sie OpenLoco den Pfad zum Originalen Locomotion ObjData Ordner zuweisen. Dieses geschieht mit folgenden Befehl:

    java -Dopenloco.dataDir=Pfad/zu/Atari/Locomotion/ObjData -jar openloco-0.0.1-SNAPSHOT.jar LoadSpriteDemo

**Hinweis:** Sie benötigen JDK 8 um die .jar Datei ausführen zu können!

Zum ausführen der Demos benötigen Sie die LWJGL 2.9.1 Libs. Dieses sollte normalerweise automatisch geschehen. Sollte die automatische Zuweisung nicht funktionieren (weil Sie ggfls. ein ungewöhnliches Betriebsystem/Plattform benutzen), so laden Sie bitte als erstes 
[LWJGL 2.9.1 von Sourceforge.net herunter](http://sourceforge.net/projects/java-game-lib/files/Official%20Releases/LWJGL%202.9.1/) und weisen Sie mit nachfolgenden Befehl OpenLoco den Pfad zu LWJGL zu.

    -Djava.library.path=Pfad/Zu/lwjgl/native/$platform

Viel Spaß mit OpenLoco!

Verfügbare Demos
---------------
Derzeit stehen in eine Reihe von Demos zur Verfügung. Diese wären:

 1. ` java -Dopenloco.dataDir=Pfad/zu/Atari/Locomotion/ObjData -jar openloco-0.0.1-SNAPSHOT.jar LoadSpriteDemo`
Mit diesen Code können Sie sich die Sprites der HST Lok anzeigen lassen

 2. `java -Dopenloco.dataDir=Pfad/zu/Atari/Locomotion/ObjData -jar openloco-0.0.1-SNAPSHOT.jar TerrainDemo`
 Hiermit wird Ihnen das Terrain angezeigt
 
 3. `java -Dopenloco.dataDir=Pfad/zu/Atari/Locomotion/ObjData -jar openloco-0.0.1-SNAPSHOT.jar TrackDemo`
Mit diesen Code wird Ihnen ein Gleisbeispiel angezeigt

 4. `java -Dopenloco.dataDir=Pfad/zu/Atari/Locomotion/ObjData -jar openloco-0.0.1-SNAPSHOT.jar NashornScriptDemo`
Dieser Code führt den angegebenen Javascript innerhalb der init-Methode aus um damit eine Dynamische Basis Demo zu ermöglichen, mit welchen Sie neue Features ausprobieren können. Mit den Argumentieren weiterer Klassen in der Kommandozeile können Sie weitere Features hinzufügen.
