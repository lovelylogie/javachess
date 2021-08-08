
import java.awt.*;
import java.awt.event.*; 
import java.awt.geom.Rectangle2D;
import java.awt.font.FontRenderContext;

public class ChessViewer implements MouseListener
{
    private static Chess board = new Chess();     // the internal representation of the chess board
    private static SimpleCanvas sc;               // the display window
    private static int cell = 100, size = 8;
    
    // colours
    private static Color cream     = new Color(208,137,71);
    private static Color brown     = new Color(254,206,158);
    private static Color legal_c   = new Color(199,145,95); // colours for circles drawn on legal
    private static Color legal_dg  = new Color(144,92,40);  // move spaces
    private static Color w         = Color.white;
    private static Color b         = Color.black;
    
    // piece unicodes
    private static String king   = "\u265A"; 
    private static String queen  = "\u265B"; 
    private static String castle = "\u265C"; 
    private static String bishop = "\u265D"; 
    private static String knight = "\u265E"; 
    private static String pawn   = "\u265F";
    
    public ChessViewer() {
        sc = new SimpleCanvas("Chess", 800, 800, Color.white);
        sc.setFont(new Font("Times", 1, 70));
        sc.addMouseListener(this);
        displayBoard();
    }
    
    public static void displayBoard() {
        drawGrid();
        drawPieces();
    }
    
    private static void drawGrid() {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
            {
                if (j % 2 == 0 && i % 2 == 0 || 
                    j % 2 == 1 && i % 2 == 1)   drawTile(i, j, cream);               
                else                            drawTile(i, j, brown);
            }
    }
        
    private static void drawPieces() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // black pieces
                Pieces black = board.getBoard(i, j);
                if (black == Pieces.EMPTY)        {continue;}
                if (black == Pieces.BLACK_KING)   {drawPiece(king,   i, j, b); continue;}
                if (black == Pieces.BLACK_QUEEN)  {drawPiece(queen,  i, j, b); continue;}
                if (black == Pieces.BLACK_BISHOP) {drawPiece(bishop, i, j, b); continue;}
                if (black == Pieces.BLACK_KNIGHT) {drawPiece(knight, i, j, b); continue;}
                if (black == Pieces.BLACK_CASTLE) {drawPiece(castle, i, j, b); continue;}
                if (black == Pieces.BLACK_PAWN)   {drawPiece(pawn,   i, j, b); continue;}
            }
            for (int j = 0; j < size; j++) {
                // white pieces
                Pieces white = board.getBoard(i, j);
                if (white == Pieces.EMPTY)        {continue;}
                if (white == Pieces.WHITE_KING)   {drawPiece(king,   i, j, w); continue;}
                if (white == Pieces.WHITE_QUEEN)  {drawPiece(queen,  i, j, w); continue;}
                if (white == Pieces.WHITE_BISHOP) {drawPiece(bishop, i, j, w); continue;}
                if (white == Pieces.WHITE_KNIGHT) {drawPiece(knight, i, j, w); continue;}
                if (white == Pieces.WHITE_CASTLE) {drawPiece(castle, i, j, w); continue;}
                if (white == Pieces.WHITE_PAWN)   {drawPiece(pawn,   i, j, w); continue;}
            }
        }
    }
    
    public static void drawPossibleMoves(String possibleMoves) {
        displayBoard();
        String coordinates_str[] = possibleMoves.split(" ");
        // now have to turn array of strings into array of ints
        int size1 = coordinates_str.length; // called it size1 so im not accidentally refering to the instance variable size
        int [] coordinates_int = new int [size1];
        for (int i = 0; i < size1; i++) 
            coordinates_int[i] = Integer.parseInt(coordinates_str[i]);
        board.possibleMoves = coordinates_int;
        for (int i = 0; i < size1; i++) {
            int z = 0;
            z = coordinates_int[i];
            drawCircle(z / 10, z % 10);
        }
    }
    
    private static void drawTile(int x, int y, Color colouw) {
        sc.drawRectangle(cell * x, cell * y, cell * x + cell, cell * y + cell, colouw); 
    }
    
    public static void drawCircle(int x, int y) {
        Color colour;
        if (x % 2 == 0 && y % 2 == 0 || 
            x % 2 == 1 && y % 2 == 1)   colour = legal_dg;               
        else                            colour = legal_c;
        sc.drawDisc(cell * x + cell / 2, cell * y + cell / 2, 17, colour);
    }

    private static  void drawPiece(String piece, int x, int y, Color colour) {
        drawCenteredString(piece, cell * x + cell / 2, cell * y + cell / 2, colour);
    }

    public void leftClick(int x, int y) {
        if (!board.isLegal(x, y)) return;
        board.leftClick(x, y);
    }
    
    public void mousePressed(MouseEvent e) {
        if (0 <= e.getX() && e.getX() < 800 &&
            0 <= e.getY() && e.getY() < 800)
            leftClick(e.getX() / cell, e.getY() / cell);
    }
    
    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    
     /**
     * Draws a string with the centre of this string
     * at the input coordinates
     */
    public static void drawCenteredString(String text, int x, int y, Color Colouw) {
        FontRenderContext frc = new FontRenderContext(null,true,true);
        Rectangle2D size = sc.getFont().getStringBounds(text, frc);
        int fontX = x - (int)size.getWidth() / 2;
        int fontY = y + (int)size.getHeight() / 4;
        sc.drawString(text,fontX,fontY,Colouw);
    }
    }