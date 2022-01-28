import java.util.Scanner;

class Main {
  private static char[][] board = new char[3][3];
  private static int size = board.length;
  private static char human = ' ';
  private static char computer = ' ';
  private static int humanX, humanY, bestMoveX, bestMoveY, CPUXrand, CPUYrand;
  private static boolean gameOn = true;
  private static boolean humanGoesFirst = true;
  private static boolean CPUfirstTurn = true;
  
  //sets
  public static void setEmpty(){
    for(int i = 0; i<board.length; i++){
      for(int j = 0; j<board.length; j++){
        board[i][j]=' ';
      }
    }
  }


  public static void printBoard(){
    System.out.println("  "+ board[0][0]+ " | "+board[0][1] +" | "+ board[0][2] +"   ");
    System.out.println("----+---+----");
    System.out.println("  "+ board[1][0]+ " | "+board[1][1] +" | "+ board[1][2] +"   ");
    System.out.println("----+---+----");
    System.out.println("  "+ board[2][0]+ " | "+board[2][1] +" | "+ board[2][2] +"   ");
  }

  public static void setChar(){
    Scanner sc = new Scanner(System.in);
    System.out.println("Play as X or O?");
    char player = sc.nextLine().charAt(0);
    while(player!='X'&&player!='O'){
      System.out.println("Enter either X or O");
      player = sc.nextLine().charAt(0);
    }
    switch(player){
      case('X'):
        human = 'X';
        computer = 'O';
        break;
      case('O'):
        human = 'O';
        computer = 'X';
        break;
    }

  }

  public static void goesFirst(){
    if((int)(Math.random()*2)==0){
      humanGoesFirst = false;
     }else{
       humanGoesFirst = true;
    }
  }

  public static void input(){
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter X (0-2) and Y(0-2) coordinates, separated by lines");
    humanX = sc.nextInt();
    humanY = sc.nextInt();
    while((humanX>2||humanY>2)||!spaceIsEmpty(humanX,humanY)){
      System.out.println("Renter a valid position. X (0-2) and Y(0-2), separated by lines");
      humanX = sc.nextInt();
      humanY = sc.nextInt();
    }
  }

  public static void placeOnBoard(int X, int Y, char c){
    board[X][Y]= c;
  }

  public static boolean boardIsEmpty(){
    for(int i = 0; i<board.length; i++){
      for(int j = 0; j<board.length; j++){
        if(board[i][j]==' ')
          return true;
      }
    }
    return false;
  }

  public static boolean spaceIsEmpty(int X, int Y){
    return(board[X][Y]==' ');
  }

  public static boolean checkWin(char c){ 
    int colCount=0;
    int dLRCount=0;
    int dRLCount=0;

    for(int i = 0; i<size; i++){
      //test row
      if((board[i][0]==c && board[i][1]==c)&&board[i][2]==c)
        return true; 
      //test diagonals
      if (board[i][i]==c)
        dLRCount++;
      if (board[i][size-1-i]==c)
        dRLCount++;
      //test cols
      colCount=0;
      for(int j = 0; j<size; j++){
        if(board[j][i]==c)
          colCount++;
      }
      if ((colCount ==3||dLRCount==3)||dRLCount==3)
        return true;
    }
    return false;
  }

  public static void CPU1Turn(){
      CPUXrand = (int)(Math.random()*2);
      CPUYrand = (int)(Math.random()*2);
      if(CPUXrand == 1){
        CPUXrand = 2;
      }
      if(CPUYrand == 1){
        CPUYrand = 2;
    }
  }


  //computer will be maxmizing, human will be minimizing
  //return the board's score
  public static int minimax(int depth, boolean isMaxmizing){
    if(checkWin(computer))
      return 10 - depth;//penalize for depth/how many turns needed to win
    if (checkWin(human))
      return -10 +depth;
    if(!boardIsEmpty()){
      return 0;
    }

    if(isMaxmizing){
      int best = -1000000;
      for(int i =0; i<size; i++){
        for(int j=0; j<size; j++){
          if(spaceIsEmpty(i, j)){
            board[i][j]=computer;
            best = Math.max(best, minimax(depth+1, false));
            board[i][j]=' ';
          }
        }
      }
      return best;

    }else{
      int best = 10000000;
      for(int i =0; i<size; i++){
        for(int j=0; j<size; j++){
          if(spaceIsEmpty(i, j)){
            board[i][j]= human;
            best = Math.min(best, minimax(depth+1, true));
            board[i][j]=' ';
          }
        }
      }
      return best;
    }
  }
  
  //find best move for maximizing player/computer
  public static void findBestMove(){
    int bestVal = -10000000;
    int moveVal = 0;
    for(int i = 0; i<size; i++){
      for(int j = 0; j<size; j++){
        if(spaceIsEmpty(i, j)){
          board[i][j] = computer;
          moveVal = minimax(0, false); //next move is minimizing
          board[i][j]=' ';
          if(moveVal>bestVal){
            bestMoveX = i;
            bestMoveY = j;
            bestVal = moveVal;
          }
        }
      }
    }
  }


  public static void main(String[] args) {
    while(gameOn){
      System.out.println("TIC  TAC  TOE");
      CPUfirstTurn=true;
      setEmpty();
      printBoard();
      setChar();
      goesFirst();
      
      if(humanGoesFirst){
        System.out.println("You go first!");
      }else{
        System.out.println("Computer goes first!");
      }

      while((boardIsEmpty()&&!checkWin(human))&&!checkWin(computer)){
        if(humanGoesFirst){
          input();
          placeOnBoard(humanX, humanY, human);
          printBoard();
          if((boardIsEmpty()&&!checkWin(human))&&!checkWin(computer)){
            System.out.println("Computer played:");
            findBestMove();
            placeOnBoard(bestMoveX, bestMoveY, computer);
            printBoard();
          }
        }else{
            System.out.println("Computer played:");
            if(CPUfirstTurn){
              CPU1Turn();
              placeOnBoard(CPUXrand, CPUYrand, computer);
              printBoard();
              CPUfirstTurn = false;
            }else{
              findBestMove();
              placeOnBoard(bestMoveX, bestMoveY, computer);
              printBoard();
            }
            
          if((boardIsEmpty()&&!checkWin(human))&&!checkWin(computer)){
            input();
            placeOnBoard(humanX, humanY, human);
            printBoard();
          }
        }
        
      }

      if(checkWin(computer)){
        System.out.println("YOU LOSE!");
      }else if(checkWin(human)){
        System.out.println("YOU WIN!");
      }else{
        System.out.println("TIE!");
      }

      System.out.println("Play Again? Y/N");
      Scanner sc = new Scanner(System.in);
      char answer = sc.nextLine().charAt(0);
      while(!(answer=='Y'||answer=='N')){
        System.out.println("Answer Y or N, Play Again?");
        answer = sc.nextLine().charAt(0);
      }

      if(answer =='N')
        gameOn = false;
      
    }

  }

}