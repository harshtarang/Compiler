package cop5555sp15;

import static cop5555sp15.TokenStream.Kind.AND;
import static cop5555sp15.TokenStream.Kind.ARROW;
import static cop5555sp15.TokenStream.Kind.ASSIGN;
import static cop5555sp15.TokenStream.Kind.AT;
import static cop5555sp15.TokenStream.Kind.BAR;
import static cop5555sp15.TokenStream.Kind.BL_FALSE;
import static cop5555sp15.TokenStream.Kind.BL_TRUE;
import static cop5555sp15.TokenStream.Kind.COLON;
import static cop5555sp15.TokenStream.Kind.COMMA;
import static cop5555sp15.TokenStream.Kind.DIV;
import static cop5555sp15.TokenStream.Kind.DOT;
import static cop5555sp15.TokenStream.Kind.EOF;
import static cop5555sp15.TokenStream.Kind.EQUAL;
import static cop5555sp15.TokenStream.Kind.GE;
import static cop5555sp15.TokenStream.Kind.GT;
import static cop5555sp15.TokenStream.Kind.IDENT;
import static cop5555sp15.TokenStream.Kind.INT_LIT;
import static cop5555sp15.TokenStream.Kind.KW_BOOLEAN;
import static cop5555sp15.TokenStream.Kind.KW_CLASS;
import static cop5555sp15.TokenStream.Kind.KW_DEF;
import static cop5555sp15.TokenStream.Kind.KW_ELSE;
import static cop5555sp15.TokenStream.Kind.KW_IF;
import static cop5555sp15.TokenStream.Kind.KW_IMPORT;
import static cop5555sp15.TokenStream.Kind.KW_INT;
import static cop5555sp15.TokenStream.Kind.KW_PRINT;
import static cop5555sp15.TokenStream.Kind.KW_RETURN;
import static cop5555sp15.TokenStream.Kind.KW_STRING;
import static cop5555sp15.TokenStream.Kind.KW_WHILE;
import static cop5555sp15.TokenStream.Kind.KW_KEY;
import static cop5555sp15.TokenStream.Kind.KW_SIZE;
import static cop5555sp15.TokenStream.Kind.KW_VALUE;
import static cop5555sp15.TokenStream.Kind.LCURLY;
import static cop5555sp15.TokenStream.Kind.LE;
import static cop5555sp15.TokenStream.Kind.LPAREN;
import static cop5555sp15.TokenStream.Kind.LSHIFT;
import static cop5555sp15.TokenStream.Kind.LSQUARE;
import static cop5555sp15.TokenStream.Kind.LT;
import static cop5555sp15.TokenStream.Kind.MINUS;
import static cop5555sp15.TokenStream.Kind.MOD;
import static cop5555sp15.TokenStream.Kind.NOT;
import static cop5555sp15.TokenStream.Kind.NOTEQUAL;
import static cop5555sp15.TokenStream.Kind.PLUS;
import static cop5555sp15.TokenStream.Kind.RANGE;
import static cop5555sp15.TokenStream.Kind.RCURLY;
import static cop5555sp15.TokenStream.Kind.RPAREN;
import static cop5555sp15.TokenStream.Kind.RSHIFT;
import static cop5555sp15.TokenStream.Kind.RSQUARE;
import static cop5555sp15.TokenStream.Kind.SEMICOLON;
import static cop5555sp15.TokenStream.Kind.STRING_LIT;
import static cop5555sp15.TokenStream.Kind.TIMES;

import java.util.ArrayList;
import java.util.List;

import cop5555sp15.CopyOfParser.SyntaxException;
import cop5555sp15.TokenStream.Kind;
import cop5555sp15.TokenStream.Token;
import cop5555sp15.ast.AssignmentStatement;
import cop5555sp15.ast.BinaryExpression;
import cop5555sp15.ast.BooleanLitExpression;
import cop5555sp15.ast.Closure;
import cop5555sp15.ast.ClosureDec;
import cop5555sp15.ast.ClosureEvalExpression;
import cop5555sp15.ast.ClosureExpression;
import cop5555sp15.ast.Declaration;
import cop5555sp15.ast.Expression;
import cop5555sp15.ast.ExpressionLValue;
import cop5555sp15.ast.ExpressionStatement;
import cop5555sp15.ast.IdentExpression;
import cop5555sp15.ast.IdentLValue;
import cop5555sp15.ast.IfElseStatement;
import cop5555sp15.ast.IfStatement;
import cop5555sp15.ast.IntLitExpression;
import cop5555sp15.ast.KeyExpression;
import cop5555sp15.ast.KeyValueExpression;
import cop5555sp15.ast.KeyValueType;
import cop5555sp15.ast.LValue;
import cop5555sp15.ast.ListExpression;
import cop5555sp15.ast.ListOrMapElemExpression;
import cop5555sp15.ast.ListType;
import cop5555sp15.ast.MapListExpression;
import cop5555sp15.ast.PrintStatement;
import cop5555sp15.ast.Program;
import cop5555sp15.ast.QualifiedName;
import cop5555sp15.ast.Block;
import cop5555sp15.ast.BlockElem;
import cop5555sp15.ast.RangeExpression;
import cop5555sp15.ast.ReturnStatement;
import cop5555sp15.ast.SimpleType;
import cop5555sp15.ast.SizeExpression;
import cop5555sp15.ast.Statement;
import cop5555sp15.ast.StringLitExpression;
import cop5555sp15.ast.Type;
import cop5555sp15.ast.UnaryExpression;
import cop5555sp15.ast.UndeclaredType;
import cop5555sp15.ast.ValueExpression;
import cop5555sp15.ast.VarDec;
import cop5555sp15.ast.WhileRangeStatement;
import cop5555sp15.ast.WhileStarStatement;
import cop5555sp15.ast.WhileStatement;

public class CopyOfParser {

	@SuppressWarnings("serial")
	public class SyntaxException extends Exception {
		Token t;
		Kind[] expected;
		String msg;

		SyntaxException(Token t, Kind expected) {
			this.t = t;
			msg = "";
			this.expected = new Kind[1];
			this.expected[0] = expected;

		}

		public SyntaxException(Token t, String msg) {
			this.t = t;
			this.msg = msg;
		}

		public SyntaxException(Token t, Kind[] expected) {
			this.t = t;
			msg = "";
			this.expected = expected;
		}

		public String getMessage() {
			StringBuilder sb = new StringBuilder();
			sb.append(" error at token ").append(t.toString()).append(" ")
					.append(msg);
			sb.append(". Expected: ");
			for (Kind kind : expected) {
				sb.append(kind).append(" ");
			}
			return sb.toString();
		}
	}

	TokenStream tokens;
	Token t;
	List<SyntaxException> exceptionList;

	public CopyOfParser(TokenStream tokens) {
		this.tokens = tokens;
		t = tokens.nextToken();
	}

	private Kind match(Kind kind) throws SyntaxException {
		if (isKind(kind)) {
			consume();
			return kind;
		}
		throw new SyntaxException(t, kind);
	}

	private Kind match(Kind... kinds) throws SyntaxException {
		Kind kind = t.kind;
		if (isKind(kinds)) {
			consume();
			return kind;
		}
		StringBuilder sb = new StringBuilder();
		for (Kind kind1 : kinds) {
			sb.append(kind1).append(kind1).append(" ");
		}
		throw new SyntaxException(t, "expected one of " + sb.toString());
	}

	private boolean isKind(Kind kind) {
		return (t.kind == kind);
	}
	
	/*
	 * isKindNextToken verifies the kind of the next token(not the current token t)
	 */
	
	private boolean isKindNextToken(Kind kind) 
	{
		
		Token temp=null;
		if (t.kind != EOF)
			temp = tokens.peekNextToken();
		return (temp.kind == kind);
	}

	private void consume() {
		if (t.kind != EOF)
			t = tokens.nextToken();
	}

	private boolean isKind(Kind... kinds) {
		for (Kind kind : kinds) {
			if (t.kind == kind)
				return true;
		}
		return false;
	}

	//This is a convenient way to represent fixed sets of
	//token kinds.  You can pass these to isKind.
	static final Kind[] REL_OPS = { BAR, AND, EQUAL, NOTEQUAL, LT, GT, LE, GE };
	static final Kind[] WEAK_OPS = { PLUS, MINUS };
	static final Kind[] STRONG_OPS = { TIMES, DIV };
	static final Kind[] VERY_STRONG_OPS = { LSHIFT, RSHIFT };
	static final Kind[]	FACTOR_PREDICT = {IDENT,INT_LIT,BL_FALSE,BL_TRUE,STRING_LIT,LPAREN,NOT,MINUS,KW_SIZE,KW_KEY,KW_VALUE,AT,LCURLY};


	public Program parse() 
	{
		Program p=null;
		exceptionList = new ArrayList<SyntaxException>();
		try
		{
			p=Program();
			if(p!=null)
			match(EOF);
		}
		catch(SyntaxException e)
		{
			exceptionList.add(e);
		}
		
		if (exceptionList.isEmpty())
			return p;
		else
			return null;
	}

	private Program Program() throws SyntaxException 
	{
		List<QualifiedName> importList=ImportList();
		Token firstToken=t;
		match(KW_CLASS);
		String name=t.getText();
		match(IDENT);
		Block block=Block();
		return new Program(firstToken,importList,name,block);
	}

	private List<QualifiedName> ImportList() throws SyntaxException 
	{
		List<QualifiedName> list=new ArrayList<QualifiedName>();
		while(isKind(KW_IMPORT))
		{
			Token firstToken=t;
			match(KW_IMPORT);
			String name=t.getText();
			match(IDENT);
			
			while(isKind(DOT))
			{
				match(DOT);
				name=name+"/"+t.getText();
				match(IDENT);
			}
			match(SEMICOLON);
			list.add(new QualifiedName(firstToken,name));
		}
		return list;
		
	}

	private Block Block() throws SyntaxException 
	{
		Token firstToken=t;
		List<BlockElem> list=new ArrayList<BlockElem>();
		BlockElem blockElem=null;
		match(LCURLY);
		while(isKind(KW_DEF) || isKind(IDENT) || isKind(KW_PRINT) || isKind(KW_WHILE) || isKind(KW_IF) || isKind(MOD) || isKind(KW_RETURN) || isKind(SEMICOLON))
		{
			if(isKind(IDENT) || isKind(KW_PRINT) || isKind(KW_WHILE) || isKind(KW_IF) || isKind(MOD) || isKind(KW_RETURN) || isKind(SEMICOLON))
			{
				blockElem=statement();
				
			}
			else 
				blockElem=declaration();
			
			if(blockElem!=null)
				list.add(blockElem);
			match(SEMICOLON);
		}
		
		match(RCURLY);
		return new Block(firstToken,list);
	}

	private Statement statement() throws SyntaxException 
	{
		if(isKind(SEMICOLON))
			return null;
		Statement statement=null;
		Token firstToken =t;
		Expression expr=null;
		switch(t.kind)
		{
		case IDENT:	LValue lvalue=lValue();
					match(ASSIGN);
					expr=expression();
					statement=new AssignmentStatement(firstToken,lvalue,expr);
					break;
					
		case KW_PRINT:	consume();
						expr=expression();
						statement = new PrintStatement(firstToken,expr);
						break;
						
		case KW_WHILE: consume();
						if(isKind(TIMES))
						{
							consume();
							match(LPAREN);
							expr=expression();
							
							if(isKind(RANGE))
							{
								consume();
								Expression expr2=expression();
								match(RPAREN);
								Block block=Block();
								RangeExpression rangeExpr=new RangeExpression(firstToken,expr,expr2);
								statement= new WhileRangeStatement(firstToken,rangeExpr,block);
							}
							else
							{
								
								match(RPAREN);
								Block block=Block();
								statement= new WhileStarStatement(firstToken,expr,block);
							}
						}
						else
						{
							
							match(LPAREN);
							expr=expression();
							match(RPAREN);
							Block block=Block();
							statement= new WhileStatement(firstToken,expr,block);
						}
						break;
						
		case KW_IF:	consume();
					match(LPAREN);
					expr=expression();
					match(RPAREN);
					Block block=Block();
					if(isKind(KW_ELSE))
					{
						consume();
						Block block2=Block();
						statement= new IfElseStatement(firstToken,expr,block,block2);
					}
					else
					{
						statement=new IfStatement(firstToken,expr,block);
					}
					break;
		
		case KW_RETURN: consume();
						expr=expression();
						statement=new ReturnStatement(firstToken,expr);
						break;
			
		case MOD:	consume();
					 expr=expression();
					statement=new ExpressionStatement(firstToken,expr);
					break;
		}
		
				return statement;
	}

	

	private Expression expression() throws SyntaxException
	{
		Token firstToken=t;
		Expression expression0=null;
		Expression expression1=null;
		Token op=null;
		
		Token oldOp=null;
		Expression prevExpr=null;
		
		expression0=Term();
		while(isKind(REL_OPS))
		{
			op=t;
			consume();
			if(prevExpr==null)
				expression1=Term();
			else
			{
				expression1=Term();
				expression0=new BinaryExpression(firstToken,expression0,oldOp,prevExpr);
			}
			prevExpr=expression1;
			oldOp=op;
		
		}
		if(expression1==null)
			return expression0;
		return new BinaryExpression(firstToken,expression0,op,expression1);
	}

	private Expression Term() throws SyntaxException
	{
		Token firstToken=t;
		Expression expression0=null;
		Expression expression1=null;
		Token op=null;
		
		Token oldOp=null;
		Expression prevExpr=null;
		
		expression0=Elem();
		
		while(isKind(WEAK_OPS))
		{
			op=t;
			consume();
			if(prevExpr==null)
				expression1=Elem();
			else
			{
				expression1=Elem();
				expression0=new BinaryExpression(firstToken,expression0,oldOp,prevExpr);
			}
			prevExpr=expression1;
			oldOp=op;
		}
		if(expression1==null)
			return expression0;
		return new BinaryExpression(firstToken,expression0,op,expression1);
		
	}

	private Expression Elem() throws SyntaxException
	{
		Token firstToken=t;
		Expression expression0=null;
		Expression expression1=null;
		Token op=null;
		
		Token oldOp=null;
		Expression prevExpr=null;
		
		expression0=Thing();
		while(isKind(STRONG_OPS))
		{
			op=t;
			consume();
			if(prevExpr==null)
				expression1=Thing();
			else
			{
				expression1=Thing();
				expression0=new BinaryExpression(firstToken,expression0,oldOp,prevExpr);
			}
			prevExpr=expression1;
			oldOp=op;
		}
		if(expression1==null)
			return expression0;
		return new BinaryExpression(firstToken,expression0,op,expression1);
	}

	private Expression Thing() throws SyntaxException
	{
		
		Token firstToken=t;
		Expression expression0=null;
		Expression expression1=null;
		Token op=null;
		
		Token oldOp=null;
		Expression prevExpr=null;
		
		expression0=Factor();
		while(isKind(VERY_STRONG_OPS))
		{
			op=t;
			consume();

			if(prevExpr==null)
				expression1=Factor();
			else
			{
				expression1=Factor();
				expression0=new BinaryExpression(firstToken,expression0,oldOp,prevExpr);
			}
			prevExpr=expression1;
			oldOp=op;
		}
		if(expression1==null)
			return expression0;
		return new BinaryExpression(firstToken,expression0,op,expression1);
		
	}

	private Expression Factor() throws SyntaxException
	{
		Token firstToken=t;
		Expression expr=null;
		switch(t.kind)
		{
			case IDENT:	if(isKindNextToken(LPAREN))
							expr=closureEvalExpression();
						else
						{
							Token identToken=t;
							consume();
							if(isKind(LSQUARE))
							{
								consume();
								Expression expression=expression();
								expr=new ListOrMapElemExpression(firstToken,identToken,expression);
								match(RSQUARE);
							}
							else
								expr=new IdentExpression(firstToken,identToken);
						}
						break;
			
			case BL_TRUE:	expr=new BooleanLitExpression(firstToken,true);
							consume();
							break;
			case BL_FALSE:	expr=new BooleanLitExpression(firstToken,false);
							consume();
							break;
			case STRING_LIT:	expr=new StringLitExpression(firstToken,t.getText());
								consume();
								break;
			case INT_LIT:	expr=new IntLitExpression(firstToken,Integer.parseInt(t.getText()));
							consume();
							break;
							
			case LPAREN:	consume();
							expr=expression();
							match(RPAREN);
							break;
							
			case NOT:		
			case MINUS:		Token op=t;
							
							consume();
							Expression expression=Factor();
							expr = new UnaryExpression(firstToken,op,expression);
							break;
							
			case KW_SIZE:	consume();
							match(LPAREN);
							expression=expression();
							match(RPAREN);
							expr=new SizeExpression(firstToken,expression);
							break;
							
			case KW_KEY:	consume();
							match(LPAREN);
							expression=expression();
							match(RPAREN);
							expr=new KeyExpression(firstToken,expression);
							break;
							
			case KW_VALUE:	consume();
							match(LPAREN);
							expression=expression();
							match(RPAREN);
							expr=new ValueExpression(firstToken,expression);
							break;
							
			case AT:	if(isKindNextToken(AT))
							expr=MapList();
						else
							expr=List();
						break;
						
			case LCURLY:	expr=new ClosureExpression(firstToken,closure());
							break;
						
			default:	throw new SyntaxException(t, IDENT);
							
		}
		return expr;
	}

	private ClosureEvalExpression closureEvalExpression() throws SyntaxException
	{
		Token firstToken=t;
		Token identToken=t;
		match(IDENT);
		match(LPAREN);
		List<Expression> expressionList=expressionList();
		match(RPAREN);
		return new ClosureEvalExpression(firstToken, identToken, expressionList);
	}

	private List<Expression> expressionList() throws SyntaxException
	{
		List<Expression> expressionList=new ArrayList<Expression>();
		if(isKind(FACTOR_PREDICT))
		{
			expressionList.add(expression());
			while(isKind(COMMA))
			{
				consume();
				expressionList.add(expression());
			}
			
		}
		return expressionList;
		
	}

	private ListExpression List() throws SyntaxException
	{
		Token firstToken=t;
		List<Expression> expressionList=null;
		match(AT);
		match(LSQUARE);
		expressionList=expressionList();
		match(RSQUARE);
		return new ListExpression(firstToken,expressionList);
	}

	private MapListExpression MapList() throws SyntaxException
	{
		Token firstToken=t;
		List<KeyValueExpression> keyValueList=null;
		match(AT);
		match(AT);
		match(LSQUARE);
		keyValueList=KeyValueList();
		match(RSQUARE);
		return new MapListExpression(firstToken,keyValueList);
	}

	private List<KeyValueExpression> KeyValueList() throws SyntaxException
	{
		List<KeyValueExpression> keyValueList=new ArrayList<KeyValueExpression>();
		if(isKind(FACTOR_PREDICT))
		{
			keyValueList.add(KeyValueExpression());
			while(isKind(COMMA))
			{
				consume();
				keyValueList.add(KeyValueExpression());
			}
		}
		return keyValueList;
	}

	private KeyValueExpression KeyValueExpression() throws SyntaxException
	{
		Expression key;
		Expression value;
		Token firstToken=t;
		key=expression();
		match(COLON);
		value=expression();
		return new KeyValueExpression(firstToken,key,value);
		
	}

	private LValue lValue() throws SyntaxException{
		
		Token firstToken=t;
		Token identToken=t;
		Expression expr=null;
		match(IDENT);
		if(isKind(LSQUARE))
		{	
			consume();
			expr=expression();
			match(RSQUARE);
			return new ExpressionLValue(firstToken,identToken,expr);
		}
		
		return new IdentLValue(firstToken,identToken);
	}

	private Declaration declaration() throws SyntaxException 
	{
		
		Token firstToken =t;
		Declaration dec=null;
		match(KW_DEF);
		if((isKind(IDENT) && isKindNextToken(COLON)) || (isKind(IDENT) && isKindNextToken(SEMICOLON)))
			dec=vardec();
		else
			dec=closuredec();
		return dec;
		
	}

	private ClosureDec closuredec() throws SyntaxException
	{
		Token firstToken=t;
		Token identToken=t;
		Closure closure=null;
		match(IDENT);
		match(ASSIGN);
		closure=closure();
		return new ClosureDec(firstToken,identToken,closure);
	}

	

	private Closure closure() throws SyntaxException
	{
		Token firstToken=t;
		List<VarDec> formalArgList=new ArrayList<VarDec>();
		List<Statement> statementList=new ArrayList<Statement>();
		match(LCURLY);
		formalArgList=formalArgList();
		match(ARROW);
		while(isKind(IDENT) || isKind(KW_PRINT) || isKind(KW_WHILE) || isKind(KW_IF) || isKind(MOD) || isKind(KW_RETURN) || isKind(SEMICOLON))
		{
			Statement st=statement();
			statementList.add(st);
			match(SEMICOLON);
		}
		match(RCURLY);
		Closure closure= new Closure(firstToken,formalArgList,statementList);
		return closure;
	}

	private List<VarDec> formalArgList() throws SyntaxException
	{
	
		List<VarDec> varDecList=new ArrayList<VarDec>();
		VarDec varDec=null;
		if(isKind(ARROW))
			return varDecList;
		varDec=vardec();
		varDecList.add(varDec);
		while(isKind(COMMA))
		{
			match(COMMA);
			varDec=vardec();
			varDecList.add(varDec);
		}
		return varDecList;
	}

	private VarDec vardec() throws SyntaxException
	{
		Token firstToken=t;
		Token identToken=t;
		Type type=null;
		match(IDENT);
		if(isKind(COLON))
		{
			consume();
			type=type();
		}
		else return new VarDec(firstToken,identToken,new UndeclaredType(firstToken));
		return new VarDec(firstToken,identToken,type);
		
	}

	private Type type() throws SyntaxException
	{
		Type type=null;
		
		if(isKind(KW_INT) || isKind(KW_BOOLEAN) || isKind(KW_STRING))
			type=simpleType();
		else if(isKind(AT) && isKindNextToken(AT))
			type=keyValueType();
		else //if(isKind(AT) && isKindNextToken(LSQUARE))
			type=listType();
		return type;
	}

	private ListType listType() throws SyntaxException
	{
		
		Token firstToken=t;
		Type type=null;
		match(AT);
		match(LSQUARE);
		type=type();
		match(RSQUARE);
		return new ListType(firstToken,type);
		
	}

	private KeyValueType keyValueType() throws SyntaxException
	{
		Token firstToken=t;
		SimpleType keyType=null;
		Type valueType=null;
		match(AT);
		match(AT);
		match(LSQUARE);
		keyType=simpleType();
		match(COLON);
		valueType=type();
		match(RSQUARE);
		
		return new KeyValueType(firstToken,keyType,valueType);
	}

	private SimpleType simpleType() throws SyntaxException
	{
		Token firstToken=t;
		Token type=t;
		if(isKind(KW_INT))
			match(KW_INT);
		else if(isKind(KW_STRING))
			match(KW_STRING);
		else //if(isKind(KW_BOOLEAN))
			match(KW_BOOLEAN);
		return new SimpleType(firstToken,type);
	}

	public List<SyntaxException> getExceptionList() 
	{
		
		return exceptionList;
	}



}