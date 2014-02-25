import os

def zoom_out():
    print 'Zooming out.'
    click("1393304419160.png")
        
    type("z")

    if os == 'osx':
        wait("1391569149349.png", 10)
    if os == 'win8':
        wait("1393290452737.png", 10)
    if os == 'win7':
        wait("1393307972388.png", 10)
    type(Key.ENTER)
        
        
def go_to(location):
    print 'Going to ' + location + '.'
    zoom_out()
    if os == 'osx':
        click("1391569331744.png")
    if os == 'win8':
        click("1393305580835.png")
    if os == 'win7':
        click("1393308025153.png")
        
        
    if os == 'osx':
        wait("1391406711654.png") 
    if os == 'win8':
        wait("1393303877488.png")
    if os == 'win7':
        wait("1393308430082.png")
        
   
    type(location)
    wait(0.5)
    type(Key.ENTER)
    wait(10)
        
        
    type(Key.ENTER)

def create_test_map():
    print 'Creating Test Map.'
    zoom_out()
    if os == 'osx':
        click("1391571736710.png")
    if os == 'win8':
        click("1393306543389.png")
    if os == 'win7':
        
        
    if os == 'osx':
        wait("1391571803329.png")
    if os == 'win8':
        wait("1393303969077.png")
        
    type("test")
    wait(0.5)
    type(Key.ENTER)

def cancel_generation():
    print 'Cancelling Map Generation.'
    zoom_out()
    if os == 'osx':
        click("1391573005711.png")
    if os == 'win8':
        click("1393306739115.png")

    if os == 'osx':
        wait("1391573029160.png", 30)
    if os == 'win8':
        wait("1393306858924.png", 30)
        

def delete():
    print 'Deleting Map.'
    zoom_out()
    if os == 'osx':
        match = find(Pattern("1391573621573.png").targetOffset(50,0))
        click(match)
    if os == 'win8':
        match = find(Pattern("1393307287561.png").targetOffset(50, 0))
        click(match)

    if os == 'osx':
        click("1391573755018.png")
    if os == 'win8':
        click("1393307447268.png")
        

def resume():
    print 'Resuming Map Generation.'
    zoom_out()
    if os == 'osx':
        click("1391573621573.png")
    if os == 'win8':
        click("1393306939427.png")
        

def locate_minecraft_dir():
    click("1391653158899.png")

os = 'win7'

go_to('London')
create_test_map()
cancel_generation()
for i in range(5):
    resume()
    wait(10)
    cancel_generation()
delete()

print 'done'