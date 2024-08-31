import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.*;
import javax.swing.*;

/**
 * Class provides the structure used to play the Wordle game. The Wordle game is a word guessing game where you have 6
 * tries to guess a randomly selected word. The guess must be composed of only 5 letters. As guesses are made the grid
 * will show certain letters in different colors. Grey means the letter is not in the randomly selected word. Yellow
 * means the letter is in the word, but not at the correct spot. Green means that the letter is in the correct spot. If
 * you have 5 green letters in a row that means that you have guessed the word correctly and the game will end. The
 * bottom part of the game help you keep track of which letters have been used. If the letter is colored red that means
 * you have used the letter.
 */
public class Wordle extends JFrame implements ActionListener {
    /**
     * Array of random words that can be picked from
     */
    private final String[] validWords = {"above","april","lower","cable","eager","fable","label","nacho","racer","boxer","brick","gravy","lucky","loved","games","oxide","olive","ounce","prize","unzip"};
    /**
     * The randomly selected word
     */
    private String word;
    /**
     * Holds every letter in the randomly selected word
     */
    private String[] wordArray;
    /**
     * Set of letters that have been used
     */
    Set<String> usedLetters;
    /**
     * Key for which panel goes to which letter in the alphabet
     */
    HashMap<JLabel, String> letterOfLabel;
    /**
     * Represents the main panel
     */
    private JPanel mainPanel;
    /**
     * Represents the 6 by 5 panel created to play the game
     */
    private JPanel gridPanel;
    /**
     * Represents the panel which the user interacts with
     */
    private JPanel userPanel;
    /**
     * Represents the panel where unused and used letter are shown
     */
    private JPanel letterPanel;
    /**
     * Represents the guess made by the user
     */
    private JTextField guess;
    /**
     * Represents the button used to enter a word
     */
    private JButton button;
    /**
     * Represents the labels put on the 6 by 5 game panel
     */
    private JLabel[] gridLabel;
    /**
     * Represents the labels put on the panel with the usage of the letters
     */
    private JLabel[] letterGrid;
    /**
     * Keeps track of how many valid guesses were made
     */
    private int guessCount;
    /**
     * Keeps track of the panel number needed for placing the user's guess
     */
    private int letterCount;
    /**
     * Keeps track if you get 5 letters at the same index
     */
    private int winCount;

    /**
     * No input constructor, Builds a window where the game grid is on top, the guessing field is in the middle, and
     * the letter bank/panel is on the bottom, The game grid is set to 30 black squares, The guessing field is given
     * text field for the user to type the guess and a button to enter the guess, the letter bank is set to all the
     * letters of the alphabet with their backgrounds as grey (Not used)
     */
    public Wordle() {

        word = validWords[new Random().nextInt(19)]; //Picking a random word from the validWords array
        //word = "array";
        System.out.println(word);
        wordArray = new String[5]; //Creating a new array to hold the randomly selected word split up into letters
        for (int i = 0; i < word.length(); i ++) {
            wordArray[i] = String.valueOf(word.charAt(i)); //Adding the letter to the array
        }

        //Initializing the count variables to 0
        guessCount = 0;
        letterCount = 0;
        winCount = 0;


        setTitle("Wordle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,730);
        setLocationRelativeTo(null); //Centers the opened window

        mainPanel = new JPanel(new BorderLayout()); //Creating a border layout for the main panel
        gridPanel = new JPanel(new GridLayout(6,5,1,1)); //Making a panel that has a grid layout for the main 6 by 5 game display

        gridLabel = new JLabel[30]; //Initializing to an array of 30 JLabels to represent the 30 spots on the game grid

        //Setting the properties for each JLabel inside the array
        //Initially the labels are all just black squares
        for (int i = 0; i < 30; i++) {
            gridLabel[i] = new JLabel("BLANK",JLabel.CENTER);
            gridLabel[i].setForeground(Color.BLACK);
            gridLabel[i].setOpaque(true); //Allows you to set the background
            gridLabel[i].setBackground(Color.BLACK);
            gridLabel[i].setFont(new Font("Serif", Font.PLAIN, 72));
            gridPanel.add(gridLabel[i]); //Adding the label to the game grid

        }

        mainPanel.add(gridPanel,BorderLayout.NORTH); //Adding the game grid to the top of the main panel

        //Initializing the button object for entering the guess
        button = new JButton("ENTER");
        button.addActionListener(this); //Adding an action listener which will allows certain tasks to take place
                                           //when the button is pressed
        //Initializing the text field object for where the user will input their guess
        guess = new JTextField(5);

        //Initializing the user panel which will contain all the information need for the user
        userPanel = new JPanel(new BorderLayout());
        userPanel.add(new JLabel("Guess:"),BorderLayout.WEST); //Adding label to the left side of the user panel asking the user to input a guess
        userPanel.add(guess,BorderLayout.CENTER); //Adding the text field to the middle of the user panel
        userPanel.add(button,BorderLayout.EAST); //Adding the button to the right side of the user panel
        mainPanel.add(userPanel,BorderLayout.CENTER); //Adding the user panel to the center of main panel

        //Initializing the letter panel as a grid layout to hold the alphabet
        letterPanel = new JPanel(new GridLayout(2,13,1,1));
        //Initializing an array to hold every letter of the alphabet
        letterGrid = new JLabel[26];
        //Making a set to hold which letters have been used my the user
        usedLetters = new HashSet<>();
        //Creating an array with every letter of the alphabet in it
        String[] letters = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
        //Making a key to hold which label corresponds to what letter
        letterOfLabel = new HashMap<>();

        //Setting the properties of each JLabel for the alphabet
        for (int i = 0; i < 26; i++) {
            letterGrid[i] = new JLabel(letters[i],JLabel.CENTER);
            letterGrid[i].setForeground(Color.BLUE);
            letterGrid[i].setOpaque(true); //Allows you to set the background
            letterGrid[i].setBackground(Color.GRAY);
            letterGrid[i].setFont(new Font("Serif", Font.PLAIN, 40));
            letterPanel.add(letterGrid[i]); //Adding the label to the letter bank/panel

            letterOfLabel.put(letterGrid[i],letters[i]); //Adding the label with its corresponding letter to the letter key

        }

        mainPanel.add(letterPanel,BorderLayout.SOUTH); //Adding letter panel to the bottom part of the main panel

        add(mainPanel); //Adding the main panel to the JFrame

    }

    /**
     * Set of actions performed when the enter button is clicked by the user
     * @param e - the event to be processed by the button
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //Checking if 6 guess attempts have been made
        if (guessCount < 6) {
            //Processing the guess made by the user
            generateTry(guess.getText());
            //Checking if the user's guess had five letters in the correct spot
            if (winCount == 5) {
                //Creating a pane for winning the game
                JOptionPane.showMessageDialog(null, new JLabel("Congratulations You Win!"),"END OF GAME",JOptionPane.PLAIN_MESSAGE);
                guessCount = 6; //Stopping any more guesses from being generated
            }
            //Checking if the guess limit has been reached
            else if (guessCount == 5) {
                //Creating a pane for losing the game
                JOptionPane.showMessageDialog(null, new JLabel("    The correct guess was " + word + "!"),"YOU LOOSE HAHA",JOptionPane.PLAIN_MESSAGE);
            }
        }
        guessCount++; //Increment guessCount for each guess made
    }

    /**
     * Checking if the guess made by the user is valid, returns true if the guess was valid
     * @param guess - the guess made by the user
     * @return boolean representing if the guess is valid
     */
    public boolean checkValid(String guess) {
        //Checking if the length of the guess is 5
        if (guess.length() == 5) {
            for (int i = 0; i < guess.length(); i ++) {
                //Checking if the guess only has letters in it
                if (!Character.isLetter(guess.charAt(i))) {
                    JOptionPane.showMessageDialog(null, new JLabel("Not all letters!"),"ERROR",JOptionPane.PLAIN_MESSAGE);
                    return false;
                }
            }
            return true;
        }
        JOptionPane.showMessageDialog(null, new JLabel("Your guess must be 5 letters long!"),"ERROR",JOptionPane.PLAIN_MESSAGE);
        return false;
    }

    /**
     * Comparing the guess to the correct word and making a color code based on how it should look on the grid
     * @param guess - the guess made by the user
     * @param correctWord - the randomly selected word
     * @return An array of strings representing the color code where each element is the color that should be outputted
     *         to the screen, the array is in order
     */
    public String[] compareTwoWords(String guess, String correctWord) {
        //Creating a hashmap to act as a key for how many times each letter pops up in the correct word
        HashMap<Character, Integer> correctCount = new HashMap<>();

        for (int i = 0; i < correctWord.length(); i++) {
            //Checking if the letter is already in the key
            if(!correctCount.containsKey(correctWord.charAt(i))) {
                correctCount.put(correctWord.charAt(i),1);
            }
            else {
                correctCount.put(correctWord.charAt(i),correctCount.get(correctWord.charAt(i))+1); //Incrementing the number of times repeated
            }
        }

        //Creating an array to hold the color code
        String[] colorCode = new String[5];
        for (int i = 0; i < 5; i++) {
            //Checking if the letter of the guess at index i is the same as the letter of the correct word at index i
            if (guess.charAt(i) == word.charAt(i)) {
                colorCode[i] = "Green";
                correctCount.put(guess.charAt(i),correctCount.get(guess.charAt(i))-1); //Decrementing count because one color has been decided for the letter
            }
            else {
                colorCode[i] = "Grey"; //All other squares are made grey
            }
        }

        //Looping through the guess
        for (int i = 0; i < guess.length(); i++) {
            //Looping through correct word
            for (int j = 0; j < correctWord.length(); j++) {
                //Checking if the letter in guess is found in the correct word
                //If the color code at index i is already set to green don't overwrite the color to yellow
                if (guess.charAt(i) == correctWord.charAt(j) && !colorCode[i].equals("Green")) {
                    //Checking if the letter is contained in correct word key
                    if (correctCount.containsKey(guess.charAt(i))) {
                        //Getting the count for the letter
                        if (correctCount.get(guess.charAt(i)) > 0) {
                            colorCode[i] = "Yellow";
                            correctCount.put(guess.charAt(i),correctCount.get(guess.charAt(i))-1); //Decrementing count because color has been accounted for
                            i++; //Moving to the next letter in guess
                            j = correctWord.length(); //Stop looping through the correct word
                        }
                    }
                }
            }
        }

        return colorCode;
    }

    /**
     * Deals with the word bank and changes the colors according to which letter have been used
     * @param guess - the guess made by the user
     */
    public void generateUsedLetters(String guess) {
        //Adding the letters used to the set
        for (int i = 0; i < guess.length(); i++) {
            usedLetters.add(String.valueOf(guess.charAt(i)));
        }

        //Turning the letter used to red on the letter grid
        for (int i = 0; i < letterGrid.length; i++) {
            String letter = letterOfLabel.get(letterGrid[i]);
            //Checking the letter in the letter grid is contained within the set
            if (usedLetters.contains(letter)) {
                letterGrid[i].setOpaque(true);
                letterGrid[i].setBackground(Color.RED);
                letterGrid[i].revalidate();
            }
        }
    }

    /**
     * Processes the guess given by the user and handles how it will be displayed on the window. The method deals with
     * game grid, the user section, and the letter grid
     * @param guess - the guess made by the user
     */
    public void generateTry (String guess) {
        guess = guess.toLowerCase(); //Making the guess lower case
        //Checking if the guess is valid
        if (checkValid(guess)) {
            //Creating an array to hold the color code for the guess
            String[] colorCode = compareTwoWords(guess,word);

            //Deciding how each section of the grid by row is going to be colored
            for (int i = 0; i < guess.length(); i++) {
                //Creating a variable to hold the letter at a specific index
                String guessAtIndex = String.valueOf(guess.charAt(i));

                //Setting the specifications for the label
                gridLabel[letterCount].setText(guessAtIndex);
                gridLabel[letterCount].setForeground(Color.WHITE);
                gridLabel[letterCount].setOpaque(true); //Allows you to set the background
                gridLabel[letterCount].setFont(new Font("Sans_Serif", Font.TRUETYPE_FONT, 72));

                //Checking if the associated color for the letter is yellow
                if (colorCode[i].equals("Yellow")) {
                    gridLabel[letterCount].setBackground(Color.YELLOW);
                }
                //Checking if the associated color for the letter is green
                else if (colorCode[i].equals("Green")){
                    gridLabel[letterCount].setBackground(Color.GREEN);
                    winCount++; //Increment win count because the letter was green
                }
                else {
                    gridLabel[letterCount].setBackground(Color.GRAY);
                }

                gridLabel[letterCount].revalidate();
                letterCount++; //Incrementing the letterCount to determine which spot on the grid we are on

            }

            //If all five letters of the guess were not correct reset win count
            if (winCount != 5) {
                winCount = 0;
            }

            //Determining which letter were used and indicating that on the letter grid
            generateUsedLetters(guess);


        }
        else {
            guessCount--; //Should not take an attempt if the guess was not valid
        }
    }
}
