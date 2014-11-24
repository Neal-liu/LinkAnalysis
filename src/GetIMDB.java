import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;


public class GetIMDB {

	private String imdbPath = "/home/neal/workspace/LinkAnalysis/Top-250-IMDB.html";					// source code for top 250 movies in IMDB 
	private String MoviePath = "/home/neal/workspace/LinkAnalysis/Top-250-Movie.txt";						// parse source code to each movie
	private String GraphPath = "/home/neal/workspace/LinkAnalysis/hw2dataset/graph_9.txt";				// Director points to Actors
	private String PeoplePath = "/home/neal/workspace/LinkAnalysis/People.txt";									// Each person has each number (each node means each person)
	private HashMap<String, Integer> PeopleHash;
	
	public String GetPeople( ){
		return PeoplePath;
	}
	
	public void GetData( ){
		
		File imdb = new File(imdbPath);
		String thisLine, Director="", Actor="";
		PeopleHash = new HashMap<String, Integer>( );
		int peopleNum = 1;
		
		try{
			PrintWriter Moviewriter = new PrintWriter(MoviePath, "UTF-8");
			PrintWriter Peoplewriter = new PrintWriter(PeoplePath, "UTF-8");
			PrintWriter Graphwriter = new PrintWriter(GraphPath, "UTF-8");
			BufferedReader br  = new BufferedReader(new FileReader( imdb ));          	
			while( (thisLine = br.readLine()) != null ){
				if( thisLine.startsWith("title") && thisLine.endsWith("</a>") && thisLine.length() > 23 ){
					Moviewriter.println(thisLine);
					thisLine = thisLine.substring( thisLine.indexOf("\"")+1 );
					thisLine = thisLine.substring( 0, thisLine.indexOf("\"") );
					thisLine = thisLine.replace(" (dir.)", "");
					
					String[] pArray = thisLine.split(",");
					for(int i=0 ; i<pArray.length ; i++){
						if(pArray[i].indexOf(" ") == 0)
							pArray[i] = pArray[i].substring(1);
						if(!PeopleHash.containsKey(pArray[i])){
							Peoplewriter.println( peopleNum + ","+pArray[i] );
							System.out.println( peopleNum + ", "+pArray[i] );
							PeopleHash.put(pArray[i], peopleNum);
							peopleNum++;
						}
						if(i == 0)
							Director = pArray[0];
						else{
							Actor = pArray[i];
							Graphwriter.println(PeopleHash.get(Director) +","+PeopleHash.get(Actor) );
						}
							
					}
//					System.out.println(thisLine);
				}
			}
			peopleNum -= 1;
			System.out.println("peopleNum : " + peopleNum);
	   		System.out.println();
	   		br.close();
	   		Moviewriter.close();
	   		Peoplewriter.close();
	   		Graphwriter.close();
	   		
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
	
