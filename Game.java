package tps.tp4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import tps.layouts.CenterLayout;
import tps.tp4.pieces.Piece;

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
	// private JLabel subMainLabel;
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

	private JTree tree;
	private JEditorPane htmlPane;
	private JSplitPane splitPane;
	private URL referenceURL;

	private HighscoreManager highScore;
	
	private ImageIcon iconImg, iconMedium, leafIcon, leafIconPiece;

	private Piece lastMovedPiece;

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
		int size = 16;
		this.fontCurrentPlayer = new Font(fontType, Font.PLAIN, size);
		this.fontPieces = new Font(fontType, Font.BOLD, size);
		this.iconImg = new ImageIcon(this.getClass().getResource("images/logo/hiveLogo2.png"));
		this.iconMedium = new ImageIcon(this.getClass().getResource("images/icon/hiveIconMedium.png"));
		this.leafIcon = new ImageIcon(this.getClass().getResource("images/icon/hiveIconSmall.png"));
		this.leafIconPiece = new ImageIcon(this.getClass().getResource("images/icon/queenbeenIcon.png"));
	}

	/**
	 * the JFrame initialization method
	 */
	private void init() {
		highScore = new HighscoreManager();
		setTitle("Hive Game");
		setSize(1000, 700);
		loadResources();
		this.isPlayerAToPlay = true;
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLayout(new CenterLayout());
		setLocationRelativeTo(null);
		setIconImage(iconImg.getImage());

		lastMovedPiece = null;

		playerAData = new PlayerData(this, true);
		JPanel panelA = new JPanel();
		panelA = playerAData.getSidePanel();
		add(panelA, BorderLayout.WEST);

		playerBData = new PlayerData(this, false);
		JPanel panelB = new JPanel();
		panelB = playerBData.getSidePanel();
		add(panelB, BorderLayout.EAST);

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
							"Restart Game Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
							iconMedium);
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

		controlPanelOut = new JPanel();
		lb_message = new JLabel("Log do Jogo");
		lb_message.setFont(fontCurrentPlayer);
		lb_message.setPreferredSize(new Dimension(500, 20));
		lb_message.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		controlPanelOut.add(lb_message);

		controlPanel.add(buttons, BorderLayout.CENTER);
		controlPanel.add(controlPanelOut, BorderLayout.SOUTH);
		add(controlPanel, BorderLayout.SOUTH);

		buildMenu();
		currentPlayerData = playerAData;

		// Main Label
		mainLabel = new JLabel(iconImg);
		lb_message.setText("Current Player -> " + "Player A");
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
				case "Rules":
					viewRules();
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

		restartMenuItem = new JMenuItem("Restart Game", KeyEvent.VK_S);
		restartMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		restartMenuItem.addActionListener(al);
		menu.add(restartMenuItem);

		menu.addSeparator();

		viewScoresItem = new JMenuItem("View Scores", KeyEvent.VK_P);
		viewScoresItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		viewScoresItem.addActionListener(al);
		menu.add(viewScoresItem);

		menu.addSeparator();

		aboutItem = new JMenuItem("About", KeyEvent.VK_A);
		aboutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		aboutItem.addActionListener(al);
		menu.add(aboutItem);

		JMenuItem helpItem = new JMenuItem("Rules", KeyEvent.VK_R);
		helpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		helpItem.addActionListener(al);
		menu.add(helpItem);

		// set Menu Bar on JFrame
		setJMenuBar(menuBar);
	}

	/**
	 * activate About window
	 */
	private void about() {
		String content = null;
		try {
			content = new String(Files.readAllBytes(Paths.get("src/tps/tp4/about.txt")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(this, content, "About information.", JOptionPane.INFORMATION_MESSAGE, iconImg);
	}

	/**
	 * activate View scores window
	 */
	private void viewScores() {
		JOptionPane.showMessageDialog(this, highScore.getHighscoreString(), "Top Scores",
				JOptionPane.INFORMATION_MESSAGE, iconMedium);
	}

	/**
	 * activate View Rules Window
	 */
	private void viewRules() {

		// Create the nodes.
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("The Hive Game");
		createNodes(top);

		// Create a tree that allows one selection at a time.
		tree = new JTree(top);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setLeafIcon(leafIconPiece);
		renderer.setOpenIcon(leafIcon);
		renderer.setClosedIcon(leafIcon);
		tree.setCellRenderer(renderer);

		TreeSelectionListener tsl = new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

				if (node == null)
					return;

				Object nodeInfo = node.getUserObject();
				if (node.isLeaf() && (nodeInfo instanceof BookInfo)) {
					BookInfo book = (BookInfo) nodeInfo;
					displayURL(book.bookURL);

				} else {
					displayURL(referenceURL);
				}

			}
		};
		// Listen for when the selection changes.
		tree.addTreeSelectionListener(tsl);

		// Create the scroll pane and add the tree to it.
		JScrollPane treeView = new JScrollPane(tree);

		// Create the HTML viewing pane.
		htmlPane = new JEditorPane();
		htmlPane.setEditable(false);
		initReference();
		JScrollPane htmlView = new JScrollPane(htmlPane);

		// Add the scroll panes to a split pane.
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(treeView);
		splitPane.setBottomComponent(htmlView);

		Dimension minimumSize = new Dimension(100, 50);
		htmlView.setMinimumSize(minimumSize);
		treeView.setMinimumSize(minimumSize);
		splitPane.setDividerLocation(100);

		splitPane.setPreferredSize(new Dimension(500, 300));
		JOptionPane.showMessageDialog(this, splitPane, "Rules", JOptionPane.INFORMATION_MESSAGE, iconMedium);
	}

	private void createNodes(DefaultMutableTreeNode top) {
		DefaultMutableTreeNode category = null;
		DefaultMutableTreeNode book = null;

		category = new DefaultMutableTreeNode("General");
		top.add(category);

		// Components
		book = new DefaultMutableTreeNode(new BookInfo("Components", "rules/component.html"));
		category.add(book);

		// Setup of the game
		book = new DefaultMutableTreeNode(new BookInfo("Setup", "rules/setup.html"));
		category.add(book);

		// Objective of the game
		book = new DefaultMutableTreeNode(new BookInfo("Objective of the Game", "rules/objective.html"));
		category.add(book);

		// Game play
		book = new DefaultMutableTreeNode(new BookInfo("Game Play", "rules/gameplay.html"));
		category.add(book);

		// Placing
		book = new DefaultMutableTreeNode(new BookInfo("Placing", "rules/placing.html"));
		category.add(book);

		// Moving
		book = new DefaultMutableTreeNode(new BookInfo("Moving", "rules/moving.html"));
		category.add(book);

		// Pieces
		category = new DefaultMutableTreeNode("Pieces");
		top.add(category);

		// Queen Bee
		book = new DefaultMutableTreeNode(new BookInfo("Queen Bee", "rules/pieces/queenbee.html"));
		category.add(book);

		// Beetle
		book = new DefaultMutableTreeNode(new BookInfo("Beetle", "rules/pieces/beetle.html"));
		category.add(book);

		// Grasshopper
		book = new DefaultMutableTreeNode(new BookInfo("Grasshopper", "rules/pieces/grasshopper.html"));
		category.add(book);

		// Spider
		book = new DefaultMutableTreeNode(new BookInfo("Spider", "rules/pieces/spider.html"));
		category.add(book);

		// Ant
		book = new DefaultMutableTreeNode(new BookInfo("Ant", "rules/pieces/ant.html"));
		category.add(book);

		// Ladybug
		book = new DefaultMutableTreeNode(new BookInfo("Ladybug", "rules/pieces/ladybug.html"));
		category.add(book);

		// Mosquito
		book = new DefaultMutableTreeNode(new BookInfo("Mosquito", "rules/pieces/mosquito.html"));
		category.add(book);

		// Pillbug
		book = new DefaultMutableTreeNode(new BookInfo("Pillbug", "rules/pieces/pillbug.html"));
		category.add(book);

		// Restrictions
		category = new DefaultMutableTreeNode("Restrictions");
		top.add(category);

		// One Hive Move
		book = new DefaultMutableTreeNode(new BookInfo("One Hive Move", "rules/restrictions/onehivemove.html"));
		category.add(book);

		// Freedom to Move
		book = new DefaultMutableTreeNode(new BookInfo("Freedom to Move", "rules/restrictions/freedomtomove.html"));
		category.add(book);

		// Unable to Move or to Place
		book = new DefaultMutableTreeNode(
				new BookInfo("Unable to Move or to Place", "rules/restrictions/unabletomove.html"));
		category.add(book);

	}

	private class BookInfo {
		public String bookName;
		public URL bookURL;

		public BookInfo(String book, String filename) {
			bookName = book;
			bookURL = this.getClass().getResource("docs/" + filename);
			if (bookURL == null) {
				System.err.println("Couldn't find file: " + filename);
			}
		}

		public String toString() {
			return bookName;
		}
	}

	private void initReference() {
		String s = "docs/rules/" + "reference.html";
		referenceURL = this.getClass().getResource(s);
		if (referenceURL == null) {
			System.err.println("Couldn't open help file: " + s);
		}
		displayURL(referenceURL);
	}

	private void displayURL(URL url) {
		try {
			if (url != null) {
				htmlPane.setPage(url);
			} else { // null url
				htmlPane.setText("File Not Found");
			}
		} catch (IOException e) {
			System.err.println("Attempted to read a bad URL: " + url);
		}
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

	public Piece getLastMoved() {
		return lastMovedPiece;
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
			lb_message.setText("Current Player -> Player B");
		} else {
			currentPlayerData = playerAData;
			isPlayerAToPlay = true;
			playerBData.setPlayerPanelActive(!isPlayerAToPlay);
			playerAData.setPlayerPanelActive(isPlayerAToPlay);
			lb_message.setText("Current Player -> Player A");
		}
		if (!currentPlayerData.isQueenBeeAlreadyOnBoard())
			placingQueenBee = false;
		if (currentHiveLabel != null) {
			currentHiveLabel.setToNormal();
			currentHiveLabel = null;
		}
		if (currentPiece != null) {
			board.getBoardPlace(currentPiece.getX(), currentPiece.getY()).setSelected(false);
			currentPiece = null;
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
		currentPlayerData = playerAData;
		currentPlayerData.setPlayerPanelActive(true);
		playerBData.setPlayerPanelActive(false);
		isPlayerAToPlay = true;
		lb_message.setText("Current Player -> Player A");
		if (currentPiece != null)
			board.getBoardPlace(currentPiece.getX(), currentPiece.getY()).setSelected(false);
		board.resetBoard();
		board.repaint();
		currentHiveLabel = null;
		currentPiece = null;
		bn_newGame.setVisible(false);
		bn_newGame.setEnabled(false);

	}

	/**
	 * Restart the Pieces label to normal state
	 * 
	 * @param component -> components from one player
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
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, iconMedium);
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
			if (p == null && currentPiece == null) {
				return;
			}
			if (currentPiece != null) {
				if (currentPiece.equals(p)) {
					board.getBoardPlace(x, y).setSelected(false);
					currentPiece = null;
					return;
				}
				int pastX = currentPiece.getX(), pastY = currentPiece.getY();
				if (currentPlayerData.isQueenBeeAlreadyOnBoard() && currentPiece.moveTo(x, y)) {
					lastMovedPiece = currentPiece;
					board.getBoardPlace(pastX, pastY).setSelected(false);
					board.repaint();
					if (checkFinishGame()) {
						doFinishGameActions();
					}
					changePlayer();
					return;
				}
				lb_message.setText("Invalid movement!");
				return;
			} else {
				if (!isPlayerAToPlay && p.isFromPlayerA() || isPlayerAToPlay && !p.isFromPlayerA()) {
					return;
				}
				currentPiece = p;
				board.getBoardPlace(x, y).setSelected(true);
				return;
			}
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
				if (!validNeib) {
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
				}
			}
		}
		if (placingQueenBee) {
			currentPlayerData.setQueenBee(currentHiveLabel.getPiece());
			placingQueenBee = false;
		}

		board.addPiece(currentHiveLabel.getPiece(), x, y);
		board.repaint();
		currentHiveLabel.deactivate();
		currentHiveLabel = null;
		if (checkFinishGame()) {
			doFinishGameActions();
		}
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
		if (currentPiece != null) {
			board.getBoardPlace(currentPiece.getX(), currentPiece.getY()).setSelected(false);
			currentPiece = null;
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
		if (board.getBoardPlace(x + 1, y) != null || board.getBoardPlace(x, y + 1) != null) {

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
			return this.physicalMove(x, y, Direction.NE, Direction.NO);
		case NE:
			return this.physicalMove(x, y, Direction.N, Direction.SE);
		case NO:
			return this.physicalMove(x, y, Direction.N, Direction.SO);
		case S:
			return this.physicalMove(x, y, Direction.SE, Direction.SO);
		case SE:
			return this.physicalMove(x, y, Direction.NE, Direction.S);
		case SO:
			return this.physicalMove(x, y, Direction.S, Direction.NO);
		default:
			break;
		}
		return false;
	}

	private boolean physicalMove(int x, int y, Direction d1, Direction d2) {

		Point pt1 = getTarget(currentPiece.getX(), currentPiece.getY(), d1);
		Point pt2 = getTarget(currentPiece.getX(), currentPiece.getY(), d2);
		if (pt1 == null || pt2 == null)
			return false;
		Piece p1 = board.getPiece((int) pt1.getX(), (int) pt1.getY());
		Piece p2 = board.getPiece((int) pt2.getX(), (int) pt2.getY());
		if (p1 != null && p2 != null) {
			return false;
		}

		return true;
	}

	private Point getTarget(int x, int y, Direction d) {
		int auxX = 0, auxY = 0;
		Point p = null;
		switch (d) {
		case N:
			if (y == 0)
				return p;
			auxX = x;
			auxY = y - 1;
			break;

		case NE:
			if (x % 2 == 0) {
				if (x == Board.DIMX || y == 0)
					return p;
				auxX = x + 1;
				auxY = y - 1;
			} else {
				if (x == Board.DIMX)
					return p;
				auxX = x + 1;
				auxY = y;
			}
			break;
		case NO:
			if (x % 2 == 0) {
				if (x == 0 || y == 0)
					return p;
				auxX = x - 1;
				auxY = y - 1;
			} else {
				if (x == 0)
					return p;
				auxX = x - 1;
				auxY = y;
			}
			break;
		case S:
			if (y == Board.DIMY)
				return p;
			auxX = x;
			auxY = y + 1;
			break;
		case SE:
			if (x % 2 == 0) {
				if (x == Board.DIMX)
					return p;
				auxX = x + 1;
				auxY = y;
			} else {
				if (x == Board.DIMX || y == Board.DIMY)
					return p;
				auxX = x + 1;
				auxY = y + 1;
			}
			break;
		case SO:
			if (x % 2 == 0) {
				if (x == 0)
					return p;
				auxX = x - 1;
				auxY = y;
			} else {
				if (x == 0 || y == 0)
					return p;
				auxX = x - 1;
				auxY = y + 1;
			}
			break;
		default:
			break;
		}
		p = new Point(auxX, auxY);
		return p;
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

		} else {
			if (checkFinishGame(true) && checkFinishGame(false)) {
				lb_message.setText("Oh my God!! It's a Draw!!!");
				JOptionPane.showMessageDialog(this, "Oh my God!! It's a Draw!!", "DRAW!",
						JOptionPane.INFORMATION_MESSAGE, iconMedium);
				return true;
			} else if (checkFinishGame(true)) {
				playerBData.setPlayerWon(true);
				return true;
			} else if (checkFinishGame(false)) {
				playerAData.setPlayerWon(true);
				return true;
			}
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
		String winningPlayer = checkWinner ? "A" : "B";
		int n = JOptionPane.showConfirmDialog(this,
				"Congratulations Player " + winningPlayer + "\n Do you want to enter your name?", "Winner Panel",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, iconMedium);

		if (n == JOptionPane.YES_OPTION) {
			String s = (String) JOptionPane.showInputDialog(this, "Please enter your name", "Player Name",
					JOptionPane.PLAIN_MESSAGE, iconMedium, null, null);
			if (s != null)
				highScore.addScore(s, (checkWinner ? playerAData.getNumberOfMoves() : playerBData.getNumberOfMoves()));
			else
				highScore.addScore("Unknown",
						(checkWinner ? playerAData.getNumberOfMoves() : playerBData.getNumberOfMoves()));
		}

		viewScores();
		lb_message.setText("Winner: Player " + winningPlayer);
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
					if (x instanceof JLabel)
						continue;
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
		for (int i = 0; i < Board.DIMY; i++) {
			for (int j = 0; j < Board.DIMX; j++) {
				BoardPlace bp = board.getBoardPlace(j, i);
				if (i == 0 && bp.getPiece() != null) {
					lb_message.setText("Can't move up! Upper limit of map reached");
					return;
				}
				board.getBoardPlace(j, i).migrateTo(Direction.N);
			}
		}
	}

	/**
	 * move hive DOWN, if it can
	 */
	private void moveDown() {
		for (int i = (Board.DIMY - 1); i >= 0; i--) {
			for (int j = 0; j < Board.DIMX; j++) {
				BoardPlace bp = board.getBoardPlace(j, i);
				if (i == Board.DIMY - 1 && bp.getPiece() != null) {
					lb_message.setText("Can't move down! Lower limit of map reached");
					return;
				}
				board.getBoardPlace(j, i).migrateTo(Direction.S);
			}
		}
	}

	/**
	 * move hive NO, if it can
	 */
	private void moveNO() {
		boolean canMove = true;
		for (int i = 0; i < Board.DIMX; i = i + 2) {
			BoardPlace bp = board.getBoardPlace(i, 0);
			if (bp.getPiece() != null) {
				canMove = false;
				break;
			}
		}
		for (int j = 0; j < Board.DIMX; j++) {
			for (int i = 0; i < Board.DIMY; i++) {
				BoardPlace bp = board.getBoardPlace(j, i);
				if ((j == 0 && bp.getPiece() != null) || !canMove) {
					lb_message.setText("Can't move NO! Northwestern limit of map reached");
					return;
				}
				board.getBoardPlace(j, i).migrateTo(Direction.NO);
			}
		}
	}

	/**
	 * move hive NE, if it can
	 */
	private void moveNE() {
		boolean canMove = true;
		for (int i = (Board.DIMX - 1); i >= 0; i = i - 2) {
			BoardPlace bp = board.getBoardPlace(i, 0);
			if (bp.getPiece() != null) {
				canMove = false;
			}
		}
		for (int j = (Board.DIMX - 1); j >= 0; j--) {
			for (int i = 0; i < Board.DIMY; i++) {
				BoardPlace bp = board.getBoardPlace(j, i);
				if ((j == (Board.DIMX - 1) && bp.getPiece() != null) | !canMove) {
					lb_message.setText("Can't move NE! Northeastern limit of map reached");
					return;
				}
				board.getBoardPlace(j, i).migrateTo(Direction.NE);
			}
		}
	}

	/**
	 * move hive SO, if it can
	 */
	private void moveSO() {
		boolean canMove = true;
		for (int i = 1; i < Board.DIMX; i = i + 2) {
			BoardPlace bp = board.getBoardPlace(i, Board.DIMY - 1);
			if (bp.getPiece() != null) {
				canMove = false;
			}
		}
		for (int i = (Board.DIMY - 1); i >= 0; i--) {
			for (int j = 0; j < Board.DIMX; j++) {
				BoardPlace bp = board.getBoardPlace(j, i);
				if ((j == 0 && bp.getPiece() != null) || !canMove) {
					lb_message.setText("Can't move SO! Southwestern limit of map reached");
					return;
				}
				board.getBoardPlace(j, i).migrateTo(Direction.SO);
			}
		}
	}

	/**
	 * move hive SE, if it can
	 */
	private void moveSE() {
		boolean canMove = true;
		for (int i = (Board.DIMX - 2); i >= 0; i = i - 2) {
			BoardPlace bp = board.getBoardPlace(i, Board.DIMY - 1);
			if (bp.getPiece() != null) {
				canMove = false;
			}
		}
		for (int i = (Board.DIMY - 1); i >= 0; i--) {
			for (int j = (Board.DIMX - 1); j >= 0; j--) {
				BoardPlace bp = board.getBoardPlace(j, i);
				if ((j == Board.DIMX - 1 && bp.getPiece() != null) || !canMove) {
					lb_message.setText("Can't move SE! Southeastern limit of map reached");
					return;
				}
				board.getBoardPlace(j, i).migrateTo(Direction.SE);
			}
		}
	}
}
