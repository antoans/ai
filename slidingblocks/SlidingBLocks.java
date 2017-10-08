package slidingblocks;
import java.util.Collection;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

public class SlidingBLocks {

	private SortedSet<BlocksState> open;
	private SortedSet<BlocksState> closed;
	private BlocksState startingState;
	private BlocksState winState;
	
	private void play() {
		readInput();
		open = new TreeSet<>();
		closed = new TreeSet<>();
		
		open.add(startingState);

		BlocksState current;
		while (true) {
			current = open.first();
			open.remove(current);
			closed.add(current);
			
			if (current.equals(winState)) {
				System.out.println(current.getHistory());
				break;
			}
			
			Collection<BlocksState> neighbours = current.getNeighbours();
			for (BlocksState state : neighbours) {
				if (closed.contains(state)) {
					continue;
				}

				BlocksState oldState = findElementInSet(open, state);
				if (oldState != null && oldState.compareTo(state) == 1) {
					//open already contains this state
					//found a better route to an existing node, so replace it
					open.remove(oldState);
					
				}
				open.add(state);
			}
			
		}
		
	}
	
	private <T> T findElementInSet(Collection<T> c, T o) {
		for (T current : c) {
			if (current.equals(o)) {
				return current;
			}
		}
		return null;
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
		int[][] startingBlocks = new int[numbersOnRow][];
		int[][] winCondition = new int[numbersOnRow][];
		
		int counter = 1;
		for (int i = 0; i < numbersOnRow; i++) {
			startingBlocks[i] = new int[numbersOnRow];
			winCondition[i] = new int[numbersOnRow];
			for (int j = 0; j < numbersOnRow; j++) {
				startingBlocks[i][j] = sc.nextInt();
				winCondition[i][j] = counter++;
			}
		}
		winCondition[numbersOnRow - 1][numbersOnRow - 1] = 0; // last block must be 0
		sc.close();
		
		startingState = new BlocksState(startingBlocks, winCondition, null, 0, "");
		winState = new BlocksState(winCondition, winCondition, null, 0, "");
	}

	public static void main(String[] args) {
		new SlidingBLocks().play();
	}
}
