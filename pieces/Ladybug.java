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

	
	public boolean moveTo(int x, int y) {
		
		boolean reachable = false;
		
		for (Direction direc : Direction.values()) {
			Point p = Board.getNeighbourPoint(getX(), getY(), direc);
			if (p == null)
				continue;

			if (toGo(p.x, p.y, x, y, 2, direc)) { //ladybug can basically move up to 2 steps in any direction
				reachable = true;
				break;
			}
		}
		/*
		for (Direction direc : Direction.values()) {
			Point p = Board.getNeighbourPoint(getX(), getY(), direc);
			if (p == null)
				continue;
			if (x == p.x && y > p.y + 3) {
				reachable = false;
			}
			else {
				toGo(p.x, p.y, x, y, 3, direc); { //ladybug can basically move up to 2 steps in any direction
				reachable = true;	
				} 
				break;
			}
		}
		*/
		if (!reachable) {
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

	
	private boolean toGo(int x, int y, int endX, int endY, int move, Direction lastDirec) {
		boolean a = false;
		if(move == 2) {
			a = true;
		}
		move -= 1;
		
		if(move == 0 && x == endX && y == endY) return true;
		
		Direction direc2 = null;

		if(game.getBoard().getPiece(x, y) == null && move > 0 && game.canPhysicallyMoveTo(endX, endY, lastDirec)) {
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