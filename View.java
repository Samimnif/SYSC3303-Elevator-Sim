import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;


public class View extends JFrame{
    private JPanel[][] simBoard;
    private int[] elevatorPos;
    private final int NUM_FLOORS, NUM_ELEVATOR;

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

        JPanel mainPanel = new JPanel();
        JPanel FloorLablePanel = new JPanel();
        JPanel ElevatorLablePanel = new JPanel();
        FloorLablePanel.setLayout(new GridLayout(NUM_FLOORS, 1, 10, 10));
        mainPanel.setLayout(new GridLayout(NUM_FLOORS,NUM_ELEVATOR, 10, 10));
        this.add(mainPanel,BorderLayout.CENTER);
        //this.add(ElevatorLablePanel,BorderLayout.PAGE_END);
        this.add(FloorLablePanel,BorderLayout.WEST);

        /*
        JMenuBar menubar = new JMenuBar();
        this.setJMenuBar(menubar);
        JMenu menu = new JMenu("Options");

        JMenuItem back = new JMenuItem("Back");
        JMenuItem submit = new JMenuItem("Submit");
        */
        for (int i = NUM_FLOORS; i > 0; i-- ){
            JPanel p = new JPanel();
            p.setSize(10,15);
            JLabel l = new JLabel("Floor"+ (i));
            p.add(l); // Add label to the panel
            FloorLablePanel.add(p);
        }

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


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000,1000);
        //this.setLayout(new GridLayout(3,0));
        this.setLocationRelativeTo(null);
        this.setVisible(true);


    }

    public void updateElevatorsBoard(ArrayList<Elevator> elevatorList){
        for (Elevator e : elevatorList){
            int eFloor = e.getCurrentFloor(), eID = e.getId();

            JPanel p = new JPanel();
            p.setBackground(Color.WHITE);
            p.setSize(10,15);
            simBoard[elevatorPos[eID-1]][eID-1] = p;

            JPanel newP = new JPanel();
            newP.setBackground(Color.BLUE);
            newP.setSize(10,15);
            JLabel label = new JLabel("Elevator "+eID);
            label.setForeground(Color.WHITE);
            newP.add(label); // Add label to the panel
            simBoard[eFloor][eID-1] = p;
        }
    }

/*
    public static void main(String[] args) {
        //JFrame frame = new JFrame();
        //frame.setLayout(new GridLayout(2, 4, 10, 10));
        View view = new View();
        //view.setVisible(true);

    }
*/
}






