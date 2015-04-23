package cop5555sp15;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import cop5555sp15.Parser.SyntaxException;
import cop5555sp15.TokenStream.Kind;
import cop5555sp15.ast.ASTNode;
import cop5555sp15.TokenStream;
import static cop5555sp15.TokenStream.Kind.*;

public class MyTestParser {

	public static void main(String[] args) 
	{
		System.out.println("***********\nfactor1");
		String input = "class A {def B:int; def C:boolean; def S: strings;  def F: sing} ";
		System.out.println(input);
		parseIncorrectInput(input,IDENT, IDENT);
		//System.out.println(parseCorrectInput(input));

	}

	private static ASTNode parseCorrectInput(String input) 
	{
		TokenStream stream = new TokenStream(input);
		Scanner scanner = new Scanner(stream);
		scanner.scan();
		Parser parser = new Parser(stream);
		System.out.println();
		ASTNode ast = parser.parse();
		//assertNotNull(ast);
		return ast;
	}
	
	private static void parseIncorrectInput(String input,
			Kind... expectedIncorrectTokenKind) {
		TokenStream stream = new TokenStream(input);
		Scanner scanner = new Scanner(stream);
		scanner.scan();
		Parser parser = new Parser(stream);
//		System.out.println(stream);
		ASTNode ast =parser.parse();
		assertNull(ast);
		List<SyntaxException> exceptions = parser.getExceptionList();
		for(SyntaxException e: exceptions){
			System.out.println(e.getMessage());
		}
			assertEquals(expectedIncorrectTokenKind.length, exceptions.size());
			for (int i = 0; i < exceptions.size(); ++i){
			assertEquals(expectedIncorrectTokenKind[i], exceptions.get(i).t.kind); // class is the incorrect token
		}
	}
}
