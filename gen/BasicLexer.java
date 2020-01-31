// Generated from /homes/cl10418/wacc_49/antlr_config/BasicLexer.g4 by ANTLR 4.8
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class BasicLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		BEGIN=1, END=2, IS=3, ASKIP=4, INT=5, DIGIT=6, STRING=7, CHAR=8, BOOL=9, 
		TRUE=10, FALSE=11, NOT=12, NEGATIVE=13, LEN=14, ORD=15, CHR=16, PLUS=17, 
		MINUS=18, EQUAL=19, NOT_EQUAL=20, B_AND=21, B_OR=22, TIME=23, DIVIDE=24, 
		GREATER=25, SMALLER=26, MOD=27, GREATER_E=28, SMALLER_E=29, READ=30, FREE=31, 
		RETURN=32, EXIT=33, PRINT=34, PRINTLN=35, IF=36, THEN=37, ELSE=38, FI=39, 
		WHILE=40, DO=41, DONE=42, OPEN_PARENTHESES=43, CLOSE_PARENTHESES=44, OPEN_SQUARE=45, 
		CLOSE_SQUARE=46, NEWPAIR=47, PAIR=48, FST=49, SND=50, COLON=51, COMMA=52, 
		CALL=53, ASSIGN=54, NULL=55, COMMENT=56, ST=57, AP=58, IGNOR=59, CHAR_LITER=60, 
		STR_LITER=61, IDENT=62, ANY_ASCII=63, ESCAPED_CHAR=64;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"BEGIN", "END", "IS", "ASKIP", "INT", "DIGIT", "STRING", "CHAR", "BOOL", 
			"TRUE", "FALSE", "NOT", "NEGATIVE", "LEN", "ORD", "CHR", "PLUS", "MINUS", 
			"EQUAL", "NOT_EQUAL", "B_AND", "B_OR", "TIME", "DIVIDE", "GREATER", "SMALLER", 
			"MOD", "GREATER_E", "SMALLER_E", "READ", "FREE", "RETURN", "EXIT", "PRINT", 
			"PRINTLN", "IF", "THEN", "ELSE", "FI", "WHILE", "DO", "DONE", "OPEN_PARENTHESES", 
			"CLOSE_PARENTHESES", "OPEN_SQUARE", "CLOSE_SQUARE", "NEWPAIR", "PAIR", 
			"FST", "SND", "COLON", "COMMA", "CALL", "ASSIGN", "NULL", "COMMENT", 
			"ST", "AP", "IGNOR", "CHARACTER", "CHAR_LITER", "STR_LITER", "IDENT", 
			"ANY_ASCII", "ESCAPED_CHAR"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'begin'", "'end'", "'is'", "'skip'", "'int'", null, "'string'", 
			"'char'", "'bool'", "'true'", "'false'", "'!'", null, "'len'", "'ord'", 
			"'chr'", "'+'", null, "'=='", "'!='", "'&&'", "'||'", "'*'", "'/'", "'>'", 
			"'<'", "'%'", "'>='", "'<='", "'read'", "'free'", "'return'", "'exit'", 
			"'print'", "'println'", "'if'", "'then'", "'else'", "'fi'", "'while'", 
			"'do'", "'done'", "'('", "')'", "'['", "']'", "'newpair'", "'pair'", 
			"'fst'", "'snd'", "';'", "','", "'call'", "'='", "'null'", null, "'\"'", 
			"'''"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "BEGIN", "END", "IS", "ASKIP", "INT", "DIGIT", "STRING", "CHAR", 
			"BOOL", "TRUE", "FALSE", "NOT", "NEGATIVE", "LEN", "ORD", "CHR", "PLUS", 
			"MINUS", "EQUAL", "NOT_EQUAL", "B_AND", "B_OR", "TIME", "DIVIDE", "GREATER", 
			"SMALLER", "MOD", "GREATER_E", "SMALLER_E", "READ", "FREE", "RETURN", 
			"EXIT", "PRINT", "PRINTLN", "IF", "THEN", "ELSE", "FI", "WHILE", "DO", 
			"DONE", "OPEN_PARENTHESES", "CLOSE_PARENTHESES", "OPEN_SQUARE", "CLOSE_SQUARE", 
			"NEWPAIR", "PAIR", "FST", "SND", "COLON", "COMMA", "CALL", "ASSIGN", 
			"NULL", "COMMENT", "ST", "AP", "IGNOR", "CHAR_LITER", "STR_LITER", "IDENT", 
			"ANY_ASCII", "ESCAPED_CHAR"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public BasicLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "BasicLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2B\u0189\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3"+
		"\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\b\3\b\3\b\3"+
		"\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13"+
		"\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\17\3"+
		"\17\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3"+
		"\24\3\24\3\25\3\25\3\25\3\26\3\26\3\26\3\27\3\27\3\27\3\30\3\30\3\31\3"+
		"\31\3\32\3\32\3\33\3\33\3\34\3\34\3\35\3\35\3\35\3\36\3\36\3\36\3\37\3"+
		"\37\3\37\3\37\3\37\3 \3 \3 \3 \3 \3!\3!\3!\3!\3!\3!\3!\3\"\3\"\3\"\3\""+
		"\3\"\3#\3#\3#\3#\3#\3#\3$\3$\3$\3$\3$\3$\3$\3$\3%\3%\3%\3&\3&\3&\3&\3"+
		"&\3\'\3\'\3\'\3\'\3\'\3(\3(\3(\3)\3)\3)\3)\3)\3)\3*\3*\3*\3+\3+\3+\3+"+
		"\3+\3,\3,\3-\3-\3.\3.\3/\3/\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3"+
		"\61\3\61\3\61\3\61\3\61\3\62\3\62\3\62\3\62\3\63\3\63\3\63\3\63\3\64\3"+
		"\64\3\65\3\65\3\66\3\66\3\66\3\66\3\66\3\67\3\67\38\38\38\38\38\39\39"+
		"\79\u015b\n9\f9\169\u015e\139\39\39\39\39\3:\3:\3;\3;\3<\3<\3<\3<\3=\3"+
		"=\5=\u016e\n=\3>\3>\3>\3>\3?\3?\7?\u0176\n?\f?\16?\u0179\13?\3?\3?\3@"+
		"\3@\3@\7@\u0180\n@\f@\16@\u0183\13@\3A\3A\3B\3B\3B\3\u015c\2C\3\3\5\4"+
		"\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22"+
		"#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C"+
		"#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62c\63e\64g\65i\66k\67m8o9q:s;u<w"+
		"=y\2{>}?\177@\u0081A\u0083B\3\2\6\5\2\13\f\17\17\"\"\5\2C\\aac|\5\2$$"+
		"))^^\13\2$$))\62\62^^ddhhppttvv\2\u018c\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3"+
		"\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2"+
		"\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35"+
		"\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)"+
		"\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2"+
		"\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2"+
		"A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3"+
		"\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2"+
		"\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2"+
		"g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3"+
		"\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2{\3\2\2\2\2}\3\2\2\2\2\177\3\2\2\2\2\u0081"+
		"\3\2\2\2\2\u0083\3\2\2\2\3\u0085\3\2\2\2\5\u008b\3\2\2\2\7\u008f\3\2\2"+
		"\2\t\u0092\3\2\2\2\13\u0097\3\2\2\2\r\u009b\3\2\2\2\17\u009d\3\2\2\2\21"+
		"\u00a4\3\2\2\2\23\u00a9\3\2\2\2\25\u00ae\3\2\2\2\27\u00b3\3\2\2\2\31\u00b9"+
		"\3\2\2\2\33\u00bb\3\2\2\2\35\u00bd\3\2\2\2\37\u00c1\3\2\2\2!\u00c5\3\2"+
		"\2\2#\u00c9\3\2\2\2%\u00cb\3\2\2\2\'\u00cd\3\2\2\2)\u00d0\3\2\2\2+\u00d3"+
		"\3\2\2\2-\u00d6\3\2\2\2/\u00d9\3\2\2\2\61\u00db\3\2\2\2\63\u00dd\3\2\2"+
		"\2\65\u00df\3\2\2\2\67\u00e1\3\2\2\29\u00e3\3\2\2\2;\u00e6\3\2\2\2=\u00e9"+
		"\3\2\2\2?\u00ee\3\2\2\2A\u00f3\3\2\2\2C\u00fa\3\2\2\2E\u00ff\3\2\2\2G"+
		"\u0105\3\2\2\2I\u010d\3\2\2\2K\u0110\3\2\2\2M\u0115\3\2\2\2O\u011a\3\2"+
		"\2\2Q\u011d\3\2\2\2S\u0123\3\2\2\2U\u0126\3\2\2\2W\u012b\3\2\2\2Y\u012d"+
		"\3\2\2\2[\u012f\3\2\2\2]\u0131\3\2\2\2_\u0133\3\2\2\2a\u013b\3\2\2\2c"+
		"\u0140\3\2\2\2e\u0144\3\2\2\2g\u0148\3\2\2\2i\u014a\3\2\2\2k\u014c\3\2"+
		"\2\2m\u0151\3\2\2\2o\u0153\3\2\2\2q\u0158\3\2\2\2s\u0163\3\2\2\2u\u0165"+
		"\3\2\2\2w\u0167\3\2\2\2y\u016d\3\2\2\2{\u016f\3\2\2\2}\u0173\3\2\2\2\177"+
		"\u017c\3\2\2\2\u0081\u0184\3\2\2\2\u0083\u0186\3\2\2\2\u0085\u0086\7d"+
		"\2\2\u0086\u0087\7g\2\2\u0087\u0088\7i\2\2\u0088\u0089\7k\2\2\u0089\u008a"+
		"\7p\2\2\u008a\4\3\2\2\2\u008b\u008c\7g\2\2\u008c\u008d\7p\2\2\u008d\u008e"+
		"\7f\2\2\u008e\6\3\2\2\2\u008f\u0090\7k\2\2\u0090\u0091\7u\2\2\u0091\b"+
		"\3\2\2\2\u0092\u0093\7u\2\2\u0093\u0094\7m\2\2\u0094\u0095\7k\2\2\u0095"+
		"\u0096\7r\2\2\u0096\n\3\2\2\2\u0097\u0098\7k\2\2\u0098\u0099\7p\2\2\u0099"+
		"\u009a\7v\2\2\u009a\f\3\2\2\2\u009b\u009c\4\62;\2\u009c\16\3\2\2\2\u009d"+
		"\u009e\7u\2\2\u009e\u009f\7v\2\2\u009f\u00a0\7t\2\2\u00a0\u00a1\7k\2\2"+
		"\u00a1\u00a2\7p\2\2\u00a2\u00a3\7i\2\2\u00a3\20\3\2\2\2\u00a4\u00a5\7"+
		"e\2\2\u00a5\u00a6\7j\2\2\u00a6\u00a7\7c\2\2\u00a7\u00a8\7t\2\2\u00a8\22"+
		"\3\2\2\2\u00a9\u00aa\7d\2\2\u00aa\u00ab\7q\2\2\u00ab\u00ac\7q\2\2\u00ac"+
		"\u00ad\7n\2\2\u00ad\24\3\2\2\2\u00ae\u00af\7v\2\2\u00af\u00b0\7t\2\2\u00b0"+
		"\u00b1\7w\2\2\u00b1\u00b2\7g\2\2\u00b2\26\3\2\2\2\u00b3\u00b4\7h\2\2\u00b4"+
		"\u00b5\7c\2\2\u00b5\u00b6\7n\2\2\u00b6\u00b7\7u\2\2\u00b7\u00b8\7g\2\2"+
		"\u00b8\30\3\2\2\2\u00b9\u00ba\7#\2\2\u00ba\32\3\2\2\2\u00bb\u00bc\7/\2"+
		"\2\u00bc\34\3\2\2\2\u00bd\u00be\7n\2\2\u00be\u00bf\7g\2\2\u00bf\u00c0"+
		"\7p\2\2\u00c0\36\3\2\2\2\u00c1\u00c2\7q\2\2\u00c2\u00c3\7t\2\2\u00c3\u00c4"+
		"\7f\2\2\u00c4 \3\2\2\2\u00c5\u00c6\7e\2\2\u00c6\u00c7\7j\2\2\u00c7\u00c8"+
		"\7t\2\2\u00c8\"\3\2\2\2\u00c9\u00ca\7-\2\2\u00ca$\3\2\2\2\u00cb\u00cc"+
		"\7/\2\2\u00cc&\3\2\2\2\u00cd\u00ce\7?\2\2\u00ce\u00cf\7?\2\2\u00cf(\3"+
		"\2\2\2\u00d0\u00d1\7#\2\2\u00d1\u00d2\7?\2\2\u00d2*\3\2\2\2\u00d3\u00d4"+
		"\7(\2\2\u00d4\u00d5\7(\2\2\u00d5,\3\2\2\2\u00d6\u00d7\7~\2\2\u00d7\u00d8"+
		"\7~\2\2\u00d8.\3\2\2\2\u00d9\u00da\7,\2\2\u00da\60\3\2\2\2\u00db\u00dc"+
		"\7\61\2\2\u00dc\62\3\2\2\2\u00dd\u00de\7@\2\2\u00de\64\3\2\2\2\u00df\u00e0"+
		"\7>\2\2\u00e0\66\3\2\2\2\u00e1\u00e2\7\'\2\2\u00e28\3\2\2\2\u00e3\u00e4"+
		"\7@\2\2\u00e4\u00e5\7?\2\2\u00e5:\3\2\2\2\u00e6\u00e7\7>\2\2\u00e7\u00e8"+
		"\7?\2\2\u00e8<\3\2\2\2\u00e9\u00ea\7t\2\2\u00ea\u00eb\7g\2\2\u00eb\u00ec"+
		"\7c\2\2\u00ec\u00ed\7f\2\2\u00ed>\3\2\2\2\u00ee\u00ef\7h\2\2\u00ef\u00f0"+
		"\7t\2\2\u00f0\u00f1\7g\2\2\u00f1\u00f2\7g\2\2\u00f2@\3\2\2\2\u00f3\u00f4"+
		"\7t\2\2\u00f4\u00f5\7g\2\2\u00f5\u00f6\7v\2\2\u00f6\u00f7\7w\2\2\u00f7"+
		"\u00f8\7t\2\2\u00f8\u00f9\7p\2\2\u00f9B\3\2\2\2\u00fa\u00fb\7g\2\2\u00fb"+
		"\u00fc\7z\2\2\u00fc\u00fd\7k\2\2\u00fd\u00fe\7v\2\2\u00feD\3\2\2\2\u00ff"+
		"\u0100\7r\2\2\u0100\u0101\7t\2\2\u0101\u0102\7k\2\2\u0102\u0103\7p\2\2"+
		"\u0103\u0104\7v\2\2\u0104F\3\2\2\2\u0105\u0106\7r\2\2\u0106\u0107\7t\2"+
		"\2\u0107\u0108\7k\2\2\u0108\u0109\7p\2\2\u0109\u010a\7v\2\2\u010a\u010b"+
		"\7n\2\2\u010b\u010c\7p\2\2\u010cH\3\2\2\2\u010d\u010e\7k\2\2\u010e\u010f"+
		"\7h\2\2\u010fJ\3\2\2\2\u0110\u0111\7v\2\2\u0111\u0112\7j\2\2\u0112\u0113"+
		"\7g\2\2\u0113\u0114\7p\2\2\u0114L\3\2\2\2\u0115\u0116\7g\2\2\u0116\u0117"+
		"\7n\2\2\u0117\u0118\7u\2\2\u0118\u0119\7g\2\2\u0119N\3\2\2\2\u011a\u011b"+
		"\7h\2\2\u011b\u011c\7k\2\2\u011cP\3\2\2\2\u011d\u011e\7y\2\2\u011e\u011f"+
		"\7j\2\2\u011f\u0120\7k\2\2\u0120\u0121\7n\2\2\u0121\u0122\7g\2\2\u0122"+
		"R\3\2\2\2\u0123\u0124\7f\2\2\u0124\u0125\7q\2\2\u0125T\3\2\2\2\u0126\u0127"+
		"\7f\2\2\u0127\u0128\7q\2\2\u0128\u0129\7p\2\2\u0129\u012a\7g\2\2\u012a"+
		"V\3\2\2\2\u012b\u012c\7*\2\2\u012cX\3\2\2\2\u012d\u012e\7+\2\2\u012eZ"+
		"\3\2\2\2\u012f\u0130\7]\2\2\u0130\\\3\2\2\2\u0131\u0132\7_\2\2\u0132^"+
		"\3\2\2\2\u0133\u0134\7p\2\2\u0134\u0135\7g\2\2\u0135\u0136\7y\2\2\u0136"+
		"\u0137\7r\2\2\u0137\u0138\7c\2\2\u0138\u0139\7k\2\2\u0139\u013a\7t\2\2"+
		"\u013a`\3\2\2\2\u013b\u013c\7r\2\2\u013c\u013d\7c\2\2\u013d\u013e\7k\2"+
		"\2\u013e\u013f\7t\2\2\u013fb\3\2\2\2\u0140\u0141\7h\2\2\u0141\u0142\7"+
		"u\2\2\u0142\u0143\7v\2\2\u0143d\3\2\2\2\u0144\u0145\7u\2\2\u0145\u0146"+
		"\7p\2\2\u0146\u0147\7f\2\2\u0147f\3\2\2\2\u0148\u0149\7=\2\2\u0149h\3"+
		"\2\2\2\u014a\u014b\7.\2\2\u014bj\3\2\2\2\u014c\u014d\7e\2\2\u014d\u014e"+
		"\7c\2\2\u014e\u014f\7n\2\2\u014f\u0150\7n\2\2\u0150l\3\2\2\2\u0151\u0152"+
		"\7?\2\2\u0152n\3\2\2\2\u0153\u0154\7p\2\2\u0154\u0155\7w\2\2\u0155\u0156"+
		"\7n\2\2\u0156\u0157\7n\2\2\u0157p\3\2\2\2\u0158\u015c\7%\2\2\u0159\u015b"+
		"\13\2\2\2\u015a\u0159\3\2\2\2\u015b\u015e\3\2\2\2\u015c\u015d\3\2\2\2"+
		"\u015c\u015a\3\2\2\2\u015d\u015f\3\2\2\2\u015e\u015c\3\2\2\2\u015f\u0160"+
		"\7\f\2\2\u0160\u0161\3\2\2\2\u0161\u0162\b9\2\2\u0162r\3\2\2\2\u0163\u0164"+
		"\7$\2\2\u0164t\3\2\2\2\u0165\u0166\7)\2\2\u0166v\3\2\2\2\u0167\u0168\t"+
		"\2\2\2\u0168\u0169\3\2\2\2\u0169\u016a\b<\2\2\u016ax\3\2\2\2\u016b\u016e"+
		"\5\u0081A\2\u016c\u016e\5\u0083B\2\u016d\u016b\3\2\2\2\u016d\u016c\3\2"+
		"\2\2\u016ez\3\2\2\2\u016f\u0170\5u;\2\u0170\u0171\5y=\2\u0171\u0172\5"+
		"u;\2\u0172|\3\2\2\2\u0173\u0177\5s:\2\u0174\u0176\5y=\2\u0175\u0174\3"+
		"\2\2\2\u0176\u0179\3\2\2\2\u0177\u0175\3\2\2\2\u0177\u0178\3\2\2\2\u0178"+
		"\u017a\3\2\2\2\u0179\u0177\3\2\2\2\u017a\u017b\5s:\2\u017b~\3\2\2\2\u017c"+
		"\u0181\t\3\2\2\u017d\u0180\t\3\2\2\u017e\u0180\5\r\7\2\u017f\u017d\3\2"+
		"\2\2\u017f\u017e\3\2\2\2\u0180\u0183\3\2\2\2\u0181\u017f\3\2\2\2\u0181"+
		"\u0182\3\2\2\2\u0182\u0080\3\2\2\2\u0183\u0181\3\2\2\2\u0184\u0185\n\4"+
		"\2\2\u0185\u0082\3\2\2\2\u0186\u0187\7^\2\2\u0187\u0188\t\5\2\2\u0188"+
		"\u0084\3\2\2\2\b\2\u015c\u016d\u0177\u017f\u0181\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}