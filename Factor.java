import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.charset.Charset;
import java.lang.Double;

public class Factor
{
	Clause cl;
	List<Atom> liAtoms;	
	List<Message> liMessages;
	public Factor(Clause c, List<Atom> atoms)
	{
		this.cl = c;		
		this.liAtoms = atoms;		
	} 
	
	public void printFactor()
	{
		cl.printClause();
		for(Atom a : liAtoms)
		{
			//a.printAtom();
		}
		//System.out.println();
	}
	
	public void initMessages()
	{
		liMessages = new ArrayList<Message>();
		
		int size = liAtoms.size();
		
		for(int i=0; i<size; i++)
		{
			liMessages.add(new Message());  //how evidence ?? TODO
		}		
	}
}
