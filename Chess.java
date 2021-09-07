
public class Chess
{
    private ChessViewer viewer;
    
    private final int size = 8;
    public boolean isPieceSelected;
    private int[] pieceSelectedCoordinate;
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
        this.selectedPiece = null;
        this.isPieceSelected = false;
        this.currentTurn = "WHITE";
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
    
    public boolean isEmpty (int x, int y) {
        return board[x][y] == Pieces.EMPTY;
    }
    
    public void changeTurn() {
        if (currentTurn.equals("WHITE")) {this.currentTurn = "BLACK";}
        else                             {this.currentTurn = "WHITE";}
    }
    
    public void movePiece(int x1, int y1, int x2, int y2) {
        board[x1][y1] = Pieces.EMPTY;
        board[x2][y2] = selectedPiece;
        changeTurn();
        viewer.displayBoard();
    }
    
    public void leftClick(int x, int y) {
        if (isPieceSelected) {
            if (x != pieceSelectedCoordinate[0] || y != pieceSelectedCoordinate[1])
                viewer.displayBoard();
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
        if (!pieceColour(x, y).equals(currentTurn) && !isEmpty(x, y)) {
            viewer.displayBoard(); 
            return;
        }
        if (isEmpty(x, y)) {
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
        if (piece.equals("KNIGHT")) {}
        if (piece.equals("CASTLE")) {castle(x, y);}
        if (piece.equals("PAWN"))   {pawn(x, y);}            
    }
    
    public void pawn(int x, int y) {
        int z;
        String possibleMoves = "";
        String oppositeColour = "";        
        if (pieceColour(x, y).equals("WHITE")) {oppositeColour = "BLACK"; z =  1;} 
        else                                   {oppositeColour = "WHITE"; z = -1;}
        if (isEmpty(x, y - z))                 {possibleMoves +=  x      + "" + (y - z)       + " ";} // forward
        if (isLegal (x - z, y - z) && pieceColour(x - z, y - z).equals(oppositeColour))  
                                               {possibleMoves += (x - z) + "" + (y - z)       + " ";} // forward left
        if (isLegal (x + z, y - z) && pieceColour(x + z, y - z).equals(oppositeColour))  
                                               {possibleMoves += (x + z) + "" + (y - z)       + " ";} // forward right
        if (isEmpty(x, y - z) && y == 6 || y == 1 && isEmpty(x, y - z) && isEmpty(x, y - (2 * z)))
                                               {possibleMoves +=  x      + "" + (y - (2 * z)) + " ";} // jump at start
        viewer.drawPossibleMoves(possibleMoves);
    }
    
    public void castle (int x, int y) {
        String pieceColour = pieceColour(x, y);
        String possibleMoves = "";
        String oppositeColour = "";
        if (pieceColour(x, y).equals("WHITE")) {oppositeColour = "BLACK";}
        else                                   {oppositeColour = "WHITE";}
        for(int z = y - 1; isLegal(x, z) && !pieceColour(x, z).equals(pieceColour); z--) 
            {possibleMoves += x + "" + z + " "; if (pieceColour(x, z).equals(oppositeColour)) {break;}} // up
        for(int z = y + 1; isLegal(x, z) && !pieceColour(x, z).equals(pieceColour); z++) 
            {possibleMoves += x + "" + z + " "; if (pieceColour(x, z).equals(oppositeColour)) {break;}} // down
        for(int z = x - 1; isLegal(z, y) && !pieceColour(z, y).equals(pieceColour); z--) 
            {possibleMoves += z + "" + y + " "; if (pieceColour(z, y).equals(oppositeColour)) {break;}} // left
        for(int z = x + 1; isLegal(z, y) && !pieceColour(z, y).equals(pieceColour); z++) 
            {possibleMoves += z + "" + y + " "; if (pieceColour(z, y).equals(oppositeColour)) {break;}} // right
        viewer.drawPossibleMoves(possibleMoves);
    }
    }