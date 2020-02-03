parser grammar BasicParser;
@header {
package antlr;
}

options {
  tokenVocab=BasicLexer;
}

pair_liter: NULL;

array_liter: OPEN_SQUARE ( expr (COMMA expr)* )? CLOSE_SQUARE ;

bool_liter: TRUE | FALSE ;

int_sign: PLUS | MINUS ;

int_liter: (int_sign)? (DIGIT)+ ;

array_elem: IDENT (OPEN_SQUARE expr CLOSE_SQUARE)+ ;

hignp_bin_op : TIME | DIVIDE | MOD ;
binary_oper: PLUS | MINUS;
binary_bool_oper: GREATER| SMALLER | GREATER_E | SMALLER_E | ;
low_binbool_op: EQUAL | NOT_EQUAL ;
lowest_binbool_op: B_AND | B_OR ;
unary_oper: NOT | NEGATIVE | LEN | ORD | CHR ;

char_liter: CHAR_LITER;
string_liter: STR_LITER;
ident: IDENT;

expr: string_liter
| int_liter
| bool_liter
| char_liter
| pair_liter
| ident
| array_elem
| unary_oper expr
| expr hignp_bin_op expr
| expr binary_oper expr
| expr binary_bool_oper expr
| expr low_binbool_op expr
| expr lowest_binbool_op expr
| OPEN_PARENTHESES expr CLOSE_PARENTHESES ;

pair_type: PAIR OPEN_PARENTHESES pair_elem_type COMMA pair_elem_type CLOSE_PARENTHESES ;

pair_elem_type: base_type
| array_type
| PAIR ;



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

stat: ASKIP                      #askip
| type IDENT ASSIGN assign_rhs   #declaration
| assign_lhs ASSIGN assign_rhs   #assignment
| READ assign_lhs                #read
| FREE expr                      #free
| RETURN expr                    #return
| EXIT expr                      #exit
| PRINT expr                     #print
| PRINTLN expr                   #println
| IF expr THEN stat ELSE stat FI #ifthenesle
| WHILE expr DO stat DONE        #whileloop
| BEGIN stat END                 #block
| stat COLON stat                #seq_compose;

param: type IDENT;

param_list: param (COMMA param)*;

func: type IDENT OPEN_PARENTHESES (param_list)? CLOSE_PARENTHESES IS stat END ;

prog: BEGIN (func)* stat END EOF ;
