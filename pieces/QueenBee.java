package tps.tp4.pieces;

import java.awt.Color;
import java.awt.Point;

import tps.tp4.Board;
import tps.tp4.Game;
import tps.tp4.Game.Direction;

/**
 * QueenBee class
 */
public class QueenBee extends Piece {
	final static private Color color = Color.yellow;

	/**
	 * constructor
	 */
	public QueenBee(Game game, boolean isFromPlayerA) {
		super("QueenBee", color, game, isFromPlayerA);
	}

	/**
	 * Move this piece to x,y if doesn't violate the rules.
	 * 
	 * The QueenBee can move only one step. Should not violate the one hive rule
	 * and the physical possible move rule.
	 * 
	 */
	public boolean moveTo(int x, int y) {
		if (game.getBoard().getPiece(x, y) != null) {
			game.setStatusInfo("Invalid move - the destiny must be empty");
			return false;
		}

		boolean reachable = false;
		// execute search for all the coordinates
		for (Direction d : Direction.values()) {
			Point p = Board.getNeighbourPoint(getX(), getY(), d);
			if (p == null)
				continue;
			if(p.getX() == x && p.getY() == y) {
				reachable = true;
				break;
			}
		}
		if(!reachable) return false;
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