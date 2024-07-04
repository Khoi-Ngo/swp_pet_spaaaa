INSERT INTO `tbl_time_slot`
(`created_by`, `created_time`, `is_deleted`, `modified_by`, `modified_time`, `end_local_date_time`, `start_local_date_time`)
VALUES
    ('admin', NOW(), 0, NULL, NULL, '10:00:00', '09:00:00'),
    ('admin', NOW(), 0, NULL, NULL, '14:00:00', '13:00:00'),
    ('admin', NOW(), 0, NULL, NULL, '18:00:00', '17:00:00');


INSERT INTO `tbl_service_category`
(`created_by`, `created_time`, `is_deleted`, `modified_by`, `modified_time`, `category_name`, `description`)
VALUES
    ('admin', NOW(), 0, NULL, NULL, 'Chăm sóc lông', 'Dịch vụ chăm sóc lông cho thú cưng'),
    ('admin', NOW(), 0, NULL, NULL, 'Khách sạn thú cưng', 'Dịch vụ khách sạn và giữ thú cưng'),
    ('admin', NOW(), 0, NULL, NULL, 'Huấn luyện', 'Dịch vụ huấn luyện và chỉnh hành vi thú cưng'),
    ('admin', NOW(), 0, NULL, NULL, 'Thú y', 'Dịch vụ chăm sóc sức khỏe thú y'),
    ('admin', NOW(), 0, NULL, NULL, 'Dinh dưỡng', 'Dịch vụ dinh dưỡng và kế hoạch ăn uống cho thú cưng');