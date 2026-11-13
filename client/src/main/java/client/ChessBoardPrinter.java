package client;

import chess.ChessGame;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class ChessBoardPrinter {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;

    ChessGame chessGame;
    ChessGame.TeamColor color;

    public ChessBoardPrinter(ChessGame chessGame, ChessGame.TeamColor color) {
        this.chessGame = chessGame;
        this.color = color;
    }

    public void printChessBoard() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    }




}
