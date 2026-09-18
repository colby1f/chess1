package chess;

import java.util.ArrayList;
import java.util.List;

public class RookMoveFinder implements ChessMoveFinder {
    public ChessMove[] pieceMoves(ChessBoard board, ChessPosition position) {
        ChessPiece piece = board.getPiece(position);
        ChessGame.TeamColor pieceColor = piece.getTeamColor();

        List<ChessMove> availableMoves = new ArrayList<>();

        int[][] directions = {{0, -1}, {0, 1}, {1, 0}, {-1, 0}};

        for (int[] dir : directions){
            int distance = 1;
            while (distance < 8){
                ChessPosition newPosition = new ChessPosition(position.getRow() + (dir[0] * distance), position.getColumn() + dir[0] * distance);

                if (board.getPiece(newPosition) == null){
                    availableMoves.add(new ChessMove(position, newPosition, null));
                }


            }

        }





        return availableMoves;
    }
}
