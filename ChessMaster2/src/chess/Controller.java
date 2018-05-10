package chess;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Controller {

	private final MouseHandler mouseHandler;
	private final Board board;
	private final int x0, y0, rowLen, colLen;
	private boolean gameComplete;
	private AI ai;
	private int gameMode;
	private GraphicsHandler graphicsHandler;
	
	public Controller(Board board, GraphicsHandler graphicsHandler, int x, int y, int rowLen, 
			int colLen) {
		this.x0 = x;
		this.y0 = y;
		this.rowLen = rowLen;
		this.colLen = colLen;
		this.board = board;
		this.graphicsHandler = graphicsHandler;
		mouseHandler = new MouseHandler();
		graphicsHandler.addMouseListener(mouseHandler);
		this.gameComplete = false;
	}
	
	/**
	 * Finds the board cell at (x,y) coordinates of graphics.
	 * Calls board.clicked() with the row and column of the clicked cell.
	 * Redraws all the graphics of the window.
	 * Checks whether the game has ended, and shows a message if that is true.
	 * if either of the player is under check, shows a message.
	 * */
	public void clicked(int x, int y)
	{	
		System.out.println("Clicked: " + x + " " + y);
		if(this.gameComplete)	//if game is complete, don't let anyone play.
			return;
		
		int col = (x-x0) / rowLen;
		int row = (y-y0) / colLen;
		
		boolean moveHappened = board.clicked(Board.rowMax-row, col+Board.colMin);
		//board.print();
		if(moveHappened && gameMode == 1)
		{
			ai.playNextMove();
			moveHappened = true;
		}
		
		if(board.isCheckMate(Board.White))
		{	this.checkMate(Board.White);
			this.gameComplete = true;
		}
		else if(moveHappened && board.isUnderCheck(Board.White))
			this.check(Board.White);
		if(board.isCheckMate(Board.Black))
		{	
			this.checkMate(Board.Black);
			this.gameComplete = true;
		}
		else if(moveHappened && board.isUnderCheck(Board.Black))
			this.check(Board.Black);
		this.graphicsHandler.repaint();
	}
	
	/**
	 * Shows a message in a dialog box notifying that given player
	 * has lost the game.
	 * e.g. "White, you lost the game :'( "
	 * */
	private void checkMate(String playerColour)
	{
		JFrame message = new JFrame("Sorry !!");
		message.add(new JLabel(playerColour+", you lost the game :'("));
		message.setVisible(true);
		message.setSize(300, 300);
	}
	
	/**
	 * Sets the gameMode to 1 if it's player vs AI.
	 * Sets it to two if it's between two human players.
	 * @throws invalid game mode exception
	 * */
	public void setGameMode(int mode) throws Exception
	{
		if(mode !=1 && mode!=2)
			throw new Exception("Invalid game mode in GraphicsHandler.");
		this.gameMode = mode;
	}
	
	/**
	 * Sets AI.
	 * If AI is to play next, it lets AI play.
	 * @throws null AI exception
	 * */
	public void setAI(AI ai) throws Exception
	{
		if(ai == null)
			throw new Exception("null AI object in setAI()");
		this.ai = ai;
		if( ai.getColour().equals( board.getCurrentTurn() ) && 
			!board.isCheckMate(ai.getColour()) &&
			!board.isCheckMate(Board.opposite(ai.getColour())))
			ai.playNextMove();
	}
	
	/**
	 * Shows a message in a dialog box warning the given player
	 * that it is under check.
	 * e.g. "White, you are given a check! :O"
	 * */
	public void check(String playerColour)
	{
		JFrame message = new JFrame("Check !!");
		message.add(new JLabel(playerColour+", you are given a check! :O"));
		message.setVisible(true);
		message.setSize(300, 300);
	}
	
	/**
	 * MouseHandler class which calls clicked() of GraphicsHandler object,
	 * with the x and y coordinates of a mouse click.
	 * */
	private class MouseHandler implements MouseListener, MouseMotionListener
	{
		@Override
		public void mouseDragged(MouseEvent e) 
		{}

		@Override
		public void mouseMoved(MouseEvent e) 
		{}

		@Override
		public void mouseClicked(MouseEvent e) 
		{
			clicked(e.getX(), e.getY());
		}

		@Override
		public void mousePressed(MouseEvent e) 
		{}

		@Override
		public void mouseReleased(MouseEvent e) 
		{}

		@Override
		public void mouseEntered(MouseEvent e) 
		{}

		@Override
		public void mouseExited(MouseEvent e) 
		{}
	}
}
