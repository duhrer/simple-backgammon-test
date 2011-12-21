package com.anthonyatkins.simplebackgammon.test;

import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;

import com.anthonyatkins.simplebackgammon.Constants;
import com.anthonyatkins.simplebackgammon.activity.TestActivity;
import com.anthonyatkins.simplebackgammon.db.DbOpenHelper;
import com.anthonyatkins.simplebackgammon.db.DbUtils;
import com.anthonyatkins.simplebackgammon.exception.InvalidMoveException;
import com.anthonyatkins.simplebackgammon.model.Game;
import com.anthonyatkins.simplebackgammon.model.Match;
import com.anthonyatkins.simplebackgammon.model.Player;

public class ActivityTests extends ActivityInstrumentationTestCase2<TestActivity> {
	public ActivityTests() {
		super("com.anthonyatkins.simplebackgammon.activity", TestActivity.class);
	}

	public void testGameSaveAndLoad() throws InvalidMoveException {
		Match match = new Match(new Player(), new Player(), 1);
		Game baselineGame = new Game(match,Constants.BLACK);
		
		// detect the valid moves and make the first possible one
		baselineGame.getCurrentTurn().makeMove(baselineGame.getCurrentTurn().getPotentialMoves().get(0));
		
    	DbOpenHelper dbHelper = new DbOpenHelper(getActivity());
		
    	SQLiteDatabase db = dbHelper.getWritableDatabase();
    	long gameId = DbUtils.saveGame(baselineGame, db);
		Game restoredGame = DbUtils.getGameById(match, gameId, db);
		DbUtils.deleteGame(baselineGame, db);
		db.close();
		
		assertTrue("Restored game is not equal to saved game.", baselineGame.equals(restoredGame));
		
		SQLiteDatabase db2 = dbHelper.getWritableDatabase();
		long secondGameId = DbUtils.saveGame(restoredGame, db2);
		Game twiceRestoredGame = DbUtils.getGameById(match, secondGameId, db2);
		DbUtils.deleteGame(restoredGame, db2);
		db2.close();
		
		assertTrue("Twice restored game is not equal to original game.", baselineGame.equals(twiceRestoredGame));
	}
}
