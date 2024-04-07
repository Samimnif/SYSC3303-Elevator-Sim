/**
 * Lamp.java is a class that represents the lamps in the buttons of the elevators, it will either turn on or off
 * depending on the button request
 * The methods in thsi class will set the lamp status when it's called from the elevator or button objects.
 *
 * @authors Jalal Mourad, Jordan Bayne
 */

import java.io.Serializable;

public class Lamp implements Serializable {
    private boolean lamp;
    private int ID;

    /**
     * Constructor for class Lamp
     * @param ID
     */
    public Lamp(int ID){
        this.ID = ID;
        this.lamp = false;
    }

    /**
     * return true if lamp is ON and false if lamp is OFF
     * @return Boolean of the lamp status
     */
    public boolean isOn(){
        return lamp;
    }

    /**
     * Sets the lamp to ON or OFF depending on the input (True or False)
     * @param lamp (Boolean True or False) ON or OFF
     */
    public void setLamp(boolean lamp){
        this.lamp = lamp;
    }

    /**
     * Toggles the lamp, Turns the lamp on and off
     */
    public void toggleLamp(){
        lamp = !lamp;
        if(lamp){
            System.out.println("Lamp #" + this.ID + " is on");
        }else{
            System.out.println("Lamp #" + this.ID + " is off");
        }
        //Add gui functionality
    }

}
