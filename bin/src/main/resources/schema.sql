

--------------------------------------------------------
--  DDL for Table CLIENT
--------------------------------------------------------

  CREATE TABLE  "CLIENT" 
   (	"CLIENT_ID" NUMBER(19,0), 
	"NAME" VARCHAR2(30 BYTE), 
	"PHONE" VARCHAR2(10 BYTE), 
	"EMAIL" VARCHAR2(30 BYTE), 
	"POSTAL_CODE" VARCHAR2(10 BYTE), 
	"COUNTRY" VARCHAR2(4 BYTE), 
	"ID_TYPE" VARCHAR2(10 BYTE), 
	"ID_VALUE" VARCHAR2(15 BYTE), 
	"PASSWORD" VARCHAR2(256 BYTE),
    "DOB" DATE
   );
--------------------------------------------------------
--  DDL for Table INVESTMENT_PREFERENCE
--------------------------------------------------------

  CREATE TABLE  "INVESTMENT_PREFERENCE" 
   (	"CLIENT_ID" NUMBER(19,0), 
	"INCOME_CATEGORY" VARCHAR2(20 BYTE), 
	"LENGTH_OF_INVESTMENT" VARCHAR2(10 BYTE), 
	"INVESTMENT_PURPOSE" VARCHAR2(30 BYTE), 
	"RISK_TOLERANCE" VARCHAR2(13 BYTE)
   );


--------------------------------------------------------
--  DDL for Table ORDER_DATA
--------------------------------------------------------

  CREATE TABLE  "ORDER_DATA" 
   (	"ORDER_ID" VARCHAR2(36 BYTE), 
	"INSTRUMENT_ID" VARCHAR2(10 BYTE), 
	"QUANTITY" NUMBER(19,0), 
	"TARGET_PRICE" NUMBER(19,4), 
	"DIRECTION" CHAR(1 BYTE), 
	"CLIENT_ID" NUMBER(19,0), 
	"PORTFOLIO_ID" VARCHAR2(36 BYTE)
   );

--------------------------------------------------------
--  DDL for Table PORTFOLIO
--------------------------------------------------------

  CREATE TABLE  "PORTFOLIO" 
   (	"PORTFOLIO_ID" VARCHAR2(36 BYTE), 
	"CLIENT_ID" NUMBER(19,0), 
	"NAME" VARCHAR2(30 BYTE), 
	"BALANCE" NUMBER(19,4), 
	"P_CATEGORY" VARCHAR2(20 BYTE)
   );


--------------------------------------------------------
--  DDL for Table PORTFOLIO_HOLDING
--------------------------------------------------------

  CREATE TABLE  "PORTFOLIO_HOLDING" 
   (	"PORTFOLIO_ID" VARCHAR2(36 BYTE), 
	"INSTRUMENT_ID" VARCHAR2(10 BYTE), 
	"QUANTITY" NUMBER(19,0), 
	"INVESTMENT_PRICE" NUMBER(19,4), 
	"ADDED_AT" TIMESTAMP (6), 
	"LAST_UPDATE_AT" TIMESTAMP (6)
   );

--------------------------------------------------------
--  DDL for Table TRADE_HISTORY
--------------------------------------------------------

  CREATE TABLE  "TRADE_HISTORY" 
   (	"TRADE_ID" VARCHAR2(36 BYTE), 
	"ORDER_ID" VARCHAR2(36 BYTE), 
	"INSTRUMENT_ID" VARCHAR2(10 BYTE), 
	"QUANTITY" NUMBER(19,0), 
	"EXECUTION_PRICE" NUMBER(19,4), 
	"DIRECTION" CHAR(1 BYTE), 
	"CLIENT_ID" NUMBER(19,0), 
	"PORTFOLIO_ID" VARCHAR2(36 BYTE), 
	"CASH_VALUE" NUMBER(19,4), 
	"TRANSACTION_AT" TIMESTAMP (6)
   );


--------------------------------------------------------
--  Constraints for Table INVESTMENT_PREFERENCE
--------------------------------------------------------

  ALTER TABLE  "INVESTMENT_PREFERENCE" ADD PRIMARY KEY ("CLIENT_ID");


--------------------------------------------------------
--  Constraints for Table CLIENT
--------------------------------------------------------

  ALTER TABLE  "CLIENT" ADD PRIMARY KEY ("CLIENT_ID");
  ALTER TABLE  "CLIENT" ADD UNIQUE ("NAME");
  ALTER TABLE  "CLIENT" ADD UNIQUE ("PHONE");
  ALTER TABLE  "CLIENT" ADD UNIQUE ("EMAIL");
  ALTER TABLE  "CLIENT" ADD CONSTRAINT "UKCIDVAL" UNIQUE ("ID_TYPE", "ID_VALUE");
  ALTER TABLE  "CLIENT" MODIFY ("NAME" NOT NULL ENABLE);
  ALTER TABLE  "CLIENT" MODIFY ("PHONE" NOT NULL ENABLE);
  ALTER TABLE  "CLIENT" MODIFY ("EMAIL" NOT NULL ENABLE);
  ALTER TABLE  "CLIENT" MODIFY ("POSTAL_CODE" NOT NULL ENABLE);
  ALTER TABLE  "CLIENT" MODIFY ("COUNTRY" NOT NULL ENABLE);
  ALTER TABLE  "CLIENT" MODIFY ("ID_TYPE" NOT NULL ENABLE);
  ALTER TABLE  "CLIENT" MODIFY ("ID_VALUE" NOT NULL ENABLE);
  ALTER TABLE  "CLIENT" MODIFY ("PASSWORD" NOT NULL ENABLE);

--------------------------------------------------------
--  Constraints for Table PORTFOLIO_HOLDING
--------------------------------------------------------

  ALTER TABLE  "PORTFOLIO_HOLDING" MODIFY ("QUANTITY" NOT NULL ENABLE);
  ALTER TABLE  "PORTFOLIO_HOLDING" MODIFY ("INVESTMENT_PRICE" NOT NULL ENABLE);
  ALTER TABLE  "PORTFOLIO_HOLDING" MODIFY ("ADDED_AT" NOT NULL ENABLE);
  ALTER TABLE  "PORTFOLIO_HOLDING" MODIFY ("LAST_UPDATE_AT" NOT NULL ENABLE);
  ALTER TABLE  "PORTFOLIO_HOLDING" ADD CONSTRAINT "PK_PH" PRIMARY KEY ("PORTFOLIO_ID", "INSTRUMENT_ID");


--------------------------------------------------------
--  Constraints for Table TRADE_HISTORY
--------------------------------------------------------

  ALTER TABLE  "TRADE_HISTORY" ADD PRIMARY KEY ("TRADE_ID");

--------------------------------------------------------
--  Constraints for Table ORDER_DATA
--------------------------------------------------------

  ALTER TABLE  "ORDER_DATA" ADD PRIMARY KEY ("ORDER_ID");

--------------------------------------------------------
--  Constraints for Table PORTFOLIO
--------------------------------------------------------

  ALTER TABLE  "PORTFOLIO" ADD PRIMARY KEY ("PORTFOLIO_ID");

--------------------------------------------------------
--  Ref Constraints for Table INVESTMENT_PREFERENCE
--------------------------------------------------------

  ALTER TABLE  "INVESTMENT_PREFERENCE" ADD CONSTRAINT "FK_IP_TO_CL" FOREIGN KEY ("CLIENT_ID")
	  REFERENCES  "CLIENT" ("CLIENT_ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ORDER_DATA
--------------------------------------------------------

  ALTER TABLE  "ORDER_DATA" ADD CONSTRAINT "FK_O_TO_P" FOREIGN KEY ("PORTFOLIO_ID")
	  REFERENCES  "PORTFOLIO" ("PORTFOLIO_ID") ON DELETE CASCADE ENABLE;
  ALTER TABLE  "ORDER_DATA" ADD CONSTRAINT "FK_O_TO_CL" FOREIGN KEY ("CLIENT_ID")
	  REFERENCES  "CLIENT" ("CLIENT_ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PORTFOLIO
--------------------------------------------------------

  ALTER TABLE  "PORTFOLIO" ADD CONSTRAINT "FK_PORT_TO_CL" FOREIGN KEY ("CLIENT_ID")
	  REFERENCES  "CLIENT" ("CLIENT_ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PORTFOLIO_HOLDING
--------------------------------------------------------

  ALTER TABLE  "PORTFOLIO_HOLDING" ADD CONSTRAINT "FK_PH_TO_P" FOREIGN KEY ("PORTFOLIO_ID")
	  REFERENCES  "PORTFOLIO" ("PORTFOLIO_ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TRADE_HISTORY
--------------------------------------------------------

  ALTER TABLE  "TRADE_HISTORY" ADD CONSTRAINT "FK_TRADE_TO_P" FOREIGN KEY ("PORTFOLIO_ID")
	  REFERENCES  "PORTFOLIO" ("PORTFOLIO_ID") ON DELETE CASCADE ENABLE;
  ALTER TABLE  "TRADE_HISTORY" ADD CONSTRAINT "FK_TRADE_TO_CL" FOREIGN KEY ("CLIENT_ID")
	  REFERENCES  "CLIENT" ("CLIENT_ID") ON DELETE CASCADE ENABLE;
  ALTER TABLE  "TRADE_HISTORY" ADD CONSTRAINT "FK_TRADE_TO_ORDER" FOREIGN KEY ("ORDER_ID")
	  REFERENCES  "ORDER_DATA" ("ORDER_ID") ON DELETE CASCADE ENABLE;






