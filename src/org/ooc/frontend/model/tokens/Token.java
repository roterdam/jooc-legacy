package org.ooc.frontend.model.tokens;

import org.ubi.Locatable;
import org.ubi.SourceReader;


public final class Token implements Locatable {

	public static final Token defaultToken = new Token(0, 0, (byte) 0);
	
	public static final class TokenType {
		public static final byte CLASS_KW = 1; // class keyword
		public static final byte COVER_KW = 2; // cover keyword
		public static final byte FUNC_KW = 3; // func keyword
		public static final byte ABSTRACT_KW = 4; // abstract keyword
		public static final byte EXTENDS_KW = 5; // from keyword
		public static final byte FROM_KW = 6; // over keyword
		
		public static final byte CONST_KW = 10; // const keyword
		public static final byte FINAL_KW = 11; // final keyword
		public static final byte STATIC_KW = 12; // static keyword
		
		public static final byte INCLUDE_KW = 13; // include keyword
		public static final byte IMPORT_KW = 14; // import keyword
		public static final byte USE_KW = 15; // use keyword
		public static final byte EXTERN_KW = 16; // extern keyword
		public static final byte INLINE_KW = 17; // extern keyword
		public static final byte PROTO_KW = 18; // proto keyword
		public static final byte UNMANGLED_KW = 19; // unmangled keyword
			
		public static final byte BREAK_KW = 20; // break keyword
		public static final byte CONTINUE_KW = 21; // continue keyword
		public static final byte FALLTHR_KW = 22; // fallthrough keyword
		
		public static final byte OPERATOR_KW = 23; // operator keyword
		
		public static final byte IF_KW = 24;
		public static final byte ELSE_KW = 25;
		public static final byte FOR_KW = 26;
		public static final byte WHILE_KW = 27;
		public static final byte DO_KW = 28;
		public static final byte MATCH_KW = 29;
		public static final byte CASE_KW = 30;
		
		public static final byte AS_KW = 31;
		public static final byte IN_KW = 32;
		
		public static final byte VERSION_KW = 33; // version keyword
		
		public static final byte RETURN_KW = 34;
		
		public static final byte TRUE = 35;
		public static final byte FALSE = 36;
		public static final byte NULL = 37;
		
		public static final byte OOCDOC = 38; // oodoc comment, e.g. /** blah */
		
		public static final byte NAME = 39; // mostly a Java identifier

		public static final byte BACKSLASH = 40; // \
		public static final byte DOUBLE_BACKSLASH = 41; // \\
		public static final byte AT = 42; // @
		public static final byte HASH = 43; // #
		public static final byte TILDE = 44; // ~
		public static final byte COMMA = 45; // ;
		public static final byte DOT = 46; // .
		public static final byte DOUBLE_DOT = 47; // ..
		public static final byte TRIPLE_DOT = 48; // ...
		public static final byte ARROW = 49; // ->
		public static final byte COLON = 50; // :
		public static final byte LINESEP = 51; // ; or newline
		
		public static final byte PLUS = 52; // +
		public static final byte PLUS_ASSIGN = 53; // +=
		public static final byte MINUS = 54; // -
		public static final byte MINUS_ASSIGN = 55; // -=
		public static final byte STAR = 56; // *
		public static final byte STAR_ASSIGN = 57; // *=
		public static final byte SLASH = 58; // /
		public static final byte SLASH_ASSIGN = 59; // /=
		
		public static final byte PERCENT = 60; // %
		public static final byte BANG = 61; // !
		public static final byte NOT_EQUALS = 62; // !=
		public static final byte QUEST = 63; // ?
		
		public static final byte GREATERTHAN = 64; // >
		public static final byte LESSTHAN = 65; // <
		public static final byte GREATERTHAN_EQUALS = 66; // >=
		public static final byte LESSTHAN_EQUALS = 67; // <=
		public static final byte ASSIGN = 68; // =
		public static final byte DECL_ASSIGN = 69; // :=
		public static final byte EQUALS = 70; // ==
		
		public static final byte DOUBLE_AMPERSAND = 71; // && (logical and)
		public static final byte DOUBLE_PIPE = 72; // || (et non pas double pipe..)
		
		public static final byte AMPERSAND = 73; // & (binary and)
		public static final byte PIPE = 74; // | (binary or)
		
		public static final byte CHAR_LIT = 75; // 'c'
		public static final byte STRING_LIT = 76; // "blah\n"
		
		public static final byte DEC_INT = 77; // 234
		public static final byte HEX_INT = 78; // 0xdeadbeef007
		public static final byte OCT_INT = 79; // 0c777
		public static final byte BIN_INT = 80; // 0b1011
		public static final byte DEC_FLOAT = 81; // 3.14
		
		public static final byte OPEN_PAREN = 82; // (
		public static final byte CLOS_PAREN = 83; // )
		
		public static final byte OPEN_BRACK = 84; // {
		public static final byte CLOS_BRACK = 85; // }
		
		public static final byte OPEN_SQUAR = 86; // [
		public static final byte CLOS_SQUAR = 87; // ]
		
		public static final byte UNSIGNED = 88;
		public static final byte SIGNED = 89;
		public static final byte LONG = 90;
		public static final byte STRUCT = 91;
		public static final byte UNION = 92;
		
		public static final byte BINARY_AND = 93; //  &
		public static final byte CARET = 94; // ^
		
		public static final byte DOUBLE_ARROW = 95; // =>
		public static final byte INTO_KW = 96; // into
	}
	
	public static final class TokenString {
		public static final String[] strings = new String[] {
			"<notoken>",
			"class",
			"cover",
			"func",
			"abstract",
			"extends",
			"from",
			"this",
			"super",
			"new",
			
			"const",
			"final",
			"static",
			
			"include",
			"import",
			"use",
			"extern",
			"inline",
			"proto",
			"unmangled",
			
			"break",
			"continue",
			"fallthrough",
			
			"operator",
			
			"if",
			"else",
			"for",
			"while",
			"do",
			"switch",
			"case",
			
			"as",
			"in",
			
			"version",
			"return",
			
			"true",
			"false",
			"null",
			
			"oocdoc",
			
			"name",
			
			"\\",
			"\\\\",
			"@",
			"#",
			"~",
			",",
			".",
			"..",
			"...",
			"->",
			":",
			"line separator",
			
			"+",
			"+=",
			"-",
			"-=",
			"*",
			"*=",
			"/",
			"/=",
			
			"%",
			"!",
			"!=",
			"?",
			
			">",
			"<",
			">=",
			"<=",
			"=",
			":=",
			"==",
			
			"&&",
			"||",
			
			"&",
			"|",
			
			"CharLiteral",
			"StringLiteral",
			
			"Decimal",
			"Hexadecimal",
			"Octal",
			"Binary",
			"DecimalFloat",
			
			"(",
			")",
			"{",
			"}",
			"[",
			"]",
			
			"unsigned",
			"signed",
			"long",
			"struct",
			"union",
			
			" &",
			"^",
			"=>",

			"into",
		};
		
	}
	
	public final int start;
	public final int length;
	public byte type;
	
	public Token(int start, int length, byte type) {
		super();
		this.start = start;
		this.length = length;
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "'"+TokenString.strings[type]+"'";
	}
	
	public String get(SourceReader sReader) {
		return sReader.getSlice(start, length);
	}

	public int getLength() {
		return length;
	}

	public int getStart() {
		return start;
	}
	
	public Token cloneEnclosing(Token end) {
		Token token = new Token(start, end.getEnd() - start, type);
		return token;
	}

	public int getEnd() {
		return start + length;
	}

	public boolean isNameToken() {
		return type == TokenType.NAME || type == TokenType.CLASS_KW || type == TokenType.IN_KW || type == TokenType.STRUCT;
	}
	
}
