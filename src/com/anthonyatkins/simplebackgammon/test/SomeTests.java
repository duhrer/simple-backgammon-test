package com.anthonyatkins.simplebackgammon.test;

import java.util.Iterator;

import android.test.AndroidTestCase;

import com.anthonyatkins.simplebackgammon.Constants;
import com.anthonyatkins.simplebackgammon.controller.GameController;
import com.anthonyatkins.simplebackgammon.model.Board;
import com.anthonyatkins.simplebackgammon.model.GameDice;
import com.anthonyatkins.simplebackgammon.model.GameDie;
import com.anthonyatkins.simplebackgammon.model.Dugout;
import com.anthonyatkins.simplebackgammon.model.Game;
import com.anthonyatkins.simplebackgammon.model.Move;
import com.anthonyatkins.simplebackgammon.model.Moves;
import com.anthonyatkins.simplebackgammon.model.Piece;
import com.anthonyatkins.simplebackgammon.model.Player;
import com.anthonyatkins.simplebackgammon.model.SimpleDice;
import com.anthonyatkins.simplebackgammon.model.SimpleDie;
import com.anthonyatkins.simplebackgammon.model.Slot;
import com.anthonyatkins.simplebackgammon.model.Turn;
import com.anthonyatkins.simplebackgammon.view.GameView;

public class SomeTests extends AndroidTestCase {
	
	public void testDieEquals() throws Throwable {
		Game game = new Game();
		Player player = new Player(Constants.WHITE,game);
		
		GameDie die = new GameDie(2, Constants.WHITE, game, player);
		GameDie die2 = new GameDie(2, Constants.WHITE, game, player);
		GameDie die6 = new GameDie(6, Constants.WHITE, game, player);
		
		assertFalse("Dice with different values are equal", die2.equals(die6));
		
		assertTrue("Dice with the same value are not equal", die.equals(die2));
	}
	
	public void testDiceEquals() throws Throwable {
		SimpleDice dice1 = new SimpleDice(Constants.BLACK);
		SimpleDice dice2 = new SimpleDice(Constants.BLACK);
		
		// Test to see if dice with the same roll in the same order are the same
		dice1.roll(2, 3);
		dice2.roll(2, 3);
		assertTrue("Dice rolled with the same preset values are not equal", dice1.equals(dice2));
				
		// Test to see if dice with different values are not equal
		dice2.roll(4, 5);
		assertFalse("A roll of 4 and 5 is equal to a roll of 2 and 3", dice1.equals(dice2));
		dice2.roll(3, 2);
		
		// Test to see if different colored dice with the same value are not equal
		dice1.setColor(Constants.WHITE);
		assertFalse("Different colored dice with the same values are equal to one another", dice1.equals(dice2));
		dice1.setColor(Constants.BLACK);
		
		// Test to see if dice that are used are not equal to dice that are unused
		for (int a=0; a<dice1.size();a++) { 
			SimpleDie die = dice1.get(a);
			die.setUsed(true); 
		}
		assertFalse("Dice that are used are equal to dice that are unused", dice1.equals(dice2));
	}
	
	public void testPlayerEquals() throws Throwable {
		Player player1 = new Player(Constants.BLACK, new Game()); 
		Player player2 = new Player(Constants.BLACK, new Game()); 
		Player player3 = new Player(Constants.WHITE, new Game());
		
		player1.getDice().roll(1,2);
		player2.getDice().roll(1,2);
		player3.getDice().roll(1,2);
		
		assertTrue("A single player object isn't even equal to itself", player1.equals(player1));
		
		assertTrue("Two players of the same color and with the same roll are not equal", player1.equals(player2));
		
		player2.getDice().roll(2,3);
		assertFalse("Two players of the same color and with different rolls are equal", player1.equals(player2));
		player2.getDice().roll(1,2);
		
		assertFalse("Two players with the same roll but different colors are equal", player2.equals(player3));
	}
	
	public void testGameEquals() throws Throwable {
		Game game1 = new Game();

		// Test to make sure the same object equals itself
		assertTrue("Game object is not equal to itself", game1.equals(game1));

		Game game2 = new Game();

		// Games won't be equal unless we make the dice equal
		game1.getBlackPlayer().getDice().roll(1, 1);
		game1.getWhitePlayer().getDice().roll(1, 1);
		game2.getBlackPlayer().getDice().roll(1, 1);
		game2.getWhitePlayer().getDice().roll(1, 1);
		
		// Test to make sure that two default games are equal to one another
		assertTrue("Two default games are not equal to one another", game1.equals(game2));

		// Test to make sure that a game with a different piece arrangement is not equal;
		int[] bothPlayersCanMoveOutConfiguration = {0,-3,-3,-3,-3,-3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,3,3,3,3,0,0,0};

		Game game3 = new Game();
		game3.getBoard().initializeSlots(bothPlayersCanMoveOutConfiguration);
		assertFalse("Different boards are mistakenly detected as equal", game2.equals(game3));
	}
	
	public void testMoveEquals() throws Throwable {
		Game game = new Game();
		Player player = new Player(Constants.WHITE, game);
		Slot startSlot = new Slot(Slot.DOWN,1);
		Slot endSlot = new Slot(Slot.DOWN,0);
		Dugout dugout = new Dugout(-1,Constants.WHITE,game);
		
		GameDie die1 = new GameDie(1, Constants.WHITE, game, player);
		GameDie die2 = new GameDie(2, Constants.WHITE, game, player);
		GameDie die6 = new GameDie(6, Constants.WHITE, game, player);

		// Test to see if two moves by the same player, from the same square to the same square are equal
		Move move1 = new Move(startSlot,endSlot,die1);
		Move move2 = new Move(startSlot,endSlot,die1);
		assertTrue("Two identical moves are not equal",move1.equals(move2));
		
		// Test to see if two moves with the same start and end square but different dice
		// This happens a lot in the end game.
		Move move3 = new Move(startSlot,dugout,die2);
		Move move4 = new Move(startSlot,dugout,die6);
		assertFalse("Two different moves equal",move3.equals(move4));
	}
	
	/* *
	 * Moves used to be a tree set, which screwed things up for doubles (same start/end point).
	 * The tests are left here to test the equals method rather than the uniqueness of the entries.
	 */
	public void testMovesEquals() throws Throwable {
		Game game = new Game();
		Player player = new Player(Constants.WHITE, game);
		Slot startSlot = new Slot(Slot.DOWN,1);
		Slot startSlot2 = new Slot(Slot.DOWN,2);
		Slot endSlot = new Slot(Slot.DOWN,2);
		Slot endSlot2 = new Slot(Slot.DOWN,3);
		Dugout dugout = new Dugout(-1,Constants.WHITE,game);
		GameDie die = new GameDie(1, Constants.WHITE, game, player);
		GameDie die2 = new GameDie(2, Constants.WHITE, game, player);
		GameDie die6 = new GameDie(6, Constants.WHITE, game, player);		
		
		Moves moves1 = new Moves();
		Moves moves2 = new Moves();
		
		moves1.add(new Move(startSlot, endSlot, die));
		moves2.add(new Move(startSlot, endSlot, die));
		
		assertTrue("Two sets of moves with the same lone entry don't match", moves1.equals(moves2));
		
		// Test variations to make sure that sets allow the right kind of variations
		
		// different start slots
		Moves moves4 = new Moves();
		moves4.add(new Move(startSlot,dugout,die2));
		moves4.add(new Move(startSlot2,dugout,die6));
		assertEquals("Moves with different start slots are not treated as distinct",2,moves4.size());

		
		// different end slots
		Moves moves5 = new Moves();
		moves5.add(new Move(startSlot,dugout,die2));
		moves5.add(new Move(startSlot,endSlot2,die6));
		assertEquals("Moves with different end slots are not treated as distinct",2,moves5.size());
		
		
		// Test to make sure that moves with the same start and end point but 
		// different dice aren't screened out
		Moves moves6 = new Moves();
		
		moves6.add(new Move(startSlot,dugout,die2));
		moves6.add(new Move(startSlot,dugout,die6));
		assertEquals("Moves with different dice are not treated as distinct",2,moves6.size());
	}
	
	public void testTurnEquals()  throws Throwable {
		Game game = new Game();
		Player player = new Player(Constants.BLACK, game);
		GameDice dice = new GameDice(Constants.BLACK, game, player);
		Turn turn1 = new Turn(player,dice);
		Turn turn2 = new Turn(player,dice);

		assertTrue("Two turns with the same player and dice don't match", turn1.equals(turn2));
	}
	
	public void testBoardStateSave() throws Throwable {
		Board board1 = new Board(new Game());
		Board board2 = new Board(new Game());
		
		// We have to manually set all the dice to the same thing, as they're random by default
		board1.getLeftPit().dice.roll(1,1);
		board1.getRightPit().dice.roll(1,1);
		board2.getLeftPit().dice.roll(1,1);
		board2.getRightPit().dice.roll(1,1);
		
		// Move the first piece from each slot two slots toward its goal
		for (Piece piece : board1.getBlackPieces()) {
			Slot sourceSlot = board1.getPlaySlots().get(piece.position);
			Slot destinationSlot = board1.getPlaySlots().get(piece.position + (piece.color * 2));
			sourceSlot.removePiece(piece);
			destinationSlot.addPiece(piece);
		}
		for (Piece piece : board1.getWhitePieces()) {
			Slot sourceSlot = board1.getPlaySlots().get(piece.position);
			Slot destinationSlot = board1.getPlaySlots().get(piece.position + (piece.color * 2));
			sourceSlot.removePiece(piece);
			destinationSlot.addPiece(piece);
		}
		
		board2.initializeSlots(board1.getBoardState());

		assertTrue("Board restored from saved state is not equal to the original", board1.equals(board2));
	}
	
	public void testIsBlocked() throws Throwable {
		Slot blackPairedSlot = new Slot(Slot.UP,0);
		blackPairedSlot.pieces.addMultiple(2,Constants.BLACK,0);
		assertTrue("Slot with 2 black pieces failed to block white piece",blackPairedSlot.isBlocked(Constants.WHITE));
		assertFalse("Slot with 2 black pieces failed to allow black piece",blackPairedSlot.isBlocked(Constants.BLACK));

		Slot blackUnpairedSlot = new Slot(Slot.UP,0);
		blackUnpairedSlot.pieces.addMultiple(1,Constants.BLACK,0);
		assertFalse("Slot with 1 black piece failed to block white piece",blackUnpairedSlot.isBlocked(Constants.WHITE));
		assertFalse("Slot with 1 black piece failed to allow black piece",blackUnpairedSlot.isBlocked(Constants.BLACK));

		Slot whitePairedSlot = new Slot(Slot.UP,0);
		whitePairedSlot.pieces.addMultiple(2,Constants.WHITE,0);
		assertTrue("Slot with 2 white pieces failed to block black piece",whitePairedSlot.isBlocked(Constants.BLACK));
		assertFalse("Slot with 2 white pieces failed to allow white piece",whitePairedSlot.isBlocked(Constants.WHITE));
		
		Slot whiteUnpairedSlot = new Slot(Slot.UP,0);
		whiteUnpairedSlot.pieces.addMultiple(1,Constants.WHITE,0);
		assertFalse("Slot with 1 black piece failed to block white piece",whiteUnpairedSlot.isBlocked(Constants.BLACK));
		assertFalse("Slot with 1 black piece failed to allow black piece",whiteUnpairedSlot.isBlocked(Constants.WHITE));
	}

	
	public void testHighestAndLowestPiece() throws Throwable {
		/* Test Available moves with the default pieces, it's just easier */
		Game game = new Game();
		assertEquals("White's lowest starting slot was not 5.",5,game.getWhitePlayer().getPieces().first().position);
		assertEquals("White's highest starting slot was not 23.",23,game.getWhitePlayer().getPieces().last().position);
		assertEquals("Black's lowest starting slot was not 0.",0,game.getBlackPlayer().getPieces().first().position);
		assertEquals("Black's highest starting slot was not 18.",18,game.getBlackPlayer().getPieces().last().position);
	}
		
	/* Test whether or not a piece on the fifth spot can move out with a roll of six. */
	public void testMovingTrailingPieceOut() throws Throwable {
		int[] bothPlayersCanMoveOutConfiguration = {0,-3,-3,-3,-3,-3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,3,3,3,3,0,0,0};
		
		Game game = new Game();
		GameView gameView = new GameView(this.getContext(),game);
		GameController gameController = new GameController(gameView);
		game.getBoard().initializeSlots(bothPlayersCanMoveOutConfiguration);
		
		game.setActivePlayer(game.getBlackPlayer());
		game.getActivePlayer().getDice().roll(6,6);

		Moves expectedBlackMoves = new Moves();
		expectedBlackMoves.add(new Move(game.getBoard().getPlaySlots().get(19),game.getBoard().getBlackOut(),game.getActivePlayer().getDice().get(0)));
		
		gameController.getAllPlayerMoves();
		assertTrue("Black player can't move out of the 20th slot with a 6.",expectedBlackMoves.equals(game.getActivePlayer().getMoves()));
		
		game.setActivePlayer(game.getWhitePlayer());
		game.getActivePlayer().getDice().roll(6,6);

		Moves expectedWhiteMoves = new Moves();
		expectedWhiteMoves.add(new Move(game.getBoard().getPlaySlots().get(4),game.getBoard().getWhiteOut(),game.getActivePlayer().getDice().get(0)));
		
		gameController.getAllPlayerMoves();
		assertTrue("White player can't move out of the 5th slot with a 6.",expectedWhiteMoves.equals(game.getActivePlayer().getMoves()));
	}
	
	public void testGetAvailableMovesFromSlot() throws Throwable {
		/* Test Available moves with the default pieces, it's just easier */
		Game game = new Game();
		GameView gameView = new GameView(this.getContext(),game);
		GameController gameController = new GameController(gameView);
		
		game.setActivePlayer(game.getBlackPlayer());
		game.getActivePlayer().getDice().roll(1, 5);
		
		Slot trailingBlackSlot = game.getBoard().getPlaySlots().get(0);
		Moves expectedBlackSlotMoves = new Moves();
		expectedBlackSlotMoves.add(new Move(trailingBlackSlot, game.getBoard().getPlaySlots().get(1),game.getActivePlayer().getDice().get(0)));
		gameController.getAvailableMovesFromSlot(trailingBlackSlot);
		assertTrue("Starting black moves with a roll of 1 and 5 failed", expectedBlackSlotMoves.equals(game.getActivePlayer().getMoves()));

		game.setActivePlayer(game.getWhitePlayer());
		game.getActivePlayer().getDice().roll(1, 5);

		Slot trailingWhiteSlot = game.getBoard().getPlaySlots().get(23);
		Moves expectedWhiteSlotMoves = new Moves();
		expectedWhiteSlotMoves.add(new Move(trailingWhiteSlot, game.getBoard().getPlaySlots().get(22),game.getActivePlayer().getDice().get(0)));
		gameController.getAvailableMovesFromSlot(trailingWhiteSlot);
		assertTrue("Starting white moves with a roll of 1 and 5 failed", expectedWhiteSlotMoves.equals(game.getActivePlayer().getMoves()));
	}

	public void testPlayerCanGoOut() throws Throwable {
		Game game = new Game();
		GameView gameView = new GameView(this.getContext(),game);
		GameController gameController = new GameController(gameView);
		
		/* test the default pieces, neither player should be able to go out */
		game.setActivePlayer(game.getBlackPlayer());
		assertFalse("Black player was able to go out from the starting position.",gameController.playerCanMoveOut());

		game.setActivePlayer(game.getWhitePlayer());
		assertFalse("White player was able to go out from the starting position.",gameController.playerCanMoveOut());

		/* testing with both players ready to go out */
		int[] bothPlayersCanMoveOutConfiguration = {0,-3,-3,-3,-3,-3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,3,3,3,3,0,0,0};
		game.getBoard().initializeSlots(bothPlayersCanMoveOutConfiguration);
		game.setActivePlayer(game.getBlackPlayer());
		assertTrue("Black player was not able to go out with all pieces in the end row.",gameController.playerCanMoveOut());
		
		game.setActivePlayer(game.getWhitePlayer());
		assertTrue("White player was not able to go out with all pieces in the end row.",gameController.playerCanMoveOut());
	}
	
	
	public void testGetAvailableMovesFromBar() throws Throwable {
		int[] blackStuckOnBarConfiguration = {0,-2,-2,-2,-2,-2,-2,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,0,2,0};
		int[] whiteStuckOnBarConfiguration = {0,-2,-2,-2,-2,-2,-2,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,0,0,2};

		int[] blackCanMoveOffBarConfiguration = {0,-2,-2,0,0,-2,-2,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,0,2,0};
		int[] whiteCanMoveOffBarConfiguration = {0,-2,-2,-2,-2,-2,-2,0,0,0,0,0,0,0,0,0,0,0,0,2,2,0,0,2,2,0,0,2};

		Game game = new Game();
		GameView gameView = new GameView(this.getContext(),game);
		GameController gameController = new GameController(gameView);

		game.setActivePlayer(game.getBlackPlayer());
		game.getActivePlayer().getDice().roll(3, 4);
		game.getBoard().initializeSlots(blackStuckOnBarConfiguration);
		
		/* the list should be empty, we have intentionally blocked the moves of both players */
		gameController.getAvailableMovesFromBar();
		assertTrue("Black failed to be stuck on bar when all slots are taken", game.getActivePlayer().getMoves().size() == 0);

		game.getBoard().initializeSlots(blackCanMoveOffBarConfiguration);
		Moves expectedBlackMoves = new Moves();
		expectedBlackMoves.add(new Move(game.getBoard().getBar(),game.getBoard().getPlaySlots().get(2),game.getActivePlayer().getDice().get(0)));
		expectedBlackMoves.add(new Move(game.getBoard().getBar(),game.getBoard().getPlaySlots().get(3),game.getActivePlayer().getDice().get(1)));
		gameController.getAvailableMovesFromBar();
		assertTrue("Black failed to be able to move off the bar into an empty slot.",expectedBlackMoves.equals(game.getActivePlayer().getMoves()));

		game.getActivePlayer().getDice().roll(5, 6);
		gameController.getAvailableMovesFromBar();
		assertTrue("Black failed to be stuck on bar when roll doesn't match free slots",game.getActivePlayer().getMoves().size() == 0);
		
		game.setActivePlayer(game.getWhitePlayer());
		game.getActivePlayer().getDice().roll(3, 4);
		game.getBoard().initializeSlots(whiteStuckOnBarConfiguration);
		
		/* the list should be empty, we have intentionally blocked the moves of both players */
		gameController.getAvailableMovesFromBar();
		assertTrue("White failed to be stuck on bar when all entry slots are blocked.", game.getActivePlayer().getMoves().size() == 0);

		game.getBoard().initializeSlots(whiteCanMoveOffBarConfiguration);
		Moves expectedWhiteMoves = new Moves();
		expectedWhiteMoves.add(new Move(game.getBoard().getBar(),game.getBoard().getPlaySlots().get(21),game.getActivePlayer().getDice().get(0)));
		expectedWhiteMoves.add(new Move(game.getBoard().getBar(),game.getBoard().getPlaySlots().get(20),game.getActivePlayer().getDice().get(1)));
		gameController.getAvailableMovesFromBar();
		assertTrue("White failed to be able to move off the bar into an empty slot.",expectedWhiteMoves.equals(game.getActivePlayer().getMoves()));

		game.getActivePlayer().getDice().roll(5, 6);
		gameController.getAvailableMovesFromBar();
		assertTrue("White failed to be stuck on bar when roll doesn't match free slots",game.getActivePlayer().getMoves().size() == 0);
	}

	public void testPlayerWon() throws Throwable {
		int[] blackWonConfiguration = {3,-2,-2,-2,-2,-2,-2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,15,0,0};
		int[] whiteWonConfiguration = {15,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,3,0,0};
		
		Game game = new Game();
		GameView gameView = new GameView(this.getContext(),game);
		GameController gameController = new GameController(gameView);
		
		/* check to make sure the black player wins when all their pieces are in the dugout */
		game.setActivePlayer(game.getBlackPlayer());
		game.getActivePlayer().getDice().roll(3, 4);
		game.getBoard().initializeSlots(blackWonConfiguration);
		assertTrue(gameController.playerWon());

		/* check to make sure the white player wins when all their pieces are in the dugout */
		game.setActivePlayer(game.getWhitePlayer());
		game.getActivePlayer().getDice().roll(3, 4);
		game.getBoard().initializeSlots(whiteWonConfiguration);
		assertTrue(gameController.playerWon());		
		
		/* check to make sure the black player doesn't win when they only have a few pieces in the dugout */
		game.setActivePlayer(game.getBlackPlayer());
		game.getActivePlayer().getDice().roll(3, 4);
		game.getBoard().initializeSlots(whiteWonConfiguration);
		assertFalse(gameController.playerWon());

		/* check to make sure the white player doesn't win when they only have a few pieces in the dugout */
		game.setActivePlayer(game.getWhitePlayer());
		game.getActivePlayer().getDice().roll(3, 4);
		game.getBoard().initializeSlots(blackWonConfiguration);
		assertFalse(gameController.playerWon());
	}
	
	public void testDiceFlagging() throws Throwable {
		/* A configuration where each player only has their trailing pieces from the initial setup */
		int[] allMovesPossibleConfiguration = {0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-2,0,0,0};
		
		Game game = new Game();
		GameView gameView = new GameView(getContext(),game);
		GameController gameController = new GameController(gameView);

		game.getBoard().initializeSlots(allMovesPossibleConfiguration);
		
		game.setActivePlayer(game.getBlackPlayer());
		
		game.getActivePlayer().getDice().roll();
		gameController.getAllPlayerMoves();
		assertTrue("Black failed to have any moves from a starting position where all moves are possible",game.getActivePlayer().getMoves().size() > 0);
		
		Iterator<SimpleDie> blackDiceIterator = game.getActivePlayer().getDice().iterator();
		while (blackDiceIterator.hasNext()) { blackDiceIterator.next().setUsed(); }
		gameController.getAllPlayerMoves();
		assertTrue("Black has moves even when all dice are flagged as used",game.getActivePlayer().getMoves().size() == 0);
		
		game.setActivePlayer(game.getWhitePlayer());
		
		game.getActivePlayer().getDice().roll();
		gameController.getAllPlayerMoves();
		assertTrue("White failed to have any moves from a starting position where all moves are possible",game.getActivePlayer().getMoves().size() > 0);
		
		Iterator<SimpleDie> whiteDiceIterator = game.getActivePlayer().getDice().iterator();
		while (whiteDiceIterator.hasNext()) { whiteDiceIterator.next().setUsed(); }
		gameController.getAllPlayerMoves();
		assertTrue("White has moves even when all dice are flagged as used",game.getActivePlayer().getMoves().size() == 0);
	}
	
	public void testCloning() throws Throwable {
		Game baselineGame = new Game();

		// set the basic conditions
		baselineGame.setActivePlayer(baselineGame.getBlackPlayer());
		baselineGame.getActivePlayer().getDice().roll(1, 2);
				
		// clone the game
		Game modifiedGame = new Game(baselineGame);

		// Check to see if the games are equal
		assertEquals("Cloned game is not a faithful copy of the original", baselineGame, modifiedGame);
	}
	
	public void testMoveAndUndo() throws Throwable {
		Game baselineGame = new Game();
		
		// set the basic conditions
		baselineGame.setActivePlayer(baselineGame.getBlackPlayer());
		baselineGame.getActivePlayer().getDice().roll(1, 2);
		baselineGame.setCurrentTurn(new Turn(baselineGame.getActivePlayer(),new GameDice(baselineGame.getActivePlayer().getDice())));
		GameView baselineGameView = new GameView(getContext(),baselineGame);
		GameController baselineGameController = new GameController(baselineGameView);
		baselineGameController.getAllPlayerMoves();
		baselineGameController.setGameState(Game.MOVE_PICK_SOURCE);
		
		// clone the game
		Game modifiedGame = new Game(baselineGame);
		GameView gameView = new GameView(getContext(),modifiedGame);
		GameController gameController = new GameController(gameView);
		
		// Test making and then undoing a single move
		gameController.getAllPlayerMoves();
		gameController.makeMove(0,1);
		
		assertFalse("Modified game is still equal to original.", baselineGame.equals(modifiedGame));
		
		gameController.undoMove();
		gameController.undoMove();
		gameController.setGameState(Game.MOVE_PICK_SOURCE);

		assertTrue("Modified game is not equal to original after undoing two moves.", baselineGame.equals(modifiedGame));
	}
	public void testMoveDoublesAndUndo() throws Throwable {
		Game baselineGame = new Game();
		
		// set the basic conditions
		baselineGame.setActivePlayer(baselineGame.getBlackPlayer());
		baselineGame.getActivePlayer().getDice().roll(2, 2);
		baselineGame.setCurrentTurn(new Turn(baselineGame.getActivePlayer(),new GameDice(baselineGame.getActivePlayer().getDice())));
		GameView baselineGameView = new GameView(getContext(),baselineGame);
		GameController baselineGameController = new GameController(baselineGameView);
		baselineGameController.getAllPlayerMoves();
		baselineGameController.setGameState(Game.MOVE_PICK_SOURCE);
		baselineGameController.getAllPlayerMoves();
		
		// clone the game
		Game modifiedGame = new Game(baselineGame);
		GameView gameView = new GameView(getContext(),modifiedGame);
		GameController gameController = new GameController(gameView);
		
		// Test making and then undoing a double move
		gameController.getAllPlayerMoves();
		gameController.makeMove(0,2);
		gameController.makeMove(0,2);
		gameController.makeMove(2,4);
		gameController.makeMove(2,4);
		
		assertFalse("Modified game is still equal to original.", baselineGame.equals(modifiedGame));
		
		gameController.undoMove();
		gameController.undoMove();
		gameController.setGameState(Game.MOVE_PICK_SOURCE);
		
		assertTrue("Modified game is not equal to original after undoing a pair of moves when rolling doubles.", baselineGame.equals(modifiedGame));
	}
}
