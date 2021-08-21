
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

    public Chess()
    {
        board      = new Pieces    [size][size];
        tileColour = new TileColour[size][size];
        //tile_colour = new 
        pieceSelectedCoordinate = new int[2];
        this.selectedPiece = null;
        this.isPieceSelected = false;
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
                    j % 2 == 1 && i % 2 == 1)   {tileColour[i][j] = TileColour.WHITE;}              
                else                            {tileColour[i][j] = TileColour.BLACK;}
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
    
    public boolean isCoordEmpty (int x, int y) {
        return board[x][y] == Pieces.EMPTY;
    }
    
    public void movePiece(int x1, int y1, int x2, int y2) {
        board[x1][y1] = Pieces.EMPTY;
        board[x2][y2] = selectedPiece;
        viewer.displayBoard();
    }
    
    public void replacePawnWithQueen(int x, int y) {
        String piece = board[x][y].name().substring(0,5);
        if (piece.equals("BLACK")) {this.board[x][y] = Pieces.BLACK_QUEEN;}
        if (piece.equals("WHITE")) {this.board[x][y] = Pieces.WHITE_QUEEN;}
    }
    
    public void leftClick(int x, int y) {
        if (isPieceSelected) {
            for (int i = 0; i < possibleMoves.length; i++) {
                int z = possibleMoves[i];
                int x_coordinate = z / 10;
                int y_coordinate = z % 10;
                if (x == x_coordinate && y == y_coordinate) {
                    movePiece(pieceSelectedCoordinate[0], pieceSelectedCoordinate[1], x, y); 
                    //if (selectedPiece == Pieces.WHITE_PAWN || selectedPiece == Pieces.BLACK_PAWN && y == 0 || y == 7) // when pawn reaches the other side
                        //{replacePawnWithQueen(x, y);}                                                                 // of the board change to queen
                    this.isPieceSelected = false; 
                    return;
                }
            }
        }
        if (getBoard(x, y) == Pieces.EMPTY) {
            this.isPieceSelected = false; 
            viewer.displayBoard(); 
            return;
        }
        this.selectedPiece = getBoard(x, y);
        this.isPieceSelected = true;
        this.pieceSelectedCoordinate[0] = x; 
        this.pieceSelectedCoordinate[1] = y;
        String piece = selectedPiece.name().substring(6); // turns the enum into a string so that the piece's class can be called
        if (piece.equals("KING"))   {}                    // cuts it off at index 6 to get rid of BLACK_ or WHITE_ 
        if (piece.equals("QUEEN"))  {}
        if (piece.equals("BISHOP")) {}
        if (piece.equals("KNIGHT")) {}
        if (piece.equals("CASTLE")) {castle(x, y);}
        if (piece.equals("PAWN"))   {pawn(x, y);}            
    }
    
    public void pawn(int x, int y)
    {
        String possibleMoves = "";
        // if the pawn is white
        if (board[x][y] == Pieces.valueOf("WHITE_PAWN")) {
            if (isCoordEmpty(x, y - 1)) {possibleMoves +=  x      + "" + (y - 1) + " ";}
            if (isCoordEmpty(x, y - 1) && y == 6 &&
                isCoordEmpty(x, y - 2)) {possibleMoves +=  x      + "" + (y - 2) + " ";} 
            if (isLegal (x - 1, y - 1) && pieceColour(x - 1, y - 1).equals("BLACK"))  
                                        {possibleMoves += (x - 1) + "" + (y - 1) + " ";}
            if (isLegal (x + 1, y - 1) && pieceColour(x + 1, y - 1).equals("BLACK"))  
                                        {possibleMoves += (x + 1) + "" + (y - 1) + " ";}
        }
        // if the pawn is black
        if (board[x][y] == Pieces.valueOf("BLACK_PAWN")) {
            if (getBoard(x, y + 1) == Pieces.EMPTY)                            // down 
                {possibleMoves +=  x      + "" + (y + 1) + " ";}
            if (getBoard(x, y + 1) == Pieces.EMPTY && y == 1 &&                // down + 1 (start)
                getBoard(x, y + 2) == Pieces.EMPTY) 
                {possibleMoves +=  x      + "" + (y + 2) + " ";}                        
            if (isLegal (x - 1, y + 1) &&                                      // down left  
                getBoard(x - 1, y + 1).name().substring(0,5).equals("WHITE"))           
                {possibleMoves += (x - 1) + "" + (y + 1) + " ";}
            if (isLegal (x + 1, y + 1) &&                                      // down right  
                getBoard(x + 1, y + 1).name().substring(0,5).equals("WHITE"))         
                {possibleMoves += (x + 1) + "" + (y + 1) + " ";}
        }
        viewer.drawPossibleMoves(possibleMoves);
    }
    
    public void castle (int x, int y) {
        String pieceColour = getBoard(x, y).name().substring(0,5);
        for (int[] ds : new int [][] {{0,1},{0,-1},{1,0},{-1,0}})
        {
            int x1 = x + ds[0];
            int y1 = y + ds[1];
            while (isLegal(x1, y1))
            {
                //if (!board[x1][y1].name().substring(0,5).equals(pieceColour)) {possibleMoves += x1 + "" + y1 + " ";}
            }
        }
        
        
        
        String possibleMoves = "";
        if (board[x][y] == Pieces.valueOf("BLACK_CASTLE")) {
        for(int z = y; getBoard(x,z).name().substring(0,5) != "BLACK"; z++) {
            {possibleMoves += x;       possibleMoves += (y + 1) + " ";}
        }
        }
        viewer.drawPossibleMoves(possibleMoves);
    }
    }