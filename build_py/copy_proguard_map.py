f = open('chunkmapperResources/build-no.txt')
build_no = f.read()
f.close()

s = 'cp target/proguard_map.txt proguard_maps/%s.txt' % build_no

import os
os.system(s)
