package tps.tp4.pieces;

import java.awt.Color;

import tps.tp4.Game;

/**
 * Beetle class
 */
public class Beetle extends Piece {
	final static private Color color = Color.magenta;

	/**
	 * constructor
	 */
	public Beetle(Game game, boolean isFromPlayerA) {
		super("Beetle", color, game, isFromPlayerA);
	}

	/**
	 * Move this piece to x,y if doesn't violate the rules.
	 * 
	 * The Beetle can move only one step and be placed on top on another
	 * piece(s). Should not violate the one hive rule.
	 */
	public boolean moveTo(int x, int y) {
		// TODO
		int pieceX = getX();
		int pieceY = getY();
		if((pieceX + 1) == x || (pieceY +1) == y) game.moveUnconditional(this, x, y);
		return true;
	}
}