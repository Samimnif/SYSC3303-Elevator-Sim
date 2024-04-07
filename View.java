/** View.java
 *  Represents the graphical user interface (GUI) for an elevator simulation.
 *  The GUI displays the current state of the elevators and any errors that occur.
 * Number of floors and elevators is specified in the config file and any changes in the config file will update the GUI accordingly
 * @authors Sami Mnif, Omar Hamzat
 */
import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;


public class View extends JFrame {
    private JPanel[][] simBoard;
    private int[] elevatorPos;
    private final int NUM_FLOORS, NUM_ELEVATOR;
    private JPanel mainPanel, buttonPanel, errorPanel;
    private JTextArea errorArea;
    private String previousString = "";

    /**
     * Constructs a new View.
     * Initializes the GUI components and sets up the layout.
     */
    public View() {
        super("Elevator Simulation");
        this.setResizable(false);

        try {
            FileInputStream propsInput = new FileInputStream("config.properties");
            Properties prop = new Properties();
            prop.load(propsInput);

            NUM_ELEVATOR = Integer.parseInt(prop.getProperty("NUM_ELEVATOR"));
            NUM_FLOORS = Integer.parseInt(prop.getProperty("NUM_FLOORS"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //set initial elevator position
        elevatorPos = new int[NUM_ELEVATOR];
        for (int i = 0; i < NUM_ELEVATOR; i++) {
            elevatorPos[i] = 1;
        }

        mainPanel = new JPanel();
        JPanel FloorLablePanel = new JPanel();
        JPanel ElevatorLablePanel = new JPanel();
        FloorLablePanel.setLayout(new GridLayout(NUM_FLOORS, 1, 10, 10));
        mainPanel.setLayout(new GridLayout(NUM_FLOORS,NUM_ELEVATOR, 10, 10));
        this.add(mainPanel,BorderLayout.CENTER);
        //this.add(ElevatorLablePanel,BorderLayout.PAGE_END);
        this.add(FloorLablePanel,BorderLayout.WEST);
       /* buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        JLabel button1 = new JLabel("up");
        JLabel button2 = new JLabel("down");
        buttonPanel.add(button1);
        buttonPanel.add(button2);
        this.add(buttonPanel, BorderLayout.NORTH);*/

        
        for (int i = NUM_FLOORS; i > 0; i-- ){
            JPanel p = new JPanel();
            p.setSize(10,15);
            JLabel l = new JLabel("Floor"+ (i));
            p.add(l); // Add label to the panel
            FloorLablePanel.add(p);
        }

        // Initialize simBoard for th elevator positions
        this.simBoard = new JPanel[NUM_FLOORS][NUM_ELEVATOR];
        for (int i = 0; i < NUM_FLOORS; i++ ) {
            for (int j = 0; j < NUM_ELEVATOR; j++) {
                if (i == NUM_FLOORS-1) { // Check if it's the last panel
                    JPanel p = new JPanel();
                    p.setBackground(Color.BLUE);
                    p.setSize(10,15);
                    JLabel label = new JLabel("Elevator "+(j+1));
                    label.setForeground(Color.WHITE);
                    p.add(label); // Add label to the panel
                    this.simBoard[i][j] = p;
                    mainPanel.add(p);
                } else {
                    JPanel p = new JPanel();
                    p.setBackground(Color.WHITE);
                    p.setSize(10,15);
                    this.simBoard[i][j] = p;
                    // Set the background color for other panels as needed
                    mainPanel.add(p);
                }
            }
        }

        /*
        buttonPanel.setLayout(new GridLayout(NUM_FLOORS,NUM_ELEVATOR, 10, 10));
        Buttons = new JPanel[2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                Buttons[j] = ;
                buttonPanel.add(Buttons);
            }
        }

         */


        // Initialize errorPanel and errorArea for displaying errors
        errorPanel = new JPanel();
        errorPanel.setSize(1000,1000);
        errorArea = new JTextArea("Elevator Fault Errors:");
        errorArea.setForeground(Color.RED);
        errorArea.setEditable(false);
        errorArea.setFocusable(false);
        errorPanel.add(errorArea);


        this.add(errorPanel, BorderLayout.EAST);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1500,1000);
        //this.setLayout(new GridLayout(3,0));
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    /**
     * Updates the error message displayed in the errorArea text area.
     *
     * @param error the new error message to be added to the display
     */
    public void updateErrorText(String error){
        String previousError;
        previousError = errorArea.getText();
        errorArea.setText(previousError +"\n" + error);
    }

    /**
     * Updates the elevator positions and states on the GUI based on the provided elevatorList.
     *
     * @param elevatorList the list of elevators to update on the GUI
     */

    public void updateElevatorsBoard(ArrayList<Elevator> elevatorList){
        for (Elevator e : elevatorList){
            int eFloor = e.getCurrentFloor(), eID = e.getId(), people=0;
            String error = e.getError_Output();
            Boolean isIdle = e.isIdle(), isStuck = e.isStuck();
            if (e.getCurrentJob() != null && e.isLoaded()){
                people = e.getCurrentJob().capacity;
            }else{
                people = 0;
            }

            JPanel p = new JPanel();
            p.setBackground(Color.WHITE);
            p.setSize(10,15);
            simBoard[NUM_FLOORS - elevatorPos[eID-1]][eID-1] = p;

            JPanel newP = new JPanel();
            newP.setSize(10,15);
            JLabel label = new JLabel("Elevator "+eID+ ", People: "+people);
            if (isIdle){
                newP.setBackground(Color.YELLOW);
                label.setForeground(Color.BLACK);
            } else if(isStuck){
                newP.setBackground(Color.RED);
                label.setForeground(Color.WHITE);
            }
            else{
                newP.setBackground(Color.BLUE);
                label.setForeground(Color.WHITE);
            }
            newP.add(label); // Add label to the panel
            simBoard[NUM_FLOORS - eFloor][eID-1] = newP;
            elevatorPos[eID-1] = eFloor;

            // Update the GUI to reflect the changes
            mainPanel.removeAll(); // Clear the main panel
            // Re-add all panels from simBoard to the main panel
            for (int i = 0; i < NUM_FLOORS; i++) {
                for (int j = 0; j < NUM_ELEVATOR; j++) {
                    mainPanel.add(simBoard[i][j]);
                }
            }
            if (error != null && !e.isStuck() && !Objects.equals(previousString, error)){
                updateErrorText(error);
                previousString = error;
            } else if(e.isStuck() && !Objects.equals(previousString, error)){
                updateErrorText(error);
                previousString = error;
            }

            mainPanel.revalidate(); // Revalidate the main panel
            mainPanel.repaint(); // Repaint the main panel



        }
    }

/*
    public static void main(String[] args) {
        View view = new View();

    }
*/
}






