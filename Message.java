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
	
	public Message()
	{
		boolean isEvidence = false;
		if(!isEvidence)
		{
			valTrue = 1;
			valFalse = 1;
		}
		else
		{
			// how to handle negation ??? TODO EG: !Friends(Gary, Ann)
			valTrue = 1;
			valFalse = 0;
		}
		
	}	
}
