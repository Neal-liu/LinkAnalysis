import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Set;


public class PageRank {
	
	private IdentityHashMap<String, String> LinkHash;
    private double[ ] PR;								// store every node's PageRank
    private double Damping = 0.15;			// damping factor
    private int[ ][ ] InOutDegree; 					// store every node's indegree and outdegree    
    
    
	public void Rank( ){
		
        LinkAnalysis getLink = new LinkAnalysis();
		String  thisLine;
		int con = 10;
		File source  = new File(getLink.getDataPath());
    	File[] file = source.listFiles();
        LinkHash = new IdentityHashMap<String, String>( );
        Set<String> hs = new HashSet<String>();           
       
        try{ 
        	for(int i=0 ; i<file.length ; i++){
        		if( file[i].getName().startsWith( String.valueOf("graph_"+getLink.getGraph() + ".txt" ) ) ){
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
        	
        	InOutDegree = new int[hs.size()][2];
        	PR = new double[hs.size()];
        	double[] SPR = new double[hs.size()];
        	int[][] Rank = new int[hs.size()][hs.size()];
        	double sum = 0;
        	double[] temp = new double[hs.size()];
        	NumberFormat nf = NumberFormat.getInstance();
        	nf.setMaximumFractionDigits(8);
        	
        	for( String name:LinkHash.keySet() ){
        		Rank[Integer.valueOf(name)-1][Integer.valueOf(LinkHash.get(name))-1] = 1;
        	}
                
        	/* 計算每個 node 的 indegree and outdegree */
        	for(int i=0;i<Rank.length;i++){
        		for(int j=0;j<Rank[i].length;j++){
        			System.out.print(Rank[i][j] +" ");
        			if(Rank[i][j] == 1){
        				InOutDegree[i][1]++;
        				InOutDegree[j][0]++;
                   	}
        		}
        		System.out.println();
        	}
                
        	for(int i=0;i<PR.length;i++)
        		PR[i] = 0;
                
        	/*pagerank 的算法 , 始其趨近於收斂*/
            while(con != -1){	
            	for(int i=0;i<Rank.length;i++){
            		for(int j=0;j<Rank[i].length;j++){
            			if(Rank[j][i] == 1)
            				sum += PR[j]/InOutDegree[j][1];
            		}
            		temp[i] = Damping/hs.size() + (1-Damping)*sum;
            		temp[i] = Double.parseDouble(nf.format(temp[i]));
            		if(PR[i] != temp[i])
            			PR[i] = temp[i];
            		else {
            			System.out.println("con = " + con);
            			con = -2;
            		}
            		sum = 0;
            	}
            	con ++;
            }
            System.arraycopy(PR, 0, SPR, 0, PR.length);
            Arrays.sort(SPR);
            double tmp=0;
            
            System.out.println("The PageRank ranking is : ");
            for(int i=PR.length-1 ; i>=0 ; i--){
            	if(tmp != SPR[i]){
            		for(int j=0 ; j<PR.length ; j++){
            			if(SPR[i] == PR[j]){
            				if(getLink.getGraph() == 9){
            					String people = PrintName(j+1);
            					System.out.printf("%s : \n\t%.8f\n", people, SPR[i]);
            				}
            				else
            					System.out.printf("Node %d : \n\t%.8f\n", j+1, SPR[i]);
            			}
            				
            		}
            	}
            	tmp = SPR[i];
            	if( (PR.length-i) == 10 )
            		break;
            }

        }catch(IOException e){
        	e.printStackTrace();
        }
        
	}
	
	public String PrintName(int node){
		
		GetIMDB gi = new GetIMDB();
		File f9 = new File(gi.GetPeople());
		String thisLine;

		try{
			BufferedReader br  = new BufferedReader(new FileReader( f9 ));
			while( (thisLine = br.readLine()) != null ){
				if( thisLine.startsWith(String.valueOf(node)) ){
					String[] pArray = thisLine.split(",");
					return pArray[1];
				}
			}
			br.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return "No people";
	}
	
	
}
