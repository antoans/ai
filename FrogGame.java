import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class FrogGame {

	private final char EMPTY_FIELD = '_';
	private Deque<String> stack = new ArrayDeque<>();
	private boolean isGameFinished = false;
	private String winCondition;
	
	public static void main(String[] args) {
		// Since I'm using recursion, the size of the game it's able to play
		// depends on the stack size. (eg. VM arguments: -Xss100m)
		new FrogGame().play();
	}

	private void play() {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		sc.close();
		
		// Build the strings, eg.: n = 2, start: >>_<<, end: <<_>>
		StringBuilder start = new StringBuilder();
		StringBuilder end = new StringBuilder();
		for (int i = 0; i < n; i++) {
			start.append(">");
			end.append("<");
		}
		start.append(EMPTY_FIELD);
		end.append(EMPTY_FIELD);
		for (int i = n + 1; i < 2 * n + 1; i++) {
			start.append("<");
			end.append(">");
		}
		
		winCondition = end.toString();
		check(start.toString());
		
//		System.out.println(stack.size());
		Object[] arr = stack.toArray();
		for (int i = arr.length - 1; i >= 0; i--) {
			System.out.println(arr[i]);
		}
	}

	/**
	 * Checks if the current positions of the 'frogs' wins the game
	 * If not, tries to move a frog, and performs 'check(new_positions)'
	 */
	private void check(String field) {
		stack.push(field);
		if ( field.equals(winCondition) ) {
			isGameFinished = true;
			return;
		}
		int emptyIndex = field.indexOf(EMPTY_FIELD);
		if (canTellIfItsWrong(field, emptyIndex) ) {
			//If we can tell earlier that the current scenario is wrong, don't bother continuing
			stack.pop();
			return;
		}
		
		tryToMove(-2, field);
		tryToMove(-1, field);
		tryToMove(1, field);
		tryToMove(2, field);
		
		if (!isGameFinished) {
			stack.pop();			
		}
	}

	private boolean canTellIfItsWrong(String field, int emptyIndex) {
		if (field.indexOf("><<_") != -1 ||
				field.indexOf("_>><") != -1 ||
				field.indexOf("_>>><") != -1 ||
				field.indexOf("><<<_") != -1) {
			return true;
		}
		return false;
	}

	private void tryToMove(int pos, String field) {
		if (isGameFinished) {
			return;
		}
		int emptyIndex = field.indexOf(EMPTY_FIELD);
		if (emptyIndex + pos >= 0 && emptyIndex + pos < field.length()) {//there is a frog there
			if (pos < 0 && field.charAt(emptyIndex + pos) == '>' || //on the left of the empty space, facing right
					pos > 0 && field.charAt(emptyIndex + pos) == '<') { //on the right of the empty space, facing left
				//move the frog
				char[] chArr = field.toCharArray();
				chArr[emptyIndex] = pos < 0 ? '>' : '<';
				chArr[emptyIndex + pos] = '_';
				
				check(String.valueOf(chArr));
			}
		}
		
	}

	
}
