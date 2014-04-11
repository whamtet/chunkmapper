python build_py/setVersion.py 0.1-MULTIPLAYER
python build_py/update_build_no.py
mvn clean package
python build_py/copy_proguard_map.py

cd target
mkdir inspect
cd inspect
unzip ../chunkmapper-0.0.1-SNAPSHOT.jar
cd ../../dist
ant multiplayer_zip -Dclasses=inspect
#cp Chunkmapper-Multiplayer.zip ~/Desktop
#cd ~/Desktop
#rm -r Chunkmapper-Multiplayer
#open Chunkmapper-Multiplayer.zip
