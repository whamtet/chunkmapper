import os

def zoom_out():
    print 'Zooming out.'
    try:
        click("1393304419160.png")
        click("1393304419160.png")
    except FindFailed:
        click("1393308929042.png")
        
    type("z")

    if os == 'osx':
#        wait("1391569149349.png", 10)
        wait("1423648914622.png", 10)
    if os == 'win8':
        wait("1393290452737.png", 10)
    if os == 'win7':
        wait("1393307972388.png", 10)
    if os == 'winxp':
        #wait("1393310561703.png", 10)
        wait("1396950310084.png", 10)
        
    if os == 'ubuntu':
        wait("1393325264417.png", 10)
    if os == 'fedora':
        wait("1393383702659.png", 10)
        
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
    if os == 'winxp':
        click("1393310590115.png")
    if os == 'ubuntu':
        click("1393325300477.png")
    if os == 'fedora':
        click("1393383746956.png")
        
        
    if os == 'osx':
        wait("1391406711654.png") 
    if os == 'win8':
        wait("1393303877488.png")
    if os == 'win7':
        wait("1393308430082.png")
    if os == 'winxp':
        wait("1393310630183.png")
    if os == 'ubuntu':
        wait("1393325333646.png")
    if os == 'fedora':
        wait("1393383779164.png")
        
   
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
        click("1393308569993.png")
    if os == 'winxp':
        click("1393310661652.png")
    if os == 'ubuntu':
        click("1393325362176.png")
    if os == 'fedora':
        click("1393383807840.png")
        
        
    if os == 'osx':
        wait("1391571803329.png")
    if os == 'win8':
        wait("1393303969077.png")
    if os == 'win7':
        wait("1393308611482.png")
    if os == 'winxp':
        wait("1393310691918.png")
    if os == 'ubuntu':
        wait("1393381457308.png") #it's ok, buddy
    if os == 'fedora':
        wait("1393384660525.png")
        
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
    if os == 'win7':
        click("1393309115460.png")
    if os == 'winxp':
        click("1393310724681.png")
    if os == 'ubuntu':
        click("1393325427889.png")
    if os == 'fedora':
        click("1393383989673.png")
        

    if os == 'osx':
        wait("1391573029160.png", 30)
    if os == 'win8':
        wait("1393306858924.png", 30)
    if os == 'win7':
        wait("1393309169047.png", 30)
    if os == 'winxp':
        wait("1393310820275.png", 30)
    if os == 'ubuntu':
        wait("1393325461587.png", 30)
    if os == 'fedora':
        wait("1393384015700.png", 30)
        
        
        

def delete():
    print 'Deleting Map.'
    zoom_out()
    if os == 'osx':
        match = find(Pattern("1391573621573.png").targetOffset(50,0))
        click(match)
    if os == 'win8':
        match = find(Pattern("1393307287561.png").targetOffset(50, 0))
        click(match)
    if os == 'win7':
        match = find(Pattern("1393309257243.png").targetOffset(50, 0))
        click(match)
    if os == 'winxp':
        match = find(Pattern("1393309257243.png").targetOffset(50, 0))
        click(match)
    if os == 'ubuntu':
        match = find(Pattern("1393326622363.png").targetOffset(50, 0))
        click(match)
    if os == 'fedora':
        match = find(Pattern("1393384057939.png").targetOffset(50, 0))
        click(match)
        
        

    if os == 'osx':
        click("1391573755018.png")
    if os == 'win8':
        click("1393307447268.png")
    if os == 'win7':
        click("1393557266062.png")
        
        
    if os == 'winxp':
        click("1393311952529.png")
    if os == 'ubuntu':
        click("1393325677032.png")
    if os == 'fedora':
        click("1393384112700.png")
        

def resume():
    print 'Resuming Map Generation.'
    zoom_out()
    if os == 'osx':
        click("1391573621573.png")
    if os == 'win8':
        click("1393306939427.png")
    if os == 'win7':
        click("1393309334807.png")
    if os == 'winxp':
        click("1393311987610.png")
    if os == 'ubuntu':
        click("1393325711914.png")
    if os == 'fedora':
        click("1393384143090.png")

def delete_cache():
    click("1397718928064.png")
    click("1397718944132.png")
    click("1397718956656.png")
    click("1397718963641.png")
    
        

os = 'osx'
wait(1)
#delete_cache()
#delete()
go_to('Mumbai')
create_test_map()
cancel_generation()
for i in range(5):
    resume()
    wait(10)
    cancel_generation()
delete()


print 'done'