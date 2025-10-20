public class MazeGame {
    // Create the maze.
    public Maze createMaze() {
        Maze maze = Maze.SOLE;
        Room r1 = new Room(1);
        Room r2 = new Room(2);
        Door door = new Door(r1, r2);
        maze.addRoom(r1);
        maze.addRoom(r2);
        r1.setSide(Direction.North, new Wall());
        r1.setSide(Direction.East, door);
        r1.setSide(Direction.South, new Wall());
        r1.setSide(Direction.West, new Wall());
        r2.setSide(Direction.North, new Wall());
        r2.setSide(Direction.East, new Wall());
        r2.setSide(Direction.South, new Wall());
        r2.setSide(Direction.West, door);
        return maze;
    }
}

interface Builder(){

    public void buildRoom();

    public void buildDoor();

    public void reset();
}

public class SideBuilder extends Builder()
{
    }

    public class DoorBuilder extends Builder()
    {
        }

        // Director can encapsulate the building logic (like create how many rooms,
        // doors, etc)
        public class Director(){
            private Builder builder;

   public createMaze(){
      builder.buildRoom();
      builder.buildDoor();
   }
}

/**
 * client code:
 * builder = new SideBuilder();
 * new Director(builder).createMaze();
 */