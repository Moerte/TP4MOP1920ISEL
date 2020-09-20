package tps.tp4.pieces;

import java.awt.Color;

import tps.tp4.Game;
import tps.tp4.Game.Direction;

/**
 * Ant class
 */
public class Ant extends Piece {
	final static private Color color = Color.blue;

	/**
	 * constructor
	 */
	public Ant(Game game, boolean isFromPlayerA) {
		super("Ant", color, game, isFromPlayerA);
	}

	/**
	 * move this piece to x,y if doesn't violate the rules
	 * 
	 * The Ant must move any numbers of steps. Should not violate the one hive rule
	 * and the physical possible move rule in each step.
	 */
	public boolean moveTo(int x, int y) {
		if(game.getBoard().getPiece(x, y) != null) return false;
		boolean canMove = false;
		Direction direc = Piece.getDirection(getX(), getY(), x, y);
		if(game.canPhysicallyMoveTo(x, y, direc)) canMove = true;
		boolean moved = false;
		if(canMove) moved = moveWithOnehiveRuleChecked(x, y);

		if (moved) {
			game.moveUnconditional(this, x, y);
		}
		return moved;

	}

}