package chess;

import java.util.ArrayList;
import java.util.List;

public class KingMoveFinder implements ChessMoveFinder {
    public List<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessGame.TeamColor pieceColor = board.getPiece(position).getTeamColor();

        List<ChessMove> availableMoves = new ArrayList<>();

        int[][] directions = {{1, -1}, {1, 1}, {-1, -1}, {-1, 1}, {0, -1}, {0, 1}, {1, 0}, {-1, 0}};

        for (int[] dir : directions) {

            ChessPosition newPosition = new ChessPosition(position.getRow() + (dir[0]), position.getColumn() + (dir[1]));
            if (newPosition.getRow() <= 8 && newPosition.getRow() >= 1 && newPosition.getColumn() <= 8 && newPosition.getColumn() >= 1) {
                if (board.getPiece(newPosition) == null) {
                    availableMoves.add(new ChessMove(position, newPosition, null));
                } else {
                    if (board.getPiece(newPosition).getTeamColor() != pieceColor) {
                        availableMoves.add(new ChessMove(position, newPosition, null));
                    }
                }
            }

        }


        return availableMoves;
    }
}
