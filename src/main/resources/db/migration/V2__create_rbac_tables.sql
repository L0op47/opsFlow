CREATE TABLE sys_role(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(32) NOT NULL,
    name VARCHAR(64) NOT NULL,
    description VARCHAR(255),
    status TINYINT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uk_role_code UNIQUE (code)
);
CREATE TABLE sys_permission(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(64) NOT NULL,
    name VARCHAR(64) NOT NULL,
    description VARCHAR(255),
    status TINYINT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uk_permission_code UNIQUE (code)
);
CREATE TABLE sys_user_role(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_user_role UNIQUE (user_id, role_id),
    CONSTRAINT fk_user_role_user
        FOREIGN KEY (user_id) REFERENCES sys_user(id),
    CONSTRAINT fk_user_role_role
        FOREIGN KEY (role_id) REFERENCES sys_role(id),
    INDEX idx_user_role_role_id (role_id)
);
CREATE TABLE sys_role_permission
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    permission_id BIGINT   NOT NULL,
    role_id       BIGINT   NOT NULL,
    created_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_role_permission UNIQUE (role_id, permission_id),
    CONSTRAINT fk_role_permission_role
        FOREIGN KEY (role_id) REFERENCES sys_role (id),
    CONSTRAINT fk_role_permission_permission
        FOREIGN KEY (permission_id) REFERENCES sys_permission (id),
    INDEX idx_role_permission_permission_id (permission_id)
);

