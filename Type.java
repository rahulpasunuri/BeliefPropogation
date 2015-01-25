import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.charset.Charset;
import java.lang.Double;

public class Type
{
	String name;
	List<String> domain;
	
	public Type(String name)
	{
		this.name = name;
		this.domain = new ArrayList<String>();
	}

	public void addToDomain(String s)
	{
		for(String s1 : domain)
		{
			if(s1.equals(s))
			{
				return;
			}
		}
		domain.add(s);
	}
}
