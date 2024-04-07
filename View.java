import javax.swing.*;
import java.awt.*;


public class View {
    private ElevatorShape elevator1;
    private ElevatorShape elevator2;
    private ElevatorShape elevator3;
    private ElevatorShape elevator4;
    private FloorShape floor1;
    private FloorShape floor2;
    private FloorShape floor3;
    private FloorShape floor4;
    private FloorShape floor5;
    private ButtonShape upButton;
    private ButtonShape downButton;

    public View() {
        elevator1 = new ElevatorShape("elev1");
        elevator2 = new ElevatorShape("elev2");
        elevator3 = new ElevatorShape("elev3");
        elevator4 = new ElevatorShape("elev4");
        floor1 = new FloorShape("floor5");
        floor2 = new FloorShape("floor4");
        floor3 = new FloorShape("floor3");
        floor4 = new FloorShape("floor2");
        floor5 = new FloorShape("floor1");

        upButton = new ButtonShape("up");
        downButton = new ButtonShape("down");

        upButton.moveHorizontal(350);
        downButton.moveHorizontal(350);
        downButton.moveVertical(80);

        boolean is = true;


        elevator1.moveHorizontal(-250);
        elevator2.moveHorizontal(-150);
        //elevator3.moveHorizontal(-100);
        elevator4.moveHorizontal(100);

        if (is == true){
            upButton.changeColor("yellow");
            elevator1.slowMoveVertical(-300, 10);
            int targetYPosition = 100;
            int positionThreshold = 5; // Adjust as needed

            if (Math.abs(elevator1.getyPosition() - targetYPosition) <= positionThreshold) {
                elevator1.slowMoveHorizontal(0, 10);
                upButton.changeColor("blue");
            }
        }





        floor2.moveVertical(120);
        floor3.moveVertical(240);
        floor4.moveVertical(340);
        floor5.moveVertical(440);


    }


    public static void main(String[] args) {
        //JFrame frame = new JFrame();
        //frame.setLayout(new GridLayout(2, 4, 10, 10));
        View view = new View();
        view.setVisible(true);

    }


    private void setVisible(boolean b) {
        floor1.makeVisible();
        floor2.makeVisible();
        floor3.makeVisible();
        floor4.makeVisible();
        floor5.makeVisible();
        elevator1.makeVisible();
        elevator2.makeVisible();
        elevator3.makeVisible();
        elevator4.makeVisible();
        upButton.makeVisible();
        downButton.makeVisible();

    }
}






