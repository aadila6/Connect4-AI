import java.util.*;

public class minimax_916186665 extends AIModule {
    int player;
    int opponent;
    int maxDepth = 5;
    int bestMoveSeen;

    public void getNextMove(final GameStateModule game) {
        player = game.getActivePlayer();
        opponent = (game.getActivePlayer() == 1 ? 2 : 1);
        // begin recursion
        while (!terminate) {
            minimax(game, 0, player);
            if (!terminate)
                chosenMove = bestMoveSeen;
        }
        if (game.canMakeMove(chosenMove))
            game.makeMove(chosenMove);
    }

    private int minimax(final GameStateModule state, int depth, int playerID) {
        if (terminate)
            return 0;
        if (depth == maxDepth) {
            return eval(state);
        }
        int value = 0;
        depth++;
        // max's turn
        int bestVal = Integer.MIN_VALUE;
        if (playerID == player) {
            value = Integer.MIN_VALUE + 1;
            for (int i = 0; i < state.getWidth(); i++) {
                if (state.canMakeMove(i)) {
                    state.makeMove(i);
                    value = Math.max(value, minimax(state, depth, opponent));
                    state.unMakeMove();
                    if (value > bestVal) {
                        bestVal = value;
                        if (depth == 1) { // top of recursion, make our move choice
                            bestMoveSeen = i;
                        }
                    }
                }
            }
            return value;
            // min's turn
        } else {
            value = Integer.MAX_VALUE;
            for (int i = 0; i < state.getWidth(); i++) {
                if (state.canMakeMove(i)) {
                    state.makeMove(i);
                    value = Math.min(value, minimax(state, depth, player));
                    state.unMakeMove();
                }
            }
            return value;
        }
    }

    private int eval(final GameStateModule state) {
        int eScore = getScore(getBoard(state));
        return eScore;
    }

    public ArrayList<ArrayList<Integer>> getBoard(final GameStateModule game) {
        ArrayList<ArrayList<Integer>> Board = new ArrayList<ArrayList<Integer>>();
        for (int x = 0; x < game.getWidth(); x++) {
            ArrayList<Integer> col = new ArrayList<Integer>();
            for (int y = 0; y < game.getHeight(); y++) {
                int check = game.getAt(x, y);
                col.add(check);
            }
            Board.add(col);
        }
        return Board;
    }

    public int score(int a, int b, int c, int d, int playerID, int x) {
        int count = 0;
        if ((a != 0 && a != playerID) || (b != 0 && b != playerID) || (c != 0 && c != playerID)
                || (d != 0 && d != playerID)) {
            return 0;
        } else {
            if (a == playerID) {
                count++;
            }
            if (b == playerID) {
                count++;
            }
            if (c == playerID) {
                count++;
            }
            if (d == playerID) {
                count++;
            }
            if (count == 4) {
                return -1;
            }
            if (x == 3) {
                if (a == playerID) {
                    count += 3;
                }
            } else if (x == 2 || x == 4) {
                if (a == playerID) {
                    count += 2;
                }
            }
        }
        return count;
    }

    public int getScore(ArrayList<ArrayList<Integer>> Board) {
        int score = 0;
        int playercheck = 0;
        int enemycheck = 0;
        // CHECKING '|'
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 7; x++) {
                playercheck = score(Board.get(x).get(y), Board.get(x).get(y + 1), Board.get(x).get(y + 2),
                        Board.get(x).get(y + 3), player, x);
                enemycheck = score(Board.get(x).get(y), Board.get(x).get(y + 1), Board.get(x).get(y + 2),
                        Board.get(x).get(y + 3), opponent, x);
                if (playercheck == -1) {
                    score += 1000;
                } else if (enemycheck == -1) {
                    score -= 1000;
                } else {
                    score += playercheck;
                    score -= enemycheck;
                }
            }
        }
        // CHECKING '--'
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 4; x++) {
                playercheck = score(Board.get(x).get(y), Board.get(x + 1).get(y), Board.get(x + 2).get(y),
                        Board.get(x + 3).get(y), player, x);
                enemycheck = score(Board.get(x).get(y), Board.get(x + 1).get(y), Board.get(x + 2).get(y),
                        Board.get(x + 3).get(y), opponent, x);
                if (playercheck == -1) {
                    score += 1000;
                } else if (enemycheck == -1) {
                    score -= 1000;
                } else {
                    score += playercheck;
                    score -= enemycheck;
                }
            }
        }
        // CHECKING '/'
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 4; x++) {
                playercheck = score(Board.get(x).get(y), Board.get(x + 1).get(y + 1), Board.get(x + 2).get(y + 2),
                        Board.get(x + 3).get(y + 3), player, x);
                enemycheck = score(Board.get(x).get(y), Board.get(x + 1).get(y + 1), Board.get(x + 2).get(y + 2),
                        Board.get(x + 3).get(y + 3), opponent, x);
                if (playercheck == -1) {
                    score += 1000;
                } else if (enemycheck == -1) {
                    score -= 1000;
                } else {
                    score += playercheck;
                    score -= enemycheck;
                }
            }
        }
        // CHECKING '\'
        for (int y = 5; y > 2; y--) {
            for (int x = 0; x < 4; x++) {
                playercheck = score(Board.get(x).get(y), Board.get(x + 1).get(y - 1), Board.get(x + 2).get(y - 2),
                        Board.get(x + 3).get(y - 3), player, x);
                enemycheck = score(Board.get(x).get(y), Board.get(x + 1).get(y - 1), Board.get(x + 2).get(y - 2),
                        Board.get(x + 3).get(y - 3), opponent, x);
                if (playercheck == -1) {
                    score += 1000;
                } else if (enemycheck == -1) {
                    score -= 1000;
                } else {
                    score += playercheck;
                    score -= enemycheck;
                }
            }
        }
        return score;
    }
}