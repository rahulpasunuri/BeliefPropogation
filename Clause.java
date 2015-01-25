import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.charset.Charset;
import java.lang.Double;

public class Clause
{
	List<Literal> liLiterals;
	List<Parameter> liParams;
	double weight;
	public Clause(String s)
	{
		liLiterals = new ArrayList<Literal>();
		liParams = new ArrayList<Parameter>();
		//parse the weight..		
		int i = s.indexOf(' ');
		String w = s.substring(0, i);
		weight=Double.parseDouble(w.trim());
		
		
		//parse the literals..
		s = s.substring(i+1).trim();
		
		i = s.indexOf(" v ");
		while(i>=0)
		{
			liLiterals.add(new Literal(s.substring(0, i).trim()));
			//System.out.println(s);
			s = s.substring(i+3).trim();
			System.out.println(s);
			i = s.indexOf(" v ");
		}
		liLiterals.add(new Literal(s.trim()));
		
		for(Literal l : liLiterals)
		{
			for(Parameter p1 : l.liParams)
			{
				boolean isPresent = false;
				for(Parameter p2 : liParams)
				{
					if(p1.name.equals(p2.name))
					{
						isPresent = true;
						break;
					}
				}
				if(!isPresent)
				{
					liParams.add(p1);
				}
			}			
		}				
	}
	
	public void printClause()
	{		
		System.out.print(weight+"\t");
		for(Literal l : liLiterals)
		{			
			l.printLiteral();	
			System.out.print(" v ");		
		}
		System.out.println();
	}

}
