/*
We changed the data type of datetime to varchar because SQLite doesn't support the data type datetime.
 */
CREATE TABLE Beergame (
  GameId varchar(36) NOT NULL,
  GameName varchar(255) NOT NULL,
  GameDate varchar NOT NULL,
  GameEndDate varchar NULL,
  CONSTRAINT PK_Beergame PRIMARY KEY (GameId)
);

CREATE TABLE Round (
  GameId varchar(36) NOT NULL,
  RoundId smallint NOT NULL,
  CONSTRAINT PK_Round PRIMARY KEY (GameId, RoundId),
  CONSTRAINT FK_Round_Beergame FOREIGN KEY (GameId) REFERENCES Beergame(GameId)
);

CREATE TABLE Configuration (
  GameId varchar(36) NOT NULL,
  AmountOfRounds smallint NOT NULL,
  AmountOfFactories smallint NOT NULL,
  AmountOfWholesales smallint NOT NULL,
  AmountOfDistributors smallint NOT NULL,
  AmountOfRetailers smallint NOT NULL,
  MinimalOrderRetail smallint NOT NULL,
  MaximumOrderRetail smallint NOT NULL,
  ContinuePlayingWhenBankrupt bit NOT NULL,
  InsightFacilities bit NOT NULL,
  CONSTRAINT PK_Configuration PRIMARY KEY (GameId),
  CONSTRAINT FK_Configuration_Beergame FOREIGN KEY (GameId) REFERENCES Beergame(GameId)
);

CREATE TABLE FacilityType (
  FacilityType varchar(24) NOT NULL,
  GameId varchar(36) NOT NULL,
  ValueIncomingGoods smallint NOT NULL,
  ValueOutgoingGoods smallint NOT NULL,
  StockHoldingCosts smallint NOT NULL,
  OpenOrderCosts smallint NOT NULL,
  StartingBudget smallint NOT NULL,
  StartingOrder smallint NOT NULL,
  CONSTRAINT PK_FacilityType PRIMARY KEY (FacilityType, GameId),
  CONSTRAINT FK_FacilityType FOREIGN KEY (GameId) REFERENCES Configuration(GameId)
);

CREATE TABLE Facility  (
  GameId varchar(36) NOT NULL,
  FacilityId smallint NOT NULL,
  FacilityType varchar(24) NOT NULL,
  PlayerId varchar(36) NULL,
  GameAgentName varchar(255) NOT NULL,
  Bankrupt bit NOT NULL,
  CONSTRAINT FK_Facility_Configuration FOREIGN KEY (GameId) REFERENCES  Configuration(GameId),
  CONSTRAINT FK_Facility_FacilityType FOREIGN KEY (FacilityType) REFERENCES FacilityType(FacilityType),
  CONSTRAINT FK_Facility_GameAgent FOREIGN KEY (GameAgentName) REFERENCES GameAgent (GameAgentName),
  CONSTRAINT FK_Facility_Player FOREIGN KEY (PlayerId) REFERENCES Player (PlayerId),
  CONSTRAINT PK_Facility PRIMARY KEY (GameId, FacilityId)
);

CREATE TABLE FacilityLinkedTo (
  GameId varchar(36) NOT NULL,
  FacilityIdOrder smallint NOT NULL,
  FacilityIdDeliver smallint NOT NULL,
  Active bit NOT NULL,
  CONSTRAINT FK_FacilityLinkedTo_Configuration FOREIGN KEY (GameId) REFERENCES Configuration(GameId),
  CONSTRAINT FK_FacilityLinkedTo_Facility_Deliver FOREIGN KEY (FacilityIdDeliver) REFERENCES Facility(FacilityId),
  CONSTRAINT FK_FacilityLinkedTo_Facility_Order FOREIGN KEY (FacilityIdOrder) REFERENCES Facility(FacilityId),
  CONSTRAINT PK_FacilityLinkedTo PRIMARY KEY (GameId, FacilityIdOrder, FacilityIdDeliver)
);

CREATE TABLE FacilityTurn (
  RoundId smallint NOT NULL,
  FacilityIdOrder smallint NOT NULL,
  FacilityIdDeliver smallint NOT NULL,
  GameId varchar(36) NOT NULL,
  Stock smallint NOT NULL,
  RemainingBudget smallint NOT NULL,
  OrderAmount int NOT NULL,
  OpenOrderAmount int NOT NULL,
  OutgoingGoodsAmount int NOT NULL,
  CONSTRAINT PK_FacilityTurn PRIMARY KEY (RoundId, FacilityIdOrder, GameId, FacilityIdDeliver),
  CONSTRAINT FK_FacilityTurn_Facility FOREIGN KEY (FacilityIdOrder, FacilityIdDeliver, GameId) REFERENCES FacilityLinkedTo(FacilityIdOrder, FacilityIdDeliver, GameId),
  CONSTRAINT FK_FacilityTurn_Round FOREIGN KEY (RoundId) REFERENCES Round(RoundId)
);

CREATE TABLE GameAgent (
  GameId varchar(36) NOT NULL,
  GameAgentName varchar(255) NOT NULL,
  FacilityId smallint NOT NULL,
  CONSTRAINT PK_GameAgent PRIMARY KEY (GameId, GameAgentName, FacilityId),
  CONSTRAINT FK_GameAgent_Facility FOREIGN KEY (GameId, FacilityId) REFERENCES Facility(GameId, FacilityId)
);

CREATE TABLE Player (
  GameId varchar(36) NOT NULL,
  PlayerId varchar(36) NOT NULL,
  IpAddress varchar(45) NOT NULL,
  FacilityId smallint NOT NULL,
  Name varchar(255) NOT NULL,
  IsConnected bit NOT NULL,
  CONSTRAINT PK_Player PRIMARY KEY (GameId, PlayerId),
  CONSTRAINT FK_Player_Beergame FOREIGN KEY (GameId) REFERENCES Beergame(GameId),
  CONSTRAINT FK_Player_Facility FOREIGN KEY (FacilityId) REFERENCES Facility(FacilityId)
);

CREATE TABLE Leader (
  GameId varchar(36) NOT NULL,
  PlayerId varchar(36) NOT NULL,
  IsLeader bit NOT NULL,
  Timestamp timestamp NOT NULL,
  CONSTRAINT PK_Leader PRIMARY KEY (GameId, PlayerId),
  CONSTRAINT FK_Leader_Player FOREIGN KEY (GameId, PlayerId) REFERENCES Player(GameId, PlayerId)
);

CREATE TABLE FacilityTurn_GameBusinessRules (
  RoundId smallint NOT NULL,
  FacilityIdDeliver smallint NOT NULL,
  FacilityIdOrder smallint NOT NULL,
  GameId varchar(36) NOT NULL,
  GameAgentName varchar(255) NOT NULL,
  GameBusinessRule varchar NOT NULL,
  CONSTRAINT PK_FacilityTurn_GameBusinessRules PRIMARY KEY (RoundId, FacilityIdDeliver, FacilityIdOrder, GameId, GameAgentName, GameBusinessRule),
  CONSTRAINT FK_FacilityTurn_GameBusinessRules_GameBusinessRules FOREIGN KEY (GameAgentName, GameBusinessRule) REFERENCES GameBusinessRules (GameAgentName, GameBusinessRule),
  CONSTRAINT FK_FacilityTurn_GameBusinessRules_FacilityTurn FOREIGN KEY (RoundId, FacilityIdDeliver, FacilityIdOrder, GameId) REFERENCES FacilityTurn(RoundId, FacilityIdDeliver, FacilityIdOrder, GameId)
);

CREATE TABLE GameBusinessRules (
  FacilityId smallint NOT NULL,
  GameId varchar(36) NOT NULL,
  GameAgentName varchar(255) NOT NULL,
  GameBusinessRule varchar NOT NULL,
  GameAST varchar NOT NULL,
  CONSTRAINT PK_GameBusinessRules PRIMARY KEY (FacilityId, GameId, GameAgentName, GameBusinessRule),
  CONSTRAINT FK_GameBusinessRules_GameAgent FOREIGN KEY (FacilityId, GameId, GameAgentName) REFERENCES GameAgent(FacilityId, GameId, GameAgentName)
);

CREATE TABLE ProgrammedAgent (
  ProgrammedAgentName varchar(255) NOT NULL,
  CONSTRAINT PK_ProgrammedAgent PRIMARY KEY (ProgrammedAgentName)
);

CREATE TABLE ProgrammedBusinessRules (
  ProgrammedAgentName varchar(255) NOT NULL,
  ProgrammedBusinessRule varchar NOT NULL,
  ProgrammedAST varchar NOT NULL,
  CONSTRAINT PK_ProgrammedBusinessRules PRIMARY KEY (ProgrammedAgentName, ProgrammedBusinessRule, ProgrammedAST),
  CONSTRAINT FK_ProgrammedBusinessRules_ProgrammedAgent FOREIGN KEY (ProgrammedAgentName) REFERENCES ProgrammedAgent(ProgrammedAgentName)
);