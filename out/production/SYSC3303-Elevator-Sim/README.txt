# Elevator Simulator

Description:
-----------

This project contains 14 java classes, 1 UML diagram, 1 sequence diagram 1 ReadMe.txt file.
In this deliverable, we added RPC communication between the FloorSubsystem, ElevatorSubsystem, and Scheduler.

The Project is made up of one file:
Scheduler.java A single Java script
UnitTests.java A single Java script
FloorSubSystem.java A single Java script
Job.java A single Java script
Button.java A single Java script
Door.java A single Java script
Elevator.java A single Java script
ElevatorSubsystem.java A single Java script
Floor.java A single Java script
Main.java A single Java script
Lamp.java A single Java script
Motor.java A single Java script
SchedulerStateMachine.java A single Java script
ElevatorSubsystemStateMachine.java A single Java script
UML diagram A single image of a UML diagram
Sequence diagram A single image of a UML diagram
State machine diagram A single image of a State machine diagram

Installation:
-------------
To be able to run the program, you should have Java 15.0.0 or later installed on your
computer.

Usage:
------
> `Scheduler.java`: This class is the communication channel between the Elevator and the floor subsystem.<br>
> `FloorSubSystem.java`: This class represents the floor subsystem, which sends and receives input to the scheduler.<br>
> `Job.java`: The Job class takes the input parameters that the elevator has.<br>
> `Button.java`: This class represents the button in the Elevator.<br>
> `Door.java`: This class represents the elevator doors, which has a functionality of opening and closing.<br>
> `Elevator.java`: This class collects all the functionalities of the elevator including the buttons, motor, lamp, and
> door.<br>
> `ElevatorSubsystem.java`: This class represents the Elevator subsystem, which sends and receives input to the
> scheduler.<br>
> `Lamp.java`:This class performs the functionality of the Elevator lamp, which is used by the Elevator Buttons.<br>
> `Motor.java`: This class performs the functionality of the Elevator motor.<br>
> `ElevatorSubsystemStateMachine.java`: This class contains the functionality of the Elevator system State machine.<br>
> `SchedulerStateMachine`: This class contains the functionality of the Scheduler system State machine.<br>

Credits:
--------

| Classes                              | Iteration 1                           | Iteration 2                                                    | Iteration 3                         | Iteration 4                          | Iteration 5             |
|--------------------------------------|---------------------------------------|----------------------------------------------------------------|-------------------------------------|--------------------------------------|-------------------------|
| `Scheduler.java`                     | Sami Mnif, Jalal Mourad               |                                                                | Sami Mnif, Omar Hamzat              | Sami Mnif, Omar Hamzat               | Sami Mnif, Jalal Mourad |
| `ElevatorSubsystem.java`             | Sami Mnif                             |                                                                | Sami Mnif, Muaadh Ali               |                                      |                         |
| `FloorSubsystem.java`                | Sami Mnif, Muaadh Ali                 |                                                                | Sami Mnif, Jalal Mourad,            | Muaadh Ali                           | Muaadh Ali, Sami Mnif   |
| `Elevator.java`                      | Sami Mnif, Jalal Mourad, Omar Hamzat, |                                                                | Sami Mnif, Muaadh Ali, Jordan Bayne | Jordan Bayne,Sami Mnif, Jalal Mourad | Jordan Bayne,Sami Mnif  |
| `Button.java`                        | Jalal Mourad                          |                                                                |                                     |                                      |                         |
| `Door.java`                          | Jalal Mourad, Jordan Bayne            |                                                                |                                     | Jordan Bayne                         |                         |
| `Lamp.java`                          | Jalal Mourad                          |                                                                |                                     | Jordan Bayne                         |                         |
| `Motor.java`                         | Jalal Mourad, Jordan Bayne            |                                                                |                                     |                                      |                         |
| `Floor.java`                         | Muaadh Ali                            |                                                                |                                     |                                      |                         |
| `SchedulerStateMachine.java`         | NA                                    | Sami Mnif, Jalal Mourad, Omar Hamzat, Muaadh Ali, Jordan Bayne |                                     |                                      |                         |
| `ElevatorSubsystemStateMachine.java` | NA                                    | Sami Mnif, Jalal Mourad, Omar Hamzat, Muaadh Ali, Jordan Bayne | Muaadh Ali                          |                                      |                         |
| `UnitTests.java`                     | Jordan Bayne                          |                                                                | Sami Mnif, Jalal Mourad, Muaadh Ali |                                      |                         |
| `Job.java`                           | Sami Mnif, Omar Hamzat, Muaadh Ali    |                                                                |                                     | Muaadh Ali                           |                         |
| `Main.java`                          | Omar Hamzat                           |                                                                | Sami Mnif, Omar Hamzat              |                                      |                         |
| `View.java`                          | NA                                    | NA                                                             | NA                                  | NA                                   | Omar Hamzat, Sami Mnif  |

## Iteration1:

* Sami Mnif: ElevatorSubsystem.java, Job.java, Elevator.java, Scheduler.java, FloorSubsystem.java
* Jalal Mourad:Elevator.java Button.java,Lamp.java,Scheduler.java,Motor.java,README.txt,Door.java
* Omar Hamzat: Job.java, Elevator.java, Main.java, UML diagrams
* Muaadh Ali: Floor.java, FloorSubsystem.java, Job.java
* Jordan Bayne: UnitTests.java, Motor.java, Door.java

## Iteration 2:

* Sami Mnif:SchedulerStateMachine, ElevatorSubsystemStateMachine, UML diagram
* Jalal Mourad:SchedulerStateMachine, ElevatorSubsystemStateMachine, README.txt, State machine diagram
* Omar Hamzat:SchedulerStateMachine, ElevatorSubsystemStateMachine, Sequence diagram
* Muaadh Ali:SchedulerStateMachine, ElevatorSubsystemStateMachine
* Jordan Bayne:SchedulerStateMachine, ElevatorSubsystemStateMachine

## Iteration 3:

* Sami Mnif:UML diagram, Elevator.java, ElevatorSubsystem.java, Scheduler.java, UnitTests.java
* Jalal Mourad: README.txt, StateMachine diagram, FloorSubsystem.java, UnitTests.java
* Omar Hamzat:Sequence diagram, Scheduler.java
* Muaadh Ali: ElevatorSubsystemStateMachine.java, UnitTests.java
* Jordan Bayne: Elevator.java

<hr>
Copyright 2024 Jalal Mourad, Sami Mnif, Omar Hamzat, Jordan Bayne, Muaadh Ali