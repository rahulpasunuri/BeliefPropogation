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
			 			System.out.println(s+"\tClause");			 				 			
			 		}
			 		else
			 		{
			 			//rule is a predicate..
			 			System.out.println(s+"\tPredicate");
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
			List<String> lines = Files.readAllLines(Paths.get(input), Charset.defaultCharset());
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
			List<String> lines = Files.readAllLines(Paths.get(input), Charset.defaultCharset());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	

}
