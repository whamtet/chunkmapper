Usage:

java -Xmx1G -jar Chunkmapper-Multiplayer.java "place to start"

e.g.

java -Xmx1G -jar Chunkmapper-Multiplayer.java "nelson, new zealand"

------
Upon first execution Chunkmapper-Multiplayer will create server.properties and world/ directories.  The Minecraft server can then be run from the current directory AT THE SAME TIME as Chunkmapper.  Keep it going so that it can expand the map as players move.

LIMITATION

Single player chunkmapper can always generate terrain faster than the player can move, so you'll never run out of map.  If a large number of multiplayers move in opposite directions, however, Chunkmapper may not be able to keep up.  Multiplayer has no gui, so inspect the file
world/chunkmapper/mappedSquares.txt

---mappedSquares.txt---

  011111111000000000
  111111111100000000
  111111111100000000
  111*11111100000000
  111111111100000000
  111111111110000000
  111111111111000000
  111111111111111110
  0111111*1111111110
  111111111111111111
  111111111111111111
  11111111*111111111
  111111111111111111
  11111111111111*111
  111111111111111111
  111111111111111111
  000000000001111111

The * designate player locations, 1 and 0 are generated and ungenerated map.  So long as all * are not directly adjacent to 0 or the edge of the map, you're all good.
