package main.java.main;

import org.eclipse.swt.graphics.Rectangle;

public class Utility {


	public static Rectangle sommaRect(Rectangle a, Rectangle b) {
		return new Rectangle(a.x+b.x,a.y+b.y,a.width+b.width,a.height+b.height);
	}

	/*
		@par the position i to check
		@return true if the position i is valid, false otherwise
	 */

	/*
		Restituisce true se la posizione è valida, false altrimenti
	 */
	public static boolean checkPos(int i){
		if((i>=0) && (i<Math.pow(Brique.N,2))){
			return true;
		}
		return false;
	}

	public static void printState(Brique game){
		int state[] = game.getGameState();
		for(int i=0; i<Math.pow(Brique.N,2);i++){
			if (state[i]==1){
				System.out.print("1 ");
			}
			else if (state[i]==2){
				System.out.print("2 ");
			}
			else{
				System.out.print("O ");
			}
			if ((i+1)%Brique.N==0){
				System.out.println();
			}
		}
	}

	public static void resetState(Brique game){
		game.setGameState(new int[Brique.N*Brique.N]);
	}

}
