grammar Prototype;

// Example business rules
// if inventory is lower than 20 and inventory is higher than 10 then order 40

// These are not implemented
// if stock is 20% higher than the backlog then order 50% of inventory
// if stock is 20% higher than the backlog then order stock plus 50% of stock
// if stock is 20% higher than the backlog then order stock plus 20

//--- LEXER: ---
IF: 'if';
THEN: 'then';
DEFAULT: 'default';
ORDER: 'order';
ROUND : 'round';

FACILITY: 'factory' | 'distributer' | 'wholesaler' | 'retailer';
GAME_VALUE: 'inventory' | 'stock' | 'backlog' | 'incoming order' | 'back orders';
INT_VALUE: [0-9]+;
PERCENTAGE: [0-9]+'%';

NOTEQUAL: '!=' | 'is '? 'not equal';
GREATER: 'greater' | 'is '? ([0-9]+'% ')? 'higher';
LESS: 'less' | 'is '? ([0-9]+'% ')? 'lower';
EQUAL: '=' | 'equal' | 'is';

BOOLEAN_OPERATOR: 'and' | 'or';
PLUS: '+' | 'plus';
MIN: 'minus' | '-';
MUL: 'times' | '*';
DIV: 'divided' | '/';


GARBAGE: [a-zA-Z]+ -> skip;
WS: [ \t\r\n]+ -> skip;

//--- PARSER: ---
businessrules: businessrule+;
businessrule: IF comparisonstatement THEN action | DEFAULT action;
comparisonstatement: comparison | comparison BOOLEAN_OPERATOR comparisonstatement;
comparison: comparison_value comparison_operator comparison_value;
comparison_value: operation | ROUND;
operation: value #defaultOperation | operation PLUS operation #plusOperation | operation MIN operation #minOperation | operation MUL operation #mulOperation | operation DIV operation #divOperation;
value: INT_VALUE | GAME_VALUE | PERCENTAGE GAME_VALUE | GAME_VALUE FACILITY;
comparison_operator: EQUAL | NOTEQUAL | PERCENTAGE? GREATER | PERCENTAGE? LESS;
action: ORDER operation;

