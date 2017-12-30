CREATE TABLE test.persons (
  username varchar(45) NOT NULL,
  name varchar(45) NOT NULL,
  surname varchar(60) NOT NULL,
  address varchar(200) NOT NULL,
  email varchar(45) NOT NULL,
  NIP LONG NOT NULL,
  phone_number LONG NOT NULL,
  date_of_birth DATE NOT NULL,
  name_of_revenue VARCHAR(70) NOT NULL,
  PRIMARY KEY (username),
  KEY fk_username_person (username),
  CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES users (username));

ALTER TABLE test.persons MODIFY COLUMN surname TEXT CHARACTER SET utf8;
ALTER TABLE test.persons MODIFY COLUMN name TEXT CHARACTER SET utf8;
ALTER TABLE test.persons MODIFY COLUMN address TEXT CHARACTER SET utf8;
ALTER TABLE test.persons MODIFY COLUMN name_of_revenue TEXT CHARACTER SET utf8;

INSERT INTO test.persons(username, name, surname, address, email, NIP, phone_number, date_of_birth,
                         name_of_revenue)
VALUES('user2', 'Grzegorz Marian', 'Kozieł-Bartosz', 'ul. Nowa 32 06-400 Ciechanów',
       'koziel_bartosz@wp.pl', '1234567890', '123456789', '1998/12/01', 'Urząd Skarbowy w Ciechanowie');