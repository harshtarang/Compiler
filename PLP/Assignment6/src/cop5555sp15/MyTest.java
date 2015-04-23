package cop5555sp15;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import cop5555sp15.Assignment4Tests.DynamicClassLoader;
import cop5555sp15.ast.ASTNode;
import cop5555sp15.ast.CodeGenVisitor;
import cop5555sp15.ast.Program;
import cop5555sp15.ast.TypeCheckVisitor;
import cop5555sp15.ast.TypeCheckVisitor.TypeCheckException;
import cop5555sp15.symbolTable.SymbolTable;

public class MyTest {

	public static void main(String[] args) throws Exception
	{
		// TODO Auto-generated method stub
		String input = "class A {\n def i:int; def l:@[int]; def l2:@[int];\n"
				+ "def ll:@[@[int]]; i=0; l=@[];\n"
				+ " while(i<4){ l[i]=i;i=i+1;}; \n"
				+ "ll=@[]; ll[0]=l; \n"
				+ " print l[2]; \n"
				+ "l2=ll[0]; print ll;\n}";
		System.out.println(input);
		Program program = (Program) parseCorrectInput(input);
		
		typeCheckCorrectAST(program);
		byte[] bytecode = generateByteCode(program);
		
		System.out.println("\nexecuting bytecode:");
		executeByteCode(program.JVMName, bytecode);
	}
	
	
	private static ASTNode parseCorrectInput(String input) {
		TokenStream stream = new TokenStream(input);
		Scanner scanner = new Scanner(stream);
		scanner.scan();
		Parser parser = new Parser(stream);
		System.out.println();
		ASTNode ast = parser.parse();
		if (ast == null) {
			System.out.println("errors " + parser.getErrors());
		}
		assertNotNull(ast);
		return ast;
	}
	
	
	private static ASTNode typeCheckCorrectAST(ASTNode ast) throws Exception {
		SymbolTable symbolTable = new SymbolTable();
		TypeCheckVisitor v = new TypeCheckVisitor(symbolTable);
		try {
			ast.visit(v, null);
		} catch (TypeCheckException e) {
			System.out.println(e.getMessage());
			fail("no errors expected");
		}
		return ast;
	}
	
	
	private static byte[] generateByteCode(ASTNode ast) throws Exception {
		CodeGenVisitor v = new CodeGenVisitor();
			byte[] bytecode = (byte[]) ast.visit(v, null);
			
			return bytecode;
	}
	
	
	public static void executeByteCode(String name, byte[] bytecode) throws InstantiationException, IllegalAccessException{
        DynamicClassLoader loader = new DynamicClassLoader(Thread
                .currentThread().getContextClassLoader());
        Class<?> testClass = loader.define(name, bytecode);
        Codelet codelet = (Codelet) testClass.newInstance();
        codelet.execute();
    }
   

}
