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
	private Piece currentHolding = null;

	/**
	 * constructor
	 */
	public PillBug(Game game, boolean isFromPlayerA) {
		super("PillBug", color, game, isFromPlayerA);
	}

	/**
	 * Move this piece to x,y if doesn't violate the rules.
	 * 
	 * The PillBug can move only one step. Should not violate the one hive rule.
	 */
	public boolean moveTo(int x, int y) {
		boolean reachable = false;
		int posX = getX(), posY = getY();
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
				
				Direction direc = Piece.getDirection(getX(), getY(), x, y);
				
				boolean canMove = game.canPhysicallyMoveTo(x, y, direc);
				// move if one hive rule checked
				boolean moved = false;
				if(canMove) {
					moved = moveWithOnehiveRuleChecked(x, y);
				}
				if (moved) {
					game.moveUnconditional(this, x, y);
				}

				return moved;
			} else {
				for (Direction direc : Direction.values()) {
					Point p = Board.getNeighbourPoint(posX, posY, direc);
					if (p == null)
						continue;
					
					int originalX = piece.getX(), originalY = piece.getY();
					game.moveUnconditional(piece, getX(), getY());
					boolean moved = game.getBoard().justOneHive(getX(), getY());
					if(!moved) game.moveUnconditional(piece, originalX, originalY);

					if (x == p.x && y == p.y &&(game.getLastMoved() == null || !game.getLastMoved().equals(piece)) && moved) {
						if (game.getBoard().getBoardPlace(x, y).getNumPieces() > 2)
							return false;
						currentHolding = piece; 
						game.getBoard().repaint();
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
	
	private void dropPiece(Piece holdingPiece,int x,int y) {
		game.getBoard().remPiece(holdingPiece);
		game.getBoard().addPiece(holdingPiece, x, y);
		game.getBoard().repaint();

	}
}