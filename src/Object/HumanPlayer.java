package Object;


import java.io.Serializable;

public class HumanPlayer extends Player implements Serializable {
	public HumanPlayer() {
		super();
	}
	
	public HumanPlayer(int turn, Piece playerPiece) {
		super(turn, playerPiece);
	}
}
