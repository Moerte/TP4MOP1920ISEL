package tps.tp4.pieces;

import java.awt.Color;
import java.awt.Point;

import tps.tp4.Board;
import tps.tp4.Game;
import tps.tp4.Game.Direction;

/**
 * PillBug class
 */
public class PillBug extends Piece {
	final static private Color color = Color.cyan;
	private Piece currentHolding;

	/**
	 * constructor
	 */
	public PillBug(Game game, boolean isFromPlayerA) {
		super("PillBug", color, game, isFromPlayerA);
		currentHolding = null;
	}

	/**
	 * Move this piece to x,y if doesn't violate the rules.
	 * 
	 * The PillBug can move only one step. Should not violate the one hive rule.
	 */
	public boolean moveTo(int x, int y) {
		boolean reachable = false;
		if (currentHolding == null) {
			Piece piece = game.getBoard().getPiece(x, y);
			if (piece == null) {
				
				// execute search for all the coordinates
				for (Direction direc : Direction.values()) {
					Point p = Board.getNeighbourPoint(getX(), getY(), direc);
					if (p == null)
						continue;
					if (p.getX() == x && p.getY() == y) {
						reachable = true;
						break;
					}
				}
				if (!reachable)
					return false;
				// move if one hive rule checked
				boolean moved = moveWithOnehiveRuleChecked(x, y);
				if (moved) {
					System.out.println("Piece " + this + " with (x,y) of (" + getX() + ", " + getY() + ") moved to ("
							+ x + ", " + y + ")");
					game.moveUnconditional(this, x, y);
				}

				return moved;
			} else {
				for (Direction direc : Direction.values()) {
					Point p = Board.getNeighbourPoint(getX(), getY(), direc);
					if (p == null)
						continue;
					boolean moved = moveWithOnehiveRuleChecked(x, y);
					
					if (x == p.x && y == p.y && !game.getLastMoved().equals(piece) && moved) {
						if (game.getBoard().getBoardPlace(x, y).getNumPieces() > 1)
							return false;
						currentHolding = game.getBoard().getPiece(x, y);
						pickupPiece(currentHolding);
						return false;
					}
				}
			}
		}else {
			Piece piece = game.getBoard().getPiece(x, y);
			if(piece != null) return false;
			for (Direction direc : Direction.values()) {
				Point p = Board.getNeighbourPoint(getX(), getY(), direc);
				if (p == null)
					continue;
				if (p.getX() == x && p.getY() == y) {
					reachable = true;
					break;
				}
			}
			if (!reachable)
				return false;
			dropPiece(currentHolding, x, y);
			currentHolding = null;
			return true;
		}
		return false;
	}

	private void pickupPiece(Piece holdingPiece) {
		game.getBoard().remPiece(holdingPiece);
		game.getBoard().addPiece(holdingPiece, this.getX(), this.getY());
		game.getBoard().repaint();

	}
	
	private void dropPiece(Piece holdingPiece,int x,int y) {
		game.getBoard().remPiece(holdingPiece);
		game.getBoard().addPiece(holdingPiece, x, y);
		game.getBoard().repaint();

	}
}