package virtualEscapeRoom;
import java.util.*;

public class Room5_Game {
	public static void start(Scanner scanner) {
		String[] hints = { "w3in", "d4i", "VOID5", "ga1r", "um7", "io56sa", "DSA_CONCEPTS", "lev25" };

		String[] traps = { "VOID5" };
		boolean[] visited = new boolean[hints.length];
		List<String> path = new ArrayList<>();

		System.out.println("\n************ ROOM 5 : Cipher of PotterHeads ************");
		System.out.println("Find the treasure: DSA_CONCEPTS using hint digits!");
		System.out.println("Each hint has a number that indicates your next jump.\n");

		for (String h : hints)
			System.out.print(h + "\t");

		System.out.println("\n");

		int currentIndex = 0;
		boolean treasureFound = false;

		while (true) {
			if (currentIndex < 0 || currentIndex >= hints.length) {
				System.out.println("You stepped out of bounds. The hunt ends here.");
				GameMain.lifelines--;

				if (GameMain.lifelines == 0)
					return;

				miniGame(scanner);
				return;
			}

			String relic = hints[currentIndex];
			System.out.println("\nYou found: " + relic);
			path.add(relic);

			// Trap check
			for (String trap : traps) {
				if (relic.equals(trap)) {
					System.out.println("It's a trap! The relic crumbles into dust.");
					System.out.println("You lost 1 lifeline.");
					GameMain.lifelines--;

					if (GameMain.lifelines == 0)
						return;

					miniGame(scanner);
					return;
				}
			}

			// Treasure check
			if (relic.startsWith("DSA")) {
				scanner.nextLine();
				System.out.println("\nYou found the treasure: " + relic);
				System.out.println("But, for the key, say the spell obtained through array:");
				String spell = scanner.nextLine().toUpperCase();

				if (spell.equals("WINGARDIUM LEVIOSA") || spell.equals("WINGARDIUMLEVIOSA")) {
					treasureFound = true;
					GameMain.score += 20;
					System.out.println("✨ Correct spell! +20 points");
				}

				else {
					System.out.println("❌ Wrong spell! Moving to mini puzzle...");
					miniGame(scanner); // no lifeline lost here
				}
				break;
			}

			// Loop detection
			if (visited[currentIndex]) {
				System.out.println("You've already been here. A loop has formed!");
				GameMain.lifelines--;

				if (GameMain.lifelines == 0)
					return;

				miniGame(scanner);
				return;
			}

			visited[currentIndex] = true;

			System.out.print("Enter index you want to jump to: ");
			int jumpIndex = scanner.nextInt();

			// Jumping logic simplified
			if (jumpIndex < 0 || jumpIndex >= hints.length) {
				System.out.println("Invalid index! Out of range.");
				GameMain.lifelines--;

				if (GameMain.lifelines == 0)
					return;

				miniGame(scanner);
				return;
			}

			System.out.println("Jumping to index " + jumpIndex + "...\n");
			currentIndex = jumpIndex;
		}

		System.out.println("\nYour path: " + path);
		if (treasureFound) {
			System.out.println("✨ You found the DSA treasure!");
			System.out.println("⭐ +20 points | ❤ Lifelines left: " + GameMain.lifelines);
		}

		else {
			System.out.println("The treasure remains hidden... for now.");
			GameMain.lifelines--;

			if (GameMain.lifelines > 0)
				miniGame(scanner);
		}
	}

	// MINI PUZZLE (Text Adventure)
	public static void miniGame(Scanner sc) {
		while (GameMain.lifelines > 0) { // loop until success or lifelines end
			ArrayList<String> inventory = new ArrayList<>();
			boolean gameOver = false;

			System.out.println("\n🧩 MINI PUZZLE: TEXT ADVENTURE 🧩");
			System.out.println("You wake up in a mysterious forest...");
			System.out.println("Your goal: Escape safely by finding the key to the gate!\n");

			System.out.println("You see two paths:");
			System.out.println("1️⃣ Go left toward the old cabin");
			System.out.println("2️⃣ Go right toward the dark cave");
			System.out.print("Choose (1 or 2): ");
			int choice1 = sc.nextInt();

			if (choice1 == 1) {
				System.out.println("\nYou walk to the old cabin and find a shiny 🔑 key!");
				inventory.add("key");
			}

			else if (choice1 == 2) {
				System.out.println("\nYou enter the cave... A trap! 💀 You fell into a pit!");
				gameOver = true;
			}

			else {
				System.out.println("Invalid choice! You got lost!");
				gameOver = true;
			}

			if (gameOver) {
				GameMain.lifelines--;

				if (GameMain.lifelines == 0) {
					System.out.println("\n💀 No lifelines left! Game Over.");
					return;
				}

				System.out.println("Retrying mini puzzle...");
				continue; // retry miniGame
			}

			System.out.println("\nYou reach a big metal gate 🚪");
			System.out.println("It looks locked.");
			System.out.println("1️⃣ Try to open the gate");
			System.out.println("2️⃣ Search nearby bushes for something useful");
			System.out.print("Choose (1 or 2): ");
			int choice2 = sc.nextInt();

			if (choice2 == 1) {
				if (inventory.contains("key")) {
					System.out.println("\nYou use the 🔑 key to unlock the gate! You escaped! 🎉");
					GameMain.score += 5;
					return;
				}

				else {
					System.out.println("\nThe gate is locked and you have no key! 💀");
					GameMain.lifelines--;
				}
			}

			else if (choice2 == 2) {
				System.out.println("\nYou search and find a 🗡 sword!");
				inventory.add("sword");

				System.out.println("A wild wolf appears! 🐺");
				System.out.println("1️⃣ Fight");
				System.out.println("2️⃣ Run");
				System.out.print("Choose: ");
				int fightChoice = sc.nextInt();

				if (fightChoice == 1) {
					if (inventory.contains("sword")) {
						System.out.println("\nYou defeat the wolf and find a hidden key!");
						inventory.add("key");
						System.out.println("You open the gate and escape! 🎉");
						GameMain.score += 5;
						return;
					}

					else {
						System.out.println("No weapon! Wolf attacks 💀");
						GameMain.lifelines--;
					}
				}

				else {
					System.out.println("You ran away and got lost 💀");
					GameMain.lifelines--;
				}
			}

			else {
				System.out.println("Invalid choice 💀");
				GameMain.lifelines--;
			}

			if (GameMain.lifelines == 0) {
				System.out.println("\n💀 No lifelines left! Game Over.");
				return;
			}

			System.out.println("\nRetrying mini puzzle... ❤ Lifelines left: " + GameMain.lifelines);
		}
	}
}