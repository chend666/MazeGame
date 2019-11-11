//Name: Dian Chen & Yang Gao

public class block {

		int size;  //size of each block
		
		boolean[] wall = new boolean[4]; //edge of each block
		boolean ifOccupy = false;  //if the block has been touched
		int x;  //x-coordinate of the block
		int y;  //y-coordinate of the block

		
		public block(int x, int y, int size) {
			this.x = x;
			this.y = y;
			this.size = size;
			
			//Initially set block with four concrete edges
			wall[0] = true;  
			wall[1] = true;
			wall[2] = true;
			wall[3] = true;
			
			
		}
		
		
}
