package slidingblocks;

import java.util.ArrayList;
import java.util.Collection;
import static slidingblocks.BlocksState.Direction.*;

public class BlocksState implements Comparable<BlocksState>{
	
	/** The 'distance' from the starting point to this one. */
	private int gCost;
	
	/** The 'distance' from this point to the final. */
	private int hCost;
	
	/** gCost + hCost */
	private int fCost;
	
	private Direction direction;
	private int[][] state;
	private int[][] winCondition;
	private String history;

	public String getHistory() {
		return history;
	}

	public BlocksState(int[][] state, int[][] winCondition, Direction direction, int gCost, String history) {
		this.state = state;
		this.winCondition = winCondition;
		this.direction = direction;
		this.gCost = gCost;
		hCost = calculateHCost();
		fCost = gCost + hCost;
		this.history = history;
	}

	private int calculateHCost() {
		int xDistance = 0;
		int yDistance = 0;
		
		int[] targetYX;
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state.length; j++) {
				if(state[i][j] == 0) {
					continue;
				}
				targetYX = getYXFromState(state[i][j], winCondition);
				xDistance += Math.abs(targetYX[1] - j);
				yDistance += Math.abs(targetYX[0] - i);
			}
		}
		
		return xDistance + yDistance;
	}

	/** @return Array with 2 elements.<br>
	 * 			First element is the Y-coordinate of the empty block.<br>
	 * 			Second element is the X-coordinate of the empty block. */
	private int[] getYXFromState(int currentNum, int[][] state) {
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state.length; j++) {
				if (state[i][j] == currentNum) {
					return new int[] {i, j};
				}
			}
		}
		return null;
	}
	
	public Collection<BlocksState> getNeighbours() {
		Collection<BlocksState> neighbours = new ArrayList<>();
		int[] emptyYX = getYXFromState(0, state);
		
		if (!LEFT.equals(direction)) {// no need to move right, if the last move was left
			//take 'right' neighbour ( '1 0 2' -> '0 1 2')
			if (emptyYX[1] != 0) {
				neighbours.add(new BlocksState(move(state, RIGHT, emptyYX),
						winCondition, RIGHT, gCost + 1, history + "right "));
			}
		}
		
		if (!RIGHT.equals(direction)) {
			//take 'left' neighbour ( '1 0 2' -> '1 2 0')
			if (emptyYX[1] != state.length - 1) {
				neighbours.add(new BlocksState(move(state, LEFT, emptyYX),
						winCondition, LEFT, gCost + 1, history + "left "));
			}
		}
		
		if (!UP.equals(direction)) {
			//take 'down' neighbour    1    0
			//                         0 -> 1
			//                         2    2
			if (emptyYX[0] != 0) {
				neighbours.add(new BlocksState(move(state, DOWN, emptyYX),
						winCondition, DOWN, gCost + 1, history + "down "));
			}
		}
		
		if (!DOWN.equals(direction)) {
			//take 'up' neighbour      1    1
			//                         0 -> 2
			//                         2    0
			if (emptyYX[0] != state.length - 1) {
				neighbours.add(new BlocksState(move(state, UP, emptyYX),
						winCondition, UP, gCost + 1, history + "up "));
			}
		}
		
		return neighbours;
	}	

	private int[][] move(int[][] oldState, Direction dir, int[] emptyYX) {
		int deltaX = 0;
		int deltaY = 0;
		switch (dir) {
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
		
		int emptyY = emptyYX[0];
		int emptyX = emptyYX[1];
		
		int[][] newState = copyArray(oldState);

		newState[emptyY][emptyX] = oldState[emptyY + deltaY][emptyX + deltaX];
		newState[emptyY + deltaY][emptyX + deltaX] = oldState[emptyY][emptyX];
		
		return newState;
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
	public int hashCode() {
		return state.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof BlocksState) {
			BlocksState other = (BlocksState) obj;
			for (int i = 0; i < state.length; i++) {
				for (int j = 0; j < state.length; j++) {
					if (state[i][j] != other.state[i][j]) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "BlocksState [blocks=" + state + ", gCost=" + gCost + ", hCost=" + hCost + ", fCost=" + fCost + "]";
	}

	@Override
	public int compareTo(BlocksState o) {
		if (fCost < o.fCost) {
			return -1;
		} else if (fCost > o.fCost) {
			return 1;
		}
		
		if (hCost < o.hCost) {
			return -1;
		} else if (hCost > o.hCost) {
			return 1;
		}
		
		return 0;
	}
	
	public static enum Direction {
		UP, DOWN, LEFT, RIGHT;
		
		public boolean isOpposite() {
			return false;//TODO
		}
	}
	
	
}
