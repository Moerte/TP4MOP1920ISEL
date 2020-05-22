package tps.tp4.pieces;

import java.awt.Color;

import tps.tp4.Game;

/**
 * QueenBee class
 */
public class QueenBee extends Piece {
	final static private Color color = Color.yellow;

	/**
	 * constructor
	 */
	public QueenBee(Game game, boolean isFromPlayerA) {
		super("", color, null, false);
		// TODO
	}

	/**
	 * Move this piece to x,y if doesn't violate the rules.
	 * 
	 * The QueenBee can move only one step. Should not violate the one hive rule
	 * and the physical possible move rule.
	 * 
	 */
	public boolean moveTo(int x, int y) {
		// TODO
		return false;
	}

}