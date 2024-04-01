INSERT INTO user(user_id,email,password,deleted,authority,company) values (1,"test@email","$2a$10$iPFzYQC.Yw/fESftpYk.TOBQqIX18dD14E7A6y.eV/BrTSxCDKvI.",0,"USER","소융대");
INSERT INTO admin(admin_id,email,password,authority) values (9999,"admin@email","$2a$10$iPFzYQC.Yw/fESftpYk.TOBQqIX18dD14E7A6y.eV/BrTSxCDKvI.","ADMIN");
# INSERT INTO user(user_id,email,password) values (1,"test@email","$2a$10$iPFzYQC.Yw/fESftpYk.TOBQqIX18dD14E7A6y.eV/BrTSxCDKvI.");

INSERT INTO location(location_id,address,camera_id) values (1,"복지관 4층 복도",1),(2,"미래관 4층 복도",2),(3,"북안관 1호 엘레베이터",3);