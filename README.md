Data Stream
===========

The data stream library exports telemetry data from simulations
and build up processing graphs for statistical data. It links
a model with one or multiple views.


## Sources

The repository contains two projects:
1. datastream: It contains the core of the data stream lib
2. datastream.eclipse: It contains a view, which integrates in Eclipse and allows to open Editors for data streams. A view is providing an overview about the available data streams.

## License

All datastream sources are made available under the Apache License 2.0.


## Dependencies

Datastream requires [JUnit](https://github.com/KentBeck/junit) for its test cases.
The normal code do not depend on any external library.

Datastream.eclipse depends on Eclipse and should work with 3.6, 4.2, and maybe some others.

Moreover, datastream.eclipse depends on [JFreeChart](http://sourceforge.net/projects/jfreechart/) (v1.0.13).
It is available under the LGPL 2.0 license.
You have to provide the JFreeChart as OSGi bundle, which exports the classes required by datastream.eclipse.
On ClassNotFoundException problems with PaintListener, you have to
[add the dependency org.eclipse.swt to the JFreeChart bundle](http://www.java-forum.org/plattformprogrammierung/84229-jfreechart-eclipse-rcp.html).
