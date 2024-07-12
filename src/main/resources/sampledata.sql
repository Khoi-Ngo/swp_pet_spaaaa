INSERT INTO `tbl_time_slot`
(`created_by`, `created_time`, `is_deleted`, `modified_by`, `modified_time`, `end_local_date_time`, `start_local_date_time`)
VALUES
    ('admin', NOW(), 0, NULL, NULL, '09:30:00', '08:00:00'),
    ('admin', NOW(), 0, NULL, NULL, '11:00:00', '09:30:00'),
    ('admin', NOW(), 0, NULL, NULL, '12:30:00', '11:00:00'),
    ('admin', NOW(), 0, NULL, NULL, '14:00:00', '12:30:00'),
    ('admin', NOW(), 0, NULL, NULL, '15:30:00', '14:00:00'),
    ('admin', NOW(), 0, NULL, NULL, '17:00:00', '15:30:00');


INSERT INTO `tbl_service_category`
(`created_by`, `created_time`, `is_deleted`, `modified_by`, `modified_time`, `category_name`, `description`)
VALUES
    ('admin', NOW(), 0, NULL, NULL, 'Khách sạn thú cưng', 'Dịch vụ chăm sóc và giữ thú cưng'),
    ('admin', NOW(), 0, NULL, NULL, 'Dịch vụ tắm rửa', 'Dịch vụ chăm sóc lông cho thú cưng '),
    ('admin', NOW(), 0, NULL, NULL, 'Dịch vụ mát xa đặc biệt', 'Dịch vụ chăm sóc sức khỏe thú cưng bằng phương pháp đặc biệt'),
    ('admin', NOW(), 0, NULL, NULL, 'Dịch vụ mát xa', 'Dịch vụ chăm sóc sức khỏe thú cưng'),
    ('admin', NOW(), 0, NULL, NULL, 'Dịch vụ làm đẹp', 'Dịch vụ chăm sóc cho thú cưng của bạn trở nên đẹp nhất');