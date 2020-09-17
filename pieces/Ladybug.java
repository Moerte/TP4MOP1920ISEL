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
		// execute search for all the coordinates
		for (Direction direc : Direction.values()) {
			Point p = Board.getNeighbourPoint(getX(), getY(), direc);
			if (p == null)
				continue;
			if (game.getBoard().getPiece(p.x, p.y) != null) {
				if (toGo(getX(), getY(), x, y, direc)) {
					reachable = true;
					break;
				}
				
			}
		}	
		if (!reachable) {
			game.setStatusInfo("OPS! Invalid move - The "+this.getName()+" can't move to that position");
			return false;
		}
		

		// move if one hive rule checked
		boolean moved = moveWithOnehiveRuleChecked(x, y);

		if (moved) {
			game.setStatusInfo("The Piece "+ this.getName()+" moved!");
			System.out.println("Piece " + this + " with (x,y) of (" + getX() + ", " + getY() + ") moved to (" + x + ", " + y + ")");
			game.moveUnconditional(this, x, y);
			return true;
		}
		return false;
	}
	
	
	private boolean toGo(int x, int y, int endX, int endY, Direction direc){
		
		if(Board.getNeighbourPoint(x, y, direc) == null) return false;
	
		
		if(game.getBoard().getPiece(x, y) != null) {
			if((endX == x + 1 || endX == x - 1) && endY == y - 2) return true;

			else return false;			
		}
		if(toGo((int)Board.getNeighbourPoint(x, y, direc).x, Board.getNeighbourPoint(x, y, direc).y, endX, endY, direc)) return true;
		
		return false;
	}
}