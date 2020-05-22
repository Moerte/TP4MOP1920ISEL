package tps.tp4;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import tps.tp4.pieces.Ant;
import tps.tp4.pieces.Beetle;
import tps.tp4.pieces.Grasshopper;
import tps.tp4.pieces.Piece;
import tps.tp4.pieces.QueenBee;
import tps.tp4.pieces.Spider;

/**
 * class that keep and control the data from one player
 */
public class PlayerData {

	private static Color ACTIVEPLAYERCOLOR = Color.orange;
	private static Color INACTIVEPLAYERCOLOR = Color.gray;

	/**
	 * one Queen, two Beetles, two Grasshoppers, three Spiders and three Ants
	 * 
	 * Don't change this
	 */
	private final PiecesAndItsNumber[] ListaDePecas = new PiecesAndItsNumber[] {
			new PiecesAndItsNumber(PType.QUEENBEE, 1),
			new PiecesAndItsNumber(PType.BEETLE, 2),
			new PiecesAndItsNumber(PType.GRASHOPPER, 2),
			new PiecesAndItsNumber(PType.SPIDER, 3),
			new PiecesAndItsNumber(PType.ANT, 3) };

	private JPanel sidePanel = new JPanel();
	private JLabel movesLabel;
	private JLabel playerLabel;
	private HiveLabel queenBeeLabel = null;
	private QueenBee queenBee = null;

	private int numberOfPiecesOnBoard = 0;
	private int numberOfMoves = 0;

	private boolean playerWon = false;

	/**
	 * auxiliary class
	 */
	private class PiecesAndItsNumber {
		PType tipo;
		int nPecas;

		public PiecesAndItsNumber(PType tipo, int nPecas) {
			this.tipo = tipo;
			this.nPecas = nPecas;
		}

	}

	/**
	 * Constructor - should build the side panel for the player
	 */
	public PlayerData(Game game, boolean isPlayerA) {
		// TODO
	}

	/**
	 * Initializes the counters and the labels
	 */
	public void init(boolean playerIsActive) {
		// TODO
	}

	/**
	 * get side panel
	 */
	JPanel getSidePanel() {
		return sidePanel;
	}

	/**
	 * get number of moves of this player
	 */
	int getNumberOfMoves() {
		return numberOfMoves;
	}

	/**
	 * increment number of moves of this player
	 */
	void incNumberOfMoves() {
		// TODO
	}

	/**
	 * get Queen Bee reference of this player
	 */
	QueenBee getQueenBee() {
		return queenBee;
	}

	/**
	 * sets the number of moves ...
	 */
	void setNumberOfMoves(int n) {
		// TODO
	}

	/**
	 * get the number of pieces on board ...
	 */
	int getNumberOfPiecesOnBoard() {
		return numberOfPiecesOnBoard;
	}

	/**
	 * set the number of pieces on board ...
	 */
	void setNumberOfPiecesOnBoard(int np) {
		// TODO
	}

	/**
	 * increases the number of pieces on board ...
	 */
	void incNumberOfPiecesOnBoard() {
		// TODO
	}

	/**
	 * decreases the number of pieces on board ..
	 */
	void decNumberOfPiecesOnBoard() {
		// TODO
	}

	/**
	 * set this player background as current player or not
	 */
	public void setPlayerPanelActive(boolean active) {
		// TODO
	}

	/**
	 * check if queen bee of this player is already on board
	 */
	public boolean isQueenBeeAlreadyOnBoard() {
		// TODO
		return false;
	}

	/**
	 * display the current number of moves in the last label
	 */
	public void displayNumberOfMoves() {
		movesLabel.setText("" + numberOfMoves);
	}

	/**
	 * get the reference for the queen bee of this player
	 */
	public HiveLabel getQueenBeeLabel() {
		return queenBeeLabel;
	}

	/**
	 * sets if player won
	 */
	void setPlayerWon(boolean won) {
		playerWon = won;
	}

	/**
	 * return true if player won
	 */
	boolean playerWon() {
		return playerWon;
	}

}

/**
 * classe que suporta as labels das pe�as iniciais de cada jogador
 */
class HiveLabel extends JLabel {
	private static final long serialVersionUID = 1L;
	final static Border unselBorder = BorderFactory
			.createLineBorder(Color.darkGray);
	final static Border selBorder = BorderFactory.createLineBorder(Color.white,
			3);

	private Piece p;
	private Game game;
	private boolean isDeactivated = false;

	/**
	 * 
	 */
	public HiveLabel(Piece p, Game game) {
		// TODO
	}

	/**
	 * 
	 */
	public Piece getPiece() {
		return p;
	}

	/**
	 * 
	 */
	public String toString() {
		return p.toString();
	}

	/**
	 * 
	 */
	public void init() {
		// TODO
	}

	/**
	 * 
	 */
	public void activate() {
		setBorder(selBorder);
	}

	/**
	 * 
	 */
	public void setToNormal() {
		// TODO
	}

	/**
	 * 
	 */
	public void deactivate() {
		// TODO
	}

	/**
	 * 
	 */
	public boolean isDeactivated() {
		return isDeactivated;
	}

}

/**
 * enum with the several pieces and create methods
 */
enum PType {
	QUEENBEE {
		Piece createNew(Game game, boolean isFromPlayerA) {
			return new QueenBee(game, isFromPlayerA);
		};
	},
	BEETLE {
		Piece createNew(Game game, boolean isFromPlayerA) {
			return new Beetle(game, isFromPlayerA);
		};
	},
	GRASHOPPER {
		Piece createNew(Game game, boolean isFromPlayerA) {
			return new Grasshopper(game, isFromPlayerA);
		};
	},
	SPIDER {
		Piece createNew(Game game, boolean isFromPlayerA) {
			return new Spider(game, isFromPlayerA);
		};
	},
	ANT {
		Piece createNew(Game game, boolean isFromPlayerA) {
			return new Ant(game, isFromPlayerA);
		};
	};

	abstract Piece createNew(Game game, boolean isFromPlayerA);
};
