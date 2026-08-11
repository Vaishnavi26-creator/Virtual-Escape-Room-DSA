package virtualEscapeRoom;

import java.util.*;

public class Room4_Game {

	static Stack<Integer> A = new Stack<>();
	static Stack<Integer> B = new Stack<>();
	static Stack<Integer> C = new Stack<>();

	public static boolean start(Scanner sc) {
		A.clear();
		B.clear();
		C.clear();

		int n = 3;
		int moves = 10;

		// Initialize Tower A
		for (int i = n; i >= 1; i--) {
			A.push(i);
		}

		System.out.println("\n🏰 ROOM 4 – CODE CHAMBER 🧩");
		System.out.println("Welcome to Main Puzzle: Tower of Hanoi!");

		System.out.println("Goal: Move all disks from Rod A → Rod C in same order.");
		System.out.println("Rules:");
		System.out.println("1. Move only the top disk each time.");
		System.out.println("2. Cannot place a larger disk on a smaller one.");
		System.out.println("3. You have " + moves + " moves to complete it.");
		System.out.println("------------------------------------------------------");

		displayTowers();

		while (true) {
			System.out.println("Moves left: " + moves);
			System.out.print("Enter move (e.g. A C): ");
			String from = sc.next();
			String to = sc.next();

			// validate move
			if (!isValidMove(from, to)) {
				System.out.println("Invalid move! Try again.");
			}

			else {
				moveDisk(from, to);
				moves--;
			}

			displayTowers();

			// ✅ If puzzle solved
			if (A.isEmpty() && B.isEmpty() && C.size() == n) {
				System.out.println("🎉 You solved the Tower of Hanoi!");
				dsQuestion(sc); // Go to DS question
				return true;
			}

			// ❌ If out of moves → lose one lifeline and go to mini puzzle
			if (moves == 0) {
				System.out.println("❌ Out of moves! You lose one lifeline.");
				GameMain.lifelines--;

				if (GameMain.lifelines == 0) {
					System.out.println("💀 Game Over! No lifelines left.");
					return false;
				}

				// Move to Dead-End Room (Mini Puzzle)
				miniPuzzle(sc, true); // true = came from main puzzle lose
				return true;
			}
		}
	}

	static void dsQuestion(Scanner sc) {
		System.out.println("\n🧠 Concept Check!");
		System.out.println("Which concept is primarily used to implement the Tower of Hanoi logic?");
		System.out.println("A) Tree\nB) Queue\nC) Recursion\nD) ArrayList");

		System.out.print("Enter your answer (A/B/C/D): ");
		char ans = sc.next().toUpperCase().charAt(0);

		if (ans == 'C') {
			System.out.println("✅ Correct! Tower of Hanoi uses Recursion.");
			GameMain.score += 20;
			System.out.println("🚪 Door unlocked! Moving to next room...");
			showSummary();
		}

		else {
			System.out.println("❌ Wrong! Correct answer: Recursion.");
			System.out.println("You must solve a mini-puzzle to proceed...");
			miniPuzzle(sc, false); // false = came from dsQuestion lose
		}
	}

	static void miniPuzzle(Scanner sc, boolean fromMainLose) {
		Random random = new Random();
		String[] puzzles = { "3, 6, 12, ?, 48", "2, 4, 8, ?, 32", "1, 3, 9, ?, 81", "5, 10, 20, ?, 80",
				"10, 20, 40, ?, 160", "2, 6, 12, ?, 24" };

		int[] answers = { 24, 16, 27, 40, 80, 18 };

		while (GameMain.lifelines > 0) {
			int index = random.nextInt(puzzles.length);

			System.out.println("\n🧩 MINI PUZZLE - Math Sequence Challenge!");
			System.out.println("Find the missing number:");
			System.out.println("Sequence: " + puzzles[index]);
			int userAns = sc.nextInt();

			if (userAns == answers[index]) {
				System.out.println("✅ Correct! +5 points");
				GameMain.score += 5;
				showSummary();
				return;
			}

			else {
				System.out.println("❌ Wrong! The correct answer was " + answers[index]);
				GameMain.lifelines--;

				if (fromMainLose) {
					System.out.println("You must retry this mini puzzle until you win or lifelines end.");
				}

				else {
					System.out.println("You can retry since this was from DS Question.");
				}

				if (GameMain.lifelines == 0) {
					System.out.println("💀 Game Over! No lifelines left.");
					return;
				}
			}
		}
	}

	static void showSummary() {
		System.out.println("\nROOM COMPLETE!");
		System.out.println("⭐ Score: " + GameMain.score);
		System.out.println("❤ Lifelines remaining: " + GameMain.lifelines);

		if (GameMain.lifelines > 0)
			System.out.println("➡ Moving to next room...");
		else
			System.out.println("💀 Game Over!");
	}

	static boolean isValidMove(String from, String to) {
		Stack<Integer> source = getTower(from);
		Stack<Integer> dest = getTower(to);

		if (source == null || dest == null)
			return false;
		if (source.isEmpty())
			return false;
		if (!dest.isEmpty() && source.peek() > dest.peek())
			return false;
		return true;
	}

	static void moveDisk(String from, String to) {
		Stack<Integer> source = getTower(from);
		Stack<Integer> dest = getTower(to);
		dest.push(source.pop());
	}

	static Stack<Integer> getTower(String name) {
		switch (name.toUpperCase()) {
		case "A":
			return A;
		case "B":
			return B;
		case "C":
			return C;
		default:
			return null;
		}
	}

	static void displayTowers() {
		System.out.println("A: " + A);
		System.out.println("B: " + B);
		System.out.println("C: " + C);
		System.out.println("----------------------------");
	}
}