import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.charset.Charset;
import java.lang.Double;


public class Message
{
	double valTrue;
	double valFalse;
	double truebk;
	double falsebk;
	double precision;
	public Message()
	{
		precision = 100;
		valTrue = 1;
		valFalse = 1;
		
		truebk=1;
		falsebk=1;		
	}	
	
	public void backUp()
	{
		valTrue = truebk;
		valFalse = falsebk;		
	}
	
	public boolean isConverged()
	{
		//System.out.println("Checking for convergence");
		//System.out.println(truebk + "\t"+ valTrue);
		//System.out.println(falsebk + "\t"+ valFalse);
		//System.out.println();
	
		double sum = truebk+falsebk;	
		//truebk =truebk/sum;
		//falsebk = falsebk/sum;
		 		
		if(Math.abs(truebk-valTrue) > precision  || Math.abs(valFalse - falsebk) > precision)
		{

			//check for convergence...
			return false;
		}
		/*
		System.out.println("Checking for convergence");
		System.out.println(truebk + "\t"+ valTrue);
		System.out.println(falsebk + "\t"+ valFalse);
		System.out.println();
		*/
		return true;
	}
}
