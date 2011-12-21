package com.anthonyatkins.simplebackgammon.test;

import java.util.Iterator;

import junit.framework.TestCase;

import com.anthonyatkins.simplebackgammon.Constants;
import com.anthonyatkins.simplebackgammon.exception.InvalidMoveException;
import com.anthonyatkins.simplebackgammon.model.Board;
import com.anthonyatkins.simplebackgammon.model.Dugout;
import com.anthonyatkins.simplebackgammon.model.Game;
import com.anthonyatkins.simplebackgammon.model.GameDie;
import com.anthonyatkins.simplebackgammon.model.Match;
import com.anthonyatkins.simplebackgammon.model.Move;
import com.anthonyatkins.simplebackgammon.model.Moves;
import com.anthonyatkins.simplebackgammon.model.Piece;
import com.anthonyatkins.simplebackgammon.model.Player;
import com.anthonyatkins.simplebackgammon.model.SimpleDice;
import com.anthonyatkins.simplebackgammon.model.SimpleDie;
import com.anthonyatkins.simplebackgammon.model.Slot;
import com.anthonyatkins.simplebackgammon.model.Turn;

public class SomeTests extends TestCase {

	public void testDieEquals() throws Throwable {
		SimpleDie die = new SimpleDie(2, Constants.WHITE);
		SimpleDie die2 = new SimpleDie(2, Constants.WHITE);
		SimpleDie die6 = new SimpleDie(6, Constants.WHITE);
		
		assertFalse("Dice with different values are equal", die2.equals(die6));
		
		assertTrue("Dice with the same value are not equal", die.equals(die2));
	}
	
	public void testDiceEquals() throws Throwable {
		SimpleDice dice1 = new SimpleDice(2,3,Constants.BLACK);
		SimpleDice dice2 = new SimpleDice(2,3,Constants.BLACK);
		SimpleDice dice3 = new SimpleDice(4,5,Constants.BLACK);
		
		// Test to see if dice with the same roll in the same order are the same
		assertTrue("Dice rolled with the same preset values are not equal", dice1.equals(dice2));
				
		// Test to see if dice with different values are not equal
		assertFalse("A roll of 4 and 5 is equal to a roll of 2 and 3", dice3.equals(dice2));
		
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
		Player player1 = new Player("Some Dude",1); 
		Player player2 = new Player("Some Dude",2); 
		Player player3 = new Player("Another Dude",3);
		
		assertTrue("A single player object isn't even equal to itself", player1.equals(player1));
		
		assertFalse("Two players with the same name and different ids are equal", player1.equals(player2));
		
		assertFalse("Two players with different names and ids are equal", player1.equals(player3));
	}
	
	public void testGameEquals() throws Throwable {
		Match match1 = new Match(new Player("Black Player"),new Player("White Player"),1);
		Game game1 = new Game(match1,Constants.BLACK);

		// Test to make sure the same object equals itself
		assertTrue("Game object is not equal to itself", game1.equals(game1));

		Match match2 = new Match(new Player("Black Player"),new Player("White Player"),1);
		Game game2 = new Game(match2,Constants.BLACK);

		// Games won't be equal unless we make the dice equal
		game1.getCurrentTurn().getDice().roll(1,1);
		game1.getCurrentTurn().findAllPotentialMoves();
		game2.getCurrentTurn().getDice().roll(1,1);
		game2.getCurrentTurn().findAllPotentialMoves();
		
		// Test to make sure that two default games are equal to one another
		assertTrue("Two default games are not equal to one another", game1.equals(game2));

		// Test to make sure that a game with a different piece arrangement is not equal;
		int[] bothPlayersCanMoveOutConfiguration = {0,-3,-3,-3,-3,-3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,3,3,3,3,0,0,0};

		Match match3 = new Match(new Player("Black Player"),new Player("White Player"),1);
		Game game3 = new Game(match3,Constants.BLACK);
		game3.getBoard().initializeSlots(bothPlayersCanMoveOutConfiguration);
		game3.getCurrentTurn().findAllPotentialMoves();
		assertFalse("Different boards are mistakenly detected as equal", game2.equals(game3));
	}
	
	public void testMoveEquals() throws Throwable {
		Match match = new Match(new Player(),new Player(),1);
		Game game = new Game(match,Constants.WHITE);

		Player player = new Player();
		Slot startSlot = new Slot(Slot.DOWN,1, game);
		Slot endSlot = new Slot(Slot.DOWN,0, game);
		Dugout dugout = new Dugout(-1,Constants.WHITE,game);
		
		Turn turn = new Turn(player,game,Constants.WHITE);
		turn.findAllPotentialMoves();
		GameDie die1 = new GameDie(1, Constants.WHITE, turn);
		GameDie die2 = new GameDie(2, Constants.WHITE, turn);
		GameDie die6 = new GameDie(6, Constants.WHITE, turn);

		
		// Test to see if two moves by the same player, from the same square to the same square are equal
		Move move1 = new Move(startSlot,endSlot,die1, player);
		Move move2 = new Move(startSlot,endSlot,die1, player);
		assertTrue("Two identical moves are not equal",move1.equals(move2));
		
		// Test to see if two moves with the same start and end square but different dice
		// This happens a lot in the end game.
		Move move3 = new Move(startSlot,dugout,die2, player);
		Move move4 = new Move(startSlot,dugout,die6, player);
		assertFalse("Two different moves equal",move3.equals(move4));
	}
	
	/* *
	 * Moves used to be a tree set, which screwed things up for doubles (same start/end point).
	 * The tests are left here to test the equals method rather than the uniqueness of the entries.
	 */
	public void testMovesEquals() throws Throwable {
		Player whitePlayer = new Player();
		Match match = new Match(whitePlayer,whitePlayer,1);
		Game game = new Game(match,Constants.WHITE);
		Slot startSlot = new Slot(Slot.DOWN,1, game);
		Slot startSlot2 = new Slot(Slot.DOWN,2, game);
		Slot endSlot = new Slot(Slot.DOWN,2, game);
		Slot endSlot2 = new Slot(Slot.DOWN,3, game);
		Dugout dugout = new Dugout(-1,Constants.WHITE,game);

		
		Turn turn = new Turn(whitePlayer,game,Constants.WHITE);
		turn.findAllPotentialMoves();
		
		GameDie die = new GameDie(1, Constants.WHITE, turn);
		GameDie die2 = new GameDie(2, Constants.WHITE, turn);
		GameDie die6 = new GameDie(6, Constants.WHITE, turn);		
		
		
		Moves moves1 = new Moves();
		Moves moves2 = new Moves();
		
		moves1.add(new Move(startSlot, endSlot, die, whitePlayer));
		moves2.add(new Move(startSlot, endSlot, die, whitePlayer));
		
		assertTrue("Two sets of moves with the same lone entry don't match", moves1.equals(moves2));
		
		// Test variations to make sure that sets allow the right kind of variations
		
		// different start slots
		Moves moves4 = new Moves();
		moves4.add(new Move(startSlot,dugout,die2, whitePlayer));
		moves4.add(new Move(startSlot2,dugout,die6, whitePlayer));
		assertEquals("Moves with different start slots are not treated as distinct",2,moves4.size());

		
		// different end slots
		Moves moves5 = new Moves();
		moves5.add(new Move(startSlot,dugout,die2, whitePlayer));
		moves5.add(new Move(startSlot,endSlot2,die6, whitePlayer));
		assertEquals("Moves with different end slots are not treated as distinct",2,moves5.size());
		
		
		// Test to make sure that moves with the same start and end point but 
		// different dice aren't screened out
		Moves moves6 = new Moves();
		
		moves6.add(new Move(startSlot,dugout,die2, whitePlayer));
		moves6.add(new Move(startSlot,dugout,die6, whitePlayer));
		assertEquals("Moves with different dice are not treated as distinct",2,moves6.size());
	}
	
	public void testTurnEquals()  throws Throwable {
		Player blackPlayer = new Player();
		Player whitePlayer = new Player();
		Match match = new Match(blackPlayer,whitePlayer,1);
		Game game = new Game(match,Constants.BLACK);
		
		Turn turn1 = new Turn(blackPlayer, game,Constants.BLACK);
		turn1.findAllPotentialMoves();
		Turn turn2 = new Turn(turn1,game);
		turn2.findAllPotentialMoves();

		boolean equals = turn1.equals(turn2);
		assertTrue("Two turns with the same player and dice don't match.", equals);
		
		Turn turn3 = new Turn(whitePlayer, game,Constants.WHITE);
		turn3.findAllPotentialMoves();
		assertFalse("Two turns with different players and dice match.", turn1.equals(turn3));
	}
	
	public void testBoardStateSave() throws Throwable {
		Match match = new Match(new Player(),new Player(),1);
		Game game1 = new Game(match,Constants.BLACK);
		Board board1 = new Board(game1);
		Game game2 = new Game(match,Constants.BLACK);
		Board board2 = new Board(game2);
		
		// We have to manually set all the dice to the same thing, as they're random by default
		game1.getCurrentTurn().getDice().roll(1,1);
		game2.getCurrentTurn().getDice().roll(1,1);
		
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
		Match match = new Match(new Player(),new Player(),1);
		Game game = new Game(match,Constants.BLACK);
		
		Slot blackPairedSlot = new Slot(Slot.UP,0, game);
		blackPairedSlot.getPieces().addMultiple(2,Constants.BLACK,0);
		assertTrue("Slot with 2 black pieces failed to block white piece",blackPairedSlot.isBlocked(Constants.WHITE));
		assertFalse("Slot with 2 black pieces failed to allow black piece",blackPairedSlot.isBlocked(Constants.BLACK));

		Slot blackUnpairedSlot = new Slot(Slot.UP,0, game);
		blackUnpairedSlot.getPieces().addMultiple(1,Constants.BLACK,0);
		assertFalse("Slot with 1 black piece failed to block white piece",blackUnpairedSlot.isBlocked(Constants.WHITE));
		assertFalse("Slot with 1 black piece failed to allow black piece",blackUnpairedSlot.isBlocked(Constants.BLACK));

		Slot whitePairedSlot = new Slot(Slot.UP,0, game);
		whitePairedSlot.getPieces().addMultiple(2,Constants.WHITE,0);
		assertTrue("Slot with 2 white pieces failed to block black piece",whitePairedSlot.isBlocked(Constants.BLACK));
		assertFalse("Slot with 2 white pieces failed to allow white piece",whitePairedSlot.isBlocked(Constants.WHITE));
		
		Slot whiteUnpairedSlot = new Slot(Slot.UP,0, game);
		whiteUnpairedSlot.getPieces().addMultiple(1,Constants.WHITE,0);
		assertFalse("Slot with 1 black piece failed to block white piece",whiteUnpairedSlot.isBlocked(Constants.BLACK));
		assertFalse("Slot with 1 black piece failed to allow black piece",whiteUnpairedSlot.isBlocked(Constants.WHITE));
	}

	
	public void testHighestAndLowestPiece() throws Throwable {
		/* Test Available moves with the default pieces, it's just easier */
		Match match = new Match(new Player(),new Player(),1);
		Game game = new Game(match,Constants.BLACK);

		assertEquals("White's lowest starting slot was not 5.",5,game.getBoard().getWhitePieces().first().position);
		assertEquals("White's highest starting slot was not 23.",23,game.getBoard().getWhitePieces().last().position);
		assertEquals("Black's lowest starting slot was not 0.",0,game.getBoard().getBlackPieces().first().position);
		assertEquals("Black's highest starting slot was not 18.",18,game.getBoard().getBlackPieces().last().position);
	}
		
	/* Test whether or not a piece on the fifth spot can move out with a roll of six. */
	public void testMovingTrailingPieceOut() throws Throwable {
		int[] bothPlayersCanMoveOutConfiguration = {0,-3,-3,-3,-3,-3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,3,3,3,3,0,0,0};
		
		Match match = new Match(new Player(),new Player(),1);
		Game game = new Game(match,Constants.BLACK);
		game.getBoard().initializeSlots(bothPlayersCanMoveOutConfiguration);
		
		Player blackPlayer = game.getBlackPlayer();
		new Turn(blackPlayer,game,Constants.BLACK); 
		game.getCurrentTurn().getDice().roll(6,6);
		game.getCurrentTurn().findAllPotentialMoves();
		
		Moves expectedBlackMoves = new Moves();
		expectedBlackMoves.add(new Move(game.getBoard().getPlaySlots().get(19),game.getBoard().getBlackOut(),game.getCurrentTurn().getDice().get(0), blackPlayer));
		
		assertTrue("Black player can't move out of the 20th slot with a 6.",expectedBlackMoves.equals(game.getCurrentTurn().getPotentialMoves()));
		
		Player whitePlayer = game.getWhitePlayer();
		new Turn(whitePlayer,game,Constants.WHITE); 
		game.getCurrentTurn().getDice().roll(6,6);
		game.getCurrentTurn().findAllPotentialMoves();

		Moves expectedWhiteMoves = new Moves();
		expectedWhiteMoves.add(new Move(game.getBoard().getPlaySlots().get(4),game.getBoard().getWhiteOut(),game.getCurrentTurn().getDice().get(0), whitePlayer));
		
		assertTrue("White player can't move out of the 5th slot with a 6.",expectedWhiteMoves.equals(game.getCurrentTurn().getPotentialMoves()));
	}
	
	public void testGetAvailableMovesFromSlot() throws Throwable {
		/* Test Available moves with the default pieces, it's just easier */
		Player blackPlayer = new Player();
		Player whitePlayer = new Player();
		Match match = new Match(blackPlayer,whitePlayer,1);
		Game game = new Game(match,Constants.BLACK);
		
		game.getCurrentTurn().getDice().roll(1, 5);
		
		Slot trailingBlackSlot = game.getBoard().getPlaySlots().get(0);
		Moves expectedBlackSlotMoves = new Moves();
		expectedBlackSlotMoves.add(new Move(trailingBlackSlot, game.getBoard().getPlaySlots().get(1),game.getCurrentTurn().getDice().get(0), blackPlayer));
		Moves blackDetectedMoves = game.getCurrentTurn().findAllPotentialMoves().getMovesForStartSlot(trailingBlackSlot);
		assertTrue("Starting black moves with a roll of 1 and 5 failed", expectedBlackSlotMoves.equals(blackDetectedMoves));


		Turn whiteTurn = new Turn(whitePlayer,game,Constants.WHITE);
		whiteTurn.findAllPotentialMoves();
		game.getCurrentTurn().getDice().roll(1, 5);
		
		Slot trailingWhiteSlot = game.getBoard().getPlaySlots().get(23);
		Moves expectedWhiteSlotMoves = new Moves();
		expectedWhiteSlotMoves.add(new Move(trailingWhiteSlot, game.getBoard().getPlaySlots().get(22),game.getCurrentTurn().getDice().get(0), whitePlayer));
		Moves whiteDetectedMoves = game.getCurrentTurn().findAllPotentialMoves().getMovesForStartSlot(trailingWhiteSlot);
		assertTrue("Starting white moves with a roll of 1 and 5 failed", expectedWhiteSlotMoves.equals(whiteDetectedMoves));
	}

	public void testPlayerCanGoOut() throws Throwable {
		Player whitePlayer = new Player();
		Player blackPlayer = whitePlayer;
		Match match = new Match(blackPlayer,whitePlayer,1);
		Game game = new Game(match,Constants.BLACK);
		
		/* test the default pieces, neither player should be able to go out */
		assertFalse("Black player was able to go out from the starting position.",game.playerCanMoveOut());

		Turn whiteTurn = new Turn(whitePlayer,game,Constants.WHITE);
		whiteTurn.findAllPotentialMoves();
		assertFalse("White player was able to go out from the starting position.",game.playerCanMoveOut());

		/* testing with both players ready to go out */
		int[] bothPlayersCanMoveOutConfiguration = {0,-3,-3,-3,-3,-3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,3,3,3,3,0,0,0};
		game.getBoard().initializeSlots(bothPlayersCanMoveOutConfiguration);
		Turn blackTurn = new Turn(blackPlayer,game,Constants.BLACK);
		blackTurn.findAllPotentialMoves();
		assertTrue("Black player was not able to go out with all pieces in the end row.",game.playerCanMoveOut());
		
		Turn whiteTurn2 = new Turn(whitePlayer,game,Constants.WHITE);
		whiteTurn2.findAllPotentialMoves();
		assertTrue("White player was not able to go out with all pieces in the end row.",game.playerCanMoveOut());
	}
	
	
	public void testGetAvailableMovesFromBar() throws Throwable {
		int[] blackStuckOnBarConfiguration = {0,-2,-2,-2,-2,-2,-2,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,0,2,0};
		int[] whiteStuckOnBarConfiguration = {0,-2,-2,-2,-2,-2,-2,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,0,0,2};

		int[] blackCanMoveOffBarConfiguration = {0,-2,-2,0,0,-2,-2,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,0,2,0};
		int[] whiteCanMoveOffBarConfiguration = {0,-2,-2,-2,-2,-2,-2,0,0,0,0,0,0,0,0,0,0,0,0,2,2,0,0,2,2,0,0,2};

		Player blackPlayer = new Player();
		Player whitePlayer = new Player();
		Match match = new Match(blackPlayer,whitePlayer,1);
		Game game = new Game(match,Constants.BLACK);

		game.getBoard().initializeSlots(blackStuckOnBarConfiguration);
		game.getCurrentTurn().getDice().roll(3, 4);
		game.getCurrentTurn().findAllPotentialMoves();
		
		/* the list should be empty, we have intentionally blocked the moves of both players */
		assertTrue("Black failed to be stuck on bar when all slots are taken", game.getCurrentTurn().getPotentialMoves().size() == 0);

		game.getBoard().initializeSlots(blackCanMoveOffBarConfiguration);
		game.getCurrentTurn().findAllPotentialMoves();
		Moves expectedBlackMoves = new Moves();
		expectedBlackMoves.add(new Move(game.getBoard().getBar(),game.getBoard().getPlaySlots().get(2),game.getCurrentTurn().getDice().get(0), blackPlayer));
		expectedBlackMoves.add(new Move(game.getBoard().getBar(),game.getBoard().getPlaySlots().get(3),game.getCurrentTurn().getDice().get(1), blackPlayer));
		assertTrue("Black failed to be able to move off the bar into an empty slot.",expectedBlackMoves.equals(game.getCurrentTurn().getPotentialMoves()));

		game.getCurrentTurn().getDice().roll(5, 6);
		game.getCurrentTurn().findAllPotentialMoves();
		assertTrue("Black failed to be stuck on bar when roll doesn't match free slots",game.getCurrentTurn().getPotentialMoves().size() == 0);
		
		new Turn(game.getBlackPlayer(),game,Constants.WHITE);
		game.getCurrentTurn().getDice().roll(3, 4);
		game.getBoard().initializeSlots(whiteStuckOnBarConfiguration);
		game.getCurrentTurn().findAllPotentialMoves();
		
		/* the list should be empty, we have intentionally blocked the moves of both players */
		assertTrue("White failed to be stuck on bar when all entry slots are blocked.", game.getCurrentTurn().getPotentialMoves().size() == 0);

		game.getBoard().initializeSlots(whiteCanMoveOffBarConfiguration);
		game.getCurrentTurn().findAllPotentialMoves();
		Moves expectedWhiteMoves = new Moves();
		expectedWhiteMoves.add(new Move(game.getBoard().getBar(),game.getBoard().getPlaySlots().get(21),game.getCurrentTurn().getDice().get(0), whitePlayer));
		expectedWhiteMoves.add(new Move(game.getBoard().getBar(),game.getBoard().getPlaySlots().get(20),game.getCurrentTurn().getDice().get(1), whitePlayer));
		assertTrue("White failed to be able to move off the bar into an empty slot.",expectedWhiteMoves.equals(game.getCurrentTurn().getPotentialMoves()));

		game.getCurrentTurn().getDice().roll(5, 6);
		game.getCurrentTurn().findAllPotentialMoves();
		assertTrue("White failed to be stuck on bar when roll doesn't match free slots",game.getCurrentTurn().getPotentialMoves().size() == 0);
	}

	public void testPlayerWon() throws Throwable {
		int[] blackWonConfiguration = {3,-2,-2,-2,-2,-2,-2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,15,0,0};
		int[] whiteWonConfiguration = {15,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,3,0,0};
		
		Player blackPlayer = new Player();
		Player whitePlayer = new Player();
		Match match = new Match(blackPlayer,whitePlayer,1);
		Game game = new Game(match,Constants.BLACK);
		
		/* check to make sure the black player wins when all their pieces are in the dugout */
		game.getBoard().initializeSlots(blackWonConfiguration);
		assertTrue(game.playerWon());

		/* check to make sure the white player wins when all their pieces are in the dugout */
		Turn whiteTurn = new Turn(whitePlayer,game,Constants.WHITE);
		whiteTurn.findAllPotentialMoves();
		game.getBoard().initializeSlots(whiteWonConfiguration);
		assertTrue(game.playerWon());		
		
		/* check to make sure the black player doesn't win when they only have a few pieces in the dugout */
		Turn blackTurn = new Turn(blackPlayer,game,Constants.BLACK);
		blackTurn.findAllPotentialMoves();
		game.getBoard().initializeSlots(whiteWonConfiguration);
		assertFalse(game.playerWon());

		/* check to make sure the white player doesn't win when they only have a few pieces in the dugout */
		Turn whiteTurn2 = new Turn(whitePlayer,game,Constants.WHITE);
		whiteTurn2.findAllPotentialMoves();
		game.getBoard().initializeSlots(blackWonConfiguration);
		assertFalse(game.playerWon());
	}
	
	public void testDiceFlagging() throws Throwable {
		/* A configuration where each player only has their trailing pieces from the initial setup */
		int[] allMovesPossibleConfiguration = {0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-2,0,0,0};
		
		Player whitePlayer = new Player();
		Match match = new Match(new Player(),whitePlayer,1);
		Game game = new Game(match,Constants.BLACK);

		game.getBoard().initializeSlots(allMovesPossibleConfiguration);
		assertTrue("Black failed to have any moves from a starting position where all moves are possible",game.getCurrentTurn().getPotentialMoves().size() > 0);
		
		Iterator<SimpleDie> blackDiceIterator = game.getCurrentTurn().getDice().iterator();
		while (blackDiceIterator.hasNext()) { blackDiceIterator.next().setUsed(); }
		game.getCurrentTurn().findAllPotentialMoves();
		assertTrue("Black has moves even when all dice are flagged as used",game.getCurrentTurn().getPotentialMoves().size() == 0);
		
		new Turn(whitePlayer,game,Constants.WHITE);
		game.getCurrentTurn().findAllPotentialMoves();
		assertTrue("White failed to have any moves from a starting position where all moves are possible",game.getCurrentTurn().getPotentialMoves().size() > 0);
		
		Iterator<SimpleDie> whiteDiceIterator = game.getCurrentTurn().getDice().iterator();
		while (whiteDiceIterator.hasNext()) { whiteDiceIterator.next().setUsed(); }
		game.getCurrentTurn().findAllPotentialMoves();
		assertTrue("White has moves even when all dice are flagged as used",game.getCurrentTurn().getPotentialMoves().size() == 0);
	}
	
	public void testCloning() throws Throwable {
		Match match = new Match(new Player(),new Player(),1);
		Game baselineGame = new Game(match,Constants.BLACK);

		// set the basic conditions
		baselineGame.getCurrentTurn().getDice().roll(1, 2);
		baselineGame.getCurrentTurn().findAllPotentialMoves();
				
		// clone the game
		Game modifiedGame = new Game(baselineGame);

		// Check to see if the games are equal
		assertTrue("Cloned game is not a faithful copy of the original", baselineGame.equals(modifiedGame));
	}
	
	public void testMoveAndUndo() throws Throwable {
		Player blackPlayer = new Player();
		Match match = new Match(blackPlayer,blackPlayer,1);
		Game baselineGame = new Game(match,Constants.BLACK);
		
		// set the basic conditions
		baselineGame.getCurrentTurn().getDice().roll(1, 2);
		baselineGame.getCurrentTurn().findAllPotentialMoves();
		
		// clone the game
		Game modifiedGame = new Game(baselineGame);
		
		// Test making and then undoing a single move
		modifiedGame.getCurrentTurn().makeMove(0,1);
		
		assertFalse("Modified game is still equal to original.", baselineGame.equals(modifiedGame));
		
		modifiedGame.getCurrentTurn().undoMove();
		modifiedGame.getCurrentTurn().undoMove();

		assertTrue("Modified game is not equal to original after undoing two moves.", baselineGame.equals(modifiedGame));
	}

	public void testMoveDoublesAndUndo() throws Throwable {
		Player blackPlayer = new Player();
		Match match = new Match(blackPlayer,new Player(),1);
		Game baselineGame = new Game(match,Constants.BLACK);
		
		// set the basic conditions
		baselineGame.getCurrentTurn().getDice().roll(2, 2);
		baselineGame.getCurrentTurn().findAllPotentialMoves();
		
		baselineGame.getCurrentTurn().makeMove(0,2);
		baselineGame.getCurrentTurn().makeMove(0,2);
		
		// clone the game
		Game modifiedGame = new Game(baselineGame);
		assertTrue("Cloned game isn't equal to original.", baselineGame.equals(modifiedGame));
		
		// Test making and then undoing a double move
		modifiedGame.getCurrentTurn().makeMove(2,4);
		modifiedGame.getCurrentTurn().makeMove(2,4);
		
		assertFalse("Modified game is still equal to original.", baselineGame.equals(modifiedGame));
		
		modifiedGame.getCurrentTurn().undoMove();
		modifiedGame.getCurrentTurn().undoMove();
		
		assertTrue("Modified game is not equal to original after undoing a pair of moves when rolling doubles.", baselineGame.equals(modifiedGame));
	}
	
	public void testUndoFromBar() throws InvalidMoveException {
		int[] blackCanMoveOffBarConfiguration = {0,-2,-2,0,0,-2,-2,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,0,2,0};
		
		Match match = new Match(new Player(),new Player(),1);
		Game game = new Game(match,Constants.BLACK);
		game.getBoard().initializeSlots(blackCanMoveOffBarConfiguration);
		game.getCurrentTurn().getDice().roll(3, 4);
		game.getCurrentTurn().findAllPotentialMoves();

		Game clonedGame = new Game(game);
		
		assertTrue("No potential moves found from bar, can't test undoing.",game.getCurrentTurn().getPotentialMoves().size() > 0);

		game.getCurrentTurn().makeMove(game.getCurrentTurn().getPotentialMoves().get(0));
		game.getCurrentTurn().undoMove();
		
		assertTrue("Undoing a move from the bar doesn't revert the game to its previous state",game.equals(clonedGame));
	}
}
