package slidingblocks_v2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class SlidingBlocksGame {

	private List<Node> open;
	private List<Node> closed;
	private int[][] startingState;
	private int[][] goal;
	
	private void play() {
		open = new ArrayList<>();
		closed = new ArrayList<>();
		readInput();
//		startingState = new int[][] { {0, 4, 6}, {5, 1, 3}, {2, 8, 7}};
//		goals = new int[][] { {1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
		Node.setGoal(goal);
		Node startingNode = new Node(startingState, null, null);
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
	
	private void readInput() {
		/* 8
		   1 2 3
		   4 5 6
		   0 7 8 */
		int n;
		int numbersOnRow;
		Scanner sc = new Scanner(System.in);
		n = sc.nextInt();
		numbersOnRow = (int) Math.sqrt(n + 1);
		startingState = new int[numbersOnRow][];
		goal = new int[numbersOnRow][];
		
		int counter = 1;
		for (int i = 0; i < numbersOnRow; i++) {
			startingState[i] = new int[numbersOnRow];
			goal[i] = new int[numbersOnRow];
			for (int j = 0; j < numbersOnRow; j++) {
				startingState[i][j] = sc.nextInt();
				goal[i][j] = counter++;
			}
		}
		goal[numbersOnRow - 1][numbersOnRow - 1] = 0; // last block must be 0
		sc.close();
	}


	public static void main(String[] args) {
		new SlidingBlocksGame().play();
	}
}