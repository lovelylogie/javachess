import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ChessViewer implements MouseListener, KeyListener
{
    private static Chess board;                   // the internal representation of the chess board
    private static SimpleCanvas sc;               // the display window
    private static int cell = 100, size = 8;

    // colours
    private static Color cream     = new Color(208,137,71);
    private static Color brown     = new Color(254,206,158);
    private static Color legal_c   = new Color(199,145,95); // colours for circles drawn on legal
    private static Color legal_dg  = new Color(144,92,40);  // move spaces
    private static Color purple    = new Color(148,0,211);
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

    public ChessViewer(Chess board) {
        this.board = board;
        sc = new SimpleCanvas("Chess", 800, 800, Color.white);
        sc.setFont(new Font("Times", 1, 90));
        sc.addMouseListener(this);
        sc.addKeyListener(this);
        displayBoard();
    }

    public static void displayBoard() {
        drawGrid();
        drawMoveMade();
        //drawAttackingArray();
        drawPieces();
        drawNotation();
    }

    public static void drawAttackingArray() {
        for (int x = 0; x < size; x++)
            for (int y = 0; y < size; y++) {
                if (board.getAttackArray(x, y) == pieceColour.BOTH) {
                    drawTile(x, y, purple);
                    continue;
                }
                if (board.getAttackArray(x, y) == pieceColour.BLACK) {
                    drawTile(x, y, Color.blue);
                    continue;
                }
                if (board.getAttackArray(x, y) == pieceColour.WHITE) {
                    drawTile(x, y, Color.red);
                }
            }
    }

    private static void drawGrid() {
        for (int x = 0; x < size; x++)
            for (int y = 0; y < size; y++) {
                if (board.getTileColour(x, y) == TileColour.WHITE) {drawTile(x, y, cream);}
                if (board.getTileColour(x, y) == TileColour.BLACK) {drawTile(x, y, brown);}
            }
        int cS = board.getCheckSquare();
        if (cS != -1) sc.drawDisc(cell * (cS / 10) + cell / 2, cell * (cS % 10) + cell / 2, 50, Color.red);
    }

    public static void drawPieces() {
        for (int x = 0; x < size; x++)
            for (int y = 0; y < size; y++) {
                if (board.isEmpty(x, y)) continue;
                String piece = board.getPieceName(x, y).name().toLowerCase();
                Color colour = (board.pieceColour(x, y) == pieceColour.WHITE) ? white : black;
                drawPiece(pieceString.get(piece), x, y, colour);
            }
    }

    public static void drawNotation() {
        sc.setFont(new Font("Times", 1, 24));
        if (board.getOrientation()) {
            int number = 8;
            for (int i = 0; i < size; i++) {
                Color colour = (board.getTileColour(0, i) == TileColour.WHITE) ? legal_dg : legal_c;
                sc.drawCenteredString(number + "", 10, 15 + i * cell, colour);
                number--;
            }
            number = 97;
            for (int i = 0; i < size; i++) {
                Color colour = (board.getTileColour(i, 7) == TileColour.WHITE) ? legal_dg : legal_c;
                sc.drawCenteredString((char) number + "", 88 + i * cell, 788, colour);
                number++;
            }
        }
        else {
            int number = 1;
            for (int i = 0; i < size; i++) {
                Color colour = (board.getTileColour(0, i) == TileColour.WHITE) ? legal_dg : legal_c;
                sc.drawCenteredString(number + "", 10, 15 + i * cell, colour);
                number++;
            }
            number = 104;
            for (int i = 0; i < size; i++) {
                Color colour = (board.getTileColour(i, 7) == TileColour.WHITE) ? legal_dg : legal_c;
                sc.drawCenteredString((char) number + "", 88 + i * cell, 788, colour);
                number--;
            }
        }
        sc.setFont(new Font("Times", 1, 90));
    }

    public static void drawPossibleMoves() {
        List<Integer> moves = board.getPossibleMoves();
        for (int i = 0; i < moves.size(); i++) {
            int x = moves.get(i) / 10;
            int y = moves.get(i) % 10;
            if (board.isEmpty(x, y)) drawCircle(x, y);
            else                     drawTakingCircle(x, y);
        }
        drawPieces();
    }

    public static void drawCheckmate() {
        sc.drawRectangle(290,360, 510,440, Color.white);
        sc.setFont(new Font("Times", 1, 20));
        sc.drawCenteredString(board.getCurrentTurn().toLowerCase() + " is victorious", 400, 385, black);
        sc.drawCenteredString("by checkmate", 400, 415, black);
        sc.setFont(new Font("Times", 1, 90));
    }

    public static void drawStalemate() {
        sc.drawRectangle(290,360, 510,440, Color.white);
        sc.setFont(new Font("Times", 1, 20));
        sc.drawCenteredString("draw", 400, 385, black);
        sc.drawCenteredString("by stalemate", 400, 415, black);
        sc.setFont(new Font("Times", 1, 90));
    }

    public static void drawPromotion(int x, int y, Color colour) {
        sc.drawRectangle(x * cell, y * cell, x * cell + cell, y * cell + cell * 4, Color.gray);
        drawPiece(pieceString.get("queen"),  x, y,               colour);
        drawPiece(pieceString.get("knight"), x, y + 1,     colour);
        drawPiece(pieceString.get("rook"),   x, y + 2, colour);
        drawPiece(pieceString.get("bishop"), x, y + 3, colour);
    }

    public static void drawMoveMade() {
        if (board.movesMade() == 0) return;
        Color colour1 = (board.getTileColour(board.pieceMovedX1(), board.pieceMovedY1()) == TileColour.WHITE) ? legal_dg : legal_c;
        Color colour2 = (board.getTileColour(board.pieceMovedX2(), board.pieceMovedY2()) == TileColour.WHITE) ? legal_dg : legal_c;
        drawTile(board.pieceMovedX1(), board.pieceMovedY1(), colour1);
        drawTile(board.pieceMovedX2(), board.pieceMovedY2(), colour2);
    }

    public static void drawTile(int x, int y, Color colour) {
        sc.drawRectangle(cell * x, cell * y, cell * x + cell, cell * y + cell, colour);
    }

    public static void drawCircle(int x, int y) {
        Color colour = (board.getTileColour(x, y) == TileColour.WHITE) ? legal_dg : legal_c;
        sc.drawDisc(cell * x + cell / 2, cell * y + cell / 2, 17, colour);
    }

    public static void drawTakingCircle(int x, int y) {
        Color colour1 = null;
        Color colour2 = null;
        if (board.getTileColour(x, y) == TileColour.WHITE) {colour1 = legal_dg; colour2 = cream;}
        if (board.getTileColour(x, y) == TileColour.BLACK) {colour1 = legal_c;  colour2 = brown;}
        drawTile(x, y, colour2);
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

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'r') {
            board.flip();
            displayBoard();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}