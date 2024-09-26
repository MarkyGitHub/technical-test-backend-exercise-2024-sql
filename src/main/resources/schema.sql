
CREATE TABLE wallet (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,  -- Ensure auto-increment for the primary key
  balance NUMERIC(38, 2)
);


CREATE TABLE wallet_actions (  
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY ,
  wallet_id BIGINT,
  actionType VARCHAR(255),
  amount NUMERIC(38, 2),
  created TIMESTAMP,
  FOREIGN KEY (wallet_id) REFERENCES wallet(id)
);
