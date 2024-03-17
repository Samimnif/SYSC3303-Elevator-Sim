/**
 * Represents a floor in a building.
 */
public class Floor {
    int ID;

    /**
     * Creates a new floor with the given ID.
     * @param ID the unique identifier of the floor
     */
    public Floor( int ID ) {
        this.ID = ID;
    }

    /**
     * Getter for the ID of the floor.
     * @return the ID of the floor
     */
    public int getID() {
        return ID;
    }
}
