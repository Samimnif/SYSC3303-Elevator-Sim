/**
 * Represents a job for an elevator, containing information about the destination floor, pickup floor, timestamp, button pressed, and any faults.
 * @authors Sami Mnif, Muaadh Ali, Jalal Mourad, Jordan Bayne
 */
import java.io.Serializable;

public class Job implements Serializable {
    private String timeStamp;
    private int destinationFloor, pickupFloor;
    public int capacity;
    private String button;
    private int fault;

    /**
     * Constructs a new job with the specified parameters.
     *
     * @param timeStamp         the timestamp of the job
     * @param destinationFloor  the destination floor of the job
     * @param pickupFloor       the pickup floor of the job
     * @param button            the button pressed for the job
     * @param fault             the fault status of the job
     */
    public Job(String timeStamp, int destinationFloor, int pickupFloor, String button, int fault) {
        capacity = 1;
        this.timeStamp = timeStamp;
        this.destinationFloor = destinationFloor;
        this.pickupFloor = pickupFloor;
        this.button = button;
        this.fault = fault;
    }

    /**
     * Gets the timestamp of the job.
     *
     * @return the timestamp
     */
    public String getTimeStamp() {
        return timeStamp;
    }

    /**
     * Gets the destination floor of the job.
     *
     * @return the destination floor
     */
    public int getDestinationFloor() { return destinationFloor; }

    /**
     * Gets the pickup floor of the job.
     *
     * @return the pickup floor
     */
    public int getPickupFloor() { return pickupFloor; }

    /**
     * Gets the button pressed for the job.
     *
     * @return the button pressed
     */
    public String getButton() {
        return button;
    }

    /**
     * Sets the timestamp of the job.
     *
     * @param timeStamp the new timestamp
     */
    public void setTimeStamp(String timeStamp) { this.timeStamp = timeStamp; }

    /**
     * Sets the destination floor of the job.
     *
     * @param destinationFloor the new destination floor
     */
    public void setDestinationFloor(int destinationFloor) { this.destinationFloor = destinationFloor; }

    /**
     * Sets the pickup floor of the job.
     *
     * @param pickupFloor the new pickup floor
     */
    public void setPickupFloor(int pickupFloor) { this.pickupFloor = pickupFloor; }

    /**
     * Sets the button pressed for the job.
     *
     * @param button the new button pressed
     */
    public void setButton(String button) { this.button = button; }

    /**
     * Sets the fault status of the job.
     *
     * @param fault the new fault status
     */
    public void setFault(int fault) {
        this.fault = fault;
    }

    /**
     * Gets the fault status of the job.
     *
     * @return the fault status
     */
    public int getFault() {
        return fault;
    }
}
