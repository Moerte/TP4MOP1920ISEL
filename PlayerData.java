package tps.tp4;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

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
			new PiecesAndItsNumber(PType.QUEENBEE, 1), new PiecesAndItsNumber(PType.BEETLE, 2),
			new PiecesAndItsNumber(PType.GRASHOPPER, 2), new PiecesAndItsNumber(PType.SPIDER, 3),
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

		public PType getTipo() {
			return tipo;
		}

		public int getnPecas() {
			return nPecas;
		}
	}

	/**
	 * Constructor - should build the side panel for the player
	 */
	public PlayerData(Game game, boolean isPlayerA) {
		
		Dimension dim = new Dimension(150, 20);
		// TODO
		String player = isPlayerA == true ? "A" : "B";
		playerLabel = new JLabel("Player " + player, SwingConstants.CENTER);
		playerLabel.setPreferredSize(dim);
		playerLabel.setOpaque(true);
		playerLabel.setBackground(INACTIVEPLAYERCOLOR);
		sidePanel.add(playerLabel);

		JLabel playerColor = new JLabel("Player Color", SwingConstants.CENTER);
		playerColor.setPreferredSize(dim);
		playerColor.setForeground(Color.WHITE);
		playerColor.setBackground(Game.getColorFromPlayer(isPlayerA));
		playerColor.setOpaque(true);
		sidePanel.add(playerColor);
		
		// TODO melhorar
		JPanel piecesPanel = new JPanel(new GridLayout(11, 1, 0, 0));
		for (PiecesAndItsNumber p : ListaDePecas) {
			Piece addedPiece = p.getTipo().createNew(game, isPlayerA);
			JLabel piece = new JLabel();
			for (int i = 0; i < p.getnPecas(); i++) {
				HiveLabel pieceLabel = new HiveLabel(addedPiece, game);
				//piece = pieceLabel.getPiece().getName(), SwingConstants.CENTER);
				piece = new JLabel();
				piece.setText(pieceLabel.getPiece().getName());
				piece.setHorizontalAlignment(SwingConstants.CENTER);
				piece.setPreferredSize(dim);
				piece.setForeground(Color.WHITE);
				piece.setBackground(pieceLabel.getPiece().getColor());
				piece.setOpaque(true);
				//piecesPanel.add(piece);
				if(pieceLabel.getPiece().getName() == "QueenBee") {
					queenBeeLabel = pieceLabel;
					queenBee = (QueenBee)pieceLabel.getPiece();
				}
				
			}
			//sidePanel.add(piecesPanel);
			piecesPanel.add(piece);
		}
		sidePanel.add(piecesPanel);
		
		
		movesLabel = new JLabel(String.valueOf(numberOfMoves), SwingConstants.CENTER);
		movesLabel.setOpaque(true);
		movesLabel.setBackground(Color.GREEN);
		movesLabel.setPreferredSize(dim);
		sidePanel.add(movesLabel);

		// End Player
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
		this.numberOfMoves++;
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
		this.numberOfMoves = n;
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
		this.numberOfPiecesOnBoard = np;
	}

	/**
	 * increases the number of pieces on board ...
	 */
	void incNumberOfPiecesOnBoard() {
		this.numberOfPiecesOnBoard++;
	}

	/**
	 * decreases the number of pieces on board ..
	 */
	void decNumberOfPiecesOnBoard() {
		if (this.numberOfPiecesOnBoard > 0)
			this.numberOfPiecesOnBoard--;
	}

	/**
	 * set this player background as current player or not
	 */
	public void setPlayerPanelActive(boolean active) {
		playerLabel.setBackground(ACTIVEPLAYERCOLOR);
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
 * classe que suporta as labels das peï¿½as iniciais de cada jogador
 */
class HiveLabel extends JLabel {
	private static final long serialVersionUID = 1L;
	final static Border unselBorder = BorderFactory.createLineBorder(Color.darkGray);
	final static Border selBorder = BorderFactory.createLineBorder(Color.white, 3);

	private Piece p;
	private Game game;
	private boolean isDeactivated = false;

	/**
	 * 
	 */
	public HiveLabel(Piece p, Game game) {
		this.p = p;
		this.game = game;
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
		

	}

	/**
	 * 
	 */
	public void activate() {
		setBorder(selBorder);
		this.isDeactivated = false;
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
		setBorder(selBorder);
		this.isDeactivated = false;
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
