f = open('chunkmapperResources/build-no.txt')
build_no = int(f.read())
f.close()

build_no += 1

f = open('chunkmapperResources/build-no.txt', 'w')
f.write(str(build_no))
f.close()
