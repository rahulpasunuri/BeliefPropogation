import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.charset.Charset;
import java.lang.Double;

public class Predicate
{
	boolean isClosedWorld;
	String name;
	List<Type> liTypes;
	
	boolean isEvidence;
	boolean isQuery;
	
	public Predicate(String s)
	{
		MLN mln = MLN.getMLN();
		//parse the predicate string
		int i = s.indexOf('(');
		name = s.substring(0, i).trim();
		liTypes = new ArrayList<Type>();
		//check for closed world condition..
		if(name.charAt(0) == '*')
		{
			this.isClosedWorld = true;
			
			name = name.substring(1).trim();
		}		
		
		int j = s.indexOf(')');
		String params = s.substring(i+1, j); 
		
		i = params.indexOf(',');
		while(i>=0)
		{
			//parse types..
			String t = params.substring(0, i);
			params = params.substring(i+1);
			i = params.indexOf(',');
			params = params.trim();
			liTypes.add(mln.addType(t.trim()));				
		}

		liTypes.add(mln.addType(params)); //adds the last type		
	}
	
	public void printPredicate()
	{
		System.out.print(name+"(");
		for(Type t : liTypes)
		{
			System.out.print(t.name+",");
		}
		System.out.print(")\n");		
	}
}
