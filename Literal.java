import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.charset.Charset;
import java.lang.Double;


public class Literal
{
	List<Parameter> liParams;
	Predicate pred;
	boolean isNegated;
	public Literal(String s)
	{
		MLN mln = MLN.getMLN();
		liParams = new ArrayList<Parameter>();
		
		if(s.charAt(0) == '!')
		{
			this.isNegated = true;
			s = s.substring(1).trim();
		}
		
		int i = s.indexOf('(');
		String name = s.substring(0, i).trim();
		int j = s.indexOf(')');
		String p = s.substring(i+1, j);
		
				
		for(Predicate p1 : mln.liPredicates)
		{			
			if(p1.name.equals(name))
			{
				this.pred = p1;
				break;
			}
		}
		int k=0;
		i = p.indexOf(',');
		while(i>=0)
		{
			Type t = pred.liTypes.get(k);
			liParams.add(new Parameter(p.substring(0, i).trim(), t));
			p = p.substring(i+1).trim();
			i = p.indexOf(',');			
			k++;
		}		
		Type t = pred.liTypes.get(k);
		liParams.add(new Parameter(p.trim(), t));				
	}
	
	public void printLiteral()
	{
		if(isNegated)
		{
			System.out.print("!");			
		}
		System.out.print(pred.name+"(");
		for(Parameter p : liParams)
		{
			System.out.print(p.name+", ");
		}
		System.out.print(")");

	}
}

