package cop5555sp15;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cop5555sp15.TestCodeGenerationAssignment5.DynamicClassLoader;
import cop5555sp15.ast.ASTNode;
import cop5555sp15.ast.CodeGenVisitor;
import cop5555sp15.ast.Program;
import cop5555sp15.ast.TypeCheckVisitor;
import cop5555sp15.ast.TypeCheckVisitor.TypeCheckException;
import cop5555sp15.symbolTable.SymbolTable;
public class CodeletBuilder 
{
	
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
		//assertNotNull(ast);
		return ast;
	}
	
	private static byte[] generateByteCode(ASTNode ast) throws Exception {
		CodeGenVisitor v = new CodeGenVisitor();
			byte[] bytecode = (byte[]) ast.visit(v, null);
			//dumpBytecode(bytecode);
			return bytecode;
	}
	
	
	private static ASTNode typeCheckCorrectAST(ASTNode ast) throws Exception {
		SymbolTable symbolTable = new SymbolTable();
		TypeCheckVisitor v = new TypeCheckVisitor(symbolTable);
		try {
			ast.visit(v, null);
		} catch (TypeCheckException e) {
			System.out.println(e.getMessage());
			
		}
		return ast;
	}
	
	
	public static Codelet newInstance(String source) throws Exception
	{
		
		Program program = (Program) parseCorrectInput(source);
		typeCheckCorrectAST(program);
		byte[] bytecode = generateByteCode(program);
		DynamicClassLoader loader = new DynamicClassLoader(Thread
                .currentThread().getContextClassLoader());
        Class<?> testClass = loader.define(program.JVMName, bytecode);
        Codelet codelet = (Codelet) testClass.newInstance();
		return codelet;
	}
	
	public static Codelet newInstance(File file) throws Exception 
	{
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line=null;
		StringBuilder sb=new StringBuilder();
		while((line=br.readLine())!=null)
		{
			sb.append(line);
			
		}	
		br.close();
		
		return newInstance(sb.toString());
	}
	
	@SuppressWarnings("rawtypes")
	public static List getList(Codelet codelet, String name) throws Exception
	{
	//TODO
		
		Class<? extends Codelet> codeletClass = codelet.getClass();
		Field l1Field = codeletClass.getDeclaredField(name);
		l1Field.setAccessible(true);
		List l = (ArrayList) l1Field.get(codelet);
		return l;
	
	}
	
	public static int getInt(Codelet codelet, String name) throws Exception
	{
		//TODO
		Class<? extends Codelet> codeletClass = codelet.getClass();
		Field l1Field = codeletClass.getDeclaredField(name);
		l1Field.setAccessible(true);
		int i = (int) l1Field.get(codelet);
		return i;
	}
	
	public static void setInt(Codelet codelet, String name, int value) throws Exception
	{
		
		Class<? extends Codelet> codeletClass = codelet.getClass();
		Field l1Field = codeletClass.getDeclaredField(name);
		l1Field.setAccessible(true);
		l1Field.set(codelet, value);
	}
	
	
	public static String getString(Codelet codelet, String name) throws Exception
	{
		
		Class<? extends Codelet> codeletClass = codelet.getClass();
		Field l1Field = codeletClass.getDeclaredField(name);
		l1Field.setAccessible(true);
		String s = (String) l1Field.get(codelet);
		return s;
	}
	public static void setString(Codelet codelet, String name, String value) throws Exception
	{
		
		Class<? extends Codelet> codeletClass = codelet.getClass();
		Field l1Field = codeletClass.getDeclaredField(name);
		l1Field.setAccessible(true);
		l1Field.set(codelet, value);
	}
	
	public static boolean getBoolean(Codelet codelet, String name) throws Exception
	{
		
		Class<? extends Codelet> codeletClass = codelet.getClass();
		Field l1Field = codeletClass.getDeclaredField(name);
		l1Field.setAccessible(true);
		boolean b = (boolean) l1Field.get(codelet);
		return b;
	}

	public static void setBoolean(Codelet codelet, String name, boolean value) throws Exception
	{
		
		Class<? extends Codelet> codeletClass = codelet.getClass();
		Field l1Field = codeletClass.getDeclaredField(name);
		l1Field.setAccessible(true);
		l1Field.set(codelet, value);
	}
}