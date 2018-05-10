
package chess;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import piece.Piece;

/**
 * @author Ravishankar P. Joshi
 */
public class GraphicsHandler extends JPanel {
	private static final long serialVersionUID = 1L;
	private final Board board;
	private final int x0, y0, rowLen, colLen, border;
	private final Color HIGHLIGHT, NEXTMOVE;

	/**
	 * Sets starting point of graphics as (x, y). (Window before it will remain
	 * blank.) Sets row-length and column length and border width. Sets board. Sets
	 * colour of a cell for highlight and next move. Adds a mouse event handler for
	 * the graphics.
	 */
	public GraphicsHandler(Board b, int x, int y, int rowLen, int colLen, int border) {
		board = b;
		x0 = x;
		y0 = y;
		this.rowLen = rowLen;
		this.colLen = colLen;
		HIGHLIGHT = Color.YELLOW;
		NEXTMOVE = Color.orange;
		this.border = border;
	}

	/**
	 * Draws the board and shows pieces, row and column number/letters. Marks
	 * highlighted cell with HIGHLIGHT colour and next move cell with NEXTMOVE
	 * colour. Paints alternate cell WHITE and GRAY colour.
	 */
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		this.drawBoardLabels(graphics);
		for (int row = 0; row < Board.BOARD_SIZE; row++) {
			for (int col = 0; col < Board.BOARD_SIZE; col++) {
				this.drawCellWithPiece(graphics, row, col);
			}
		}
	}
	
	/**
	 * Draws the row numbers down the left hand side of the board and the column letters across the top.
	 */
	private void drawBoardLabels(Graphics graphics) {
		graphics.setFont(new Font("Serif", Font.BOLD, 24));
		this.drawColumnLabels(graphics);
		this.drawRowLabels(graphics);
	}
	
	/**
	 * Draws a cell of the chess board, including a border and the piece on the cell.
	 */
	private void drawCellWithPiece(Graphics graphics, int row, int col) {
		int cellXOnScreen = x0 + row * rowLen;
		int cellYOnScreen = y0 + col * colLen;
		Cell currentCell = board.getCellAt(Board.rowMax - col, row + Board.colMin);
		if(greyCell(row, col)) {
			this.drawGreyCell(graphics, cellXOnScreen, cellYOnScreen);
		} else {
			this.drawWhiteCell(graphics, cellXOnScreen, cellYOnScreen);
		}
		this.highlightCellIfNecessary(graphics, currentCell, cellXOnScreen, cellYOnScreen);
		graphics.setColor(Color.BLACK);
		graphics.drawRect(cellXOnScreen, cellYOnScreen, colLen, rowLen);
		String pieceType = board.getPieceType(currentCell);
		Class<? extends Piece> pieceClass = board.getPieceClass(currentCell);
		
		this.drawPiece(graphics, pieceType, pieceClass, cellXOnScreen, cellYOnScreen);
	}
	
	/**
	 * Draws a piece, given the piece type and a location on the screen.
	 */
	private void drawPiece(Graphics graphics, String pieceType, Class<? extends Piece> pieceClass, int cellXOnScreen,
			int cellYOnScreen) {
		if (pieceType == null) {
			return;
		}
		
		BufferedImage img = null;

		try {
			img = ImageIO.read(pieceClass.getResource(pieceType + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Exception in paintComponents()" + " of GraphicsHandler.");
		}

		Image scaledImg = img.getScaledInstance(colLen, rowLen, Image.SCALE_SMOOTH);
		Icon pieceIcon = new ImageIcon(scaledImg);

		pieceIcon.paintIcon(this, graphics, cellXOnScreen, cellYOnScreen);
	}

	/**
	 * Returns whether a cell should be grey (returns true) or white (returns false).
	 */
	private boolean greyCell(int row, int col) {
		return (row + col) % 2 == 1;
	}

	/**
	 * Draws the row numbers down the left side of the board.
	 */
	private void drawRowLabels(Graphics graphics) {
		graphics.setColor(Color.BLACK);
		
		for (int row = 0; row < Board.BOARD_SIZE; row++) {
			int labelX = x0 - 20;
			int labelY = 2 * y0 + row * colLen;
			String rowTitle = (char) ('8' - row) + "";
			graphics.drawString(rowTitle, labelX, labelY);
		}
	}

	/**
	 * Draws the column letters across the top of the board.
	 */
	private void drawColumnLabels(Graphics graphics) {
		graphics.setColor(Color.BLACK);
		
		for (int col = 0; col < Board.BOARD_SIZE; col++) {
			int labelx = 3 * x0 / 2 + col * rowLen;
			int labely = y0 - 5;
			String colTitle = (char) ('a' + col) + "";
			graphics.drawString(colTitle, labelx, labely);
		}
	}
	
	/**
	 * Draws a grey cell.
	 */
	private void drawGreyCell(Graphics graphics, int cellXOnScreen, int cellYOnScreen) {
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.fillRect(cellXOnScreen + border, cellYOnScreen + border, colLen - border, rowLen - border);
	}
	
	/**
	 * Draws a white cell.
	 */
	private void drawWhiteCell(Graphics graphics, int cellXOnScreen, int cellYOnScreen) {
		graphics.setColor(Color.WHITE);
		graphics.fillRect(cellXOnScreen + border, cellYOnScreen + border, colLen - border, rowLen - border);
	}
	
	/**
	 * Checks if a cell is selected, or if it is a square that can be moved to. In either case highlights the cell the appropriate colour.
	 */
	private void highlightCellIfNecessary(Graphics graphics, Cell cell, int cellXOnScreen, int cellYOnScreen) {
		if (cell.isSelected()) {
			graphics.setColor(HIGHLIGHT);
			graphics.fillRect(cellXOnScreen, cellYOnScreen, colLen, rowLen);
			
		} else if (cell.isNextMove()) {
			graphics.setColor(NEXTMOVE);
			graphics.fillRect(cellXOnScreen, cellYOnScreen, colLen, rowLen);
		}
	}
}
