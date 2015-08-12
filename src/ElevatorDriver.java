import java.util.List;

import main.java.com.nobelium.elevatorCS.ElevatorControlSystem;

public class ElevatorDriver {
	private static void printStatus(List<List<Integer>> statuses) {
		System.out.println("\n");
		for(List<Integer> state: statuses) {
			System.out.println("status: id: " + state.get(0) 
					+ " curFloor: " + state.get(1)
					+ " destFloor: " + state.get(2));
		}
		System.out.println("\n");
	}
	
	public static void main(String[] args) {
		ElevatorControlSystem ecs = new ElevatorControlSystem(2, 20);
		
		ecs.pickup(2, 1);
		ecs.pickup(6, 1);
		ecs.step();
		printStatus(ecs.status());
		ecs.pickup(5, -1);
		ecs.step();
		printStatus(ecs.status());
		ecs.step();
		printStatus(ecs.status());
		ecs.step();
		printStatus(ecs.status());
		ecs.step();
		printStatus(ecs.status());
		ecs.step();
		printStatus(ecs.status());
		//ecs.setDest(1, 1); // You should receive illegal argument exception - lift hasn't reached dest
		ecs.step();
		printStatus(ecs.status());
		ecs.setDest(1, 1);  
		ecs.step();
		printStatus(ecs.status());
		ecs.pickup(2, -1); // You should piggyback 
		//ecs.pickup(2, 1);
		ecs.step();
		printStatus(ecs.status());
		ecs.step();
		printStatus(ecs.status());
		ecs.step();
		printStatus(ecs.status());
		ecs.step();
		printStatus(ecs.status());
		
	}
}
