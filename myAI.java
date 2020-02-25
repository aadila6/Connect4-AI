import java.util.*;

public class myAI extends AIModule {
    int player;
    int opponent;
    int maxDepth = 4;
    int bestMoveSeen;
    int evaluate_count=0;

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
            System.out.println("Player is : " + player);
            System.out.println("Opponent is : " + opponent);
    }

    private int minimax(final GameStateModule state, int depth, int playerID) {
        if (terminate)
            return 0;
        if (depth == maxDepth) {
            int score = eval(state,playerID);
            // System.out.println("Chosen score value is: "+  score);
            return score;
        }
        depth++;
        int value = 0;
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
                            //CHECK THE ENEMY'S STATE and return that col if its 4s
                            int check = getDangerousCol(getBoard(state), opponent);
                            if(check == -1){
                                //Meaning enemy has a 4 is to be connected! We need to block it 
                                System.out.println("DANGEROUS COLUMN : "+ dangerous);
                                // bestMoveSeen = dangerous;
                            }else{
                                bestMoveSeen = i;
                            }
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
                    // System.out.println("Current Value: " + value);
                    state.unMakeMove();
                }
            }
            return value;
        }
    }

    // randomly assigns a value to a state
    private int eval(final GameStateModule state, int playerID) {
        // Before we evaluate player's best next move, we need to think should I check opponent's connected 3? or 2?
        // Give some prioritization on that.
        int playerScore = getScore(getBoard(state), player);
        return playerScore;
    }
    int dangerous = -1;

    public int getDangerousCol(ArrayList<ArrayList<Integer>> Board, int playerID) {
        int playercheck = 0;
        // score all combinations of 4 vertical '|'
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 7; x++) {
                playercheck = score(Board.get(x).get(y), Board.get(x).get(y + 1), Board.get(x).get(y + 2),
                        Board.get(x).get(y + 3), playerID);
                if (playercheck == 10 ) {
                    dangerous = y;
                    return -1;
                }
                // score -= enemycheck;
            }
        }
        // score all combinations of 4 horizontal '--'
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 4; x++) {
                playercheck = score(Board.get(x).get(y), Board.get(x + 1).get(y), Board.get(x + 2).get(y),
                        Board.get(x + 3).get(y), playerID);
                if (playercheck == 10 ) {
                    dangerous = y;
                    return -1;
                }
                // score -= enemycheck;
            }
        }

        // score all combinations of 4 diagonal '/'
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 4; x++) {
                playercheck = score(Board.get(x).get(y), Board.get(x + 1).get(y + 1), Board.get(x + 2).get(y + 2),
                        Board.get(x + 3).get(y + 3), playerID);
               
                if (playercheck == 10) {
                    dangerous = y;
                    return -1;
                }
            }
        }

        // score all combinations of 4 diagonal '\'
        for (int y = 5; y > 2; y--) {
            for (int x = 0; x < 4; x++) {
                playercheck = score(Board.get(x).get(y), Board.get(x + 1).get(y - 1), Board.get(x + 2).get(y - 2),
                        Board.get(x + 3).get(y - 3), playerID);
                if (playercheck == 10) {
                    dangerous = y;
                    return -1;
                }
            }
        }
        return 0;
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

    public int score(int num1, int num2, int num3, int num4, int playerID) {
        //either cannot connect or its all enemies's
        //We need to check again for enemy's position to update the scores
        if ((num1 != 0 && num1 != playerID) || (num2 != 0 && num2 != playerID) || (num3 != 0 && num3 != playerID)
                || (num4 != 0 && num4 != playerID)) {
            return 0;
        } else {
            int count = 0;
            if (num1 == playerID) {
                count += 1;
            }
            if (num2 == playerID) {
                count += 1;
            }
            if (num3 == playerID) {
                count += 1;
            }
            if (num4 == playerID) {
                count += 1;
            }
            if (count == 0) {
                return 0;
            }
            if (count == 1) {
                return 1;
            }
            if (count == 2) {
                return 4;
            }
            if (count == 3) {
                return 10;
            }
            if (count == 4) {
                return -1; 
            }
        }
        return 1;
    }
    
    public int getScore(ArrayList<ArrayList<Integer>> Board, int playerID) {
        int score = 0;
        int playercheck = 0;
        int enemycheck = 0;
        // score all combinations of 4 vertical '|'
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 7; x++) {
                playercheck = score(Board.get(x).get(y), Board.get(x).get(y + 1), Board.get(x).get(y + 2),
                        Board.get(x).get(y + 3), playerID);
                // enemycheck = score(Board.get(x).get(y), Board.get(x).get(y + 1), Board.get(x).get(y + 2),
                //         Board.get(x).get(y + 3), opponent);
                if (playercheck == -1 ) {
                    return -1;
                }
                score += playercheck;
                // score -= enemycheck;
            }
        }
        // score all combinations of 4 horizontal '--''
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 4; x++) {
                playercheck = score(Board.get(x).get(y), Board.get(x + 1).get(y), Board.get(x + 2).get(y),
                        Board.get(x + 3).get(y), playerID);
                // enemycheck = score(Board.get(x).get(y), Board.get(x + 1).get(y), Board.get(x + 2).get(y),
                // Board.get(x + 3).get(y), opponent);
                if (playercheck == -1 ) {
                    return -1;
                }
                score += playercheck;
                // score -= enemycheck;
            }
        }

        // score all combinations of 4 diagonal '/'
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 4; x++) {
                playercheck = score(Board.get(x).get(y), Board.get(x + 1).get(y + 1), Board.get(x + 2).get(y + 2),
                        Board.get(x + 3).get(y + 3), playerID);
                // enemycheck = score(Board.get(x).get(y), Board.get(x + 1).get(y + 1), Board.get(x + 2).get(y + 2),
                // Board.get(x + 3).get(y + 3), opponent);
                if (playercheck == -1) {
                    return -1;
                }
                score += playercheck;
                // score -= enemycheck;
            }
        }

        // score all combinations of 4 diagonal '\'
        for (int y = 5; y > 2; y--) {
            for (int x = 0; x < 4; x++) {
                playercheck = score(Board.get(x).get(y), Board.get(x + 1).get(y - 1), Board.get(x + 2).get(y - 2),
                        Board.get(x + 3).get(y - 3), playerID);
                // enemycheck = score(Board.get(x).get(y), Board.get(x + 1).get(y - 1), Board.get(x + 2).get(y - 2),
                // Board.get(x + 3).get(y - 3), opponent);
                if (playercheck == -1) {
                    return -1;
                }
                score += playercheck;
                // score -= enemycheck;
            }
        }
        return score;
    }
}