package tps.tp4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

import tps.layouts.CenterLayout;
import tps.tp4.pieces.Piece;
import tps.tp4.pieces.Spider;

/**
 * HIVE GAME: one Queen, two Beetles, two Grasshoppers, three Spiders and three
 * Ants
 * 
 * http://en.wikipedia.org/wiki/Hive_(game)
 * http://www.gen42.com/downloads/rules/Hive_Rules.pdf
 */

/**
 * the main class - that supports the game
 */
public class Game extends JFrame {

	// enumerate that supports directions
	public enum Direction {
		N, NE, SE, S, SO, NO
	}

	private static final long serialVersionUID = 1L;
	private static final Color COLORPLAYER_A = Color.black;
	private static final Color COLORPLAYER_B = Color.lightGray;
	private static final int MAX_NUMBER_OF_MOVES_TO_PLACE_QUEENBEE = 4;

	private JLabel mainLabel;
	private HiveLabel currentHiveLabel = null;
	private Piece currentPiece = null;
	private JPanel controlPanelOut;
	private JLabel lb_message;
	private JPanel controlPanel;
	private Board board;

	private Font fontCurrentPlayer;
	private Font fontPieces;

	// buttons to move the Hive, if possible
	private JButton bn_newGame; // Adicionado por Nuno
	private JButton bn_startAgain; // Adicionado por Nuno
	private JButton bn_moveUp;
	private JButton bn_moveDown;
	private JButton bn_moveNO;
	private JButton bn_moveNE;
	private JButton bn_moveSE;
	private JButton bn_moveSO;
	private JButton bn_changePlayer;
	private JButton bn_giveUp;

	private JMenuBar menuBar;

	private boolean placingQueenBee = false;
	private boolean isPlayerAToPlay;
	private PlayerData currentPlayerData;
	private boolean endOfGame = false;

	private PlayerData playerAData;
	private PlayerData playerBData;

	/**
	 * methods =============================================
	 */

	/**
	 * main
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// create object Game
				Game g = new Game();
				// launch the frame, but will be activated with some delay
				g.init();
			}
		});
		System.out.println("End of main...");

	}

	/**
	 * load resources: fonts, images, sounds
	 */
	private void loadResources() {

		String fontType = "Comic Sans MS";
		int size = 20;
		this.fontCurrentPlayer = new Font(fontType, Font.BOLD, size);
		this.fontPieces = new Font(fontType, Font.BOLD, size);
	}

	/**
	 * the JFrame initialization method
	 */
	private void init() {
		setTitle("Hive Game");
		setSize(1000, 700);
		loadResources();
		this.isPlayerAToPlay = true;
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLayout(new CenterLayout());

		setLocationRelativeTo(null);

		board = new Board(this, fontPieces);
		add(board, BorderLayout.CENTER);

		controlPanel = new JPanel(new GridLayout(2, 9, 0, 0));
		
		Game that = this;
		ActionListener al = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				switch (e.getActionCommand()) {
				case "new_game":
					enableControlButtons(true);
					startAgain();
					break;
				case "start_again":
					int n = JOptionPane.showConfirmDialog(that, "Are you sure about your decision?",
							"Restart Game Confirmation", JOptionPane.YES_NO_OPTION);
					if (n == JOptionPane.YES_OPTION) {
						enableControlButtons(true);
						startAgain();
					} else {
						return;
					}
					break;
				case "move_up":
					moveHiveUp();
					break;
				case "move_down":
					moveDown();
					break;
				case "move_no":
					moveNO();
					break;
				case "move_ne":
					moveNE();
					break;
				case "move_se":
					moveSE();
					break;
				case "move_so":
					moveSO();
					break;
				case "change_player":
					bn_changePlayerAction();
					break;
				case "give_up":
					giveUp();
					break;
				}
			}
		};
		JPanel buttons = new JPanel();
		
		bn_newGame = new JButton("New Game");
		bn_newGame.setActionCommand("new_game");
		bn_newGame.addActionListener(al);
		bn_newGame.setVisible(false);
		bn_newGame.setEnabled(false);
		buttons.add(bn_newGame);
		
		bn_startAgain = new JButton("Start Again");
		bn_startAgain.setActionCommand("start_again");
		bn_startAgain.addActionListener(al);
		buttons.add(bn_startAgain);

		bn_moveUp = new JButton("Move UP");
		bn_moveUp.setActionCommand("move_up");
		bn_moveUp.addActionListener(al);
		buttons.add(bn_moveUp);

		bn_moveDown = new JButton("Move Down");
		bn_moveDown.setActionCommand("move_down");
		bn_moveDown.addActionListener(al);
		buttons.add(bn_moveDown);

		bn_moveNO = new JButton("Move NO");
		bn_moveNO.setActionCommand("move_no");
		bn_moveNO.addActionListener(al);
		buttons.add(bn_moveNO);

		bn_moveNE = new JButton("Move NE");
		bn_moveNE.setActionCommand("move_ne");
		bn_moveNE.addActionListener(al);
		buttons.add(bn_moveNE);

		bn_moveSE = new JButton("Move SE");
		bn_moveSE.setActionCommand("move_se");
		bn_moveSE.addActionListener(al);
		buttons.add(bn_moveSE);

		bn_moveSO = new JButton("Move SO");
		bn_moveSO.setActionCommand("move_so");
		bn_moveSO.addActionListener(al);
		buttons.add(bn_moveSO);

		bn_changePlayer = new JButton("Change Player");
		bn_changePlayer.setActionCommand("change_player");
		bn_changePlayer.addActionListener(al);
		buttons.add(bn_changePlayer);

		bn_giveUp = new JButton("Give Up");
		bn_giveUp.setActionCommand("give_up");
		bn_giveUp.addActionListener(al);
		buttons.add(bn_giveUp);

		// TODO tirar a margem entre os botões e a label
		controlPanelOut = new JPanel();
		lb_message = new JLabel("Log do Jogo");
		lb_message.setPreferredSize(new Dimension(500, 20));
		lb_message.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		controlPanelOut.add(lb_message);

		controlPanel.add(buttons, BorderLayout.CENTER);
		controlPanel.add(controlPanelOut, BorderLayout.SOUTH);
		add(controlPanel, BorderLayout.SOUTH);

		playerAData = new PlayerData(this, true);
		JPanel panelA = new JPanel();
		panelA = playerAData.getSidePanel();
		add(panelA, BorderLayout.WEST);

		playerBData = new PlayerData(this, false);
		JPanel panelB = new JPanel();
		panelB = playerBData.getSidePanel();
		add(panelB, BorderLayout.EAST);

		buildMenu();
		currentPlayerData = playerAData;

		// Main Label
		mainLabel = new JLabel("HIVE GAME: Current Player -> " + "Player A", SwingConstants.CENTER);
		mainLabel.setFont(this.fontCurrentPlayer);
		mainLabel.setOpaque(true);
		mainLabel.setBackground(Color.LIGHT_GRAY);
		add(mainLabel, BorderLayout.NORTH);

		setVisible(true);
	}

	/**
	 * build menu
	 */
	private void buildMenu() {
		JMenu menu;
		JMenuItem restartMenuItem;
		JMenuItem viewScoresItem;
		JMenuItem aboutItem;
		ActionListener al = null;

		// Menu Action Listener
		al = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JMenuItem mi = (JMenuItem) (e.getSource());
				String menuItemText = mi.getText();
				switch (menuItemText) {
				case "Restart Game":
					enableControlButtons(true);
					startAgain();
					break;
				case "View Scores":
					viewScores();
					break;
				case "About":
					about();
					break;
				case "Help":

					break;
				}
			}
		};

		// Create the menu bar.
		menuBar = new JMenuBar();

		// Build the menu.
		menu = new JMenu("Options...");
		menu.setMnemonic(KeyEvent.VK_O);
		menu.addSeparator();
		menuBar.add(menu);

		restartMenuItem = new JMenuItem("Restart Game", KeyEvent.VK_R);
		restartMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		restartMenuItem.addActionListener(al);
		menu.add(restartMenuItem);

		menu.addSeparator();

		viewScoresItem = new JMenuItem("View Scores", KeyEvent.VK_P);
		viewScoresItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		viewScoresItem.addActionListener(al);
		menu.add(viewScoresItem);

		menu.addSeparator();

		// TODO Adicionar foto ao menu
		aboutItem = new JMenuItem("About", KeyEvent.VK_A);
		aboutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		aboutItem.addActionListener(al);
		menu.add(aboutItem);

		JMenuItem helpItem = new JMenuItem("Help", KeyEvent.VK_H);
		helpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
		helpItem.addActionListener(al);
		menu.add(helpItem);

		// set Menu Bar on JFrame
		setJMenuBar(menuBar);
	}

	/**
	 * activate About window
	 */
	private void about() {
		JOptionPane.showMessageDialog(this, "Hive Game - V 1.0\nProduced by:\nNuno Oliveira and Eduardo Marques.",
				"About information.", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * activate View scores window
	 */
	private void viewScores() {
		// TODO Adicionar as pontuações
		JOptionPane.showMessageDialog(this, "Top Scores:\n", "Top Scores", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * get color from player
	 */
	static public Color getColorFromPlayer(boolean isPlayerA) {
		return isPlayerA ? COLORPLAYER_A : COLORPLAYER_B;
	}

	/**
	 * get board
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * get player data
	 */
	public PlayerData getPlayerData(boolean fromPlayerA) {
		return fromPlayerA ? playerAData : playerBData;
	}

	/**
	 * change player actions - to be called from the menu or from the button
	 * changePlayer
	 */
	private void bn_changePlayerAction() {
		this.changePlayer();
	}

	/**
	 * change player actions
	 */
	private void changePlayer() {
		if (currentPlayerData.getNumberOfMoves() == MAX_NUMBER_OF_MOVES_TO_PLACE_QUEENBEE - 1
				&& !currentPlayerData.isQueenBeeAlreadyOnBoard()) {
			System.out.println("You need to place the QueenBee.");
			lb_message.setText("You need to place the QueenBee.");
			if (!placingQueenBee)
				return;
		}
		currentPlayerData.incNumberOfMoves();
		if (currentPlayerData.equals(playerAData)) {
			currentPlayerData = playerBData;
			isPlayerAToPlay = false;
			playerAData.setPlayerPanelActive(isPlayerAToPlay);
			playerBData.setPlayerPanelActive(true);
			mainLabel.setText("HIVE GAME: Current Player -> Player B");
		} else {
			currentPlayerData = playerAData;
			isPlayerAToPlay = true;
			playerBData.setPlayerPanelActive(!isPlayerAToPlay);
			playerAData.setPlayerPanelActive(isPlayerAToPlay);
			mainLabel.setText("HIVE GAME: Current Player -> Player A");
		}
		if (currentHiveLabel != null) {
			currentHiveLabel.setToNormal();
			currentHiveLabel = null;
		}

	}

	/**
	 * start again actions
	 */
	private void startAgain() {

		Component[] playAComponents = playerAData.getSidePanel().getComponents();
		Component[] playBComponents = playerBData.getSidePanel().getComponents();
		this.labelRestart(playAComponents);
		this.labelRestart(playBComponents);
		playerAData.setNumberOfMoves(0);
		playerAData.setNumberOfPiecesOnBoard(0);
		playerBData.setNumberOfMoves(0);
		playerBData.setNumberOfPiecesOnBoard(0);
		if (!isPlayerAToPlay) {
			currentPlayerData = playerAData;
			currentPlayerData.setPlayerPanelActive(true);
			playerBData.setPlayerPanelActive(false);
			isPlayerAToPlay = true;
			mainLabel.setText("HIVE GAME: Current Player -> Player "+ (isPlayerAToPlay? "A": "B"));
		}
		board.resetBoard();
		board.repaint();
		if (currentPiece != null)
			board.getBoardPlace(currentPiece.getX(), currentPiece.getY()).setSelected(false);
		currentHiveLabel = null;
		currentPiece = null;
		bn_newGame.setVisible(false);
		bn_newGame.setEnabled(false);
	}

	/**
	 * TODO
	 * 
	 * @param component
	 */
	private void labelRestart(Component[] component) {
		for (Component c : component) {
			if (c instanceof JPanel) {
				JPanel jp = (JPanel) c;
				Component[] piecesLabel = jp.getComponents();
				for (Component x : piecesLabel) {
					HiveLabel z = (HiveLabel) x;
					z.setToNormal();
				}
			}
			c.setEnabled(true);
		}
	}

	private void giveUp() {
		int n = JOptionPane.showConfirmDialog(this, "Are you that chicken?", "Give Up Confirmation",
				JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.YES_OPTION) {
			this.endOfGame = true;

			if (this.checkFinishGame()) {
				if (currentPiece != null && currentHiveLabel != null) {
					board.getBoardPlace(currentPiece.getX(), currentPiece.getY()).setSelected(false);
					currentHiveLabel = null;
					currentPiece = null;
				}
				this.doFinishGameActions();
			}

		} else if (n == JOptionPane.NO_OPTION) {
			return;
		}
	}

	/**
	 * check if coordinate x,y only have friendly neighbor of current player
	 */
	private boolean onlyHaveFriendlyNeighbors(int x, int y) {
		boolean enemyNeib = false;
		for (Direction d : Direction.values()) {
			Point p = Board.getNeighbourPoint(x, y, d);
			if (p == null)
				continue;
			int k = (int) p.getX();
			int j = (int) p.getY();
			Piece piece = board.getPiece(k, j);
			if (piece == null)
				continue;
			if (board.getPiece(k, j).isFromPlayerA() && currentPlayerData.equals(playerAData)) {
				enemyNeib = true;
			} else if (!board.getPiece(k, j).isFromPlayerA() && currentPlayerData.equals(playerBData)) {
				enemyNeib = true;
			} else if (board.getPiece(k, j).isFromPlayerA() && currentPlayerData.equals(playerBData)) {
				enemyNeib = false;
				break;
			} else if (!board.getPiece(k, j).isFromPlayerA() && currentPlayerData.equals(playerAData)) {
				enemyNeib = false;
				break;
			}
		}
		System.out.println("CEnas =>" + enemyNeib);
		return enemyNeib;
	}

	/**
	 * a click on the board
	 */
	public void clickOnBoard(int x, int y) {
		if (currentPlayerData.getNumberOfMoves() == MAX_NUMBER_OF_MOVES_TO_PLACE_QUEENBEE - 1
				&& !currentPlayerData.isQueenBeeAlreadyOnBoard()) {
			lb_message.setText("You need to place the QueenBee.");
			if (!placingQueenBee)
				return;
		}
		if (currentHiveLabel == null) {
			Piece p = board.getPiece(x, y);
			if (p == null)
				return;
			if (currentPiece != null) {
				// TODO
				System.out.println("Cheguei");
				if(currentPiece.moveTo(x, y)) {
					board.getBoardPlace(currentPiece.getX(), currentPiece.getY()).setSelected(false);
					board.repaint();
					changePlayer();
				}
				return;
			} else {
				currentPiece = p;
				board.getBoardPlace(x, y).setSelected(true);
				return;
			}
		}
		if (currentPiece != null) {
			System.out.println("Cheguei 222");
			currentPiece.moveTo(x, y);
			//return;
			// currentPiece.moveTo(x, y);
		}
		if (isPlayerAToPlay) {
			if (currentPlayerData.getNumberOfMoves() != 0 && !this.onlyHaveFriendlyNeighbors(x, y)) {
				lb_message.setText("Invalid position! Can't play here!");
				return;
			} else {
				Piece p = board.getPiece(x, y);
				if (p != null) {
					lb_message.setText("All pieces must be played on the Board");
					return;
					// TODO LOG: Todas as peças tem de ser jogadas em campo (não pode ser por cima
					// de outra peça)
				} else {
					// TODO Podes jogar a peça
				}
			}
		} else {
			if (currentPlayerData.getNumberOfMoves() == 0) {
				boolean validNeib = false;
				for (Direction d : Direction.values()) {
					Point p = Board.getNeighbourPoint(x, y, d);
					if (p == null)
						continue;
					int k = (int) p.getX();
					int j = (int) p.getY();
					if (board.getPiece(k, j) != null)
						validNeib = true;
				}
				if (validNeib) {
					// TODO Podes jogar
				} else {
					lb_message.setText("The Piece has must be played adjacent to another piece");
					return;
				}
			} else if (!this.onlyHaveFriendlyNeighbors(x, y)) {
				lb_message.setText("Invalid position! Can't play here!");
				return;
			} else {
				Piece p = board.getPiece(x, y);
				if (p != null) {
					lb_message.setText("All pieces must be played on the Board");
					return;
					// TODO LOG: Todas as peças tem de ser jogadas em campo (não pode ser por cima
					// de outra peça)
				} else {
					// TODO Podes jogar a peça
				}
			}
		}
		// currentPiece = currentHiveLabel.getPiece();
		board.addPiece(currentHiveLabel.getPiece(), x, y);
		board.repaint();
		currentHiveLabel.deactivate();
		currentHiveLabel = null;
		changePlayer();
	}

	/**
	 * a click on a label on side panel
	 */
	public void clickOnPieceLabelOnSidePanel(HiveLabel hl) {
		if (hl.isDeactivated())
			return;
		if (hl.getPiece().isFromPlayerA() != isPlayerAToPlay)
			return;
		if (currentHiveLabel != null) {
			currentHiveLabel.setToNormal();
			if (placingQueenBee)
				placingQueenBee = false;
			if (currentHiveLabel.equals(hl)) {
				currentHiveLabel = null;
				return;
			}
		}
		currentHiveLabel = hl;
		currentHiveLabel.activate();
		if (currentHiveLabel.getPiece().getName().equalsIgnoreCase("queenbee"))
			placingQueenBee = true;
	}

	/**
	 * Can move to border, used to check if piece can be placed on hive physically
	 * sliding from the border. We can use a ArrayList to keep the boardPlaces and
	 * try to find a way to the border. The ArrayList is used to avoid loops. If a
	 * new boardPlace is already in the ArrayList so it will start a loop, so
	 * abandon that boardPlace as not valid move. This method only call the
	 * auxiliary method.
	 */
	private boolean canMoveToBorder(int x, int y) {
		return canMoveToBorder(x, y, new ArrayList<BoardPlace>());
	}

	/**
	 * can move to border - auxiliary method
	 */
	private boolean canMoveToBorder(int x, int y, ArrayList<BoardPlace> path) {
		if(board.getBoardPlace(x +1, y) != null || board.getBoardPlace(x, y+1) != null) {
			
			return true;
		}
			
		return false;
	}

	/**
	 * check if can move physically from x,y in to the direction received
	 * 
	 * By physical we mean, that the piece has physical space to move. A piece, with
	 * the NE and NO places occupied, cannot move to N.
	 */
	public boolean canPhysicallyMoveTo(int x, int y, Direction d) {
		switch (d) {
		case N:
			if(canMoveToBorder(x, y-1)) return this.physicalMove(x, y, Direction.NE, Direction.NO);
		case NE:
			if(canMoveToBorder(x+1, y)) return this.physicalMove(x, y, Direction.N, Direction.SE);
		case NO:
			if(canMoveToBorder(x-1, y)) return this.physicalMove(x, y, Direction.N, Direction.SO);
		case S:
			if(canMoveToBorder(x, y+1)) return this.physicalMove(x, y, Direction.SE, Direction.SO);
		case SE:
			if(canMoveToBorder(x+1, y+1)) return this.physicalMove(x, y, Direction.NE, Direction.S);
		case SO:
			if(canMoveToBorder(x-1, y+1)) return this.physicalMove(x, y, Direction.S, Direction.NO);
		default:
			break;
		}
		return false;
	}

	private boolean physicalMove(int x, int y, Direction d1, Direction d2) {
		Point pt1 = Board.getNeighbourPoint(x, y, d1);
		Point pt2 = Board.getNeighbourPoint(x, y, d2);
		if (pt1 == null || pt2 == null)
			return true;
		Piece p1 = board.getPiece((int) pt1.getX(), (int) pt1.getY());
		Piece p2 = board.getPiece((int) pt2.getX(), (int) pt2.getY());
		if (p1 != null && p2 != null)
			return false;
		return true;
	}

	/**
	 * set status info in label status
	 */
	public void setStatusInfo(String str) {
		lb_message.setText(str);
	}

	/**
	 * Move the received piece unconditionally from its position to target position
	 * with: remPiece and addPiece.
	 */
	public void moveUnconditional(Piece p, int x, int y) {
		board.remPiece(p);
		board.addPiece(p, x, y);
		p.setXY(x, y);
	}

	/**
	 * check if end of game - update playerAWon and/or playerBWon states
	 */
	private boolean checkFinishGame() {
		if (endOfGame) {
			if (isPlayerAToPlay)
				playerBData.setPlayerWon(true);
			else
				playerAData.setPlayerWon(true);
			return true;
		}
		return false;
	}

	/**
	 * check if queen of received player is surrounded
	 */
	private boolean checkFinishGame(boolean playerA) {
		if (playerA) {
			return this.checkSurroundPos(playerAData);
		} else {
			return this.checkSurroundPos(playerBData);
		}
	}

	private boolean checkSurroundPos(PlayerData player) {
		Piece pt = player.getQueenBee();
		if (pt == null)
			return false;
		int x = pt.getX();
		int y = pt.getY();
		boolean emptyNeib = false;
		for (Direction d : Direction.values()) {
			Point p = Board.getNeighbourPoint(x, y, d);
			if (p == null)
				continue;
			int k = (int) p.getX();
			int j = (int) p.getY();
			if (board.getPiece(k, j) == null) {
				emptyNeib = true;
				break;
			}
		}
		return !emptyNeib;
	}

	/**
	 * do end of game actions
	 */
	private void doFinishGameActions() {
		boolean checkWinner = playerAData.playerWon();
		JOptionPane.showMessageDialog(this, "Congratulations Player " + (checkWinner ? "A" : "B"), "Winner Panel",
				JOptionPane.INFORMATION_MESSAGE);
		lb_message.setText("Winner: Player "+ (checkWinner ? "A" : "B"));
		enableControlButtons(false);
		bn_newGame.setVisible(true);
		bn_newGame.setEnabled(true);
	}

	/**
	 * change state of general buttons
	 */
	private void enableControlButtons(boolean enable) {
		Component[] container = controlPanel.getComponents();
		Component[] playerAContainer = playerAData.getSidePanel().getComponents();
		Component[] playerBContainer = playerBData.getSidePanel().getComponents();

		for (Component c : container) {
			if (c instanceof JPanel) {
				JPanel jp = (JPanel) c;
				Component[] buttons = jp.getComponents();
				for (Component x : buttons) {
					if(x instanceof JLabel) continue;
					x.setEnabled(enable);
				}
			}
		}
		this.deactivateLabels(playerBContainer, enable);
		this.deactivateLabels(playerAContainer, enable);
		board.resetBoard();
		board.repaint();
	}

	private void deactivateLabels(Component[] component, boolean enable) {
		for (Component c : component) {
			if (c instanceof JPanel) {
				JPanel jp = (JPanel) c;
				Component[] piecesLabel = jp.getComponents();
				for (Component x : piecesLabel) {
					x.setEnabled(enable);
					HiveLabel z = (HiveLabel) x;
					z.deactivate();
				}
			}
			c.setEnabled(enable);
		}
	}

	/**
	 * move hive UP, if it can be moved
	 */
	private void moveHiveUp() {
		lb_message.setText("Move Hive Up");
		BoardPlace legit = board.getBoardPlace(0, 0);
		legit.migrateTo(Direction.N);
	}

	/**
	 * move hive DOWN, if it can
	 */
	private void moveDown() {
		lb_message.setText("Move Down");
		BoardPlace legit = board.getBoardPlace(0, 0);
		legit.migrateTo(Direction.S);
	}

	/**
	 * move hive NO, if it can
	 */
	private void moveNO() {
		lb_message.setText("Move NO");
		BoardPlace legit = board.getBoardPlace(Board.DIMX - 1, Board.DIMY - 1);
		legit.migrateTo(Direction.NO);
	}

	/**
	 * move hive NE, if it can
	 */
	private void moveNE() {
		lb_message.setText("Move NE");
		BoardPlace legit = board.getBoardPlace(0, Board.DIMY - 1);
		legit.migrateTo(Direction.NE);
	}

	/**
	 * move hive SO, if it can
	 */
	private void moveSO() {
		lb_message.setText("Move SO");
		BoardPlace legit = board.getBoardPlace(Board.DIMX - 1, 0);
		legit.migrateTo(Direction.SO);
	}

	/**
	 * move hive SE, if it can
	 */
	private void moveSE() {
		lb_message.setText("Move SE");
		BoardPlace legit = board.getBoardPlace(0, 0);
		legit.migrateTo(Direction.SE);
	}

}
