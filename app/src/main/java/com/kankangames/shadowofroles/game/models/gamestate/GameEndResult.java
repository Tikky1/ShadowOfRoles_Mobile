package com.kankangames.shadowofroles.game.models.gamestate;

public class GameEndResult {
    private final boolean gameFinished;
    private final GameEndReason reason;
    private final WinStatus winStatus;

    public GameEndResult(boolean gameFinished, GameEndReason reason, WinStatus winStatus) {
        this.gameFinished = gameFinished;
        this.reason = reason;
        this.winStatus = winStatus;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public GameEndReason getReason() {
        return reason;
    }

    public WinStatus getWinStatus() {
        return winStatus;
    }
}
