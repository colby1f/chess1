package chess;

import java.util.ArrayList;
import java.util.List;

public class IsInCheck {
    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean checkFinder(ChessBoard board, ChessGame.TeamColor teamColor) {

        // finds kings position
        ChessPosition kingPosition;
        if (teamColor == ChessGame.TeamColor.WHITE) {
            kingPosition = board.getWhiteKingPosition();
        } else {
            kingPosition = board.getBlackKingPosition();
        }

        // checks if the king is in check to a knight
        KiKnMoveFinder knightMoveFinder = new KiKnMoveFinder();
        int[][] knightDirections = {{2, -1}, {2, 1}, {-2, -1}, {-2, 1}, {1, -2}, {1, 2}, {-1, -2}, {-1, 2}};
        List<ChessMove> knightMoves = knightMoveFinder.pieceMoves(board, kingPosition, knightDirections);
        for (ChessMove move : knightMoves) {
            ChessPiece otherPiece = board.getPiece(move.getEndPosition());
            if (otherPiece != null) {
                if (otherPiece.getPieceType() == ChessPiece.PieceType.KNIGHT && otherPiece.getTeamColor() != teamColor) {
                    return true;
                }
            }
        }

        // checks if the king is in check to a pawn
        List<ChessPosition> pawnMoves = new ArrayList<>();
        int dir = 1;
        if (teamColor == ChessGame.TeamColor.BLACK) {
            dir = -1;
        }
        pawnMoves.add(new ChessPosition(kingPosition.getRow() + dir, kingPosition.getColumn() + 1));
        pawnMoves.add(new ChessPosition(kingPosition.getRow() + dir, kingPosition.getColumn() - 1));
        for (ChessPosition move : pawnMoves) {
            if (move.getRow() <= 8 && move.getRow() >= 1 && move.getColumn() <= 8 && move.getColumn() >= 1) {
                ChessPiece otherPiece = board.getPiece(move);
                if (otherPiece != null) {
                    if (otherPiece.getPieceType() == ChessPiece.PieceType.PAWN && otherPiece.getTeamColor() != teamColor) {
                        return true;
                    }
                }
            }
        }

        // checks if the king is in check horizontally to a rook or queen
        RoBiQuMoveFinder rookMoveFinder = new RoBiQuMoveFinder();
        int[][] rookDirections = {{0, -1}, {0, 1}, {1, 0}, {-1, 0}};
        List<ChessMove> rookMoves = rookMoveFinder.pieceMoves(board, kingPosition, rookDirections);
        for (ChessMove move : rookMoves) {
            ChessPiece otherPiece = board.getPiece(move.getEndPosition());
            if (otherPiece != null) {
                if (otherPiece.getPieceType() == ChessPiece.PieceType.ROOK || otherPiece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                    if (otherPiece.getTeamColor() != teamColor) {
                        return true;
                    }
                }
            }
        }

        // checks if the king is in check diagonally to a bishop or queen
        RoBiQuMoveFinder bishopMoveFinder = new RoBiQuMoveFinder();
        int[][] bishopDirections = {{1, -1}, {1, 1}, {-1, -1}, {-1, 1}};
        List<ChessMove> bishopMoves = bishopMoveFinder.pieceMoves(board, kingPosition, bishopDirections);
        for (ChessMove move : bishopMoves) {
            ChessPiece otherPiece = board.getPiece(move.getEndPosition());
            if (otherPiece != null) {
                if (otherPiece.getPieceType() == ChessPiece.PieceType.BISHOP || otherPiece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                    if (otherPiece.getTeamColor() != teamColor) {
                        return true;
                    }
                }
            }
        }

        // checks if the king is next to the other king
        KiKnMoveFinder kingMoveFinder = new KiKnMoveFinder();
        int[][] kingDirections = {{1, -1}, {1, 1}, {-1, -1}, {-1, 1}, {0, -1}, {0, 1}, {1, 0}, {-1, 0}};
        List<ChessMove> kingMoves = kingMoveFinder.pieceMoves(board, kingPosition, kingDirections);
        for (ChessMove move : kingMoves) {
            ChessPiece otherPiece = board.getPiece(move.getEndPosition());
            if (otherPiece != null) {
                if (otherPiece.getPieceType() == ChessPiece.PieceType.KING) {
                    if (otherPiece.getTeamColor() != teamColor) {
                        return true;
                    }
                }
            }
        }


        // if not in check returns false
        return false;
    }
}
