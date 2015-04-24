/*
 * 
 * In this class List, there is a List which is initialized to [100,200], but it does not 
 * enter the while loop because i is set to zero by default; But in the second run, the value of 
 *  i is modified to 2 and hence it enters the loop and modifies the list.
 *  Both the lists are printed on the terminal.
 *  
 *  Expected output:
 *  
 *  l first time:[100, 200]
	l second time:[1, 2]
 * 
 */

package cop5555sp15;
import java.io.File;
import java.util.List;
public class Example4 
{
	public static void main(String[] args) throws Exception
	{
		//File file=new File("test");
	
		String source = "class List \n\n"
		+ "	{ \n"
		+ "def l:@[int];\n"
		
		+ "def i:int;\n"
		
		+ "l=@[100,200];\n"
		
		+ "while(i>0)\n"
		+ "{\n"
		+ "	l[i-1]=i;\n"
		+ "	i=i-1;\n"
		+ "};\n"
		
		+	" } \n";
	
	Codelet codelet = CodeletBuilder.newInstance(source);
	codelet.execute();
	@SuppressWarnings("rawtypes")
	List l = CodeletBuilder.getList(codelet, "l");
	System.out.println("l first time:"+l);
	//System.out.println("l1: "+l1);
	int i=CodeletBuilder.getInt(codelet, "i");
	CodeletBuilder.setInt(codelet, "i",2);
	//System.out.println("Value of i: "+i);
	codelet.execute();
	
	l = CodeletBuilder.getList(codelet, "l");
	System.out.println("l second time:"+l);
	

	}
}