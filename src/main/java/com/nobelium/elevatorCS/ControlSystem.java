package main.java.com.nobelium.elevatorCS;

import java.util.List;

public interface ControlSystem {
	public List<List <Integer>> status();
	
	public void setState(int id, int curFloor, int destFloor);
	
	public void setDest(int id, int destFloor);
	
	public void pickup(int floor, int direction);
	
	public void step();
}
