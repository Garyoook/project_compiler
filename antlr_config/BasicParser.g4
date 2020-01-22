parser grammar BasicParser;

options {
  tokenVocab=BasicLexer;
}

pair_liter: NULL;

array_liter: OPEN_SQUARE ( expr (COMMA expr)* )? CLOSE_SQUARE ;

bool_liter: TRUE | FALSE ;

int_sign: PLUS | MINUS ;

int_liter: (int_sign)? (DIGIT)+ ;

array_elem: IDENT (OPEN_SQUARE expr CLOSE_SQUARE)+ ;

binary_oper: TIME | DIVIDE | MOD | PLUS | MINUS | GREATER
| SMALLER | GREATER_E | SMALLER_E | EQUAL | NOT_EQUAL | B_AND | B_OR ;

unary_oper: NOT | NEGATIVE | LEN | ORD | CHR ;

expr: STR_LITER
|int_liter
| bool_liter
| CHAR_LITER
| pair_liter
| IDENT
| array_elem
| unary_oper expr
| expr binary_oper expr
| OPEN_PARENTHESES expr CLOSE_PARENTHESES ;

pair_type: PAIR OPEN_PARENTHESES pair_elem_type COMMA pair_elem_type CLOSE_PARENTHESES ;

pair_elem_type: base_type
| array_type
| PAIR ;


// array_type: type OPEN_SQUARE CLOSE_SQUARE ; //type


base_type: INT
| BOOL
| CHAR
| STRING ;

type: array_type
| base_type
| pair_type ;

array_type: (base_type | pair_type) OPEN_SQUARE CLOSE_SQUARE
 | array_type OPEN_SQUARE CLOSE_SQUARE ;

pair_elem: FST expr
| SND expr;

arg_list: expr (COMMA expr)* ;

assign_rhs: expr
| array_liter
| NEWPAIR OPEN_PARENTHESES expr COMMA expr CLOSE_PARENTHESES
| pair_elem
| CALL IDENT OPEN_PARENTHESES (arg_list)? CLOSE_PARENTHESES ;

assign_lhs: IDENT
| array_elem
| pair_elem ;

stat: ASKIP
| type IDENT ASSIGN assign_rhs
| assign_lhs ASSIGN assign_rhs
| READ assign_lhs
| FREE expr
| RETURN expr
| EXIT expr
| PRINT expr
| PRINTLN expr
| IF expr THEN stat ELSE stat FI
| WHILE expr DO stat DONE
| BEGIN stat END
| stat COLON stat;

param: type IDENT;

param_list: param (COMMA param)*;

func: type IDENT OPEN_PARENTHESES (param_list)? CLOSE_PARENTHESES IS stat END ;

// EOF indicates that the program must consume to the end of the input.
prog: BEGIN (func)* stat END EOF ;
