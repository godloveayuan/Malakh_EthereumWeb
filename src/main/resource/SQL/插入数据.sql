-- =======================================    插入数据   ==================================================
-- ===== 代理设备
SELECT *
FROM agent_info;
INSERT INTO agent_info (device_name, device_uid, address, device_mac, manufacture,create_time,update_time)
VALUES ('家庭网关', '89a128b6-dc25-4fd3-afb3-95096abb5861', '127.0.0.1:9090', 'ac:de:48:00:11:22', '圆梦科技',NOW(),NOW()),
       ('卧室代理', 'fe5e19a6-a584-403b-9248-bf6b4561e269', '192.168.1.102:9090', 'cf:de:48:00:11:33', '圆梦科技',NOW(),NOW()),
       ('客厅代理', '7ff44c2e-acff-4dc3-837b-ce2e6eb09242', '192.168.1.103:9090', 'ad:de:48:00:22:33', '圆梦科技',NOW(),NOW());

-- ==== 设备信息
SELECT *
FROM device_info;
SELECT device_name,
       device_mac,
       device_uid,
       device_type,
       attribute_type,
       attribute_position,
       attribute_system,
       manufacture
FROM device_info;
INSERT INTO device_info
(device_name, device_mac, device_uid, device_type, attribute_type, attribute_position, attribute_system, agent_device,
 manufacture,create_time,update_time)
VALUES ('卧室灯光', '18-fe-34-a4-8c-b7', '5067bf08-84a6-452c-80f5-d03f58d32af1', 'wifi', 'terminal', 'bedRoom', 'lighting',
        '89a128b6-dc25-4fd3-afb3-95096abb5861', '圆梦科技',NOW(),NOW()),
       ('客厅空调', '18-fe-34-a4-8c-2b', 'e3f2d408-f45e-4b7f-911d-6243c0e80318', 'wifi', 'terminal', 'drawingRoom',
        'temperature', '89a128b6-dc25-4fd3-afb3-95096abb5861', '圆梦科技',NOW(),NOW()),
       ('客厅温度显示器', 'ab-de-63-00-11-33', '063a786e-2ad8-4d3b-a9fd-97d0a3e568cd', 'other', 'displayer', 'drawingRoom',
        'temperature', '7ff44c2e-acff-4dc3-837b-ce2e6eb09242', '圆梦科技',NOW(),NOW()),
       ('客厅温度控制器', 'cf-de-4a-0b-13-3a', 'ae9ac63c-5eb5-47c2-a041-fdcfb0b930a5', 'other', 'controller', 'drawingRoom',
        'temperature', '7ff44c2e-acff-4dc3-837b-ce2e6eb09242', '圆梦科技',NOW(),NOW()),
       ('卧室灯光控制器', 'ad-de-48-0f-2a-5a', 'f1c91b61-c9f1-4381-93ea-eadb30b2cc7a', 'other', 'controller', 'bedRoom',
        'lighting', 'fe5e19a6-a584-403b-9248-bf6b4561e269', '圆梦科技',NOW(),NOW());


-- ===== 合约信息表
SELECT *
FROM contract_info;
INSERT INTO contract_info (NAME, TYPE, OWNER, address,create_time,update_time)
VALUES ('公共策略合约', 'public', 'all', '0xb71f397af4d9d4dbb5dda6827f40ab5a37b115fb',NOW(),NOW()),
       ('卧室灯光专属策略合约', 'private', '5067bf08-84a6-452c-80f5-d03f58d32af1', '0x7c1d8ebbc9fe31a009d5656559dc9a6a1ab36b06',NOW(),NOW()),
       ('客厅空调专属策略合约', 'private', 'e3f2d408-f45e-4b7f-911d-6243c0e80318', '0xad5d30f6675af26c7f1a6121ec01929a43187b41',NOW(),NOW());

-- ===== 安全规则表
SELECT *
FROM security_rule;

INSERT INTO security_rule
(NAME, TYPE, check_count, check_time_number, check_time_unit, punish_number, punish_unit, kick_out,create_time,update_time)
    VALUE
    ('访问频率限制规则', 1, 10, 1, 'minute', 5, 'minute', FALSE,NOW(),NOW()),
    ('身份认证失败频率限制', 2, 3, 1, 'minute', 5, 'minute', FALSE,NOW(),NOW()),
    ('越权访问频率限制', 3, 5, 1, 'minute', 5, 'minute', FALSE,NOW(),NOW());


-- ======= 用户表
SELECT * FROM user_info;
INSERT INTO user_info (user_name,user_pwd,create_time,update_time)VALUE ('Malakh','123456',NOW(),NOW());

SELECT * FROM device_info WHERE device_uid='ef711328-543b-4761-95ee-e69c790fa808';