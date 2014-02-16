mvn clean package
cd target
mkdir inspect
cd inspect
unzip ../chunkmapper2-0.0.1-SNAPSHOT.jar
cd ../../dist
ant -Dclasses=inspect
mkdir inspect
cd inspect
rm -r *
unzip ../Chunkmapper.jar
open com/chunkmapper/gui/a/a.class
java -jar ../Chunkmapper.jar -Xmx1G
