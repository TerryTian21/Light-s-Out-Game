package LightsOut;
//Terry Tian

//CS 20 Final Project
//Western Canada High School
//January 22, 2021

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Game extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	// Initializing Global Varies
	public static int n; // This represents the side length of the matrix
	private static int move = 0;
	private static JButton butArray[][]; // Array for buttons
	static int sizeSquared; // Represents n squared. Will be used later on.
	static JLabel moves; // How many move the use makes
	private static int solution[][];

	// Main function just calls the Game to run
	public static void main(String[] args) throws Exception {

		System.out.println("Hello");
		n = errorTrap() + 2; // This is done so n value matches the difficulty

		Game start = new Game();
		start.setVisible(true);

	}

	// Error trap, pretty self explanatory
	// Asks user to enter difficulty level
	private static int errorTrap() throws Exception {

		System.out.println("What difficulty would you like? Level 1, 2 or 3?");
		Scanner input = new Scanner(System.in);
		int userInput = 0;
		boolean tryAgain;

		do {

			tryAgain = false;

			try {
				userInput = input.nextInt();

				if (userInput > 3 || userInput < 1) {

					tryAgain = true;
					System.out.println("You did not enter a valid input. Try Again.");
				}
			} catch (Exception e) {

				String garbage = input.next();
				tryAgain = true;
				System.out.println("You did not enter a valid input. Try Again.");
			}

		} while (tryAgain == true);

		return userInput;
	}

	// Main Game, sets up the window and graphics
	// Defines buttons
	public Game() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setTitle("Light's Out");
		setSize(500, 500);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		butArray = new JButton[n][n]; // Defines Button Array
		JPanel array = new JPanel(new GridLayout(n, n));
		array.setLayout(new GridLayout(n, n));

		// Initializes buttons
		for (int i = 0; i < n; i++)
			for (int x = 0; x < n; x++) {

				JButton button = new JButton();
				butArray[i][x] = button;
				button.setName("" + i + x);
				button.setBackground(Color.BLACK);
				button.addActionListener(this);
				array.add(butArray[i][x]);

			}

		panel.add(array, BorderLayout.CENTER);

		// Following Actions creates the buttons solution, and new game
		JButton newGame = new JButton("New Game");
		newGame.setName("newGame");
		newGame.addActionListener(this);
		JButton answer = new JButton("Solution");
		answer.setName("solution");
		answer.addActionListener(this);

		JPanel userControl = new JPanel();
		userControl.setLayout(new GridLayout(1, 2));
		userControl.add(newGame);
		userControl.add(answer);

		panel.add(userControl, BorderLayout.PAGE_END);

		// JPanel to display number of user moves
		JPanel display = new JPanel();
		display.add(moves = new JLabel());
		moves.setText("Moves: " + move);

		panel.add(display, BorderLayout.PAGE_START);

		newGame();

		setContentPane(panel);
		setVisible(true);
	}

	// Sets up all the actions i.e. mouse clicks
	// "Command Center" for user interface
	// Dictates what to perform whenever user clicks a button

	public void actionPerformed(ActionEvent e) {

		JButton button = (JButton) e.getSource();
		String name = button.getName();

		if (name.equals("newGame")) {

			newGame();
			return;

		}
		if (name.equals("solution")) {

			boolean solve = true;
			solution();
			return;
		}

		move++;
		moves.setText("Moves: " + move);

		char colChar = name.charAt(0);
		char rowChar = name.charAt(1);
		int col = Character.getNumericValue(colChar);
		int row = Character.getNumericValue(rowChar);

		changeColours(col, row);

		completed();

	}

	// Changes the color of the button and the surrounding buttons

	public static JButton[][] changeColours(int x, int y) {

		// Checks to see if there is a button available to the top, bottom, left and
		// center
		// If so, then changes the color of the button

		if (x + 1 < n) {

			if (butArray[x + 1][y].getBackground() == Color.YELLOW)
				butArray[x + 1][y].setBackground(Color.BLACK);
			else
				butArray[x + 1][y].setBackground(Color.YELLOW);
		}

		if (x - 1 >= 0) {

			if (butArray[x - 1][y].getBackground() == Color.YELLOW)
				butArray[x - 1][y].setBackground(Color.BLACK);
			else
				butArray[x - 1][y].setBackground(Color.YELLOW);

		}

		if (y - 1 >= 0) {

			if (butArray[x][y - 1].getBackground() == Color.YELLOW)
				butArray[x][y - 1].setBackground(Color.BLACK);
			else
				butArray[x][y - 1].setBackground(Color.YELLOW);

		}

		if (y + 1 < n) {

			if (butArray[x][y + 1].getBackground() == Color.YELLOW)
				butArray[x][y + 1].setBackground(Color.BLACK);
			else
				butArray[x][y + 1].setBackground(Color.YELLOW);
		}

		if (butArray[x][y].getBackground() == Color.YELLOW)
			butArray[x][y].setBackground(Color.BLACK);
		else
			butArray[x][y].setBackground(Color.YELLOW);

		return butArray;
	}

	// Checks if all the lights are turned off.

	public static void completed() {

		boolean completed = false;

		for (int i = 0; i < n; i++) {

			for (int x = 0; x < n; x++) {

				if (butArray[i][x].getBackground() != Color.YELLOW) {

					completed = true;
				} else {
					completed = false;
					break;
				}
			}

			if (completed == false)
				break;
		}

		if (completed == true) {

			winGame();

		}

	}

	// Whenever the user clicks the new game button
	// Clears the board to all black
	// Note that in the 4x4 and 5x5 matrix there are possibilities with no solutions
	// To prevent this, we start with an empty board i.e. no lights on
	// Then simulate n^2 button clicks to randomize the initial set of lights
	// This ensures that there will always be a solution since you can simply just
	// reverse the order of clicks
	public static void newGame() {

		move = 0;
		moves.setText("Moves: " + move);

		for (int i = 0; i < n; i++)
			for (int z = 0; z < n; z++)
				butArray[i][z].setText("");

		Random rand = new Random();

		for (int i = 0; i < n; i++)
			for (int x = 0; x < n; x++) {
				butArray[i][x].setBackground(Color.BLACK);
				butArray[i][x].setText("");
			}

		for (int i = 0; i < Math.pow(n, 2); i++) {

			int x = rand.nextInt(n);
			int y = rand.nextInt(n);

			changeColours(x, y);
		}

	}

	// If the user wins game, display a message in option pane.
	public static void winGame() {

		JOptionPane.showMessageDialog(null, "Congratulations, you have won!");

	}

	// This is the solution solver.

	public static void solution() {

		int z = 0;

		sizeSquared = (int) Math.pow(n, 2);
		solution = new int[sizeSquared][sizeSquared + 1];

		// The Following For Loops Populate the Array
		// The first column denotes the lights that are on/off currently
		// 1 = light on, 0 = light off
		// The subsequent columns denote which lights would be turned on if button 1,2,3
		// ...,n was clicked

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {

				if (butArray[i][j].getBackground() == Color.YELLOW)
					solution[z][0] = 1;
				z++;
			}
		}

		for (int i = 1; i < sizeSquared + 1; i++) {

			solution[i - 1][i] = 1;

			int x = i % n + 1;
			int y = i % n - 1;
			if (x > 1)
				solution[i][i] = 1;
			if (y != 0)
				solution[i - 2][i] = 1;

			x = i - n;
			y = i + n;
			if (x > 0)
				solution[x - 1][i] = 1;
			if (y <= sizeSquared)
				solution[y - 1][i] = 1;

		}

		// The next series of steps returns values in a row-reduced echelon form and
		// hence solving the puzzle.

		for (int i = 1; i < sizeSquared + 1; i++) {

			for (int x = 0; x < sizeSquared; x++) {

				if ((i - 1) == x)
					continue;

				if (solution[i - 1][i] == solution[x][i])
					add(i - 1, x);
			}

			ArrayList<Integer> temp = new ArrayList<Integer>();

			for (int x = i; x < sizeSquared; x++) {

				if (solution[x][i + 1] == 1)
					temp.add(x);
			}

			for (int x = i; x < sizeSquared; x++) {

				if (temp.isEmpty())
					break;
				else if (solution[x][i + 1] == 1)
					temp.remove(0);
				else {
					swap(x, temp.get(0));
					temp.remove(0);
				}

			}

		}

		// After getting the matrix into row-reduced form the first column represents
		// the solution
		// 1s represent the buttons that need to be clicked

		String text = "1";

		for (int i = 0; i < sizeSquared; i++) {

			if (solution[i][0] == 1) {

				int y = i % n;
				int x = i / n;

				butArray[x][y].setText(text);
				butArray[x][y].setForeground(Color.BLUE);
				butArray[x][y].setFont(new Font("Arial", Font.PLAIN, 15));
				int temp = Integer.parseInt(text);
				temp++;
				text = String.valueOf(temp);
			}

		}

	}

	// Method used to add rows to help solve matrix
	public static int[][] add(int i, int x) {

		for (int z = 0; z < (sizeSquared + 1); z++) {
			solution[x][z] = (solution[x][z] + solution[i][z]) % 2;

		}

		return solution;

	}

	// Method used to swap rows to help solve matrix
	public static int[][] swap(int x, int i) {

		for (int z = 0; z < (sizeSquared + 1); z++) {

			int temp = solution[x][z];
			solution[x][z] = solution[i][z];
			solution[i][z] = temp;
		}

		return solution;
	}

}
