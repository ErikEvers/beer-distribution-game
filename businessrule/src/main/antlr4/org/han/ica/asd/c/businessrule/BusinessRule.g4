grammar BusinessRule;

//--- LEXER: ---
IF: 'if' | 'If';
THEN: 'then';
DEFAULT: 'default' | 'Default';
ORDER: 'order';
DELIVER: 'deliver';
ROUND : 'round';
FROM : 'from';
TO : 'to';
ENTER : [\n];
WHERE: 'where';
NODE : ('factory' | 'regional warehouse' | 'wholesaler' | 'retailer') (' '?[0-9]+)?;


GAME_VALUE: 'inventory' | 'stock' | 'backlog' | 'incoming order' | 'back orders' | 'budget' | 'outgoing goods'|'ordered';
INT_VALUE: [0-9]+;
PERCENTAGE: [0-9]+'%';
LOWEST: ('lowest' | 'smallest');
HIGHEST: ('highest' | 'biggest');

NOTEQUAL: '!=' | '<>' | ('is ' | 'are ')? 'not equal';
GREATEREQUAL: '>=' | ('is ' | 'are ')?  ('greater' | 'higher') ' than or equal to';
LESSEQUAL: '<=' | ('is ' | 'are ')?  ('less' | 'lower') ' than or equal to';
GREATER: '>' | ('is ' | 'are ')?  ('greater' | 'higher');
LESS: '<' | ('is ' | 'are ')?  ('less' | 'lower');
EQUAL: '=' | ('is ' | 'are ')? 'equal' | 'is' | 'are';

BOOLEAN_OPERATOR: 'and' | 'or';
PLUS: 'plus' | '+';
MIN: 'minus' | '-';
MUL: 'times' | '*';
DIV: 'divided' | '/';

GARBAGE: [a-zA-Z'.]+ -> skip;
WS: [ \t\r]+ -> skip;

//--- PARSER: ---
businessrules: businessrule+ EOF;
businessrule: IF comparisonstatement THEN action ENTER? #ifRule | DEFAULT action ENTER? #defaultRule;
comparisonstatement: comparison | comparison BOOLEAN_OPERATOR comparisonstatement;
comparison: comparison_value comparison_operator comparison_value;
comparison_value: operation | ROUND;
operation: value #defaultOperation | operation PLUS operation #plusOperation | operation MIN operation #minOperation | priority_operation #priorityOperation;
priority_operation: (value MUL value | value MUL priority_operation) #mulOperation | (value DIV value | value DIV priority_operation) #divOperation;
value: INT_VALUE | GAME_VALUE | PERCENTAGE GAME_VALUE | GAME_VALUE NODE | (LOWEST | HIGHEST);
comparison_operator: EQUAL | NOTEQUAL | GREATER | LESS | GREATEREQUAL | LESSEQUAL;
action: ORDER operation person? | DELIVER operation person?;
person: (FROM | TO) NODE (WHERE comparisonstatement)?;

