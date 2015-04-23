package cop5555sp15;

import static org.junit.Assert.*;

import org.junit.Test;
import cop5555sp15.SimpleParser.SyntaxException;
import cop5555sp15.TokenStream.Kind;
import static cop5555sp15.TokenStream.Kind.*;

public class TestSimpleParser1 {

	
	private void parseIncorrectInput(String input,
			Kind ExpectedIncorrectTokenKind) {
		TokenStream stream = new TokenStream(input);
		Scanner scanner = new Scanner(stream);
		scanner.scan();
		SimpleParser parser = new SimpleParser(stream);
		System.out.println(stream);
		try {
			parser.parse();
			fail("expected syntax error");
		} catch (SyntaxException e) {
			assertEquals(ExpectedIncorrectTokenKind, e.t.kind); // class is the incorrect token
		}
	}
	
	private void parseCorrectInput(String input) throws SyntaxException {
		TokenStream stream = new TokenStream(input);
		Scanner scanner = new Scanner(stream);
		scanner.scan();
		System.out.println(stream);
		SimpleParser parser = new SimpleParser(stream);
		parser.parse();
	}	


	/**This is an example of testing correct input
	 * Just call parseCorrectInput
	 * @throws SyntaxException
	 */
	@Test
	public void almostEmpty() throws SyntaxException {
		System.out.println("almostEmpty");
		String input = "class A { } ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	
	/**This is an example of testing incorrect input.
	 * The second parameter to parseIncorrectInput is
	 * the Kind of the erroneous token.
	 * For example, in this test, the ] should be a },
	 * so the parameter is RSQUARE

	 * @throws SyntaxException
	 */
	@Test
	public void almostEmptyIncorrect() throws SyntaxException {
		System.out.println("almostEmpty");
		String input = "class A { ] ";
		System.out.println(input);
		parseIncorrectInput(input,RSQUARE);		
	}

	@Test
	public void almostEmptyCorrect() throws SyntaxException {
		System.out.println("almostEmpty Correct");
		String input = "class A { } ";
		System.out.println(input);
		parseCorrectInput(input);		
	}

	@Test
	public void emptyIncorrect() throws SyntaxException {
		System.out.println("emptyIncorrect");
		String input = "";
		System.out.println(input);
		parseIncorrectInput(input, EOF);		
	}

	@Test
	public void import1() throws SyntaxException {
		System.out.println("import1");
		String input = "import X; class A { } ";
		System.out.println(input);
		parseCorrectInput(input);
	}

	@Test
	public void import11() throws SyntaxException {
		System.out.println("import11");
		String input = "import X; class A { ";
		System.out.println(input);
		parseIncorrectInput(input, EOF);
	}

	@Test
	public void import12() throws SyntaxException {
		System.out.println("import12");
		String input = "import X class A { ";
		System.out.println(input);
		parseIncorrectInput(input, KW_CLASS);
	}
	
	@Test
	public void import2() throws SyntaxException {
		System.out.println("import2");
		String input = "import X.Y.Z; import W.X.Y; class A { } ";
		System.out.println(input);
		parseCorrectInput(input);
	}

	@Test
	public void import21() throws SyntaxException {
		System.out.println("import2");
		String input = "import X.Y..Z; import W.X.Y; class A { } ";
		System.out.println(input);
		parseIncorrectInput(input, RANGE);
	}
	
	@Test
	public void import3() throws SyntaxException {
		System.out.println("import2");
		String input = "import class A { } "; // this input is wrong.
		System.out.println(input);
		Kind ExpectedIncorrectTokenKind = KW_CLASS;
		parseIncorrectInput(input, ExpectedIncorrectTokenKind);
	}

	@Test
	public void def_simple_type1() throws SyntaxException {
		System.out.println("def_simple_type1");
		String input = "class A {def B:int; def C:boolean; def S: string;} ";
		System.out.println(input);
		parseCorrectInput(input);
	}

	@Test
	public void def_simple_type11() throws SyntaxException {
		System.out.println("def_simple_type11");
		String input = "class A {def B:int;; def C:boolean;; def S; def S  ; } ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void def_simple_type2() throws SyntaxException {
		System.out.println("def_simple_type2");
		String input = "class A {def B:int; def C:boolean; def S: string} ";
		System.out.println(input);
		parseIncorrectInput(input, RCURLY);
	}
	
	@Test
	public void def_key_value_type1() throws SyntaxException {
		System.out.println("def_key_value_type1");
		String input = "class A {def C:@[string]; def S:@@[int:boolean];} ";
		System.out.println(input);
		parseCorrectInput(input);
	}	
	
	@Test
	public void def_key_value_type2() throws SyntaxException {
		System.out.println("def_key_value_type1");
		String input = "class A {def C:@[string]; def S:@@[int:@@[string:boolean]];} ";
		System.out.println(input);
		parseCorrectInput(input);
	}	

	@Test
	public void def_type_nesting() throws SyntaxException {
		System.out.println("def_type_nesting");
		String input = "import a; class A {def C:@[string]; def S:@@[int:@@[string:boolean]]; "
				+ " def C:@@[string:string]; def D:@@[string:@[int]]; def E:@[@@[string:@@[int:@[@[int]]]]]; } ";
		System.out.println(input);
		parseCorrectInput(input);
	}	
	
	@Test
	public void def_closure1() throws SyntaxException {
		System.out.println("def_closure1");
		String input = "class A {def C={->};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}

	@Test
	public void def_closure11() throws SyntaxException {
		System.out.println("def_closure11");
		String input = "class A {def C={a->};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}

	@Test
	public void def_closure12() throws SyntaxException {
		System.out.println("def_closure11");
		String input = "class A {def C={a:int->};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}

	@Test
	public void def_closure13() throws SyntaxException {
		System.out.println("def_closure13");
		String input = "class A {def C={a:int,b:string->};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}

	@Test
	public void def_closure14() throws SyntaxException {
		System.out.println("def_closure14");
		String input = "class A {def C={a,b,c->};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}

	@Test
	public void def_closure15() throws SyntaxException {
		System.out.println("def_closure15");
		String input = "class A {def C={a:int,b,c:string->;}; def D={->}; def E={a,b->};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void def_closure2() throws SyntaxException {
		System.out.println("def_closure2");
		String input = "class A {def C={->};  def z:string;} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void factor1() throws SyntaxException {
		System.out.println("factor1");
		String input = "class A {def C={->x=y;};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	
	@Test
	public void factor2() throws SyntaxException {
		System.out.println("factor2");
		String input = "class A {def C={->x=y[z];};  def D={->x=y[1];};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void factor3() throws SyntaxException {
		System.out.println("factor3");
		String input = "class A {def C={->x=3;};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void factor4() throws SyntaxException {
		System.out.println("factor4");
		String input = "class A {def C={->x=\"hello\";};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void factor5() throws SyntaxException {
		System.out.println("factor5");
		String input = "class A {def C={->x=true; z = false;};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	
	@Test
	public void factor6() throws SyntaxException {
		System.out.println("factor6");
		String input = "class A {def C={->x=-y; z = !y;};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void factor7() throws SyntaxException {
		System.out.println("factor7");
		String input = "class A {def C={->x= &y; z = !y;};} ";
		System.out.println(input);
		parseIncorrectInput(input,AND);
	}
	
	@Test
	public void expressions1() throws SyntaxException {
		System.out.println("expressions1");
		String input = "class A {def C={->x=x+1; z = 3-4-5;};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}

	@Test
	public void expressions11() throws SyntaxException {
		System.out.println("expressions1");
		String input = "class A {def C={->x=x+1; z = 3-4-5;};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void expressions2() throws SyntaxException {
		System.out.println("expressions2");
		String input = "class A {def C={->x=x+1/2*3--4; z = 3-4-5;};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void expressions3() throws SyntaxException {
		System.out.println("expressions3");
		String input = "class A {def C={->x=x+1/2*3--4; z = 3-4-5;};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void expressions4() throws SyntaxException {
		System.out.println("expressions4");
		String input = "class A {x = a<<b; c = b>>z;} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void statements1()throws SyntaxException {
		System.out.println("statements1");
		String input = "class A {x = y; z[1] = b; print a+b; print (x+y-z);} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void statements2()throws SyntaxException {
		System.out.println("statements2");
		String input = "class A  {\n while (x) {};  \n while* (1..4){}; } ";
		System.out.println(input);
		parseCorrectInput(input);
	} 

	@Test
	public void statements21()throws SyntaxException {
		System.out.println("statements21");
		String input = "class A  {\n while (x) {};  \n while* (1..4){}; while*(x) {}; } ";
		System.out.println(input);
		parseCorrectInput(input);
	} 
	
	@Test
	public void statements3()throws SyntaxException {
		System.out.println("statements3");
		String input = "class A  {\n if (x) {};  \n if (y){} else {}; \n if (x) {} else {if (z) {} else {};} ; } ";
		System.out.println(input);
		parseCorrectInput(input);
	} 

	@Test
	public void statements31()throws SyntaxException {
		System.out.println("statements31");
		String input = "class A  {\n if (x) {};  \n if (y){} else {}; \n if (x) {} else {if (z) {} else {};} ; \n if (x) {if(x) {}; };} ";
		System.out.println(input);
		parseCorrectInput(input);
	} 

	@Test
	public void statements32()throws SyntaxException {
		System.out.println("statements31");
		String input = "class A  {\n while*(x..y) {if(x) { %y;} else{}; };  \n if (y){ while(x){print a;}; } else { while*(x){return z;}; }; } ";
		System.out.println(input);
		parseCorrectInput(input);
	} 
	
	@Test
	public void emptyStatement()throws SyntaxException {
		System.out.println("emptyStatement");
		String input = "class A  { ;;; } ";
		System.out.println(input);
		parseCorrectInput(input);
	} 	
	
	@Test
	public void statements4()throws SyntaxException {
		System.out.println("statements4");
		String input = "class A  { %a(1,2,3); } ";
		System.out.println(input);
		parseCorrectInput(input);
	} 	
	
	@Test
	public void statements5()throws SyntaxException {
		System.out.println("statements5");
		String input = "class A  { x = a(1,2,3); } ";
		System.out.println(input);
		parseCorrectInput(input);
	} 	
	
	@Test
	public void closureEval()throws SyntaxException {
		System.out.println("closureEva");
		String input = "class A  { x[z] = a(1,2,3); } ";
		System.out.println(input);
		parseCorrectInput(input);
	} 	
	
	@Test
	public void list1()throws SyntaxException {
		System.out.println("list1");
		String input = "class A  { \n x = @[a,b,c]; \n y = @[d,e,f]+x; \n } ";
		System.out.println(input);
		parseCorrectInput(input);
	} 	
	
	@Test
	public void maplist1()throws SyntaxException {
		System.out.println("maplist1");
		String input = "class A  { x = @@[x:y]; y = @@[x:y,4:5]; } ";
		System.out.println(input);
		parseCorrectInput(input);
	} 	
}