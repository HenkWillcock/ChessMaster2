package chess;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import javax.swing.JFrame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ControllerTests {
	
	private Controller controller;
	private Board board;
	private Movement movement;
	private GraphicsHandler graphicsHandler;
	
	@BeforeEach
	void initialiseController() {
		this.board = new Board(true);
		this.movement = board.getMovement();
		this.graphicsHandler = new GraphicsHandler(board, 50, 50, 75, 75, 1);
		this.controller = new Controller(board, graphicsHandler, 50, 50, 75, 75);
	}
	
	/**
	 * Test method for {@link chess.Controller#setGameMode()}.
	 */
	@Test
	void setGameModeExceptionTest() {
		try {
			this.controller.setGameMode(0);
			fail();
		} catch (Exception e) {
			//Pass.
		}
	}
	
	/**
	 * Test method for {@link chess.Controller#setGameMode()}.
	 */
	@Test
	void setGameModeTest() {
		try {
			this.controller.setGameMode(1);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		assertEquals(this.controller.gameMode(), 1);
	}
	
	/**
	 * Test method for {@link chess.Controller#setAI()}.
	 */
	@Test 
	void setAIExceptionTest() {
		try {
			this.controller.setAI(null);
			fail();
		} catch (Exception e) {
			//Pass
		}
	}
	
	/**
	 * Test method for {@link chess.Controller#setAI()}.
	 */
	@Test 
	void setAITest() {
		Board board = new Board(true);
		try {
			this.controller.setAI(new AI(board, "White", new Movement(board)));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
