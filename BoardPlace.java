package tps.tp4;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

import tps.tp4.Game.Direction;
import tps.tp4.pieces.Piece;

public class BoardPlace {

	private static Color PIECEBACKGROUNDCOLOR = new Color(0x9CCF3A);
	private static Color PIECESELECTIONCOLOR = Color.RED;

	public static int STARTXY = 5;

	// the place to have the pieces on this board place
	// pieces must be added at the tail, and the only accessible piece must be
	// the tail piece
	ArrayList<Piece> pieces = new ArrayList<Piece>();

	// is selected or not
	private boolean selected = false;

	// board reference
	private Board board;

	// the board place coordinates
	int x, y;

	// the polygon for this board place
	Polygon polygon = new Polygon();

	// the selection polygon for this board place
	Polygon selPolygon = new Polygon();

	// the base xy from the board for this place
	private int baseX;
	private int baseY;

	// Methods ============================================

	/**
	 * constructor
	 */
	public BoardPlace(Board board, int x, int y) {
		this.board = board;
		this.x = x;
		this.y = y;

		// base data for polygons
		baseX = STARTXY + (int) (x * Piece.DIMPIECE * 0.75);
		baseY = STARTXY + (int) (y * Piece.DIMPIECE);

		if (x % 2 == 1) {
			baseY += Piece.DIMPIECE / 2;
		}

		// build polygon for this board place
		polygon.addPoint(baseX + Piece.DIMPIECE / 4 + 1, baseY + 1);
		polygon.addPoint(baseX + (Piece.DIMPIECE * 3) / 4 - 1, baseY + 1);

		polygon.addPoint(baseX + Piece.DIMPIECE - 1, baseY + Piece.DIMPIECE / 2);
		polygon.addPoint(baseX + (Piece.DIMPIECE * 3) / 4 - 1, baseY
				+ Piece.DIMPIECE - 1);
		polygon.addPoint(baseX + Piece.DIMPIECE / 4 + 1, baseY + Piece.DIMPIECE
				- 1);
		polygon.addPoint(baseX + 1, baseY + Piece.DIMPIECE / 2);

		// build selected polygon
		selPolygon.addPoint(baseX + Piece.DIMPIECE / 4, baseY - 1);
		selPolygon.addPoint(baseX + (Piece.DIMPIECE * 3) / 4, baseY - 1);

		selPolygon.addPoint(baseX + Piece.DIMPIECE, baseY + Piece.DIMPIECE / 2);
		selPolygon.addPoint(baseX + (Piece.DIMPIECE * 3) / 4, baseY
				+ Piece.DIMPIECE);
		selPolygon.addPoint(baseX + Piece.DIMPIECE / 4, baseY + Piece.DIMPIECE);
		selPolygon.addPoint(baseX, baseY + Piece.DIMPIECE / 2);

	}
	
	public ArrayList<Piece> getList(){
		return pieces;
	}
	
	public int getNumPieces() {
		return pieces.size();
	}

	/**
	 * get the tail piece - the others are not accessible
	 */
	public Piece getPiece() {
		if (pieces.size() == 0)
			return null;

		return pieces.get(pieces.size() - 1);
	}

	/**
	 * Add piece to tail
	 */
	public void addPiece(Piece p) {
		pieces.add(p);
	}

	/**
	 * remove piece P if it is on tail
	 */
	public boolean remPiece(Piece p) {
		if(pieces.get(pieces.size() - 1).equals(p)) {
			pieces.remove(pieces.size() - 1);
			return true;
		}
		return false;
	}

	/**
	 * clear all the pieces on this boardPlace
	 */
	public void clear() {
		for (int i = 0; i < pieces.size();i++) {
			pieces.remove(i);
		}
	}

	/**
	 * set selected state
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
		board.repaint();
	}

	/**
	 * get selected state
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * equals, two BoardPlaces are equal if they have the same x and y
	 */
	public boolean equals(Object o) {
		return this.x == ((BoardPlace)o).x && this.y == ((BoardPlace)o).y;
	}

	/**
	 * to be viewed in debug watch
	 */
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	/**
	 * Migrate the state of this board place 1 position to the neighbor in the
	 * received direction. To be used is move HIVE up, down, NO, ....
	 */
	public void migrateTo(Direction d) {
		if(pieces.size() ==  0)
			return;
		switch(d) {
			case N:
				BoardPlace bpn = board.getBoardPlace(x, y-1);
				bpn.setSelected(this.isSelected());
				this.swapPieces(bpn);
				break;
			case NO:
				BoardPlace bpno;
				if(x % 2 == 0)
					bpno = board.getBoardPlace(x-1, y-1);
				else
					bpno = board.getBoardPlace(x-1, y);
				bpno.setSelected(this.isSelected());
				this.swapPieces(bpno);
				break;
			case NE:
				BoardPlace bpne;
				if(x % 2 == 0)
					bpne = board.getBoardPlace(x + 1, y - 1);
				else
					bpne = board.getBoardPlace(x + 1, y);
				bpne.setSelected(this.isSelected());
				this.swapPieces(bpne);
				break;
			case S:
				BoardPlace bps = board.getBoardPlace(x, y + 1);
				bps.setSelected(this.isSelected());
				this.swapPieces(bps);
				break;
			case SO:
				BoardPlace bpso;
				if(x % 2 == 0)
					bpso = board.getBoardPlace(x-1, y);
				else
					bpso = board.getBoardPlace(x-1, y + 1);
				bpso.setSelected(this.isSelected());
				this.swapPieces(bpso);
				break;
			case SE:
				BoardPlace bpse;
				if(x % 2 == 0)
					bpse = board.getBoardPlace(x+1, y);
				else
					bpse = board.getBoardPlace(x+1, y+1);
				bpse.setSelected(this.isSelected());
				this.swapPieces(bpse);
				break;
			default:
				break;
		}
		board.repaint();
			
	}
	
	private void swapPieces(BoardPlace bp) {
		if(pieces.size() == 1) {
			Piece p = this.getPiece();
			this.remPiece(p);
			bp.addPiece(p);
			p.setXY(bp.x, bp.y);
		} else {
			Deque<Piece> stack = new LinkedList<Piece>();
			while(pieces.size() > 0) {
				Piece p = this.getPiece();
				this.remPiece(p);
				p.setXY(bp.x, bp.y);
				stack.add(p);
			}
			while(!stack.isEmpty()) {
				bp.addPiece(stack.pollLast());
			}
		}
	}

	/**
	 * Paint this boardPiece - if it doesn't have any piece we should the draw
	 * polygon in background color
	 */
	public void paintComponent(Graphics g) {

		if (getPiece() == null) {
			// draw empty board place
			g.setColor(PIECEBACKGROUNDCOLOR);
			g.fillPolygon(polygon);
		} else {
			g.setColor(getPiece().getColor());
			g.fillPolygon(polygon);
			if(getPiece().isFromPlayerA()) g.setColor(Color.BLACK);
			else g.setColor(Color.LIGHT_GRAY);
			g.drawString(String.valueOf(getPiece().getClass().getSimpleName().charAt(0)), baseX+7, baseY+17);
			
		}

		// if selected, draw selection
		if (isSelected()) {
			g.setColor(PIECESELECTIONCOLOR);
			g.drawPolygon(polygon);
		}
	}

	/**
	 * check if x,y received is inside the polygon of this boardPlace - uses the
	 * contains method from polygon
	 */
	public boolean isInsideBoardPlace(int x, int y) {
		return polygon.contains(x, y);
	}

}
