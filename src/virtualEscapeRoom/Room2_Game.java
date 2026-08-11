package virtualEscapeRoom;

import java.util.*;

public class Room2_Game {

    public static boolean start(Scanner sc) {

        System.out.println("\n================ ROOM 2: HANGMAN GAME ================");

        boolean puzzleWon = playHangman(sc);

        if (puzzleWon) {

            System.out.println("\n✅ Great job completing Hangman!");

            System.out.println("\n💡 FINAL HINT QUESTION BEFORE MOVING TO NEXT ROOM 💡");
            System.out.println("Which data structure did we use for mapping words and hints?");
            System.out.println("1) ArrayList");
            System.out.println("2) HashMap");
            System.out.println("3) Stack");
            System.out.println("4) Queue");

            System.out.print("Your answer: ");

            /*
             * Read the answer as a String.
             * This allows the user to enter either:
             * 2
             * OR
             * HashMap
             */
            String answer = sc.nextLine().trim();

            if (answer.equals("2") || answer.equalsIgnoreCase("hashmap")) {

                System.out.println("✅ Correct! That was a HashMap!");

                GameMain.score += 20;

                System.out.println("⭐ +20 points!");
                System.out.println("Moving to the next room...\n");

            } else {

                System.out.println("❌ Wrong! The correct answer was: HashMap.");
                System.out.println("⚠ Moving to Dead-End Room (no lifeline lost)");

                System.out.println("\nYou must solve a mini puzzle to continue...");

                playRiddleMini(sc);
            }
        }

        else {

            System.out.println("\n❌ You failed the main puzzle!");

            GameMain.lifelines--;

            System.out.println("❤ Lifeline lost! Now: " + GameMain.lifelines);

            if (GameMain.lifelines > 0) {
                playRiddleMini(sc);
            }
        }

        if (GameMain.lifelines <= 0) {

            System.out.println("💀 No lifelines left! GAME OVER!");

            return false;
        }

        return true;
    }


    // ============================================================
    // HANGMAN GAME
    // ============================================================

    public static boolean playHangman(Scanner sc) {

        Random rand = new Random();

        /*
         * HashMap stores:
         * Word -> Category/Hint
         */
        HashMap<String, String> wordMap = new HashMap<>();

        wordMap.put("banana", "Fruit");
        wordMap.put("apple", "Fruit");
        wordMap.put("grapes", "Fruit");
        wordMap.put("orange", "Fruit");
        wordMap.put("mango", "Fruit");
        wordMap.put("cherry", "Fruit");

        wordMap.put("elephant", "Animal");
        wordMap.put("giraffe", "Animal");
        wordMap.put("kangaroo", "Animal");
        wordMap.put("penguin", "Animal");

        wordMap.put("australia", "Country");
        wordMap.put("india", "Country");
        wordMap.put("canada", "Country");
        wordMap.put("germany", "Country");
        wordMap.put("brazil", "Country");


        // Convert HashMap keys into an array
        String[] words = wordMap.keySet().toArray(new String[0]);


        // Sort words using Bubble Sort
        bubbleSort(words);


        // Select random word
        String wordToGuess = words[rand.nextInt(words.length)];

        // Get hint from HashMap
        String hint = wordMap.get(wordToGuess);


        // Create hidden word
        char[] guessedWord = new char[wordToGuess.length()];

        Arrays.fill(guessedWord, '_');


        int attempts = 5;

        boolean wordGuessed = false;


        System.out.println("\n🎯 HANGMAN GAME STARTED...");

        System.out.println("Hint: The word is related to -> " + hint);

        System.out.println("You have " + attempts + " wrong attempts allowed.");

        System.out.println("Word list(sorted): " + Arrays.toString(words));


        // ========================================================
        // HANGMAN LOOP
        // ========================================================

        while (attempts > 0 && !wordGuessed) {

            System.out.println("\nWord: " + String.valueOf(guessedWord));

            System.out.print("Enter a letter: ");

            String input = sc.nextLine().trim().toLowerCase();


            // Handle empty input
            if (input.isEmpty()) {

                System.out.println("⚠ Please enter a letter.");

                continue;
            }


            // Take first character
            char guess = input.charAt(0);

            boolean correctGuess = false;


            // Check character in word
            for (int i = 0; i < wordToGuess.length(); i++) {

                if (wordToGuess.charAt(i) == guess &&
                    guessedWord[i] == '_') {

                    guessedWord[i] = guess;

                    correctGuess = true;
                }
            }


            if (!correctGuess) {

                attempts--;

                System.out.println("❌ Wrong guess!");

                System.out.println("Attempts remaining: " + attempts);

            }

            else {

                System.out.println("✅ Good guess!");
            }


            // Check whether entire word has been guessed
            if (String.valueOf(guessedWord).equals(wordToGuess)) {

                wordGuessed = true;
            }
        }


        // ========================================================
        // HANGMAN RESULT
        // ========================================================

        if (wordGuessed) {

            System.out.println("\n🎉 You guessed the word: "
                    + wordToGuess.toUpperCase());

            System.out.println("Category: " + hint);

            return true;
        }

        else {

            System.out.println("\n💀 Game Over!");

            System.out.println("The word was: "
                    + wordToGuess.toUpperCase());

            return false;
        }
    }


    // ============================================================
    // MINI RIDDLE / DEAD-END ROOM
    // ============================================================

    public static void playRiddleMini(Scanner sc) {

        System.out.println("\n========== 💡 MINI RIDDLE PUZZLE ==========");


        String[][] riddles = {

            {
                "What has keys but can't open locks?",
                "keyboard"
            },

            {
                "What has to be broken before you can use it?",
                "egg"
            },

            {
                "I'm tall when I'm young, and I'm short when I'm old. What am I?",
                "candle"
            },

            {
                "What has a neck but no head?",
                "bottle"
            },

            {
                "The more you take, the more you leave behind. What are they?",
                "footsteps"
            }
        };


        Random rand = new Random();

        String[] r = riddles[rand.nextInt(riddles.length)];


        System.out.println("\nRiddle: " + r[0]);


        while (GameMain.lifelines > 0) {

            System.out.print("Your answer: ");

            String answer = sc.nextLine().trim().toLowerCase();


            if (answer.equals(r[1])) {

                System.out.println("✅ Correct! +5 points");

                GameMain.score += 5;

                System.out.println("⭐ Score: " + GameMain.score);

                return;
            }

            else {

                System.out.println("❌ Wrong! Try again...");

                GameMain.lifelines--;

                System.out.println(
                    "❤ Lifelines left: "
                    + GameMain.lifelines
                );


                if (GameMain.lifelines <= 0) {

                    System.out.println(
                        "💀 No lifelines left! GAME OVER!"
                    );

                    return;
                }
            }
        }
    }


    // ============================================================
    // BUBBLE SORT
    // ============================================================

    public static void bubbleSort(String[] arr) {

        for (int i = 0; i < arr.length - 1; i++) {

            for (int j = 0; j < arr.length - i - 1; j++) {

                if (arr[j].compareTo(arr[j + 1]) > 0) {

                    String temp = arr[j];

                    arr[j] = arr[j + 1];

                    arr[j + 1] = temp;
                }
            }
        }
    }
}