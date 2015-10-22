#!/bin/bash

mvn compile

java -Xmx10G -classpath lib/worldwind.jar:lib/worldwindx.jar:lib/jogl-all.jar:lib/gluegen-rt.jar:lib/gdal.jar:target com.chunkmapper.gui.Main #gov.nasa.worldwindx.examples.AirspaceBuilder
