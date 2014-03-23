python setVersion.py 0.1-MULTIPLAYER
mvn clean package
cd target
mkdir inspect
cd inspect
unzip ../chunkmapper-0.0.1-SNAPSHOT.jar
cd ../../dist
ant multiplayer_zip -Dclasses=inspect
cp Chunkmapper-Multiplayer.zip ~/Desktop
cd ~/Desktop
rm -r Chunkmapper-Multiplayer
open Chunkmapper-Multiplayer.zip
