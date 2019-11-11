//Name: Dian Chen & Yang Gao

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class DrawMaze extends JFrame implements KeyListener {

	private static final long serialVersionUID = 1L;
	
	
	boolean ifArrive = false;  //if the player reaches the exit of the maze
	int numCol = 20;  //number of blocks in column
	int numRow = 20;  //number of blocks in row
	int scale = 30;   //size of each block
	int originalx = scale/4;
	int originaly = scale/4;
	int x = originalx;//initial position of the player
	int y = originaly;
	boolean canPass = true;  //if players can move, prevent it from through walls
	block[][] grid = new block[numRow][numCol];  //the grid
	int[] pos = new int[2];  //counter to record current position while searching
	
	
	public DrawMaze() {

		
		pack();
		setSize(new Dimension((numRow+1)*scale, (numCol+3)*scale));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		runAll();
		
		
	}

	public static void main(String arg[]) {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				DrawMaze a = new DrawMaze();
				a.addKeyListener(a);
			}
		});
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
		//press ENTER to restart the game
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			
			runAll();
			
		}
		
		//check if player reaches the exit
		if(x>numRow*scale-10 || y>numCol*scale-10) {
			if(ifArrive == false) {
			
			this.setVisible(false);
			this.print(this.getGraphics());
			this.setVisible(true);
			this.ifArrive = true;
			
			}
			return;
		}
		
		//check if player can move
		if(canPass) {
		int move = 0;
		 pos[0] = x;
		  pos[1] = y;
		if(e.getKeyCode() == KeyEvent.VK_DOWN){
			y+=5;
			move = 1;  //down

		}else if(e.getKeyCode() == KeyEvent.VK_LEFT){
			x-=5;
			move = 2; //left
		}else if(e.getKeyCode() == KeyEvent.VK_UP){
			y-=5;
			move = 3; //up
		}else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			x+=5;
			move = 4; //right
	}
  if(ifPass(x,y,move,grid)) {
	  this.repaint();
  }else {
	  
	  canPass = false;
  }
	
		}else {
			
			
			x = pos[0];
			y = pos[1];
			int move = 0;
			if(e.getKeyCode() == KeyEvent.VK_DOWN){
				y+=5;
				move = 1;  //down

			}else if(e.getKeyCode() == KeyEvent.VK_LEFT){
				x-=5;
				move = 2; //left
			}else if(e.getKeyCode() == KeyEvent.VK_UP){
				y-=5;
				move = 3; //up
			}else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
				x+=5;
				move = 4; //right
		}
			  if(ifPass(x,y,move,grid)) {
				  canPass = true;
				  this.repaint();
			  }
			
			
			
			
		}
		}
	

public void runAll() {
	
	boolean done = false;
	x = originalx;
	y = originaly;
	ifArrive = false;
	//numCol = 20;
	//numRow = 20;
	//scale = 30;
	canPass = true;
	grid = new block[numRow][numCol];
	pos = new int[2];

	
	//create the empty grid
	for(int x = 0;x<numCol;x++) {
		for(int y = 0;y<numRow;y++) {
			grid[x][y] = new block(x*scale,y*scale,scale);
		}
	}
	
	//store all blocks which are already visited
	ArrayList<block> occupied = new ArrayList<block>();
	//start with first one
	occupied.add(grid[0][0]);
	grid[0][0].ifOccupy = true;
	generator agent = new generator(grid, occupied,numRow, numCol,scale);
	
	
	if(occupied.size() !=0) {
		while(!done) {

			
			
				agent.generateMaze();
			
			for(int i = 0;i<occupied.size();i++) {
				
				//check each block close to the tested on (protected against out of bound array)
				if(grid[constraint(occupied.get(i).x/scale+1,numCol-1)][constraint(occupied.get(i).y/scale,numRow-1)].ifOccupy &&
						grid[constraint(occupied.get(i).x/scale-1,numCol-1)][constraint(occupied.get(i).y/scale,numRow-1)].ifOccupy &&
						grid[constraint(occupied.get(i).x/scale,numCol-1)][constraint(occupied.get(i).y/scale+1,numRow-1)].ifOccupy &&
						grid[constraint(occupied.get(i).x/scale,numCol-1)][constraint(occupied.get(i).y/scale-1,numRow-1)].ifOccupy) {
					
					occupied.remove(i);
					
					
				}
				
			}
			
			if(occupied.size() == 0) { //when there is no block which is not visited, maze finished
				done = true;
				break;
			}
		}
	}

	JPanel p = new JPanel() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			
			////
			
			//print out the maze
			for(int x = 0;x<numCol;x++) {
				for(int y = 0;y<numRow;y++) {
					
					block b = grid[x][y];
					if(x == 0) {
					}
					if(b.wall[0]) {
						Shape a = new Line2D.Double(b.x, b.y, b.x+b.size, b.y);
						g2.draw(a);
					}
					if(b.wall[1]) {
						
						Shape a = new Line2D.Double(b.x+b.size, b.y, b.x+b.size, b.y+b.size);
						g2.draw(a);
					}
					if(b.wall[2]) {
						Shape a = new Line2D.Double(b.x, b.y+b.size, b.x+b.size, b.y+b.size);
						if(y == numRow-1 && x == numCol-1) {
							
						}else {
							g2.draw(a);
						}
						
					}
					if(b.wall[3]) {
						Shape a = new Line2D.Double(b.x, b.y, b.x, b.y+b.size);
						g2.draw(a);
					}
				}
			}
		
			//////////////////////
			
			//design the game
			g.setColor(Color.ORANGE);
			g.fillRect(2, 1, scale-2, scale-2);
			g.setColor(new Color(102,255,102));
			g.fillRect((numRow-1)* scale, (numCol-1)*scale, scale, scale);
			if(x>numRow*scale-10 || y>numCol*scale-10) {
				g.setColor(Color.PINK);
				Font font1 = new Font("Verdana", Font.BOLD, 40);
				g.setFont(font1);
				g.drawString("You Win", numRow*scale/3+30, numCol*scale/3+50);
				g.setColor(Color.BLACK);
				Font font2 = new Font("Verdana", Font.ITALIC, 35);
				g.setFont(font2);
				g.drawString("Press ENTER to restart!", numRow*scale/6+10, numCol*scale/3+100);
			}else {
				g.setColor(Color.red);
				g.fillOval(x,y, scale/3, scale/3);
				pos[0] = x;
				pos[1] = y;
				
			}
		}
	};
	this.getContentPane().add(p);
	
}

//make sure we will not reach the block which is out of bound
public static int constraint(int x, int y) {
		
		if(x<0) {
			x = 0;
		}
		if(x>y) {
			x = y;
		}
		
		
		
		return x;
	}

public boolean ifPass(int x, int y,int move, block[][] grid) {
	
	boolean ifpass = true;
	int posx = 0;  //position of player before moving
	int posy = 0;
	int edgex = x; 
	int edgey = y;
	
	
	//check if player tries to move out of the grid
	if(edgex<0 || edgey<0) {
		return false;
	}
	if(move == 1) { //down
		y = y+8;
		posx = x/scale;
		posy = (y-5)/scale;
	}else if(move == 2) { //left
		
		posx = (x+5)/scale;
		posy = y/scale;
	}else if(move == 3) { //up
		
		posx = x/scale;
		posy = (y+5)/scale;
	}else if(move == 4) {  //right
		x = x+8;
		posx = (x-5)/scale;
		posy = y/scale;
	}
	
	
	x = x/scale;  //position of player after moving
	y = y/scale;
	
	//check if player try to pass through a wall
	if(posx>=0 && posy>=0 && posx<numRow-1 && posy<numCol-1) {
		
		if(x == posx && y == posy) {
			
			return true;
			
			
		}else if(x > posx) {
		 
			if(grid[posx][y].wall[1] == false && grid[x][y].wall[3] == false) {
				return true;
			}else {
				return false;
			}
			
		}else if(x < posx){
			
			if(grid[posx][y].wall[3] == false && grid[x][y].wall[1] == false) {
				return true;
			}else {
				return false;
			}
		}else if(y>posy) {
			if(grid[posx][y].wall[0] == false && grid[x][posy].wall[2] == false) {
				return true;
			}else {
				return false;
			}
		}else if(y<posy) {
			if(grid[posx][y].wall[2] == false && grid[x][posy].wall[0] == false) {
				return true;
			}else {
				return false;
			}
		}else if(posx !=x && posy!=y) {
			return false;
		}
		
		
		
	}
	
	
	return ifpass;
}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(x>numRow*scale-10 || y>numCol*scale-10) {
			if(ifArrive == false) {
			System.out.println("You Win");
			
			this.setVisible(false);
			this.print(this.getGraphics());
			this.setVisible(true);
			this.ifArrive = true;
			
			}
			return;
		}
		
	}

}
