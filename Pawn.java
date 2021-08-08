
public class Pawn
{
    private ChessViewer screen;
    private Chess board = new Chess();

    public Pawn() {}

    public void possibleMoves(int x, int y)
    {
        // find out what colour the piece selected is
        String piece = board.getBoard(x, y).name().substring(0,5);
        PieceColour colour = PieceColour.valueOf(piece);
        String possibleMoves = "";
        // since pawns can only move in a forward direction, this gets a bit tricky
        
        // if the pawn is white
        if (colour == PieceColour.valueOf("WHITE")) {
            if (board.getBoard(x, y - 1) == Pieces.EMPTY)                                            // up 
                {possibleMoves += x;       possibleMoves += (y - 1) + " ";}
            if (board.getBoard(x, y - 2) == Pieces.EMPTY)                                            // up + 1 (start)
                {possibleMoves += x;       possibleMoves += (y - 2) + " ";}   
            if (board.getBoard(x - 1, y - 1).name().substring(0, 5).equals("EMPTY") == false && 
                PieceColour.valueOf(board.getBoard(x - 1, y - 1).name().substring(0, 5)) != colour)  // up left
                {possibleMoves += (x - 1); possibleMoves += (y - 1) + " ";}
            if (board.getBoard(x - 1, y - 1).name().substring(0, 5).equals("EMPTY") == false && 
                PieceColour.valueOf(board.getBoard(x + 1, y - 1).name().substring(0, 5)) != colour)  // up right
                {possibleMoves += (x + 1); possibleMoves += (y - 1) + " ";}
        }
        
        // if the pawn is black
        if (colour == PieceColour.valueOf("BLACK")) {
            if (board.getBoard(x, y + 1)     == Pieces.EMPTY)                                        // down 
                {possibleMoves += x;       possibleMoves += (y + 1) + " ";}
            if (board.getBoard(x, y + 2)     == Pieces.EMPTY)                                        // down + 1 (start)
                {possibleMoves += x;       possibleMoves += (y + 2) + " ";}    
            if (board.getBoard(x - 1, y + 1).name().substring(0, 5).equals("EMPTY") == false &&
                PieceColour.valueOf(board.getBoard(x - 1, y - 1).name().substring(0, 5)) != colour)  // down left           
                {possibleMoves += (x - 1); possibleMoves += (y + 1) + " ";}
            if (board.getBoard(x + 1, y + 1).name().substring(0, 5).equals("EMPTY") == false &&
                PieceColour.valueOf(board.getBoard(x + 1, y - 1).name().substring(0, 5)) != colour)  // down right         
                {possibleMoves += (x + 1); possibleMoves += (y + 1) + " ";}
        }
        //System.out.println(possibleMoves);
        screen.drawPossibleMove(possibleMoves);
    }
}
