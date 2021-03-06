package main.java.com.nobelium.elevator;

import java.util.Comparator;
import java.util.PriorityQueue;

import main.java.com.nobelium.elevatorCS.PickupRequest;

class MaxHeapComparator implements Comparator<Integer> {
	public int compare(Integer x, Integer y){
        return y - x;
    }
}

public class Elevator {

	private final int id;
	private int curFloor, destFloor;
	private PriorityQueue<Integer> destinations;
	private boolean elevatorGoingUp, reqGoingUp, idle;
	
	public Elevator(Integer id) {
		this.id = id;
		this.curFloor = 0;
		this.destFloor = 0;
		this.idle = true;
		this.elevatorGoingUp = true;
		this.reqGoingUp = true;
		this.destinations = new PriorityQueue<>();
	}
	
	public int getCurFloor() {
		return curFloor;
	}

	public int getDestFloor() {
		return destFloor;
	}

	public boolean isElevatorGoingUp() {
		return elevatorGoingUp;
	}

	public boolean isReqGoingUp() {
		return reqGoingUp;
	}

	public boolean isIdle() {
		return idle;
	}
	
	public int getId() {
		return id;
	}
	
	/**
	 * Resets the state of the elevator
	 * @param curFloor
	 * @param destFloor
	 */
	public void setState(int curFloor, int destFloor) {
		this.curFloor = curFloor;
		this.destFloor = destFloor;
		destinations.clear();
		idle = curFloor == destFloor ? true : false;
		elevatorGoingUp = curFloor > destFloor ? false : true;
		reqGoingUp = elevatorGoingUp;
	}
	
	public void addPickupReq(PickupRequest req) {
		if(curFloor == req.getFloor()){
			if(destinations.isEmpty()) idle = true;
			return;
		}
		idle = false;
		reqGoingUp = req.isGoingUp();
		elevatorGoingUp = (curFloor > req.getFloor()) ? false : true;
		destinations = !(reqGoingUp) ? new PriorityQueue<>(new MaxHeapComparator()) : new PriorityQueue<>();
		destinations.add(req.getFloor());
		destFloor = destinations.peek();
	}

	// User sets dest after entering
	// Piggyback requests
	public void addDestFloor(int floor) {
		if(curFloor == floor){
			return;
		}
		if((isReqGoingUp() && floor < curFloor) || (!isReqGoingUp() && floor > curFloor)) {
			throw new IllegalArgumentException("Lift was summoned to serve a request which was in other direction");
		}
		if(isReqGoingUp() != isElevatorGoingUp() && curFloor != destFloor){
			throw new IllegalArgumentException("Lift hasn't reached the destination");
		}
		System.out.println("New destination added for ElevatorId: " + id + " new dest: " + floor + " old dest: " + destFloor);
		if(!destinations.isEmpty()) {
			Integer dest = destinations.peek();
			if(destinations.contains(floor))return;
		} else {
			destinations = (curFloor > floor) ? new PriorityQueue<>(new MaxHeapComparator()) : new PriorityQueue<>();
		}
		idle = false;
		destinations.add(floor);
		destFloor = destinations.peek();
		elevatorGoingUp = (curFloor > destFloor) ? false : true;
	}
	
	// Tells if a floor in between curFloor and destFloor
	public boolean isFloorInBetween(int floor) {
		return (curFloor <= floor && floor <= destFloor) || (destFloor <= floor && floor <= curFloor);
	}
	
	// Gives absolute distance between elevators current floor and the given floor
	public int getDistToFloor(int destFloor) {
		return Math.abs(this.curFloor - destFloor);
	}

	public void move() {
		if(curFloor == destFloor)idle = true;
		if(curFloor < destFloor) {
			curFloor = curFloor + 1;
		} else if(curFloor > destFloor) {
			curFloor = curFloor - 1; 
		}
		if(curFloor == destFloor){
			// remove top element from heap
			destinations.poll();
			if(!destinations.isEmpty()){
				destFloor = destinations.peek();
			}
		}
	}
}
