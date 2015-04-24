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
			+ "i=0;l1=@[];l2=@[]; ll =@[];\n"
			+ "while(i<5) { \n"
			+ "l1[i]=i;\n"
			+ "l2[i]=i+5; \n"
			+ "i=i+1;\n"
			+ "}; \n"
			+ "ll[0] = l1; \n"
			+ "ll[1] = l2;\n"
			+ "print ll;\n"
			+ "}";
	
	System.out.println(source);
	Codelet codelet = CodeletBuilder.newInstance(source);
	codelet.execute();
	@SuppressWarnings("rawtypes")
	List l1 = CodeletBuilder.getList(codelet, "l1");
	System.out.println("l1 size:"+l1.size());
	System.out.println("l1: "+l1);
	
	
//	i1 = CodeletBuilder.getInt(codelet, "i1");
//	System.out.println(i1);
	}
}