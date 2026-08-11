package virtualEscapeRoom;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class EscapeRoomGUI extends JFrame {

    private JTextArea outputArea;
    private JTextField inputField;

    private PrintStream originalOut;
    private InputStream originalIn;

    private Thread gameThread;

    private Runnable onClose;

    /*
     * Instead of PipedInputStream/PipedOutputStream,
     * we use a thread-safe queue for GUI input.
     */
    private final BlockingQueue<String> inputQueue =
            new LinkedBlockingQueue<>();

    private volatile boolean gameRunning = true;


    public EscapeRoomGUI(Runnable onClose) {

        this.onClose = onClose;

        setTitle("🎮 Virtual Escape Room — Adventure");
        setSize(850, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createGUI();

        setVisible(true);

        startGame();
    }


    // ============================================================
    // CREATE GUI
    // ============================================================

    private void createGUI() {

        JPanel mainPanel =
                new JPanel(new BorderLayout(10, 10));

        mainPanel.setBackground(
                new Color(20, 20, 35)
        );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 15, 15
                )
        );


        // ---------------- OUTPUT AREA ----------------

        outputArea = new JTextArea();

        outputArea.setEditable(false);

        outputArea.setFont(
                new Font(
                        Font.MONOSPACED,
                        Font.PLAIN,
                        15
                )
        );

        outputArea.setBackground(
                new Color(15, 15, 25)
        );

        outputArea.setForeground(
                new Color(230, 230, 240)
        );

        outputArea.setLineWrap(true);

        outputArea.setWrapStyleWord(true);


        JScrollPane scrollPane =
                new JScrollPane(outputArea);

        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );


        // ---------------- INPUT AREA ----------------

        JPanel bottomPanel =
                new JPanel(new BorderLayout(10, 10));

        bottomPanel.setOpaque(false);


        inputField = new JTextField();

        inputField.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        16
                )
        );


        JButton sendButton =
                new JButton("SEND");

        sendButton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        sendButton.setFocusPainted(false);


        sendButton.addActionListener(
                e -> sendInput()
        );

        inputField.addActionListener(
                e -> sendInput()
        );


        bottomPanel.add(
                inputField,
                BorderLayout.CENTER
        );

        bottomPanel.add(
                sendButton,
                BorderLayout.EAST
        );


        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );


        add(mainPanel);
    }


    // ============================================================
    // START GAME
    // ============================================================

    private void startGame() {

        try {

            originalOut = System.out;
            originalIn = System.in;


            /*
             * Create our custom InputStream.
             *
             * GameMain still uses:
             *
             * Scanner sc = new Scanner(System.in);
             *
             * so we don't need to modify your room classes.
             */
            System.setIn(
                    new GuiInputStream()
            );


            PrintStream guiOutput =
                    new PrintStream(
                            new GuiOutputStream(),
                            true,
                            StandardCharsets.UTF_8
                    );

            System.setOut(guiOutput);


            gameThread = new Thread(() -> {

                try {

                    GameMain.lifelines = 3;
                    GameMain.score = 0;

                    GameMain.main(
                            new String[0]
                    );

                }

                catch (Exception ex) {

                    ex.printStackTrace();

                    SwingUtilities.invokeLater(() -> {

                        outputArea.append(
                                "\n\n❌ GAME ERROR\n"
                        );

                        outputArea.append(
                                "Error Type: "
                                        + ex.getClass().getName()
                                        + "\n"
                        );

                        outputArea.append(
                                "Message: "
                                        + ex.getMessage()
                                        + "\n"
                        );
                    });

                }

                finally {

                    gameRunning = false;

                    restoreStreams();
                }

            });


            gameThread.start();

        }

        catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // ============================================================
    // SEND INPUT FROM GUI
    // ============================================================

    private void sendInput() {

        if (!gameRunning) {

            outputArea.append(
                    "\n⚠ Game is no longer running.\n"
            );

            return;
        }


        String input =
                inputField.getText();


        if (input == null ||
                input.trim().isEmpty()) {

            return;
        }


        // Display user's input
        outputArea.append(
                "\n> " + input + "\n"
        );


        // Clear input field
        inputField.setText("");


        /*
         * Put input into the queue.
         *
         * The Scanner inside GameMain will receive
         * this through GuiInputStream.
         */
        inputQueue.offer(
                input + "\n"
        );
    }


    // ============================================================
    // RESTORE ORIGINAL SYSTEM STREAMS
    // ============================================================

    private synchronized void restoreStreams() {

        if (originalIn != null) {

            System.setIn(originalIn);
        }

        if (originalOut != null) {

            System.setOut(originalOut);
        }
    }


    // ============================================================
    // WINDOW CLOSE
    // ============================================================

    @Override
    public void dispose() {

        gameRunning = false;


        /*
         * Wake up the game thread if it is waiting
         * for input.
         */
        inputQueue.offer("\n");


        restoreStreams();


        if (onClose != null) {

            onClose.run();
        }


        super.dispose();
    }


    // ============================================================
    // CUSTOM INPUT STREAM
    // ============================================================

    private class GuiInputStream
            extends InputStream {


        private byte[] currentData =
                new byte[0];

        private int currentPosition = 0;


        @Override
        public int read()
                throws IOException {


            while (currentPosition >=
                    currentData.length) {

                try {

                    String input =
                            inputQueue.take();

                    currentData =
                            input.getBytes(
                                    StandardCharsets.UTF_8
                            );

                    currentPosition = 0;

                }

                catch (InterruptedException ex) {

                    Thread.currentThread().interrupt();

                    throw new IOException(
                            "Input interrupted"
                    );
                }
            }


            return currentData[
                    currentPosition++
            ] & 0xFF;
        }


        @Override
        public int read(
                byte[] b,
                int off,
                int len)
                throws IOException {


            if (b == null) {

                throw new NullPointerException();
            }


            if (off < 0 ||
                    len < 0 ||
                    off + len > b.length) {

                throw new IndexOutOfBoundsException();
            }


            if (len == 0) {

                return 0;
            }


            int first =
                    read();


            b[off] =
                    (byte) first;


            int available =
                    currentData.length
                    - currentPosition;


            int toCopy =
                    Math.min(
                            len - 1,
                            available
                    );


            if (toCopy > 0) {

                System.arraycopy(
                        currentData,
                        currentPosition,
                        b,
                        off + 1,
                        toCopy
                );

                currentPosition +=
                        toCopy;
            }


            return toCopy + 1;
        }


        @Override
        public int available() {

            return currentData.length
                    - currentPosition;
        }
    }


    // ============================================================
    // CUSTOM OUTPUT STREAM
    // ============================================================

    private class GuiOutputStream
            extends OutputStream {


        @Override
        public void write(int b) {

            final char character =
                    (char) b;


            SwingUtilities.invokeLater(() -> {

                outputArea.append(
                        String.valueOf(character)
                );

                outputArea.setCaretPosition(
                        outputArea.getDocument()
                                .getLength()
                );
            });
        }


        @Override
        public void write(
                byte[] b,
                int off,
                int len) {


            String text =
                    new String(
                            b,
                            off,
                            len,
                            StandardCharsets.UTF_8
                    );


            SwingUtilities.invokeLater(() -> {

                outputArea.append(text);

                outputArea.setCaretPosition(
                        outputArea.getDocument()
                                .getLength()
                );
            });
        }
    }
}