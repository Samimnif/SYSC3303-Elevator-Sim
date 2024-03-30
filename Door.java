import java.io.Serializable;

public class Door implements Serializable {
    private Boolean isOpen;
    //Constructor for class Door
    public Door() {
        this.isOpen = false;
    }

    public void openDoor(int fault) throws Exception {
        // Checks for fault
        if(fault == 1){
            throw new Exception("Door stuck");
        }
        System.out.println("Open Door");
        isOpen = true;
        //Can add functionality for the gui
    }

    public void closeDoor(int fault) throws Exception {
        // Checks for fault
        if(fault == 1){
            throw new Exception("Door stuck");
        }
        System.out.println("Close Door");
        isOpen = false;
        //Can add functionality for the gui
    }

    public Boolean isOpen() {
        return isOpen;
    }
}
