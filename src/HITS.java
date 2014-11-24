import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class HITS {

	private IdentityHashMap<String, String> LinkHash;
	private double [ ][ ] AuthHubs;
	private int TotalNode;
	private HashMap<Integer, Double> AuthHash = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> HubsHash = new HashMap<Integer, Double>();
	
	public void Hits( ){
	
		LinkAnalysis getLink = new LinkAnalysis();
		String  thisLine;
		int root = 0;
		Scanner in = new Scanner(System.in);
		File source  = new File(getLink.getDataPath());
    	File[] file = source.listFiles();
        LinkHash = new IdentityHashMap<String, String>( );
        Set<String> hsBase = new HashSet<String>();
        Set<String> hsRoot = new HashSet<String>();
       
        try{
        	
        	for(int i=0 ; i<file.length ; i++){
        		if( file[i].getName().startsWith( String.valueOf("graph_" + getLink.getGraph() + ".txt" ) ) ){
                        BufferedReader br  = new BufferedReader(new FileReader( file[i] ));          	
                        while( (thisLine = br.readLine()) != null ){
                       		String[] vArray = thisLine.split(",");
                   //    		LinkHash.put(vArray[0], vArray[1]);
                       		hsRoot.add(vArray[0]);
                       		hsRoot.add(vArray[1]);
                       	}
                   		System.out.println();
                   		br.close();
        		}
        	}
        	
        	TotalNode = hsRoot.size();
        	System.out.println("Please choose root :");
        	for(int i=0 ; i<TotalNode ; i++)
				System.out.printf("%d. node %d\t", i+1, i+1);
			System.out.println("\nRoot : ");
			root = in.nextInt();
			if(root<=0 || root >TotalNode){
				System.out.println("No such node exists !!");
				return;
			}
			
			/* If graph size is not large than 10 nodes, then calculate whole graph.
			 * Else calculate the BaseSet. */
			if(TotalNode > 10){
				/* search RootSet */
				hsRoot.clear();
				for(int i=0 ; i<file.length ; i++){
	        		if( file[i].getName().startsWith( String.valueOf("graph_" + getLink.getGraph()) ) ){
	                        BufferedReader br  = new BufferedReader(new FileReader( file[i] ));          	
	                        while( (thisLine = br.readLine()) != null ){
	                       		String[] vArray = thisLine.split(",");
	                       		if( vArray[0].matches( String.valueOf(root) ) || vArray[1].matches(String.valueOf(root)) ){
		                       		hsRoot.add(vArray[0]);
		                       		hsRoot.add(vArray[1]);
	                       		}
	                       	}
	                   		System.out.println();
	                   		br.close();
	        		}
	        	}
	
				/* search BaseSet */
				for(int i=0 ; i<file.length ; i++){
	        		if( file[i].getName().startsWith( String.valueOf("graph_" + getLink.getGraph()) ) ){
	                        BufferedReader br  = new BufferedReader(new FileReader( file[i] ));          	
	                        while( (thisLine = br.readLine()) != null ){
	                       		String[] vArray = thisLine.split(",");
	                       		
	                       		for( String s : hsRoot ){
	                       			if(vArray[0].matches(s) || vArray[1].matches(s) ){
	                               		LinkHash.put(vArray[0], vArray[1]);
			                       		hsBase.add(vArray[0]);
			                       		hsBase.add(vArray[1]);
	                       			}
	                       		}
	                       	}
	                   		br.close();
	        		}
	        	}
			}
			else{														// whole graph is not large than 10 nodes
				for(int i=0 ; i<file.length ; i++){
	        		if( file[i].getName().startsWith( String.valueOf("graph_" + getLink.getGraph()) ) ){
	                        BufferedReader br  = new BufferedReader(new FileReader( file[i] ));          	
	                        while( (thisLine = br.readLine()) != null ){
	                       		String[] vArray = thisLine.split(",");
	                       		LinkHash.put(vArray[0], vArray[1]);
	                       		hsBase.add(vArray[0]);
	                       		hsBase.add(vArray[1]);
	                       	}
	                   		br.close();
	        		}
	        	}
			}
        	
        	AuthHubs = new double[ TotalNode ][2];
        	int[][] Rank = new int[TotalNode][TotalNode];
            double auth = 0, hubs=0, norm = 0;
            int con = 0;
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(8);
        	
            for( String name:LinkHash.keySet() )
            	Rank[Integer.valueOf(name)-1][Integer.valueOf(LinkHash.get(name))-1] = 1;
            
            for(int i=0 ; i<TotalNode ; i++){
            	AuthHubs[i][0] = 1;
            	AuthHubs[i][1] = 1;
            }
            
            /* code for calculating Authority and Hubs for each node */
            while(con != 10){
	            /* calculate Authority */
	            norm = 0;
	            for(int i=0 ; i<Rank.length ; i++){
	            	auth = 0;
	            	for(int j=0 ; j<Rank[i].length ; j++){
	            		if(Rank[j][i] == 1)
	            			auth += AuthHubs[j][1];
	            	}
	            	AuthHubs[i][0] = auth;
	            	norm += Math.pow(auth, 2);
	            }
	            norm = Math.sqrt(norm);
	            for(int i=0 ; i<Rank.length ; i++){
	            	AuthHubs[i][0] = AuthHubs[i][0]/norm;
	            	AuthHubs[i][0] = Double.parseDouble(nf.format(AuthHubs[i][0]));
	            }
	            
	            /* calculate Hubs */
	            norm=0;
	            for(int i=0 ; i<Rank.length ; i++){
	            	hubs = 0;
	            	for(int j=0 ; j<Rank[i].length ; j++){
	            		if(Rank[i][j] == 1)
	            			hubs += AuthHubs[j][0];
	            	}
	            	AuthHubs[i][1] = hubs;
	            	norm += Math.pow(hubs, 2);
	            }
	            norm = Math.sqrt(norm);
	            for(int i=0 ; i<Rank.length ; i++){
	            	AuthHubs[i][1] = AuthHubs[i][1]/norm;
	            	AuthHubs[i][1] = Double.parseDouble(nf.format(AuthHubs[i][1]));
	            }
	            
	            con++;
            }

            System.out.println("\t Authority \t Hubs");
            for(int i=0 ; i<Rank.length ; i++){
            	if(AuthHubs[i][0] != 0 || AuthHubs[i][1] != 0){
            		System.out.printf( "Node %d : \n\t %.8f \t %.8f \n", i+1, AuthHubs[i][0], AuthHubs[i][1] );
            		AuthHash.put(i+1, AuthHubs[i][0]);
            		HubsHash.put(i+1, AuthHubs[i][1]);
            	}
            }
            
            SortHits();
            
        }catch(IOException e){
        	e.printStackTrace();
        }
	
	}
	
	/* Sort Authority and Hubs and extract top 5 */
	public void SortHits( ){
	
		int rank=1;
		List<Map.Entry<Integer, Double>> list_auth = new ArrayList<Map.Entry<Integer, Double>>( AuthHash.entrySet() );
		List<Map.Entry<Integer, Double>> list_hubs = new ArrayList<Map.Entry<Integer, Double>>( HubsHash.entrySet() );
		LinkAnalysis getG = new LinkAnalysis();
		PageRank getN = new PageRank();
		
		// Authority : 由大到小作排序 
		Collections.sort(list_auth, new Comparator<Map.Entry<Integer, Double>>( ) {
        	public int compare(Map.Entry<Integer, Double> entry1, Map.Entry<Integer, Double> entry2){
        		return Double.compare(entry2.getValue(), entry1.getValue() );					
        	}
        });
        System.out.println("\nAuthority Ranking for Top 5: ");
        for(Map.Entry<Integer, Double> entry:list_auth){
        	rank ++;
        	if(getG.getGraph() == 9){
        		String people = getN.PrintName(entry.getKey());
        		System.out.printf("\t%s : %.8f \n", people, entry.getValue());
        	}
        	else
       		System.out.printf("\tNode %d : %.8f \n", entry.getKey(), entry.getValue());
        	if(rank == 5)
        		break;
        }
        
     // Hubs : 由大到小作排序 
     	Collections.sort(list_hubs, new Comparator<Map.Entry<Integer, Double>>( ) {
           	public int compare(Map.Entry<Integer, Double> entry1, Map.Entry<Integer, Double> entry2){
           		return Double.compare(entry2.getValue(), entry1.getValue() );					
           	}
     	});
        System.out.println("Hubs Ranking for Top 5: ");
        rank = 1;
        for(Map.Entry<Integer, Double> entry:list_hubs){
        	rank++;
        	if(getG.getGraph() == 9){
        		String people = getN.PrintName(entry.getKey());
        		System.out.printf("\t%s : %.8f \n", people, entry.getValue());
        	}
        	else
        		System.out.printf("\tNode %d : %.8f \n", entry.getKey(), entry.getValue());
        	if(rank == 5)
        		break;
        }
        
	}
	
}
