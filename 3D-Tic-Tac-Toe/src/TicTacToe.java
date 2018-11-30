/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
   Aidan Chandra
   Homework #
   Nov 14, 2018
 */
public class TicTacToe {

    
    int[][] firstPlane = new int[4][4];
    int[][] secondPlane = new int[4][4];
    int[][] thirdPlane = new int[4][4];
    int[][] fourthPlane = new int[4][4];
    public int[][][] board = {firstPlane,secondPlane,thirdPlane,fourthPlane};
    
    
    public TicTacToe(){
        clearBoard();
    }
    public TicTacToe(int[][][] board){
        this.board = board;
        clearBoard();
        
    }
    private void clearBoard(){
        for(int x = 0; x < 4; x++){
            for(int y = 0; y < 4; y++){
                for(int z = 0; z < 4; z++){
                    board[x][y][z] = -1;
                }
            }
        }
    }
    
    /*
    player: 0 or 1
    */
    public boolean makeMove(int playerID, int xpos, int ypos, int zpos){
        if(board[xpos][ypos][zpos] != -1)
            return false;
        board[xpos][ypos][zpos] = playerID;
        return true;
    }
    
    public boolean isGoalState(int playerID){
        
    }
    
}
