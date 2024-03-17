import java.io.Serializable;

public class Door implements Serializable {
    private Boolean door;
    //Constructor for class Door
    public Door() {
        this.door = false;
    }

    public void setDoor(Boolean door) {
        this.door = door;
    }

    public Boolean getDoor() {
        return door;
    }
}
