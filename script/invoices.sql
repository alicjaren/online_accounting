CREATE TABLE test.trade_invoices (
  id_trade_invoices INT(11) NOT NULL AUTO_INCREMENT,
  invoice_number varchar(45) NOT NULL,
  date_of_issue DATE NOT NULL,
  date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  date_changed  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  trade_partner_NIP INT(10) NOT NULL,
  trade_partner_name VARCHAR(100) CHARACTER SET utf8 NOT NULL,
  dealing_thing_name varchar(100) CHARACTER SET utf8 NOT NULL,
  net_23 DECIMAL(15,2) NOT NULL,
  net_8 DECIMAL(15,2) NOT NULL,
  net_5 DECIMAL(15,2) NOT NULL,
  vat_23 DECIMAL(15,2) NOT NULL,
  vat_8 DECIMAL(15,2) NOT NULL,
  vat_5 DECIMAL(15,2) NOT NULL,
  gross DECIMAL(15,2) NOT NULL,
  trade_record INT(11) NOT NULL,
  PRIMARY KEY (id_trade_invoices),
  KEY fk_trade_invoice_trade_record (trade_record),
  CONSTRAINT fk_trade_invoice_trade_record FOREIGN KEY (trade_record) REFERENCES trade_records (id_trade_record));


CREATE TABLE test.purchase_invoices (
  id_purchase_invoices INT(11) NOT NULL AUTO_INCREMENT,
  invoice_number varchar(45) NOT NULL,
  date_of_issue DATE NOT NULL,
  date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  date_changed  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  trade_partner_NIP INT(10) NOT NULL,
  trade_partner_name VARCHAR(100) CHARACTER SET utf8 NOT NULL,
  dealing_thing_name varchar(100) CHARACTER SET utf8 NOT NULL,
  net_23 DECIMAL(15,2) NOT NULL,
  net_8 DECIMAL(15,2) NOT NULL,
  net_5 DECIMAL(15,2) NOT NULL,
  vat_23 DECIMAL(15,2) NOT NULL,
  vat_8 DECIMAL(15,2) NOT NULL,
  vat_5 DECIMAL(15,2) NOT NULL,
  gross DECIMAL(15,2) NOT NULL,
  deducted BOOLEAN NOT NULL,
  fixed_assets_net DECIMAL(15,2) NOT NULL,
  fixed_assets_vat DECIMAL(15,2) NOT NULL,
  fixed_assets_gross DECIMAL(15,2) NOT NULL,
  purchase_record INT(11) NOT NULL,
  PRIMARY KEY (id_purchase_invoices),
  KEY fk_purchase_invoice_purchase_record (purchase_record),
  CONSTRAINT fk_purchase_invoice_purchase_record FOREIGN KEY (purchase_record) REFERENCES purchase_records (id_purchase_record));