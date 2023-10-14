package Hangman;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

public class HangmanGameGUI extends JFrame {
	private JTextField resultField;
    private JLabel notificationLbl;
    private JTextField missesField;
    private JTextField enterField;
    private JTextField addWordField;
    private JTextArea addNotificationArea;
    private JTabbedPane tabbedPane;
    private JTextArea hangmanArea;
    private JButton enterBtn;
    private JButton startBtn;
    private String word;
    private char[] guessedWord;
    private int misses;
    
    // Declaring missed letters list, that will contain the missed letters
	private List<Character> missedLetters = new ArrayList<>();
    // Declaring correct letters list, that will contain the corrected letters
	private List<Character> correctLetters = new ArrayList<>();
	
    //Creating the constructor
	public HangmanGameGUI() {
	        setTitle("Hangman Game");
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setSize(400, 500); 
	        
	        // Creating a JTabbedPane
	        tabbedPane = new JTabbedPane();
	        
	        // Creating the Game tab components
	        JPanel gamePanel = new JPanel();
	        gamePanel.setLayout(new GridLayout(3, 1));
	        startBtn = new JButton("Start");
	        JPanel btnPanel = new JPanel();
	        btnPanel.add(startBtn);
	        JPanel gameStartPanel = new JPanel();
	        gameStartPanel.setLayout(new GridLayout(3, 1));
	        gameStartPanel.add(btnPanel);
	        JLabel enterLbl = new JLabel("Enter a single letter in the word");
	        enterLbl.setHorizontalAlignment(JLabel.CENTER);
	        gameStartPanel.add(enterLbl);
	        
	        //Creating button/field components, and disabling some until user clicks start
	        enterField = new JTextField(10);
	        enterField.setEnabled(false);
	        enterBtn = new JButton("Enter");
	        enterBtn.setEnabled(false);
	        JPanel gPanel = new JPanel();
	        gPanel.add(enterField);
	        gPanel.add(enterBtn);
	        gameStartPanel.add(gPanel);
	        gamePanel.add(gameStartPanel); //adding the first part to the main game panel
	        

	        // Creating a panel for the Hangman figure
	        JPanel hangmanPanel = new JPanel();
	        hangmanArea = new JTextArea(
	        		""
	            + " +------+\n"
	            + "        |\n"
	            + "        |\n"
	            + "        |\n"
	            + "        |\n"
	            + "        |\n"
	            + "=========");
	        hangmanArea.setEditable(false); 
	        hangmanArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
	        Color labelBackgroundColor = this.getBackground();
	        hangmanArea.setBackground(labelBackgroundColor);
	        hangmanPanel.add(hangmanArea);
	        
	        // Adding the Hangman panel below the "Result Panel"
	        gamePanel.add(hangmanPanel);
	        
	        // Creating the Result Panel
	        JPanel resultPanel = new JPanel();
	        resultPanel.setLayout(new GridLayout(3, 1)); 
	        
	        // Then we added a wide text field for displaying words (at the top)
	        resultField = new JTextField(20);
	        resultField.setHorizontalAlignment(JTextField.CENTER);
	        resultField.setEditable(false); // Make it read-only
	        resultField.setBorder(null);
	        resultPanel.add(resultField);
	        
	        // Then adding a read-only text field for misses (at the center)
	        missesField = new JTextField(20);
	        missesField.setBorder(null);
	        missesField.setHorizontalAlignment(JTextField.CENTER);
	        missesField.setEditable(false); // Make it read-only
	        resultPanel.add(missesField);
	        
	        // Then adding a read-only text field for notifications (at the bottom)
	        notificationLbl = new JLabel();
	        notificationLbl.setHorizontalAlignment(JLabel.CENTER);
	        Color BgColor = this.getBackground();
	        resultPanel.add(notificationLbl);
	        

	        // Adding the "Result Panel" to the "Game" tab
	        gamePanel.add(resultPanel);
	        
	        
	        // Adding the Game tab to the tabbed pane,alt+1
	        tabbedPane.addTab("Game", null, gamePanel, "Play Hangman");
	        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

	        // EXTRA idea: Creating the Add word tab
	        addWord();
	       
	     // Initializing the initializeGame method
	        startBtn.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                initializeGame();
	                enterField.setText("");
	            	enterBtn.setEnabled(true);
	            	enterField.setEnabled(true);
	    	        tabbedPane.setEnabledAt(1, false);
	            	notificationLbl.setText("");
	            	missesField.setText("");
	            	hangmanArea.setText(
	            	""
    	            + " +------+\n"
    	            + "        |\n"
    	            + "        |\n"
    	            + "        |\n"
    	            + "        |\n"
    	            + "        |\n"
    	            + "=========");
	            }
	        });
	     // ActionListener added to the enterField to respond to Enter key
	        enterBtn.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                processGuess(enterField.getText());
	                enterField.setText(""); // To clear the text field after a guess
	            }
	        });
	    }

	
	 public void addWord() {
	        JPanel addWordPanel = new JPanel();
	        //Creating its components and adding them to the addWordPanel
	        JLabel addWordLabel = new JLabel("Enter a new word to be added in the memory");
	        addWordField = new JTextField(20);
	        addNotificationArea = new JTextArea();
	        addNotificationArea.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
	        Color BgColor = this.getBackground();
	        addNotificationArea.setBackground(BgColor);
	        addNotificationArea.setEditable(false); 
	        JButton addButton = new JButton("Add");
	        addWordPanel.add(addWordLabel);
	        addWordPanel.add(addWordField);
	        addWordPanel.add(addButton);
	        addWordPanel.add(addNotificationArea);
	        //Adding the addWordPanel to Add Word tab, and adding it to the tabbed panel
	        tabbedPane.addTab("Add Word",null, addWordPanel,"Add a word here");
	        //clicking on alt+2 to access this tab
	        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
	        tabbedPane.setEnabledAt(1, false);
	        // Adding the tabbed pane to the frame
	        add(tabbedPane);
	        //Action lister add button
	        addButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                String newWord = addWordField.getText().trim();
	                addWordToFile(newWord);
	            }
	        });
	        }
	//Creating initializeGame method that will have multiple functionalities
	private void initializeGame() {
		//Crearting a hangman file using printWriter class
        try {
            if (missedLetters != null) {
                missedLetters.clear();
            }
            if (correctLetters != null) {
                correctLetters.clear();
            }
        	File file = new File("src/Hangman/hangman.txt");
    		try (PrintWriter output = new PrintWriter(file);)
    		{
    			output.println("alaa");
    			output.println("gurpreet");
    			output.println("elena");
    			output.println("priyanka");
    			output.println("mehrnaz");
    			output.println("java");
    			output.println("swing");
    			output.println("humber");
    			output.println("hangman");
    			output.println("cherry");
    			
    			output.close();
    		}
            // Read words from the file using Scanner class and store them in an array 
    		Scanner fileScanner = new Scanner(file);
            String[] words = new String[100];
            int wordCount = 0;
            while (fileScanner.hasNext()) {
                words[wordCount] = fileScanner.next();
                wordCount++;
            }

            Random random = new Random();
            word = words[random.nextInt(wordCount)];

            guessedWord = new char[word.length()];
            for (int i = 0; i < word.length(); i++) {
                guessedWord[i] = '*';
            }
            misses = 0;
            resultField.setText(new String(guessedWord));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	private void processGuess(String guess) {
	    guess = guess.toLowerCase();
	    
	    if (guess.length() != 1 || !Character.isLetter(guess.charAt(0))) {
	        // In case of invalid input
	    	notificationLbl.setText("Please enter a single letter in the word");
	        return;
	    }

	    char letter = guess.charAt(0);
	    boolean found = false;
	    for (int i = 0; i < word.length(); i++) {
	        if (word.charAt(i) == letter) {
	            guessedWord[i] = letter;
	            found = true;
	        }
	    }

	    if (found) {
	        if (correctLetters.contains(letter)) {
	        	notificationLbl.setText("Hint: You've already guessed the letter " + letter + " correctly before!");
	        } else {
	            correctLetters.add(letter);
	        }
	    } else {
	        if (missedLetters.contains(letter)) {
	        	notificationLbl.setText("Hint: You've already missed the letter " + letter + " before!");
	        } else {
	            missedLetters.add(letter);
	            misses++;
	            updateHangmanFigure();
	        }
	    }

	    resultField.setText(new String(guessedWord));

	 //Checking if the game is over after a miss or if the word has been guessed
	    if (misses >= 7 || !new String(guessedWord).contains("*")) {
	        resultField.setText("Word: " + word);  // Display the word
	        missesField.setText("You missed " + misses + " time(s)");  // Displaying misses count

	        if (misses >= 7) {
	        	notificationLbl.setText("Sorry, you lost! The word was: " + word);
	        } else {
	        	notificationLbl.setText("<html>Congratulations ! You won! <br> Click Start to play again <br> Click alt+2 to add a new word");
	        	tabbedPane.setEnabledAt(1, true);
	        }

	        enterBtn.setEnabled(false); // Disabling the "Enter" button
	        enterField.setEnabled(false);
	        return;
	    }
	}
	
    
    //Method to add a new word in the addword panel in the add word tab
    private void addWordToFile(String newWord) {
        if (!newWord.isEmpty()) {
            try {
                // Creating a FileWriter in append mode (true) to add a new word to the file
                FileWriter writer = new FileWriter("src/Hangman/hangman.txt", true);
                BufferedWriter bufferWriter = new BufferedWriter(writer);
                PrintWriter printWriter = new PrintWriter(bufferWriter);
                
                boolean wordExists = checkWordExists("src/Hangman/hangman.txt", newWord);
                
                if (!wordExists) {
                    printWriter.println(newWord);
                    printWriter.close();
                    bufferWriter.close();

                    addNotificationArea.setText("Word '" + newWord + "' added to hangman.txt");
                    addWordField.setText(""); // Clear the input field
                } 
                else
                {
                    JFrame frame = new JFrame("Error Dialog Example");
                    JOptionPane.showMessageDialog(frame, "The word already exists in the file!", "Error", JOptionPane.ERROR_MESSAGE);
                    addNotificationArea.setText("");
                    addWordField.setText(""); // Clear the input field
                    //frame.setVisible(true);

                  
                }	


               
            } catch (IOException e) {
                e.printStackTrace();
                addNotificationArea.setText("Error adding the word to hangman.txt");
            }
        } else {
        	notificationLbl.setText("Please enter a word to add.");
        }
    }
    
    public static boolean checkWordExists(String fileName, String word) {
        try (BufferedReader br = new BufferedReader(new FileReader("src/Hangman/hangman.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().equalsIgnoreCase(word.trim())) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


     // Method to update the Hangman figure
        private void updateHangmanFigure() {
            String hangmanFigure =
            		""
    	            + " +------+\n"
    	            + "        |\n"
    	            + "        |\n"
    	            + "        |\n"
    	            + "        |\n"
    	            + "        |\n"
    	            + "=========";

            switch (misses) {
                case 1:
                    hangmanFigure = ""
    	            + " +------+\n"
    	            + "   |    |\n"
    	            + "        |\n"
    	            + "        |\n"
    	            + "        |\n"
    	            + "        |\n"
    	            + "=========";
                    break;
                case 2:
                    hangmanFigure = ""
            	            + " +------+\n"
            	            + "   |    |\n"
            	            + "   O    |\n"
            	            + "        |\n"
            	            + "        |\n"
            	            + "        |\n"
            	            + "=========";
                    break;
                case 3:
                    hangmanFigure = ""
            	            + " +------+\n"
            	            + "   |    |\n"
            	            + "   O    |\n"
            	            + "   |    |\n"
            	            + "        |\n"
            	            + "        |\n"
            	            + "=========";
                    break;
                case 4:
                    hangmanFigure = ""
            	            + " +------+\n"
            	            + "   |    |\n"
            	            + "   O    |\n"
            	            + "  /|    |\n"
            	            + "        |\n"
            	            + "        |\n"
            	            + "=========";
                    break;
                case 5:
                    hangmanFigure = ""
            	            + " +------+\n"
            	            + "   |    |\n"
            	            + "   O    |\n"
            	            + "  /|\\   |\n"
            	            + "        |\n"
            	            + "        |\n"
            	            + "=========";
                    break;
                case 6:
                    hangmanFigure = ""
            	            + " +------+\n"
            	            + "   |    |\n"
            	            + "   O    |\n"
            	            + "  /|\\   |\n"
            	            + "  /     |\n"
            	            + "        |\n"
            	            + "=========";
                    break;
                default:
                    hangmanFigure = ""
            	            + " +------+\n"
            	            + "   |    |\n"
            	            + "   O    |\n"
            	            + "  /|\\   |\n"
            	            + "  / \\   |\n"
            	            + "        |\n"
            	            + "=========";
            }
            hangmanArea.setText(hangmanFigure);
    }
	    public static void main(String[] args) {
	            HangmanGameGUI frame = new HangmanGameGUI();
	            frame.setVisible(true);
	            frame.setResizable(false);
	    }
	}