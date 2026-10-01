package chess;

public class IsInCheckmate {
    public boolean isInCheckmate(ChessGame game, ChessGame.TeamColor teamColor) {

        ChessBoard board = game.getBoard();


        IsInCheck isInCheck = new IsInCheck();
        if(!isInCheck.checkFinder(board, teamColor)) {
            return false;
        }




        return true;
    }
}
