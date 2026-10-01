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
    public Collection<ChessMove> validMoves(ChessGame game, ChessPosition startPosition) {

        ChessBoard board = game.getBoard();
        ChessPiece piece = board.getPiece(startPosition);

        if (piece == null || piece.getTeamColor() != game.getTeamTurn()) {
            return null;
        }

        Collection<ChessMove> potentialMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> legalMoves = new ArrayList<>();


        for (ChessMove move : potentialMoves) {
            ChessBoard newBoard = new ChessBoard(board);

            MoveMaker moveMaker = new MoveMaker();
            newBoard = moveMaker.moveMaker(move, newBoard);

            IsInCheck isInCheck = new IsInCheck();
            if (!isInCheck.checkFinder(newBoard, game.getTeamTurn())) {
                legalMoves.add(move);
            }

        }


        return legalMoves;
    }
}
