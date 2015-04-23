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
import cop5555sp15.TokenStream.Kind;
import cop5555sp15.TokenStream.Token;

public class SimpleParser {

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

	SimpleParser(TokenStream tokens) {
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


	public void parse() throws SyntaxException {
		Program();
		match(EOF);
	}

	private void Program() throws SyntaxException {
		ImportList();
		match(KW_CLASS);
		match(IDENT);
		Block();
	}

	private void ImportList() throws SyntaxException {
		while(isKind(KW_IMPORT))
		{
			
			match(KW_IMPORT);
			match(IDENT);
			while(isKind(DOT))
			{
				match(DOT);
				match(IDENT);
			}
			match(SEMICOLON);
		}
		
	}

	private void Block() throws SyntaxException 
	{
		match(LCURLY);
		while(isKind(KW_DEF) || isKind(IDENT) || isKind(KW_PRINT) || isKind(KW_WHILE) || isKind(KW_IF) || isKind(MOD) || isKind(KW_RETURN) || isKind(SEMICOLON))
		{
			if(isKind(IDENT) || isKind(KW_PRINT) || isKind(KW_WHILE) || isKind(KW_IF) || isKind(MOD) || isKind(KW_RETURN) || isKind(SEMICOLON))
			{
				statement();
				
			}
			else 
				declaration();
			match(SEMICOLON);
		}
		
		match(RCURLY);
	}

	private void statement() throws SyntaxException 
	{
		if(isKind(SEMICOLON))
			return;
		switch(t.kind)
		{
		case IDENT:	lValue();
					match(ASSIGN);
					expression();
					break;
					
		case KW_PRINT:	consume();
						expression();
						break;
						
		case KW_WHILE: consume();
						if(isKind(TIMES))
						{
							consume();
							match(LPAREN);
							expression();
							
							if(isKind(RANGE))
							{
								consume();
								expression();
							}
							match(RPAREN);
							Block();
						}
						else
						{
							match(LPAREN);
							expression();
							match(RPAREN);
							Block();
						}
						break;
						
		case KW_IF:	consume();
					match(LPAREN);
					expression();
					match(RPAREN);
					Block();
					if(isKind(KW_ELSE))
					{
						consume();
						Block();
					}
					break;
		
		case KW_RETURN:
		case MOD:	consume();
					expression();
					break;
		}
		
				
	}

	

	private void expression() throws SyntaxException
	{
		
		Term();
		while(isKind(REL_OPS))
		{
			consume();
			Term();
		}
	}

	private void Term() throws SyntaxException
	{
		Elem();
		while(isKind(WEAK_OPS))
		{
			consume();
			Elem();
		}
		
	}

	private void Elem() throws SyntaxException
	{
		Thing();
		while(isKind(STRONG_OPS))
		{
			consume();
			Thing();
		}
	}

	private void Thing() throws SyntaxException
	{
		Factor();
		while(isKind(VERY_STRONG_OPS))
		{
			consume();
			Factor();
		}
		
	}

	private void Factor() throws SyntaxException
	{
		switch(t.kind)
		{
			case IDENT:	if(isKindNextToken(LPAREN))
							closureEvalExpression();
						//else if(isKindNextToken(ASSIGN))
							//closure();
						else
						{
							consume();
							if(isKind(LSQUARE))
							{
								consume();
								expression();
								match(RSQUARE);
							}
						}
						break;
			
			case BL_TRUE:
			case BL_FALSE:
			case STRING_LIT:	
			case INT_LIT:	consume();
							break;
							
			case LPAREN:	consume();
							expression();
							match(RPAREN);
							break;
							
			case NOT:	
			case MINUS:		consume();
							Factor();
							break;
							
			case KW_SIZE:
			case KW_KEY:
			case KW_VALUE:	consume();
							match(LPAREN);
							expression();
							match(RPAREN);
							break;
							
			case AT:	if(isKindNextToken(AT))
							MapList();
						else
							List();
						break;
						
			case LCURLY:	closure();
							break;
						
			default:	throw new SyntaxException(t, IDENT);
							
		}
	}

	private void closureEvalExpression() throws SyntaxException
	{
		match(IDENT);
		match(LPAREN);
		expressionList();
		match(RPAREN);
		
	}

	private void expressionList() throws SyntaxException
	{
		if(isKind(FACTOR_PREDICT))
		{
			expression();
			while(isKind(COMMA))
			{
				consume();
				expression();
			}
			
		}
		
	}

	private void List() throws SyntaxException
	{
		match(AT);
		match(LSQUARE);
		expressionList();
		match(RSQUARE);
		
	}

	private void MapList() throws SyntaxException
	{
		match(AT);
		match(AT);
		match(LSQUARE);
		KeyValueList();
		match(RSQUARE);
		
	}

	private void KeyValueList() throws SyntaxException
	{
		if(isKind(FACTOR_PREDICT))
		{
			KeyValueExpression();
			while(isKind(COMMA))
			{
				consume();
				KeyValueExpression();
			}
		}
		
	}

	private void KeyValueExpression() throws SyntaxException
	{
		expression();
		match(COLON);
		expression();
		
	}

	private void lValue() throws SyntaxException{
		
		match(IDENT);
		if(isKind(LSQUARE))
		{	
			consume();
			expression();
			match(RSQUARE);
		}
	}

	private void declaration() throws SyntaxException 
	{
		
		match(KW_DEF);
		if((isKind(IDENT) && isKindNextToken(COLON)) || (isKind(IDENT) && isKindNextToken(SEMICOLON)))
			vardec();
		else
			closuredec();
		
		
	}

	private void closuredec() throws SyntaxException
	{
		
		match(IDENT);
		match(ASSIGN);
		closure();
	}

	

	private void closure() throws SyntaxException
	{
		match(LCURLY);
		formalArgList();
		match(ARROW);
		while(isKind(IDENT) || isKind(KW_PRINT) || isKind(KW_WHILE) || isKind(KW_IF) || isKind(MOD) || isKind(KW_RETURN) || isKind(SEMICOLON))
		{
			statement();
			match(SEMICOLON);
		}
		match(RCURLY);
		
	}

	private void formalArgList() throws SyntaxException
	{
	
		if(isKind(ARROW))
			return;
		vardec();
		while(isKind(COMMA))
		{
			match(COMMA);
			vardec();
		}
	}

	private void vardec() throws SyntaxException
	{
		
		match(IDENT);
		if(isKind(COLON))
		{
			consume();
			type();
		}
		else return;
	
		
	}

	private void type() throws SyntaxException
	{
		
		
		if(isKind(KW_INT) || isKind(KW_BOOLEAN) || isKind(KW_STRING))
			simpleType();
		else if(isKind(AT) && isKindNextToken(AT))
			keyValueType();
		else //if(isKind(AT) && isKindNextToken(LSQUARE))
			listType();
		
	}

	private void listType() throws SyntaxException
	{
	
		match(AT);
		match(LSQUARE);
		type();
		match(RSQUARE);
		
	}

	private void keyValueType() throws SyntaxException
	{
	
		match(AT);
		match(AT);
		match(LSQUARE);
		simpleType();
		match(COLON);
		type();
		match(RSQUARE);
		
		
	}

	private void simpleType() throws SyntaxException
	{
	
		if(isKind(KW_INT))
			match(KW_INT);
		else if(isKind(KW_STRING))
			match(KW_STRING);
		else //if(isKind(KW_BOOLEAN))
			match(KW_BOOLEAN);
		
	}



}