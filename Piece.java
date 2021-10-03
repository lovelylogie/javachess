public class Piece {    
    private pieceName name;
    private pieceColour colour;
    
    public Piece(pieceName name, pieceColour colour) {
        this.name = name;
        this.colour = colour;
    }
    
    public pieceName getName() {
        return name;
    }
    
    public pieceColour getColour() {
        return colour;
    }
}