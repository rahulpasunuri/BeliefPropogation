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
		int iterations = 0;
		while(!isConverged)
		{
		
			//if(iterations >100)
			//{
			//	break;
			//}
			iterations++;
			System.out.println("Running Iteration" + iterations);
			isConverged = true;
			
			//send messages from atoms to factors..			
			for(Atom a : liAtoms)
			{
				int factorIndex =0;
				double sumTrue =0, sumFalse = 0;
				
				//make back up of messages..
				List<Message> bk = new ArrayList<Message>();
				for(Message m : a.liMessages)
				{
					Message m1 = new Message();
					m1.valTrue = m.valTrue;
					m1.valFalse = m.valFalse;
					bk.add(m1);
				}
				for(Factor f : a.liFactors)
				{
					//send message from a - > f				
					Message m = a.liMessages.get(factorIndex);
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
					sumTrue += m.valTrue;
					sumFalse += m.valFalse;
			
					factorIndex++;
				}
				int k=0; int size = a.liMessages.size();
				while(k< size)
				{
					//normalize the values..					
					Message m = a.liMessages.get(k);
					m.valTrue = m.valTrue/sumTrue;
					m.valFalse = m.valFalse/sumFalse;
					
					Message m1 = bk.get(k);
					if(isConverged && (Math.abs(m1.valTrue-m.valTrue) > precision  || Math.abs(m1.valFalse - m.valFalse) > precision))
					{
						//check for convergence...
						isConverged = false;
					}	
					k++;
				}
			}
		
			//send messages from factors to atoms..
			for(Factor f: liFactors)
			{
				int atomIndex = 0;
				int totalAtoms = f.liAtoms.size();
				for(Atom a : f.liAtoms)
				{
					//send message from f -> a
					Message bk = new Message();
					Message m = f.liMessages.get(atomIndex);
					bk.valTrue = m.valTrue;
					bk.valFalse = m.valFalse;
					m.valTrue = 0;
					m.valFalse = 0;
					List<String> combinations = generateTFCombinations(totalAtoms-1);					
					
					boolean isNegated = f.cl.liLiterals.get(atomIndex).isNegated;										
					
					if(combinations.size() == 1)
					{
						if(!isNegated)
						{
							m.valTrue = Math.exp(f.cl.weight);
							m.valFalse = 0;
						}
						else
						{
							m.valTrue = 0;
							m.valFalse = Math.exp(f.cl.weight);						
						}
						
						if(isConverged && (Math.abs(bk.valTrue-m.valTrue) > precision  || Math.abs(bk.valFalse - m.valFalse) > precision))
						{
							//check for convergence...
							isConverged = false;
						}	
					}
					else 
					{
						for(String s: combinations)
						{
							double potentialTrue = Math.exp(f.cl.weight);
							double potentialFalse = Math.exp(f.cl.weight);
							//double potential = f.cl.weight;
							//determine the truthness of the clause..
							int k=0, strIndex = 0;
							boolean isTrue = false;
							for(Literal l : f.cl.liLiterals)
							{
								if(k!=atomIndex)
								{
									if((l.isNegated && s.charAt(strIndex) == 'F') || (!l.isNegated && s.charAt(strIndex) == 'T'))
									{
										/*
										System.out.println();
										f.printFactor();
										a.printAtom();
										System.out.println(s+"\t"+strIndex);
										System.out.println();
										*/
										isTrue = true;
										break;	
									}								
									strIndex++;
								}
								k++;							
							}
					
							if(!isTrue)
							{
										/*
										System.out.println();
										f.printFactor();
										a.printAtom();
										System.out.println(s+"\t"+strIndex);
										System.out.println();
										*/
							}
							if(!isNegated)
							{
								if(!isTrue)
								{
									potentialFalse = 0;
								}			 								 
							}
							else
							{
								if(!isTrue)
								{
									potentialTrue = 0;
								}			 		
							}
							
							double tempTrue = potentialTrue;
							double tempFalse = potentialFalse;
							strIndex = 0;;
							for(Atom a1 : f.liAtoms)
							{
								if(a1!=a)
								{
									int fIndex = 0;
									for(Factor f1: a1.liFactors)
									{
										if(f1==f)
										{
											break;
										}
										fIndex++;
									}
									if(s.charAt(strIndex) == 'T')
									{
										tempTrue *= a1.liMessages.get(fIndex).valTrue;
										tempFalse *= a1.liMessages.get(fIndex).valFalse; 
									}
									else
									{
										tempTrue *= a1.liMessages.get(fIndex).valFalse;
										tempFalse *= a1.liMessages.get(fIndex).valTrue;								
									}
									strIndex++;
								}
							}
							m.valTrue += tempTrue;
							m.valFalse += tempFalse;
						}
						//System.out.println(m.valTrue+"\t"+m.valFalse);
						if(isConverged && (Math.abs(bk.valTrue-m.valTrue) > precision  || Math.abs(bk.valFalse - m.valFalse) > precision))
						{
							//check for convergence...
							isConverged = false;
						}							
					}
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
					probFalse *= f.liMessages.get(index).valFalse;
				}				
				a.printAtom();
				System.out.println(probTrue/(probTrue+probFalse) + "\n");
				System.out.println(probTrue+"\t"+probFalse+ "\n");
			}
		}
	}
	
	public List<String> generateTFCombinations(int num)
	{
		List<String> ret = new ArrayList<String>();
		if( num ==0)
		{
			ret.add("");
			return ret;
		}
		List<String> t = generateTFCombinations(num-1);
		for(String s : t)
		{
			ret.add("T"+s);
			ret.add("F"+s);
		}
		return ret;		
	}
	
}
