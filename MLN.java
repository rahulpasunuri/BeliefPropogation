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
		double precision = 0.01;
		int iterations = 0;
		while(!isConverged)
		{

			//if(iterations >100)
			//{
			//	break;
			//}
			iterations++;
			System.out.println("Running Iteration" + iterations);
			
			//send messages from atoms to factors..			
			for(Atom a : liAtoms)
			{
				if(!a.isEvidence) // no need to change messages from evidence..
				{									
					int factorIndex =0;				
					for(Factor f : a.liFactors)
					{
						//send message from a - > f				
						Message m = a.liMessages.get(factorIndex);
						m.truebk =1;
						m.falsebk=1;
						int atomIndex = 0;
						for( Atom a2 : f.liAtoms)
						{
							if(a != a2)
							{
								m.truebk *= f.liMessages.get(atomIndex).valTrue;
								m.falsebk *= f.liMessages.get(atomIndex).valFalse;
							}
							atomIndex++;
						}					
						factorIndex++;
					}
				}
				int k=0; int size = a.liMessages.size();
				while(k< size)
				{					
					//normalize the values..					
					Message m = a.liMessages.get(k);
					//m.truebk = m.truebk/sumTrue;
					//m.falsebk = m.falsebk/sumFalse;	
					double sum = m.truebk + m.falsebk; 
					//m.truebk = m.truebk/sum;
					//m.falsebk = m.falsebk/sum;
					
					System.out.println("Sending message from ");
					a.printAtom();					
					System.out.println(m.truebk + "\t"+m.falsebk);
					System.out.println();
					
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
					if(a.isEvidence)
					{
						atomIndex++;						
						continue;
					}
					//send message from f -> a
					Message m = f.liMessages.get(atomIndex);
					m.truebk = 0;
					m.falsebk = 0;
					List<String> combinations = generateTFCombinations(totalAtoms-1);					
					
					boolean isNegated = f.cl.liLiterals.get(atomIndex).isNegated;										
					
					if(combinations.size() == 1)
					{
						if(!isNegated)
						{
							m.truebk = Math.exp(f.cl.weight);
							m.falsebk = 0;
						}
						else
						{
							m.truebk = 0;
							m.falsebk = Math.exp(f.cl.weight);						
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
										l.printLiteral();
										System.out.println(s);								
										isTrue = true;
										break;	
									}								
									strIndex++;
								}
								k++;							
							}
					
							if(!isNegated)
							{
								
								if(!isTrue)
								{

									potentialFalse = 1;
								}			 								 
							}
							else
							{
								
								if(!isTrue)
								{
									potentialTrue = 1;
								}			 		
							}
							
							double tempTrue = potentialTrue;
							double tempFalse = potentialFalse;
							//System.out.println(potentialTrue+"\t"+potentialFalse);
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
										tempFalse *= a1.liMessages.get(fIndex).valTrue; 
									}
									else
									{
										tempTrue *= a1.liMessages.get(fIndex).valFalse;
										tempFalse *= a1.liMessages.get(fIndex).valFalse;								
									}
									strIndex++;
								}
							}
							m.truebk += tempTrue;
							m.falsebk += tempFalse;
						}
						///*
						System.out.println("Sending message from ");
						f.printFactor();
						a.printAtom();						
						System.out.println(m.truebk+"\t"+m.falsebk);
						System.out.println();							
						//*/
					}
					atomIndex++;
				}
			}
			isConverged = true;

			precision = 0.01;
			isConverged = true;
			for(Atom a : liAtoms)
			{
				if(!a.isEvidence && isConverged)
				{
					double probTrue = 1.0;
					double probFalse = 1.0;
					
					double oldProbTrue = 1.0;
					double oldProbFalse = 1.0;
					
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
												
						probTrue *= f.liMessages.get(index).truebk;
						probFalse *= f.liMessages.get(index).falsebk;
						
						oldProbTrue *= f.liMessages.get(index).valTrue;
						oldProbFalse *= f.liMessages.get(index).valFalse;						
					}				
					
					oldProbTrue = oldProbTrue/(oldProbTrue+ oldProbFalse);
					probTrue = probTrue/(probTrue+ probFalse);
					
					if(Math.abs(oldProbTrue - probTrue) > precision)
					{
						isConverged = false;
					}
				
				}
			}
			
			//copy messages from backup..
			for(Atom a : liAtoms)
			{
				for(Message m : a.liMessages)
				{
					m.backUp();
				}
			}
			
			for(Factor f : liFactors)
			{
				for(Message m : f.liMessages)
				{
					m.backUp();
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
				//System.out.println(probTrue+"\t"+probFalse+ "\n");
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
