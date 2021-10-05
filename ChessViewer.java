import java.util.*;
import java.awt.*;
import java.awt.event.*; 

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
    private static Color white     = Color.white;
    private static Color black     = Color.black;
    
    // piece unicodes
    private static Map<String, String> pieceString = Map.of(
    "king",   "\u265A",
    "queen",  "\u265B",
    "rook",   "\u265C",
    "bishop", "\u265D",
    "knight", "\u265E",
    "pawn",   "\u265F"
    );
    
    public ChessViewer() {
        sc = new SimpleCanvas("Chess", 800, 800, Color.white);
        sc.setFont(new Font("Times", 1, 90));
        sc.addMouseListener(this);
        displayBoard();
    }

    public static void displayBoard() {
        drawGrid();
        drawPieces();
    }
    
    private static void drawGrid() {
        for (int x = 0; x < size; x++)
            for (int y = 0; y < size; y++) {
                if (board.getTileColour(x, y) == TileColour.WHITE) {drawTile(x, y, cream);}
                if (board.getTileColour(x, y) == TileColour.BLACK) {drawTile(x, y, brown);}
            }
    }
        
    public static void drawPieces() {
        for (int x = 0; x < size; x++)
            for (int y = 0; y < size; y++) {
                if (board.isEmpty(x, y)) {continue;}
                Color colour = Color.white;
                String piece = board.getPieceName(x, y).name().toLowerCase();
                colour = (board.pieceColour(x, y) == pieceColour.WHITE) ? white : black;
                    // if (board.pieceColour(x, y) == pieceColour.WHITE) {colour = white;}
                    // else                                              {colour = black;}
                drawPiece(pieceString.get(piece), x, y, colour);
            }
    }
    
    public static void drawPossibleMoves(int[] possibleMoves) {
        for (int i = 0; i < board.possibleMoves().length; i++) {
            int x = board.possibleMoves()[i] / 10; 
            int y = board.possibleMoves()[i] % 10;
            if (board.isEmpty(x, y)) {drawCircle(x, y);}
            else                     {drawTakingCircle(x, y);}
        }
        drawPieces();
    }
    
    public static void drawMoveMade() {
        Color colour1 = null;
        Color colour2 = null;
        if (board.getTileColour(board.pieceSelectedX(), board.pieceSelectedY()) == TileColour.WHITE) 
             {colour1 = legal_dg;}
        else {colour1 = legal_c; }
        if (board.getTileColour(board.pieceMovedX(),    board.pieceMovedY())    == TileColour.WHITE) 
             {colour2 = legal_dg;}
        else {colour2 = legal_c; }
        drawTile(board.pieceSelectedX(), board.pieceSelectedY(), colour1);
        drawTile(board.pieceMovedX(),    board.pieceMovedY(),    colour2);
    }
    
    private static void drawTile(int x, int y, Color colour) {
        sc.drawRectangle(cell * x, cell * y, cell * x + cell, cell * y + cell, colour); 
    }
    
    public static void drawCircle(int x, int y) {
        Color colour = null;
        if (board.getTileColour(x, y) == TileColour.WHITE) {colour = legal_dg;}
        if (board.getTileColour(x, y) == TileColour.BLACK) {colour = legal_c ;}   
        sc.drawDisc(cell * x + cell / 2, cell * y + cell / 2, 17, colour);
    }
    
    public static void drawTakingCircle(int x, int y) {
        Color colour1 = null;
        Color colour2 = null;
        if (board.getTileColour(x, y) == TileColour.WHITE) {colour1 = legal_dg; colour2 = cream;}
        if (board.getTileColour(x, y) == TileColour.BLACK) {colour1 = legal_c;  colour2 = brown;}     
        sc.drawDisc(cell * x + cell / 2, cell * y + cell / 2, 50, colour1);
        sc.drawDisc(cell * x + cell / 2, cell * y + cell / 2, 43, colour2);
    }

    private static void drawPiece(String piece, int x, int y, Color colour) {
        sc.drawCenteredString(piece, cell * x + cell / 2, cell * y + cell / 2 + 7, colour);
    }

    public void leftClick(int x, int y) {
        if (board.isLegal(x, y)) {board.leftClick(x, y);}
    }
    
    public void mousePressed(MouseEvent e) {
        if (0 <= e.getX() && e.getX() < 800 &&
            0 <= e.getY() && e.getY() < 800)
            {leftClick(e.getX() / cell, e.getY() / cell);}
    }
    
    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    }