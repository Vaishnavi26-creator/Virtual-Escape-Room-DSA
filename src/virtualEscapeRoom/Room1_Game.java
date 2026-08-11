package virtualEscapeRoom;
import java.util.*;

public class Room1_Game {
	public static boolean start(Scanner scanner) {

		// Define the initial stack (top to bottom)
		Stack<String> plateStack = new Stack<>();
		plateStack.push("EARTH");
		plateStack.push("WIND");
		plateStack.push("WATER");
		plateStack.push("FIRE");
		plateStack.push("WIND");

		// Define the target sequence
		List<String> targetSequence = Arrays.asList("WIND", "FIRE", "WATER");

		System.out.println("\n---------------- Welcome to the Stack Puzzle Room! ----------------\n");
		System.out.println("**************** Room 1: The Tower of Forgotten Spells ****************\n\n"
				+ "Long long ago, the great wizard Altherion stored his most powerful spells on enchanted plates and stacked them inside a tall obsidian cabinet.\n"
				+ "Each plate holds a fragment of elemental magic - FIRE, WATER, WIND, EARTH - but only the correct sequence can unlock the door to the next chamber.\n"
				+ "You, a young apprentice, have stumbled into this room during your quest to master the ancient art of Data Structure Sorcery.\n"
				+ "The cabinet obeys a mysterious rule: you may only touch the topmost plate.\n"
				+ "To retrieve the correct spell sequence, you must use your wits and the laws of the Stack - push, pop, peek - and restore the forgotten order.\n"
				+ "Remember, UNDO = LIFELINE -1 !!! (And you have only 3 lifelines.)");

		System.out.println("\nThe current STACK is:");
		System.out.println("| WIND |\n| FIRE |\n|WATER |\n| WIND |\n|EARTH |\n__");
		System.out.println("\nRetrieve the following sequence from the stack : " + targetSequence);
		System.out.println("Use commands: POP, PEEK, UNDO, EXIT");

		Stack<String> playerStack = new Stack<>();
		Stack<String> undoStack = new Stack<>();
		boolean timeConstraint = true;

		System.out.println("\nAre you ready?");
		String yes_or_no = scanner.nextLine().toUpperCase();
		if (!yes_or_no.equals("YES")) {
			System.out.println("Exiting the game...");
			return false;
		}

		long startTime = System.currentTimeMillis();
		long timeLimit = 60 * 1000; // 60 seconds

		while (!playerStack.equals(targetSequence) && GameMain.lifelines > 0) {
			long elapsedTime = System.currentTimeMillis() - startTime;
			
			if (elapsedTime > timeLimit) {
				System.out.println("\n⏰ Time's up! The spells fade into darkness...");
				timeConstraint = false;
				break;
			}

			long timeLeft = (timeLimit - elapsedTime) / 1000;
			System.out.println("Time remaining: " + timeLeft + " seconds");

			System.out.println("\nCurrent Stack Top: " + (plateStack.isEmpty() ? "EMPTY" : plateStack.peek()));
			System.out.print("Enter command: ");
			String command = scanner.nextLine().trim().toUpperCase();

			switch (command) {
			case "POP":
				if (!plateStack.isEmpty()) {
					String popped = plateStack.pop();
					playerStack.push(popped);
					undoStack.push(popped);
					System.out.println("Popped: " + popped);
				} 
				
				else {
					System.out.println("Stack is empty!");
				}
				break;

			case "PEEK":
				if (!plateStack.isEmpty()) {
					System.out.println("Peek: " + plateStack.peek());
				} 
				
				else {
					System.out.println("Stack is empty!");
				}
				break;

			case "UNDO":
				if (!undoStack.isEmpty()) {
					String last = undoStack.pop();
					playerStack.pop();
					plateStack.push(last);
					
					System.out.println("Undo: Returned " + last + " to stack.");
					GameMain.lifelines--;
					System.out.println("Lifeline -1 | Remaining: " + GameMain.lifelines);
				} 
				
				else {
					System.out.println("Nothing to undo!");
				}
				break;

			case "EXIT":
				System.out.println("Exiting Room 1...");
				return false;

			default:
				System.out.println("Invalid command. Try POP, PEEK, UNDO, or EXIT.");
			}

			System.out.println("Your sequence so far: " + playerStack);
		}

		// 🔹 Main Puzzle Result
		if (playerStack.equals(targetSequence) && GameMain.lifelines > 0 && timeConstraint) {
			System.out.println("\n🎉 Congratulations! You've retrieved the correct sequence: " + playerStack);
			System.out.println("\n🚪 A magical gate appears... but which one will you choose?");
			dsQuestion(scanner); // Go to DS Question phase
			return true;
		} 
		
		else {
			System.out.println("\n❌ You failed to retrieve the correct spell sequence...");
			GameMain.lifelines--;
			System.out.println("Lifeline lost! Remaining: " + GameMain.lifelines);
			
			if (GameMain.lifelines == 0) {
				System.out.println("💀 All lifelines exhausted! Game Over!");
				return false;
			}
		
			System.out.println("\nYou must now enter the Dead-End Room to redeem yourself...");
			miniPuzzle(scanner);
			return true;
		}
	}

	// 💡 Hint / DS Question Phase
	static void dsQuestion(Scanner scanner) {
		System.out.println("\n💡 The gate glows with runes that whisper a riddle...");
		System.out.println("I'm always right, but never wrong. I'm the hand that writes the song. Who am I?");
		System.out.println("Options: LEFT | RIGHT");
		String gateChoice = scanner.nextLine().toUpperCase();

		if (gateChoice.equals("RIGHT")) {
			System.out.println("\n✅ The door to the next room opens!");
			GameMain.score += 20;
			System.out.println("Score: " + GameMain.score);
		} 
		
		else {
			System.out.println("\n❌ Wrong choice!");
			System.out.println("But destiny offers another chance...");
			System.out.println("\nA stone sentinel appears and asks:");
			System.out.println("| FIRE |\n|WATER |\n| WIND |\n|EARTH |\n__");
			System.out.println("If I POP once, then PEEK, what element will I see?");
			String answer = scanner.nextLine().toUpperCase();

			if (answer.equals("WATER")) {
				System.out.println("✅ Correct! You have the god's grace!");
				GameMain.score += 20;
				System.out.println("Score: " + GameMain.score);
			} 
			
			else {
				System.out.println("❌ Wrong again! You must face the Dead-End Room challenge!");
				miniPuzzle(scanner);
			}
		}
	}

	// ⚰ Dead-End Mini Puzzle
	static void miniPuzzle(Scanner sc) {
		System.out.println("\n--- Dead-End Room Challenge ---");
		System.out.println("A glowing riddle appears on the wall:");
		System.out.println("Find the missing number: 3, 6, 12, ?, 48");
		
		System.out.print("Enter your answer: ");
		int ans = sc.nextInt();
		sc.nextLine();

		while (true) {
			if (ans == 24) {
				System.out.println("✅ Correct! You solved the mini puzzle!");
				GameMain.score += 5;
				System.out.println("Score: " + GameMain.score);
				break;
			} 
			
			else {
				System.out.println("❌ Wrong answer! The walls close in...");
				GameMain.lifelines--;
			
				if (GameMain.lifelines == 0) {
					System.out.println("💀 All lifelines exhausted! Game Over!");
					break;
				}
				
				System.out.println("Try again! Lifelines left: " + GameMain.lifelines);
				System.out.print("Enter your answer again: ");
				ans = sc.nextInt();
				sc.nextLine();
			}
		}
	}
}