package tps.tp4.pieces;

import java.awt.Color;
import java.awt.Point;

import tps.tp4.Board;
import tps.tp4.Game;
import tps.tp4.Game.Direction;

/**
 * Spider class
 */
public class Spider extends Piece {
	final static private Color color = new Color(0xA62D00);

	/**
	 * constructor
	 */
	public Spider(Game game, boolean isFromPlayerA) {
		super("Spider", color, game, isFromPlayerA);
	}

	/**
	 * Move this piece to x,y if doesn't violate the rules.
	 * 
	 * The Spider must move exactly 3 different steps. Should not violate the one
	 * hive rule and the physical possible move rule in each step.
	 */
	public boolean moveTo(int x, int y) {

		// execute search for all the coordinates, with limit of 3 steps
		boolean reachable = false;
		for (Direction direc : Direction.values()) {
			Point p = Board.getNeighbourPoint(getX(), getY(), direc);
			if (p == null)
				continue;
			if (toGo(p.x, p.y, x, y, 3, direc)) {
				reachable = true;
				break;
			}
		}

		if (!reachable) {
			return false;
		}

		// move if one hive rule checked
		boolean moved = moveWithOnehiveRuleChecked(x, y);

		if (moved) {
			game.moveUnconditional(this, x, y);
		}
		return moved;
	}

	/**
	 * Find if current Spider can move in 3 steps to the final position. For each
	 * step it decreases the value toMove. If it is zero that means and is not the
	 * destiny, that means that the Spider doesn't arrived at the destination by
	 * this path, We must try all the paths.
	 */
	private boolean toGo(int x, int y, int endX, int endY, int move, Direction lastDirec) {
		if (move > 3)
			return false;
		int auxX = x, auxY = y;
		while (move != 0) {

			boolean canMove = game.canPhysicallyMoveTo(auxX, auxY, lastDirec);
			// move if one hive rule checked
			boolean moved = false;
			if (canMove) {
				moved = game.getBoard().justOneHive(auxX, auxY);
			}
			if (!moved)
				return false;
			move--;

			Point p = Board.getNeighbourPoint(auxX, auxY, lastDirec);
			if (p == null)
				continue;
			if (auxX == endX && auxY == endY)
				move = 0;
			else {
				auxX = p.x;
				auxY = p.y;
			}

		}
		if (move == 0 && auxX == endX && auxY == endY)
			return true;
		return false;
	}
}