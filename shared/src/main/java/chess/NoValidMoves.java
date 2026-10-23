package chess;

public class NoValidMoves {
    public boolean noValidMoves(ChessGame game, ChessGame.TeamColor teamColor) {

        ChessBoard board = game.getBoard();

        if (game.getTeamTurn() != teamColor) {
            return false;
        }

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if (piece != null) {
                    ValidMoveFinder validMoves = new ValidMoveFinder();
                    if (piece.getTeamColor() == teamColor && !validMoves.validMoves(board, position).isEmpty()) {
                        return false;
                    }
                }
            }
        }

        return true;

    }

}
