
/**
 * The internal representation of the chess board
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Chess
{
    private final int size = 8;
    private Pieces[][] black;
    private Pieces[][] white;

    /**
     * Constructor for objects of class Chess
     */
    public Chess()
    {
        black = new Pieces[size][size];
        white = new Pieces[size][size];
        
        // kings
        
        black[4][0] = Pieces.KING;
        white[4][7] = Pieces.KING;
        
        // queens
        
        black[3][0] = Pieces.QUEEN;
        white[3][7] = Pieces.QUEEN;
        
        // bishops
        
        black[2][0] = Pieces.BISHOP;
        black[5][0] = Pieces.BISHOP;
        white[2][7] = Pieces.BISHOP;
        white[5][7] = Pieces.BISHOP;
        
        // knights
        
        black[1][0] = Pieces.KNIGHT;
        black[6][0] = Pieces.KNIGHT;
        white[1][7] = Pieces.KNIGHT;
        white[6][7] = Pieces.KNIGHT;
        
        // castles
        
        black[0][0] = Pieces.CASTLE;
        black[7][0] = Pieces.CASTLE;
        white[0][7] = Pieces.CASTLE;
        white[7][7] = Pieces.CASTLE;
        
        // pawns
        
        for (int i = 0; i < size; i++) 
            black[i][1] = Pieces.PAWN;
        for (int i = 0; i < size; i++)
            white[i][6] = Pieces.PAWN;
            
        // empty spaces
        
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                if (black[i][j] == null) black[i][j] = Pieces.EMPTY;
            }
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                if (white[i][j] == null) white[i][j] = Pieces.EMPTY;
            }
    }
    
    public boolean isLegal(int k) {
        return k >= 0 && k < size;
    }
    
    public boolean isLegal(int r, int c) {
        return isLegal(r) && isLegal(c);
    }
    
    public Pieces getBlack(int x, int y) {
        return this.black[x][y];
    }
    
    public Pieces getWhite(int x, int y) {
        return this.white[x][y];
    }
}
