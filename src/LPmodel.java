import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;


public class LPmodel {
	
	private String Graph7Path = "/home/neal/workspace/LinkAnalysis/hw2dataset/graph_7.txt";
	private String Graph8Path = "/home/neal/workspace/LinkAnalysis/hw2dataset/graph_8.txt";
	private int Graph7Max = 50;
	private int Graph8Max = 100;
	
	public void RandomCreate(){
		
		Random rand = new Random();										// random seed
		int randEdge7 = rand.nextInt(Graph7Max*2)+1;			// random number of edge
		int randEdge8 = rand.nextInt(Graph8Max*2)+1;			// random number of edge
		
		try{
			PrintWriter Graphwriter7 = new PrintWriter(Graph7Path, "UTF-8");
			PrintWriter Graphwriter8 = new PrintWriter(Graph8Path, "UTF-8");
			
			/* random create nodes link for graph_7.txt  */
			for(int i=0 ; i<randEdge7 ; i++){
				int randNode1 = rand.nextInt(Graph7Max)+1;
				int randNode2 = rand.nextInt(Graph7Max)+1;
				if(randNode1 != randNode2)
					Graphwriter7.println(randNode1+","+randNode2);
			}
			
			/* random create nodes link for graph_8.txt  */
			for(int i=0 ; i<randEdge8 ; i++){
				int randNode1 = rand.nextInt(Graph8Max)+1;
				int randNode2 = rand.nextInt(Graph8Max)+1;
				if(randNode1 != randNode2)
					Graphwriter8.println(randNode1+","+randNode2);
			}
			
			Graphwriter7.close();
			Graphwriter8.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
}
