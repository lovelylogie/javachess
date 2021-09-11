import java.util.*;
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
    private static Color g         = Color.gray;
    
    // piece unicodes
    private static String king   = "\u265A"; 
    private static String queen  = "\u265B"; 
    private static String castle = "\u265C"; 
    private static String bishop = "\u265D"; 
    private static String knight = "\u265E"; 
    private static String pawn   = "\u265F";
    
    // scoreboard vairables
    private static int NumberPiecesWhite = 1;
    private int NumberPiecesBlack = 1;
    
    public ChessViewer() {
        sc = new SimpleCanvas("Chess", 800, 950, g);
        sc.setFont(new Font("Times", 1, 90));
        sc.addMouseListener(this);
        displayBoard();
    }
    
    public static void displayBoard() {
        drawGrid();
        drawPieces();
        drawScoreboard();
    }
    
    private static void drawGrid() {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                if (board.getTileColour(i, j) == TileColour.WHITE) {drawTile(i, j, cream);}
                if (board.getTileColour(i, j) == TileColour.BLACK) {drawTile(i, j, brown);}
            }
    }
        
    public static void drawPieces() {
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
    
    private static void drawScoreboard() {
        sc.setFont(new Font("Times", 1, 20));
        sc.drawString("White",  50, 840, w);
        sc.drawString("Black", 520,840, b);
        for (int i = 0; i < 16; i++) {  
            Pieces black = board.getScoreBoardWhite(i);
                if (black == Pieces.BLACK_KING)   {drawScorePieceWhite(king,   i); continue;}
                if (black == Pieces.BLACK_QUEEN)  {drawScorePieceWhite(queen,  i); continue;}
                if (black == Pieces.BLACK_BISHOP) {drawScorePieceWhite(bishop, i); continue;}
                if (black == Pieces.BLACK_KNIGHT) {drawScorePieceWhite(knight, i); continue;}
                if (black == Pieces.BLACK_CASTLE) {drawScorePieceWhite(castle, i); continue;}
                if (black == Pieces.BLACK_PAWN)   {drawScorePieceWhite(pawn,   i); continue;}
    }
    for (int i = 0; i < 16; i++) {  
            Pieces white = board.getScoreBoardBlack(i);
                if (white == Pieces.WHITE_KING)   {drawScorePieceBlack(king,   i); continue;}
                if (white == Pieces.WHITE_QUEEN)  {drawScorePieceBlack(queen,  i); continue;}
                if (white == Pieces.WHITE_BISHOP) {drawScorePieceBlack(bishop, i); continue;}
                if (white == Pieces.WHITE_KNIGHT) {drawScorePieceBlack(knight, i); continue;}
                if (white == Pieces.WHITE_CASTLE) {drawScorePieceBlack(castle, i); continue;}
                if (white == Pieces.WHITE_PAWN)   {drawScorePieceBlack(pawn,   i); continue;}
    }
        sc.setFont(new Font("Times", 1, 90));
    }
    public static void drawPossibleMoves(int[] possibleMoves) {
        for (int i = 0; i < board.possibleMoves.length; i++) {
            int x = board.possibleMoves[i] / 10; 
            int y = board.possibleMoves[i] % 10;
            if (board.isEmpty(x, y)) {drawCircle(x, y);}
            else                     {drawTakingCircle(x, y);}
        }
        drawPieces();
    }
    
    public static void drawMoveMade() {
        Color colour1 = null;
        Color colour2 = null;
        if (board.getTileColour(board.pieceSelectedCoordinate[0], board.pieceSelectedCoordinate[1]) == TileColour.WHITE) 
             {colour1 = legal_dg;}
        else {colour1 = legal_c; }
        if (board.getTileColour(board.pieceMovedCoordinate[0],    board.pieceMovedCoordinate[1])    == TileColour.WHITE) 
             {colour2 = legal_dg;}
        else {colour2 = legal_c; }
        drawTile(board.pieceSelectedCoordinate[0], board.pieceSelectedCoordinate[1], colour1);
        drawTile(board.pieceMovedCoordinate[0],    board.pieceMovedCoordinate[1],    colour2);
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
        drawCenteredString(piece, cell * x + cell / 2, cell * y + cell / 2 + 7, colour);
    }
    
    private static void drawScorePieceWhite(String piece, int x) {
        sc.drawString(piece, 15 * x+ 15 / 2 + 30, 880 , w);
    }
    private static void drawScorePieceBlack(String piece, int x) {
        sc.drawString(piece, 15 * x+ 15 / 2 + 500, 880 , b);
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