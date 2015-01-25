import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.charset.Charset;
import java.lang.Double;

public  class MLN
{
	List<Clause> liClauses;
	List<Predicate> liPredicates;
	List<Type> liTypes;
	List<Atom> liAtoms;
	
	static MLN mln;
	
	public static MLN getMLN()
	{
		if(mln == null)
		{
			mln = new MLN();
		}
		return mln;
	}
	
	public void addClause(String s)
	{
		Clause c = new Clause(s);
		liClauses.add(c);
	}
	
	public void printClauses()
	{
		System.out.println("*****************************************");
		System.out.println("Printing Clauses");
		System.out.println("*****************************************");
		
		for(Clause c : liClauses)
		{
			c.printClause();
		}
		System.out.println();
	}
	
	private MLN()
	{
		liClauses = new ArrayList<Clause>();
		liPredicates = new ArrayList<Predicate>();
		liTypes = new ArrayList<Type>();
		liAtoms = new ArrayList<Atom>();
	}
	
	public Predicate getPredicateByName(String name)
	{
		for(Predicate p : liPredicates)
		{
			if(p.name.equals(name))
			{
				return p;
			}
		}
		return null;
	}
	
	public void addEvidence(String inp, Predicate p)
	{
		liAtoms.add(new Atom(inp, p, true));
	}
	
	public void printEvidence()
	{
		System.out.println("*****************************************");
		System.out.println("Printing Evidence");
		System.out.println("*****************************************");
	
		for(Atom a : liAtoms)
		{
			if(a.isEvidence)
			{
				a.printAtom();
			}
		}
	}
	
	public void printPredicates()
	{
		System.out.println("*****************************************");
		System.out.println("Printing Predicates");
		System.out.println("*****************************************");

		for(Predicate p : liPredicates)
		{
			p.printPredicate();
		}
		System.out.println();
	}
	
	public Type addType(String s)
	{
		for(Type t : liTypes)
		{
			if(t.name.equals(s))
			{
				return t;
			}
		}
		Type t = new Type(s);
		//add type here..
		liTypes.add(t);
		return t;
	}
	
	public void addPredicate(String s)
	{
		Predicate p = new Predicate(s);
		liPredicates.add(p);
	}
	
}
