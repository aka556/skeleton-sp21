# Project 3 Prep

**For tessellating hexagons, one of the hardest parts is figuring out where to place each hexagon/how to easily place hexagons on screen in an algorithmic way.
After looking at your own implementation, consider the implementation provided near the end of the lab.
How did your implementation differ from the given one? What lessons can be learned from it?**

Answer: My implementation used two helper methods `hexRowWidth` and `hexRowOffset` to compute the width and offset for each row, then used `hexX` and `hexY` methods to calculate each hexagon's position in the tessellation. Key lessons learned: 1) Hierarchical abstraction is crucial — breaking complex tasks into small, manageable methods; 2) Mathematical formulas (for offsets and widths) help solve problems algorithmically; 3) Each hexagon's position can be determined by column and row numbers rather than manually specifying each coordinate.

-----

**Can you think of an analogy between the process of tessellating hexagons and randomly generating a world using rooms and hallways?
What is the hexagon and what is the tesselation on the Project 3 side?**

Answer: The analogy is:
- Hexagon → Room: Each hexagon is an independent region, similar to a room in Project 3. Rooms have fixed shapes and sizes.
- Hexagon tessellation → Room and hallway connections: Just as hexagons connect by sharing edges to form a honeycomb structure, rooms in Project 3 connect via hallways to form a complete world.
- Hexagon position calculation → Room layout algorithm: The method of calculating hexagon positions is similar to the algorithm that determines where to place rooms in Project 3.

-----

**If you were to start working on world generation, what kind of method would you think of writing first? 
Think back to the lab and the process used to eventually get to tessellating hexagons.**

Answer: If I were to start working on Project 3 world generation, I would write the following methods first:
1. **Basic drawing method**: Similar to `addHexagon`, implement a method to draw a single room first.
2. **Position calculation method**: Similar to `hexX` and `hexY`, calculate the room's position in the world.
3. **Room generation method**: Generate rooms with random sizes and shapes.
4. **Hallway connection method**: Connect adjacent rooms.
5. **World initialization method**: Similar to `drawWorld`, combine all parts to generate the complete world.

The key is to implement the smallest unit first (a single room), then gradually expand to more complex structures (room connections, complete world).

-----

**What distinguishes a hallway from a room? How are they similar?**

Answer:

**Differences:**
- **Shape**: Rooms are usually rectangular or irregular shapes, while hallways are narrow passages.
- **Size**: Rooms have larger areas, while hallways are narrower (usually 1-2 tiles wide).
- **Function**: Rooms are "destinations", while hallways are "connecting paths".

**Similarities:**
- Both are composed of tiles (TETile).
- Both need boundaries (walls).
- Both occupy space in the world.
- Both can be drawn using similar methods (filling tile arrays).
- Both need to consider connections with other structures.

In terms of implementation, rooms and hallways can both be drawn using the same underlying methods — the differences lie in size parameters and position calculations.
