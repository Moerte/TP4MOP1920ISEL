package tps.tp4;

import java.awt.Color;
import java.awt.Dimension;
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
import tps.tp4.pieces.Ladybug;
import tps.tp4.pieces.Mosquito;
import tps.tp4.pieces.Piece;
import tps.tp4.pieces.PillBug;
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
			new PiecesAndItsNumber(PType.ANT, 3),
			new PiecesAndItsNumber(PType.MOSQUITO, 1), new PiecesAndItsNumber(PType.LADYBUG, 1), new PiecesAndItsNumber(PType.PILLBUG, 1) };

	private JPanel sidePanel;
	private JLabel movesLabel;
	private JLabel playerLabel;
	private HiveLabel queenBeeLabel;
	private QueenBee queenBee;

	private int numberOfPiecesOnBoard;
	private int numberOfMoves;

	private boolean playerWon;

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
		this.init(isPlayerA);
		
		JPanel piecesPanel = new JPanel(new GridLayout(14, 1, 0, 0));
		for (PiecesAndItsNumber p : ListaDePecas) {
			for (int i = 0; i < p.getnPecas(); i++) {
				Piece addedPiece = p.getTipo().createNew(game, isPlayerA);
				HiveLabel pieceLabel = new HiveLabel(addedPiece, game);
				pieceLabel.setText(pieceLabel.getPiece().getName());
				pieceLabel.setHorizontalAlignment(SwingConstants.CENTER);
				pieceLabel.setPreferredSize(new Dimension(150, 20));
				pieceLabel.setForeground(Color.WHITE);
				pieceLabel.setBackground(pieceLabel.getPiece().getColor());
				pieceLabel.setOpaque(true);
				pieceLabel.addMouseListener(new MouseAdapter() {
	                @Override
	                public void mouseClicked(MouseEvent e) {
	                	game.clickOnPieceLabelOnSidePanel(pieceLabel);
	                }
	            });
				if(pieceLabel.getPiece().getName().equalsIgnoreCase("QueenBee")) {
					queenBeeLabel = pieceLabel;
				}
				piecesPanel.add(pieceLabel);
			}
		}
		sidePanel.add(piecesPanel);
		
		String bjoras = String.valueOf(numberOfMoves);
		movesLabel = new JLabel(bjoras, SwingConstants.CENTER);
		movesLabel.setOpaque(true);
		movesLabel.setBackground(Color.GREEN);
		movesLabel.setPreferredSize(new Dimension(150, 20));
		sidePanel.add(movesLabel);

	}

	/**
	 * Initializes the counters and the labels
	 */
	public void init(boolean playerIsActive) {
		numberOfMoves = 0;
		numberOfPiecesOnBoard = 0;
		queenBee = null;
		queenBeeLabel = null;
		sidePanel = new JPanel();
		playerWon = false;
		
		Dimension dim = new Dimension(150, 20);
		String player = playerIsActive == true ? "A" : "B";
		playerLabel = new JLabel("Player " + player, SwingConstants.CENTER);
		playerLabel.setPreferredSize(dim);
		playerLabel.setOpaque(true);
		if(playerIsActive)playerLabel.setBackground(ACTIVEPLAYERCOLOR);
		else playerLabel.setBackground(INACTIVEPLAYERCOLOR);
		sidePanel.add(playerLabel);

		JLabel playerColor = new JLabel("Player Color", SwingConstants.CENTER);
		playerColor.setPreferredSize(dim);
		playerColor.setForeground(Color.WHITE);
		playerColor.setBackground(Game.getColorFromPlayer(playerIsActive));
		playerColor.setOpaque(true);
		sidePanel.add(playerColor);
	
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
		numberOfMoves++;
		displayNumberOfMoves();
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
		this.displayNumberOfMoves();
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
		if (active)playerLabel.setBackground(ACTIVEPLAYERCOLOR);
		else playerLabel.setBackground(INACTIVEPLAYERCOLOR);
	}
	
	public void setQueenBee(Piece queen) {
		queenBee = (QueenBee)queen;
	}

	/**
	 * check if queen bee of this player is already on board
	 */
	public boolean isQueenBeeAlreadyOnBoard() {
		return queenBee != null;
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
		init();
		
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
		this.setToNormal();

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
		setBorder(BorderFactory.createLineBorder(p.getColor()));
		this.isDeactivated = false;
	}

	/**
	 * 
	 */
	public void deactivate() {
		setBorder(unselBorder);
		this.isDeactivated = true;
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
	},
	MOSQUITO {
		Piece createNew(Game game, boolean isFromPlayerA) {
			return new Mosquito(game, isFromPlayerA);
		};
	},
	LADYBUG {
		Piece createNew(Game game, boolean isFromPlayerA) {
			return new Ladybug(game, isFromPlayerA);
		};
	},
	PILLBUG {
		Piece createNew(Game game, boolean isFromPlayerA) {
			return new PillBug(game, isFromPlayerA);
		};
	};

	abstract Piece createNew(Game game, boolean isFromPlayerA);
};
