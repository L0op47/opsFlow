
CREATE TABLE sys_department
(
    id         BIGINT AUTO_INCREMENT  NOT NULL COMMENT '部门id',
    name       VARCHAR(64)            NOT NULL COMMENT '部门名称',
    code       VARCHAR(32)            NOT NULL COMMENT '部门代码',
    status     TINYINT  DEFAULT 1     NOT NULL COMMENT '部门状态',
    created_at datetime DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '更新时间',
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
) COMMENT ='部门表';

CREATE TABLE sys_ticket
(
    id            BIGINT AUTO_INCREMENT         NOT NULL,
    ticket_no     VARCHAR(32)                   NOT NULL,
    title         VARCHAR(100)                  NOT NULL,
    `description` LONGTEXT                      NOT NULL,
    category      VARCHAR(32)                   NOT NULL,
    priority      VARCHAR(16) DEFAULT 'MEDIUM'  NOT NULL,
    status        VARCHAR(20) DEFAULT 'PENDING' NOT NULL,
    creator_id    BIGINT                        NOT NULL,
    assignee_id   BIGINT                        NULL,
    department_id BIGINT                        NULL,
    asset_id      BIGINT                        NULL,
    created_at    datetime DEFAULT CURRENT_TIMESTAMP    NOT NULL,
    accepted_at   datetime                      NULL,
    resolved_at   datetime                      NULL,
    closed_at     datetime                      NULL,
    deadline_at   datetime                      NULL,
    updated_at    datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
)COMMENT ='工单表';

CREATE TABLE sys_ticket_history
(
    id          BIGINT AUTO_INCREMENT  NOT NULL,
    ticket_id   BIGINT                 NOT NULL,
    operator_id BIGINT                 NOT NULL,
    action      VARCHAR(32)            NOT NULL,
    from_status VARCHAR(32)            NULL,
    to_status   VARCHAR(32)            NOT NULL,
    remark      VARCHAR(50)            NULL,
    created_at  datetime DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
)COMMENT ='工单历史表';

CREATE TABLE sys_user
(
    id            BIGINT AUTO_INCREMENT  NOT NULL,
    username      VARCHAR(50)            NOT NULL,
    password      VARCHAR(100)           NOT NULL,
    real_name     VARCHAR(50)            NOT NULL,
    email         VARCHAR(100)           NULL,
    phone         VARCHAR(20)            NULL,
    department_id BIGINT                 NULL,
    status        TINYINT  DEFAULT 1     NOT NULL,
    created_at    datetime DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at    datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
)COMMENT ='用户表';

ALTER TABLE sys_ticket
    ADD CONSTRAINT ticket_no UNIQUE (ticket_no);

ALTER TABLE sys_department
    ADD CONSTRAINT uk_sys_department_code UNIQUE (code);

ALTER TABLE sys_user
    ADD CONSTRAINT username UNIQUE (username);

CREATE INDEX idx_ticket_creator_id ON sys_ticket (creator_id);

CREATE INDEX idx_ticket_history_ticket_created_at ON sys_ticket_history (ticket_id, created_at);