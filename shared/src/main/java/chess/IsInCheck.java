package chess;

import java.util.List;

public class IsInCheck {
    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck (ChessBoard board, ChessGame.TeamColor teamColor) {
        ChessPosition kingPosition;
        if (teamColor == ChessGame.TeamColor.WHITE) {
            kingPosition = board.getWhiteKingPosition();
        } else {
            kingPosition = board.getBlackKingPosition();
        }


        ChessMoveFinder knightMoveFinder = new KnightMoveFinder();
        List<ChessMove> knightMoves = knightMoveFinder.pieceMoves(board, kingPosition);
        for (ChessMove move : knightMoves) {
            ChessPiece otherPiece = board.getPiece(move.getEndPosition());
            if (otherPiece != null) {
                if (otherPiece.getPieceType() == ChessPiece.PieceType.KNIGHT && otherPiece.getTeamColor() != teamColor) {
                    return true;
                }
            }
        }






        return false;
    }
}
