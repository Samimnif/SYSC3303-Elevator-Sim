import java.io.Serializable;

public class Lamp implements Serializable {
    private boolean lamp;
    private int ID;

    //Constructor for class Lamp
    public Lamp(int ID){
        this.ID = ID;
        this.lamp = false;
    }

    //return true if lamp is ON and false if lamp is OFF
    public boolean isOn(){
        return lamp;
    }

    public void setLamp(boolean lamp){
        this.lamp = lamp;
    }

    //Turns the lamp on and off
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
