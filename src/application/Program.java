package application;

import java.util.Scanner;

import board.Board;
import board.BoardException;
import piece.Position;

public class Program {
	
	public static void main(String[] args) {
		
		String imput, error = "";
		Boolean playing = true;
		Scanner sc = new Scanner(System.in);
		Board board = new Board();
		UI ui = new UI(board);
		
		System.out.println("Select the game colors (Warning: for colors work properly, you must run the game through a console with color support (e.g. Git Bash))\n");
		System.out.println("1 - Colored");
		System.out.println("2 - Black and White\n");
		System.out.print(">");
		try { 
			imput = sc.nextLine();
			if (imput.toLowerCase().equals("1")) AnsiColors.setToColored(); 
		}
		catch (Exception e) { }
		
		Boolean withTry = true;

		while (playing) {
			board.reset();
			while (!board.checkMate()) {
				if (board.pieceWasPromoted()) ui.promotePiece(sc);
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
					
					if (!board.pieceIsSelected()) 
						System.out.print("Enter source piece coordinate (e.g. a1): ");
					else System.out.print("Enter target coordinate (e.g. a1): ");
					if (withTry) {
						try {
							imput = sc.nextLine();
							if (imput.length() >= 4) {
								board.selectPiece(Position.stringToPosition(imput.substring(0, 2)));
								board.movePieceTo(Position.stringToPosition(imput.substring(2, 4)));
							}
							else if (!board.pieceIsSelected()) board.selectPiece(Position.stringToPosition(imput));
							else board.movePieceTo(Position.stringToPosition(imput));
						}
						catch (BoardException e) { error = e.getMessage(); }
						catch (RuntimeException e)
							{ error = "Invalid coordinate! The value must be between 'a1' to 'h8'"; }
					}
					else {
						imput = sc.nextLine();
						if (!board.pieceIsSelected()) board.selectPiece(Position.stringToPosition(imput));
						else board.movePieceTo(Position.stringToPosition(imput));
					}
				}
			}
			AnsiColors.clearScreen();
			ui.drawBoard();
			System.out.println();
			playing = false;
			System.out.println(AnsiColors.ANSI_CYAN + "CHECKMATE! " + ui.turnColor(board.getWinnerColor()) + " won!");
			System.out.print(AnsiColors.ANSI_RESET + "Play again? (y|n): ");
			try { if (sc.nextLine().toLowerCase().equals("y")) playing = true; }
			catch (Exception e) { }
		}	
		sc.close();
	}
}
