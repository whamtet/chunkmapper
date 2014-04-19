python build_py/setVersion.py 0.2-SINGLEPLAYER
python build_py/update_build_no.py
mvn clean package
python build_py/copy_proguard_map.py

cd target
mkdir inspect
cd inspect
unzip ../chunkmapper-0.0.1-SNAPSHOT.jar
cd ../../dist
ant all -Dclasses=inspect

git add proguard_maps
git add src
git add chunkmapperResources
git commit -am w
git push
ssh dad.chunkmapper.com
./deploy.sh
