package chess;

import java.util.ArrayList;
import java.util.List;

public class PawnMoveFinder implements ChessMoveFinder {
    public List<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessGame.TeamColor pieceColor = board.getPiece(position).getTeamColor();

        List<ChessMove> availableMoves = new ArrayList<>();

        ChessPiece.PieceType[] promotionTypes = {
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT,
        };

        int promotion = 8;
        int movement = 1;
        int twoMove = 2;
        if (pieceColor == ChessGame.TeamColor.BLACK) {
            promotion = 1;
            movement = -1;
            twoMove = 7;
        }

        ChessPosition oneForward = new ChessPosition(position.getRow() + movement, position.getColumn());
        ChessPosition twoForward = new ChessPosition(position.getRow() + movement * 2, position.getColumn());
        ChessPosition leftDiagonal = new ChessPosition(position.getRow() + movement, position.getColumn() - 1);
        ChessPosition rightDiagonal = new ChessPosition(position.getRow() + movement, position.getColumn() + 1);

        if (board.getPiece(oneForward) == null) {
            if (oneForward.getRow() == promotion) {
                for (ChessPiece.PieceType promotionType : promotionTypes) {
                    availableMoves.add(new ChessMove(position, oneForward, promotionType));
                }
            } else {
                availableMoves.add(new ChessMove(position, oneForward, null));
            }
            if (position.getRow() == twoMove) {
                if (board.getPiece(twoForward) == null) {
                    availableMoves.add(new ChessMove(position, twoForward, null));
                }
            }
        }

        if (leftDiagonal.getColumn() >= 1) {
            if (board.getPiece(leftDiagonal) != null) {
                if (board.getPiece(leftDiagonal).getTeamColor() != pieceColor) {
                    if (leftDiagonal.getRow() == promotion) {
                        for (ChessPiece.PieceType promotionType : promotionTypes) {
                            availableMoves.add(new ChessMove(position, leftDiagonal, promotionType));
                        }
                    } else {
                        availableMoves.add(new ChessMove(position, leftDiagonal, null));
                    }
                }
            }
        }

        if (rightDiagonal.getColumn() <= 8) {
            if (board.getPiece(rightDiagonal) != null) {
                if (board.getPiece(rightDiagonal).getTeamColor() != pieceColor) {
                    if (rightDiagonal.getRow() == promotion) {
                        for (ChessPiece.PieceType promotionType : promotionTypes) {
                            availableMoves.add(new ChessMove(position, rightDiagonal, promotionType));
                        }
                    } else {
                        availableMoves.add(new ChessMove(position, rightDiagonal, null));
                    }
                }
            }
        }

        return availableMoves;
    }
}
