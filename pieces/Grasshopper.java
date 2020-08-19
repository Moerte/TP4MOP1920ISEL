package tps.tp4.pieces;

import java.awt.Color;
import java.awt.Point;

import tps.tp4.Board;
import tps.tp4.Game;
import tps.tp4.Game.Direction;

/**
 * Grasshopper class
 */
public class Grasshopper extends Piece {
	final static private Color color = new Color(70, 90, 40);

	/**
	 * constructor
	 */
	public Grasshopper(Game game, boolean isFromPlayerA) {
		super("Grasshopper", color, game, isFromPlayerA);
	}

	/**
	 * Move this piece to x,y if doesn't violate the rules.
	 * 
	 * The Grasshopper must move in strait jumps over at least one piece (but not
	 * empty places). Should not violate the one hive rule.
	 */
	public boolean moveTo(int x, int y) {
		if (game.getBoard().getPiece(x, y) != null) {
			game.setStatusInfo("Invalid move - the destiny must be empty");
			return false;
		}

		Direction d = getDirection(getX(), getY(), x, y);
		Point p = Board.getNeighbourPoint(getX(), getY(), d);
		if(p == null) {
			return false;
		}
		
		Piece piece = game.getBoard().getPiece((int)p.getX(), (int)p.getY());
		if(piece == null) {
			return false;
		}


		// move if one hive rule checked
		boolean moved = moveWithOnehiveRuleChecked(x, y);

		if (moved) {
			System.out.println("Piece " + this + " with (x,y) of (" + getX() + ", " + getY() + ") moved to (" + x + ", "
					+ y + ")");
			game.moveUnconditional(this, x, y);
		}
		return moved;
	}
}