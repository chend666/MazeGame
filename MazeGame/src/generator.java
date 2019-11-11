//Name: Dian Chen & Yang Gao

import java.util.ArrayList;

public class generator {

	int[] pos = new int[2];
	int numCol;
	int numRow;
	int scale;
	block[][] grid = new block[numRow][numCol];
	
	ArrayList<block> occupied = new ArrayList<block>();
	int count = 0;  //counter used to break a tie
	boolean done;  //if visit all block
	
	
	
	public generator(block[][] grid, ArrayList<block> occupied, int numRow, int numCol, int scale) {
		this.grid = grid;
		this.occupied = occupied;
		this.pos[0] = 0;
		this.pos[1] = 0;
		this.numCol = numCol;
		this.numRow = numRow;
		this.scale = scale;
		done = false;
	}
	
	//prevent from out of bound
	public int constraint(int x, int y) {
		
		if(x<0) {
			x = 0;
		}
		if(x>y) {
			x = y;
		}
		
		
		
		return x;
	}
	
	//main function to generator
	//using recursive backtracking algorithm 
	public boolean generateMaze() {
		
		while(true) {
			
			//randomly choose a direction
			int current = (int) Math.floor((Math.random() * 4));
			
			
			//try to move to that direction
			if(current == 0 && pos[0] < numCol- 1) { //Right
			
				if(grid[pos[0]+1][pos[1]].ifOccupy == false) {
					grid[pos[0]][pos[1]].wall[1] = false;  //remove the wall
					grid[pos[0]+1][pos[1]].wall[3] = false;
					pos[0]++; //move generator to next postion
					break;
				}
			}
			else if(current == 1 && pos[1] < numRow -1) {//Down
				if(grid[pos[0]][pos[1]+1].ifOccupy == false) {
					grid[pos[0]][pos[1]].wall[2] = false;
					grid[pos[0]][pos[1]+1].wall[0] = false;
					pos[1]++;
					break;
				}
				
			}
			else if(current == 2 && pos[0] > 0) { //Left
				
				if(grid[pos[0]-1][pos[1]].ifOccupy == false) {
					grid[pos[0]][pos[1]].wall[3] = false;
					grid[pos[0]-1][pos[1]].wall[1] = false;
					pos[0]--;
					break;
				}
			}
			
			else if(current == 3 && pos[1] > 0) { //Up
				if(grid[pos[0]][pos[1]-1].ifOccupy == false) {
					grid[pos[0]][pos[1]].wall[0] = false;
					grid[pos[0]][pos[1]-1].wall[2] = false;
					pos[1]--;
					
					break;
				}
			}
			
			//if no direction is available to move
			//randomly choose a block which is not visited yet(back to previous state)
			if(grid[constraint(pos[0]+1,numCol-1)][constraint(pos[1],numRow-1)].ifOccupy && 
					grid[constraint(pos[0]-1,numCol-1)][constraint(pos[1],numRow-1)].ifOccupy && 
					grid[constraint(pos[0],numCol-1)][constraint(pos[1]+1,numRow-1)].ifOccupy &&
					grid[constraint(pos[0],numCol-1)][constraint(pos[1]-1,numRow-1)].ifOccupy) {
				
				int newOne = (int)Math.floor(Math.random() * occupied.size());
				
				pos[0] = occupied.get(newOne).x/scale;
				pos[1] = occupied.get(newOne).y/scale;
				count++;
				if(count>50) {   //if can't find an available block
					return true;
				}
				
			}
			
			
			
		}
		//add block to a list of all visited blocks
		occupied.add(grid[pos[0]][pos[1]]);
		grid[pos[0]][pos[1]].ifOccupy = true;
		
		return done;
	}
	
}
