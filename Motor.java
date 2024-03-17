import java.io.Serializable;

/**
 * Represents a motor in the elevator.
 */
public class Motor implements Serializable {
    private Boolean motor;
    /**
     * Constructor for a new motor.
     */
    public Motor() {
        this.motor = false;
    }

    /**
     * setter for the motor state.
     */
    public void setMotor(Boolean motor) {
        this.motor = motor;
    }

    /**
     * Getter for the motor state.
     * @return the motor state
     */
    public Boolean getMotor() {
        return motor;
    }
}
