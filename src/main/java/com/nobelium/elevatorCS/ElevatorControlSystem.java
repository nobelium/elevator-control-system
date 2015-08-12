package main.java.com.nobelium.elevatorCS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.java.com.nobelium.elevator.Elevator;
import main.java.com.nobelium.elevatorCS.PickupRequest;

public class ElevatorControlSystem implements ControlSystem {

	private List<Elevator> elevators = new ArrayList<>();
	// FIFO req queue
	private List<PickupRequest> requests = new ArrayList<>();
	
	private final int noOfElevators, noOfFloors;
	
	void checkFloorValidity(int floorVal){
		if(floorVal < 0 || floorVal >= noOfFloors)
			throw new IllegalArgumentException("Invalid floor value");
	}
	
	void checkElevatorValidity(int elevatorVal){
		if(elevatorVal < 0 || elevatorVal >= noOfElevators)
			throw new IllegalArgumentException("Invalid elevator ID");
	}
	
	public ElevatorControlSystem(int noOfElevators, int noOfFloors) {
		this.noOfElevators = noOfElevators;
		this.noOfFloors = noOfFloors;
		for(int i=0;i<noOfElevators;i++){
			Elevator elevator = new Elevator(i);
			elevators.add(elevator);
		}
	}
	
	@Override
	public List<List<Integer>> status() {
		List<List<Integer>> statuses = new ArrayList<>();
		for(Elevator elevator: elevators) {
			statuses.add(Arrays.asList(elevator.getId(), elevator.getCurFloor(), elevator.getDestFloor()));
		}
		return statuses;
	}

	/**
	 * aka update
	 */
	@Override
	public void setState(int id, int curFloor, int destFloor) {
		checkElevatorValidity(id);
		checkFloorValidity(curFloor);
		checkFloorValidity(destFloor);
		for(Elevator elevator: elevators) {
			if(elevator.getId() == id) {
				elevator.setState(curFloor, destFloor);
			}
		}
	}

	// Used to set direction of an elevator once the user enters
	@Override
	public void setDest(int id, int destFloor) {
		checkElevatorValidity(id);
		checkFloorValidity(destFloor);
		for(Elevator elevator: elevators) {
			if(elevator.getId() == id) {
				elevator.addDestFloor(destFloor);
				System.out.println("Setting dest elevatorId: " + id +" floor: " + destFloor );
			}
		}
	}

	@Override
	public void pickup(int floor, int direction) {
		checkFloorValidity(floor);
		PickupRequest pickupReq = new PickupRequest(floor, direction, noOfFloors);
		Elevator elevator = findPiggybackElevator(pickupReq);
		if(elevator != null){
			elevator.addDestFloor(pickupReq.getFloor());
			System.out.print("\nPiggyback possible for floor: " + pickupReq.getFloor() + " isGoingup: " + pickupReq.isGoingUp() + " added to elevatorID: " + elevator.getId());
		} else {
			requests.add(pickupReq);
			System.out.print("\nAdding to FIFO queue. Piggyback NOT possible for floor: " + pickupReq.getFloor() + " isGoingup: " + pickupReq.isGoingUp());
		}
	}

	private Elevator findPiggybackElevator(PickupRequest pickupReq) {
		Elevator result = null;
		Integer closest = Integer.MAX_VALUE;
		for(Elevator elevator: elevators) {
			if(elevator.isElevatorGoingUp() == pickupReq.isGoingUp() && elevator.isReqGoingUp() == pickupReq.isGoingUp()) {
				if(elevator.isFloorInBetween(pickupReq.getFloor())) {
					if(elevator.getDistToFloor(pickupReq.getFloor()) < closest) {
						result = elevator;
					}
				}
			}
		}
		return result;
	}

	@Override
	public void step() {
		// Move all elevators by one step and update idle logic
		for(Elevator elevator: elevators) {
			elevator.move();
		}

		List<Integer> processed = new ArrayList<>(); 
		// Process FIFO queue
		for(int i=0;i<requests.size();i++) {
			PickupRequest req = requests.get(i);
			Elevator closestElevator = findIdleElevator(req.getFloor());
			if(closestElevator != null){
				closestElevator.addPickupReq(req);
				System.out.print("\nFIFO req: " + req.getFloor() + " isGoingup: " + req.isGoingUp() + " assigned to elevatorID: " + closestElevator.getId());
				processed.add(i);
			} else {
				closestElevator = findPiggybackElevator(req);
				if(closestElevator != null){
					closestElevator.addDestFloor(req.getFloor());
					System.out.print("\nPiggyback possible for floor: " + req.getFloor() + " isGoingup: " + req.isGoingUp() + " added to elevatorID: " + closestElevator.getId());
					processed.add(i);
				}
			}
		}
		
		// Remove all processed requests
		for(int i=processed.size()-1;i>=0;i--) {
			requests.remove(i);
		}
	}

	private Elevator findIdleElevator(int destFloor) {
		int closestDist = Integer.MAX_VALUE;
		Elevator closestElevator = null;
		for(Elevator elevator: elevators){
			if(elevator.isIdle()){
				if(elevator.getDistToFloor(destFloor) < closestDist){
					closestElevator = elevator;
				}
			}
		}
		return closestElevator;
	}

}
