CREATE TABLE sys_notification
(
    id                BIGINT PRIMARY KEY AUTO_INCREMENT,
    event_id          VARCHAR(36)  NOT NULL,
    recipient_user_id BIGINT       NOT NULL,
    type              VARCHAR(32)  NOT NULL,
    title             VARCHAR(100) NOT NULL,
    content           VARCHAR(500) NOT NULL,
    business_type     VARCHAR(32)  NOT NULL,
    business_id       BIGINT       NOT NULL,
    read_status       TINYINT      NOT NULL DEFAULT 0,
    created_at        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    read_at           DATETIME     DEFAULT NULL,

    CONSTRAINT uk_notification_event_recipient
        UNIQUE (event_id, recipient_user_id),

    INDEX idx_notification_recipient_created_at
        (recipient_user_id, created_at),

    INDEX idx_notification_recipient_read_created_at
        (recipient_user_id, read_status, created_at)
) COMMENT = '用户通知表';