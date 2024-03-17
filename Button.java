import java.io.Serializable;

public class Button implements Serializable {

    private boolean button ; //no need for this line
    private int ID;
    private Lamp lamp;

    //set to true if the button is pressed, and false otherwise
    public Button(int id, Lamp floorLamp){
        this.button = false;
        this.ID = id;
        this.lamp = floorLamp;
    }
    //returns true if the button is pressed and false if it's not
    public boolean isButtonPressed(){
        return button;
    }

    public void setButton(boolean button) {
        this.button = button;
    }

    public void pressButton(){
        //if we press the button we set the lamp indicator to on
        this.button = true;
        this.lamp.setLamp(true);
    }

    public void resetButton(){
        this.button = false;
        this.lamp.setLamp(false);
    }


}
