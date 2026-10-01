package chess;

public class MoveMaker {
    public ChessBoard moveMaker(ChessMove move, ChessBoard board) {

        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();

        ChessPiece piece = board.getPiece(startPosition);

        board.addPiece(startPosition, null);
        board.addPiece(endPosition, piece);

        return board;
    }
}
