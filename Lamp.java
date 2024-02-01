public class Lamp {
    private boolean lamp;
    private int ID;

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
    public void toggleLamp(){
        if (lamp){
            lamp = false;
        }
        else{
            lamp = true;
        }
    }

}
