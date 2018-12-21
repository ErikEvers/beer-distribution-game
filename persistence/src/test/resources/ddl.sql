CREATE TABLE IF NOT EXISTS Beergame (
  GameId varchar(36) NOT NULL,
  GameName varchar(255) NOT NULL,
  GameDate varchar NOT NULL,
  GameEndDate varchar NULL,
  CONSTRAINT PK_Beergame PRIMARY KEY (GameId)
);

CREATE TABLE IF NOT EXISTS Round (
  GameId varchar(36) NOT NULL,
  RoundId int NOT NULL,
  CONSTRAINT PK_Round PRIMARY KEY (GameId, RoundId),
  CONSTRAINT FK_Round_Beergame FOREIGN KEY (GameId) REFERENCES Beergame(GameId)
  ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS Configuration (
  GameId varchar(36) NOT NULL,
  AmountOfRounds int NOT NULL,
  AmountOfFactories int NOT NULL,
  AmountOfWholesales int NOT NULL,
  AmountOfDistributors int NOT NULL,
  AmountOfRetailers int NOT NULL,
  MinimalOrderRetail int NOT NULL,
  MaximumOrderRetail int NOT NULL,
  ContinuePlayingWhenBankrupt bit NOT NULL,
  InsightFacilities bit NOT NULL,
  CONSTRAINT PK_Configuration PRIMARY KEY (GameId),
  CONSTRAINT FK_Configuration_Beergame FOREIGN KEY (GameId) REFERENCES Beergame(GameId)
  ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS  FacilityType (
  GameId varchar(36) NOT NULL,
  FacilityName varchar(24) NOT NULL,
  ValueIncomingGoods int NOT NULL,
  ValueOutgoingGoods int NOT NULL,
  StockHoldingCosts int NOT NULL,
  OpenOrderCosts int NOT NULL,
  StartingBudget int NOT NULL,
  StartingOrder int NOT NULL,
  StartingStock int NOT NULL,
  CONSTRAINT PK_FacilityType PRIMARY KEY (FacilityName, GameId),
  CONSTRAINT FK_FacilityType FOREIGN KEY (GameId) REFERENCES Configuration(GameId)
  ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS  Facility  (
  FacilityId int NOT NULL,
  GameId varchar(36) NOT NULL,
  GameAgentName varchar(255) NOT NULL,
  PlayerId varchar(36) NULL,
  FacilityName varchar(24) NOT NULL,
  CONSTRAINT PK_Facility PRIMARY KEY (GameId, FacilityId),
  CONSTRAINT FK_Facility_Configuration FOREIGN KEY (GameId) REFERENCES  FacilityType(GameId)
  ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT FK_Facility_FacilityType FOREIGN KEY (FacilityName) REFERENCES FacilityType(FacilityName)
  ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT FK_Facility_GameAgent FOREIGN KEY (GameAgentName) REFERENCES GameAgent (GameAgentName)
  ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT FK_Facility_Player FOREIGN KEY (PlayerId) REFERENCES Player (PlayerId)
  ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS  FacilityLinkedTo (
  GameId varchar(36) NOT NULL,
  FacilityIdOrderering int NOT NULL,
  FacilityIdDelivering int NOT NULL,
  CONSTRAINT PK_FacilityLinkedTo PRIMARY KEY (GameId, FacilityIdOrdering, FacilityIdDelivering),
  CONSTRAINT FK_FacilityLinkedTo_Configuration FOREIGN KEY (GameId) REFERENCES Configuration(GameId)
  ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT FK_FacilityLinkedTo_Facility_Delivering FOREIGN KEY (FacilityIdDelivering) REFERENCES Facility(FacilityId)
  ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT FK_FacilityLinkedTo_Facility_Ordering FOREIGN KEY (FacilityIdOrdering) REFERENCES Facility(FacilityId)
  ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS FacilityTurn (
  GameId varchar(36) NOT NULL,
  RoundId int NOT NULL,
  FacilityId int NOT NULL,
  Stock int NOT NULL,
  RemainingBudget int NOT NULL,
  Bankrupt bit NOT NULL,
  CONSTRAINT PK_FacilityTurn PRIMARY KEY (GameId, RoundId, FacilityId),
  CONSTRAINT FK_FacilityTurn_Facility FOREIGN KEY (FacilityId) REFERENCES Facility(FacilityId)
  ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT FK_FacilityTurn_Round FOREIGN KEY (GameId, RoundId) REFERENCES Round(GameId, RoundId)
  ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS GameAgent (
  FacilityId int NOT NULL,
  GameId varchar(36) NOT NULL,
  GameAgentName varchar(255) NOT NULL,
  CONSTRAINT PK_GameAgent PRIMARY KEY (FacilityId, GameId, GameAgentName),
  CONSTRAINT FK_GameAgent_Facility FOREIGN KEY (GameId, FacilityId) REFERENCES Facility(GameId, FacilityId)
  ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS Player (
  GameId varchar(36) NOT NULL,
  PlayerId varchar(36) NOT NULL,
  FacilityId int NOT NULL,
  IpAddress varchar(45) NOT NULL,
  Name varchar(255) NOT NULL,
  CONSTRAINT PK_Player PRIMARY KEY (GameId, PlayerId),
  CONSTRAINT FK_Player_Beergame FOREIGN KEY (GameId) REFERENCES Beergame(GameId)
  ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT FK_Player_Facility FOREIGN KEY (FacilityId) REFERENCES Facility(FacilityId)
  ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS Leader (
  GameId varchar(36) NOT NULL,
  PlayerId varchar(36) NOT NULL,
  Timestamp timestamp NOT NULL,
  CONSTRAINT PK_Leader PRIMARY KEY (GameId, PlayerId),
  CONSTRAINT FK_Leader_Player FOREIGN KEY (GameId, PlayerId) REFERENCES Player(GameId, PlayerId)
  ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS GameBusinessRules (
  FacilityId int NOT NULL,
  GameId varchar(36) NOT NULL,
  GameAgentName varchar(255) NOT NULL,
  GameBusinessRule varchar NOT NULL,
  GameAST varchar NOT NULL,
  CONSTRAINT PK_GameBusinessRules PRIMARY KEY (FacilityId, GameId, GameAgentName, GameBusinessRule, GameAST),
  CONSTRAINT FK_GameBusinessRules_GameAgent FOREIGN KEY (FacilityId, GameId, GameAgentName) REFERENCES GameAgent(FacilityId, GameId, GameAgentName)
  ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS GameBusinessRulesInFacilityTurn (
  RoundId int NOT NULL,
  FacilityId int NOT NULL,
  GameId varchar(36) NOT NULL,
  GameAgentName varchar(255) NOT NULL,
  GameBusinessRule varchar NOT NULL,
  GameAST varchar NOT NULL,
  CONSTRAINT PK_GameBusinessRulesInFacilityTurn PRIMARY KEY (RoundId, FacilityId, GameId, GameAgentName, GameBusinessRule, GameAST),
  CONSTRAINT FK_GameBusinessRulesInFacilityTurn_GameBusinessRules FOREIGN KEY (GameAgentName, GameBusinessRule, GameAST) REFERENCES GameBusinessRules (GameAgentName, GameBusinessRule, GameAST)
  ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT FK_GameBusinessRulesInFacilityTurn_FacilityTurn FOREIGN KEY (RoundId, FacilityId, GameId) REFERENCES FacilityTurn(RoundId, FacilityIdDeliver, FacilityIdOrder, GameId)
  ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS FacilityTurnOrder (
  GameId varchar(36) NOT NULL,
  RoundId int NOT NULL,
  FacilityIdOrder int NOT NULL,
  FacilityId int NOT NULL,
  OrderAmount int NOT NULL,
  CONSTRAINT PK_FacilityTurnOrder PRIMARY KEY (GameId, RoundId, FacilityIdOrder, FacilityId),
  CONSTRAINT FK_FacilityTurnOrder_Facility FOREIGN KEY (FacilityIdOrder) REFERENCES Facility(FacilityId),
  CONSTRAINT FK_FacilityTurnOrder_FacilityTurn FOREIGN KEY (GameId, RoundId, FacilityId) REFERENCES FacilityTurn(GameId, RoundId, FacilityId)
  ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS FacilityTurnDeliver (
  GameId varchar(36) NOT NULL,
  RoundId int NOT NULL,
  FacilityIdDeliver int NOT NULL,
  FacilityId int NOT NULL,
  DeliverrAmount int NOT NULL,
  OpenOrderAmount int NOT NULL,
  CONSTRAINT PK_FacilityTurnOrder PRIMARY KEY (GameId, RoundId, FacilityIdOrder, FacilityId),
  CONSTRAINT FK_FacilityTurnOrder_Facility FOREIGN KEY (FacilityIdDeliver) REFERENCES Facility(FacilityId),
  CONSTRAINT FK_FacilityTurnOrder_FacilityTurn FOREIGN KEY (GameId, RoundId, FacilityId) REFERENCES FacilityTurn(GameId, RoundId, FacilityId)
  ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS ProgrammedAgent (
  ProgrammedAgentName varchar(255) NOT NULL,
  CONSTRAINT PK_ProgrammedAgent PRIMARY KEY (ProgrammedAgentName)
);

CREATE TABLE IF NOT EXISTS  ProgrammedBusinessRules (
  ProgrammedAgentName varchar(255) NOT NULL,
  ProgrammedBusinessRule varchar NOT NULL,
  ProgrammedAST varchar NOT NULL,
  CONSTRAINT PK_ProgrammedBusinessRules PRIMARY KEY (ProgrammedAgentName, ProgrammedBusinessRule, ProgrammedAST),
  CONSTRAINT FK_ProgrammedBusinessRules_ProgrammedAgent FOREIGN KEY (ProgrammedAgentName) REFERENCES ProgrammedAgent(ProgrammedAgentName)
  ON UPDATE CASCADE ON DELETE RESTRICT
);