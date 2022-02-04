mvn package -Dmain=com.chunkmapper.multiplayer.MPThread
mkdir out
cp target/chunkmapper-0.2.jar out
cat > out/chunkmapper.sh <<EOF
java -Xmx1G -jar chunkmapper-0.2.jar
EOF
cp out/chunkmapper.sh out/chunkmapper.bat
