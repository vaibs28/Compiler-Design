
/* *
 * Developed  for the class project in COP5556 Programming Language Principles 
 * at the University of Florida, Fall 2019.
 * 
 * This software is solely for the educational benefit of students 
 * enrolled in the course during the Fall 2019 semester.  
 * 
 * This software, and any software derived from it,  may not be shared with others or posted to public web sites or repositories,
 * either during the course or afterwards.
 * 
 *  @Beverly A. Sanders, 2019
 */
package cop5556fa19;

import static cop5556fa19.Token.Kind.*;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import cop5556fa19.Token.Kind;

public class Scanner {

	Reader r;
	static StringBuilder sb = new StringBuilder();

	private enum State {
		START, HAVE_EQ, HAVE_KW, AFTER_DOT, AFTER_EQ, AFTER_GT, AFTER_LT, AFTER_COLON, AFTER_DOTDOT
	};

	static int pos = 0;
	static int line = 0;
	List<Token> tokenList = new ArrayList<Token>();

	@SuppressWarnings("serial")
	public static class LexicalException extends Exception {
		public LexicalException(String arg0) {
			super(arg0);
		}
	}

	public Scanner(Reader r) throws IOException {
		this.r = r;
	}

	public Token getNext() throws Exception {
		// replace this code. Just for illustration
		// if (r.read() == -1) { return new Token(EOF,"eof",0,0);}
		Token token = null;
		State state = State.START;
		int ch;

		while ((ch = r.read()) != -1) {
			Character c = new Character((char) ch);
			sb.append(Character.toString(c));
		}

		// System.out.println(sb.toString());
		int i = 0;
		while (i < sb.length()) {

			switch (state) {
			case START:

				ch = sb.charAt(pos);
				// handling tokens with single character first
				switch (ch) {

				case ',':
					token = new Token(Kind.COMMA, ",", pos, line);
					pos++;
					return token;

				case ';':
					token = new Token(Kind.SEMI, ";", pos, line);
					pos++;
					return token;

				case '(':
					token = new Token(Kind.LPAREN, "(", pos, line);
					pos++;
					return token;

				case ')':
					token = new Token(Kind.RPAREN, ")", pos, line);
					pos++;
					return token;

				case '[':
					token = new Token(LSQUARE, "[", pos, line);
					pos++;
					return token;

				case '{':
					token = new Token(LCURLY, "{", pos, line);
					pos++;
					return token;

				case '}':
					token = new Token(Kind.RCURLY, "}", pos, line);
					pos++;
					return token;

				case '+':
					token = new Token(Kind.OP_PLUS, "+", pos, line);
					pos++;
					return token;

				case '-':
					token = new Token(Kind.OP_MINUS, "-", pos, line);
					pos++;
					return token;

				case '*':
					token = new Token(Kind.OP_TIMES, "*", pos, line);
					pos++;
					return token;

				case '^':
					token = new Token(Kind.OP_POW, "^", pos, line);
					pos++;
					return token;

				case '%':
					token = new Token(Kind.OP_MOD, "%", pos, line);
					pos++;
					return token;

				case '#':
					token = new Token(Kind.OP_HASH, "#", pos, line);
					pos++;
					return token;

				// checking for tokens that can have multiple characters
				case '.':
					state = State.AFTER_DOT;
					pos++;
					break;

				default:
					throw new LexicalException("Useful error message");

				}
				// Checking for tokens .. and ...
			case AFTER_DOT:
				// get the next character
				ch = sb.charAt(pos);
				if (ch == '.') {
					state = State.AFTER_DOTDOT;
					pos++;
					//break;
				} else {
					return new Token(Kind.DOT, ".", pos, line);
				}

			case AFTER_DOTDOT:
				// get the next character
				ch = sb.charAt(pos);
				if (ch == '.') {
					return new Token(Kind.DOTDOTDOT, "...", pos, line);
				} else {
					return new Token(Kind.DOTDOT, "..", pos, line);
				}
			
			}
			i++;
			
		}
		return new Token(EOF, "eof", pos, line);
	}

}
