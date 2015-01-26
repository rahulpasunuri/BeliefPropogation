import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.charset.Charset;
import java.lang.Double;
import java.lang.Math;

public  class MLN
{
	List<Clause> liClauses;
	List<Predicate> liPredicates;
	List<Type> liTypes;
	List<Atom> liAtoms;
	List<Factor> liFactors;
	
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
		liFactors = new ArrayList<Factor>();
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
		System.out.println();
	}
	
	public void printAtoms()
	{
		System.out.println("*****************************************");
		System.out.println("Printing Atoms");
		System.out.println("*****************************************");
	
		for(Atom a : liAtoms)
		{
				a.printAtom();
		}
		System.out.println();
	}
	
	
	public void printTypes()
	{
		System.out.println("*****************************************");
		System.out.println("Printing Types");
		System.out.println("*****************************************");

		for(Type t : liTypes)
		{
			t.printType();
		}
		System.out.println();
	}
	
	public void addQuery(String l)
	{
		int i = l.indexOf('(');
		String name = l.substring(0, i).trim();
		Predicate p = getPredicateByName(name);
		p.isQuery = true;		
	}
	
	public void printQuery()
	{
		System.out.println("*****************************************");
		System.out.println("Printing Query");
		System.out.println("*****************************************");
		for(Predicate p : liPredicates)
		{
			if(p.isQuery)
			{
				p.printPredicate();
			}
		}
		System.out.println();
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
	
	public List<List<String>> generateTuple(List<Type> types, int currType)
	{
		if(types.size()-1 == currType)
		{
			//base case..
			List<List<String>> tuple = new ArrayList<List<String>>();
			List<String> dom = types.get(currType).domain;
			
			for(String s : dom)
			{
				List<String> temp = new ArrayList<String>();
				temp.add(s);
				tuple.add(temp);
			}			
			return tuple;
		}
				
		List<List<String>> t = generateTuple(types, currType+1);
		List<List<String>> tuple = new ArrayList<List<String>>();
		List<String> dom = types.get(currType).domain;
		
		for(String curr : dom)
		{
			for(List<String> temp : t)
			{
				List<String> p = new ArrayList<String>();
				p.add(curr);
				for(String pqr: temp)
				{
					p.add(pqr);
				}
				tuple.add(p);				
			}			
		}
		return tuple;		
	}
	
	
	public Atom getAtom(Predicate p, List<String> vals)
	{
		for(Atom a : liAtoms)
		{
			if(a.pred.name.equals(p.name))
			{
				//check for values..
				int size = vals.size();
				boolean isMatched = true;
				for(int i=0; i<size; i++)
				{
					String s1 = a.liParamValues.get(i);
					String s2 = vals.get(i);
					if(!s1.equals(s2))
					{
						isMatched = false;
					}					
				}
				if(isMatched)
				{
					return a;
				}
			}
		}
		return null;
	}
	
	public void printFactors()
	{
	
		System.out.println("*****************************************");
		System.out.println("Printing Factors");
		System.out.println("*****************************************");
	
		for(Factor f : liFactors)
		{
			f.printFactor();
		}
	}
	
	public boolean checkIfAtomExists(Predicate p, List<String> vals)
	{
		for(Atom a : liAtoms)
		{
			if(a.pred.name.equals(p.name))
			{
				//check for values..
				int size = vals.size();
				boolean isMatched = true;
				for(int i=0; i<size; i++)
				{
					String s1 = a.liParamValues.get(i);
					String s2 = vals.get(i);
					if(!s1.equals(s2))
					{
						isMatched = false;
					}					
				}
				if(isMatched)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public void generateFactorGraph()
	{
		for(Predicate p : liPredicates)
		{
			if(!p.isClosedWorld)
			{
				List<List<String>> tuples = generateTuple( p.liTypes, 0);
				for(List<String> tup : tuples)
				{
					if(!checkIfAtomExists(p, tup))
					{
						//create atom...
						Atom a = new Atom(p, tup);
						liAtoms.add(a);
					}					
				}					
			}
		}
		
		//generate factor nodes...
		for(Clause c : liClauses)
		{
			List<Type> liTypes = new ArrayList<Type>();
			for(Parameter par : c.liParams)
			{
				liTypes.add(par.type);
			}		
		
			List<List<String>> tuples = generateTuple(liTypes, 0);
			for(List<String> tup : tuples)
			{
				boolean atomsExist = true;
				List<Atom> liAtoms = new ArrayList<Atom>();
				for(Literal l : c.liLiterals)
				{
					List<Parameter> pars = l.liParams;
					List<String> vals = new ArrayList<String>();
					for(Parameter p : pars)
					{
						//find each value of the parameter..
						int index = 0;
						while(!c.liParams.get(index).name.equals(p.name))
						{
							index++;
						}
						vals.add(tup.get(index));
					}
					
					//check if the atom exists or not..					
					if(!checkIfAtomExists(l.pred, vals))
					{
						atomsExist = false;
						break;
					}
					else
					{
						liAtoms.add(getAtom(l.pred, vals));
					}					
				}
				
				if(atomsExist)
				{
					//generate the factor node..
					Factor f = new Factor( c, liAtoms);
					liFactors.add(f);
					
					for(Atom a : liAtoms)
					{
						a.liFactors.add(f);
					}
				}
			}
		}
		
	}
	
	public void performBP()
	{
		System.out.println("Performing Belief Propogation");
		
		//init messages..
		
		for(Atom a : liAtoms)
		{
			a.initMessages();
		}
		
		for(Factor f : liFactors)
		{
			f.initMessages();
		}
		
		boolean isConverged = false;
		double precision = 0.1;
		
		while(!isConverged)
		{
			isConverged = true;
			
			//send messages from atoms to factors..
			for(Atom a : liAtoms)
			{
				int factorIndex =0;
				for(Factor f : a.liFactors)
				{
					//send message from a - > f				
					Message m = a.liMessages.get(factorIndex);
					Message bk = new Message();
					bk.valTrue = m.valTrue;
					bk.valFalse = m.valFalse;
					m.valTrue =1;
					m.valFalse=1;
					int atomIndex = 0;
					for( Atom a2 : f.liAtoms)
					{
						if(a != a2)
						{
							m.valTrue *= f.liMessages.get(atomIndex).valTrue;
							m.valFalse *= f.liMessages.get(atomIndex).valFalse;
						}
						atomIndex++;
					}
				
					if(isConverged && (Math.abs(bk.valTrue-m.valTrue) > precision  || Math.abs(bk.valFalse - m.valFalse) > precision))
					{
						//check for convergence...
						isConverged = false;
					}				
					factorIndex++;
				}
			}
		
			//send messages from factors to atoms..
			for(Factor f: liFactors)
			{
				int atomIndex = 0;
				for(Atom a : f.liAtoms)
				{
					//send message from f -> a

				
					atomIndex++;
				}
			}
		}
		System.out.println("*****************************************");
		System.out.println("Printing Results");
		System.out.println("*****************************************");
		
		for(Atom a: liAtoms)
		{
			if(a.isQuery)
			{
				double probTrue = 1.0;
				double probFalse = 1.0;
				for(Factor f : a.liFactors)
				{
					int index = 0;
					int size = f.liAtoms.size();
					for(int i=0; i< size; i++)
					{
						if( f.liAtoms.get(i) == a)
						{
							break;
						}
						index++;
					}
					probTrue *= f.liMessages.get(index).valTrue;
					probTrue *= f.liMessages.get(index).valFalse;
				}				
				a.printAtom();
				System.out.println(probTrue/(probTrue+probFalse));
			}
		}
	}
	
}
