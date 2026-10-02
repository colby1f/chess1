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
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition startPosition) {

        ChessPiece piece = board.getPiece(startPosition);

        if (piece == null) {
            return new ArrayList<>();
        }

        Collection<ChessMove> potentialMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> legalMoves = new ArrayList<>();


        for (ChessMove move : potentialMoves) {
            ChessBoard newBoard = new ChessBoard(board);

            MoveMaker moveMaker = new MoveMaker();
            newBoard = moveMaker.moveMaker(move, newBoard);

            IsInCheck isInCheck = new IsInCheck();
            if (!isInCheck.checkFinder(newBoard, piece.getTeamColor())) {
                legalMoves.add(move);
            }

        }


        return legalMoves;
    }
}
