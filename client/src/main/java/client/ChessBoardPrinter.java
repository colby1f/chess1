package client;

import chess.*;
import ui.EscapeSequences;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class ChessBoardPrinter {

    ChessBoard board;
    ChessGame.TeamColor color;
    int SQUARE_SIZE = 3;
    int PIECE_POSITION = 2;

    public ChessBoardPrinter(ChessGame chessGame, ChessGame.TeamColor color) {
        this.board = chessGame.getBoard();
        this.color = color;
    }

    public void printChessBoard() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(EscapeSequences.SET_TEXT_BOLD);
        printSquares(out);
    }


    private void printSquares(PrintStream out) {
        for (int row = 8; row >= 1; row--) {
            printRow(out, row);
            out.println();
        }
    }


    private void printRow(PrintStream out, int row) {
        for (int dim = 1; dim <= SQUARE_SIZE; dim++) {

            for (int col = 1; col <= 8; col++) {

                int newRow = row;
                int newCol = col;

                if (color == ChessGame.TeamColor.BLACK) {
                    newRow = invert(row);
                    newCol = invert(col);
                }
                setSquareColor(newRow, newCol, out);

                ChessPiece chessPiece = board.getPiece(new ChessPosition(newRow, newCol));
                if (dim == PIECE_POSITION && chessPiece != null) {
                    printPiece(chessPiece, newRow, newCol, out);
                } else {
                    out.print(EscapeSequences.EMPTY.repeat(SQUARE_SIZE));
                    out.print(EscapeSequences.RESET_BG_COLOR);
                }
            }

            if (dim != SQUARE_SIZE) {
                out.println();
            }
        }
    }

    private void printPiece(ChessPiece chessPiece, int row, int col, PrintStream out) {
        String pieceString = getPieceString(chessPiece);

        out.print(EscapeSequences.EMPTY);

        if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + pieceString);
        } else {
            out.print(EscapeSequences.SET_TEXT_COLOR_BLACK + pieceString);
        }

        setSquareColor(row, col, out);
        out.print(EscapeSequences.EMPTY);
        out.print(EscapeSequences.RESET_BG_COLOR);
    }


    private void setSquareColor(int row, int col, PrintStream out) {
        if ((row + col) % 2 != 0) {
            out.print(EscapeSequences.SET_BG_COLOR_WHITE);
            out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        } else {
            out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            out.print(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);
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


    private int invert(int num) {

        int newNum;

        if (num == 1) {
            newNum = 8;
        } else if (num == 2) {
            newNum = 7;
        } else if (num == 3) {
            newNum = 6;
        } else if (num == 4) {
            newNum = 5;
        } else if (num == 5) {
            newNum = 4;
        } else if (num == 6) {
            newNum = 3;
        } else if (num == 7) {
            newNum = 2;
        } else {
            newNum = 1;
        }

        return newNum;
    }

}
