repo=~/python/eclipse-repo/repo
#-DlocalRepositoryPath=$repo

mvn install:install-file -Dfile=lib/simple-5.1.5.jar -DgroupId=self -DartifactId=simple -Dversion=5.1.5 -Dpackaging=jar -DlocalRepositoryPath=$repo

mvn install:install-file -Dfile=lib/gluegen-rt.jar -DgroupId=gov.nasa -DartifactId=gluegen-rt -Dversion=912.1822 -Dpackaging=jar -DlocalRepositoryPath=$repo

mvn install:install-file -Dfile=lib/jogl-all.jar -DgroupId=gov.nasa -DartifactId=jogl-all -Dversion=912.1822 -Dpackaging=jar -DlocalRepositoryPath=$repo

mvn install:install-file -Dfile=lib/plugin.jar -DgroupId=gov.nasa -DartifactId=plugin -Dversion=912.1822 -Dpackaging=jar -DlocalRepositoryPath=$repo

mvn install:install-file -Dfile=lib/worldwind.jar -DgroupId=gov.nasa -DartifactId=worldwind -Dversion=912.1822 -Dpackaging=jar -DlocalRepositoryPath=$repo

mvn install:install-file -Dfile=lib/worldwindx.jar -DgroupId=gov.nasa -DartifactId=worldwindx -Dversion=912.1822 -Dpackaging=jar -DlocalRepositoryPath=$repo

mvn install:install-file -Dfile=lib/gdal.jar -DgroupId=gov.nasa -DartifactId=gdal -Dversion=912.1822 -Dpackaging=jar -DlocalRepositoryPath=$repo