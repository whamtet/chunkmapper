python build_py/open_pom.py true
mvn clean package
python build_py/open_pom.py false
cd target
mkdir inspect
cd inspect
unzip ../chunkmapper-unobfuscated-0.2.jar
