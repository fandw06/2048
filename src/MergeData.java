
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * 2048 is a small interesting game.
 *
 * @author Dawei Fan
 * @version 1.1 12/03/2014
 * 			1) Paint custom board and elements.
 *
 * 			1.0 12/03/2014
 * 			1) Directly use the screenshots as number display.
 * 			2) No animation when moving.
 *
 */
public class MergeData extends JPanel{

	private static final long serialVersionUID = 1L;

	/** Available space to put new numbers. */
	private List<Integer> available= null;

	/** Picture name of all picture candidates. */
	private static final String pics[] = {"2.png",  "4.png", "8.png", "16.png", "32.png", "64.png", "128.png",
			"256.png", "512.png", "1024.png", "2048.png"};

	private static final Color cBackground[] = {new Color(238, 228, 218), new Color(237, 224, 200),
									new Color(242, 177, 121), new Color(245, 149, 99),
									new Color(246, 124, 95), new Color(246, 94, 59),
									new Color(237, 207, 114), new Color(237, 204, 97),
									new Color(237, 200, 80), new Color(237, 197, 63),
									new Color(252, 211, 71)};

	private static final Color cForeground[] = {new Color(119, 110, 101), new Color(119, 110, 101),
												Color.white, Color.white, Color.white, Color.white, Color.white, Color.white,
												Color.white, Color.white, Color.white};

	private static final int fontSize[] = {60, 60, 60, 60, 60, 60, 56, 56, 56, 52, 52};

	private static final Color cGrid = new Color(186, 174, 161);
	private static final Color cBoard = new Color(203, 193, 180);

	/** The board data for this game. */
	private int board[][] = null;

	/** Size parameters of GUI components. */
	private static final int wGrid = 15;
	private static final int wBlock = 106;

	public MergeData(){
		/* Do not use layout. */
		super(null);
		this.setSize(500, 700);
		this.setBackground(cBoard);
		this.setLocation(0,0);
		this.setOpaque(true);
		this.setVisible(true);
		this.setFocusable(true);
		this.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				int keyCode = e.getKeyCode();
		   		if (keyCode==KeyEvent.VK_LEFT)
		   		  	moveLeft();

		   		else if (keyCode==KeyEvent.VK_RIGHT)
		   		  	moveRight();

		   		else if (keyCode==KeyEvent.VK_UP)
		   		  	moveUp();

		   		else if (keyCode==KeyEvent.VK_DOWN)
		   		  	moveDown();

		   		else return;
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

		});

		initialize();
	}

	private void initialize(){
		/** Initialize available list. */
		available = new LinkedList<Integer>();
		for(int i = 0; i<16; i++)
			available.add(i);

		/** Initialize board data. */
		board = new int[4][4];
		for(int i = 0; i<4; i++){
			for(int j = 0; j<4; j++)
				board[i][j] = 0;
		}

		/** Add two numbers at the beginning. */
		addNewNumber();
		addNewNumber();
		setupBoard();
	}

	private void restart(){
		this.removeAll();
		repaint();
		initialize();
	}

	private void setupBoard(){
		/** Draw horizontal grids. */
		int offset[] = {0, 0};
		for(int i = 0; i<5; i++){
			JPanel grid = new JPanel();
			grid.setBackground(cGrid);
			grid.setSize(5*wGrid+4*wBlock, wGrid);
			grid.setLocation(offset[0], offset[1]+i*(wGrid+wBlock));
			grid.setVisible(true);
			this.add(grid);
		}

		/** Draw vertical grids. */
		for(int i = 0; i<5; i++){
			JPanel grid = new JPanel();
			grid.setBackground(cGrid);
			grid.setSize(wGrid, 5*wGrid+4*wBlock);
			grid.setLocation(offset[0]+i*(wGrid+wBlock), offset[1]);
			grid.setVisible(true);
			this.add(grid);
		}
	}


	private void addNewNumber(){

		Random r=new Random();
		int number = r.nextInt(available.size());
		int value = 1;
		/** The new one has 90% probability to be 2, and 10% to be 4. */
		if(r.nextInt(10)==0)
			value = 2;

		/** Add new number in the board. */
		board[available.get(number)/4][available.get(number)%4] = value;

		/** Add new number in GUI display. */
		addNumberAt(available.get(number)%4, available.get(number)/4, value);

		printBoard();
		System.out.println(available);

	}

	private void addNumberAt(int x, int y, int value){
		/** Add new number in GUI display. */
		JLabel n = new JLabel();
		n.setSize(wBlock, wBlock);
		n.setBackground(cBackground[value-1]);
		n.setForeground(cForeground[value-1]);
		n.setText(Integer.toString((int)Math.pow(2, value)));
		n.setFont(new Font("Microsoft Accor black", Font.PLAIN, fontSize[value-1]));
		n.setHorizontalAlignment(SwingConstants.CENTER);
		n.setLocation(x*(wBlock+wGrid)+wGrid, y*(wBlock + wGrid)+wGrid);
		n.setOpaque(true);
		n.setVisible(true);
		this.add(n);
		repaint();
		/** Update available list. */
		available.remove((Object)(4*y+x));
	}

	private void removeNumberAt(int x, int y){
		/** remove new number in GUI display. */
		Component n = this.findComponentAt(x*(wBlock+wGrid)+wGrid, y*(wBlock + wGrid)+wGrid);
		this.remove(n);
		/** Update available list. */
		available.add(4*y+x);
		repaint();
	}

	private boolean isWin(){
		for(int i = 0; i<4; i++){
			for(int j = 0; j<4; j++){
				if(board[i][j]==11)
					return true;
			}
		}
		return false;
	}

	private boolean isFailed(){
		if(available.size()==0){
			for(int i = 0; i<4; i++){
				for(int j = 0; j<3; j++){
					if(board[i][j]==board[i][j+1])
						return false;
				}
			}
			for(int i = 0; i<4; i++){
				for(int j = 0; j<3; j++){
					if(board[j][i]==board[j+1][i])
						return false;
				}
			}
			return true;
		}

		return false;
	}

	private void decideStatus(){
		addNewNumber();
		int select = 0;
		if(isWin()){
			select = JOptionPane.showConfirmDialog(this,
							"You win!",
                            "Congratulations!", JOptionPane.OK_CANCEL_OPTION);
			if(select == JOptionPane.OK_OPTION)
				restart();
			else
				System.exit(0);

		}
		if(isFailed()){
			select = JOptionPane.showConfirmDialog(this,
					"Failed!",
                    "Game over!", JOptionPane.OK_CANCEL_OPTION);
			if(select == JOptionPane.OK_OPTION)
				restart();
			else
				System.exit(0);
		}
	}

	private void moveLeft(){

		/**
		 *	For every direction, take left for instance,
		 *	1) Iterate from left to right.
		 *	2) While current block has a space on its left, move left until there is another block
		 *		or boundary.
		 *  3) If it has a block left number A has the same number, merge.
		 *  4) If its left neighbor has been merged, don't merge again this step.
		 *
		 */
		boolean change = false;
		/** i is y-axis, j is x-axis. */
		for(int i = 0; i<4; i++){
			boolean merged = false;
			for(int j = 1; j<4; j++){
				if(board[i][j]!=0){

					int current = j;
					while(current>=1 && board[i][current-1]==0)
						current--;
					/**
					 *  Now current block should be at (i, current).
					 *  Check if its neighbor is a block and has the same number and previous not mergeed, then merge.
					 */
					if((current>0) && board[i][current-1] == board[i][j] && merged == false){
						/** Update board data. */
						board[i][current-1] += 1;
						board[i][j] = 0;
						/** Update the GUI and available list. */
						removeNumberAt(j, i);
						removeNumberAt(current-1, i);
						addNumberAt(current-1, i, board[i][current-1]);
						/** Not merged. */
						merged = true;
						change = true;
					}
					/** Else just set the position, don't merge. */
					else if (current!=j){
						/** Update board data. */
						board[i][current] = board[i][j];
						board[i][j] = 0;

						/** Update the GUI and available list. */
						removeNumberAt(j, i);
						addNumberAt(current, i, board[i][current]);
						/** Not merged. */
						merged = false;
						change = true;
					}
					/** If current == j, do nothing. */
				}
			}
		}
		if(change)
			decideStatus();
	}

	private void moveRight(){

		boolean change = false;
		/** i is y-axis, j is x-axis. */
		for(int i = 0; i<4; i++){
			boolean merged = false;
			for(int j = 2; j>=0; j--){
				if(board[i][j]!=0){

					int current = j;
					while(current<=2 && board[i][current+1]==0)
						current++;
					/**
					 *  Now current block should be at (i, current).
					 *  Check if its right neighbor is a block and has the same number and previous not merged, then merge.
					 */
					if((current<3) && board[i][current+1] == board[i][j] && merged == false){
						/** Update board data. */
						board[i][current+1] += 1;
						board[i][j] = 0;

						/** Update the GUI and available list. */
						removeNumberAt(j, i);
						removeNumberAt(current+1, i);
						addNumberAt(current+1, i, board[i][current+1]);
						/** Not merged. */
						merged = true;
						change = true;
					}
					/** Else just set the position, don't merge. */
					else if (current!=j){
						/** Update board data. */
						board[i][current] = board[i][j];
						board[i][j] = 0;

						/** Update the GUI and available list. */
						removeNumberAt(j, i);
						addNumberAt(current, i, board[i][current]);
						/** Not merged. */
						merged = false;
						change = true;
					}
					/** If current == j, do nothing. */
				}
			}
		}
		if(change)
			decideStatus();
	}

	private void moveUp(){

		boolean change = false;
		/** i is x-axis, j is y-axis. */
		for(int i = 0; i<4; i++){
			boolean merged = false;
			for(int j = 1; j<4; j++){
				if(board[j][i]!=0){

					int current = j;
					while(current>=1 && board[current-1][i]==0){
						current--;
					}
					/**
					 *  Now current block should be at (current, i).
					 *  Check if its neighbor is a block and has the same number and previous not mergeed, then merge.
					 */
					if((current>0) && board[current-1][i] == board[j][i] && merged == false){
						System.out.println("Here!");
						/** Update board data. */
						board[current-1][i] += 1;
						board[j][i] = 0;

						/** Update the GUI and available list. */
						removeNumberAt(i, j);
						removeNumberAt(i, current-1);
						addNumberAt(i, current-1, board[current-1][i]);
						/** Not merged. */
						merged = true;
						change = true;
					}
					/** Else just set the position, don't merge. */
					else if (current!=j){
						/** Update board data. */
						board[current][i] = board[j][i];
						board[j][i] = 0;

						/** Update the GUI and available list. */
						removeNumberAt(i, j);
						addNumberAt(i, current, board[current][i]);
						/** Not merged. */
						merged = false;
						change = true;
					}
					/** If current == j, do nothing. */
				}
			}
		}
		if(change)
			decideStatus();
	}

	private void moveDown(){

		boolean change = false;
		/** i is x-axis, j is y-axis. */
		for(int i = 0; i<4; i++){
			boolean merged = false;
			for(int j = 2; j>=0; j--){
				if(board[j][i]!=0){

					int current = j;
					while(current<3 && board[current+1][i]==0){
						current++;
					}
					/**
					 *  Now current block should be at (current, i).
					 *  Check if its neighbor is a block and has the same number and previous not mergeed, then merge.
					 */
					if((current<3) && board[current+1][i] == board[j][i] && merged == false){
						/** Update board data. */
						board[current+1][i] += 1;
						board[j][i] = 0;
						/** Update the GUI and available list. */
						removeNumberAt(i, j);
						removeNumberAt(i, current+1);
						addNumberAt(i, current+1, board[current+1][i]);
						/** Not merged. */
						merged = true;
						change = true;
					}
					/** Else just set the position, don't merge. */
					else if (current!=j){
						/** Update board data. */
						board[current][i] = board[j][i];
						board[j][i] = 0;

						/** Update the GUI and available list. */
						removeNumberAt(i, j);
						addNumberAt(i, current, board[current][i]);
						/** Not merged. */
						merged = false;
						change = true;
					}
					/** If current == j, do nothing. */
				}
			}
		}
		if(change)
			decideStatus();
	}

	private void printBoard(){
		for(int i = 0; i<4; i++){
			for(int j = 0; j<4; j++){
				System.out.print(board[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}

	public static void createAndShowGUI(){
		JFrame window = new JFrame ("2048" + "  Author: Dawei Fan");
		Container container = window.getContentPane();
		MergeData md = new MergeData();
		container.add(md);
		window.setSize(505, 528);
		window.setLocation(500, 100);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setVisible(true);

	}

	public static void main(String[] arg){
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				createAndShowGUI();
			}

		});
	}
}
