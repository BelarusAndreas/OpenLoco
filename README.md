openloco
========

Running Demos
-------------

When running the demos, you'll need to make sure that the LWJGL libs are on the library path - assuming they're extracted to your file system you can do this by setting the following command line option:

    -Djava.library.path=path/to/lwjgl/native/$platform
    
In addition, you'll need to set the object data directory to the correct path in your locomotion installation:

    -Dopenloco.dataDir=path/to/locomotion/ObjData
