package client;

import chess.*;
import ui.EscapeSequences;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ChessBoardPrinter {

    ChessBoard board;
    ChessGame.TeamColor color;
    ArrayList<String> TOP_BOTTOM_BOARDER = new ArrayList<>(List.of(" ", "  a ", " b  ", " c  ", " d ", " e  ", " f  ", " g  ", "h  ", " "));
    ArrayList<String> SIDE_BOARDER = new ArrayList<>(List.of(" 8  ", " 7  ", " 6  ", " 5  ", " 4  ", " 3  ", " 2  ", " 1  "));

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

        printBoarderTopBottom(out);
        for (int row = 1; row <= 8; row++) {
            printRow(out, row);
            out.println();
        }
        printBoarderTopBottom(out);
    }


    private void printRow(PrintStream out, int row) {

        printBoarderSquare(out, row, SIDE_BOARDER);

        for (int col = 1; col <= 8; col++) {

            int newRow = invert(row);
            int newCol = col;

            if (color == ChessGame.TeamColor.BLACK) {
                newRow = row;
                newCol = invert(col);
            }
            setSquareColor(newRow, newCol, out);

            ChessPiece chessPiece = board.getPiece(new ChessPosition(newRow, newCol));
            if (chessPiece != null) {
                printPiece(chessPiece, out);
            } else {
                out.print(EscapeSequences.EMPTY);
                out.print(EscapeSequences.RESET_BG_COLOR);
            }
        }

        printBoarderSquare(out, row, SIDE_BOARDER);

    }


    private void printBoarderTopBottom(PrintStream out) {

        for (int col = 1; col <= 10; col++) {
            printBoarderSquare(out, col, TOP_BOTTOM_BOARDER);
        }

        out.print(EscapeSequences.RESET_BG_COLOR);
        out.println();
    }


    private void printBoarderSquare(PrintStream out, int pos, ArrayList<String> list) {
        out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        out.print(EscapeSequences.SET_TEXT_COLOR_RED);


        ArrayList<String> listCopy = new ArrayList<>(list);
        if (color == ChessGame.TeamColor.BLACK) {
            Collections.reverse(listCopy);
        }
        String value = listCopy.get(pos - 1);


        if (Objects.equals(value, " ")) {
            out.print(EscapeSequences.EMPTY);
        } else {
            out.print(value);
        }

        out.print(EscapeSequences.RESET_BG_COLOR);
    }


    private void printPiece(ChessPiece chessPiece, PrintStream out) {

        String pieceString = getPieceString(chessPiece);

        if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + pieceString);
        } else {
            out.print(EscapeSequences.SET_TEXT_COLOR_BLACK + pieceString);
        }

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
