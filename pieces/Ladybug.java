package tps.tp4.pieces;

import java.awt.Color;
import java.awt.Point;

import tps.tp4.Board;
import tps.tp4.Game;
import tps.tp4.Game.Direction;

/**
 * Beetle class
 */
public class Ladybug extends Piece {
	final static private Color color = Color.red;

	/**
	 * constructor
	 */
	public Ladybug(Game game, boolean isFromPlayerA) {
		super("Ladybug", color, game, isFromPlayerA);
	}

	/**
	 * Move this piece to x,y if doesn't violate the rules.
	 * 
	 * The Beetle can move only one step and be placed on top on another piece(s).
	 * Should not violate the one hive rule.
	 */
	public boolean moveTo(int x, int y) {
		if (game.getBoard().getPiece(x, y) != null) {
			game.setStatusInfo("Invalid move - the destiny must be empty");
			return false;
		}

		// execute search for all the coordinates, with limit of 3 steps
		boolean found = false;
		for (Direction d : Direction.values()) {
			Point p = Board.getNeighbourPoint(getX(), getY(), d);
			if (p == null)
				continue;

			if (findPlace(p.x, p.y, x, y, 3)) {
				found = true;
				break;
			}
		}

		if (!found) {
			game.setStatusInfo("Invalid move - the destiny can't be reached in 3 valid steps");
			game.moveUnconditional(this, getX(), getY());
			return false;
		}

		// move if one hive rule checked
		boolean moved = moveWithOnehiveRuleChecked(x, y);

		if (moved) {
			System.out.println("Piece " + this + " with (x,y) of (" + getX() + ", " + getY() + ") moved to (" + x + ", "
					+ y + ")");
			game.moveUnconditional(this, getX(), getY());
		}
		return moved;
	}

	/**
	 * TODO Mudar esta descrição
	 * Find if current Spider can move in 3 steps to the final position. For each
	 * step it decreases the value toMove. If it is zero that means and is not the
	 * destiny, that means that the Spider doesn't arrived at the destination by
	 * this path, We must try all the paths.
	 */
	private boolean findPlace(int thisX, int thisY, int xFinal, int yFinal, int toMove) {

		if (toMove == 3 && game.getBoard().getPiece(thisX, thisY) == null)
			return false;

		toMove -= 1;
		if (toMove == 0 && thisX == xFinal && thisY == yFinal)
			return true;
		if (toMove == 2) {
			for (Direction d : Direction.values()) {
				Point p = Board.getNeighbourPoint(getX(), getY(), d);
				if (game.getBoard().getPiece(p.x, p.y) != null && game.getBoard().getPiece(p.x, p.y) != this
						&& game.getBoard().justOneHive(p.x, p.y)) {
					if (findPlace(p.x, p.y, xFinal, yFinal, toMove))
						return true;
				}
			}
		} else if (toMove == 1) {
			for (Direction d : Direction.values()) {
				Point p = Board.getNeighbourPoint(getX(), getY(), d);
				if (game.getBoard().getPiece(p.x, p.y) != null && game.getBoard().justOneHive(p.x, p.y)) {
					if (findPlace(p.x, p.y, xFinal, yFinal, toMove))
						return true;
				}
			}
		}
		return false;
	}
}