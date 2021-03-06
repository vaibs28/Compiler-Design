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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.internal.Throwables;
import org.junit.jupiter.api.Test;

import cop5556fa19.Scanner.LexicalException;
import cop5556fa19.Token.Kind;

import static cop5556fa19.Token.Kind.*;

class ScannerTest {

    // I like this to make it easy to print objects and turn this output on and off
    static boolean doPrint = true;

    private void show(Object input) {
	if (doPrint) {
	    System.out.println(input.toString());
	}
    }

    /**
     * Example showing how to get input from a Java string literal.
     * 
     * In this case, the string is empty. The only Token that should be returned is
     * an EOF Token.
     * 
     * This test case passes with the provided skeleton, and should also pass in
     * your final implementation. Note that calling getNext again after having
     * reached the end of the input should just return another EOF Token.
     * 
     */
    @Test
    void test0() throws Exception {
	Reader r = new StringReader("");
	Scanner s = new Scanner(r);
	Token t;
	show(t = s.getNext());
	assertEquals(EOF, t.kind);
	show(t = s.getNext());
	assertEquals(EOF, t.kind);
    }

    /**
     * Example showing how to create a test case to ensure that an exception is
     * thrown when illegal input is given.
     * 
     * This "@" character is illegal in the final scanner (except as part of a
     * String literal or comment). So this test should remain valid in your complete
     * Scanner.
     */
    @Test
    void test1() throws Exception {
	Reader r = new StringReader("@");
	Scanner s = new Scanner(r);
	assertThrows(LexicalException.class, () -> {
	    s.getNext();
	});
    }

    /**
     * Example showing how to read the input from a file. Otherwise it is the same
     * as test1.
     *
     */
    @Test
    void test2() throws Exception {
	String file = "testInputFiles/test2.input";
	Reader r = new BufferedReader(new FileReader(file));
	Scanner s = new Scanner(r);
	assertThrows(LexicalException.class, () -> {
	    s.getNext();
	});
    }

    /**
     * Another example. This test case will fail with the provided code, but should
     * pass in your completed Scanner.
     * 
     * @throws Exception
     */
    @Test
    void test3() throws Exception {
	Reader r = new StringReader(",;:=");
	Scanner s = new Scanner(r);
	Token t;
	show(t = s.getNext());
	assertEquals(t.kind, COMMA);
	assertEquals(t.text, ",");

	show(t = s.getNext());
	assertEquals(t.kind, SEMI);
	assertEquals(t.text, ";");

	show(t = s.getNext());
	assertEquals(t.kind, COLON);
	assertEquals(t.text, ":");

	show(t = s.getNext());
	assertEquals(t.kind, ASSIGN);
	assertEquals(t.text, "=");
    }

    // custom test for reading a single character only
    @Test
    void test4() throws Exception {
	Reader r = new StringReader("(());,");
	Scanner s = new Scanner(r);
	Token t;
	show(t = s.getNext());
	assertEquals(t.kind, LPAREN);
	assertEquals(t.text, "(");

	show(t = s.getNext());
	assertEquals(t.kind, LPAREN);
	assertEquals(t.text, "(");

	show(t = s.getNext());
	assertEquals(t.kind, RPAREN);
	assertEquals(t.text, ")");

	show(t = s.getNext());
	assertEquals(t.kind, RPAREN);
	assertEquals(t.text, ")");

	show(t = s.getNext());
	assertEquals(t.kind, SEMI);
	assertEquals(t.text, ";");

	show(t = s.getNext());
	assertEquals(t.kind, COMMA);
	assertEquals(t.text, ",");

    }

    // custom test for EOF
    @Test
    void test5() throws Exception {
	Reader r = new StringReader("");
	Scanner s = new Scanner(r);
	Token t;
	show(t = s.getNext());
	assertEquals(t.kind, EOF);
	assertEquals(t.text, "eof");
    }

    // custom test for checking dots

    @Test
    void test6() throws Exception {
	Reader r = new StringReader("(,...==)");
	Scanner s = new Scanner(r);
	Token t;

	show(t = s.getNext());
	assertEquals(t.kind, LPAREN);
	assertEquals(t.text, "(");

	show(t = s.getNext());
	assertEquals(t.kind, COMMA);
	assertEquals(t.text, ",");

	show(t = s.getNext());
	assertEquals(t.kind, DOTDOTDOT);
	assertEquals(t.text, "...");

	show(t = s.getNext());
	assertEquals(t.kind, REL_EQEQ);
	assertEquals(t.text, "==");

	show(t = s.getNext());
	assertEquals(t.kind, RPAREN);
	assertEquals(t.text, ")");
    }

    // test to check for << , >> and //
    @Test
    void test7() throws Exception {
	Reader r = new StringReader("+-+=#<<>//");
	Scanner s = new Scanner(r);
	Token t;

	show(t = s.getNext());
	assertEquals(t.kind, OP_PLUS);
	assertEquals(t.text, "+");

	show(t = s.getNext());
	assertEquals(t.kind, OP_MINUS);
	assertEquals(t.text, "-");

	show(t = s.getNext());
	assertEquals(t.kind, OP_PLUS);
	assertEquals(t.text, "+");

	show(t = s.getNext());
	assertEquals(t.kind, ASSIGN);
	assertEquals(t.text, "=");

	show(t = s.getNext());
	assertEquals(t.kind, OP_HASH);
	assertEquals(t.text, "#");

	show(t = s.getNext());
	assertEquals(t.kind, BIT_SHIFTL);
	assertEquals(t.text, "<<");

	show(t = s.getNext());
	assertEquals(t.kind, Kind.REL_GT);
	assertEquals(t.text, ">");

	show(t = s.getNext());
	assertEquals(t.kind, OP_DIVDIV);
	assertEquals(t.text, "//");
    }

    @Test
    void test8() throws Exception {
	Reader r = new StringReader("1234::+-1234..1234");
	Scanner s = new Scanner(r);
	Token t;

	show(t = s.getNext());
	assertEquals(t.kind, INTLIT);
	assertEquals(t.text, "1234");

	show(t = s.getNext());
	assertEquals(t.kind, COLONCOLON);
	assertEquals(t.text, "::");

	show(t = s.getNext());
	assertEquals(t.kind, OP_PLUS);
	assertEquals(t.text, "+");

	show(t = s.getNext());
	assertEquals(t.kind, OP_MINUS);
	assertEquals(t.text, "-");

	show(t = s.getNext());
	assertEquals(t.kind, INTLIT);
	assertEquals(t.text, "1234");

	show(t = s.getNext());
	assertEquals(t.kind, DOTDOT);
	assertEquals(t.text, "..");

	show(t = s.getNext());
	assertEquals(t.kind, INTLIT);
	assertEquals(t.text, "1234");

    }

    @Test
    void test9() throws Exception {
	Reader r = new StringReader(".+-123");
	Scanner s = new Scanner(r);
	Token t;

	show(t = s.getNext());
	assertEquals(t.kind, DOT);
	assertEquals(t.text, ".");

	show(t = s.getNext());
	assertEquals(t.kind, OP_PLUS);
	assertEquals(t.text, "+");

	show(t = s.getNext());
	assertEquals(t.kind, OP_MINUS);
	assertEquals(t.text, "-");

	show(t = s.getNext());
	assertEquals(t.kind, INTLIT);
	assertEquals(t.text, "123");

    }

    // test for reading string literal
    @Test
    void test10() throws Exception {
	Reader r = new StringReader("-..\"abc123\"");
	Scanner s = new Scanner(r);
	Token t;

	show(t = s.getNext());
	assertEquals(t.kind, OP_MINUS);
	assertEquals(t.text, "-");

	show(t = s.getNext());
	assertEquals(t.kind, DOTDOT);
	assertEquals(t.text, "..");

	show(t = s.getNext());
	assertEquals(t.kind, STRINGLIT);
	assertEquals(t.text, "\"abc123\"");

    }

    @Test
    void test11() throws Exception {
	Reader r = new StringReader("while");
	Scanner s = new Scanner(r);
	Token t;

	show(t = s.getNext());
	assertEquals(t.kind, KW_while);
	assertEquals(t.text, "while");
    }

    @Test
    void test12() throws Exception {
	Reader r = new StringReader("while123......++==abc");
	Scanner s = new Scanner(r);
	Token t;

	show(t = s.getNext());
	assertEquals(t.kind, NAME);
	assertEquals(t.text, "while123");

	show(t = s.getNext());
	assertEquals(t.kind, DOTDOTDOT);
	assertEquals(t.text, "...");

	show(t = s.getNext());
	assertEquals(t.kind, DOTDOTDOT);
	assertEquals(t.text, "...");

	show(t = s.getNext());
	assertEquals(t.kind, OP_PLUS);
	assertEquals(t.text, "+");

	show(t = s.getNext());
	assertEquals(t.kind, OP_PLUS);
	assertEquals(t.text, "+");

	show(t = s.getNext());
	assertEquals(t.kind, REL_EQEQ);
	assertEquals(t.text, "==");

	show(t = s.getNext());
	assertEquals(t.kind, NAME);
	assertEquals(t.text, "abc");
    }

    @Test
    void test13() throws Exception {
	Reader r = new StringReader("0and");
	Scanner s = new Scanner(r);
	Token t;

	show(t = s.getNext());
	assertEquals(t.kind, INTLIT);
	assertEquals(t.text, "0");

	show(t = s.getNext());
	assertEquals(t.kind, KW_and);
	assertEquals(t.text, "and");

    }

    @Test
    void test14() throws Exception {
	Reader r = new StringReader("1abc");
	Scanner s = new Scanner(r);
	Token t;

	show(t = s.getNext());
	assertEquals(t.kind, INTLIT);
	assertEquals(t.text, "1");

	show(t = s.getNext());
	assertEquals(t.kind, NAME);
	assertEquals(t.text, "abc");

    }

    // integer literal range test
    @Test
    void test15() throws Exception {
	Reader r = new StringReader("21474836478");
	Scanner s = new Scanner(r);
	Token t;

	assertThrows(LexicalException.class, () -> {
	    s.getNext();
	});
    }

    // invalid token test
    @Test
    void test16() throws Exception {
	Reader r = new StringReader("@@");
	Scanner s = new Scanner(r);
	Token t;

	assertThrows(LexicalException.class, () -> {
	    s.getNext();
	});
    }

    // Test for identifiers start
    @Test
    void test17() throws Exception {
	Reader r = new StringReader("abc@");
	Scanner s = new Scanner(r);
	Token t;

	show(t = s.getNext());
	assertEquals(t.kind, NAME);
	assertEquals(t.text, "abc");

	show(t = s.getNext());

    }

    // test for int literals
    @Test
    void test18() throws Exception {
	Reader r = new StringReader("00120.0256");
	Scanner s = new Scanner(r);
	Token t;

	show(t = s.getNext());
	assertEquals(t.kind, INTLIT);
	assertEquals(t.text, "0");

	show(t = s.getNext());
	assertEquals(t.kind, INTLIT);
	assertEquals(t.text, "0");

	show(t = s.getNext());
	assertEquals(t.kind, INTLIT);
	assertEquals(t.text, "120");

	show(t = s.getNext());
	assertEquals(t.kind, DOT);
	assertEquals(t.text, ".");

	show(t = s.getNext());
	assertEquals(t.kind, INTLIT);
	assertEquals(t.text, "0");

	show(t = s.getNext());
	assertEquals(t.kind, INTLIT);
	assertEquals(t.text, "256");

    }

    // test for whitespaces
    @Test
    void test19() throws Exception {
	Reader r = new StringReader("  00120 256 345 " + "45");
	Scanner s = new Scanner(r);
	Token t;

	show(t = s.getNext());
	assertEquals(t.kind, INTLIT);
	assertEquals(t.text, "0");

	show(t = s.getNext());
	assertEquals(t.kind, INTLIT);
	assertEquals(t.text, "0");

	show(t = s.getNext());
	assertEquals(t.kind, INTLIT);
	assertEquals(t.text, "120");

	show(t = s.getNext());
	assertEquals(t.kind, INTLIT);
	assertEquals(t.text, "256");

	show(t = s.getNext());
	assertEquals(t.kind, INTLIT);
	assertEquals(t.text, "345");

	show(t = s.getNext());
	assertEquals(t.kind, INTLIT);
	assertEquals(t.text, "45");

    }

    // test for keywords
    @Test
    void test20() throws Exception {
	Reader r = new StringReader("else elseif while if ");
	Scanner s = new Scanner(r);
	Token t;

	show(t = s.getNext());
	assertEquals(t.kind, KW_else);
	assertEquals(t.text, "else");

	show(t = s.getNext());
	assertEquals(t.kind, KW_elseif);
	assertEquals(t.text, "elseif");

	show(t = s.getNext());
	assertEquals(t.kind, KW_while);
	assertEquals(t.text, "while");

	show(t = s.getNext());
	assertEquals(t.kind, KW_if);
	assertEquals(t.text, "if");
    }

    @Test
    void test21() throws Exception {
	Reader r = new StringReader("\"abc\b\"");
	Scanner s = new Scanner(r);
	Token t;

	show(t = s.getNext());
	assertEquals(t.kind, STRINGLIT);
	assertEquals(t.text, "\"abc" + (char) 7 + "\"");

    }

    @Test
    void test22() throws Exception {
	Reader r = new StringReader("a=d");
	Scanner s = new Scanner(r);
	Token t;

	show(t = s.getNext());
	assertEquals(t.kind, NAME);
	assertEquals(t.text, "a");

	show(t = s.getNext());
	assertEquals(t.kind, ASSIGN);
	assertEquals(t.text, "=");

	show(t = s.getNext());
	assertEquals(t.kind, NAME);
	assertEquals(t.text, "d");

    }

    @Test
    void test23() throws Exception {
	Reader r = new StringReader("aaaaa--comment\n1234--comment\r\nbbbb");
	Scanner s = new Scanner(r);
	Token t;

	show(t = s.getNext());
	// assertEquals(t.kind, KW_while);
	// assertEquals(t.text, "while");

	show(t = s.getNext());
	// assertEquals(t.kind, NAME);
	// assertEquals(t.text, "do");

	show(t = s.getNext());
	// assertEquals(t.kind, NAME);
	// assertEquals(t.text, "and");

    }

    // test for strlit
    @Test
    void test24() throws Exception {
	Reader r = new StringReader("\"\\a \\b\"");
	Scanner s = new Scanner(r);
	Token t;

	show(t = s.getNext());
	assertEquals(t.kind, STRINGLIT);
	// assertEquals(t.text, "\"abc\\n\"");

    }

    // failed tests

    @Test
    void failedTest() throws Exception {
	String file = "testInputFiles/test2.input";
	Reader r = new BufferedReader(new FileReader(file));
	Scanner s = new Scanner(r);
	s.getNext();

	assertThrows(LexicalException.class, () -> {
	    s.getNext();
	});

    }

    @Test
    void test26() throws Exception {
	Reader r = new StringReader("-10");
	Scanner s = new Scanner(r);
	Token t;

	show(t = s.getNext());
	show(t = s.getNext());
    }
}
