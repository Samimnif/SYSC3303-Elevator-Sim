Description:
-----------

This project contains 12 java classes, 1 UML diagram, 1 sequence diagram, and 1 ReadMe.txt file. In this project, we created 3 threads, one for each
subsystem (Elevator, Floor, and Scheduler). The floor reads the input given to it, then each line input will be sent to the scheduler. The elevator will make calls
to the Scheduler which will then reply when there is work to be done. The Elevator will then send the data back to the Scheduler who will then send it back to the Floor.

The Project is made up of one file:
    Scheduler.java            A single Java script
    UnitTests.java            A single Java script
    FloorSubSystem.java       A single Java script
    Job.java                  A single Java script
    Button.java               A single Java script
    Door.java                 A single Java script
    Elevator.java             A single Java script
    ElevatorSubsystem.java    A single Java script
    Floor.java                A single Java script
    Main.java                 A single Java script
    Lamp.java                 A single Java script
    Motor.java                A single Java script
    UML diagram               A single image of a UML diagram
    Sequence diagram          A single image of a UML diagram

Installation:
-------------
To be able to run the program, you should have Java 15.0.0 or later installed on your
computer.

Usage:
------
>Scheduler.java: This class is the communication channel between the Elevator and the floor subsystem.
>FloorSubSystem.java: This class represents the floor subsystem, which sends and receives input to the scheduler.
>Job.java: The Job class takes the input parameters that the elevator has.
>Button.java: This class represents the button in the Elevator.
>Door.java: This class represents the elevator doors, which has a functionality of opening and closing.
>Elevator.java: This class collects all the functionalities of the elevator including the buttons, motor, lamp, and door.
>ElevatorSubsystem.java: This class represents the Elevator subsystem, which sends and receives input to the scheduler.
>Lamp.java:This class performs the functionality of the Elevator lamp, which is used by the Elevator Buttons.
>Motor.java: This class performs the functionality of the Elevator motor.

Credits:
--------
Sami Mnif: ElevatorSubsystem.java, Job.java, Elevator.java, Scheduler.java, FloorSubsystem.java
Jalal Mourad:Elevator.java Button.java,Lamp.java,Scheduler.java,Motor.java,README.txt,Door.java
Omar Hamzat: Job.java, Elevator.java, Main.java, UML diagrams
Muaadh Ali: Floor.java, FloorSubsystem.java, Job.java
Jordan Bayne: UnitTests.java, Motor.java, Door.java

Copyright 2024 Jalal Mourad, Sami Mnif, Omar Hamzat, Jordan Bayne, Muaadh Ali