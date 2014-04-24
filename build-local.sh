mvn clean install
mkdir -p ~/clojure/chunkbackend/repo/com/chunkmapper/chunkmapper-unobfuscated
cp -r ~/.m2/repository/com/chunkmapper/chunkmapper-unobfuscated/ ~/clojure/chunkbackend/repo/com/chunkmapper/chunkmapper-unobfuscated
cd ~/clojure/chunkbackend
lein clean
