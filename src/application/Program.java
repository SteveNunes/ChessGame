package application;

import java.util.Scanner;

import board.Board;
import enums.ChessPlayMode;
import enums.PieceType;
import exceptions.PieceMoveException;
import exceptions.PieceSelectionException;
import piece.PiecePosition;

public class Program {
	
	private static Board board;
	private static UI ui;
	
	public static void main(String[] args) {
		
		board = new Board();
		ui = new UI(board);
		String imput, error = "";
		Boolean playing = true;
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Select the game mode\n");
		System.out.println("1 - Player vs Player");
		System.out.println("2 - Player vs CPU");
		System.out.println("3 - CPU vs CPU\n");
		System.out.print(">");
		try { 
			imput = sc.nextLine();
			board.setPlayMode(imput.equals("1") ? ChessPlayMode.PLAYER_VS_PLAYER :
				imput.equals("2") ? ChessPlayMode.PLAYER_VS_CPU : ChessPlayMode.CPU_VS_CPU);
		}
		catch (Exception e) { }

		System.out.println("Select the game colors (Warning: for colors work properly, you must run the game through a console with color support (e.g. Git Bash))\n");
		System.out.println("1 - Colored");
		System.out.println("2 - Black and White\n");
		System.out.print(">");
		try { 
			imput = sc.nextLine();
			if (imput.equals("1")) 
				AnsiColors.setToColored();
		}
		catch (Exception e) { }
		
		while (playing) {
			try {
				board.reset();
				setPiecesOnTheBoard();
				board.validateBoard();
			}
			catch (Exception e) {
				e.printStackTrace();
				return;
			}
			while (!board.checkMate()) {
				if (board.pawnWasPromoted()) ui.promotePiece(sc);
				else {
					AnsiColors.clearScreen();
					ui.drawBoard();
					System.out.println();
					ui.drawInfos();
					System.out.println();
					if (!error.isEmpty()) {
						System.out.println(AnsiColors.ANSI_RED + error + AnsiColors.ANSI_RESET);
						error = "";
					}
					
					if (!board.isCpuTurn()) {
						if (!board.pieceIsSelected()) {
							if (board.getPlayMode() != ChessPlayMode.PLAYER_VS_PLAYER &&
									board.getLastMovedPiece() != null) {
								
							}
							System.out.print("Enter source piece coordinate (e.g. a1): ");
						}
						else System.out.print("Enter target coordinate (e.g. a1): ");
						try {
							imput = sc.nextLine();
							if (imput.length() >= 4) {
								board.selectPiece(PiecePosition.stringToPosition(imput.substring(0, 2)));
								board.movePieceTo(PiecePosition.stringToPosition(imput.substring(2, 4)));
							}
							else if (!board.pieceIsSelected()) board.selectPiece(PiecePosition.stringToPosition(imput));
							else board.movePieceTo(PiecePosition.stringToPosition(imput));
						}
						catch (PieceMoveException e) 
							{ error = e.getMessage(); }
						catch (PieceSelectionException e) 
						{ error = e.getMessage(); }
						catch (RuntimeException e)
							{ error = "Invalid coordinate! The value must be between 'a1' to 'h8'"; }
					}
					else {
						try {
							board.getChessAI().doCpuSelectAPiece();
							if (board.pawnWasPromoted())
								board.promotePawnTo(PieceType.QUEEN);
							board.getChessAI().doCpuMoveSelectedPiece();
						}
						catch (Exception e) {}
					}
				}
			}
			AnsiColors.clearScreen();
			ui.drawBoard();
			System.out.println();
			playing = false;
			System.out.println(AnsiColors.ANSI_CYAN + "CHECKMATE! " + ui.currentTurnColor(board.getWinnerColor()) + " won!");
			System.out.print(AnsiColors.ANSI_RESET + "Play again? (y|n): ");
			try { if (sc.nextLine().toLowerCase().equals("y")) playing = true; }
			catch (Exception e) { }
		}	
		sc.close();
	}
	
	private static void setPiecesOnTheBoard() throws Exception {
		board.setBoard(new Character[][] {
			{'r','n','b','q','k','b','n','r'},
			{'p','p','p','p','p','p','p','p'},
			{' ',' ',' ',' ',' ',' ',' ',' '},
			{' ',' ',' ',' ',' ',' ',' ',' '},
			{' ',' ',' ',' ',' ',' ',' ',' '},
			{' ',' ',' ',' ',' ',' ',' ',' '},
			{'P','P','P','P','P','P','P','P'},
			{'R','N','B','Q','K','B','N','R'}
		});
	}

}
