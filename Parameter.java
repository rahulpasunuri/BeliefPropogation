import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.charset.Charset;
import java.lang.Double;

public class Parameter
{
	String name;
	Type type;
	
	public Parameter(String name, Type t)
	{		
		this.name = name;
		this.type = t;
	}

}
