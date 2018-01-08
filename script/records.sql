CREATE  TABLE test.trade_records (
  id_trade_record INT (11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(10) NOT NULL,
  sum_net_23 DECIMAL(15,2) NOT NULL,
  sum_net_8 DECIMAL(15,2) NOT NULL,
  sum_net_5 DECIMAL(15,2) NOT NULL,
  sum_vat_23 DECIMAL(15,2) NOT NULL,
  sum_vat_8 DECIMAL(15,2) NOT NULL,
  sum_vat_5 DECIMAL(15,2) NOT NULL,
  sum_gross DECIMAL(15,2) NOT NULL,
  PRIMARY KEY (id_trade_record));

CREATE  TABLE test.purchase_records (
  id_purchase_record INT (11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(10) NOT NULL,
  sum_net_23 DECIMAL(15,2) NOT NULL,
  sum_net_8 DECIMAL(15,2) NOT NULL,
  sum_net_5 DECIMAL(15,2) NOT NULL,
  sum_vat_23 DECIMAL(15,2) NOT NULL,
  sum_vat_8 DECIMAL(15,2) NOT NULL,
  sum_vat_5 DECIMAL(15,2) NOT NULL,
  sum_gross DECIMAL(15,2) NOT NULL,
  sum_fixed_assets_net DECIMAL(15,2) NOT NULL,
  sum_fixed_assets_vat DECIMAL(15,2) NOT NULL,
  sum_fixed_assets_gross DECIMAL(15,2) NOT NULL,
  PRIMARY KEY (id_purchase_record));