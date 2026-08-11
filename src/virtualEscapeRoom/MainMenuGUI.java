package virtualEscapeRoom;

import javax.swing.*;
import java.awt.*;

public class MainMenuGUI extends JFrame {

    private JLabel scoreLabel;
    private JLabel lifelineLabel;

    public MainMenuGUI() {

        setTitle("🎮 Virtual Escape Room");
        setSize(650, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBackground(new Color(25, 25, 45));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        );

        JLabel title = new JLabel(
                "🎮 VIRTUAL ESCAPE ROOM",
                SwingConstants.CENTER
        );

        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 30));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel(
                "A DSA Adventure",
                SwingConstants.CENTER
        );

        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitle.setForeground(new Color(210, 210, 230));

        JPanel heading = new JPanel(new GridLayout(2, 1));
        heading.setOpaque(false);

        heading.add(title);
        heading.add(subtitle);

        mainPanel.add(heading, BorderLayout.NORTH);

        JPanel roomPanel = new JPanel(new GridLayout(5, 1, 12, 12));
        roomPanel.setOpaque(false);

        roomPanel.add(createRoomLabel(
                "🧱 Room 1 — Tower of Forgotten Spells",
                "Stack"
        ));

        roomPanel.add(createRoomLabel(
                "🎯 Room 2 — Hangman",
                "HashMap + Bubble Sort"
        ));

        roomPanel.add(createRoomLabel(
                "🧭 Room 3 — The Labyrinth",
                "Graph + BFS"
        ));

        roomPanel.add(createRoomLabel(
                "🏰 Room 4 — Code Chamber",
                "Stack + Recursion"
        ));

        roomPanel.add(createRoomLabel(
                "🔐 Room 5 — Cipher of PotterHeads",
                "Array + ArrayList"
        ));

        mainPanel.add(roomPanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridLayout(1, 3, 15, 15));
        bottom.setOpaque(false);

        JButton startButton = createButton("▶ START GAME");
        JButton instructionsButton = createButton("📖 INSTRUCTIONS");
        JButton exitButton = createButton("❌ EXIT");

        startButton.addActionListener(e -> startGame());

        instructionsButton.addActionListener(e ->
                showInstructions()
        );

        exitButton.addActionListener(e ->
                System.exit(0)
        );

        bottom.add(startButton);
        bottom.add(instructionsButton);
        bottom.add(exitButton);

        mainPanel.add(bottom, BorderLayout.SOUTH);

        add(mainPanel);

        setVisible(true);
    }

    private JPanel createRoomLabel(String room, String dsa) {

        JPanel panel = new JPanel(new BorderLayout());

        panel.setBackground(new Color(45, 45, 70));

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(100, 100, 150),
                                1
                        ),
                        BorderFactory.createEmptyBorder(
                                10, 15, 10, 15
                        )
                )
        );

        JLabel roomLabel = new JLabel(room);
        roomLabel.setFont(
                new Font("Segoe UI Emoji", Font.BOLD, 15)
        );
        roomLabel.setForeground(Color.WHITE);

        JLabel dsaLabel = new JLabel(
                "DSA: " + dsa
        );

        dsaLabel.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );
        dsaLabel.setForeground(
                new Color(180, 180, 210)
        );

        panel.add(roomLabel, BorderLayout.CENTER);
        panel.add(dsaLabel, BorderLayout.EAST);

        return panel;
    }

    private JButton createButton(String text) {

        JButton button = new JButton(text);

        button.setFont(
                new Font("Segoe UI Emoji", Font.BOLD, 14)
        );

        button.setFocusPainted(false);

        button.setBackground(
                new Color(100, 90, 180)
        );

        button.setForeground(Color.WHITE);

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        12, 15, 12, 15
                )
        );

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        return button;
    }

    private void startGame() {

        setVisible(false);

        new EscapeRoomGUI(() -> {
            setVisible(true);
        });
    }

    private void showInstructions() {

        String message =
                "🎮 VIRTUAL ESCAPE ROOM\n\n" +

                "You have 3 lifelines.\n" +
                "Each main room can reward 20 points.\n" +
                "Mini puzzles can reward 5 bonus points.\n\n" +

                "ROOM 1 → Stack\n" +
                "ROOM 2 → HashMap + Bubble Sort\n" +
                "ROOM 3 → Graph + BFS\n" +
                "ROOM 4 → Stack + Recursion\n" +
                "ROOM 5 → Array / ArrayList\n\n" +

                "Solve the puzzles and reach the final treasure!";

        JOptionPane.showMessageDialog(
                this,
                message,
                "How to Play",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                MainMenuGUI::new
        );
    }
}