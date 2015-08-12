package main.java.com.nobelium.elevatorCS;

public class PickupRequest {

	public PickupRequest(int floor, int direction, int maxFloor) {
		this.floor = floor;
		this.goingUp = (direction >= 0) ? true : false;
		this.maxFloor = maxFloor;
		
		checkValidity();
	}
	
	void checkValidity(){
		if((floor == maxFloor-1 && isGoingUp()) || (floor == 0 && !isGoingUp())){
			throw new IllegalArgumentException("Invalid pick up request");
		}
	}
	
	private final int floor, maxFloor;
	
	public int getFloor() {
		return floor;
	}
	
	private final boolean goingUp;
	
	public boolean isGoingUp() {
		return goingUp;
	}
}
