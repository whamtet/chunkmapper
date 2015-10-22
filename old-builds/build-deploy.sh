git add proguard_maps
git add src
git add chunkmapperResources
git commit -am w
git push
ssh dad.chunkmapper.com 'bash -s' < deploy.sh
tput bel
