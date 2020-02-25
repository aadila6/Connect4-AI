import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.awt.Point;
import java.util.List;
import java.util.Comparator;
import java.util.Arrays;

public class MiniMaxAI extends AIModule
{
  // returns a matrix showing the current board state
  // row 1 of the matrix represents column 1 of the board,
  // cells in the matrix represent which player's piece occupies the position
  // players represented by numbers 1 and 2
  // empty spaces represented by 0
   public ArrayList<ArrayList<Integer>> getBoard(final GameStateModule game){
    ArrayList<ArrayList<Integer>> Board = new ArrayList<ArrayList<Integer>>();
    for (int i = 0; i < game.getWidth(); i++){
      ArrayList<Integer> Column = new ArrayList<Integer>();
      for (int j = 0; j < game.getHeight(); j++){
          int TileOwner = game.getAt(i,j);
          Column.add(TileOwner);
        }
      Board.add(Column);
    }
    return Board;
  }
  
  // returns key corresponding to highest value in HashMap
  public int getHashMapMax(HashMap<Integer,Integer> map){
    Integer max = null;
    int maxMove = -1;
    for (Map.Entry<Integer,Integer> entry : map.entrySet()){
      if (max == null){
        max = entry.getValue();
        maxMove = entry.getKey();
      } else {
        if (entry.getValue() > max){
          max = entry.getValue();
          maxMove = entry.getKey();
        }
      }
    }
    return maxMove;
  }
  
   public int getHashMapMin(HashMap<Integer,Integer> map){
    Integer min = null;
    int minMove = -1;
    for (Map.Entry<Integer,Integer> entry : map.entrySet()){
      if (min == null){
        min = entry.getValue();
        minMove = entry.getKey();
      } else {
        if (entry.getValue() < min){
          min = entry.getValue();
          minMove = entry.getKey();
        }
      }
    }
    return minMove;
  }
  
   public int getHashMapMinVal(HashMap<Integer,Integer> map){
    Integer min = 999999;
    int minMove = -1;
    for (Map.Entry<Integer,Integer> entry : map.entrySet()){
      if (min == 999999){
        min = entry.getValue();
        minMove = entry.getKey();
      } else {
        if (entry.getValue() < min){
          min = entry.getValue();
          minMove = entry.getKey();
        }
      }
    }
    return min;
  }
  
    public int getHashMapMaxVal(HashMap<Integer,Integer> map){
    Integer max = -999999;
    int maxMove = -1;
    for (Map.Entry<Integer,Integer> entry : map.entrySet()){
      if (max == -999999){
        max = entry.getValue();
        maxMove = entry.getKey();
      } else {
        if (entry.getValue() > max){
          max = entry.getValue();
          maxMove = entry.getKey();
        }
      }
    }
    return max;
  }
  
  // Places piece into a matrix board, Used to replicate board state in the situation when    // we place a piece in the column
  public void placePiece(int Move, ArrayList<ArrayList<Integer>> Board, int CurrentPlayer, int[] heights){
    heights[Move] += 1;
    for (int i = 0; i < Board.get(Move).size(); i++){
      if (Board.get(Move).get(i) == 0){
        Board.get(Move).set(i, CurrentPlayer);
        return;
      }
    }
  }
  
  // remove the piece to make it the original board again
  public void removePiece(int Move, ArrayList<ArrayList<Integer>> Board, int CurrentPlayer, int[] heights){
    heights[Move] -= 1;
    for (int i = 0; i < 6; i++){
      if (Board.get(Move).get(i) == 0){
        Board.get(Move).set(i-1, 0);
        return;
      }
      if (i == 5){
        Board.get(Move).set(i, 0);
        return;
      }
    }
  }
  
   public void placePieceOrder(int Move, ArrayList<ArrayList<Integer>> Board, int CurrentPlayer){
    for (int i = 0; i < Board.get(Move).size(); i++){
      if (Board.get(Move).get(i) == 0){
        Board.get(Move).set(i, CurrentPlayer);
        return;
      }
    }
  }
  
  public void removePieceOrder(int Move, ArrayList<ArrayList<Integer>> Board, int CurrentPlayer){
    for (int i = 0; i < 6; i++){
      if (Board.get(Move).get(i) == 0){
        Board.get(Move).set(i-1, 0);
        return;
      }
      if (i == 5){
        Board.get(Move).set(i, 0);
        return;
      }
    }
  }
  
  public int SwitchPiece(int Piece){
    if (Piece == 1){
      return 2;
    }
    else{
      return 1;
    }
  }
  
  public int minChoice(ArrayList<ArrayList<Integer>> Board, int CurrentPlayer, int depth, int CurrentPiece, ArrayList<Integer> AlphaBeta, ArrayList<Integer> ParentAlphaBeta, int[] heights, int points, int enemypoints, HashMap<String,Integer> Record){

    HashMap<Integer,Integer> MoveEvaluationScores = new HashMap<Integer,Integer>();
    ArrayList<Integer> Order = getMoveOrderingMin(Board, CurrentPlayer, CurrentPiece);
    for (int i = 0; i < Order.size(); i++){
      int Score = Evaluation(Order.get(i), Board, CurrentPlayer, depth - 1, CurrentPiece, AlphaBeta, heights, points, enemypoints, Record);
        if (Score <= ParentAlphaBeta.get(0)){

            return -9999999;
          }
        if (Score < AlphaBeta.get(1)){
          AlphaBeta.set(1,Score);
        }

        MoveEvaluationScores.put(Order.get(i),Score);
    }

    int MinVal = getHashMapMinVal(MoveEvaluationScores);
    AlphaBeta.set(0,MinVal);
    if (MinVal > ParentAlphaBeta.get(0)){
      ParentAlphaBeta.set(0, MinVal);
    }
    return MinVal;
  }
  
  public int maxChoice(ArrayList<ArrayList<Integer>> Board, int CurrentPlayer, int depth, int CurrentPiece, ArrayList<Integer> AlphaBeta, ArrayList<Integer> ParentAlphaBeta, int[] heights, int points, int enemypoints, HashMap<String,Integer> Record){

    HashMap<Integer,Integer> MoveEvaluationScores = new HashMap<Integer,Integer>();
    ArrayList<Integer> Order = getMoveOrderingMax(Board, CurrentPlayer, CurrentPlayer);
    for (int i = 0; i < Order.size(); i++){
        int Score = Evaluation(Order.get(i), Board, CurrentPlayer, depth - 1, CurrentPiece, AlphaBeta, heights, points, enemypoints, Record);
        if (Score >= ParentAlphaBeta.get(1)){

            return 9999999;
          }
        if (Score > AlphaBeta.get(0)){
          AlphaBeta.set(0,Score);
        }

        MoveEvaluationScores.put(Order.get(i),Score);
    }

    int MaxVal = getHashMapMaxVal(MoveEvaluationScores);
    AlphaBeta.set(1,MaxVal);
    if (MaxVal < ParentAlphaBeta.get(1)){
      ParentAlphaBeta.set(1, MaxVal);
    }
    return MaxVal;
  }
  
  
  // if the opposing piece is in one of the squares, returns 0 because cannot form a 4 in a row in this sequence
  // otherwise count the number of pieces that your piece occupies
  // 1 gives 1 point
  // 2 gives 4 points
  // 3 gives 10 points
  // no particular reason why I chose these numbers, maybe some other numbers might be better
  public int rate(int num1, int num2, int num3, int num4, int piece){
    if ((num1 != 0 && num1!= piece )|| (num2 != 0 && num2!= piece ) || (num3 != 0 && num3!= piece ) || (num4 != 0 && num4 != piece )){
      return 0;
    } else {
      int count = 0;
      if (num1 == piece){
        count += 1;
      }
      if (num2 == piece){
        count += 1;
      }
      if (num3 == piece){
        count += 1;
      }
      if (num4 == piece){
        count += 1;
      }
      if (count == 0){
        return 0;
      }
      if (count == 1){
        return 1;
      }
      if (count == 2){
        return 4;
      }
      if (count == 3){
        return 10;
      }
      if (count == 4){
        return -1;  // if -1 is returned from a rate function we should exit immediately.
      }
      
    }
      return 1;
  }
  
  // if there is a four in a row returns -1
  // Checks every combination of 4's. Then applies rate function to rate the 4's
  public int getScore(ArrayList<ArrayList<Integer>> Board, int piece){
  int score = 0;
  int increment = 0;
  // score all combinations of 4 vertical
    for (int i = 0; i < 3; i++){
      for (int j = 0; j < 7; j++){
        increment = rate(Board.get(j).get(i), Board.get(j).get(i+1) , Board.get(j).get(i+2), Board.get(j).get(i+3), piece);
        if (increment == -1){
          return -1;
        }
        score += increment;
      }
    }
  // score all combinations of 4 horizontal
    for (int i = 0; i < 6; i++){
      for (int j = 0; j < 4; j++){
        increment = rate(Board.get(j).get(i), Board.get(j+1).get(i) , Board.get(j+2).get(i), Board.get(j+3).get(i), piece);
        if (increment == -1){
          return -1;
        }
        score += increment;
      }
    }
  // score all combinations of 4 diagonal '/'
    for (int i = 0; i < 3; i++){
      for (int j = 0; j < 4; j++){
        increment = rate(Board.get(j).get(i), Board.get(j+1).get(i+1) , Board.get(j+2).get(i+2), Board.get(j+3).get(i+3), piece);
        if (increment == -1){
          return -1;
        }
        score += increment;
      }
    }
  // score all combinations of 4 diagonal '\'
    for (int i = 5; i > 2; i--){
      for (int j = 0; j < 4; j++){
        increment = rate(Board.get(j).get(i), Board.get(j+1).get(i-1) , Board.get(j+2).get(i-2), Board.get(j+3).get(i-3), piece);
        if (increment == -1){
          return -1;
        }
        score += increment;
      }
    }
    return score;
  }
  
public int updateScore(ArrayList<ArrayList<Integer>> Board, int i, int previousScore, int piece, int[] heights){
    int update = 0;
    int increment = 0;

    for (int j = heights[i]; j > -1; j--){
      if (j < 3){
      increment = rate(Board.get(i).get(j), Board.get(i).get(j+1) , Board.get(i).get(j+2), Board.get(i).get(j+3), piece);
      if (increment == -1){
        return -1;
        }
      update += increment;
      if (j == heights[i]){
      increment = rate(0, Board.get(i).get(j+1) , Board.get(i).get(j+2), Board.get(i).get(j+3), piece);
      }
      if (j == heights[i]-1){
      increment = rate(Board.get(i).get(j), 0 , Board.get(i).get(j+2), Board.get(i).get(j+3), piece);
      }
      if (j == heights[i]-2){
      increment = rate(Board.get(i).get(j), Board.get(i).get(j+1) , 0, Board.get(i).get(j+3), piece);
      }
      if (j == heights[i]-3){
      increment = rate(Board.get(i).get(j), Board.get(i).get(j+1) , Board.get(i).get(j+2), 0, piece);
      }
      update -= increment;
    }
    }
    
    for (int mincol = i - 3; mincol <= i; mincol++){
      if ((mincol > -1) && ((mincol + 3) < 7)){
        increment = rate(Board.get(mincol).get(heights[i]), Board.get(mincol+1).get(heights[i]), Board.get(mincol+2).get(heights[i]), Board.get(mincol+3).get(heights[i]), piece);
        if (increment == -1){
        return -1;
        }
        update += increment;
        if (i == mincol && heights[i] < 6){
        increment=rate(0, Board.get(mincol+1).get(heights[i]), Board.get(mincol+2).get(heights[i]), Board.get(mincol+3).get(heights[i]), piece);
        } else if (i == mincol+1){
        increment=rate(Board.get(mincol).get(heights[i]),0, Board.get(mincol+2).get(heights[i]), Board.get(mincol+3).get(heights[i]), piece);
        } else if (i == mincol+2){
        increment=rate(Board.get(mincol).get(heights[i]), Board.get(mincol+1).get(heights[i]), 0, Board.get(mincol+3).get(heights[i]), piece);
        } else if (i == mincol+3){
        increment=rate(Board.get(mincol).get(heights[i]), Board.get(mincol+1).get(heights[i]), Board.get(mincol+2).get(heights[i]), 0, piece);
        } 
        if (increment == -1){
        return -1;
        }
        update -= increment;
      }
    }

    
    for (int mincol = i - 3; mincol <= i; mincol++){
      if (((mincol > -1) && ((mincol + 3) < 7)) && ( ((heights[i]-(i-mincol)) > -1) && ((heights[i] + 3 -(i-mincol) < 6) ))){
        increment =rate(Board.get(mincol).get(heights[i]-(i-mincol)), Board.get(mincol+1).get(heights[i]-(i-mincol)+1), Board.get(mincol+2).get(heights[i]-(i-mincol)+2), Board.get(mincol+3).get(heights[i]-(i-mincol)+3), piece);
        if (increment == -1){
        return -1;
        }
        update += increment;
        if (i == mincol && heights[i] < 6){
        increment = rate(0, Board.get(mincol+1).get(heights[i]-(i-mincol)+1), Board.get(mincol+2).get(heights[i]-(i-mincol)+2), Board.get(mincol+3).get(heights[i]-(i-mincol)+3), piece);
        } else if (i == mincol+1){
        increment =rate(Board.get(mincol).get(heights[i]-(i-mincol)), 0, Board.get(mincol+2).get(heights[i]-(i-mincol)+2), Board.get(mincol+3).get(heights[i]-(i-mincol)+3), piece);
        } else if (i == mincol+2){
        increment =rate(Board.get(mincol).get(heights[i]-(i-mincol)), Board.get(mincol+1).get(heights[i]-(i-mincol)+1), 0, Board.get(mincol+3).get(heights[i]-(i-mincol)+3), piece);
        } else if (i == mincol+3){
        increment =rate(Board.get(mincol).get(heights[i]-(i-mincol)), Board.get(mincol+1).get(heights[i]-(i-mincol)+1), Board.get(mincol+2).get(heights[i]-(i-mincol)+2), 0, piece);
        } 
        if (increment == -1){
        return -1;
        }
        update -= increment;
      }
    } 

    for (int mincol = i - 3; mincol <= i; mincol++){
        if (((mincol > -1) && ((mincol + 3) < 7)) && ( ((heights[i]+ (i-mincol)) < 6) && ((heights[i] +(i-mincol) - 3 > -1) ))){
        increment =rate(Board.get(mincol).get(heights[i]+(i-mincol)), Board.get(mincol+1).get(heights[i]+(i-mincol)-1), Board.get(mincol+2).get(heights[i]+(i-mincol)-2), Board.get(mincol+3).get(heights[i]+(i-mincol)-3), piece);
        if (increment == -1){
        return -1;
        }
        update+=increment;

        if (i == mincol && heights[i] < 6){
        increment = rate(0, Board.get(mincol+1).get(heights[i]+(i-mincol)-1), Board.get(mincol+2).get(heights[i]+(i-mincol)-2), Board.get(mincol+3).get(heights[i]+(i-mincol)-3), piece);
        } else if (i == mincol+1){
        increment =rate(Board.get(mincol).get(heights[i]+(i-mincol)), 0, Board.get(mincol+2).get(heights[i]+(i-mincol)-2), Board.get(mincol+3).get(heights[i]+(i-mincol)-3), piece);
        } else if (i == mincol+2){
        increment =rate(Board.get(mincol).get(heights[i]+(i-mincol)), Board.get(mincol+1).get(heights[i]+(i-mincol)-1), 0, Board.get(mincol+3).get(heights[i]+(i-mincol)-3), piece);
        } else if (i == mincol+3){
        increment =rate(Board.get(mincol).get(heights[i]+(i-mincol)), Board.get(mincol+1).get(heights[i]+(i-mincol)-1), Board.get(mincol+2).get(heights[i]+(i-mincol)-2), 0, piece);
        } 
        if (increment == -1){
        return -1;
        }
        update -= increment;
      }

    } 
    return previousScore + update;
  }

  public int Evaluation(int Move, ArrayList<ArrayList<Integer>> Board, int CurrentPlayer, int depth, int CurrentPiece, ArrayList<Integer> ParentAlphaBeta, int[] heights, int parentpoints, int parentenemypoints, HashMap<String,Integer> Record){
    
    
  
    ArrayList<Integer> AlphaBeta = new ArrayList<Integer>();
    AlphaBeta.add(-9999999);
    AlphaBeta.add(9999999);
    placePiece(Move,Board,CurrentPiece, heights);
    String key = Board.toString();
    if (Record.containsKey(key) == true){
      removePiece(Move,Board,CurrentPiece, heights);
      return Record.get(key);
    }
    int currentScore;
    int myScore = updateScore(Board, Move, parentpoints, CurrentPlayer, heights);;
    int enemyScore = updateScore(Board, Move, parentenemypoints, SwitchPiece(CurrentPlayer), heights);
    
    if (CurrentPlayer == CurrentPiece){
      if (myScore == -1){
        removePiece(Move,Board,CurrentPiece, heights);
        return 9999999;
      }
    } else {
      if (enemyScore == -1){
        removePiece(Move,Board,CurrentPiece, heights);
        return -9999999;
      }
    }
    if (depth == 0){
          removePiece(Move,Board,CurrentPiece, heights);
          return myScore - enemyScore;
    }
    int NextPiece = SwitchPiece(CurrentPiece);
    if (NextPiece == CurrentPlayer){
      currentScore = maxChoice(Board,CurrentPlayer,depth,NextPiece, AlphaBeta, ParentAlphaBeta, heights, myScore, enemyScore, Record);
    } else {
      currentScore = minChoice(Board,CurrentPlayer,depth,NextPiece, AlphaBeta, ParentAlphaBeta, heights, myScore, enemyScore, Record);
   }
     Record.put(key, currentScore);
    removePiece(Move,Board,CurrentPiece, heights);
    return currentScore;
  }



public ArrayList<Integer> getMoveOrderingMin(ArrayList<ArrayList<Integer>> Board, int CurrentPlayer, int CurrentPiece){
  HashMap<Integer,Integer> Orders = new HashMap<Integer,Integer>();
  ArrayList<Integer> Ordering = new ArrayList<Integer>();
  for (int i = 0; i < 7; i++){
    if (Board.get(i).get(5) == 0){
    placePieceOrder(i,Board,CurrentPiece);
    int score = getScore(Board, CurrentPlayer) - getScore(Board, SwitchPiece(CurrentPlayer));
    Orders.put(i,score);
    removePieceOrder(i,Board,CurrentPiece);
    }
  }

  int now = Orders.size();
  for (int i = 0; i < now; i++){

    int num = getHashMapMin(Orders);
    Ordering.add(num);
    Orders.remove(num);
  }
  return Ordering;
}

public ArrayList<Integer> getMoveOrderingMax(ArrayList<ArrayList<Integer>> Board, int CurrentPlayer, int CurrentPiece){
  HashMap<Integer,Integer> Orders = new HashMap<Integer,Integer>();
  ArrayList<Integer> Ordering = new ArrayList<Integer>();
  for (int i = 0; i < 7; i++){
    if (Board.get(i).get(5) == 0){
    placePieceOrder(i,Board,CurrentPiece);
    int score = getScore(Board, CurrentPlayer) - getScore(Board, SwitchPiece(CurrentPlayer));
    Orders.put(i,score);
    removePieceOrder(i,Board,CurrentPiece);
    }
  }

  int now = Orders.size();
  for (int i = 0; i < now; i++){

    int num = getHashMapMax(Orders);
    Ordering.add(num);
    Orders.remove(num);
  }
  return Ordering;
}


  public void getNextMove(final GameStateModule game){
    int CurrentPlayer = game.getActivePlayer();
    ArrayList<Integer> AlphaBeta = new ArrayList<Integer>();
    AlphaBeta.add(-9999999);
    AlphaBeta.add(9999999);
    
    
    ArrayList<ArrayList<Integer>> Board = getBoard(game);
    ArrayList<Integer> Order = getMoveOrderingMax(Board, CurrentPlayer, CurrentPlayer);
    String key = Board.toString();
    
    int[] heights = {game.getHeightAt(0)-1,game.getHeightAt(1)-1,game.getHeightAt(2)-1,game.getHeightAt(3)-1,game.getHeightAt(4)-1,game.getHeightAt(5)-1,game.getHeightAt(6)-1};

    int points = getScore(Board, CurrentPlayer);
    int enemypoints = getScore(Board,SwitchPiece(CurrentPlayer));
    int value = points - enemypoints;
    //System.out.println(value);
    HashMap<String,Integer> Record = new HashMap<String,Integer>();
    
    HashMap<Integer,Integer> MoveEvaluationScores = new HashMap<Integer,Integer>();

		for(int i = 0; i < Order.size(); i++){
        int Score = Evaluation(Order.get(i), Board, CurrentPlayer, 5, CurrentPlayer, AlphaBeta, heights, points, enemypoints, Record);
        //System.out.println(Score);
        MoveEvaluationScores.put(Order.get(i),Score);
    }
 
    chosenMove = getHashMapMax(MoveEvaluationScores);
  }
}
