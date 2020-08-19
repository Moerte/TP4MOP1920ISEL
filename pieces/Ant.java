package tps.tp4.pieces;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import tps.tp4.Board;
import tps.tp4.BoardPlace;
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

		if (game.getBoard().getPiece(x, y) != null) {
			game.setStatusInfo("Invalid move - the destiny must be empty");
			return false;
		}

		// execute search for all the coordinates, with limit of 3 steps
		boolean found = false;
		ArrayList<BoardPlace> pathList = new ArrayList<BoardPlace>();
		pathList.add(game.getBoard().getBoardPlace(getX(), getY()));
		if (findPlace(getX(), getY(), x, y, pathList)) {
			found = true;

		}
		game.moveUnconditional(this, x, y);
		if (!found) {
			game.setStatusInfo("Invalid move - the destiny can't be reached in 3 valid steps");
			return false;
		}

		// move if one hive rule checked
		boolean moved = moveWithOnehiveRuleChecked(x, y);

		if (moved) {
			System.out.println("Piece " + this + " with (x,y) of (" + getX() + ", " + getY() + ") moved to (" + x + ", "
					+ y + ")");

		}
		return moved;
	}

	/**
	 * Find if current Ant can move in any number of steps to the final position.
	 * The Spider should try all the paths. But must prevent loops, For that, it
	 * receives a ArrayList with the BoardPlaces that we already moved. If the new
	 * one is already there, that means that is a loop, so it must abandon that
	 * path.
	 */
	private boolean findPlace(int nextX, int nextY, int xFinal, int yFinal, ArrayList<BoardPlace> pathList) {
		if (nextX == xFinal && nextY == yFinal)
			return true;

		if (nextX != xFinal && nextY != yFinal) {
			for (Direction d : Direction.values()) {
				Point p = Board.getNeighbourPoint(getX(), getY(), d);
				if (game.getBoard().getPiece(p.x, p.y) != null
						&& !(pathList.contains(game.getBoard().getBoardPlace(p.x, p.y)))) {
					if (game.canPhysicallyMoveTo(nextX, nextY, d)) {
						pathList.add(game.getBoard().getBoardPlace(p.x, p.y));
						if (findPlace(p.x, p.y, xFinal, yFinal, pathList))
							return true;
					}
				}
			}
		}

		return false;
	}
}