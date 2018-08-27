package taAdr;

import taAdr.RegexOptions;
import java.util.regex.*;

public class Regex {
	
	public void Regex() {}
	public String Regex (String cmdString, RegexOptions opt) {
		//TODO: RegexOption is byte  
		
		switch(opt): {
			case COMPILED:
				
				break;
		}
		return cmdString;
	}
	
	static public String replace(String src, String rplValue, String rplwith){
		
		return src;
	}
	
	static Matcher Match(String theString)
	{
		Matcher match;		
		return match;
	}
	//regular expression match
	static boolean IsMatch(String str1, String str2){
		
		str1.trim();
		str2.trim();
		
		if (str1.compareToIgnoreCase(str2) == 0)
			return true;
		return false;
	}

}
