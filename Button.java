/**
 * Button.java represents the Button in the elevator that includes the Lamp that switches on when it's pressed
 *
 * @authors Jalal Mourad
 */

import java.io.Serializable;

public class Button implements Serializable {

    private boolean button ; //no need for this line
    private int ID;
    private Lamp lamp;

    //set to true if the button is pressed, and false otherwise

    /**
     * Intiializes the Button, by assigning the ID the lamp object assigned to it
     *
     * @param id
     * @param floorLamp
     */
    public Button(int id, Lamp floorLamp){
        this.button = false;
        this.ID = id;
        this.lamp = floorLamp;
    }
    //returns true if the button is pressed and false if it's not

    /**
     * isButtonPressed(): returns the Boolean if the button was pressed or not
     *
     * @return Boolean button pressed?
     */
    public boolean isButtonPressed(){
        return button;
    }

    /**
     * setButton(boolean button): sets the button to True or False
     * @param button (True or False)
     */
    public void setButton(boolean button) {
        this.button = button;
    }

    /**
     * pressButton(): once pressed, it will set the button to true and sets the lamp to ON
     */
    public void pressButton(){
        //if we press the button we set the lamp indicator to on
        this.button = true;
        this.lamp.setLamp(true);
    }

    /**
     * resetButton(): resets the button to false and resets the Lamp assigned to it to OFF
     */
    public void resetButton(){
        this.button = false;
        this.lamp.setLamp(false);
    }


}
