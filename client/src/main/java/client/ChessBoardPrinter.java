package client;

import chess.*;
import ui.EscapeSequences;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class ChessBoardPrinter {

    ChessBoard board;
    ChessGame.TeamColor color;

    public ChessBoardPrinter(ChessGame chessGame, ChessGame.TeamColor color) {
        this.board = chessGame.getBoard();
        this.color = color;
    }

    public void printChessBoard() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        printSquares(out);
    }


    private void printSquares(PrintStream out) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPiece chessPiece = board.getPiece(new ChessPosition(row, col));
                if (chessPiece != null) {
                    String pieceString = getPieceString(chessPiece);
                }

                if ((row + col) % 2 != 0) {
                    setWhite(out);
                } else {
                    setBlack(out);
                }

            }
        }
    }


    private String getPieceString(ChessPiece chessPiece) {

        if (color == ChessGame.TeamColor.WHITE) {
            return switch (chessPiece.getPieceType()) {
                case KING -> EscapeSequences.WHITE_KING;
                case QUEEN -> EscapeSequences.WHITE_QUEEN;
                case ROOK -> EscapeSequences.WHITE_ROOK;
                case BISHOP -> EscapeSequences.WHITE_BISHOP;
                case KNIGHT -> EscapeSequences.WHITE_KNIGHT;
                case PAWN -> EscapeSequences.WHITE_PAWN;
            };
        }

        return switch (chessPiece.getPieceType()) {
            case KING -> EscapeSequences.BLACK_KING;
            case QUEEN -> EscapeSequences.BLACK_QUEEN;
            case ROOK -> EscapeSequences.BLACK_ROOK;
            case BISHOP -> EscapeSequences.BLACK_BISHOP;
            case KNIGHT -> EscapeSequences.BLACK_KNIGHT;
            case PAWN -> EscapeSequences.BLACK_PAWN;
        };

    }


    private void setWhite(PrintStream out) {

    }

    private void setBlack(PrintStream out) {

    }

}
