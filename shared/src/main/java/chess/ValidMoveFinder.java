package chess;

import java.util.ArrayList;
import java.util.Collection;

public class ValidMoveFinder {
    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessGame game, ChessBoard board, ChessPosition startPosition) {

        ChessPiece piece = board.getPiece(startPosition);

        if (piece == null) {
            return null;
        }

        Collection<ChessMove> potentialMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> availableMoves = new ArrayList<>();








        return availableMoves;
    }
}
