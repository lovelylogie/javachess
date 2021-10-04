
public class Chess
{
    private ChessViewer viewer;
    
    private final int size = 8;
    private boolean isPieceSelected;
    private int pieceSelectedX;
    private int pieceSelectedY;
    private int pieceMovedX;
    private int pieceMovedY;
    private int[] possibleMoves;
    private Piece selectedPiece;
    private Piece[][] board;
    private TileColour[][] tileColour;
    private pieceColour currentTurn;
    
    private pieceColour white = pieceColour.WHITE;
    private pieceColour black = pieceColour.BLACK;
    
    private pieceName king = pieceName.KING;
    private pieceName rook = pieceName.ROOK;
    private pieceName bishop = pieceName.BISHOP;
    private pieceName queen = pieceName.QUEEN;
    private pieceName knight = pieceName.KNIGHT;
    private pieceName pawn = pieceName.PAWN;
    
    public Chess()
    {
        board       = new Piece     [size][size];
        tileColour  = new TileColour[size][size];
        selectedPiece = null;
        isPieceSelected = false;
        currentTurn = white;
        // kings        
        board[4][0] = new Piece(king, black);
        board[4][7] = new Piece(king, white);     
        // queens        
        board[3][0] = new Piece(queen, black);
        board[3][7] = new Piece(queen, white);       
        // bishops        
        board[2][0] = new Piece(bishop, black);
        board[5][0] = new Piece(bishop, black);
        board[2][7] = new Piece(bishop, white);
        board[5][7] = new Piece(bishop, white);        
        // knights        
        board[1][0] = new Piece(knight, black);
        board[6][0] = new Piece(knight, black);
        board[1][7] = new Piece(knight, white);
        board[6][7] = new Piece(knight, white);      
        // rooks        
        board[0][0] = new Piece(rook, black);
        board[7][0] = new Piece(rook, black);
        board[0][7] = new Piece(rook, white);
        board[7][7] = new Piece(rook, white);       
        // pawns        
        for (int i = 0; i < size; i++) board[i][1] = new Piece(pawn, black);
        for (int i = 0; i < size; i++) board[i][6] = new Piece(pawn, white);
        // tile colour
        for (int x = 0; x < size; x++)
            for (int y = 0; y < size; y++) {
                if (x % 2 == 0 && y % 2 == 0 || 
                    x % 2 == 1 && y % 2 == 1) tileColour[x][y] = TileColour.WHITE;
                else                          tileColour[x][y] = TileColour.BLACK;
            }
    }
    
    public boolean isLegal(int k) {
        return k >= 0 && k < size;
    }
    
    public boolean isLegal(int x, int y) {
        return isLegal(x) && isLegal(y);
    }
    
    public boolean isPieceSelected() {
        return isPieceSelected;
    }
    
    // new methods I am working on for the rook (isBlockedX & isBlockedY)
    public boolean isBlockedX(int x, int y) {
        boolean b1 = false;
        int z = 0;
        if (x < pieceSelectedX) z = 1;
        else                    z = 2;
        if (x == 1) for (int i = x + 1; i < pieceSelectedX; i++) {
                        if (!isEmpty(i, y)) b1 = true; break;
                    }
        if (x == 2) for (int i = pieceSelectedX + 1; i < x; i++) {
                        if (!isEmpty(i, y)) b1 = true; break;
                    }
        return b1;
    }
    
    public boolean isBlockedY(int x, int y) {       
        boolean b1 = false;
        int z = 0;
        if (x < pieceSelectedX) z = 1;
        else                    z = 2;
        if (x == 1) for (int i = y + 1; i < pieceSelectedY; i++) {
                        if (!isEmpty(i, y)) b1 = true; break;
                    }
        if (x == 2) for (int i = pieceSelectedY + 1; i < y; i++) {
                        if (!isEmpty(i, y)) b1 = true; break;
                    }
        return b1;
    }
    
    public int pieceSelectedX() {
        return pieceSelectedX;
    }
    
    public int pieceSelectedY() {
        return pieceSelectedY;
    }
    
    public int pieceMovedX() {
        return pieceMovedX;
    }
    
    public int pieceMovedY() {
        return pieceMovedY;
    }
    
    public int[] possibleMoves() {
        return possibleMoves;
    }
    
    public TileColour tileColour(int x, int y) {
        return tileColour[x][y];
    }
    
    public Piece getBoard(int x, int y) {
        return board[x][y];
    }
    
    public TileColour getTileColour(int x, int y) {
        return tileColour[x][y];
    }
    
    public pieceColour pieceColour(int x, int y) {
        return board[x][y].getColour();
    }
    
    public pieceName getPieceName(int x, int y) {
        return board[x][y].getName();
    }    

    public Piece getPiece(int x, int y) {
        return board[x][y];
    }
    
    public boolean isEmpty(int x, int y) {
        return board[x][y] == null;
    }
    
    public boolean isWhite(int x, int y) {
        return board[x][y].getColour() == white;
    }
    
    public boolean canTakePiece(int x, int y) {
        if (isEmpty(x, y)) return false;
        else               return !(pieceColour(pieceSelectedX, pieceSelectedY) == (pieceColour(x, y)));
    }
    
    public boolean isSameColour(int x, int y) {
        if (isEmpty(x, y)) return false;
        else               return pieceColour(pieceSelectedX, pieceSelectedY) == (pieceColour(x, y));
    }
    
    public void changeTurn() {
        if (currentTurn == white) this.currentTurn = black;
        else                      this.currentTurn = white;
    }
    
    public void movePiece(int x1, int y1, int x2, int y2) {
        board[x1][y1] = null;
        board[x2][y2] = selectedPiece;
        changeTurn();
        this.pieceMovedX = x2;
        this.pieceMovedY = y2;
        viewer.displayBoard();
        viewer.drawMoveMade();
        viewer.drawPieces();
    }
    
    public void leftClick(int x, int y) {
        if (isPieceSelected) {
            if (x != pieceSelectedX || y != pieceSelectedY)
                {viewer.displayBoard();}
            for (int i = 0; i < possibleMoves.length; i++) {
                int x_coordinate = possibleMoves[i] / 10; 
                int y_coordinate = possibleMoves[i] % 10;
                if (x == x_coordinate && y == y_coordinate) {
                    movePiece(pieceSelectedX, pieceSelectedY, x, y); 
                    this.isPieceSelected = false; 
                    return;
                }
            }
        }
        if (!(pieceColour(x, y) == (currentTurn)) && !isEmpty(x, y) || isEmpty(x, y)) {
            this.isPieceSelected = false;
            viewer.displayBoard(); 
            return;
        }
        this.selectedPiece = getBoard(x, y);
        this.isPieceSelected = true;
        this.pieceSelectedX = x; 
        this.pieceSelectedY = y;
        String piece = getPieceName(x, y).name().toLowerCase();
        if (piece.equals("king"))   {king(x, y);}
        if (piece.equals("queen"))  {queen(x, y);}
        if (piece.equals("bishop")) {bishop(x, y);}
        if (piece.equals("knight")) {knight(x, y);}
        if (piece.equals("rook"))   {castle(x, y);}
        if (piece.equals("pawn"))   {pawn(x, y);}   
        viewer.drawPossibleMoves(possibleMoves);
    }
    
    private void possibleMoves(String moves) {
        if (moves.equals("")) {return;}
        String coordinates_str[] = moves.split(" ");
        int arraySize = coordinates_str.length;
        int [] coordinates_int = new int [arraySize];
        for (int i = 0; i < arraySize; i++) 
            {coordinates_int[i] = Integer.parseInt(coordinates_str[i]);}
        this.possibleMoves = coordinates_int;
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
            
        //for (int i = x; i < size; i++) {
        //    if (!isBlockedX(i, y)) moves += i + "" + y + " ";
        //}
        
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
    
    public void bishop(int x, int y) {
        String moves = "";
        for (int[] ds : new int[][] {{-1,-1},{-1, 1},{1, 1},{1,-1}}) {
            int x1 = x + ds[0];
            int y1 = y + ds[1];
            while (isLegal(x1, y1)) {
                if (canTakePiece(x1, y1) || isEmpty(x1, y1))      moves += x1 + "" + y1 + " ";
                if (canTakePiece(x1, y1) || isSameColour(x1, y1)) break;
                x1 += ds[0];
                y1 += ds[1];
            }
        }
        possibleMoves(moves);
    }
    
    public void queen(int x, int y) {
        String moves = "";
        for (int[] ds : new int[][] {{-1,-1},{-1, 1},{1, 1},{1,-1},{0,-1},{0,1},{-1,0},{1,0}}) {
            int x1 = x + ds[0];
            int y1 = y + ds[1];
            while (isLegal(x1, y1)) {
                if (canTakePiece(x1, y1) || isEmpty(x1, y1))      moves += x1 + "" + y1 + " ";
                if (canTakePiece(x1, y1) || isSameColour(x1, y1)) break;
                x1 += ds[0];
                y1 += ds[1];
            }
        }
        possibleMoves(moves);
    }
    
    public void king(int x, int y) {        
        String moves = "";
        pieceColour colour = pieceColour(x, y);
        for (int[] ds : new int[][] {{-1,-1},{-1, 1},{1, 1},{1,-1},{0,-1},{0, 1},{-1, 0},{1, 0}}) {
            int x1 = x + ds[0];
            int y1 = y + ds[1];
            if (isLegal(x1, y1)) {              
                if (canTakePiece(x1, y1) || isEmpty(x1, y1))          moves += x1 + "" + y1 + " ";
            }
        }
        possibleMoves(moves);
    }
}
