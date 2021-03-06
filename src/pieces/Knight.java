package pieces;

import java.util.ArrayList;
import java.util.List;

import board.Board;
import piece.*;

public class Knight extends Piece  {

	public Knight(Board board, Position position, Color color) 
		{ super(board, position, color); }

	@Override
	public List<Position> possibleMoves() {
		List<Position> moves = new ArrayList<>();
		int[][] inc = {
			{-2,-2,-1,-1,2,2,1,1},
			{-1,1,-2,2,-1,1,-2,2}
		};
		Position p = new Position(getPosition());
		// 8 directions check
		for (int dir = 0; dir < 8; dir++) {
			p.setValues(getPosition());
			p.incValues(inc[0][dir], inc[1][dir]);
			if (getBoard().isValidBoardPosition(p) &&
				(!getBoard().thereHavePiece(p) || getBoard().isOpponentPiece(p, getColor())))
					moves.add(new Position(p));
		}
		return moves;
	}

	@Override
	public String toString() { return "N"; }

}