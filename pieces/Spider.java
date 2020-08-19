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

			if (findPlace(p.x, p.y, x, y, 3, d)) {
				found = true;
				break;
			}
		}

		if (!found) {
			game.setStatusInfo("Invalid move - the destiny can't be reached in 3 valid steps");
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
	 * Find if current Spider can move in 3 steps to the final position. For each
	 * step it decreases the value toMove. If it is zero that means and is not the
	 * destiny, that means that the Spider doesn't arrived at the destination by
	 * this path, We must try all the paths.
	 */
	private boolean findPlace(int thisX, int thisY, int xFinal, int yFinal, int toMove, Direction lastDirection) {
		boolean first = false;
		if (toMove == 3)
			first = true;
		
		toMove -= 1;
		if(toMove == 1) {
			for (Direction d : Direction.values()) {
				Point p = Board.getNeighbourPoint(getX(), getY(), d);
				if(game.getBoard().getPiece(p.x, p.x) == this) return false;
			}
			
		}
		if(toMove == 0 && thisX == xFinal && thisY == yFinal) return true;
		Direction notDirection = null;
		switch (lastDirection) {
		case N:
			notDirection = Direction.S;
			break;
		case NE: 
			notDirection = Direction.SO;
			break;
		case NO:
			notDirection = Direction.SE;
			break;
		case S:
			notDirection = Direction.N;
			break;
		case SE: 
			notDirection = Direction.NO;
			break;
		case SO:
			notDirection = Direction.NE;
			break;
		}
		if(game.getBoard().getPiece(thisX, thisY) == null && toMove > 0 && game.canPhysicallyMoveTo(thisX, thisY, notDirection)) {
			if(first) notDirection = null;
			for(Direction d : Direction.values()){
				if(d != notDirection) {
					Point p = Board.getNeighbourPoint(thisX, thisY, d);
					if(p != null && game.getBoard().getPiece(p.x, p.y) == null && game.canPhysicallyMoveTo(thisX, thisY, d) && game.getBoard().justOneHive(p.x, p.y)) {
						if(findPlace(p.x, p.y, xFinal, yFinal, toMove, d)) return true;
					}
				}
			}
			
		}
		return false;
	}

}