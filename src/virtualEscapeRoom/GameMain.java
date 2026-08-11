package virtualEscapeRoom;
import java.util.*;

public class GameMain {

    static int lifelines = 3; // total lifelines
    static int score = 0;     // total score

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("\n🎮 WELCOME TO THE VIRTUAL ESCAPE ROOM ADVENTURE 🎮");
        System.out.println("===============================================");
        System.out.println("You have ❤ " + lifelines + " lifelines to survive all 5 rooms.");
        System.out.println("Each room gives +20 points if you win!");
        System.out.println("Mini puzzles can give +5 bonus points if solved.\n");

        // Room 1
        System.out.println("➡ Entering Room 1...");
        Room1_Game.start(sc);
        if (lifelines <= 0)
            endGame();

        // Room 2
        System.out.println("\n➡ Entering Room 2...");
        Room2_Game.start(sc);
        if (lifelines <= 0)
            endGame();

        // Room 3
        System.out.println("\n➡ Entering Room 3...");
        Room3_Game.start(sc);
        if (lifelines <= 0)
            endGame();

        // Room 4 (returns boolean)
        System.out.println("\n➡ Entering Room 4...");
        boolean room4Result = Room4_Game.start(sc);
        if (!room4Result)
            endGame();

        if (lifelines <= 0)
            endGame();

        // Room 5
        System.out.println("\n➡ Entering Room 5...");
        Room5_Game.start(sc);
        if (lifelines <= 0)
            endGame();

        // Final result
        finalResult();

        sc.close();
    }

    // Called if player runs out of lifelines
    static void endGame() {
        System.out.println("\n💀 You lost all your lifelines...");
        System.out.println("Game Over! Better luck next time, adventurer!");
        System.out.println("⭐ Final Score: " + score);
        System.exit(0);
    }

    // Final Game Result
    static void finalResult() {

        System.out.println("\n================ FINAL RESULT ================");

        if (lifelines > 0) {

            if (score >= 100) {
                System.out.println("🏆 CONGRATULATIONS! You won all 5 rooms!");
                System.out.println("💎 You are the Ultimate Escape Master!");
            }

            else if (score >= 80) {
                System.out.println("🎉 You escaped successfully after solving the final mini puzzle!");
                System.out.println("⭐ Total Score: " + score + " | ❤ Lifelines left: " + lifelines);
            }

            else {
                System.out.println("😅 You reached the end but missed some treasures!");
                System.out.println("⭐ Score: " + score + " | ❤ Lifelines left: " + lifelines);
            }
        }

        else {
            System.out.println("💀 You lost all lifelines before finishing...");
            System.out.println("Game Over! Try again to conquer all rooms!");
        }

        System.out.println("=============================================");
    }
}