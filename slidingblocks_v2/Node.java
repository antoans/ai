package slidingblocks_v2;
import static slidingblocks_v2.Node.Direction.DOWN;
import static slidingblocks_v2.Node.Direction.LEFT;
import static slidingblocks_v2.Node.Direction.RIGHT;
import static slidingblocks_v2.Node.Direction.UP;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 * A square with size N, containing numbers from 0 (incl.) to N (incl.).<br>
 * The square if the numbers are in this order:<br>
 * If N=8:<br>
 * 1 2 3<br>
 * 4 5 6<br>
 * 7 8 0 <br>
 */
public class Node implements Comparable<Node> {
	
	private static int[][] goal;
	private int[][] state;
	private Node parent;
	private List<Node> children;
	
	private int gCost;//Distance from start
	private int hCost;//Manhattan distance
	private int fCost;//Sum of g and h
	
	private static Direction[] ALL_DIRECTIONS = Direction.values();
	
	/** Used for tracking the path. */
	private Direction direction;
	
	public static void setGoal(int[][] goal) {
		Node.goal = goal;
	}

	public Node(int[][] state, Node parent, Direction direction) {
		this.state = state;
		this.parent = parent;
		this.direction = direction;
		gCost = parent != null ? parent.gCost + 1 : 0;
		hCost = calculateHCost();
		fCost = hCost + gCost;
		//TODO Can add "emptyIndex" to optimize.
	}
	
	public List<Node> generateAndGetChildren() {
		children = new ArrayList<>();
		
		int[] coordsOfEmpty = getYXOfNumberFromState(0, state);
		for (int i = 0; i < ALL_DIRECTIONS.length; i++) {
			//fills children
			tryToMakeMove(ALL_DIRECTIONS[i], coordsOfEmpty);
		}
		
		return children;
	}

	private int calculateHCost() {
		int xDistance = 0;
		int yDistance = 0;
		
		int[] coordinatesInGoal;
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state.length; j++) {
				if(state[i][j] == 0) {
					continue;
				}
				coordinatesInGoal = getYXOfNumberFromState(state[i][j], goal);
				xDistance += Math.abs(coordinatesInGoal[1] - j);
				yDistance += Math.abs(coordinatesInGoal[0] - i);
			}
		}
		
		return xDistance + yDistance;
	}
	
	/** @return Array with 2 int-s.<br>
	 * 			First element is the Y-coordinate of <b>number</b>.<br>
	 * 			Second element is the X-coordinate of <b>number</b>. */
	private int[] getYXOfNumberFromState(int number, int[][] state) {
		for (int y = 0; y < state.length; y++) {
			for (int x = 0; x < state.length; x++) {
				if (state[y][x] == number) {
					return new int[] {y, x};
				}
			}
		}
		return null;
	}
	
	private void tryToMakeMove(Direction direction, int[] coordsOfEmpty) {
		if (canMove(direction, coordsOfEmpty)) {
			children.add( makeMove(direction, coordsOfEmpty) );			
		}
	}
	
	private boolean canMove(Direction direction, int[] coordsOfEmpty) {
		switch (direction) {
		case UP:
			if (DOWN.equals(this.direction) || coordsOfEmpty[0] == state.length - 1) {
				return false;
			}
			break;
		case DOWN:
			if (UP.equals(this.direction) || coordsOfEmpty[0] == 0) {
				return false;
			}
			break;
		case LEFT:
			if (RIGHT.equals(this.direction) || coordsOfEmpty[1] == state.length - 1) {
				return false;
			}
			break;
		case RIGHT:
			if (LEFT.equals(this.direction) || coordsOfEmpty[1] == 0) {
				return false;
			}
			break;

		default:
			break;
		}
		return true;
	}

	private Node makeMove(Direction direction, int[] coordsOfEmpty) {
		int deltaX = 0;
		int deltaY = 0;
		switch (direction) {
		case RIGHT:
			deltaX = -1;
			break;
		case LEFT:
			deltaX = 1;
			break;
		case UP:
			deltaY = 1;
			break;
		case DOWN:
			deltaY = -1;
			break;
		default:
			break;
		}
		
		int emptyY = coordsOfEmpty[0];
		int emptyX = coordsOfEmpty[1];
		
		int[][] newState = copyArray(this.state);

		newState[emptyY][emptyX] = this.state[emptyY + deltaY][emptyX + deltaX];
		newState[emptyY + deltaY][emptyX + deltaX] = this.state[emptyY][emptyX];
		
		return new Node(newState, this, direction);
	}

	private int[][] copyArray(int[][] oldState) {
		int len = oldState.length;
		int[][] ret = new int[len][];
		for (int i = 0; i < len; i++) {
			ret[i] = new int[len];
			for (int j = 0; j < len; j++) {
				ret[i][j] = oldState[i][j];
			}
		}
		return ret;
	}

	
	@Override
	public int compareTo(Node o) {
		if (fCost > o.fCost) {
			return 1;
		} else if (fCost < o.fCost) {
			return -1;
		} else {
			if (hCost > o.hCost) {
				return 1;
			} else if (hCost < o.hCost) {
				return -1;
			}
		}
		return 0;
	}	
	
	public void printPath() {
		Deque<Direction> stack = new ArrayDeque<>();
		stack.push(direction);
		
		Node current = this;
		while (current.parent != null) {
			stack.push(current.parent.direction);
			current = current.parent;
		}
		
		int size = stack.size();
		for (int i = 0; i < size ; i++) {
			if (i == 0) {
				stack.pop();
				continue;//TODO : ...
			}
			System.out.println(stack.pop());
		}
		System.out.println();
		System.out.println("Total steps: " + (size - 1));
	}
	
	@Override
	public String toString() {
		return "[" + Arrays.deepToString(state) + ", " + direction 
				+ ", f=" + fCost + ", h=" + hCost + ", g=" + gCost + "]";
	}
	
	public static enum Direction {
		UP, DOWN, LEFT, RIGHT
	}

	public boolean isGoal() {
		for (int y = 0; y < state.length; y++) {
			for (int x = 0; x < state.length; x++) {
				if (state[y][x] != goal[y][x]) {
					return false;					
				}
			}
		}
		return true;
	}
}
