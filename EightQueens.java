//@author Alyssa Gugneja
//ITCS 3153 Spring 2022

package queens;

import java.util.*;

public class EightQueens {
  
    final private int [][] board = new int[8][8];
    final private int [][] testBoard = new int[8][8];
    private int stateChanges = 0;
    private int queenCount = 0;
    private int heuristic = 0;
    private int lowerH = 8;
    private int totalQueens = 8;
    private int numRestarts = 0;
  
    public EightQueens() { //initializes the board
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
            	board[i][j] = 0;
            }
        }
    }
  
  
    public void random() { //randomly places 8 queens on board
      Random rand = new Random();
      
      while(queenCount < totalQueens){
            for(int i = 0; i < totalQueens; i++){
            	board[rand.nextInt(7)][i] = 1;
            	queenCount++;
                }
            }
      heuristic = heuristic(board);
    }
  
    public boolean columnCheck(int [][] check, int j) { //counts number of queens in same columns
        boolean isClash = false;
        int count = 0;
        for(int i = 0; i < 8; i++) {
            if(check[j][i] == 1){
                count++;
            }
        }
        if(count > 1) {
        	isClash = true;
        }
        return isClash;
    }
    
    public boolean rowCheck(int [][] check, int j) { //counts number of queens in same rows
        boolean isClash = false;
        int count = 0;
      
        for(int i = 0; i < 8; i++){
            if(check[i][j] == 1){
                count++;
            }
        }
        if(count > 1){
        	isClash = true;
        }
        return isClash;
    }
  
    public boolean diagonalCheck(int [][] check, int j, int k) { //counts number of queens in same diagonals
        boolean isClash = false;
      
        for(int i = 1; i < 8; i++) {
            if(isClash == true) {
                break;
            }

            if((j + i < 8) && (k + i < 8)) {
                if(check[j + i][k + i] == 1) {
                	isClash = true;
                }
            }
            
            if((j + i < 8) && (k - i >= 0)) {
                if(check[j + i][k - i] == 1) {
                	isClash = true;
                }
            }
            
            if((j - i >= 0) && (k + i < 8)) {
                if(check[j - i][k + i] == 1) {
                	isClash = true;
                }
            }  
            
            if((j - i >= 0) && (k - i >= 0)) {
                if(check[j  -i][k - i] == 1) {
                	isClash = true;
                }
            }
        }
        return isClash;
    }
  
    public int heuristic(int [][] check) { //counts total number of queens in some sort of conflict
    int count = 0;
    boolean rowClash = false;
    boolean columnClash = false;
    boolean diagonalClash = false;
      
        for(int i = 0; i < 8; i++) {
            for(int j= 0; j < 8; j++) {
                if(check[i][j] == 1) {
                    rowClash = rowCheck(check, j);
                    columnClash = columnCheck(check, i);
                    diagonalClash = diagonalCheck(check, i, j);
                  
                    if(rowClash == true || columnClash == true || diagonalClash == true) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public void move() { //moves a queen and either continues to new state, restart, or show solution
        int[][] hCheck = new int[8][8];
        int numColumns = 0;
        int minRows = 0;
        int minColumns = 0;
        int prevColQueen = 0;
      
        while(true){
        	numColumns = 0;
      
            for(int i = 0; i < 8; i++) {
            	for(int j = 0; j < 8; j++) {
            		testBoard[i][j] = board[i][j];
            	}
            }
            while(numColumns < 8) {
                for(int i = 0; i < 8;i++) {
                	testBoard[i][numColumns] = 0;
                }
                for(int i = 0; i < 8; i++) {
                    if(board[i][numColumns] == 1) {
                        prevColQueen = i;
                    }
                    testBoard[i][numColumns] = 1;
                    hCheck[i][numColumns] = heuristic(testBoard);
                    testBoard[i][numColumns] = 0;
                }
                testBoard[prevColQueen][numColumns] = 1;
                numColumns++;
            }
          
            if(determineRestart(hCheck)) {
            	queenCount = 0;
                for(int i = 0; i < 8; i++) {
                    for(int j = 0; j < 8; j++) {
                    	board[i][j] = 0;
                    }
                }
                random( );
                System.out.println("RESTART");
                numRestarts++;
            }
      
            minColumns = findMinColumns(hCheck);
            minRows = findMinRows(hCheck);
      
            for(int i = 0; i < 8; i++) {
            	board[i][minColumns] = 0;
            }
      
            board[minRows][minColumns] = 1;
            stateChanges++;
            heuristic = heuristic(board);
          
            if(heuristic(board) == 0) {
                System.out.println("\nCurrent State");
                for(int i = 0; i < 8; i++) {
                    for(int j = 0; j < 8; j++) {
                        System.out.print(board[i][j] + " ");
                    }
                    System.out.print("\n");
                }
            System.out.println("Solution Found!");
            System.out.println("State changes: " + stateChanges);
            System.out.println("Restarts: " + numRestarts);
            break;
            }

            System.out.println("\n");
            System.out.println("Current h: " + heuristic);
            System.out.println("Current State");
            for(int i = 0; i < 8; i++) {
                for(int j = 0; j < 8; j++) {
                    System.out.print(board[i][j] + " ");
                }
                System.out.print("\n");
            }
            System.out.println("Neighbors found with lower h: " + lowerH);
            System.out.println("Setting new current State");
        }
    }
    public int findMinColumns(int[][] test) { //finds column of min neighbor
        int minColumns = 8;
        int minVal = 8;
        int count = 0;
      
        for(int i = 0; i < 8; i++) {
          for(int j = 0; j < 8; j++) {
              if(test[i][j] < minVal) {
                  minVal = test[i][j];
                  minColumns = j;
              }
              if(test[i][j] < heuristic) {
                  count++;
              }
          }
        }
        lowerH = count;
        return minColumns;
    }
    public int findMinRows(int[][] test) { //finds row of min neighbor
        int minRows = 8;
        int minVal = 8;
      
        for(int i = 0; i < 8; i++) {
          for(int j = 0; j < 8; j++) {
              if(test[i][j] < minVal) {
                  minVal = test[i][j];
                  minRows = i;
              }
          }
        }
        return minRows;
    }
    public boolean determineRestart(int [][] test) { //checks if need to restart
        int minVal = 8;
        boolean restart = false;
      
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(test[i][j] < minVal) {
                    minVal = test[i][j];
                }
            }
        }
        if(lowerH == 0) {
            restart = true;
        }
        return restart;
    }

    public static void main(String[] args) {
     EightQueens queenBoard = new EightQueens( );  //Creates EightQueens object and runs program as necessary to find solution
     queenBoard.random();
     queenBoard.move();
    }
}