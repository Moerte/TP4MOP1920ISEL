package tps.tp4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
		// TODO
	}

	/**
	 * the JFrame initialization method
	 */
	private void init() {
		setSize(800, 600);
		setLayout(new CenterLayout());

		setLocationRelativeTo(null);

		board = new Board(this, fontPieces);
		add(board, BorderLayout.CENTER);

		// add a spider - just to check
		Spider s = new Spider(this, true);
		getBoard().addPiece(s, 10, 5);

		// TODO

		setVisible(true);
	}

	/**
	 * build menu
	 */
	private void buildMenu() {
		// must have: Restart game, ViewScores, About
		// TODO
	}

	/**
	 * activate About window
	 */
	private void about() {
		// TODO
		System.out.println("About window...");
	}

	/**
	 * activate View scores window
	 */
	private void viewScores() {
		// TODO
		System.out.println("View Scores window...");
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
		// TODO
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
	 * By physical we mean, that the piece has physical space to move. A piece,
	 * with the NE and NO places occupied, cannot move to N.
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
