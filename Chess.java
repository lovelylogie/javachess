
public class Chess
{
    private ChessViewer viewer;
    
    private final int size = 8;
    public boolean isPieceSelected;
    private int[] pieceSelectedCoordinate = new int[2];
    public int[] possibleMoves;
    private Pieces selectedPiece;
    
    private King   king;
    private Queen  queen;
    private Bishop bishop;
    private Knight knight;
    private Castle castle;
    private Pawn   pawn;
    
    private Pieces[][] board;

    public Chess()
    {
        board = new Pieces[size][size];
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
        
        for (int i = 0; i < size; i++) 
            board[i][1] = Pieces.BLACK_PAWN;
        for (int i = 0; i < size; i++)
            board[i][6] = Pieces.WHITE_PAWN;
            
        // empty spaces
        
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                if (board[i][j] == null) board[i][j] = Pieces.EMPTY;
            }
    }
    
    public boolean isLegal(int k) {
        return k >= 0 && k < size;
    }
    
    public boolean isLegal(int x, int y) {
        return isLegal(x) && isLegal(y);
    }       
    
    public void movePiece(int x1, int y1, int x2, int y2) {
            board[x1][y1] = Pieces.EMPTY;
            board[x2][y2] = selectedPiece;
            this.isPieceSelected = false;
            viewer.displayBoard();
    }
    
    public void leftClick(int x, int y) {
        if (isPieceSelected) {
            for (int i = 0; i < possibleMoves.length; i++) {
                int z = possibleMoves[i];
                int x_coordinate = z / 10;
                int y_coordinate = z % 10;
                if (x == x_coordinate && y == y_coordinate) 
                    {movePiece(pieceSelectedCoordinate[0], pieceSelectedCoordinate[1], x, y);}
            }
            if (getBoard(x, y) == Pieces.EMPTY) {this.isPieceSelected = false; viewer.displayBoard(); return;}
        }
        else if (getBoard(x, y) != Pieces.EMPTY) {
            this.selectedPiece = getBoard(x, y);
            this.isPieceSelected = true;
            this.pieceSelectedCoordinate[0] = x; 
            this.pieceSelectedCoordinate[1] = y;
            String piece = selectedPiece.name().substring(6); // turns the enum into a string so that the piece's class can be called
            if (piece.equals("KING"))   {}                    // cuts it off at index 6 to get rid of BLACK_ or WHITE_ 
            if (piece.equals("QUEEN"))  {}
            if (piece.equals("BISHOP")) {}
            if (piece.equals("KNIGHT")) {}
            if (piece.equals("CASTLE")) {}
            if (piece.equals("PAWN"))   {pawn = new Pawn(); pawn.possibleMoves(x, y);} 
        }
        else return;           
    }
    
    public Pieces getBoard(int x, int y) {
        return this.board[x][y];
    }
}
