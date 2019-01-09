/* Inserting default agent into database */
INSERT INTO ProgrammedAgent(ProgrammedAgentName) VALUES ("Default");
INSERT INTO ProgrammedBusinessRules(ProgrammedAgentName, ProgrammedBusinessRule, ProgrammedAST) VALUES ("Default", "default order incoming order", "BR(D()A(AR(order)V(incoming order)))"), ("Defaut", "default deliver incoming order", "BR(D()A(AR(deliver)V(incoming order)))");