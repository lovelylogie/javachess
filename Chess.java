
public class Chess
{
    private ChessViewer viewer;
    
    private final int size = 8;
    public boolean isPieceSelected;
    public int[] pieceSelectedCoordinate;
    public int[] pieceMovedCoordinate;
    public int[] possibleMoves;
    private Pieces selectedPiece;
    private Pieces[][] board;
    public TileColour[][] tileColour;
    private String currentTurn;

    public Chess()
    {
        board      = new Pieces    [size][size];
        tileColour = new TileColour[size][size];
        pieceSelectedCoordinate = new int[2];
        pieceMovedCoordinate    = new int[2];
        selectedPiece   = null;
        isPieceSelected = false;
        currentTurn     = "WHITE";
        // kings        
        board[4][0] = Pieces.BLACK_KING;
        board[4][7] = Pieces.WHITE_KING;        
        // queens        
        board[3][0] = Pieces.BLACK_QUEEN;
        board[3][7] = Pieces.WHITE_QUEEN;       
        // bishops        
        board[2][0] = Pieces.BLACK_BISHOP;
        board[5][0] = Pieces.BLACK_BISHOP;
        board[2][7] = Pieces.WHITE_BISHOP;
        board[5][7] = Pieces.WHITE_BISHOP;        
        // knights        
        board[1][0] = Pieces.BLACK_KNIGHT;
        board[6][0] = Pieces.BLACK_KNIGHT;
        board[1][7] = Pieces.WHITE_KNIGHT;
        board[6][7] = Pieces.WHITE_KNIGHT;        
        // castles        
        board[0][0] = Pieces.BLACK_CASTLE;
        board[7][0] = Pieces.BLACK_CASTLE;
        board[0][7] = Pieces.WHITE_CASTLE;
        board[7][7] = Pieces.WHITE_CASTLE;        
        // pawns        
        for (int i = 0; i < size; i++) {board[i][1] = Pieces.BLACK_PAWN;}
        for (int i = 0; i < size; i++) {board[i][6] = Pieces.WHITE_PAWN;}
        // empty spaces
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) 
                if (board[i][j] == null) {board[i][j] = Pieces.EMPTY;}
        // tile colour
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                if (j % 2 == 0 && i % 2 == 0 || 
                    j % 2 == 1 && i % 2 == 1) {tileColour[i][j] = TileColour.WHITE;}
                else                          {tileColour[i][j] = TileColour.BLACK;}
            }
    }
    
    public boolean isLegal(int k) {
        return k >= 0 && k < size;
    }
    
    public boolean isLegal(int x, int y) {
        return isLegal(x) && isLegal(y);
    }       
    
    public Pieces getBoard(int x, int y) {
        return board[x][y];
    }
    
    public TileColour getTileColour(int x, int y) {
        return tileColour[x][y];
    }
    
    public String pieceColour(int x, int y) {
        return board[x][y].name().substring(0,5);
    }
    
    public String getPiece(int x, int y) {
        return board[x][y].name().substring(6);
    }
    
    public boolean isEmpty(int x, int y) {
        return board[x][y] == Pieces.EMPTY;
    }
    
    public boolean isBlocked(int x, int y) {
        if (x == pieceSelectedCoordinate[0]) {
            for (int i = 0; i < 8; i++) {
                
            }
        }
        
        return true;
    }
    
    public boolean isWhite(int x, int y) {
        return pieceColour(x, y).equals("WHITE");
    }
    
    public boolean canTakePiece(int x, int y) {
        if (isEmpty(x, y)) return false;
        else               return !pieceColour(pieceSelectedCoordinate[0], pieceSelectedCoordinate[1]).equals(pieceColour(x, y));
    }
    
    public boolean isSameColour(int x, int y) {
        return pieceColour(pieceSelectedCoordinate[0], pieceSelectedCoordinate[1]).equals(pieceColour(x, y));
    }
    
    public void changeTurn() {
        if (currentTurn.equals("WHITE")) {this.currentTurn = "BLACK";}
        else                             {this.currentTurn = "WHITE";}
    }
    
    public void movePiece(int x1, int y1, int x2, int y2) {
        board[x1][y1] = Pieces.EMPTY;
        board[x2][y2] = selectedPiece;
        changeTurn();
        this.pieceMovedCoordinate[0] = x2;
        this.pieceMovedCoordinate[1] = y2;
        viewer.displayBoard();
        viewer.drawMoveMade();
        viewer.drawPieces();
    }
    
    public void leftClick(int x, int y) {
        if (isPieceSelected) {
            if (x != pieceSelectedCoordinate[0] || y != pieceSelectedCoordinate[1])
                {viewer.displayBoard();}
            for (int i = 0; i < possibleMoves.length; i++) {
                int x_coordinate = possibleMoves[i] / 10; 
                int y_coordinate = possibleMoves[i] % 10;
                if (x == x_coordinate && y == y_coordinate) {
                    movePiece(pieceSelectedCoordinate[0], pieceSelectedCoordinate[1], x, y); 
                    this.isPieceSelected = false; 
                    return;
                }
            }
        }
        if (!pieceColour(x, y).equals(currentTurn) && !isEmpty(x, y) || isEmpty(x, y)) {
            this.isPieceSelected = false;
            viewer.displayBoard(); 
            return;
        }
        this.selectedPiece = getBoard(x, y);
        this.isPieceSelected = true;
        this.pieceSelectedCoordinate[0] = x; 
        this.pieceSelectedCoordinate[1] = y;
        String piece = getPiece(x, y);
        if (piece.equals("KING"))   {}
        if (piece.equals("QUEEN"))  {}
        if (piece.equals("BISHOP")) {}
        if (piece.equals("KNIGHT")) {knight(x, y);}
        if (piece.equals("CASTLE")) {castle(x, y);}
        if (piece.equals("PAWN"))   {pawn(x, y);}            
    }
    
    private void possibleMoves(String moves) {
        if (moves.equals("")) {return;}
        String coordinates_str[] = moves.split(" ");
        int arraySize = coordinates_str.length;
        int [] coordinates_int = new int [arraySize];
        for (int i = 0; i < arraySize; i++) 
            {coordinates_int[i] = Integer.parseInt(coordinates_str[i]);}
        this.possibleMoves = coordinates_int;
        viewer.drawPossibleMoves(coordinates_int);
    }
    
    public void pawn(int x, int y) {
        int z;
        String moves = "";
        String oppositeColour = "";        
        if (isWhite(x, y)) z =  1;
        else               z = -1;
        if (isEmpty(x, y - z)) moves +=  x      + "" + (y - z)       + " "; // forward
        if (isLegal(x - z, y - z) && canTakePiece(x - z, y - z))  
                               moves += (x - z) + "" + (y - z)       + " "; // forward left
        if (isLegal(x + z, y - z) && canTakePiece(x + z, y - z))  
                               moves += (x + z) + "" + (y - z)       + " "; // forward right
        if (isEmpty(x, y - z) && y == 6 || y == 1 && isEmpty(x, y - z) && isEmpty(x, y - (2 * z)))
                               moves +=  x      + "" + (y - (2 * z)) + " "; // jump at start
        possibleMoves(moves);
    }
    
    public void castle(int x, int y) {
        String moves = "";
        for(int z = y - 1; isLegal(x, z) && !isSameColour(x, z); z--) 
            {moves += x + "" + z + " "; if  (canTakePiece(x, z)) break;} // up
        for(int z = y + 1; isLegal(x, z) && !isSameColour(x, z); z++) 
            {moves += x + "" + z + " "; if  (canTakePiece(x, z)) break;} // down
        for(int z = x - 1; isLegal(z, y) && !isSameColour(z, y); z--) 
            {moves += z + "" + y + " "; if  (canTakePiece(z, y)) break;} // left
        for(int z = x + 1; isLegal(z, y) && !isSameColour(z, y); z++) 
            {moves += z + "" + y + " "; if  (canTakePiece(z, y)) break;} // right
        possibleMoves(moves);
    }
    
    public void knight(int x, int y) {
        String moves = "";
        for (int[] ds : new int[][] {{-1,-2},{-2,-1},{-2, 1},{-1, 2},{1, 2},{2, 1},{2,-1},{1,-2}}) {
            int x1 = x + ds[0];
            int y1 = y + ds[1];
            if (isLegal(x1, y1))
                if (canTakePiece(x1, y1) || isEmpty(x1, y1)) moves += x1 + "" + y1 + " ";
        }
        possibleMoves(moves);
    }
}