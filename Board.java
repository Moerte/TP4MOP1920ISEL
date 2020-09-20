package tps.tp4;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import tps.tp4.Game.Direction;
import tps.tp4.pieces.Piece;

public class Board extends JPanel {
	private static final long serialVersionUID = 1L;

	// background color for the board
	static Color BOARDBACKGROUNDCOLOR = new Color(0xC9F76F);

	// number of cells in the board
	public static final int DIMX = 31;
	public static final int DIMY = 15;

	// the board data - array 2D of BoardPlaces
	private BoardPlace[][] board;

	// game reference
	private Game game;

	// references for the two PlayerData
	private PlayerData playerAData, playerBData;

	// Font for the pieces
	private Font piecesFont;

	// methods ===============================================

	/**
	 * constructor
	 * 
	 * @param fontPieces
	 */
	public Board(Game game, Font piecesFont) {
		this.game = game;
		this.piecesFont = piecesFont;
		playerAData = game.getPlayerData(true);
		playerBData = game.getPlayerData(false);
		this.board = new BoardPlace[DIMX][DIMY];
		setPreferredSize(new Dimension(600, 400));
		initBoard();
	}

	/**
	 * Create the board places for pieces
	 */
	private void initBoard() {
		setBackground(BOARDBACKGROUNDCOLOR);
		setFont(piecesFont);
		for (int y = 0; y < DIMY; y++) {
			for (int x = 0; x < DIMX; x++) {
				board[x][y] = new BoardPlace(this, x, y);

			}
		}
		MouseListener ml = new MouseListener() {

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				int b = e.getButton();
				switch (b) {
				case MouseEvent.BUTTON1:
					int posX = e.getX();
					int posY = e.getY();
					clickOnBoard(posX, posY);
					break;
				case MouseEvent.BUTTON2:
					break;
				case MouseEvent.BUTTON3:
					break;
				default:
					break;
				}
			}

		};
		addMouseListener(ml);

	}

	/**
	 * method called by the mouseListener of the board. Should check if the x, y
	 * received is inside any of the polygons. In affirmative case should call
	 * game.clickOnBoard with the (x, y) of the BoardPlace clicked
	 */
	private void clickOnBoard(int xPix, int yPix) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j].isInsideBoardPlace(xPix, yPix)) {
					game.clickOnBoard(i, j);
					return;
				}
			}
		}

	}

	/**
	 * clears the board data - clear all the pieces on board
	 */
	public void resetBoard() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j].clear();
			}
		}
		playerAData.setNumberOfPiecesOnBoard(0);
		playerBData.setNumberOfPiecesOnBoard(0);
	}

	/**
	 * sets one boardPlace selected state
	 */
	public void setSelXY(int x, int y, boolean selectedState) {
		board[x][y].setSelected(selectedState);
	}

	/**
	 * draw the board - call the paintComponent for the super and for each one of
	 * the BoardPlaces
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int y = 0; y < DIMY; y++) {
			for (int x = 0; x < DIMX; x++) {
				board[x][y].paintComponent(g);
			}
		}
	}

	/**
	 * get the neighbor point starting from x,y and going in the d direction. If the
	 * point doesn't exist the method returns null.
	 */
	public static Point getNeighbourPoint(int x, int y, Direction d) {
		Point p = new Point(x, y);

		// IMPORTANT NOTE: as the move depends on X, we must work on Y first
		switch (d) {
		case N:
			if (y == 0)
				return null;
			p.y--;
			break;
		case NE:
			// y first
			if (p.x % 2 == 0) {
				if (y == 0)
					return null;
				p.y--;
			}
			// then x
			if (x == Board.DIMX - 1)
				return null;
			p.x++;
			break;
		case SE:
			if (p.x % 2 == 1) {
				if (y == Board.DIMY - 1)
					return null;
				p.y++;
			}
			if (x == Board.DIMX - 1)
				return null;
			p.x++;
			break;
		case S:
			if (y == Board.DIMY - 1)
				return null;
			p.y++;
			break;
		case SO:
			if (p.x % 2 == 1) {
				if (y == Board.DIMY - 1)
					return null;
				p.y++;
			}
			if (x == 0)
				return null;
			p.x--;
			break;
		case NO:
			if (p.x % 2 == 0) {
				if (y == 0)
					return null;
				p.y--;
			}
			if (x == 0)
				return null;
			p.x--;
			break;
		}
		return p;
	}

	/**
	 * returns the (tail) piece on board[x][y]
	 */
	public Piece getPiece(int x, int y) {
		if (isInside(x, y))
			return board[x][y].getPiece();
		else
			return null;
	}

	/**
	 * returns the BoardPlace at board[x][y]
	 */
	public BoardPlace getBoardPlace(int x, int y) {
		return board[x][y];
	}

	/**
	 * add a piece (on tail) on the BoardPlace x,y. Should increase the
	 * numberOfPiecesOnBoard of the player that own the piece. Any change to the
	 * board should call the repaint() method. Every piece on board should keep its
	 * BoardPlace coordinates on board.
	 */
	public void addPiece(Piece p, int x, int y) {
		board[x][y].addPiece(p);
		if (p.isFromPlayerA())
			playerAData.incNumberOfPiecesOnBoard();
		else
			playerBData.incNumberOfPiecesOnBoard();
		p.setXY(x, y);
	}

	/**
	 * Removes the piece if this piece is on tail on its BoardPlace. Should adjust
	 * numberOfPiecesOnBoard from its owner
	 */
	public boolean remPiece(Piece p) {
		int x = p.getX(), y = p.getY();
		if (p.isFromPlayerA())
			playerAData.decNumberOfPiecesOnBoard();
		else
			playerBData.decNumberOfPiecesOnBoard();
		return board[x][y].remPiece(p);
	}

	/**
	 * check if staring from x,y is just one hive. The number of adjacent pieces
	 * should be all the pieces on board. Can be used an ArrayList to collect the
	 * pieces.
	 */
	public boolean justOneHive(int x, int y) {
		int nPieces = playerAData.getNumberOfPiecesOnBoard() + playerBData.getNumberOfPiecesOnBoard();
		List<Piece> l = new ArrayList<Piece>();
		this.getPiecesFromThisPoint(x, y, l);
		return l.size() == nPieces;
	}

	/**
	 * Get all the pieces that are connected with the x, y received, and put them on
	 * the List received.
	 */
	private void getPiecesFromThisPoint(int x, int y, List<Piece> pieces) {
		ArrayList<Piece> pieceList = board[x][y].getList();
		for (Piece p : pieceList) {
			pieces.add(p);
		}
		for (Direction d : Direction.values()) {
			Point point = getNeighbourPoint(x, y, d);
			if(point == null) continue;
			Piece p2 = board[point.x][point.y].getPiece();
			if (p2 != null && !pieces.contains(p2)) {
				getPiecesFromThisPoint(point.x, point.y, pieces);
			}
		}
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isInside(int x, int y) {
		if (x >= 0 && x < DIMX && y >= 0 && y < DIMY)
			return true;
		return false;
	}

};
