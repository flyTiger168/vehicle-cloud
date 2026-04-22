-- ============================================================
-- 初始化车辆数据（H2 兼容）
-- VIN 编码规则：17 位，前 5 位为厂商前缀
--   LWVBD = AITO M5
--   LWVBE = AITO M7
--   LWVBF = AITO M9
-- owner_user_id 分散给 1~5 号用户
-- ============================================================

-- ---- AITO M5（10 辆，VIN 前缀 LWVBD）----
INSERT INTO vehicle (vin, model, owner_user_id, created_at) VALUES ('LWVBD1A56NR100001', 'AITO M5', 1, TIMESTAMP '2025-01-15 08:30:00');
INSERT INTO vehicle (vin, model, owner_user_id, created_at) VALUES ('LWVBD1A56NR100002', 'AITO M5', 1, TIMESTAMP '2025-02-10 09:15:00');
INSERT INTO vehicle (vin, model, owner_user_id, created_at) VALUES ('LWVBD1A56NR100003', 'AITO M5', 2, TIMESTAMP '2025-02-20 10:00:00');
INSERT INTO vehicle (vin, model, owner_user_id, created_at) VALUES ('LWVBD1A56NR100004', 'AITO M5', 2, TIMESTAMP '2025-03-05 11:20:00');
INSERT INTO vehicle (vin, model, owner_user_id, created_at) VALUES ('LWVBD1A56NR100005', 'AITO M5', 3, TIMESTAMP '2025-03-18 14:00:00');
INSERT INTO vehicle (vin, model, owner_user_id, created_at) VALUES ('LWVBD1A56NR100006', 'AITO M5', 3, TIMESTAMP '2025-04-01 08:45:00');
INSERT INTO vehicle (vin, model, owner_user_id, created_at) VALUES ('LWVBD1A56NR100007', 'AITO M5', 4, TIMESTAMP '2025-04-12 16:30:00');
INSERT INTO vehicle (vin, model, owner_user_id, created_at) VALUES ('LWVBD1A56NR100008', 'AITO M5', 4, TIMESTAMP '2025-05-08 09:00:00');
INSERT INTO vehicle (vin, model, owner_user_id, created_at) VALUES ('LWVBD1A56NR100009', 'AITO M5', 5, TIMESTAMP '2025-05-22 13:15:00');
INSERT INTO vehicle (vin, model, owner_user_id, created_at) VALUES ('LWVBD1A56NR100010', 'AITO M5', 5, TIMESTAMP '2025-06-01 10:30:00');

-- ---- AITO M7（10 辆，VIN 前缀 LWVBE）----
INSERT INTO vehicle (vin, model, owner_user_id, created_at) VALUES ('LWVBE2B78NR200001', 'AITO M7', 1, TIMESTAMP '2025-01-20 09:00:00');
INSERT INTO vehicle (vin, model, owner_user_id, created_at) VALUES ('LWVBE2B78NR200002', 'AITO M7', 1, TIMESTAMP '2025-02-14 10:30:00');
INSERT INTO vehicle (vin, model, owner_user_id, created_at) VALUES ('LWVBE2B78NR200003', 'AITO M7', 2, TIMESTAMP '2025-03-01 11:45:00');
INSERT INTO vehicle (vin, model, owner_user_id, created_at) VALUES ('LWVBE2B78NR200004', 'AITO M7', 2, TIMESTAMP '2025-03-15 14:20:00');
INSERT INTO vehicle (vin, model, owner_user_id, created_at) VALUES ('LWVBE2B78NR200005', 'AITO M7', 3, TIMESTAMP '2025-04-02 08:00:00');
INSERT INTO vehicle (vin, model, owner_user_id, created_at) VALUES ('LWVBE2B78NR200006', 'AITO M7', 3, TIMESTAMP '2025-04-18 15:30:00');
INSERT INTO vehicle (vin, model, owner_user_id, created_at) VALUES ('LWVBE2B78NR200007', 'AITO M7', 4, TIMESTAMP '2025-05-05 09:45:00');
INSERT INTO vehicle (vin, model, owner_user_id, created_at) VALUES ('LWVBE2B78NR200008', 'AITO M7', 4, TIMESTAMP '2025-05-20 11:00:00');
INSERT INTO vehicle (vin, model, owner_user_id, created_at) VALUES ('LWVBE2B78NR200009', 'AITO M7', 5, TIMESTAMP '2025-06-03 13:30:00');
INSERT INTO vehicle (vin, model, owner_user_id, created_at) VALUES ('LWVBE2B78NR200010', 'AITO M7', 5, TIMESTAMP '2025-06-15 16:00:00');

-- ---- AITO M9（10 辆，VIN 前缀 LWVBF）----
INSERT INTO vehicle (vin, model, owner_user_id, created_at) VALUES ('LWVBF3C90NR300001', 'AITO M9', 1, TIMESTAMP '2025-02-01 08:00:00');
INSERT INTO vehicle (vin, model, owner_user_id, created_at) VALUES ('LWVBF3C90NR300002', 'AITO M9', 1, TIMESTAMP '2025-02-28 10:15:00');
INSERT INTO vehicle (vin, model, owner_user_id, created_at) VALUES ('LWVBF3C90NR300003', 'AITO M9', 2, TIMESTAMP '2025-03-12 11:30:00');
INSERT INTO vehicle (vin, model, owner_user_id, created_at) VALUES ('LWVBF3C90NR300004', 'AITO M9', 2, TIMESTAMP '2025-03-25 14:45:00');
INSERT INTO vehicle (vin, model, owner_user_id, created_at) VALUES ('LWVBF3C90NR300005', 'AITO M9', 3, TIMESTAMP '2025-04-08 09:00:00');
INSERT INTO vehicle (vin, model, owner_user_id, created_at) VALUES ('LWVBF3C90NR300006', 'AITO M9', 3, TIMESTAMP '2025-04-22 15:15:00');
INSERT INTO vehicle (vin, model, owner_user_id, created_at) VALUES ('LWVBF3C90NR300007', 'AITO M9', 4, TIMESTAMP '2025-05-10 10:30:00');
INSERT INTO vehicle (vin, model, owner_user_id, created_at) VALUES ('LWVBF3C90NR300008', 'AITO M9', 4, TIMESTAMP '2025-05-28 12:00:00');
INSERT INTO vehicle (vin, model, owner_user_id, created_at) VALUES ('LWVBF3C90NR300009', 'AITO M9', 5, TIMESTAMP '2025-06-08 08:30:00');
INSERT INTO vehicle (vin, model, owner_user_id, created_at) VALUES ('LWVBF3C90NR300010', 'AITO M9', 5, TIMESTAMP '2025-06-20 17:00:00');
