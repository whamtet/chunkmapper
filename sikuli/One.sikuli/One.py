import os
from threading import Thread

class MyClass(Thread):
    def run(self):
        os.system('java -jar /Users/matthewmolloy/workspace/chunkmapper/dist/Chunkmapper.jar -Xmx1G')

class MyClass2(Thread):
    def run(self):
        os.system('java -jar /Users/matthewmolloy/workspace/chunkmapper/dist/Chunkmapper.jar -Xmx1G -flawed')
        
def launch_chunkmapper():
    if not exists("1391382376214-1.png"):
        print 'No Chunkmapper Window detected.  Starting new one.'
        myClass = MyClass()
        myClass.start()
        wait("1391381716886.png", 20)

def launch_flawed_chunkmapper():
    if not exists("1391382376214-1.png"):
        print 'No Chunkmapper Window detected.  Starting new one.'
        myClass = MyClass2()
        myClass.start()
        wait("1391381716886.png", 20)


def go_to(location):
    print 'Going to ' + location + '.'
    zoom_out()
    click("1391569331744.png")
    wait("1391406711654.png") 
    type(location)
    wait(0.5)
    type(Key.ENTER)
    wait(10)

def zoom_out():
    print 'Zooming out.'
    click("1391403833556.png")
    type("z")
    wait("1391569149349.png", 10)
    type(Key.ENTER)

def create_test_map():
    print 'Creating Test Map.'
    zoom_out()
    click("1391571736710.png")
    wait("1391571803329.png")
    type("test")
    wait(0.5)
    type(Key.ENTER)

def cancel_generation():
    print 'Cancelling Map Generation.'
    zoom_out()
    click("1391573005711.png")
    wait("1391573029160.png", 20)

def delete():
    print 'Deleting Map.'
    zoom_out()
    match = find(Pattern("1391573621573.png").targetOffset(50,0))
    click(match)
    click("1391573755018.png")

def resume():
    print 'Resuming Map Generation.'
    zoom_out()
    click("1391573621573.png")

def locate_minecraft_dir():
    click("1391653158899.png")

go_to('Tokyo')
create_test_map()
cancel_generation()
for i in range(10):
    resume()
    wait(10)
    cancel_generation()
    
print 'done'