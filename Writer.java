package mvh.util;

import mvh.enums.WeaponType;
import mvh.world.Entity;
import mvh.world.Hero;
import mvh.world.Monster;
import mvh.world.World;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Class to assist writing world files
 *
 * @author Ryan Loi
 * @date (dd / mm / yr): 27/03/22
 * @version 1.0
 */

public final class Writer {

    /**
     *
     * @param FileObject a file object that points back to a file that the user selected in the GUI for saving
     * @param world a world object containing the game map and all of the entities to be stored in the file
     * @return returns a string describing if the writing process was successful or not, this will be displayed in the
     *         bottom right status bar
     */
    public static String createFile(File FileObject, World world){

        // check if the fileobject is a file, exists, and we can write to it
        if(FileObject.isFile() && FileObject.exists() && FileObject.canWrite()){

            // create the print writer if conditions are met, and do so with resources so it can catch exceptions that
            // may occur and close the file if any are found
            try(PrintWriter writer = new PrintWriter(new FileWriter(FileObject));) {
                //Empty string storing the data to be written to the file
                String output = "";
                // adding in the row data and column data as the first two lines of the world file
                output += world.getRows() + "\n";
                output += world.getColumns() + "\n";

                // loop through all of the rows in the world
                for(int r = 0; r< world.getRows(); r++){
                    // loop through all of the columns in the world
                    for(int c = 0; c< world.getColumns(); c++){
                        // add a comma
                        output = output + r + "," + c;
                        // determine if the spot is empty if not see if there is a hero or a monster there
                        if(world.getEntity(r,c) != null){
                            // if a hero resides at the row and column (r,c) we are looking at, get its symbol, health,
                            // weapon strength, and armor and append this to the output string along with a newline
                            if(world.isHero(r,c)) {
                                Hero hero = (Hero) world.getEntity(r,c);
                                output = output + "," + "HERO" + "," + hero.getSymbol() + "," + hero.getHealth() + "," + hero.weaponStrength() + "," + hero.armorStrength() + "\n";
                            }
                            // if a monster resides at the row and column (r,c) we are looking at, get its symbol, health,
                            // weapon type and append this to the output string along with a newline
                            else if (world.isMonster(r,c)){
                                Monster monster = (Monster) world.getEntity(r,c);
                                // getting the character of the monster's weapon
                                String weapon = weaponChar(monster.getWeaponType());
                                // only create the monster if the weapon type is valid aka the weaponChar function
                                // doesn't return problem
                                if(!weapon.equals("problem")){
                                output = output + "," + "MONSTER" + "," + monster.getSymbol() + "," + monster.getHealth() + "," + weapon + "\n";
                                }
                                // if the monster weapon type was invalid return an error message
                                else{
                                    return "World contains invalid Monster Weapon Type, Weapon Type must be: (C)lub/(A)xe/(S)word)";
                                }
                            }
                        } // add a new line to the output string if this r,c location was a floor
                        else{
                            output += "\n";
                        }
                    }
                }
                // print output to text file that the user selected
                writer.print(output);
                // flush after the write to make sure everything reaches the file
                writer.flush();
                // return true to indicate it wrote to the file successfully
                return "Successfully saved to file";
                // return that the file could not be written to
            }catch (Exception e){
                return "Error: File could not be written to";
            }
        }
    // returns error message if the file cannot be written to, because if it gets to this point that means the if statement block
    // was not entered and returned within there so thus the file writer couldn't write to the file
    return "Error: File could not be written to";
    }


    /**
     * A function that returns the string character of the weapontype a monster possesses
     * @param Type the weapon type the monster is holding
     * @return String of the type of weapon the monster is holding
     */
    private static String weaponChar(WeaponType Type){
        if (Type == WeaponType.CLUB) {
            return "C";
        } else if (Type == WeaponType.AXE) {
            return "A";
        } else if (Type.equals(WeaponType.SWORD)) {
            return "S";
            // returns problem if the monster was not holding one of the above weapons, i.e. the weapon type was invalid
            // this way the GUI will be able to display this error message
        } else {
            return "problem";
        }
    }


}
