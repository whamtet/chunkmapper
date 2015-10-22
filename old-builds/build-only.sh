python build_py/setVersion.py 0.3-SINGLEPLAYER
python build_py/update_build_no.py
mvn clean package
python build_py/copy_proguard_map.py

cd target
mkdir inspect
cd inspect
unzip ../chunkmapper-0.2.jar
cd ../../dist
ant all -Dclasses=inspect
