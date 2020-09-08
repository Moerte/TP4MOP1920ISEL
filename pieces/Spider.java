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
	private boolean toGo(int x, int y, int endX, int endY, int move, Direction lastDirec) {
		boolean a = false;
		if (move == 3) {
			a = true;
		}		
		move -= 1;
		if(move == 1) {
			for (Direction direc1 : Direction.values()) {
				Point p1 = Board.getNeighbourPoint(getX(), getY(), direc1);
				if(game.getBoard().getPiece(p1.x, p1.x) == this) return false;
			}
			
		}
		if(move == 0 && x == endX && y == endY) return true;
		
		Direction direc2 = null;
		switch (lastDirec) {
		case N:
			direc2 = Direction.S;
			break;
		case S:
			direc2 = Direction.N;
			break;
		case NE: 
			direc2 = Direction.SO;
			break;
		case SE: 
			direc2 = Direction.NO;
			break;
		case NO:
			direc2 = Direction.SE;
			break;
		case SO:
			direc2 = Direction.NE;
			break;
		}
		if(game.getBoard().getPiece(x, y) == null && move > 0 && game.canPhysicallyMoveTo(endX, endY, lastDirec)) {
			if(a) direc2 = null;
			for(Direction direc3 : Direction.values()){
				if(direc3 != direc2) {
					Point p2 = Board.getNeighbourPoint(x, y, direc3);
					if(p2 != null && game.getBoard().getPiece(p2.x, p2.y) == null && game.canPhysicallyMoveTo(x, y, direc3) && game.getBoard().justOneHive(p2.x, p2.y)) {
						if(toGo(p2.x, p2.y, endX, endY, move, direc3)) return true;
					}
				}
			}	
		}
		return false;
	}
}