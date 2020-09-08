package tps.tp4.pieces;

import java.awt.Color;
import java.awt.Point;

import tps.tp4.Board;
import tps.tp4.Game;
import tps.tp4.Game.Direction;

/**
 * Beetle class
 */
public class Beetle extends Piece {
	final static private Color color = Color.magenta;

	private boolean isAbove;

	/**
	 * constructor
	 */
	public Beetle(Game game, boolean isFromPlayerA) {
		super("Beetle", color, game, isFromPlayerA);
	}

	/**
	 * Move this piece to x,y if doesn't violate the rules.
	 * 
	 * The Beetle can move only one step and be placed on top on another piece(s).
	 * Should not violate the one hive rule.
	 */
	public boolean moveTo(int x, int y) {
		boolean reachable = false;
		// execute search for all the coordinates
		for (Direction direc : Direction.values()) {
			Point p = Board.getNeighbourPoint(getX(), getY(), direc);
			if (p == null)
				continue;
			if (p.getX() == x && p.getY() == y) {
				reachable = true;
				break;
			}
		}
		if (!reachable)
			return false;
		if (game.getBoard().getPiece(x, y) != null) {
			if (!isAbove) {
				game.getPlayerData(this.isFromPlayerA());
			}
		}
		// move if one hive rule checked
		boolean moved = moveOneCheckedStep(x, y);
		if (moved) {
			System.out.println("Piece " + this + " with (x,y) of (" + getX() + ", " + getY() + ") moved to (" + x + ", "
					+ y + ")");
			game.moveUnconditional(this, x, y);
		}
		return moved;
	}
}