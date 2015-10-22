python build_py/open_pom.py true
mvn clean install
python build_py/open_pom.py false
mkdir -p ~/clojure/chunkbackend/repo/com/chunkmapper/chunkmapper-unobfuscated
cp -r ~/.m2/repository/com/chunkmapper/chunkmapper-unobfuscated/ ~/clojure/chunkbackend/repo/com/chunkmapper/chunkmapper-unobfuscated
cp -r ~/.m2/repository/com/chunkmapper/chunkmapper-unobfuscated/ ~/clojure/chunkmapper-website/repo/com/chunkmapper/chunkmapper-unobfuscated
cd ~/clojure/chunkbackend
lein clean
tput bel
