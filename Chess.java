import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Character.isLowerCase;

public class Chess
{
    private ChessViewer viewer;

    private final int size = 8;
    private boolean isPieceSelected;
    private boolean gameOver;
    private boolean orientation; // if true, white is bottom
    private boolean isPromotion;
    private int movesMade;
    private int pieceSelectedX;
    private int pieceSelectedY;
    private int pieceMovedX1;
    private int pieceMovedY1;
    private int pieceMovedX2;
    private int pieceMovedY2;
    private int checkSquare;
    private int whiteKingLoc;
    private int blackKingLoc;
    private int promotionSquare;
    private String afterSpaceString;
    private List<Integer> possibleMoves;
    private Piece selectedPiece;
    private Piece[][] board;
    private pieceColour[][] squaresAttacked;
    private TileColour[][] tileColour;
    private pieceColour currentTurn;

    private final pieceColour white = pieceColour.WHITE;
    private final pieceColour black = pieceColour.BLACK;

    private final pieceName king = pieceName.KING;
    private final pieceName rook = pieceName.ROOK;
    private final pieceName bishop = pieceName.BISHOP;
    private final pieceName queen = pieceName.QUEEN;
    private final pieceName knight = pieceName.KNIGHT;
    private final pieceName pawn = pieceName.PAWN;

    private final String defaultPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public Chess(String fen) {
        board           = new Piece      [size][size];
        tileColour      = new TileColour [size][size];
        squaresAttacked = new pieceColour[size][size];
        selectedPiece = null;
        isPieceSelected = false;
        isPromotion = false;
        currentTurn = white;
        checkSquare = -1;
        fenToBoard(fen);
        // tile colour
        for (int x = 0; x < size; x++)
            for (int y = 0; y < size; y++)
                tileColour[x][y] = (x % 2 == 0 && y % 2 == 0 || x % 2 == 1 && y % 2 == 1) ?
                        TileColour.BLACK : TileColour.WHITE;
        attackingArray();
        getKingLocations();
        isCheck();
        viewer = new ChessViewer(this);
    }

    public void fenToBoard(String fen) {
        Piece[] fenBoard = new Piece[64];
        Arrays.fill(fenBoard, new Piece(pieceName.EMPTY, pieceColour.EMPTY));
        int arrPosition = 0;
        if (fen.isEmpty()) fen = defaultPosition;
        for (int i = 0; i < fen.length(); i++) {
            char c = fen.charAt(i);
            if (Character.isDigit(fen.charAt(i))) {
                arrPosition += Character.getNumericValue(c);
                continue;
            }
            if (c == '/') continue;
            if (c == ' ') {
                afterSpaceString = fen.substring(i);
                break;
            }
            fenBoard[arrPosition] = charToPiece(c);
            arrPosition++;
        }
        // 1d to 2d array
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                board[i][j] = fenBoard[(i * size) + j];
        // rotate anticlockwise
        Piece[][] boardCopy = new Piece[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                boardCopy[size - 1 - j][i] = board[i][j];
        // flip horizontally
        for (int i = 0; i < size / 2; i++) {
            for (int j = 0; j < size; j++) {
                Piece temp = boardCopy[i][j];
                boardCopy[i][j] = boardCopy[boardCopy.length - 1 - i][j];
                boardCopy[boardCopy.length - 1 - i][j] = temp;
            }
        }
        board = boardCopy.clone();
        currentTurn = (afterSpaceString.charAt(1) == 'w') ? white : black;
        orientation = afterSpaceString.charAt(1) == 'w';
        if (!orientation) {
            flip();
            orientation = false;
        }
    }

    public Piece charToPiece(char c) {
        pieceColour colour = (isLowerCase(c)) ? black : white;
        pieceName name = null;
        c = Character.toLowerCase(c);
        if (c == 'k') name = king;
        if (c == 'q') name = queen;
        if (c == 'b') name = bishop;
        if (c == 'n') name = knight;
        if (c == 'r') name = rook;
        if (c == 'p') name = pawn;
        return new Piece(name, colour);
    }

    // sets up a 2d array that shows which squares are being attacked by which side (either black or white)
    public void attackingArray() {
        // clear the array first
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                squaresAttacked[x][y] = pieceColour.EMPTY;
            }
        }
        // then set it up
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) { // is there a way to make this cleaner?
                if (isEmpty(x, y)) {
                    continue;
                }
                if (getPiece(x, y).getName() == pawn) {
                    listToAttackArray(pawn(x, y, true), x, y);
                    continue;
                    }
                if (getPiece(x, y).getName() == rook) {
                    listToAttackArray(rook(x, y, true), x, y);
                    continue;
                    }
                if (getPiece(x, y).getName() == knight) {
                    listToAttackArray(knight(x, y, true), x, y);
                    continue;
                    }
                if (getPiece(x, y).getName() == bishop) {
                    listToAttackArray(bishop(x, y, true), x, y);
                    continue;
                    }
                if (getPiece(x, y).getName() == queen) {
                    listToAttackArray(queen(x, y, true), x, y);
                    continue;
                    }
                if (getPiece(x, y).getName() == king) {
                    listToAttackArray(king(x, y, true), x, y);
                    }
            }
        }
    }

    // converts list of integer into elements of spaceAttacked array
    // i know i know, using pieceColour.BOTH is gross. cbf changing it though haha
    public void listToAttackArray(List<Integer> moves, int x, int y) {
        if (moves.isEmpty()) return;
        pieceColour colour = (getPiece(x, y).getColour() == white) ? pieceColour.WHITE : pieceColour.BLACK;
        pieceColour temp = colour;
        for (int i = 0; i < moves.size(); i++) {
            int x1 = moves.get(i) / 10;
            int y1 = moves.get(i) % 10;
            if (squaresAttacked[x1][y1] == pieceColour.BOTH) continue;
            if (squaresAttacked[x1][y1] == pieceColour.WHITE && colour == pieceColour.BLACK ||
                squaresAttacked[x1][y1] == pieceColour.BLACK && colour == pieceColour.WHITE)
                colour = pieceColour.BOTH;
            squaresAttacked[x1][y1] = colour;
            colour = temp;
        }
    }

    public void getKingLocations() {
        boolean foundKing = false;
        search :
        {
            for (int x = 0; x < size; x++)
                for (int y = 0; y < size; y++) {
                    if (getPieceName(x, y) == king) {
                        if (getPiece(x, y).getColour() == white)
                             whiteKingLoc = x * 10 + y;
                        else blackKingLoc = x * 10 + y;
                        if (foundKing) break search;
                        foundKing = true;
                    }
                }
        }
    }

    public Piece emptyPiece() {
        return new Piece(pieceName.EMPTY, pieceColour.EMPTY);
    }

    public pieceColour getAttackArray(int x, int y) {
        return squaresAttacked[x][y];
    }

    public boolean getOrientation() {
        return orientation;
    }

    public String getCurrentTurn() {
        changeTurn();
        return currentTurn.name();
    }

    public pieceColour isAttacking(int x, int y) {
        return squaresAttacked[x][y];
    }

    public boolean isLegal(int k) {
        return k >= 0 && k < size;
    }

    public boolean isLegal(int x, int y) {
        return isLegal(x) && isLegal(y);
    }

    public boolean isPieceSelected() {
        return isPieceSelected;
    }

    public int movesMade() {
        return movesMade;
    }

    public void plusMove() {
        this.movesMade++;
    }

    public int pieceSelectedX() {
        return pieceSelectedX;
    }

    public int pieceSelectedY() {
        return pieceSelectedY;
    }

    public int pieceMovedX1() {
        return pieceMovedX1;
    }

    public int pieceMovedY1() {
        return pieceMovedY1;
    }

    public int pieceMovedX2() {
        return pieceMovedX2;
    }

    public int pieceMovedY2() {
        return pieceMovedY2;
    }

    public List<Integer> getPossibleMoves() {
        return possibleMoves;
    }

    public TileColour tileColour(int x, int y) {
        return tileColour[x][y];
    }

    public Piece getBoard(int x, int y) {
        return board[x][y];
    }

    public int getCheckSquare() {
        return checkSquare;
    }

    public TileColour getTileColour(int x, int y) {
        return tileColour[x][y];
    }

    public pieceColour pieceColour(int x, int y) {
        return board[x][y].getColour();
    }

    public pieceColour oppositeColour(pieceColour colour) {
        return (colour == pieceColour.WHITE) ? pieceColour.BLACK : pieceColour. WHITE;
    }

    public pieceColour oppositeColourGivenPiece(Piece piece) {
        return oppositeColour(piece.getColour());
    }

    public pieceName getPieceName(int x, int y) {
        return board[x][y].getName();
    }

    public Piece getPiece(int x, int y) {
        return board[x][y];
    }

    public boolean isEmpty(int x, int y) {
        if (board[x][y] == null) board[x][y] = new Piece(pieceName.EMPTY, pieceColour.EMPTY);
        return board[x][y].getName() == pieceName.EMPTY;
    }

    public boolean isWhite(int x, int y) {
        return board[x][y].getColour() == white;
    }

    public boolean canTakePiece(int x1, int y1, int x2, int y2) {
        if (!isLegal(x1, y1)) return false;
        if (!isLegal(x2, y2)) return false;
        if (isEmpty(x2, y2))  return false;
        else                  return !(pieceColour(x1, y1) == (pieceColour(x2, y2)));
    }

    public boolean isSameColour(int x1, int y1, int x2, int y2) {
        if (isEmpty(x2, y2)) return false;
        else                 return pieceColour(x1, y1) == pieceColour(x2, y2);
    }

    public void changeTurn() {
        currentTurn = (currentTurn == white) ? black : white;
    }

    public void changeOrientation() {
        orientation = !orientation;
    }

    public void flip() {
        flipBoard();
        flipAttackingArray();
        flipPieceMoved();
        flipCheckSquare();
        changeOrientation();
        getKingLocations();
    }

    public void flipBoard() {
        for (int i = 0; i < size / 2; i++) {
            for (int j = 0; j < size; j++) {
                Piece temp = board[i][j];
                board[i][j] = board[size - i - 1][size - j - 1];
                board[size - i - 1][size - j - 1] = temp;
            }
        }
    }

    public void flipAttackingArray() {
        for (int i = 0; i < size / 2; i++) {
            for (int j = 0; j < size; j++) {
                pieceColour temp = squaresAttacked[i][j];
                squaresAttacked[i][j] = squaresAttacked[size - i - 1][size - j - 1];
                squaresAttacked[size - i - 1][size - j - 1] = temp;
            }
        }
    }

    // might have to make pieceMoved an array or list cause this is kinda gross
    public void flipPieceMoved() {
        pieceMovedX1 = size - pieceMovedX1 - 1;
        pieceMovedY1 = size - pieceMovedY1 - 1;
        pieceMovedX2 = size - pieceMovedX2 - 1;
        pieceMovedY2 = size - pieceMovedY2 - 1;
    }

    // same with checkSquare
    public void flipCheckSquare() {
        if (checkSquare == -1) return;
        int x = checkSquare / 10;
        int y = checkSquare % 10;
        checkSquare = (size - x - 1) * 10 + size - y - 1;
    }

    public void leftClick(int x, int y) {
        if (gameOver) return;
        if (isPromotion) {
            if (promote(x, y)) {
                afterMove();
                isPromotion = false;
            }
            return;
        }
        if (isPieceSelected) {
            isPossibleMove(x, y);
            if (!isPieceSelected) {
                possibleMoves.clear();
                return;
            }
            viewer.displayBoard();
        }
        if (isEmpty(x, y) || getPiece(x, y).getColour() != currentTurn) return;
        this.selectedPiece = getBoard(x, y);
        this.isPieceSelected = true;
        this.pieceSelectedX = x;
        this.pieceSelectedY = y;
        possibleMoves = getPossibleMoves(x, y, selectedPiece.getName());
        viewer.drawPossibleMoves();
    }

    public void isPossibleMove(int x, int y) {
        for (int i = 0; i < possibleMoves.size(); i++) {
            int x_coordinate = possibleMoves.get(i) / 10;
            int y_coordinate = possibleMoves.get(i) % 10;
            if (x == x_coordinate && y == y_coordinate) {
                if (selectedPiece.getName() == king && isCastling(x, y)) castle(x, y);
                movePiece(pieceSelectedX, pieceSelectedY, x, y);
                this.isPieceSelected = false;
                break;
            }
        }
    }

    private List<Integer> getPossibleMoves(int x, int y, pieceName name) {
        List<Integer> moves = new ArrayList<>();
        if (name == king)   {moves = king(x, y, false);}
        if (name == queen)  {moves = queen(x, y, false);}
        if (name == bishop) {moves = bishop(x, y, false);}
        if (name == knight) {moves = knight(x, y, false);}
        if (name == rook)   {moves = rook(x, y, false);}
        if (name == pawn)   {moves = pawn(x, y, false);}
        int checkSquareCopy = checkSquare;
        for (int i = 0; i < moves.size(); i++) {
            if (isCheck(x, y, moves.get(i) / 10, moves.get(i) % 10)) {
                moves.remove(i);
                i--;
            }
        }
        attackingArray();
        checkSquare = checkSquareCopy;
        return moves;
    }

    public void movePiece(int x1, int y1, int x2, int y2) {
        board[x1][y1] = emptyPiece();
        board[x2][y2] = selectedPiece;
        this.pieceMovedX1 = x1;
        this.pieceMovedY1 = y1;
        this.pieceMovedX2 = x2;
        this.pieceMovedY2 = y2;
        selectedPiece.pieceHasMoved();
        if (selectedPiece.getName() == king) updateKingLocation(x2, y2);
        if (selectedPiece.getName() == pawn)
            if (y2 == 0 || y2 == 7)          pawnPromotion     (x2, y2);
        if (!isPromotion) afterMove();
    }

    public void afterMove() {
        changeTurn();
        plusMove();
        attackingArray();
        isCheck();
        viewer.displayBoard();
        if (isMate()) {
            if (isStaleMate()) {
                viewer.drawStalemate();
                gameOver = true;
                return;
            }
            viewer.drawCheckmate();
            gameOver = true;
        }
    }

    public void pawnPromotion(int x, int y) {
        isPromotion = true;
        promotionSquare = x * 10 + y;
        Color colour = (getPiece(x, y).getColour() == white) ? Color.white : Color.black;
        viewer.drawPromotion(x, y, colour);
    }

    public boolean promote(int x, int y) {
        boolean b = false;
        int pieceX = promotionSquare / 10;
        int pieceY = promotionSquare % 10;
        pieceColour colour = (getPiece(pieceX, pieceY).getColour() == white) ? white : black;
        if (x == promotionSquare / 10 && y < 4) {
            b = true;
            if (y == 0) board[pieceX][pieceY] = new Piece(pieceName.QUEEN, colour);
            if (y == 1) board[pieceX][pieceY] = new Piece(pieceName.KNIGHT, colour);
            if (y == 2) board[pieceX][pieceY] = new Piece(pieceName.ROOK, colour);
            if (y == 3) board[pieceX][pieceY] = new Piece(pieceName.BISHOP, colour);
        }
        return b;
    }

    public void updateKingLocation(int x2, int y2) {
        if (selectedPiece.getColour() == white)
             whiteKingLoc = x2 * 10 + y2;
        else blackKingLoc = x2 * 10 + y2;
    }

    private void castle(int x, int y) {
        if (x == 2) {
            board[3][y] = board[0][y];
            board[0][y] = null;
        }
        if (x == 6) {
            board[5][y] = board[7][y];
            board[7][y] = null;
        }
    }

    // if the king moves more than one space, then castling is happening
    private boolean isCastling(int x, int y) {
        return java.lang.Math.abs(pieceSelectedX - x) != 1;
    }

    public boolean isCheck(int x1, int y1, int x2, int y2) {
        boolean b = false;
        Piece tempPiece1 = board[x1][y1];
        Piece tempPiece2 = board[x2][y2];
        if (!(tempPiece1.getColour() == tempPiece2.getColour())) {
            board[x1][y1] = emptyPiece();
            board[x2][y2] = tempPiece1;
        }
        if (tempPiece1.getName() == king) getKingLocations();
        attackingArray();
        isCheck();
        if (checkSquare != -1) {
            b = true;
        }
        checkSquare = -1;
        board[x1][y1] = tempPiece1;
        board[x2][y2] = tempPiece2;
        if (tempPiece1.getName() == king) getKingLocations();
        return b;
    }

    public void isCheck() {
        checkSquare = -1;
        int kingLoc = (currentTurn == white) ? whiteKingLoc : blackKingLoc;
        if (squaresAttacked[kingLoc / 10][kingLoc % 10] != pieceColour.EMPTY &&
            squaresAttacked[kingLoc / 10][kingLoc % 10] != currentTurn) {
            checkSquare = kingLoc;
        }
    }

    public boolean isMate() {
        boolean isMate = true;
        search :
        {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    if (getPiece(x, y).getColour() == currentTurn) {
                        if (!getPossibleMoves(x, y, getPieceName(x, y)).isEmpty()) {
                            isMate = false;
                            break search;
                        }
                    }
                }
            }
        }
        return isMate;
    }

    public boolean isStaleMate() {
        boolean b = false;
        int king;
        king = (currentTurn == white) ? whiteKingLoc : blackKingLoc;
        int kingX = king / 10;
        int kingY = king % 10;
        // this is why pieceColour.BOTH is gross
        if (squaresAttacked[kingX][kingY] != oppositeColourGivenPiece(getPiece(kingX, kingY))) b = true;
        if (squaresAttacked[kingX][kingY] == pieceColour.BOTH) b = false;
        return b;
    }

    // piece methods

    // need to find a better way to add moves to list, multiplying by 10 works but is a bit messy
    public List<Integer> pawn(int x, int y, boolean forArray) {
        List<Integer> moves = new ArrayList<>();
        int d = isWhite(x, y) ? 1 : -1; // ternary operators are sexy XD
        if (!orientation) d = -d;
        // forward
        if (!forArray && isEmpty(x, y - d))
            moves.add(x * 10 + (y - d));
        // forward left
        if (!forArray && canTakePiece(x, y, x - d, y - d) && isLegal(x - d, y - d))
            moves.add((x - d) * 10 + (y - d));
        // forward right
        if (!forArray && canTakePiece(x, y, x + d, y - d) && isLegal(x + d, y - d))
            moves.add((x + d) * 10 + (y - d));
        // jump at start
        if (!forArray) {
            if (orientation) { // make this cleaner
                if (getPiece(x, y).getColour() == white && y == 6)
                    if (isEmpty(x, y - d) && isEmpty(x, y - d * 2))
                        moves.add(x * 10 + (y - (2 * d)));
                if (getPiece(x, y).getColour() == black && y == 1)
                    if (isEmpty(x, y - d) && isEmpty(x, y - d * 2))
                        moves.add(x * 10 + (y - (2 * d)));
            }
            if (!orientation) {
                if (getPiece(x, y).getColour() == white && y == 1)
                    if (isEmpty(x, y - d) && isEmpty(x, y - d * 2))
                        moves.add(x * 10 + (y - (2 * d)));
                if (getPiece(x, y).getColour() == black && y == 6)
                    if (isEmpty(x, y - d) && isEmpty(x, y - d * 2))
                        moves.add(x * 10 + (y - (2 * d)));
            }
        }
        // FOR ATTACK ARRAY
        // forward left
        if (forArray && isLegal(x - d, y - d) && !isSameColour(x, y, x - d, y - d))
            moves.add((x - d) * 10 + (y - d));
        // forward right
        if (forArray && isLegal(x + d, y - d) && !isSameColour(x, y, x + d, y - d))
            moves.add((x + d) * 10 + (y - d));
        return moves;
    }

    public List<Integer> rook(int x, int y, boolean forArray) {
        List<Integer> moves = new ArrayList<>();
        for (int[] ds : new int[][] {{0,-1},{1,0},{0,1},{-1,0}}) {
            int x1 = x + ds[0];
            int y1 = y + ds[1];
            while (isLegal(x1, y1)) {
                if (!forArray)
                    if (canTakePiece(x, y, x1, y1) || isEmpty(x1, y1)) {
                        moves.add(x1 * 10 + y1);
                        if (canTakePiece(x, y, x1, y1) || isSameColour(x, y, x1, y1)) break;
                    } else break;
                if (forArray)
                    if (canTakePiece(x, y, x1, y1) || isEmpty(x1, y1) || isSameColour(x, y, x1, y1)) {
                        moves.add(x1 * 10 + y1);
                        if (canTakePiece(x, y, x1, y1) || isSameColour(x, y, x1, y1)) break;
                    }
                x1 += ds[0];
                y1 += ds[1];
            }
        }
        return moves;
    }

    public List<Integer> knight(int x, int y, boolean forArray) {
        List<Integer> moves = new ArrayList<>();
        for (int[] ds : new int[][] {{-1,-2},{-2,-1},{-2,1},{-1,2},{1,2},{2,1},{2,-1},{1,-2}}) {
            int x1 = x + ds[0];
            int y1 = y + ds[1];
            if (isLegal(x1, y1)) {
                if (!forArray)
                    if (canTakePiece(x, y, x1, y1) || isEmpty(x1, y1))
                        moves.add(x1 * 10 + y1);
                if (forArray)
                    if (canTakePiece(x, y, x1, y1) || isEmpty(x1, y1) || isSameColour(x, y, x1, y1))
                        moves.add(x1 * 10 + y1);
            }

        }
        return moves;
    }

    public List<Integer> bishop(int x, int y, boolean forArray) {
        List<Integer> moves = new ArrayList<>();
        for (int[] ds : new int[][] {{-1,-1},{-1, 1},{1, 1},{1,-1}}) {
            int x1 = x + ds[0];
            int y1 = y + ds[1];
            while (isLegal(x1, y1)) {
                if (!forArray) {
                    if (canTakePiece(x, y, x1, y1) || isEmpty(x1, y1))
                        moves.add(x1 * 10 + y1);
                    if (canTakePiece(x, y, x1, y1) || isSameColour(x, y, x1, y1)) break;
                }
                if (forArray) {
                    if (canTakePiece(x, y, x1, y1) || isEmpty(x1, y1) || isSameColour(x, y, x1, y1))
                        moves.add(x1 * 10 + y1);
                    if (canTakePiece(x, y, x1, y1) || isSameColour(x, y, x1, y1)) break;
                }
                x1 += ds[0];
                y1 += ds[1];
            }
        }

        return moves;
    }

    public List<Integer> queen(int x, int y, boolean forArray) {
        List<Integer> moves = new ArrayList<>();
        for (int[] ds : new int[][] {{-1,-1},{-1,1},{1,1},{1,-1},{0,-1},{0,1},{-1,0},{1,0}}) {
            int x1 = x + ds[0];
            int y1 = y + ds[1];
            while (isLegal(x1, y1)) {
                if (!forArray) {
                    if (canTakePiece(x, y, x1, y1) || isEmpty(x1, y1))
                        moves.add(x1 * 10 + y1);
                    if (canTakePiece(x, y, x1, y1) || isSameColour(x, y, x1, y1)) break;
                }
                if (forArray) {
                    if (canTakePiece(x, y, x1, y1) || isEmpty(x1, y1) || isSameColour(x, y, x1, y1))
                        moves.add(x1 * 10 + y1);
                    if (canTakePiece(x, y, x1, y1) || isSameColour(x, y, x1, y1)) break;
                }
                x1 += ds[0];
                y1 += ds[1];
            }
        }
        return moves;
    }

    public List<Integer> king(int x, int y, boolean forArray) {
        List<Integer> moves = new ArrayList<>();
        pieceColour colour = pieceColour(x, y);
        for (int[] ds : new int[][] {{-1,-1},{-1, 1},{1, 1},{1,-1},{0,-1},{0, 1},{-1, 0},{1, 0}}) {
            int x1 = x + ds[0];
            int y1 = y + ds[1];
            if (isLegal(x1, y1)) {
                if (!forArray)
                    if (canTakePiece(x, y, x1, y1) || isEmpty(x1, y1))
                        moves.add(x1 * 10 + y1);
                if (forArray)
                    if (canTakePiece(x, y, x1, y1) || isEmpty(x1, y1) || isSameColour(x, y, x1, y1))
                        moves.add(x1 * 10 + y1);
            }
        }
        if (!getPiece(x, y).hasPieceMoved() && checkSquare == -1) {
            if (getPiece(0, y).getName() == rook && !getPiece(0, y).hasPieceMoved()) {
                boolean isClear = true;
                for (int i = 1; i < x; i++) {
                    if (!isEmpty(i, y) || squaresAttacked[i][y] == pieceColour.BOTH
                                       || squaresAttacked[i][y] == oppositeColour(colour)) {
                        isClear = false;
                        break;
                    }
                }
                if (isClear) moves.add(2 * 10 + y);
            }
            if (getPiece(7, y).getName() == rook && !getPiece(7, y).hasPieceMoved()) {
                boolean isClear = true;
                for (int i = 6; i > x; i--) {
                    if (!isEmpty(i, y) || squaresAttacked[i][y] == pieceColour.BOTH
                                       || squaresAttacked[i][y] == oppositeColour(colour)) {
                        isClear = false;
                        break;
                    }
                }
                if (isClear) moves.add(6 * 10 + y);
            }
        }
        return moves;
    }
}
