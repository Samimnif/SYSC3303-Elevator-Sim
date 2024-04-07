/** Door.java
 * This class represents the door of an elevator object.
 * The Door class will have opening and closing functions with the specific delay time from the config file
 * If there is a fault recorded from the events.txt job, then we raise an exception for door fault
 *
 * @authors Jordan Bayne, Jalal Mourad, Sami Mnif
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

public class Door implements Serializable {
    private Boolean isOpen;
    private static final String file = "config.properties";
    private final double DOOR_TIME, TRANSIENT_FAULT_TIME;
    private Boolean hasFault;

    /** Door(): Constructor for the Door object
     * Sets the door as closed (False)
     * and reads the DOOR_MOVEMENT time from the config file
     */
    public Door() {
        this.isOpen = false;
        hasFault = false;
        try {
            FileInputStream propsInput = new FileInputStream(file);
            Properties prop = new Properties();
            prop.load(propsInput);

            this.DOOR_TIME = Double.parseDouble(prop.getProperty("DOOR_MOVEMENT"));
            this.TRANSIENT_FAULT_TIME = Double.parseDouble(prop.getProperty("TRANSIENT_FAULT_TIME"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void toggleFault(){
        hasFault = !hasFault;
    }
    /**
     * openDoor(): will open the door by setting the Boolean to true.
     * It will then thread.sleep for the amount of time that was inputted from config file, to simulate
     * a door opening
     * Raises the exception for door fault if the fault inputted in the events.txt is "1"
     * @throws Exception
     */
    public void openDoor() throws Exception {
        // Checks for fault
        if(hasFault){
            Thread.sleep((long) (TRANSIENT_FAULT_TIME*1000));
            throw new Exception("\033[1;31mDoor stuck when opening\033[0m");
        }
        System.out.println("Open Door");
        Thread.sleep((long) (1000*DOOR_TIME));
        isOpen = true;
        //Can add functionality for the gui
    }

    /**
     * closeDoor(): will close the door by setting the Boolean to false
     * It will then thread.sleep for the amount of time that was inputted from config file, to simulate
     * a door closing.
     * @throws Exception
     */
    public void closeDoor() throws Exception {
        // Checks for fault
        if(hasFault){
            throw new Exception("\033[1;31mDoor stuck when closing\033[0m");
        }
        System.out.println("Close Door");
        Thread.sleep((long) (1000*DOOR_TIME));
        isOpen = false;
        //Can add functionality for the gui
    }

    /**
     * isOpen(): checks if the door is open or not
     * @return
     */
    public Boolean isOpen() {
        return isOpen;
    }
}
