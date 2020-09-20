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
		
		boolean moved = moveWithOnehiveRuleChecked(x, y);

		if (moved) {
			game.setStatusInfo("The Piece "+ this.getName()+" moved!");
			game.moveUnconditional(this, x, y);
			return true;
		}
		return false;
	}
	
	
	private boolean toGo(int x, int y, int endX, int endY, Direction direc){
		
		if(Board.getNeighbourPoint(x, y, direc) == null) return false;
	
		
		if(game.getBoard().getPiece(x, y) == null) {
			if(x == endX && y == endY) return true;
			
			else return false;
			
		}
		if(toGo((int)Board.getNeighbourPoint(x, y, direc).x, Board.getNeighbourPoint(x, y, direc).y, endX, endY, direc)) return true;
		
		return false;
	}
}