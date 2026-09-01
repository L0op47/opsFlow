CREATE TABLE sys_asset(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    asset_no VARCHAR(32) NOT NULL ,
    name varchar(100) NOT NULL,
    category VARCHAR(32) NOT NULL ,
    model VARCHAR(100),
    assigned_user_id BIGINT DEFAULT NULL,
    status TINYINT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT  CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_asset_assigned_user_id(assigned_user_id),
    CONSTRAINT uk_asset_no UNIQUE (asset_no)
);