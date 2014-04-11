import sys
version = sys.argv[1]
f = open('chunkmapperResources/version.txt', 'w')
f.write(version)
f.close()

