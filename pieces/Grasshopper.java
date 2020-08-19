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
		boolean found = false;
		for (Direction d : Direction.values()) {
			Point p = Board.getNeighbourPoint(getX(), getY(), d);
			if(game.getBoard().getPiece(p.x, p.y) != null) {
				if(findPlace(getX(), getY(), x, y, d)) {
					found = true;
					break;
				}
			}
		}
		if(!found) {
			game.setStatusInfo("OPS! Invalid move - The Grasshopper can't movet to that position");
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
	
	/**
	 * TODO Mudar esta descrição e ver a função getNeighbourPoint porque devolve double
	 * Find if current Spider can move in 3 steps to the final position. For each
	 * step it decreases the value toMove. If it is zero that means and is not the
	 * destiny, that means that the Spider doesn't arrived at the destination by
	 * this path, We must try all the paths.
	 */
	private boolean findPlace(int thisX, int thisY, int xFinal, int yFinal, Direction d) {
		if(Board.getNeighbourPoint(thisX, thisY, d) == null) return false;
		
		if(game.getBoard().getPiece(thisX, thisY) == null) {
			if(thisX == xFinal && thisY == yFinal) return true;
			else return false;
		}
		if(findPlace((int)Board.getNeighbourPoint(thisX, thisY, d).x, Board.getNeighbourPoint(thisX, thisY, d).y, xFinal, yFinal, d)) return true;
		return false;
	}
}