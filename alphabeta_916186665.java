import java.util.*;
import java.util.Collections;
import java.util.ArrayList;

public class alphabeta_916186665 extends AIModule {
    int player;
    int opponent;
    int maxDepth = 5;
    int bestMoveSeen;

    public void getNextMove(final GameStateModule game) {
        player = game.getActivePlayer();
        opponent = (game.getActivePlayer() == 1 ? 2 : 1);
        // begin recursion
        while (!terminate) {
            minimax(game, 0, player, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (!terminate)
                chosenMove = bestMoveSeen;
        }
        if (game.canMakeMove(chosenMove))
            game.makeMove(chosenMove);
    }

    public class Successor {
        int move;
        int value;

        Successor() {
            move = 0;
            value = 0;
        }

        int getVal() {
            return value;
        }
    }

    class SortbyASE implements Comparator<Successor> {
        public int compare(Successor a, Successor b) {
            return a.value - b.value;
        }
    }

    class SortbyDES implements Comparator<Successor> {
        public int compare(Successor a, Successor b) {
            return b.value - a.value;
        }
    }

    public Successor[] sucessorfunc(GameStateModule state, int playerID) {
        int num = 0;
        for (int i = 0; i < state.getWidth(); i++) {
            if (state.canMakeMove(i)) {
                num++;
            }
        }
        Successor[] successors = new Successor[num];
        int index = 0;
        for (int i = 0; i < state.getWidth(); i++) {
            if (state.canMakeMove(i)) {
                state.makeMove(i);
                successors[index] = new Successor();
                successors[index].move = i;
                successors[index].value = eval(state);
                state.unMakeMove();
                index++;
            }
        }
        if (playerID == player) {
            Arrays.sort(successors, new SortbyDES());
        } else {
            Arrays.sort(successors, new SortbyASE());
        }
        return successors;
    }

    private int minimax(final GameStateModule state, int depth, int playerID, int alpha, int beta) {
        if (terminate)
            return 0;
        if (depth == maxDepth) {
            return eval(state);
        }
        int value = 0;
        depth++;
        // max's turn
        if (playerID == player) {
            int bestVal = Integer.MIN_VALUE;
            value = Integer.MIN_VALUE + 1;
            Successor[] sucessors = sucessorfunc(state, player);
            for (Successor successor : sucessors) {
                if (state.canMakeMove(successor.move)) {
                    state.makeMove(successor.move);
                    value = Math.max(value, minimax(state, depth, opponent, alpha, beta));
                    state.unMakeMove();
                    if (value > bestVal) {
                        bestVal = value;
                        if (depth == 1) { // top of recursion, make our move choice
                            bestMoveSeen = successor.move;
                            // System.out.println("Best move seen: " + bestMoveSeen);
                            // System.out.println("Best move seen: " + successor.value);
                        }
                    }
                    alpha = Math.max(alpha, bestVal);
                    if (beta <= alpha) {
                        // System.out.println("MAX:" + depth);
                        break;
                    }
                }
            }
            return value;
            // min's turn
        } else {
            int bestVal = Integer.MAX_VALUE;
            value = Integer.MAX_VALUE;
            // for (int i = 0; i < state.getWidth(); i++) {
            Successor[] sucessors = sucessorfunc(state, opponent);
            for (Successor successor : sucessors) {
                if (state.canMakeMove(successor.move)) {
                    state.makeMove(successor.move);
                    value = Math.min(value, minimax(state, depth, player, alpha, beta));
                    state.unMakeMove();
                    bestVal = Math.min(bestVal, value);
                    beta = Math.min(beta, bestVal);
                    if (beta <= alpha) {
                        // System.out.println("MIN:" + depth);
                        break;
                    }
                }
            }
            return value;
        }
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

    private int eval(final GameStateModule state) {
        int eScore = getScore(getBoard(state));
        return eScore;
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