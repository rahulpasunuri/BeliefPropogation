import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.charset.Charset;
import java.lang.Double;

public class Atom
{
	boolean isEvidence;
	boolean isQuery;
	Predicate pred;
	List<String> liParamValues;
	boolean isNegated;
	
	public Atom(String s, Predicate pred,boolean isEvidence)
	{
		this.isEvidence = isEvidence;
		isQuery = !isEvidence;
		liParamValues = new ArrayList<String>();
		MLN mln = MLN.getMLN();
		this.pred = pred;
		s = s.trim();
		//get values..	
		if(s.charAt(0) == '!')
		{
			isNegated = true;
		}	
		int i = s.indexOf('(');
		int j = s.indexOf(')');
		String p = s.substring(i+1, j).trim();
		i = p.indexOf(',');
		int k=0;
		while(i>=0)
		{
			String val = p.substring(0, i).trim();
			liParamValues.add(val);
			pred.liTypes.get(k).addToDomain(val);			
			k++;
			p = p.substring(i+1).trim();
			i = p.indexOf(',');
		}
		liParamValues.add(p);
		pred.liTypes.get(k).addToDomain(p);				
	}
	
	public void printAtom()
	{
		if(isNegated)
		{
			System.out.print("!");
		}
		System.out.print(pred.name+"(");
		for(String s : liParamValues )
		{
			System.out.print(s+", ");
		}
		System.out.print(")\n");
	}

}
