package Object;

import java.io.Serializable;

public abstract class Player implements Serializable {
    private int turn;
    private Piece playerPiece;

    public Player() {}

    public Player(int turn, Piece playerPiece) {
        this.turn = turn;
        this.playerPiece = playerPiece;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public Piece getPlayerPiece() {
        return playerPiece;
    }

    @Override
    public String toString() {
        return "[Turn: " + turn + ", Piece: " + playerPiece.getColor() + "]";
    }
}
