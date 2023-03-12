package hw2;

/**
 * Model of a Monopoly-like game. Two players take turns rolling dice to move
 * around a board. The game ends when one of the players has at least
 * MONEY_TO_WIN money or one of the players goes bankrupt (has negative money).
 * 
 * @author Autrin Hakimi
 */
public class CyGame {
	private int numSquares;
	private int player1Money;
	private int player1Square;
	private int player1Units;
	private int player2Money;
	private int player2Square;
	private int player2Units;
	private int playersTurn;
	/**
	 * Do nothing square type.
	 */
	public static final int DO_NOTHING = 0;
	/**
	 * Pass go square type.
	 */
	public static final int PASS_GO = 1;
	/**
	 * Cyclone square type.
	 */
	public static final int CYCLONE = 2;
	/**
	 * Pay the other player square type.
	 */
	public static final int PAY_PLAYER = 3;
	/**
	 * Get an extra turn square type.
	 */
	public static final int EXTRA_TURN = 4;
	/**
	 * Jump forward square type.
	 */
	public static final int JUMP_FORWARD = 5;
	/**
	 * Stuck square type.
	 */
	public static final int STUCK = 6;
	/**
	 * Points awarded when landing on or passing over go.
	 */
	public static final int PASS_GO_PRIZE = 200;
	/**
	 * The amount paid to the other player per unit when landing on a PAY_PLAYER
	 * square.
	 */
	public static final int PAYMENT_PER_UNIT = 20;
	/**
	 * The amount of money required to win.
	 */
	public static final int MONEY_TO_WIN = 400;
	/**
	 * The cost of one unit.
	 */
	public static final int UNIT_COST = 50;

	/**
	 * Constructs a game that has the given number of squares and starts both
	 * players on square 0.
	 * 
	 * @param numSquares, number of squares on board
	 * @param startingMoney, starting money for each player
	 * 
	 */
	public CyGame(int numSquares, int startingMoney) {
		this.numSquares = numSquares;
		player1Money = startingMoney;
		player2Money = startingMoney;
		player1Units = 1;
		player2Units = 1;
		playersTurn = 1;
		player2Square = 0;
		player1Square = 0;

	}

	/**
	 * Method called to indicate the current player attempts to buy one unit.
	 */
	public void buyUnit() {
		if (getCurrentPlayer() == 1 && !isGameEnded()) {
			if (getSquareType(getPlayer1Square()) == DO_NOTHING && UNIT_COST <= getPlayer1Money()) {
				player1Money -= UNIT_COST;
				player1Units += 1;
				endTurn();
			}
		} else if (getCurrentPlayer() == 2 && !isGameEnded()){
			if (getSquareType(getPlayer2Square()) == DO_NOTHING && UNIT_COST <= getPlayer2Money()) {
				player2Money -= UNIT_COST;
				player2Units += 1;
				endTurn();
			}
		}
	}

	/**
	 * Change the current turn from Player 1 to Player 2 or vice versa.
	 */
	public void endTurn() {
		if (playersTurn == 1){
			playersTurn =2;
		}
		else {
			playersTurn =1;
		}
	}

	/**
	 * Get the current player.
	 * 
	 * @return 1 or 2, indicating the player's turn.
	 */
	public int getCurrentPlayer() {
		return playersTurn;
	}

	/**
	 * Get Player 1's money.
	 * 
	 * @return player1Money
	 */
	public int getPlayer1Money() {
		return player1Money;
	}

	/**
	 * Get Player 1's square.
	 * 
	 * @return player1Square
	 */
	public int getPlayer1Square() {
		return player1Square;
	}

	/**
	 * Get Player 1's units.
	 * 
	 * @return player1Units
	 */
	public int getPlayer1Units() {
		return player1Units;
	}

	/**
	 * Get Player 2's money.
	 * 
	 * @return player2Money
	 */
	public int getPlayer2Money() {
		return player2Money;
	}

	/**
	 * Get Player 2's square.
	 * 
	 * @return player2Square
	 */
	public int getPlayer2Square() {
		return player2Square;
	}

	/**
	 * Get Player 2's units.
	 * 
	 * @return player2Units
	 */
	public int getPlayer2Units() {
		return player2Units;
	}

	/**
	 * Get the type of square for the given square number.
	 * 
	 * @param square
	 * @return type, the type of the square.
	 */
	public int getSquareType(int square) {

		int type = 0;

		if (square == 0) {
			type = PASS_GO;
		} else if (square == (numSquares - 1)) {
			type = CYCLONE;
		} else if (square % 5 == 0) {
			type = PAY_PLAYER;
		} else if (square % 7 == 0 || square % 11 == 0) {
			type = EXTRA_TURN;
		} else if (square % 3 == 0) {
			type = STUCK;
		} else if (square % 2 == 0) {
			type = JUMP_FORWARD;
		} else {
			type = DO_NOTHING;
		}

		return type;
	}

	/**
	 * Returns true if game is over, false otherwise.
	 * 
	 * @return true or false.
	 */
	public boolean isGameEnded() {
		if (player1Money >= MONEY_TO_WIN || player1Money < 0) {
			return true;
		}
		if (player2Money >= MONEY_TO_WIN || player2Money < 0) {
			return true;
		}
		return false;
	}

	/**
	 * Method called to indicate the dice has been rolled.
	 * It can advance the current player forward by a number
	 * of squares determined by the number rolled.
	 * 
	 * @param value
	 */
	public void roll(int value) {
		// Player 1:
		if (getCurrentPlayer() == 1 && !isGameEnded()) {
			if (getSquareType(player1Square) == STUCK) {
				if (value % 2 == 0) {
					player1Square = (player1Square + value);
				} else {
					endTurn();
					return; //we want to quit the method.
				}
			} else {
				player1Square = (player1Square + value);
			}
			if (player1Square >= numSquares) {
				player1Square %= numSquares;
				player1Money += PASS_GO_PRIZE;
			}

			int type = getSquareType(player1Square);
			if (type == PAY_PLAYER) {
				player1Money -= PAYMENT_PER_UNIT * getPlayer2Units();
				player2Money += PAYMENT_PER_UNIT * getPlayer2Units();
				endTurn();
			} else if (type == CYCLONE) {
				player1Square = player2Square;
				endTurn();
			} else if (type == EXTRA_TURN) {
				playersTurn = 1;
			} else if (type == STUCK) {
				endTurn();
			}else if (type == JUMP_FORWARD) {
				if (player1Square + 4 > numSquares) {
					player1Money += PASS_GO_PRIZE;
				}
				player1Square = (player1Square + 4) % numSquares;
				if (player1Square > (numSquares)) {
					player1Money += PASS_GO_PRIZE;
				}
				endTurn();
			} else if (type == DO_NOTHING) {
				endTurn();
			}
		} 

		// Player 2:
		else if (getCurrentPlayer() == 2 && !isGameEnded()) {
			if (getSquareType(player2Square) == STUCK) {
				if (value % 2 == 0) {
					player2Square = (player2Square + value);
				} else {
					endTurn();
					return; //we want to quit the method.
				}
			} else {
				player2Square = (player2Square + value);
			}
			if (player2Square >= numSquares) {
				player2Square %= numSquares;
				player2Money += PASS_GO_PRIZE;
				endTurn();
			}

			int type = getSquareType(player2Square);
			if (type == PAY_PLAYER) {
				player2Money -= PAYMENT_PER_UNIT * getPlayer1Units();
				player1Money += PAYMENT_PER_UNIT * getPlayer1Units();
				endTurn();
			} else if (type == CYCLONE) {
				player2Square = player1Square;
				endTurn();
			} else if (type == EXTRA_TURN) {
				playersTurn = 2;
			} else if (type == STUCK) {
				endTurn();
			}else if (type == JUMP_FORWARD) {
				if (player1Square + 4 > numSquares) {
					player1Money += PASS_GO_PRIZE;
				}
				player2Square = (player2Square + 4) % numSquares;
				if (player2Square > (numSquares)) {
					player2Money += PASS_GO_PRIZE;
				}
				endTurn();
			} else if (type == DO_NOTHING) {
				endTurn();
			}
		}
	}

	/**
	 * Method called to indicate the current player attempts to sell one unit.
	 */
	public void sellUnit() {
		if (getCurrentPlayer() == 1 && !isGameEnded()) {
			if (player1Units >= 1) {
				player1Money += UNIT_COST;
				player1Units -= 1;
				endTurn();
			}
		} else if (getCurrentPlayer() == 2 && !isGameEnded()) {
			if (player2Units >= 1) {
				player2Money += UNIT_COST;
				player2Units -= 1;
				endTurn();
			}
		}
	}

	/**
	 * Returns a one-line string representation of the current game state. The
	 * format is:
	 * <p>
	 * <tt>Player 1*: (0, 0, $0) Player 2: (0, 0, $0)</tt>
	 * <p>
	 * The asterisks next to the player's name indicates which players turn it is.
	 * The numbers (0, 0, $0) indicate which square the player is on, how many units
	 * the player has, and how much money the player has respectively.
	 * 
	 * @return one-line string representation of the game state
	 */
	public String toString() {
		String fmt = "Player 1%s: (%d, %d, $%d) Player 2%s: (%d, %d, $%d)";
		String player1Turn = "";
		String player2Turn = "";
		if (getCurrentPlayer() == 1) {
			player1Turn = "*";
		} else {
			player2Turn = "*";
		}
		return String.format(fmt, player1Turn, getPlayer1Square(), getPlayer1Units(), getPlayer1Money(), player2Turn,
				getPlayer2Square(), getPlayer2Units(), getPlayer2Money());
	}

}
