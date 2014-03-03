mvn clean package
cd target
mkdir inspect
cd inspect
unzip ../chunkmapper2-0.0.1-SNAPSHOT.jar
cd ../../dist
ant copy -Dclasses=inspect
#mkdir inspect
#cd inspect
#rm -r *
#unzip ../Chunkmapper.jar
#open com/chunkmapper/gui/a/a.class
#java -jar /Users/matthewmolloy/workspace/chunkmapper/dist/Chunkmapper.jar -Xmx1G
