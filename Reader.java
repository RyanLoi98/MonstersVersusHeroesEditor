package mvh.util;

import mvh.enums.WeaponType;
import mvh.world.Hero;
import mvh.world.Monster;
import mvh.world.World;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

/**
 * Class to assist reading in world file
 *
 * ****** Copied this file reader from my (Ryan Loi) Assignment 2 code *********
 *
 * @author Ryan Loi 
 * @date (dd / mm / yr): 27/03/22
 * @version 1.0
 */
public final class Reader {

    //CONSTANTS
    static int minRequiredData = 2;
    static int entityTypeIndex = 2;
    static int entitySymbolIndex = 3;
    static int entityHealthIndex = 4;
    static int monsterWeaponIndex = 5;
    static int heroAttackIndex = 5;
    static int heroArmorIndex = 6;

    /**
     * A class method that will open a user provided input file, read its contents, and create a world and return it
     * (unless the file cannot be read, at this point the program will exit with an error)
     * @param fileWorld File object linking to the file.txt entered by the user as an argument
     * @return world object which is a 2D grid (array) containing our entities (monsters or heroes), the floor are the
     * null spots
     */
    public static World loadWorld(File fileWorld) {
        // creating world object
        World world;

        // Creating the fileReader and bufferedReader within the try round brackets to enable file closing via resources
        // also catches any exceptions that may occur
        try (FileReader fileReader = new FileReader(fileWorld); BufferedReader bufferedReader = new BufferedReader(fileReader);){

            // getting the number of rows (first line) and columns (second line)
            int rows = Integer.parseInt(bufferedReader.readLine().trim());
            int columns = Integer.parseInt(bufferedReader.readLine().trim());

            // initializing world
            world = new World(rows, columns);

            // looping through all of the rows (which is 1 less than the number of rows and attainable when r<rows)
            for (int r = 0; r < rows; r++){

                // looping through all of the columns (which is 1 less than the number of columns and attainable when c<columns)
                for (int c = 0; c < columns; c++){
                    String line = bufferedReader.readLine();

                    //splitting the line via the commas
                    String[] lineSplit = line.split(",");

                    // checking if there is an entity present in this cell (rowXcolumn) if there is the length line will
                    // be > 2 (minRequiredData)

                    if (lineSplit.length > minRequiredData){

                        // check if row and column of the entity matches the row and column that was supposed to be
                        // present, if not throw exception so null will be returned
                        if(Integer.parseInt(lineSplit[0].trim()) != r || Integer.parseInt(lineSplit[1].trim()) != c){
                            throw new Exception();
                        }


                        // if index 2 of the split comma separated line is Monster (after trimming spaces and turning it into uppercase, create a monster entity and add it
                        // to the world in this row X column
                        if (lineSplit[entityTypeIndex].trim().toUpperCase(Locale.ROOT).equals("MONSTER")){

                            // getting the symbol of the entity which is in string form, so it is converted into a char
                            // and using index 0 because it is only one symbol at index 3 (entitiySymbolIndex) of the
                            // split comma separated line
                            char entitySymbolChar = lineSplit[entitySymbolIndex].trim().charAt(0);

                            // obtaining the char for the weapon type in the same fashion as the symbol.
                            char weaponChar = lineSplit[monsterWeaponIndex].trim().charAt(0);
                            // getting weapon using the weapon type method
                            WeaponType weapon = WeaponType.getWeaponType(weaponChar);

                            //creating monster, health is converted into an int from the split line
                            Monster monster = new Monster(Integer.parseInt(lineSplit[entityHealthIndex].trim()),entitySymbolChar,weapon);

                            // adding monster to the world at its specified row and column
                            world.addEntity(r , c, monster);
                        }
                        // else if index 2 of the split comma separated line is hero (after trimming white spaces and turning it into uppercase, create a hero entity and add it
                        // to the world in this row (r) X column (c)
                        else if (lineSplit[2].trim().toUpperCase(Locale.ROOT).equals("HERO")){

                            // check if attack or defense is greater than or equal to 0, if not throw exception so a null world can be returned
                            if(Integer.parseInt(lineSplit[heroAttackIndex].trim()) < 0 || Integer.parseInt(lineSplit[heroArmorIndex].trim()) < 0){
                                throw new Exception();
                            }

                            // getting the symbol of the entity which is in string form, so it is converted into a char
                            // and using index 0 because it is only one symbol at index 3 (entitiySymbolIndex) of the
                            // split comma separated line
                            char entitySymbolChar = lineSplit[entitySymbolIndex].trim().charAt(0);

                            // creating the hero, health, attack, and defense were obtained using indexing of the
                            // split comma separated line
                            Hero hero = new Hero(Integer.parseInt(lineSplit[entityHealthIndex].trim()),entitySymbolChar,Integer.parseInt(lineSplit[heroAttackIndex].trim()), Integer.parseInt(lineSplit[heroArmorIndex].trim()));

                            // adding hero to the world at its specified row and column
                            world.addEntity(r , c, hero);

                            // return null if there are entities outside of Hero and monster
                        }else {
                            return null;
                        }

                    }


                }

            }

            // closes file when it is done
            bufferedReader.close();
            return world;
        } //catching any errors from reading the file (file will be closed automatically here because it was opened via
          // resources in the try ()
        catch(IOException e){
            // return null to the world object instead of exiting
            return null;
        }
        // catch any other exceptions
        catch (Exception e){
            // return null to the world object instead of exiting
            return null;
        }
    }
}
