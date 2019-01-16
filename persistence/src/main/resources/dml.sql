/* Inserting default agent into database */
INSERT INTO ProgrammedAgent(ProgrammedAgentName) VALUES ("Default");
INSERT INTO ProgrammedBusinessRules(ProgrammedAgentName, ProgrammedBusinessRule, ProgrammedAST) VALUES ("Default", "default order 20", "BR(D()A(AR(order)V(20)))"), ("Default", "default deliver 20", "BR(D()A(AR(deliver)V(20)))");