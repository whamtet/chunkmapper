mvn clean package
cd target
mkdir inspect
cd inspect
unzip ../chunkmapper-0.0.1-SNAPSHOT.jar
cd ../../dist
ant test_jar -Dclasses=inspect
java -jar Chunkmapper-Test.jar
