package tps.tp4.pieces;

import java.awt.Color;
import java.awt.Point;

import tps.tp4.Board;
import tps.tp4.Game;
import tps.tp4.Game.Direction;

public abstract class Piece {

	final public static int DIMPIECE = 25;

	private boolean isFromPlayerA;
	private int x = -1, y = -1;
	protected Game game;

	private String name;
	private Color color;

	/**
	 * constructor
	 */
	public Piece(String name, Color color, Game game, boolean isFromPlayerA) {
		this.name = name;
		this.game = game;
		this.isFromPlayerA = isFromPlayerA;
		this.color = color;
	}

	/**
	 * toString
	 */
	public String toString() {
		return "The piece " + getName() + " with color " + getColor() + " is from player A? -> " + isFromPlayerA();
	}

	public String getName() {
		return this.name;
	}

	/**
	 * get color
	 */
	public Color getColor() {
		return this.color;
	}

	/**
	 * get if piece if from player A or not
	 */
	public boolean isFromPlayerA() {
		return this.isFromPlayerA;
	}

	/**
	 * set xy
	 */
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * get x
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * set y
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * move this piece to x,y if doesn't violate the rules
	 */
	public abstract boolean moveTo(int x, int y);

	/**
	 * checks if the x, y received position have one neighbor that is not me
	 */
	protected boolean haveValidNeighbour(int x, int y) {
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= j + 1; j++) {
				if (!game.getBoard().isInside(i, j))
					continue;
				if (x == i && y == j)
					continue;
				if (searchPiece(i, j))
					return true;
			}
		}
		return false;
	}

	protected boolean searchPiece(int x, int y) {
		if (game.getBoard().getPiece(x, y) != null)
			return true;
		return false;
	}

	/**
	 * move one step if it is verify the rules
	 */
	protected boolean moveOneCheckedStep(int x, int y) {	
		int currentX = -1, currentY = -1;
		Direction d = null;
		if(currentX < 0) {
			d = getDirection(getX(), getY(), x, y);
			Point p = Board.getNeighbourPoint(getX(), getY(), d);
			currentX = (int)p.getX();
			currentY = (int)p.getY();
		}
		while(currentX != x && currentY != y) {
			System.out.println(game.getBoard().justOneHive(x, y));
			if(!game.canPhysicallyMoveTo(currentX, currentY, d)||!game.getBoard().justOneHive(x, y)) {
				return false;
			}
			d = getDirection(currentX, currentY, x, y);
			Point p = Board.getNeighbourPoint(currentX, currentY, d);
			currentX = (int)p.getX();
			currentY = (int)p.getY();
		
		}
		return true;
	}

	/**
	 * move to the destination if the move from the current position to the destiny
	 * doesn't violate the one hive rule. It can move several steps.
	 */
	protected boolean moveWithOnehiveRuleChecked(int x, int y) {
		int originalX = getX(), originalY = getY();
		game.moveUnconditional(this, x, y);
		boolean oneHive = game.getBoard().justOneHive(x, y);
		if(!oneHive) game.moveUnconditional(this, originalX, originalY);
		else game.getBoard().repaint();
		return oneHive;
	}

	/**
	 * get the direction from start coordinates to destiny coordinates
	 */
	protected static Direction getDirection(int fromX, int fromY, int toX, int toY) {

		Direction d1 = null;
		if( fromX != toX || fromY != toY) {
			if(fromX % 2 == 0) {
				if(toX > fromX && toY < fromY)
					d1 = Direction.NE;
				else if(toX > fromX)
					d1 = Direction.SE;
				else if(toX == fromX && toY < fromY)
					d1 = Direction.N;
				else if(toX == fromX && toY > fromY)
					d1 = Direction.S;
				else if(toX < fromX && toY < fromY)
					d1 = Direction.NO;
				else
					d1 = Direction.SO;
			} else {
				if(toX > fromX && toY > fromY)
					d1 = Direction.SE;
				else if(toX > fromX)
					d1 = Direction.NE;
				else if(toX == fromX && toY < fromY)
					d1 = Direction.N;
				else if(toX == fromX && toY > fromY)
					d1 = Direction.S;
				else if(toX < fromX && toY > fromY)
					d1 = Direction.SO;
				else
					d1 = Direction.NO;
			}
		}
		return d1;
	}

}
