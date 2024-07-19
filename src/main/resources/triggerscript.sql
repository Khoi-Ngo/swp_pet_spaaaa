DELIMITER //

CREATE TRIGGER trigger_notification_update_status_booking
    AFTER UPDATE
    ON tbl_booking
    FOR EACH ROW
BEGIN
    DECLARE bookedDate VARCHAR(255);
    DECLARE customerid INT;
    DECLARE bookingid INT;
    DECLARE shopownerid INT;
    DECLARE customername VARCHAR(255);
    DECLARE notiStatus VARCHAR(255);

    IF NOT (NEW.status <=> OLD.status) THEN

        SELECT c.local_date
        INTO bookedDate
        FROM tbl_booking b
                 INNER JOIN tbl_cache_shop_time_slot c ON c.id = b.cache_shop_time_slot_id
        WHERE b.id = NEW.id limit 1;

        SELECT DISTINCT u.id
        INTO shopownerid
        FROM tbl_user u
                 INNER JOIN tbl_shop s ON u.id = s.shop_owner_id
        WHERE s.id = NEW.shop_id limit 1;

        SELECT u.username
        INTO customername
        FROM tbl_user u
        WHERE u.id = NEW.customer_id;

        SET customerid = NEW.customer_id;
        SET bookingid = NEW.id;
        SET notiStatus = NEW.status;

        -- Insert into notification -> customer
        INSERT INTO tbl_notification (content, user_id, booking_id, is_deleted, is_read, created_time )
        VALUES (CONCAT('Lịch đặt của bạn vào ngày   ', bookedDate, ' cập nhật: ', notiStatus), customerid, bookingid, false, false, NOW());
        -- Insert into notification -> shop owner
        INSERT INTO tbl_notification (content, user_id, booking_id, is_deleted, is_read, created_time)
        VALUES (CONCAT('Lịch đặt của ', customername, ' vào  ', bookedDate, ' cập nhật: ', notiStatus), shopownerid, bookingid, false, false, NOW());

    END IF;
END //

DELIMITER ;


--    trigger create

DELIMITER //

CREATE TRIGGER trigger_notification_create_booking
    AFTER INSERT
    ON tbl_booking
    FOR EACH ROW
BEGIN
    DECLARE bookedDate VARCHAR(255);
    DECLARE customerid INT;
    DECLARE bookingid INT;
    DECLARE shopownerid INT;
    DECLARE customername VARCHAR(255);

    SELECT c.local_date
    INTO bookedDate
    FROM tbl_booking b
             INNER JOIN tbl_cache_shop_time_slot c ON c.id = b.cache_shop_time_slot_id
    WHERE b.id = NEW.id limit 1;

    SELECT DISTINCT u.id
    INTO shopownerid
    FROM tbl_user u
             INNER JOIN tbl_shop s ON u.id = s.shop_owner_id
    WHERE s.id = NEW.shop_id limit 1;

    SELECT u.username
    INTO customername
    FROM tbl_user u
    WHERE u.id = NEW.customer_id;

    SET customerid = NEW.customer_id;
    SET bookingid = NEW.id;

    -- insert into notification for customer
    INSERT INTO tbl_notification (content, user_id, booking_id, is_deleted, is_read, created_time)
    VALUES (CONCAT('Lịch đặt của bạn vào ', bookedDate, ' đã được tạo'), customerid, bookingid, false, false, NOW());

    -- insert into notification for shopowner
    INSERT INTO tbl_notification (content, user_id, booking_id, is_deleted, is_read, created_time)
    VALUES (CONCAT('Lịch đặt của ', customername, ' vào ', bookedDate,'đã được tạo'), shopownerid, bookingid, false, false, NOW());
END //

DELIMITER ;
