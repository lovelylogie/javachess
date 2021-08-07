/**
 * Displays the chess board through SimpleCanvas
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.font.FontRenderContext;

public class ChessViewer
{
    private Chess board = new Chess();     // the internal representation of the chess board
    private SimpleCanvas sc;               // the display window
    private final int cell = 100, size = 8;
    
    // colours
    private Color cream     = new Color(238, 238, 210, 255);
    private Color darkGreen = new Color(118, 150, 86, 255);
    private Color b         = Color.black;
    private Color w         = Color.white;
    
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
        display();
    }
    
    private void display() {
        drawGrid();
        drawPieces();
    }
    
    private void drawGrid() {
        // **CAUTION** SEXY CODE BELOW. PROCEED WITH CAUTION.
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
            {
                if (j % 2 == 0 && i % 2 == 0 || 
                    j % 2 == 1 && i % 2 == 1)   drawTile(i, j, cream);               
                else                            drawTile(i, j, darkGreen);
            }
    }
        
    private void drawPieces() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // black pieces
                Pieces black = board.getBlack(i, j);
                if (black == Pieces.EMPTY)  {continue;}
                if (black == Pieces.KING)   {drawBlack(king,   i, j); continue;}
                if (black == Pieces.QUEEN)  {drawBlack(queen,  i, j); continue;}
                if (black == Pieces.BISHOP) {drawBlack(bishop, i, j); continue;}
                if (black == Pieces.KNIGHT) {drawBlack(knight, i, j); continue;}
                if (black == Pieces.CASTLE) {drawBlack(castle, i, j); continue;}
                if (black == Pieces.PAWN)   {drawBlack(pawn,   i, j); continue;}
                
            }
            for (int j = 0; j < size; j++) {
                // white pieces
                Pieces white = board.getWhite(i, j);
                if (white == Pieces.EMPTY)  {continue;}
                if (white == Pieces.KING)   {drawWhite(king,   i, j); continue;}
                if (white == Pieces.QUEEN)  {drawWhite(queen,  i, j); continue;}
                if (white == Pieces.BISHOP) {drawWhite(bishop, i, j); continue;}
                if (white == Pieces.KNIGHT) {drawWhite(knight, i, j); continue;}
                if (white == Pieces.CASTLE) {drawWhite(castle, i, j); continue;}
                if (white == Pieces.PAWN)   {drawWhite(pawn,   i, j); continue;}
            }
        }
    }
    
    private void drawTile(int i, int j, Color colour) {
        sc.drawRectangle(cell * i, cell * j, cell * i + cell, cell * j + cell, colour); 
    }

    private void drawBlack(String piece, int i, int j) {
        drawCenteredString(piece, cell * i + cell / 2, cell * j + cell / 2, b);
    }
    
    private void drawWhite(String piece, int i, int j) {
        drawCenteredString(piece, cell * i + cell / 2, cell * j + cell / 2, b);
        sc.setFont(new Font("Times", 1, 62));
        drawCenteredString(piece, cell * i + cell / 2, cell * j + cell / 2, w);
        sc.setFont(new Font("Times", 1, 70));
    }
    
    /**
     * Draws a string with the centre of this string
     * at the input coordinates
     */
    public void drawCenteredString(String text, int x, int y, Color Colour) {
        FontRenderContext frc = new FontRenderContext(null,true,true);
        Rectangle2D size = sc.getFont().getStringBounds(text, frc);
        int fontX = x - (int)size.getWidth() / 2;
        int fontY = y + (int)size.getHeight() / 4;
        sc.drawString(text,fontX,fontY,Colour);
    }
    }