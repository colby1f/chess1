package chess;

import java.util.ArrayList;
import java.util.Collection;

public class ValidMoveFinder {
    public Collection<ChessMove> validMoves(ChessGame game, ChessBoard board, ChessPosition startPosition) {

        ChessPiece piece = board.getPiece(startPosition);

        if (piece == null) {
            return null;
        }

        Collection<ChessMove> potentialMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> allMoves = new ArrayList<>();








        return allMoves;
    }
}
