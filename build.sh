python setVersion.py 0.1-SINGLEPLAYER
mvn clean package
cd target
mkdir inspect
cd inspect
unzip ../chunkmapper-0.0.1-SNAPSHOT.jar
cd ../../dist
ant all -Dclasses=inspect -Dchunkmapper=Chunkmapper
#mkdir inspect
#cd inspect
#rm -r *
#unzip ../Chunkmapper.jar
#open com/chunkmapper/gui/a/a.class
#java -jar /Users/matthewmolloy/workspace/chunkmapper/dist/Chunkmapper.jar -Xmx1G
