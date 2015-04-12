package sjy.elwg.notation;
import java.io.*;  
  
public class  XMLComparator{  
	
	public static boolean compare(InputStream in1, InputStream in2){
		InputStream standardAnswer = new BufferedInputStream(in1);
		InputStream answer = new BufferedInputStream(in2);
		standardAnswer = in1;
		answer = in2;
		 //判断两个文件是否相等  
        try  
        {
            if(standardAnswer.available() != answer.available())  
            {  
                return false;
            }  
            else  
            {  
                boolean tag = true;  

                while( standardAnswer.read() != -1 && answer.read() != -1)  
                {  
                    if(standardAnswer.read() != answer.read())  
                    {  
                        tag = false;  
                        return false;
                    }  
                }  
                return tag;
            }  
        }  
        catch(IOException e)  
        {  
            e.printStackTrace();
            return false;
        }
		  
	}

}  