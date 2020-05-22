package tps.tp4.pieces;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import tps.tp4.Board;
import tps.tp4.BoardPlace;
import tps.tp4.Game;
import tps.tp4.Game.Direction;

/**
 * Ant class
 */
public class Ant extends Piece {
	final static private Color color = Color.blue;

	/**
	 * constructor
	 */
	public Ant(Game game, boolean isFromPlayerA) {
		super("", null, null, false);
		// TODO
	}

	/**
	 * move this piece to x,y if doesn't violate the rules
	 * 
	 * The Ant must move any numbers of steps. Should not violate the one hive
	 * rule and the physical possible move rule in each step.
	 */
	public boolean moveTo(int x, int y) {
		// TODO
		return false;
	}

	/**
	 * Find if current Ant can move in any number of steps to the final
	 * position. The Spider should try all the paths. But must prevent loops,
	 * For that, it receives a ArrayList with the BoardPlaces that we already
	 * moved. If the new one is already there, that means that is a loop, so it
	 * must abandon that path.
	 */
	private boolean findPlace(int nextX, int nextY, int xFinal, int yFinal,
			Direction lastDirection, ArrayList<BoardPlace> pathList) {
		// TODO
		return false;
	}
}