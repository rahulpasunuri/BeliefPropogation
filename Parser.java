import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.charset.Charset;
import java.lang.Double;
import java.lang.String;

public class Parser
{
	public static void parseProgram(String input)
	{
		try
		{
			 MLN mln = MLN.getMLN();
			 List<String> lines = Files.readAllLines(Paths.get(input), Charset.defaultCharset());
			 for(String s : lines)
			 {
			 	s = s.trim();
			 	if(s.equals(""))
			 	{
			 		continue; //empty line
			 	}
			 	else if(s.substring(0,2).equals("//"))
			 	{
			 		continue; //comments in the file
			 	}
			 	else
			 	{
			 		if(s.contains(" v "))
			 		{
			 			//it is a clause..	
			 			mln.addClause(s);
			 			//System.out.println(s+"\tClause");			 				 			
			 		}
			 		else
			 		{
			 			//rule is a predicate..
			 			mln.addPredicate(s);
			 			//System.out.println(s+"\tPredicate");
			 		}
			 	}
			 }
		 }
		 catch(IOException e)
		 {
		 	 e.printStackTrace();
		 }
	}
	
	public static void parseEvidence(String input)
	{
		try
		{
			MLN mln = MLN.getMLN();
			List<String> lines = Files.readAllLines(Paths.get(input), Charset.defaultCharset());						
			
			for(String line : lines)
			{
							
				line = line.trim();
				//check for comments..
				if(line.equals(""))
				{
					continue;
				}
				String c = line.substring(0, 3);
				if(c.equals("//"))
				{
					continue;
				}
				
				int i = line.indexOf('(');
				String name = line.substring(0, i).trim();
				boolean isNegated = false;
				if(name.charAt(0) == '!')
				{
					isNegated = true;
					name = name.substring(1);
				}
				
				Predicate p = mln.getPredicateByName(name);
				
				if(p.isClosedWorld && isNegated)
				{
					//ignore the line 
				}
				else
				{
					mln.addEvidence(line, p);
				}
			}

		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void parseQuery(String input)
	{
		try
		{
			MLN mln = MLN.getMLN();
			List<String> lines = Files.readAllLines(Paths.get(input), Charset.defaultCharset());
			for(String line : lines)
			{
				line = line.trim();
				if(line.equals(""))
				{
					continue;
				}
				String c = line.substring(0, 2);
				if(c.equals("//"))
				{
					continue;
				}
				mln.addQuery(line);								
			}			
			
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	

}
