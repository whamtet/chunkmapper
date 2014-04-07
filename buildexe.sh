mvn clean package
cd target
mkdir inspect
cd inspect
unzip ../chunkmapper-0.0.1-SNAPSHOT.jar
cd ../../dist
ant singleplayer_jar -Dclasses=inspect
cp Chunkmapper.jar ~/clojure/chunkmapper-downloads/resources/dist
