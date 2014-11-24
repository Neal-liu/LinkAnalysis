import java.io.*;              
import java.util.Scanner;

public class LinkAnalysis {

        private String dataPath = new String("/home/neal/workspace/LinkAnalysis/hw2dataset/");
        private static int graph;										// select which graph you want to analysis
        
        public LinkAnalysis(){
        
        }	
        public int getGraph( ){
        	return graph;
        }
        public String getDataPath( ){
        	return this.dataPath;
        }
        
        public void FileChoose(){
        	PageRank pr = new PageRank();
        	HITS hits = new HITS();
        	SimRank sr = new SimRank();
        	File source  = new File(dataPath);
        	File[] file = source.listFiles();
        	Scanner in = new Scanner(System.in);
        	int k = 1, method=0;
        	
	        System.out.println("Please choose File : ");
	        while(k < 11){
	       		for(int i=0 ; i<file.length ; i++){
	       			if(file[i].getName().startsWith("graph_"+ k  + ".txt" )){
	       				System.out.println( k + ". " + file[i].getName() );
	       			}
	       		}
	       		k++;
	       	}
	        graph = in.nextInt();   
	        File fx = new File(dataPath+"/graph_"+graph+".txt");
	        if(!fx.exists()){
	        	System.out.println("This File does not exist !!");
	        	in.close();
	        	return;
	        }
	        	
	        System.out.println("Please choose methods for link analysis : ");
	        System.out.println("1. HITS ");
	        System.out.println("2. PageRank");
	        System.out.println("3. SimRank");
	        method = in.nextInt();
	        if(method == 1)
	        	hits.Hits();
	        else if(method == 2)
	        	pr.Rank();
	        else if (method == 3)
	        	sr.Sim();
	        else
	        	System.out.println("Error choice !!");	       
	        
	       in.close();
        }

        public static void main(String args[]) throws IOException{
        		LinkAnalysis analysis = new LinkAnalysis();
//        		GetIMDB getData = new GetIMDB();
        		LPmodel lp = new LPmodel();
        		lp.RandomCreate();
//        		getData.GetData();	
        		analysis.FileChoose();
        		

        }

}

