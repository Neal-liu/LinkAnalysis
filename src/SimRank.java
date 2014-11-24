import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Scanner;
import java.util.Set;


public class SimRank {

	private IdentityHashMap<String, String> LinkHash;
	private HashMap<Integer, HashMap<Integer, Double>> OuterSimHash;
	private int pair1;
	private int pair2;
	private double DecayFactor = 0.85;
	private int[ ] InDegree;
    private int TotalNodes;
		
	public void Sim( ){
		
		Scanner in = new Scanner(System.in);
		LinkAnalysis getLink = new LinkAnalysis();
		String  thisLine;
		File source  = new File(getLink.getDataPath());
    	File[] file = source.listFiles();
        LinkHash = new IdentityHashMap<String, String>( );
        Set<String> hs = new HashSet<String>();        
        OuterSimHash = new HashMap<Integer, HashMap<Integer, Double>>( );
        
		try{
			for(int i=0 ; i<file.length ; i++){
	    		if( file[i].getName().startsWith( String.valueOf("graph_" + getLink.getGraph() + ".txt"  ) ) ){
	                    BufferedReader br  = new BufferedReader(new FileReader( file[i] ));          	
	                    while( (thisLine = br.readLine()) != null ){
	                   		String[] vArray = thisLine.split(",");
	                   		LinkHash.put(vArray[0], vArray[1]);
	                   		hs.add(vArray[0]);
	                   		hs.add(vArray[1]);
	                   	}
	               		System.out.println();
	               		br.close();
	    		}
	    	}
			TotalNodes = hs.size();
			InDegree = new int[TotalNodes];
			int[][] Rank = new int[TotalNodes][TotalNodes];
			
			for( String name:LinkHash.keySet() ){
        		Rank[Integer.valueOf(name)-1][Integer.valueOf(LinkHash.get(name))-1] = 1;
        	}
			
			/* 計算每個 node 的 InDegree */
        	for(int i=0;i<Rank.length;i++){
        		for(int j=0;j<Rank[i].length;j++){
        			System.out.print(Rank[i][j] +" ");
        			if(Rank[i][j] == 1){
        				InDegree[j]++;
                   	}
        		}
        		System.out.println();
        	}
        	/* Initialize SimRank value */
        	for(int i=0 ; i<TotalNodes ; i++){
        		HashMap<Integer, Double> InnerSimHash = new HashMap<Integer, Double>( );
        		for(int j=0 ; j<TotalNodes ; j++){
        			if(i == j)
        				InnerSimHash.put(j, 1.0);
        			else
        				InnerSimHash.put(j, 0.0);
        		}  		
        		OuterSimHash.put(i, InnerSimHash);     
        	}
        	
        	int iterator = 0;
        	while(iterator != -1){
	        	double SeqAdd = 0;
	        	for(int i=0 ; i<TotalNodes ; i++){				// for a node
	        		if(InDegree[i] == 0)
	        			continue;
	        		HashMap<Integer, Double> UpdateSim = OuterSimHash.get(i);
	        		for(int j=0 ; j<TotalNodes ; j++){			// for b node
	        			if(InDegree[j] == 0)
	            			continue;
	        			SeqAdd = 0;
	        			for(int k=0 ; k<TotalNodes ; k++){							// for a's InNeighbor
	        				HashMap<Integer, Double> getIn; 
	        				if(Rank[k][i] == 1){
	        					getIn = OuterSimHash.get(k);
	        					for(int m=0 ; m<TotalNodes ; m++){				// for b's InNeighbor
	        						if(Rank[m][j] == 1){
	        							SeqAdd += getIn.get(m);
	        						}
	        					}
	        				}		
	        			}
	        			SeqAdd = SeqAdd * DecayFactor/(InDegree[i] * InDegree[j] ) ;			// calculate SimRank
	        			if(UpdateSim.get(j) == SeqAdd)
	        				iterator = -1;
	        			else
	        				UpdateSim.put( j , SeqAdd );																		// update SimRank with node( i , j )
	        		}
	        	}
        	}        
			
			System.out.println("Please choose two nodes to calculate similarity : ");
			for(int i=0 ; i<TotalNodes ; i++)
				System.out.printf("%d. node %d\t", i+1, i+1);
			System.out.println("\nPair 1 : ");
			pair1 = in.nextInt();
			if(pair1<=0 || pair1 >TotalNodes ){
				System.out.println("No such file exists !!");
				return;
			}
			System.out.println("Pair 2 : ");
			pair2 = in.nextInt();
			if(pair2<=0 || pair2 >TotalNodes ){
				System.out.println("No such file exists !!");
				return;
			}
			
			HashMap<Integer, Double> test = OuterSimHash.get(pair1-1);
        	System.out.printf("\nS(%d,%d) : %.8f \n",pair1, pair2,  test.get(pair2-1) );
			
	//		System.out.print(pair1 +" " +pair2);
		}catch(IOException e){
			e.printStackTrace();
		}

	}
	
}
