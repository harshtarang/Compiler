package cop5555sp15;

import cop5555sp15.SimpleParser.SyntaxException;

public class MyParserTester 
{

	public static void main(String[] args) throws SyntaxException
	{
		System.out.println("almostEmpty");
		//String input = "class A {def C={->x= &y; z = !y;};} ";
		//String input="class A {def c:;} ";
		String input ="class A {def C={def s:@@[string:string],def i:@[int]->x=1;};} ";
		System.out.println(input);
		parseCorrectInput(input);

	}
	
	private static void parseCorrectInput(String input) throws SyntaxException 
	{
		TokenStream stream = new TokenStream(input);
		Scanner scanner = new Scanner(stream);
		scanner.scan();
		System.out.println(stream);
		SimpleParser parser = new SimpleParser(stream);
		parser.parse();
	}	

}
