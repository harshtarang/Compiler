package cop5555sp15;
import java.util.List;
public class Example1 
{
	public static void main(String[] args) throws Exception
	{
	String source = "class ListInit{\n"
			+ "def l1: @[int];\n"
			+ "def l2: @[string];\n"
			+ "def i1: int;\n"
			+ "l1 = @[300,400,500];\n"
			+ "l2 = @[\"go\", \"gators\"];\n"
			+ "i1 = 42;\n"
		//	+ "print i1;"
			+ "}";
	Codelet codelet = CodeletBuilder.newInstance(source);
	codelet.execute();
	@SuppressWarnings("rawtypes")
	List l1 = CodeletBuilder.getList(codelet, "l1");
	System.out.println("l1 size:"+l1.size());
	System.out.println("L1 first element:"+l1.get(0));
	@SuppressWarnings("rawtypes")
	List l2 = CodeletBuilder.getList(codelet, "l2");
	System.out.println("l2 size:"+l2.size());
	System.out.println("l2 2nd element:"+l2.get(1));
	int i1 = CodeletBuilder.getInt(codelet, "i1");
	System.out.println(i1);
	CodeletBuilder.setInt(codelet, "i1", 43);
//	i1 = CodeletBuilder.getInt(codelet, "i1");
//	System.out.println(i1);
	}
}