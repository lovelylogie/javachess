/**
 * Displays the chess board through SimpleCanvas
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.awt.*;
import java.awt.event.*; 
import java.awt.geom.Rectangle2D;
import java.awt.font.FontRenderContext;

public class ChessViewer implements MouseListener
{
    private Chess board = new Chess();     // the internal representation of the chess board
    private SimpleCanvas sc;               // the display window
    private final int cell = 100, size = 8;
    
    // colours
    private Color cream     = new Color(208,137,71,255);
    private Color brown     = new Color(254,206,158,255);
    private Color legal_c   = new Color(214,214,189,255);
    private Color legal_dg  = new Color(106, 135, 77, 255);
    private Color w         = Color.white;
    private Color b         = Color.black;
    
    // piece unicodes
    private String king   = "\u265A"; 
    private String queen  = "\u265B"; 
    private String castle = "\u265C"; 
    private String bishop = "\u265D"; 
    private String knight = "\u265E"; 
    private String pawn   = "\u265F";
    
    /**
     * Constructor for objects of class ChessViewer
     */
    public ChessViewer() {
        sc = new SimpleCanvas("Chess", 800, 800, Color.white);
        sc.setFont(new Font("Times", 1, 70));
        sc.addMouseListener(this);
        displayBoard();
    }
    
    private void displayBoard() {
        drawGrid();
        drawPieces();
    }
    
    private void drawGrid() {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
            {
                if (j % 2 == 0 && i % 2 == 0 || 
                    j % 2 == 1 && i % 2 == 1)   drawTile(i, j, cream);               
                else                            drawTile(i, j, brown);
            }
    }
        
    private void drawPieces() {
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
    
    public void drawPossibleMove(String possibleMoves) {
        String coordinates_str[] = possibleMoves.split(" ");
        // now have to turn array of strings into array of ints
        int size1 = coordinates_str.length; // called it size1 so im not accidentally refering to the instance variable size
        int [] coordinates_int = new int [size1];
        for (int i = 0; i < size1; i++) 
            coordinates_int[i] = Integer.parseInt(coordinates_str[i]);
        for (int i = 0; i < size1; i++) {
            int z = 0;
            z = coordinates_int[i];
            drawCircle(z / 10, z % 10);
        }
    }
    
    private void drawTile(int x, int y, Color colouw) {
        sc.drawRectangle(cell * x, cell * y, cell * x + cell, cell * y + cell, colouw); 
    }
    
    public void drawCircle(int x, int y) {
        Color colour;
        if (x % 2 == 0 && y % 2 == 0 || 
            x % 2 == 1 && y % 2 == 1)   colour = legal_c;               
        else                            colour = legal_dg;
        sc.drawDisc(cell * x + cell / 2, cell * y + cell / 2, 17, colour);
    }

    private void drawPiece(String piece, int x, int y, Color colouw) {
        drawCenteredString(piece, cell * x + cell / 2, cell * y + cell / 2, colouw);
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
    public void drawCenteredString(String text, int x, int y, Color Colouw) {
        FontRenderContext frc = new FontRenderContext(null,true,true);
        Rectangle2D size = sc.getFont().getStringBounds(text, frc);
        int fontX = x - (int)size.getWidth() / 2;
        int fontY = y + (int)size.getHeight() / 4;
        sc.drawString(text,fontX,fontY,Colouw);
    }
    }