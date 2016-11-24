package logic;

public class RabinKarp{
	
	//find the number of matches
	public int match(String txt, String ptn){
		String ret=""; // variable to store match positions
		txt = txt.replaceAll("\\s+", "");
		ptn = ptn.replaceAll("\\s+", "");
		int n=txt.length();	//get the length of the given text
		int m=ptn.length();	//get the length of the pattern given
		int hashOfPatt= ptn.hashCode(); //get the hash value of the pattern
		int matches = 0;
	
		/*a loop runs shorter than the text length to find out the hashes tally each other*/
		for(int i=0;i<n;i++){
			if(i+m-1<n){
				String sub=txt.substring(i,i+m);	//split the text consecutively by length of the pattern
	
				int hashOfSub=sub.hashCode(); //check hash of the text and hash of the pattern match each other
	
				if(hashOfPatt==hashOfSub){ //if the pattern and text tallies, then start character by character checking
					int k=0;
					boolean d=true;
	
					 /*loop runs through both the selected parts to check whether both the parts match each other*/
					for(int j=i;j<i+m;j++){ 
						if(txt.charAt(j)==ptn.charAt(k)){ 
							k++;
						}else {
							d=false;
							break; //end of loop when an unmatched part found
					    }   
					}
					if(d){
						ret = ret+i+" ";  // storing match positions
						matches++;// = matches + sub + "\n";
					}   
				}
			}
		}
		return matches;
	}
	
	//split text by n words
	public String[] nGrams(String s, int n) {
	    String[] parts = s.split(" ");
	    int size=0;
	    
	    if((parts.length%n)==0){
	    	size = parts.length/n;
	    }
	    else{
	    	size = parts.length/n + 1;
	    }
	    
	    String[] result = new String[size];
	    int k=0;
	    String left="";
	    for(int i=0;i<size;i++){
	    	result[i]="";
	    	left="";
	    	try
	    	{
	    		for(int l=0;l<n;l++) {
	    			left+=parts[k+l]+" ";
	    		}
		    	result[i]=left;
		    	k=k+n;
	    	}
	    	catch(Exception e)
	    	{
	    		result[i]=left;
	    	}
	    }
	    return result;
	}
	
	//split text by periods
	public String[] lines(String s){
		String[] parts = s.split("\\n");
		return parts;
	}
} 