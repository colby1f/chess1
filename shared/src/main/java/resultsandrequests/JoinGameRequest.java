package resultsAndRequests;

import chess.*;

public record JoinGameRequest(ChessGame.TeamColor playerColor, Integer gameID) {
}
