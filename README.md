openloco
========

Building
--------
If you want to build the code, it should be pretty straightforward - you'll need to install JDK 8 and Maven. Once you
have, you should be able to build the project using the following:

    mvn package

That will create a single executable jar in the target directory that contains all the dependencies required to run the
code, named with the current version number.

Running
-------

Firstly, we're using all the original locomotion graphics, so if you haven't already, go buy a copy of the game. Once
you have it installed, you need to set a command line option to give openloco the path to the ObjData within your
locomotion installation directory:

    java -jar openloco-0.0.1-SNAPSHOT.jar -Dopenloco.dataDir=path/to/locomotion/ObjData

Again - you'll need Java 8 installed in order to run the jar.

When running the demos, the LWJGL 2.9.1 libs are need to be on the library path. This should happen automatically, but
if it doesn't (or you're running on an unusual platform) you'll need to add them to the path manually using the
following command line argument (assuming you've [downloaded the libraries](http://sourceforge.net/projects/java-game-lib/files/Official%20Releases/LWJGL%202.9.1/)
to somewhere on your machine):

    -Djava.library.path=path/to/lwjgl/native/$platform

Enjoy!