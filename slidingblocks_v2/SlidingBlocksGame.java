package slidingblocks_v2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SlidingBlocksGame {

	private List<Node> open;
	private List<Node> closed;
	
	private void play() {
		open = new ArrayList<>();
		closed = new ArrayList<>();
		
		int[][] startingState = { {6,1,7}, {2,0,8}, {3,4,5}};
		int[][] goal = { {1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
		Node.setGoal(goal);
		Node startingNode = new Node(startingState, null, Node.Direction.UP);
		open.add(startingNode);
		
		Node current;
		while (true) {
			Collections.sort(open);
			current = open.get(0);
			
			open.remove(current);
			closed.add(current);
			
			if (current.isGoal()) {
				current.printPath();
				break;
			}
			
			List<Node> children = current.generateAndGetChildren();
			for (Node child : children) {
				if (closed.contains(child)) {
					continue;
				}
				
				open.add(child);
			}
		}
	}

	public static void main(String[] args) {
		new SlidingBlocksGame().play();
	}
}
