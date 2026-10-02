package chess;

public class IsInCheckmate {
    public boolean isInCheckmate(ChessGame game, ChessGame.TeamColor teamColor) {

        ChessBoard board = game.getBoard();

        if (game.getTeamTurn() != teamColor) {
            return false;
        }

        IsInCheck isInCheck = new IsInCheck();
        if (!isInCheck.checkFinder(board, teamColor)) {
            return false;
        }

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if (piece != null) {
                    if (piece.getTeamColor() == teamColor) {
                        ValidMoveFinder validMoves = new ValidMoveFinder();
                        if (!validMoves.validMoves(board, position).isEmpty()) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }
}
