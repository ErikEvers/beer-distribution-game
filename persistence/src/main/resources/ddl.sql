/*
We changed the data type of datetime to varchar because SQLite doesn't have the data type datetime.
 */
CREATE TABLE Beergame (
  GameName varchar(255) NOT NULL,
  GameDate varchar NOT NULL,
  GameEndDate varchar NOT NULL,
  CONSTRAINT PK_Beergame PRIMARY KEY (GameName, GameDate, GameEndDate)
);

CREATE TABLE Round (
  GameName varchar(255) NOT NULL,
  GameDate varchar NOT NULL,
  GameEndDate varchar NOT NULL,
  RoundId smallint NOT NULL,
  CONSTRAINT PK_Round PRIMARY KEY (GameName, GameDate, GameEndDate, RoundId),
  CONSTRAINT FK_Round_Beergame FOREIGN KEY (GameName, GameDate, GameEndDate) REFERENCES Beergame(GameName, GameDate, GameEndDate)
);

CREATE TABLE Configuration (
  GameName varchar(255) NOT NULL,
  GameDate varchar NOT NULL,
  GameEndDate varchar NOT NULL,
  AmountOfRounds smallint NOT NULL,
  AmountOfFactories smallint NOT NULL,
  AmountOfWholesales smallint NOT NULL,
  AmountOfDistributors smallint NOT NULL,
  AmountOfRetailers smallint NOT NULL,
  MinimalOrderRetail smallint NOT NULL,
  MaximumOrderRetail smallint NOT NULL,
  ContinuePlayingWhenBankrupt bit NOT NULL,
  InsightFacilities bit NOT NULL,
  CONSTRAINT PK_Configuration PRIMARY KEY (GameName, GameDate, GameEndDate),
  CONSTRAINT FK_Configuration_Beergame FOREIGN KEY (GameDate, GameName, GameEndDate) REFERENCES Beergame(GameName, GameDate, GameEndDate)
);

CREATE TABLE FacilityType (
  FacilityType varchar(24) NOT NULL,
  GameName varchar(255) NOT NULL,
  GameDate varchar NOT NULL,
  GameEndDate varchar NOT NULL,
  ValueIncomingGoods smallint NOT NULL,
  ValueOutgoingGoods smallint NOT NULL,
  StockHoldingCosts smallint NOT NULL,
  OpenOrderCosts smallint NOT NULL,
  StartingBudget smallint NOT NULL,
  StartingOrder smallint NOT NULL,
  CONSTRAINT PK_FacilityType PRIMARY KEY (FacilityType, GameName, GameDate, GameEndDate),
  CONSTRAINT FK_FacilityType FOREIGN KEY (GameDate, GameName, GameEndDate) REFERENCES Configuration(GameName, GameDate, GameEndDate)
);

CREATE TABLE Facility  (
  GameName varchar(255) NOT NULL,
  GameDate varchar NOT NULL,
  GameEndDate varchar NOT NULL,
  FacilityId smallint NOT NULL,
  FacilityType varchar(24) NOT NULL,
  PlayerId varchar(36) NULL,
  GameAgentName varchar(255) NOT NULL,
  Bankrupt bit NOT NULL,
  CONSTRAINT FK_Facility_Configuration FOREIGN KEY (GameName, GameDate, GameEndDate) REFERENCES  Configuration(GameName, GameDate, GameEndDate),
  CONSTRAINT FK_Facility_FacilityType FOREIGN KEY (FacilityType) REFERENCES FacilityType(FacilityType),
  CONSTRAINT FK_Facility_GameAgent FOREIGN KEY (GameAgentName) REFERENCES GameAgent (GameAgentName),
  CONSTRAINT FK_Facility_Player FOREIGN KEY (PlayerId) REFERENCES Player (PlayerId),
  CONSTRAINT PK_Facility PRIMARY KEY (GameDate, GameName, GameEndDate, FacilityId)
);

CREATE TABLE FacilityLinkedTo (
  GameName varchar(255) NOT NULL,
  GameDate varchar NOT NULL,
  GameEndDate varchar NOT NULL,
  FacilityIdOrder smallint NOT NULL,
  FacilityIdDeliver smallint NOT NULL,
  Active bit NOT NULL,
  CONSTRAINT FK_FacilityLinkedTo_Configuration FOREIGN KEY (GameName, GameDate, GameEndDate) REFERENCES Configuration(GameName, GameDate, GameEndDate),
  CONSTRAINT FK_FacilityLinkedTo_Facility_Deliver FOREIGN KEY (FacilityIdDeliver) REFERENCES Facility(FacilityId),
  CONSTRAINT FK_FacilityLinkedTo_Facility_Order FOREIGN KEY (FacilityIdOrder) REFERENCES Facility(FacilityId),
  CONSTRAINT PK_FacilityLinkedTo PRIMARY KEY (GameName, GameDate, GameEndDate, FacilityIdOrder, FacilityIdDeliver)
);

CREATE TABLE FacilityTurn (
  RoundId smallint NOT NULL,
  FacilityIdOrder smallint NOT NULL,
  FacilityIdDeliver smallint NOT NULL,
  GameName varchar(255) NOT NULL,
  GameDate varchar NOT NULL,
  GameEndDate varchar NOT NULL,
  Stock smallint NOT NULL,
  RemainingBudget smallint NOT NULL,
  CONSTRAINT PK_FacilityTurn PRIMARY KEY (RoundId, FacilityIdOrder, GameName, GameDate, GameEndDate, FacilityIdDeliver),
  CONSTRAINT FK_FacilityTurn_Facility FOREIGN KEY (FacilityIdOrder, FacilityIdDeliver, GameDate, GameName, GameEndDate) REFERENCES FacilityLinkedTo(FacilityIdOrder, FacilityIdDeliver, GameDate, GameName, GameEndDate),
  CONSTRAINT FK_FacilityTurn_Round FOREIGN KEY (RoundId) REFERENCES Round(RoundId)
);

CREATE TABLE GameAgent (
  GameName varchar(255) NOT NULL,
  GameDate varchar NOT NULL,
  GameEndDate varchar NOT NULL,
  GameAgentName varchar(255) NOT NULL,
  FacilityId smallint NOT NULL,
  CONSTRAINT PK_GameAgent PRIMARY KEY (GameName, GameDate, GameEndDate, GameAgentName, FacilityId),
  CONSTRAINT FK_GameAgent_Facility FOREIGN KEY (GameName, GameDate, GameEndDate, FacilityId) REFERENCES Facility(GameName, GameDate, GameEndDate, FacilityId)
);

CREATE TABLE Player (
  GameName varchar(255) NOT NULL,
  GameDate varchar NOT NULL,
  GameEndDate varchar NOT NULL,
  PlayerId varchar(36) NOT NULL,
  IpAddress varchar(45) NOT NULL,
  FacilityId smallint NOT NULL,
  Name varchar(255) NOT NULL,
  IsConnected bit NOT NULL,
  CONSTRAINT PK_Player PRIMARY KEY (GameName, GameDate, GameEndDate, PlayerId),
  CONSTRAINT FK_Player_Beergame FOREIGN KEY (GameName, GameDate, GameEndDate) REFERENCES Beergame(GameName, GameDate, GameEndDate),
  CONSTRAINT FK_Player_Facility FOREIGN KEY (FacilityId) REFERENCES Facility(FacilityId)
);

CREATE TABLE Leader (
  GameName varchar(255) NOT NULL,
  GameDate varchar NOT NULL,
  GameEndDate varchar NOT NULL,
  PlayerId varchar(36) NOT NULL,
  IsLeader bit NOT NULL,
  Timestamp timestamp NOT NULL,
  CONSTRAINT PK_Leader PRIMARY KEY (GameName, GameDate, GameEndDate, PlayerId),
  CONSTRAINT FK_Leader_Player FOREIGN KEY (GameName, GameDate, GameEndDate, PlayerId) REFERENCES Player(GameName, GameDate, GameEndDate, PlayerId)
);

CREATE TABLE FacilityTurn_GameBusinessRules (
  RoundId smallint NOT NULL,
  FacilityIdDeliver smallint NOT NULL,
  FacilityIdOrder smallint NOT NULL,
  GameName varchar(255) NOT NULL,
  GameDate varchar NOT NULL,
  GameEndDate varchar NOT NULL,
  GameAgentName varchar(255) NOT NULL,
  GameBusinessRule varchar NOT NULL,
  CONSTRAINT PK_FacilityTurn_GameBusinessRules PRIMARY KEY (RoundId, FacilityIdDeliver, FacilityIdOrder, GameName, GameDate, GameEndDate, GameAgentName, GameBusinessRule),
  CONSTRAINT FK_FacilityTurn_GameBusinessRules_GameBusinessRules FOREIGN KEY (GameAgentName, GameBusinessRule) REFERENCES GameBusinessRules (GameAgentName, GameBusinessRule),
  CONSTRAINT FK_FacilityTurn_GameBusinessRules_FacilityTurn FOREIGN KEY (RoundId, FacilityIdDeliver, FacilityIdOrder, GameName, GameDate, GameEndDate) REFERENCES FacilityTurn(RoundId, FacilityIdDeliver, FacilityIdOrder, GameName, GameDate, GameEndDate)
);

CREATE TABLE 'Order' (
  GameName varchar(255) NOT NULL,
  GameDate varchar NOT NULL,
  GameEndDate varchar NOT NULL,
  FacilityIdOrder smallint NOT NULL,
  RoundId smallint NOT NULL,
  FacilityIdDeliver smallint NOT NULL,
  OrderAmount smallint NOT NULL,
  CONSTRAINT PK_Order PRIMARY KEY (GameName, GameDate, GameEndDate, FacilityIdOrder, RoundId, FacilityIdDeliver),
  CONSTRAINT FK_Order_FacilityLinkedTo FOREIGN KEY (GameName, GameDate, GameEndDate, FacilityIdOrder, FacilityIdDeliver) REFERENCES FacilityLinkedTo(GameName, GameDate, GameEndDate, FacilityIdOrder, FacilityIdDeliver),
  CONSTRAINT FK_Order_FacilityTurn  FOREIGN KEY (RoundId) REFERENCES FacilityTurn(RoundId)
);

CREATE TABLE OpenOrder (
  GameName varchar(255) NOT NULL,
  GameDate varchar NOT NULL,
  GameEndDate varchar NOT NULL,
  FacilityIdOrder smallint NOT NULL,
  RoundId smallint NOT NULL,
  FacilityIdDeliver smallint NOT NULL,
  OpenOrderAmount smallint NOT NULL,
  CONSTRAINT PK_OpenOrder PRIMARY KEY (GameName, GameDate, GameEndDate, FacilityIdOrder, FacilityIdDeliver, RoundId),
  CONSTRAINT FK_OpenOrder_FacilityLinkedTo FOREIGN KEY (GameName, GameDate, GameEndDate, FacilityIdOrder, FacilityIdDeliver) REFERENCES FacilityLinkedTo(GameName, GameDate, GameEndDate, FacilityIdOrder, FacilityIdDeliver),
  CONSTRAINT FK_OpenOrder_FacilityTurn FOREIGN KEY (RoundId) REFERENCES FacilityTurn(RoundId)
);

CREATE TABLE OutgoingGoods (
  GameName varchar(255) NOT NULL,
  GameDate varchar NOT NULL,
  GameEndDate varchar NOT NULL,
  FacilityIdOrder smallint NOT NULL,
  RoundId smallint NOT NULL,
  FacilityIdDeliver smallint NOT NULL,
  OutgoingGoodsAmount smallint NOT NULL,
  CONSTRAINT PK_OutgoingGoods PRIMARY KEY (GameName, GameDate, GameEndDate, FacilityIdOrder, FacilityIdDeliver, RoundId),
  CONSTRAINT FK_OutgoingGoods_FacilityLinkedTo FOREIGN KEY (GameName, GameDate, GameEndDate, FacilityIdOrder, FacilityIdDeliver) REFERENCES FacilityLinkedTo(GameName, GameDate, GameEndDate, FacilityIdOrder, FacilityIdDeliver),
  CONSTRAINT FK_OutgoingGoods_FacilityTurn FOREIGN KEY (RoundId) REFERENCES FacilityTurn(RoundId)
);

CREATE TABLE GameBusinessRules (
  FacilityId smallint NOT NULL,
  GameName varchar(255) NOT NULL,
  GameDate varchar NOT NULL,
  GameEndDate varchar NOT NULL,
  GameAgentName varchar(255) NOT NULL,
  GameBusinessRule varchar NOT NULL,
  GameAST varchar NOT NULL,
  CONSTRAINT PK_GameBusinessRules PRIMARY KEY (FacilityId, GameName, GameDate, GameEndDate, GameAgentName, GameBusinessRule),
  CONSTRAINT FK_GameBusinessRules_GameAgent FOREIGN KEY (FacilityId, GameName, GameDate, GameEndDate, GameAgentName) REFERENCES GameAgent(FacilityId, GameName, GameDate, GameEndDate, GameAgentName)
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