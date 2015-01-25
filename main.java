import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.charset.Charset;
import java.lang.Double;

public class main
{
        public static void main(String[] args)
        { 
        	String programFile = "samples/smoke/prog.mln";
        	String evidenceFile = "samples/smoke/evidence.db";
        	String queryFile = "samples/smoke/query.db";
        	        	
        	Parser.parseProgram(programFile);
        }
}
