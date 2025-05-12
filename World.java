package mvh.world;

import mvh.enums.Direction;
import mvh.enums.Symbol;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A World is a 2D grid of entities, null Spots are floor spots
 * @author Ryan Loi
 * @date (dd / mm / yr): 27/03/22
 * @version 1.0a
 */
public class World {

    /**
     * World starts ACTIVE, but will turn INACTIVE after a simulation ends with only one type of Entity still ALIVE
     */
    private enum State {
        ACTIVE, INACTIVE
    }

    /**
     * The World starts ACTIVE
     */
    private State state;
    /**
     * The storage of entities in World, floor is null, Dead entities can be moved on top of (deleting them essentially from the map)
     */
    private final Entity[][] world;
    /**
     * We track the order that entities were added (this is used to determine order of actions each turn)
     * Entities remain in this list (Even if DEAD) ,unlike the world Entity[][] where they can be moved on top of causing deletion.
     */
    private final ArrayList<Entity> entities;
    /**
     * We use a HashMap to track entity location in world {row, column}
     * We will update this every time an Entity is shifted in the world Entity[][]
     */
    private final HashMap<Entity, Integer[]> locations;

    /**
     * The local view of world will be 3x3 grid for attacking
     */
    private static final int ATTACK_WORLD_SIZE = 3;
    /**
     * The local view of world will be 5x5 grid for moving
     */
    private static final int MOVE_WORLD_SIZE = 5;

    /**
     * A new world of ROWSxCOLUMNS in size
     *
     * @param rows    The 1D of the 2D world (rows)
     * @param columns The 2D of the 2D world (columns)
     */
    public World(int rows, int columns) {
        this.world = new Entity[rows][columns];
        this.entities = new ArrayList<>();
        this.locations = new HashMap<>();
        //Starts active
        this.state = State.ACTIVE;
    }


    /**
     * Move an existing entity
     *
     * @param row    The  row location of existing entity
     * @param column The  column location of existing entity
     * @param d      The direction to move the entity in
     */
    public void moveEntity(int row, int column, Direction d) {
        Entity entity = getEntity(row, column);
        int moveRow = row + d.getRowChange();
        int moveColumn = column + d.getColumnChange();
        this.world[moveRow][moveColumn] = entity;
        this.world[row][column] = null;
        this.locations.put(entity, new Integer[]{moveRow, moveColumn});
    }

    /**
     * Add a new entity
     *
     * @param row    The  row location of new entity
     * @param column The  column location of new entity
     * @param entity The entity to add
     */
    public void addEntity(int row, int column, Entity entity) {
        this.world[row][column] = entity;
        this.entities.add(entity);
        locations.put(entity, new Integer[]{row, column});
    }

    /**
     * Get entity at a location
     *
     * @param row    The row of the entity
     * @param column The column of the entity
     * @return The Entity at the given row, column
     */
    public Entity getEntity(int row, int column) {
        return this.world[row][column];
    }

    /**
     * Get entity at a location
     *
     * @param row    The row of the entity
     * @param column The column of the entity
     * @param d      The direction adjust look up towards
     * @return The Entity at the given row, column
     */
    public Entity getEntity(int row, int column, Direction d) {
        return getEntity(row + d.getRowChange(), column + d.getColumnChange());
    }

    /**
     * See if we can move to location
     *
     * @param row    The row to check
     * @param column The column to check
     * @return True if we can move to that location
     */
    public boolean canMoveOnTopOf(int row, int column) {
        Entity entity = getEntity(row, column);
        if (entity == null) {
            return true;
        }
        return entity.canMoveOnTopOf();
    }

    /**
     * See if we can move to location
     *
     * @param row    The row to check
     * @param column The column to check
     * @param d      The direction adjust look up towards
     * @return True if we can move to that location
     */
    public boolean canMoveOnTopOf(int row, int column, Direction d) {
        return canMoveOnTopOf(row + d.getRowChange(), column + d.getColumnChange());
    }

    /**
     * See if we can attack entity at a location
     *
     * @param row    The row to check
     * @param column The column to check
     * @return True if we can attack entity at that location
     */
    public boolean canBeAttacked(int row, int column) {
        Entity entity = getEntity(row, column);
        if (entity == null) {
            return false;
        }
        return entity.canBeAttacked();

    }

    /**
     * See if we can attack entity at a location
     *
     * @param row    The row to check
     * @param column The column to check
     * @param d      The direction adjust look up towards
     * @return True if we can attack entity at that location
     */
    public boolean canBeAttacked(int row, int column, Direction d) {
        return canBeAttacked(row + d.getRowChange(), column + d.getColumnChange());

    }

    /**
     * See if entity is hero at this location
     *
     * @param row    The row to check
     * @param column The column to check
     * @return True if entity is a hero at that location
     */
    public boolean isHero(int row, int column) {
        Entity entity = getEntity(row, column);
        if (entity == null) {
            return false;
        }
        return entity instanceof Hero;
    }


    /**
     * See if entity is monster at this location
     *
     * @param row    The row to check
     * @param column The column to check
     * @return True if entity is a monster at that location
     */
    public boolean isMonster(int row, int column) {
        Entity entity = getEntity(row, column);
        if (entity == null) {
            return false;
        }
        return entity instanceof Monster;
    }

    /**
     * The rows of the world
     *
     * @return The rows of the world
     */
    public int getRows() {
        return world.length;
    }

    /**
     * The columns of the world
     *
     * @return The columns of the world
     */
    public int getColumns() {
        return world[0].length;
    }


    /**
     * World method to create the world map of the game. Walls are represented as '#', floor as '.', dead entities as '$',
     * and alive entities by their user designated symbols from the input file
     *
     * Code taken from assignment 2, created by Ryan Loi
     *
     * @return returns the world map of the game as a string to the caller
     */
    public String worldString(){
        // String holding the game map
        String map = "";

        // String to hold the walls for the top and bottom of the map
        String topBotWall = "";

        /*Creating the walls for the top and bottom of the map, this will get the number of columns of the array world
          using index 0 to access the first row. Then it will add 2 to it so the walls will be able to line up with the
          side walls. Then the loop will add the appropriate number of wall pieces to the string topBotWall for
          the top and bottom walls of the map
        */

        int rowCounter = 0;
        int columnCounter = 0;

        for (int i = 0; i< (world[0].length + 2); i++){
            topBotWall += Symbol.WALL.getSymbol();
        }

        // adding the top wall to the map with a newline escape
        map = map + topBotWall + "\n";

        // loop through the rows of the world array
        for (Entity[] rows : world){
            // add left wall to the row of the map before we loop through said row and get the columns
            map = map + Symbol.WALL.getSymbol();

            // looping through each column of the array and adding whatever is contained there to the map, this can
            // be an Entity (dead = $, alive = symbol) or floor (if column is null)
            for (Entity column : rows){

                if (column == null){
                    map = map + Symbol.FLOOR.getSymbol();
                }
                else if (column.isAlive()){
                    map = map + column.getSymbol();
                }
                // checks if dead hero if so then put symbol of dead entity
                else if (column.isDead() && isHero(rowCounter, columnCounter)){
                    map = map + Symbol.DEAD.getSymbol();
                }
                // checks if dead monster if so then put symbol of dead entity
                else if (column.isDead() && isMonster(rowCounter, columnCounter)) {
                    map = map + Symbol.DEAD.getSymbol();
                }
                // if anything else this is a wall so put wall here, this is mainly used for the getLocal where it puts
                // walls within the function, so if we need to print that function this is here to ensure it is done
                // properly
                else{
                    map = map + Symbol.WALL.getSymbol();
                }

                // increment columnCounter
                columnCounter++;
            }
            // adding a right wall piece for this row of the map and new line so each row of the map is separate
            map = map + Symbol.WALL.getSymbol() + "\n";
            //increment rowCounter
            rowCounter ++;
            // reset columnCounter at end of columns
            columnCounter = 0;
        }

        // adding the bottom wall to the map
        map = map + topBotWall;

        // return the map
        return map;
    }



    @Override
    public String toString() {

        return worldString();
    }
}
