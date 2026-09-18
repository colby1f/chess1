package chess;

import java.util.ArrayList;
import java.util.List;

public class PawnMoveFinder implements ChessMoveFinder {
    public List<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessGame.TeamColor pieceColor = board.getPiece(position).getTeamColor();

        List<ChessMove> availableMoves = new ArrayList<>();

        int promotion = 8;
        int movement = 1;
        if (pieceColor == ChessGame.TeamColor.BLACK){
            promotion = 1;
            movement = -1;
        }

        ChessPosition oneForward = new ChessPosition(position.getRow() + movement, position.getColumn());
        ChessPosition twoForward = new ChessPosition(position.getRow() + movement * 2, position.getColumn());
        ChessPosition leftDiagonal = new ChessPosition(position.getRow() + movement, position.getColumn() - 1);
        ChessPosition rightDiagonal = new ChessPosition(position.getRow() + movement, position.getColumn() + 1);

        if (board.getPiece(oneForward) == null){
            if(oneForward.getRow() == 8){
                availableMoves.add(new ChessMove(position, oneForward, ChessPiece.PieceType.QUEEN));
                availableMoves.add(new ChessMove(position, oneForward, ChessPiece.PieceType.ROOK));
                availableMoves.add(new ChessMove(position, oneForward, ChessPiece.PieceType.BISHOP));
                availableMoves.add(new ChessMove(position, oneForward, ChessPiece.PieceType.KNIGHT));
            } else{
                availableMoves.add(new ChessMove(position, oneForward, null));
            }

            if(board.getPiece(twoForward) == null){
                availableMoves.add(new ChessMove(position, twoForward, null));
            }
        }

        if (leftDiagonal.getColumn() >= 1) {
            if (board.getPiece(leftDiagonal) == null) {
                if (leftDiagonal.getRow() == 8) {
                    availableMoves.add(new ChessMove(position, leftDiagonal, ChessPiece.PieceType.QUEEN));
                    availableMoves.add(new ChessMove(position, leftDiagonal, ChessPiece.PieceType.ROOK));
                    availableMoves.add(new ChessMove(position, leftDiagonal, ChessPiece.PieceType.BISHOP));
                    availableMoves.add(new ChessMove(position, leftDiagonal, ChessPiece.PieceType.KNIGHT));
                } else {
                    availableMoves.add(new ChessMove(position, leftDiagonal, null));
                }
            }
        }

        if (rightDiagonal.getColumn() >= 1) {
            if (board.getPiece(rightDiagonal) == null) {
                if (rightDiagonal.getRow() == 8) {
                    availableMoves.add(new ChessMove(position, rightDiagonal, ChessPiece.PieceType.QUEEN));
                    availableMoves.add(new ChessMove(position, rightDiagonal, ChessPiece.PieceType.ROOK));
                    availableMoves.add(new ChessMove(position, rightDiagonal, ChessPiece.PieceType.BISHOP));
                    availableMoves.add(new ChessMove(position, rightDiagonal, ChessPiece.PieceType.KNIGHT));
                } else {
                    availableMoves.add(new ChessMove(position, rightDiagonal, null));
                }
            }
        }

        return availableMoves;
    }
}
