import java.util.*;
import java.io.*;
import java.util.concurrent.*;
import java.io.IOException;

// GALAGA GAME ON TERMINAL
// Implemented using Java threads
// Bilaw, Apolinario, Pilipina

public class Galaga {
	static volatile char[][] matrix;
	static Boolean alive = true;
	static int level = 1;
	static int level_ = 0;
	volatile static int[] pos = new int[2]; // current position of ship
	static Scanner scanner = new Scanner(System.in);
	static String input = " ";
	static char temp = ' ';
	static volatile int score = 0;
	static Thread ship = new Thread(new Ship());
	static Thread swarm = new Thread(new Swarm());
	static int x = 0; 

	public static void clear(String[] arg) throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
    }

	public synchronized static void gameOver() {
		printMatrix();
		System.out.printf("\nGAME OVER! SCORE: %d\n",score);
		alive = false;
		System.exit(0);
	}


	public static class Ship implements Runnable {

		synchronized void moveShip(char d) {
			if (d=='D' || d=='d'&& pos[1] < 20) {
				matrix[pos[0]][pos[1]] = ' ';
				if (matrix[pos[0]][pos[1]+1] == '*') {
					gameOver();
				}
				matrix[pos[0]][++pos[1]] = '^';
			}
			else if (d=='A' || d=='a'&& pos[1] > 0) {
				matrix[pos[0]][pos[1]] = ' ';
				if (matrix[pos[0]][pos[1]-1] == '*') {
					gameOver();
				}
				matrix[pos[0]][--pos[1]] = '^';
			}
			/*else if (d=='S' || d=='s'&& pos[0] < 8) {
				matrix[pos[0]][pos[1]] = ' ';
				if (matrix[pos[0]+1][pos[1]] == '*') {
					gameOver();
				}
				matrix[++pos[0]][pos[1]] = '^';
			}
			else if (d=='W' || d=='w'&& pos[0] > 1) { 
				matrix[pos[0]][pos[1]] = ' ';
				if (matrix[pos[0]-1][pos[1]] == '*') {
					gameOver();
				}
				matrix[--pos[0]][pos[1]] = '^';
			}*/
		}
		

		public void run() {
			pos[0] = 8; pos[1] = 10;
			int done = 0;
			
			printMatrix();
			while (alive && temp != 'Q' && temp !='q') {
				input = input = scanner.nextLine();
				if (input.length()==1) {temp = input.charAt(0);}
				if (input.length()==1) {
					if (temp=='D' || temp=='A' || temp=='W' || temp=='S' ||
						temp=='d' || temp=='a' || temp=='w' || temp=='s') {
						moveShip(temp);
						//try { Thread.sleep(100); } catch(Exception e) {}
						printMatrix();
					}
					else if (temp=='K' || temp=='k') {
						Thread b = new Thread(new Bullet());
						b.start();
					}
				}
			}
			System.out.printf("YOU QUIT THE GAME.\nSCORE: %d\n",score);
			System.exit(0);
		}
	}

	public static class Swarm implements Runnable {

		synchronized void moveSwarm(char d) {

			char temp;
			int temp1;

			if (d=='R') {
				for (int i=0; i<10; i++) {
					for (int j=20; j>=0; j--) {
						if (matrix[i][j]=='*' && j<20) {
							matrix[i][j] = ' ';
							temp = matrix[i][j+1];
							temp1 = j+1;
							if(temp=='^') {
								matrix[i][temp1] = '*';
								gameOver();
							}
							/*else if (temp=='\'') {
								matrix[i][temp1]  = ' ';
								score += 5;
							}*/
							else {
								matrix[i][temp1] = '*';

							}
						}
					}
				}
			}
			else if (d=='L') {
				for (int i=0; i<10; i++) {
					for (int j=0; j<21; j++) {
						if (matrix[i][j]=='*' && j>0) {
							matrix[i][j] = ' ';
							temp = matrix[i][j-1];
							temp1 = j-1;
							if (temp=='^') {
								matrix[i][temp1] = '*';
								gameOver();
							}
							/*
							else if (temp=='\'') {
								matrix[i][temp1]  = ' ';
								score += 5;
							}*/
							else {
								matrix[i][temp1] = '*';	
							}
						}
					}
				}
			}
			else if (d=='D') {
				for (int i=8; i>=0; i--) {
					for (int j=0; j<21; j++) {
						if (matrix[i][j]=='*' && i<8) {
							matrix[i][j] = ' ';
							temp = matrix[i+1][j];
							temp1 = i+1;
							if(temp=='^') {
								matrix[temp1][j] = '*';
								gameOver();
							}
							/*else if (temp=='\'') {
								matrix[temp1][j]  = ' ';
								score += 5;
							}*/
							else {
								matrix[temp1][j] = '*';
							}
							
						}
					}
				}

				for (int i=0; i<9; i++) {
					if (x % 2==0) {
						if (i % 2 == 0) { matrix[1][i] = '*'; }
						else { matrix[1][i] = ' '; }
					}
					else { matrix[1][i] = '*'; }
				} level_++; x++;

				if (level_ == 7) {
					level++;
					level_ = 0;
				}
			}
			
		}

		synchronized Boolean isEmpty() {
			for (int i=1; i<8; i++) {
				for (int j=0; j<20; j++) {
					if( matrix[i][j]=='*') {
						return false;
					}
				}
			}
			return true;
		}

		public void run() {
			while (alive) {

				if (isEmpty()) {
					for (int i=6; i<15; i++) {
						matrix[1][i] = '*';
					}
					printMatrix();
					try { Thread.sleep(500); } catch(Exception e) {}
				}
				
				
				while (matrix[0][20] != '*' && matrix[1][20] != '*' && matrix[2][20] != '*'
					&& matrix[3][20] != '*' && matrix[4][20] != '*' && matrix[5][20] != '*'
					&& matrix[6][20] != '*' && matrix[7][20] != '*' && matrix[8][20] != '*'
					&& matrix[9][20] != '*') {
					moveSwarm('R'); printMatrix();
					try { Thread.sleep(500); } catch(Exception e) {}
				}
				while (matrix[0][0] != '*' && matrix[1][0] != '*' && matrix[2][0] != '*'
					&& matrix[3][0] != '*' && matrix[4][0] != '*' && matrix[5][0] != '*'
					&& matrix[6][0] != '*' && matrix[7][0] != '*' && matrix[8][0] != '*'
					&& matrix[9][0] != '*') {
					moveSwarm('L'); printMatrix();
					try { Thread.sleep(500); } catch(Exception e) {}
				}				
				moveSwarm('D'); printMatrix();
				try { Thread.sleep(500); } catch(Exception e) {}
			}
		}
	}

	public static class Bullet implements Runnable {

		volatile int i,j; // current position of bullet
		int i1, j1;

		synchronized void initBullet() {
			i = pos[0]-1;
		    j = pos[1];
		    matrix[i][j] = '\'';
		}

		synchronized int moveBullet() {
			i1 = i; j1 = j;
			if (matrix[i][j] == '*') {
				matrix[i1][j1] = ' ';
				score += 5;
				return 1;
			} 
			if (i != 1) {
				matrix[i][j] = ' ';
				matrix[--i][j] = '\'';
				return 0;
			} return 0;	
		}

		public void run() {
			initBullet();
			printMatrix();
			try { Thread.sleep(300);} catch(Exception e) {}

			while (i > 1) {
				if (moveBullet() == 0) {
					printMatrix();
					try { Thread.sleep(300);} catch(Exception e) {}
				}
				else {
					printMatrix();
					try { Thread.sleep(300);} catch(Exception e) {}
					break;
				}
			} 
			matrix[i][j] = ' ';
		}
	}

	public synchronized static void initMat() {
		matrix = new char[10][21];
		for (int i=0; i<10; i++) {
	 		for (int j=0; j<21; j++) {
	 			if (i==0 || i==9) matrix[i][j] = '=';
	 			else if (i==8 && j==10) matrix[i][j] = '^';
	 			else if (i==1 && (j>6 && j<14)) matrix[i][j] = '*';
	 			else if (i==2 && (j>5 && j<15)) matrix[i][j] = '*';
	 			else matrix[i][j] = ' ';
	 		}
	 	}
	}

	public synchronized static void printMatrix() {
		System.out.println("GALAGA\n");
		System.out.printf("LEVEL %d\n",level);
		System.out.printf("Score: %d\n",score);
		for (int i=0; i<10; i++) {
	 		for (int j=0; j<21; j++) {
	 			System.out.printf("%c", matrix[i][j]);
	 		} System.out.println();
	 	}
	 	System.out.println("\nMove(A/D)   Fire(K)   Quit(Q)\n");
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		initMat();

		ship.start();
		swarm.start();
	}
}