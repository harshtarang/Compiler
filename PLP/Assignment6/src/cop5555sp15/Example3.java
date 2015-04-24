/*
 * 	This class NestedList contains 3 lists : 2 lists l1,l2 of Integer type and the third ll - a list of list of integer type.
 * 
 * 	The lists l1 and l2 are initialized in a while loop and assigned to ll.
 * 
 * 	In the second run, we modify the value of i so that it does not enter the while loop and in the 2nd run
 * 
 * 	l1 and l2 are empty. The list ll is printed and we obtain the size of l1 using getList method.
 * 
 * Size of l1 should be different in the 2 executions.
 * 
 * Expected output :
 * 
 * Nested List:
	[[0, 1, 2, 3, 4], [5, 6, 7, 8, 9]]
	l1 size:5

	Second execution:

	Nested List:
	[[], []]
	l1 size:0
 * 
 * 
 */



package cop5555sp15;
import java.util.List;
public class Example3 
{
	public static void main(String[] args) throws Exception
	{
	String source = "class NestedList{\n"
			+ "def l1: @[int];\n"
			+ "def l2: @[int];\n"
			+ "def ll: @[@[int]];\n"
			+ "def i: int;\n"
			+ "l1=@[];l2=@[]; ll =@[];\n"
			+ "while(i<5) { \n"
			+ "l1[i]=i;\n"
			+ "l2[i]=i+5; \n"
			+ "i=i+1;\n"
			+ "}; \n"
			+ "ll = @[l1,l2]; \n"
			//+ "ll[1] = l2;\n"
			+ "print \"Nested List:\"; \n"
			+ "print ll;\n"
			+ "}";
	
	//System.out.println(source);
	Codelet codelet = CodeletBuilder.newInstance(source);
	codelet.execute();
	@SuppressWarnings("rawtypes")
	List l1 = CodeletBuilder.getList(codelet, "l1");
	System.out.println("l1 size:"+l1.size());
	//System.out.println("l1: "+l1);
	int i=CodeletBuilder.getInt(codelet, "i");
	
	
	CodeletBuilder.setInt(codelet, "i", 6);
	
	System.out.println("\nSecond execution:\n");
	
	codelet.execute();
	l1 = CodeletBuilder.getList(codelet, "l1");
	System.out.println("l1 size:"+l1.size());
	

	}
}