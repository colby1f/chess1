package chess;

public class IsInCheckmate {
    public boolean isInCheckmate(ChessGame game, ChessGame.TeamColor teamColor) {

        ChessBoard board = game.getBoard();


        IsInCheck isInCheck = new IsInCheck();
        if(!isInCheck.checkFinder(board, teamColor)) {
            return false;
        }

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if (piece.getTeamColor() == teamColor) {
                    ValidMoveFinder validMoves = new ValidMoveFinder();
                    if (validMoves.validMoves(game, position) != null) {
                        return false;
                    }
                }
            }
        }


        return true;
    }
}
