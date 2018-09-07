import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;

class Main {

	public static void main(String[] args) {

		Board initial = new Board(null);	
		initial.drawBoard();
		BFS breadth = new BFS(initial);
		System.exit(0);
	}
}

class BFS{
	
	Queue<Board> queue;
	TreeSet<Board> visited;
	ArrayList<byte[]> route;
	StateComparator comp = new StateComparator();
	Board finalBoard;
	Board vertex;
	Movement move;
	boolean isFound = false;
	int moveCount = 0;

	BFS(Board b){
		queue =  new LinkedList<Board>();
		route = new ArrayList<byte[]>();
		visited = new TreeSet<Board>(comp);
		queue.add(b);
		visited.add(b);
		
		while(!(queue.isEmpty()) && isFound == false){
			vertex = queue.poll();
			
				//find all neighbors
				for(int pos = 0; pos < 22; pos++){
					move = new Movement(vertex);
					move.tryMove(pos, 1);
					move.tryMove(pos, -1);
					for(int i = 0; i < move.ret.size(); i++){
						if(!(visited.contains(move.ret.get(i)))){
							queue.add(move.ret.get(i));
							visited.add(move.ret.get(i));
							if(move.ret.get(i).state[1] == -2){
								isFound = true;
								finalBoard = move.ret.get(i);
							}
						}
					}
				}
		}

		boolean go = true;
		while(go){
			
			route.add(finalBoard.state);
			moveCount++;					
				

			if(finalBoard.parent == null){
				go = false;
			}
			else
				finalBoard = finalBoard.parent;	
		}
				
		PrintWriter writer = null;
		try {
			writer= new PrintWriter("results.txt", "UTF-8");
				
		byte[] by;
		for(int i = route.size()-1; i >= 0; i--){
			 by = route.get(i);
			 for(int j = 0; j < 11; j++)
				 writer.print("("+by[2*j]+","+by[2*j+1]+") ");
			 writer.println();
		}
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			writer.close();
		}
		
	}
	
}

class Movement{
	
	Board toCheck;
	ArrayList<Board> ret;
	Movement(Board vertex){
		toCheck = vertex;
		ret = new ArrayList<Board>();
	}
	
	public void tryMove(int id, int direction){
		
		Board t = new Board(toCheck);
		t.state[id] +=direction;
		for(int i = 0; i < t.board.length; i++){
			for(int j = 0; j < t.board.length; j++)
				t.board[j][i] = false;
		}
		t.drawBoard();

		if(t.isStateValid(t)){
			ret.add(t);
			}

	}	
}

class Board {
	
	static boolean[][] board = new boolean[10][10];;
	byte[] state;
	Board parent;
	int takenSpaces;
	int drawnBlocks;
	
	Board(Board b){
		
		state = new byte[22];
		if(b != null)
			for(int i = 0; i < state.length; i++)
				state[i] = b.state[i];
			
		parent = b;

	}
	
	
	public void drawBoard(){
	
		for(int i = 0; i < 10; i++) { this.b(i, 0); this.b(i, 9); }
		for(int i = 1; i < 9; i++) { this.b(0, i); this.b(9, i); }
		b(1, 1); b(1, 2); b(2, 1);
		b(7, 1); b(8, 1); b(8, 2);
		b(1, 7); b(1, 8); b(2, 8);
		b(8, 7); b(7, 8); b(8, 8);
		b(3, 4); b(4, 4); b(4, 3);

		//red block
		shape(0, 1, 3, 2, 3, 1, 4, 2, 4);
		//green block
		shape(1, 1, 5, 1, 6, 2, 6);
		//navy block
		shape(2, 2, 5, 3, 5, 3, 6);
		//pink block
		shape(3, 3, 7, 3, 8, 4, 8);
		//yellow block
		shape(4, 4, 7, 5, 7, 5, 8);
		//tan block
		shape(5, 6, 7, 7, 7, 6, 8);
		//skyblue block
		shape(6, 5, 4, 5, 5, 5, 6, 4, 5);
		//green block
		shape(7, 6, 4, 6, 5, 6, 6, 7, 5);
		//cyan block
		shape(8, 8, 5, 8, 6, 7, 6);
		//blue block
		shape(9, 6, 2, 6, 3, 5, 3);
		//orange block
		shape(10, 5, 1, 6, 1, 5, 2);
		}
	
	
	public void b(int x, int y){
		if(this.board[x][y] == false){
			this.takenSpaces++;
		}
		this.board[x][y] = true;
		this.drawnBlocks++;	

	}

	public void shape(int id, int x1, int y1, int x2, int y2, int x3, int y3)
	{
		b(state[2 * id] + x1, state[2 * id + 1] + y1);
		b(state[2 * id] + x2, state[2 * id + 1] + y2);
		b(state[2 * id] + x3, state[2 * id + 1] + y3);
	}

	public void shape(int id, int x1, int y1, int x2, int y2,
		int x3, int y3, int x4, int y4)
	{
		shape(id, x1, y1, x2, y2, x3, y3);
		b(state[2 * id] + x4, state[2 * id + 1] + y4);
	}
	
	void printBoard(){

		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board.length; j++){
				if(board[j][i]){
					System.out.print("[-]");
				}
				else
					System.out.print("   ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public boolean isStateValid(Board check){	
		//if drawn blocks is equal to the amount of spaces used then the state is valid
		if(check.drawnBlocks == check.takenSpaces)
			return true;
		return false;
	}	
}

class StateComparator implements Comparator<Board>
{
	public int compare(Board a, Board b)
	{
		for(int i = 0; i < 22; i++)
		{
			if(a.state[i] < b.state[i])
				return -1;
			else if(a.state[i] > b.state[i])
				return 1;
		}
		return 0;
	}


}
