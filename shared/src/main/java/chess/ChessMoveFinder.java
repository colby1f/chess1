package chess;

public interface ChessMoveFinder {
    ChessMove[] pieceMoves(ChessBoard board, ChessPosition position);
}
