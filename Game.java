package tps.tp4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import classcode.p15Swing.p02buildedLayouts.ProportionalLayout;
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
		// TODO Nao percebo este metodo
		String fontType = "Comic Sans MS";
		int size = 40;
		Font f1 = new Font(fontType, Font.BOLD, size);
		// setFont(f1);
	}

	/**
	 * the JFrame initialization method
	 */
	private void init() {
		setTitle("Hive Game");
		setSize(1000, 700);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLayout(new CenterLayout());

		setLocationRelativeTo(null);

		board = new Board(this, fontPieces);
		add(board, BorderLayout.CENTER);

		// add a spider - just to check
		Spider s = new Spider(this, true);
		getBoard().addPiece(s, 4, 5);

		JPanel panelTop = new JPanel(new BorderLayout());
		panelTop.setBackground(Color.LIGHT_GRAY);

		String fontType = "Comic Sans MS";
		int size = 25;
		Font f1 = new Font(fontType, Font.BOLD, size);

		// Playable Buttons
		controlPanel = new JPanel();
		controlPanel.setSize(700, 50);

		ActionListener al = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				switch (e.getActionCommand()) {
				case "start_again":
					startAgain();
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
					changePlayer();
					break;
				case "give_up":
					giveUp();
					break;
				}
			}
		};

		bn_moveUp = new JButton("Start Again");
		bn_moveUp.setActionCommand("start_again");
		bn_moveUp.addActionListener(al);
		controlPanel.add(bn_moveUp);

		bn_moveUp = new JButton("Move UP");
		bn_moveUp.setActionCommand("move_up");
		bn_moveUp.addActionListener(al);
		controlPanel.add(bn_moveUp);

		bn_moveDown = new JButton("Move Down");
		bn_moveDown.setActionCommand("move_down");
		bn_moveDown.addActionListener(al);
		controlPanel.add(bn_moveDown);

		bn_moveNO = new JButton("Move NO");
		bn_moveNO.setActionCommand("move_no");
		bn_moveNO.addActionListener(al);
		controlPanel.add(bn_moveNO);

		bn_moveNE = new JButton("Move NE");
		bn_moveNE.setActionCommand("move_ne");
		bn_moveNE.addActionListener(al);
		controlPanel.add(bn_moveNE);

		bn_moveSE = new JButton("Move SE");
		bn_moveSE.setActionCommand("move_se");
		bn_moveSE.addActionListener(al);
		controlPanel.add(bn_moveSE);

		bn_moveSO = new JButton("Move SO");
		bn_moveSO.setActionCommand("move_so");
		bn_moveSO.addActionListener(al);
		controlPanel.add(bn_moveSO);

		bn_changePlayer = new JButton("Change Player");
		bn_changePlayer.setActionCommand("change_player");
		bn_changePlayer.addActionListener(al);
		controlPanel.add(bn_changePlayer);

		bn_giveUp = new JButton("Give Up");
		bn_giveUp.setActionCommand("give_up");
		bn_giveUp.addActionListener(al);
		controlPanel.add(bn_giveUp);

		// TODO colocar isto por baixo dos botoes...
		JTextField log = new JTextField();
		log.setEnabled(false);
		controlPanel.add(log, BorderLayout.PAGE_END);

		add(controlPanel, BorderLayout.SOUTH);
		// End Playable buttons

		// TODO
		// build menu

		playerAData = new PlayerData(this, true);
		JPanel panelA = new JPanel();
		panelA = playerAData.getSidePanel();
		add(panelA, BorderLayout.WEST);

		playerBData = new PlayerData(this, false);
		JPanel panelB = new JPanel();
		panelB = playerBData.getSidePanel();
		add(panelB, BorderLayout.EAST);

		// Main Label
		mainLabel = new JLabel("HIVE GAME: Current Player -> " + getPlayerData(true));
		mainLabel.setFont(f1);
		mainLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panelTop.add(mainLabel, BorderLayout.CENTER);
		add(panelTop, BorderLayout.NORTH);

		buildMenu();

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
					startAgain();
					System.out.println("testing...Menu");
					break;
				case "View Scores":
					viewScores();
					break;
				case "About":
					about();
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

		aboutItem = new JMenuItem("About", KeyEvent.VK_A);
		aboutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		aboutItem.addActionListener(al);
		menu.add(aboutItem);

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
		// TODO
	}

	/**
	 * change player actions
	 */
	private void changePlayer() {
		// TODO
	}

	/**
	 * start again actions
	 */
	private void startAgain() {
		// TODO Adicionar a decisão
		int n = JOptionPane.showConfirmDialog(this, "Are you sure about your decision?", "Restart Game Confirmation",
				JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.YES_OPTION) {

		} else if (n == JOptionPane.NO_OPTION) {

		}
	}

	private void giveUp() {
		// TODO Adicionar a decisão
		int n = JOptionPane.showConfirmDialog(this, "Are you that chicken?", "Give Up Confirmation",
				JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.YES_OPTION) {

		} else if (n == JOptionPane.NO_OPTION) {

		}
	}

	/**
	 * check if coordinate x,y only have friendly neighbor of current player
	 */
	private boolean onlyHaveFriendlyNeighbors(int x, int y) {
		// TODO
		return false;
	}

	/**
	 * a click on the board
	 */
	public void clickOnBoard(int x, int y) {
		// TODO
	}

	/**
	 * a click on a label on side panel
	 */
	public void clickOnPieceLabelOnSidePanel(HiveLabel hl) {
		// TODO
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
		// TODO
		return false;
	}

	/**
	 * check if can move physically from x,y in to the direction received
	 * 
	 * By physical we mean, that the piece has physical space to move. A piece, with
	 * the NE and NO places occupied, cannot move to N.
	 */
	public boolean canPhysicallyMoveTo(int x, int y, Direction d) {
		// TODO
		return false;
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
		// TODO
	}

	/**
	 * check if end of game - update playerAWon and/or playerBWon states
	 */
	private boolean checkFinishGame() {
		// TODO
		return false;
	}

	/**
	 * check if queen of received player is surrounded
	 */
	private boolean checkFinishGame(boolean playerA) {
		// TODO
		return false;
	}

	/**
	 * do end of game actions
	 */
	private void doFinishGameActions() {
		// TODO
	}

	/**
	 * change state of general buttons
	 */
	private void enableControlButtons(boolean enable) {
		// TODO
	}

	/**
	 * move hive UP, if it can be moved
	 */
	private void moveHiveUp() {
		// TODO
		System.out.println("teste");

	}

	/**
	 * move hive DOWN, if it can
	 */
	private void moveDown() {
		// TODO
	}

	/**
	 * move hive NO, if it can
	 */
	private void moveNO() {
		// TODO
	}

	/**
	 * move hive NE, if it can
	 */
	private void moveNE() {
		// TODO
	}

	/**
	 * move hive SO, if it can
	 */
	private void moveSO() {
		// TODO
	}

	/**
	 * move hive SE, if it can
	 */
	private void moveSE() {
		// TODO
	}

}
