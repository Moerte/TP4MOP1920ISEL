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
	private boolean isBelow;
	/**
	 * constructor
	 */
	public Piece(String name, Color color, Game game, boolean isFromPlayerA) {
		this.name = name;
		this.game = game;
		this.isFromPlayerA = isFromPlayerA;
		this.color = color;
		this.isBelow = false;
	}

	/**
	 * toString
	 */
	public String toString() {
		return "The piece " + getName() + " with color " + getColor()+ " is from player A? -> " + isFromPlayerA();
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
		for(int i = x - 1; i <= x + 1; i++) {
			for(int j = y - 1; j <= j + 1; j++) {
				if(!game.getBoard().isInside(i, j))
					continue;
				if(x == i && y == j)
					continue;
				if(game.getBoard().getBoardPlace(i, j).getPiece() != null)
					return true;
			}
		}
		return false;
	}

	/**
	 * move one step if it is verify the rules
	 */
	protected boolean moveOneCheckedStep(int x, int y) {
		
		//TODO
		return false; //caso não seja
	}

	/**
	 * move to the destination if the move from the current position to the
	 * destiny doesn't violate the one hive rule. It can move several steps.
	 */
	protected boolean moveWithOnehiveRuleChecked(int x, int y) {
		if(moveTo(x,y) == true) { //caso o movimento seja de acordo com as regras
			setXY(x,y);
			return true;
		}
		return false; //caso não seja
	}

	/**
	 * get the direction from start coordinates to destiny coordinates
	 */
	protected static Direction getDirection(int fromX, int fromY, int toX,
			int toY) {

		for (Direction d : Direction.values()) {
			Point p = Board.getNeighbourPoint(fromX, fromY, d);
			if (p == null)
				continue;
			if (p.x == toX && p.y == toY) {
				return d;
			}
		}
		return null;
	}

}
