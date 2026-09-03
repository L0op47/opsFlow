CREATE TABLE sys_operation_log
(
    id                BIGINT PRIMARY KEY AUTO_INCREMENT,
    operator_username VARCHAR(64)  NOT NULL,
    module            VARCHAR(32)  NOT NULL,
    action            VARCHAR(32)  NOT NULL,
    target_id         BIGINT       DEFAULT NULL,
    request_method    VARCHAR(10)  NOT NULL,
    request_uri       VARCHAR(255) NOT NULL,
    ip_address        VARCHAR(64)  DEFAULT NULL,
    success           TINYINT      NOT NULL,
    error_message     VARCHAR(500) DEFAULT NULL,
    duration_ms       BIGINT       NOT NULL,
    created_at        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_operation_log_created_at (created_at),
    INDEX idx_operation_log_operator_created_at
        (operator_username, created_at),
    INDEX idx_operation_log_module_created_at
        (module, created_at)
);