package chess;

import java.util.List;

public interface ChessMoveFinder {
    List<ChessMove> pieceMoves(ChessBoard board, ChessPosition position);
}
