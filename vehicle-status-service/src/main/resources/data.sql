-- =============================================
-- 车辆状态上报初始数据（H2 兼容语法）
-- 6 辆车 × 5 条记录 = 30 条
-- =============================================

-- LWVBD1A56NR100001（M5）- 电量递减，里程递增
INSERT INTO status_report (vin, battery_level, latitude, longitude, mileage, speed, temperature, report_time, created_at)
VALUES ('LWVBD1A56NR100001', 95, 30.5728, 104.0668, 10000.0, 60.0, 25.5, TIMESTAMP '2025-06-01 08:00:00', TIMESTAMP '2025-06-01 08:00:01');
INSERT INTO status_report (vin, battery_level, latitude, longitude, mileage, speed, temperature, report_time, created_at)
VALUES ('LWVBD1A56NR100001', 88, 30.5812, 104.0735, 10120.5, 75.0, 26.0, TIMESTAMP '2025-06-07 10:30:00', TIMESTAMP '2025-06-07 10:30:01');
INSERT INTO status_report (vin, battery_level, latitude, longitude, mileage, speed, temperature, report_time, created_at)
VALUES ('LWVBD1A56NR100001', 76, 30.5645, 104.0892, 10280.3, 55.0, 27.5, TIMESTAMP '2025-06-13 14:00:00', TIMESTAMP '2025-06-13 14:00:01');
INSERT INTO status_report (vin, battery_level, latitude, longitude, mileage, speed, temperature, report_time, created_at)
VALUES ('LWVBD1A56NR100001', 62, 30.5903, 104.1025, 10450.8, 80.0, 28.0, TIMESTAMP '2025-06-20 09:15:00', TIMESTAMP '2025-06-20 09:15:01');
INSERT INTO status_report (vin, battery_level, latitude, longitude, mileage, speed, temperature, report_time, created_at)
VALUES ('LWVBD1A56NR100001', 45, 30.5530, 104.0580, 10620.1, 40.0, 30.0, TIMESTAMP '2025-06-28 16:45:00', TIMESTAMP '2025-06-28 16:45:01');

-- LWVBD1A56NR100002（M5）- 电量递减，里程递增
INSERT INTO status_report (vin, battery_level, latitude, longitude, mileage, speed, temperature, report_time, created_at)
VALUES ('LWVBD1A56NR100002', 92, 30.5600, 104.0450, 8500.0, 50.0, 24.0, TIMESTAMP '2025-06-02 07:30:00', TIMESTAMP '2025-06-02 07:30:01');
INSERT INTO status_report (vin, battery_level, latitude, longitude, mileage, speed, temperature, report_time, created_at)
VALUES ('LWVBD1A56NR100002', 83, 30.5720, 104.0560, 8650.2, 65.0, 25.5, TIMESTAMP '2025-06-08 11:00:00', TIMESTAMP '2025-06-08 11:00:01');
INSERT INTO status_report (vin, battery_level, latitude, longitude, mileage, speed, temperature, report_time, created_at)
VALUES ('LWVBD1A56NR100002', 71, 30.5835, 104.0680, 8820.7, 70.0, 26.5, TIMESTAMP '2025-06-14 15:30:00', TIMESTAMP '2025-06-14 15:30:01');
INSERT INTO status_report (vin, battery_level, latitude, longitude, mileage, speed, temperature, report_time, created_at)
VALUES ('LWVBD1A56NR100002', 58, 30.5510, 104.0790, 8990.4, 45.0, 28.0, TIMESTAMP '2025-06-21 08:45:00', TIMESTAMP '2025-06-21 08:45:01');
INSERT INTO status_report (vin, battery_level, latitude, longitude, mileage, speed, temperature, report_time, created_at)
VALUES ('LWVBD1A56NR100002', 40, 30.5680, 104.1100, 9180.9, 55.0, 29.5, TIMESTAMP '2025-06-27 17:00:00', TIMESTAMP '2025-06-27 17:00:01');

-- LWVBE2B67NR200001（M7）- 电量递减，里程递增
INSERT INTO status_report (vin, battery_level, latitude, longitude, mileage, speed, temperature, report_time, created_at)
VALUES ('LWVBE2B67NR200001', 98, 30.5400, 104.0300, 15000.0, 70.0, 23.0, TIMESTAMP '2025-06-03 06:00:00', TIMESTAMP '2025-06-03 06:00:01');
INSERT INTO status_report (vin, battery_level, latitude, longitude, mileage, speed, temperature, report_time, created_at)
VALUES ('LWVBE2B67NR200001', 85, 30.5520, 104.0420, 15200.6, 85.0, 24.5, TIMESTAMP '2025-06-09 12:00:00', TIMESTAMP '2025-06-09 12:00:01');
INSERT INTO status_report (vin, battery_level, latitude, longitude, mileage, speed, temperature, report_time, created_at)
VALUES ('LWVBE2B67NR200001', 70, 30.5650, 104.0550, 15430.2, 60.0, 26.0, TIMESTAMP '2025-06-15 09:30:00', TIMESTAMP '2025-06-15 09:30:01');
INSERT INTO status_report (vin, battery_level, latitude, longitude, mileage, speed, temperature, report_time, created_at)
VALUES ('LWVBE2B67NR200001', 55, 30.5780, 104.0680, 15680.8, 90.0, 27.5, TIMESTAMP '2025-06-22 14:00:00', TIMESTAMP '2025-06-22 14:00:01');
INSERT INTO status_report (vin, battery_level, latitude, longitude, mileage, speed, temperature, report_time, created_at)
VALUES ('LWVBE2B67NR200001', 38, 30.6100, 104.0800, 15920.3, 50.0, 29.0, TIMESTAMP '2025-06-29 18:30:00', TIMESTAMP '2025-06-29 18:30:01');

-- LWVBE2B67NR200002（M7）- 电量递减，里程递增
INSERT INTO status_report (vin, battery_level, latitude, longitude, mileage, speed, temperature, report_time, created_at)
VALUES ('LWVBE2B67NR200002', 96, 30.5900, 104.0500, 12000.0, 55.0, 22.5, TIMESTAMP '2025-06-04 09:00:00', TIMESTAMP '2025-06-04 09:00:01');
INSERT INTO status_report (vin, battery_level, latitude, longitude, mileage, speed, temperature, report_time, created_at)
VALUES ('LWVBE2B67NR200002', 82, 30.6020, 104.0620, 12180.4, 60.0, 24.0, TIMESTAMP '2025-06-10 13:30:00', TIMESTAMP '2025-06-10 13:30:01');
INSERT INTO status_report (vin, battery_level, latitude, longitude, mileage, speed, temperature, report_time, created_at)
VALUES ('LWVBE2B67NR200002', 68, 30.6150, 104.0750, 12380.9, 75.0, 25.5, TIMESTAMP '2025-06-16 10:00:00', TIMESTAMP '2025-06-16 10:00:01');
INSERT INTO status_report (vin, battery_level, latitude, longitude, mileage, speed, temperature, report_time, created_at)
VALUES ('LWVBE2B67NR200002', 52, 30.5800, 104.0880, 12590.5, 40.0, 27.0, TIMESTAMP '2025-06-23 15:30:00', TIMESTAMP '2025-06-23 15:30:01');
INSERT INTO status_report (vin, battery_level, latitude, longitude, mileage, speed, temperature, report_time, created_at)
VALUES ('LWVBE2B67NR200002', 35, 30.5680, 104.1000, 12810.2, 65.0, 28.5, TIMESTAMP '2025-06-30 07:00:00', TIMESTAMP '2025-06-30 07:00:01');

-- LWVBF3C78NR300001（M9）- 电量递减，里程递增
INSERT INTO status_report (vin, battery_level, latitude, longitude, mileage, speed, temperature, report_time, created_at)
VALUES ('LWVBF3C78NR300001', 100, 30.5300, 104.0200, 20000.0, 80.0, 21.0, TIMESTAMP '2025-06-01 10:00:00', TIMESTAMP '2025-06-01 10:00:01');
INSERT INTO status_report (vin, battery_level, latitude, longitude, mileage, speed, temperature, report_time, created_at)
VALUES ('LWVBF3C78NR300001', 87, 30.5420, 104.0350, 20250.3, 90.0, 23.0, TIMESTAMP '2025-06-08 14:00:00', TIMESTAMP '2025-06-08 14:00:01');
INSERT INTO status_report (vin, battery_level, latitude, longitude, mileage, speed, temperature, report_time, created_at)
VALUES ('LWVBF3C78NR300001', 73, 30.5550, 104.0480, 20530.7, 65.0, 25.0, TIMESTAMP '2025-06-15 11:30:00', TIMESTAMP '2025-06-15 11:30:01');
INSERT INTO status_report (vin, battery_level, latitude, longitude, mileage, speed, temperature, report_time, created_at)
VALUES ('LWVBF3C78NR300001', 60, 30.5680, 104.0610, 20820.1, 70.0, 27.0, TIMESTAMP '2025-06-22 16:00:00', TIMESTAMP '2025-06-22 16:00:01');
INSERT INTO status_report (vin, battery_level, latitude, longitude, mileage, speed, temperature, report_time, created_at)
VALUES ('LWVBF3C78NR300001', 42, 30.5810, 104.0740, 21100.5, 45.0, 29.5, TIMESTAMP '2025-06-29 08:00:00', TIMESTAMP '2025-06-29 08:00:01');

-- LWVBF3C78NR300002（M9）- 电量递减，里程递增
INSERT INTO status_report (vin, battery_level, latitude, longitude, mileage, speed, temperature, report_time, created_at)
VALUES ('LWVBF3C78NR300002', 97, 30.5200, 104.0250, 18000.0, 65.0, 22.0, TIMESTAMP '2025-06-02 11:00:00', TIMESTAMP '2025-06-02 11:00:01');
INSERT INTO status_report (vin, battery_level, latitude, longitude, mileage, speed, temperature, report_time, created_at)
VALUES ('LWVBF3C78NR300002', 84, 30.5350, 104.0380, 18200.8, 75.0, 23.5, TIMESTAMP '2025-06-09 15:00:00', TIMESTAMP '2025-06-09 15:00:01');
INSERT INTO status_report (vin, battery_level, latitude, longitude, mileage, speed, temperature, report_time, created_at)
VALUES ('LWVBF3C78NR300002', 69, 30.5480, 104.0510, 18430.4, 55.0, 25.5, TIMESTAMP '2025-06-16 12:30:00', TIMESTAMP '2025-06-16 12:30:01');
INSERT INTO status_report (vin, battery_level, latitude, longitude, mileage, speed, temperature, report_time, created_at)
VALUES ('LWVBF3C78NR300002', 53, 30.5610, 104.0640, 18680.9, 80.0, 27.5, TIMESTAMP '2025-06-23 17:00:00', TIMESTAMP '2025-06-23 17:00:01');
INSERT INTO status_report (vin, battery_level, latitude, longitude, mileage, speed, temperature, report_time, created_at)
VALUES ('LWVBF3C78NR300002', 36, 30.5740, 104.0770, 18950.3, 50.0, 30.0, TIMESTAMP '2025-06-30 09:30:00', TIMESTAMP '2025-06-30 09:30:01');
