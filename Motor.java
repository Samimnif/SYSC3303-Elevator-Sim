import java.io.Serializable;

public class Motor implements Serializable {
    private Boolean motor;
    public Motor() {
        this.motor = false;
    }

    public void setMotor(Boolean motor) {
        this.motor = motor;
    }

    public Boolean getMotor() {
        return motor;
    }
}
