mvn clean package
cd dist
ant singleplayer_jar
java -jar Chunkmapper.jar

#python build_py/setVersion.py 0.3-SINGLEPLAYER
#python build_py/update_build_no.py
#mvn clean package
#python build_py/copy_proguard_map.py

#cd target
#mkdir inspect
#cd inspect
#unzip ../chunkmapper-unobfuscated-0.2.jar
#cd ../../dist
#ant all -Dclasses=inspect

#cd ..
#git add proguard_maps
#git add src
#git add chunkmapperResources
#git commit -am w
#git push
#ssh dad.chunkmapper.com 'bash -s' < deploy.sh
#tput bel
