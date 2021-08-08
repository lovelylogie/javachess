
/**
 * Write a description of class Pawn here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Pawn
{
    // instance variables - replace the example below with your own
    private ChessViewer screen;
    private Chess board = new Chess();

    /**
     * Constructor for objects of class Pawn
     */
    public Pawn()
    {

    }

    public void possibleMoves(int x, int y)
    {
        // find out what colour the piece selected is
        String piece = board.getBoard(x, y).name().substring(0,5);
        PieceColour colour = PieceColour.valueOf(piece);
        String possibleMoves = "";
        // since pawns can only move in a forward direction, this gets a bit tricky
        
        // if the pawn is white
        if (colour == PieceColour.valueOf("WHITE")) {
            if (board.getBoard(x, y - 1)     == Pieces.EMPTY ||       // up
            PieceColour.valueOf(board.getBoard(x, y - 1).name().substring(0,5))     != colour) 
                {possibleMoves += x;       possibleMoves += (y - 1); possibleMoves += " ";}
            if (board.getBoard(x - 1, y - 1) == Pieces.EMPTY ||       // up left
            PieceColour.valueOf(board.getBoard(x - 1, y - 1).name().substring(0,5)) != colour) 
                {possibleMoves += (x - 1); possibleMoves += (y - 1); possibleMoves += " ";}
            if (board.getBoard(x + 1, y - 1) == Pieces.EMPTY ||       // up right
            PieceColour.valueOf(board.getBoard(x + 1, y - 1).name().substring(0,5)) != colour) 
                {possibleMoves += (x + 1); possibleMoves += (y - 1); possibleMoves += " ";}
        }
        
        // if the pawn is black
        if (colour == PieceColour.valueOf("BLACK")) {
            if (board.getBoard(x, y + 1)     == Pieces.EMPTY ||       // down
            PieceColour.valueOf(board.getBoard(x, y - 1).name().substring(0,5))     != colour) 
                {possibleMoves += x;       possibleMoves += (y + 1); possibleMoves += " ";}
            if (board.getBoard(x - 1, y + 1) == Pieces.EMPTY ||       // down left
            PieceColour.valueOf(board.getBoard(x - 1, y - 1).name().substring(0,5)) != colour) 
                {possibleMoves += (x - 1); possibleMoves += (y + 1); possibleMoves += " ";}
            if (board.getBoard(x + 1, y + 1) == Pieces.EMPTY ||       // down right
            PieceColour.valueOf(board.getBoard(x + 1, y - 1).name().substring(0,5)) != colour) 
                {possibleMoves += (x + 1); possibleMoves += (y + 1); possibleMoves += " ";}
        }
        System.out.println(possibleMoves);
        //screen.drawPossibleMove(possibleMoves);
    }
}
