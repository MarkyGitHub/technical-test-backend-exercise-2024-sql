
INSERT INTO wallet (balance) VALUES (100.00);
INSERT INTO wallet (balance) VALUES (200.00);


INSERT INTO wallet_actions (wallet_id, actionType, amount, created) VALUES (1, 'TOPUP', 50.00, CURRENT_TIMESTAMP);
INSERT INTO wallet_actions (wallet_id, actionType, amount, created) VALUES (1, 'SPEND', 20.00, CURRENT_TIMESTAMP);
INSERT INTO wallet_actions (wallet_id, actionType, amount, created) VALUES (2, 'TOPUP', 100.00, CURRENT_TIMESTAMP);
INSERT INTO wallet_actions (wallet_id, actionType, amount, created) VALUES (2, 'SPEND', 50.00, CURRENT_TIMESTAMP);
