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
            throw new Exception("\033[1;31mDoor stuck when opening\033[0m");
        }
        System.out.println("Open Door");
        Thread.sleep(8500);
        isOpen = true;
        //Can add functionality for the gui
    }

    public void closeDoor(int fault) throws Exception {
        // Checks for fault
        if(fault == 1){
            throw new Exception("\033[1;31mDoor stuck when closing\033[0m");
        }
        System.out.println("Close Door");
        Thread.sleep(8500);
        isOpen = false;
        //Can add functionality for the gui
    }

    public Boolean isOpen() {
        return isOpen;
    }
}
