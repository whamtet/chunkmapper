python build_py/setVersion.py 0.1-SINGLEPLAYER
python build_py/update_build_no.py
mvn clean package
python build_py/copy_proguard_map.py

cd target
mkdir inspect
cd inspect
unzip ../chunkmapper-0.0.1-SNAPSHOT.jar
cd ../../dist
ant all -Dclasses=inspect
#open Chunkmapper.app
#ant app -Dclasses=inspect
#open Chunkmapper.app
#mkdir inspect
#cd inspect
#rm -r *
#unzip ../Chunkmapper.jar
#open com/chunkmapper/gui/a/a.class
#java -jar /Users/matthewmolloy/workspace/chunkmapper/dist/Chunkmapper.jar -Xmx1G
