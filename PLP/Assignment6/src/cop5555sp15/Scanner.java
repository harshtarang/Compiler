package cop5555sp15;

import java.io.IOException;
import java.util.ArrayList;

import cop5555sp15.TokenStream.Kind;
import cop5555sp15.TokenStream.Token;
import static cop5555sp15.TokenStream.Kind.*;

public class Scanner {

	public Scanner(TokenStream stream) 
	{
		this.stream=stream;
		lineNumber=1;
		index=0;
		begIndex=0;
		keywords=new ArrayList<String>();
		String[] words={"int","string","boolean","import","class","def","while","if","else","return","print","true","false","null","key","value","size"};
		for(String word : words)
		{
			keywords.add(word);
		}
		
		
	}
	
	private enum State
	{
		START,
		GOT_EQUALS,
		IDENT_PART,
		DIGITS,
		EOF,
		ERROR,
		GOT_LT,
		GOT_GT,
		GOT_MINUS,
		GOT_DOT,
		GOT_NOT,
		GOT_FSLASH,
		GOT_COMM_START,
		GOT_COMM_END,
		GOT_STRING,
		GOT_STRING_ESC
	}
	
	public TokenStream stream;
	
	char ch;
	private int lineNumber;			
	private int index;				//index to the character being read from input
	
	private int begIndex;
	private State state;
	
	private ArrayList<String> keywords;				// data structure to hold keywords
	
	private void getch() throws IOException
	{
		if(index<stream.inputChars.length)
			ch	= stream.inputChars[index++];
		else
		{
			ch='\0';
			index++;
		}
		
	}
	
	private char peak() throws IOException
	{
		if(index<stream.inputChars.length-1)
			return stream.inputChars[index];
		return '\0';
			
	}
	
	


	// Fills in the stream.tokens list with recognized tokens 
    //from the input
	public void scan() 
	{
         
		Token t;
		try 
		{
			getch();
			do
			{
			
				t = next();
			
				stream.tokens.add(t);
			}
			while(!t.kind.equals(Kind.EOF));
		}
		catch (IOException e) 
		{
			
			e.printStackTrace();
		}
	}




	private Token next() throws IOException
	{
		state=State.START;
		Token t=null;
		do
		{
			switch(state)
			{
				case START:	if(Character.isWhitespace(ch))
							{
								if(ch=='\n')
								{	
									lineNumber++;
									getch();
								}
								else if(ch=='\r')
								{
									getch();
									lineNumber++;
									if(ch=='\n')
										getch();
												
								}
								else
									getch();
								continue;
							}
					
							begIndex=index-1;
							
							switch(ch)
							{
								case '=':	state=State.GOT_EQUALS;
											break;
											
								case '+':	t=stream.new Token(PLUS,begIndex,index,lineNumber);
											break;
											
								case '-':	state=State.GOT_MINUS;
											break;
											
								case '*':	t=stream.new Token(TIMES,begIndex,index,lineNumber);
											break;
											
								case '0':	t=stream.new Token(INT_LIT,begIndex,index,lineNumber);
											break;
								
								case '\0':	t=stream.new Token(EOF,index-1,index-1,lineNumber);
											break;
								
								case '\n':	lineNumber++;
											break;
											
								case '"':	state=State.GOT_STRING;
											break;
											
								case '<':	state=State.GOT_LT;
											break;
											
								case '>':	state=State.GOT_GT;
											break;
											
								case '.':	state=State.GOT_DOT;
											break;
								case ',':	t=stream.new Token(COMMA,begIndex,index,lineNumber);
											break;
											
								case ';':	t=stream.new Token(SEMICOLON,begIndex,index,lineNumber);
											break;
											
								case '(':	t=stream.new Token(LPAREN,begIndex,index,lineNumber);
											break;
											
								case ')':	t=stream.new Token(RPAREN,begIndex,index,lineNumber);
											break;
											
								case '[':	t=stream.new Token(LSQUARE,begIndex,index,lineNumber);
											break;
											
								case ']':	t=stream.new Token(RSQUARE,begIndex,index,lineNumber);
											break;
											
								case '{':	t=stream.new Token(LCURLY,begIndex,index,lineNumber);
											break;
											
								case '}':	t=stream.new Token(RCURLY,begIndex,index,lineNumber);
											break;
											
								case ':':	t=stream.new Token(COLON,begIndex,index,lineNumber);
											break;
											
								case '?':	t=stream.new Token(QUESTION,begIndex,index,lineNumber);
											break;			
											
								case '|':	t=stream.new Token(BAR,begIndex,index,lineNumber);
											break;
											
								case '&':	t=stream.new Token(AND,begIndex,index,lineNumber);
											break;
											
								case '%':	t=stream.new Token(MOD,begIndex,index,lineNumber);
											break;
											
								case '@':	t=stream.new Token(AT,begIndex,index,lineNumber);
											break;
											
								case '!':	state=State.GOT_NOT;
											break;
											
								case '/':	state=State.GOT_FSLASH;
											break;
											
					//			case '#':	t=stream.new Token(ILLEGAL_CHAR,begIndex,index,lineNumber);
					//						break;
											
								default:	if(Character.isDigit(ch))
												state=State.DIGITS;
											else if(Character.isJavaIdentifierStart(ch))
												state=State.IDENT_PART;
											else
											{
												System.out.println("Illegal Character encountered: "+ch);
												t=stream.new Token(ILLEGAL_CHAR,begIndex,index,lineNumber);
											}
											break;
											
							}
							
							getch();
							break;
				
				case GOT_EQUALS:	
									switch(ch)
									{
										case '=':	t=stream.new Token(EQUAL,begIndex,index,lineNumber);
													getch();
													break;
										
										default:	
													t=stream.new Token(ASSIGN,begIndex,index-1,lineNumber);
													break;
									
									}
									
									
									break;
									
				case GOT_LT:
									switch(ch)
									{
										case '=':	t=stream.new Token(LE,begIndex,index,lineNumber);
													getch();
													break;
													
										case '<':	t=stream.new Token(LSHIFT,begIndex,index,lineNumber);
													getch();
													break;
						
										default:	
													t=stream.new Token(LT,begIndex,index-1,lineNumber);
													break;
					
									}
									break;
									
				case GOT_GT:
									switch(ch)
									{
										case '=':	t=stream.new Token(GE,begIndex,index,lineNumber);
													getch();
													break;
													
										case '>':	t=stream.new Token(RSHIFT,begIndex,index,lineNumber);
													getch();
													break;
		
										default:	
													t=stream.new Token(GT,begIndex,index-1,lineNumber);
													break;
	
									}
									break;
									
				case GOT_MINUS:	
									switch(ch)
									{
										case '>':	t=stream.new Token(ARROW,begIndex,index,lineNumber);
													getch();
													break;
						
										default:	
													t=stream.new Token(MINUS,begIndex,index-1,lineNumber);
													break;
					
									}
					
									
									break;
									
									
									
				case GOT_DOT:	
									switch(ch)
									{
										case '.':	t=stream.new Token(RANGE,begIndex,index,lineNumber);
													getch();
													break;
					
										default:	
													t=stream.new Token(DOT,begIndex,index-1,lineNumber);
													break;
					
									}
									break;
																		
				case GOT_NOT:		switch(ch)
									{
										case '=':	t=stream.new Token(NOTEQUAL,begIndex,index,lineNumber);
													getch();
													break;
	
										default:	
													t=stream.new Token(NOT,begIndex,index-1,lineNumber);
													break;
	
									}
									break;
									
				case GOT_FSLASH:	switch(ch)
									{
										case '*':	state=State.GOT_COMM_START;
													getch();
													break;

										default:	
													t=stream.new Token(DIV,begIndex,index-1,lineNumber);
													break;

									}
									
									break;
				
				case GOT_COMM_START:	if(ch=='*')
											state=State.GOT_COMM_END;
										else if(ch=='\0')
										{
											t=stream.new Token(UNTERMINATED_COMMENT,begIndex,index-1,lineNumber);
											
										}
										else if(ch=='\n' || (ch=='\r' && peak()!='\n'))
											lineNumber++;
										getch();	
										break;
										
				case GOT_COMM_END:	if(ch=='/')
									{
										state=State.START;
									}
									else if(ch=='*')
										state=State.GOT_COMM_END;
									else if(ch=='\0')
									{
										t=stream.new Token(UNTERMINATED_COMMENT,begIndex,index-1,lineNumber);
																								 
									}
									else if(ch=='\n' || (ch=='\r' && peak()!='\n'))
										lineNumber++;
									else
										state=State.GOT_COMM_START;
									getch();
									break;
				
				case GOT_STRING:	
									switch(ch)
									{
										case '"':	t=stream.new Token(STRING_LIT,begIndex,index,lineNumber);
													getch();
													break;
													
										case '\0': 	t=stream.new Token(UNTERMINATED_STRING,begIndex,index-1,lineNumber);
													
													getch();
													break;
													
										case '\\':	state=State.GOT_STRING_ESC;
													getch();
													break;
										case '\n':	lineNumber++;
													getch();
													break;
										case '\r':	if(peak()!='\n')
														lineNumber++;
													getch();
													break;
										default:	getch();
													break;
													
									}
									break;
									
				case GOT_STRING_ESC:	state=State.GOT_STRING;
										
										switch(ch)
										{
											case '"':	
														getch();
														break;
										
											case '\0':	t=stream.new Token(UNTERMINATED_STRING,begIndex,index-1,lineNumber);
														//index=begIndex+1;
														getch();
														break;
														
											default:	getch();
														break;
										}
										break;
									
				case IDENT_PART:	if(!Character.isJavaIdentifierPart(ch) || ch=='\0')
									{
												switch(isKeyWord(begIndex,index-1))
												{
													case 0:	t=stream.new Token(KW_INT,begIndex,index-1,lineNumber);
															break;
													case 1:	t=stream.new Token(KW_STRING,begIndex,index-1,lineNumber);
													break;
													case 2:	t=stream.new Token(KW_BOOLEAN,begIndex,index-1,lineNumber);
													break;
													case 3:	t=stream.new Token(KW_IMPORT,begIndex,index-1,lineNumber);
													break;
													case 4:	t=stream.new Token(KW_CLASS,begIndex,index-1,lineNumber);
													break;
													case 5:	t=stream.new Token(KW_DEF,begIndex,index-1,lineNumber);
													break;
													case 6:	t=stream.new Token(KW_WHILE,begIndex,index-1,lineNumber);
													break;
													case 7:	t=stream.new Token(KW_IF,begIndex,index-1,lineNumber);
													break;
													case 8:	t=stream.new Token(KW_ELSE,begIndex,index-1,lineNumber);
													break;
													case 9:	t=stream.new Token(KW_RETURN,begIndex,index-1,lineNumber);
													break;
													case 10:	t=stream.new Token(KW_PRINT,begIndex,index-1,lineNumber);
													break;
													case 11:	t=stream.new Token(BL_TRUE,begIndex,index-1,lineNumber);
													break;
													case 12:	t=stream.new Token(BL_FALSE,begIndex,index-1,lineNumber);
													break;
													case 13:	t=stream.new Token(NL_NULL,begIndex,index-1,lineNumber);
													break;
													case 14:	t=stream.new Token(KW_KEY,begIndex,index-1,lineNumber);
																break;
													case 15:	t=stream.new Token(KW_VALUE,begIndex,index-1,lineNumber);
																break;
													case 16:	t=stream.new Token(KW_SIZE,begIndex,index-1,lineNumber);
																break;
													case -1:	t=stream.new Token(IDENT,begIndex,index-1,lineNumber);
													break;
															
												}
												
									}
									else
									{
										state=State.IDENT_PART;
										getch();
									}
									break;
									
				
									
				case DIGITS:		if(!Character.isDigit(ch))
									{
											t=stream.new Token(INT_LIT,begIndex,index-1,lineNumber);
									}
									else
									{
										state=State.DIGITS;
										getch();
									}
									break;
							
				default: assert false : "Check the code";
			}
		}
		while(t==null);
		return t;
	}




	private int isKeyWord(int beg, int end) 
	{
		String word=String.copyValueOf(stream.inputChars, beg, end-beg);
		return keywords.indexOf(word);
			
	}

}

