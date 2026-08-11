package virtualEscapeRoom;
import java.util.*;

public class Room3_Game {
	public static boolean start(Scanner sc) {

		// Maze Layout (Walls = #, Open Path = ., Start = S, Exit = E)
		char[][] maze = { "###############".toCharArray(), "#S......#.....#".toCharArray(),
				"###.####.#.####".toCharArray(), "#.....#.......#".toCharArray(), "#.###.#.#####.#".toCharArray(),
				"#.#...#.....#.#".toCharArray(), "#.#.#######.#.#".toCharArray(), "#.#.........#E#".toCharArray(),
				"###############".toCharArray() };

		// Convert maze into graph structure for BFS path validation
		Graph graphData = buildGraphFromMaze(maze);

		// Player initial position (S location)
		int playerRow = 1, playerCol = 1;

		System.out.println("\n===== ROOM 3: THE LABYRINTH =====");
		System.out.println("You find yourself in a twisting stone maze...");
		System.out.println("Your goal is to reach the exit ⭐.\n");

		System.out.println("CONTROLS:");
		System.out.println(" W = Up | S = Down | A = Left | D = Right\n");

		System.out.println("GAME RULES:");
		System.out.println("- ⏳ You have 90 seconds to escape.");
		System.out.println("- ❌ If you hit a wall → You must solve a mini-puzzle.");
		System.out.println("- ⭐ Reaching the exit gives +20 points.\n");

		System.out.println("Press ENTER to start...");
		sc.nextLine();

		long startTime = System.currentTimeMillis();
		long timeLimit = 90 * 1000; // Time limit in milliseconds
		boolean mazeSolved = false;

		// Main game loop
		while (true) {
			long timePassed = System.currentTimeMillis() - startTime;
			long timeLeft = (timeLimit - timePassed) / 1000;

			if (timeLeft < 0)
				timeLeft = 0;

			System.out.println("\n⏳ Time Left: " + timeLeft + "s ❤ Lifelines: " + GameMain.lifelines + " ⭐ Score: "
					+ GameMain.score);

			// Display maze with player position
			for (int r = 0; r < maze.length; r++) {
				for (int c = 0; c < maze[r].length; c++) {
					if (r == playerRow && c == playerCol)
						System.out.print("🙂");

					else if (maze[r][c] == '#')
						System.out.print("⬛");

					else if (maze[r][c] == 'S')
						System.out.print("🚩");

					else if (maze[r][c] == 'E')
						System.out.print("⭐");

					else
						System.out.print("⬜");
				}
				System.out.println();
			}

			// If time expires → mini puzzle
			if (timeLeft <= 0) {
				System.out.println("\n⏳ Time Up! Solve a mini-puzzle to continue.");
				GameMain.lifelines--;
				
				if (miniPuzzle(sc) == false)
					return false;

				GameMain.score += 5;
				System.out.println("✅ +5 awarded. Moving to next room...");
				return true;
			}

			// Player movement input
			System.out.print("\nMove (W/A/S/D): ");
			char move = sc.next().toLowerCase().charAt(0);

			int newRow = playerRow, newCol = playerCol;
			if (move == 'w')
				newRow--;
			else if (move == 's')
				newRow++;
			else if (move == 'a')
				newCol--;
			else if (move == 'd')
				newCol++;
			else
				continue; // Invalid input, ignore

			// If player hits wall → mini puzzle
			if (maze[newRow][newCol] == '#') {
				System.out.println("❌ You hit a wall!");
				GameMain.lifelines--;
				
				if (miniPuzzle(sc) == false)
					return false;
				GameMain.score += 5;

				System.out.println("✅ +5 Awarded! You successfully cleared the mini-puzzle.");
				System.out.println("➡ Moving to next room...");
				System.out.println("⭐ Final Score from Room 3: " + GameMain.score);
				System.out.println("❤ Lifelines Remaining: " + GameMain.lifelines);
				return true;
			}

			// Move player to new position
			playerRow = newRow;
			playerCol = newCol;

			// BFS graph traversal (valid path simulation)
			bfsGraph(graphData, playerRow, playerCol);

			// Check exit reached
			if (maze[playerRow][playerCol] == 'E') {
				mazeSolved = true;
				System.out.println("\n🎉 You reached the Exit!");
				break;
			}
		}

		// Final Data Structure Question
		if (mazeSolved) {
			System.out.println("\n🧠 FINAL CHECK:");
			boolean dsCorrect = askDataStructureQuestion(sc);

			if (dsCorrect) {
				GameMain.score += 20;
				System.out.println("✅ +20 Score Awarded!");
			}

			else {
				System.out.println("❌ Wrong Answer! The correct answer is: Graph.");
				System.out.println("You must solve a mini-puzzle to continue!");
				while (!miniPuzzle(sc)) {
					System.out.println("Try Again...");
					GameMain.lifelines--;
				}
				GameMain.score += 5;
				System.out.println("✅ +5 Score Awarded.");
			}
		}

		System.out.println("\n➡ You may now enter Room 4.");
		System.out.println("⭐ Final Score from Room 3: " + GameMain.score);
		System.out.println("❤ Lifelines Remaining: " + GameMain.lifelines);
		System.out.println("Use these lifelines wisely in the upcoming rooms...");

		return true;
	}

	// Graph Structure for maze path representation
	static class Graph {
		int[][] adj; // Adjacency matrix
		int[][] nodeIndex; // Matrix mapping maze cells to graph nodes

		Graph(int[][] adj, int[][] nodeIndex) {
			this.adj = adj;
			this.nodeIndex = nodeIndex;
		}
	}

	// Converts maze into a graph where every open cell is a node
	static Graph buildGraphFromMaze(char[][] maze) {
		int rows = maze.length, cols = maze[0].length;
		int[][] nodeIndex = new int[rows][cols];
		int count = 0;

		// Assign index to each walkable cell
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < cols; c++)
				if (maze[r][c] != '#')
					nodeIndex[r][c] = count++;

		int[][] adj = new int[count][count];
		int[] dr = { -1, 1, 0, 0 };
		int[] dc = { 0, 0, -1, 1 };

		// Create edges between adjacent walkable cells
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				
				if (maze[r][c] != '#') {
					int node = nodeIndex[r][c];
					
					for (int i = 0; i < 4; i++) {
						int nr = r + dr[i], nc = c + dc[i];
						
						if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && maze[nr][nc] != '#')
							adj[node][nodeIndex[nr][nc]] = 1;
					}
				}
			}
		}
		return new Graph(adj, nodeIndex);
	}

	// BFS Traversal - Ensures connectivity path exists
	static void bfsGraph(Graph graph, int sr, int sc) {
		int start = graph.nodeIndex[sr][sc];
		Queue<Integer> q = new LinkedList<>();
		
		boolean[] visited = new boolean[graph.adj.length];
		q.offer(start);
		visited[start] = true;

		while (!q.isEmpty()) {
			int node = q.poll();
			
			for (int next = 0; next < graph.adj.length; next++) {
				if (graph.adj[node][next] == 1 && !visited[next]) {
					visited[next] = true;
					q.offer(next);
				}
			}
		}
	}

	// Mini Puzzle - Direction Logic
	static boolean miniPuzzle(Scanner sc) {
		System.out.println("\n🧭 MINI-PUZZLE: Direction Riddle!");
		System.out.println("You start facing North.");
		System.out.println("F=Forward, R=Right, L=Left.\n");

		String[][] sequences = { { "F", "R", "R", "F", "L" }, { "R", "F", "L", "F", "R" }, { "L", "L", "F", "R", "R" },
				{ "F", "F", "R", "F", "L" } };

		String[] directions = { "North", "East", "South", "West" };
		Random rand = new Random();
		String[] seq = sequences[rand.nextInt(sequences.length)];

		System.out.print("Sequence: ");
		for (String step : seq)
			System.out.print(step + " ");
		System.out.print("\nEnter final direction: ");

		sc.nextLine();
		String userDir = sc.nextLine().trim();

		int dirIndex = 0;
		for (String step : seq) {
		
			if (step.equals("R"))
				dirIndex = (dirIndex + 1) % 4;
			
			else if (step.equals("L"))
				dirIndex = (dirIndex - 1 + 4) % 4;
		}

		String correctDirection = directions[dirIndex];
		if (userDir.equalsIgnoreCase(correctDirection)) {
			System.out.println("✅ Correct!");
			return true;
		} 
		
		else {
			System.out.println("❌ Wrong! Correct: " + correctDirection);
			GameMain.lifelines--;
			
			System.out.println("❤ Lifelines Left: " + GameMain.lifelines);
			return GameMain.lifelines > 0;
		}
	}

	// Loop mini puzzle until solved or lifelines exhausted
	static boolean runMiniUntilSuccess(Scanner sc) {
		while (true) {
			if (miniPuzzle(sc))
				return true;
			if (GameMain.lifelines <= 0) {
				System.out.println("\n💀 No lifelines left! GAME OVER");
				return false;
			}
			System.out.println("\n🔁 Try the mini-puzzle again!");
		}
	}

	// Final Data Structure Question
	static boolean askDataStructureQuestion(Scanner sc) {
		System.out.println("\n🧠 FINAL CHECK:");
		System.out.println("Which data structure best represents this maze?");
		System.out.println("1) Stack\n2) Queue\n3) Graph\n4) Linked List");
		System.out.print("Your Answer: ");
		int ans = sc.nextInt();

		if (ans == 3) {
			System.out.println("✅ Correct! Mazes are represented as Graphs.");
			return true;
		} 
		
		else {
			System.out.println("\n❌ Wrong Answer!");
			System.out.println("➡ The correct answer is: GRAPH.");
			System.out.println("🔁 You must now solve the MINI-PUZZLE to proceed.");
			return false;
		}
	}
}