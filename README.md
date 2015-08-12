Elevator Control System
=======================
Build Instruction:
------------------
The simplest way to build is to clone the repository - git clone https://github.com/nobelium/elevator-control-system/
Move to source directory - cd src
Compile - javac ElevatorDriver.java
Run - java ElevatorDriver

(Or)

Import the project into Eclipse and click run button

Assumptions made:
-----------------
- The users of the elevator On-board or Off-board the elevator as soon as the elevator reaches the floor.
- In each step function is long enough for the elevator to travel one floor

Scheduling Algorithm:
---------------------
The scheduling Algorithm uses a two stage approach. This algorithm improves over the naive First-In-First-Out (FIFO) by adding a Piggyback stage. Each pickup request is checked if its possible to piggyback before its added to FIFO request queue.
<img src="/img/blockdiagram.jpg?raw=true" alt="Drawing" style="width: 200px;"/>

Criteria for piggybacking: 
-------------------------
The image below shows the algorithm and the type of priority queue used for each case. The First column in the image shows the direction in which the elevator is moving and the second column shows the direction of the request it is going to serve. The third column gives the criteria for piggybacking if a new pickup request is issued.
<img src="/img/piggyback.jpg?raw=true" alt="Drawing" style="width: 200px;"/>
