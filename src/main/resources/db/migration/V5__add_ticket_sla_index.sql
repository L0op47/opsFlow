CREATE INDEX idx_ticket_status_deadline_at
    ON sys_ticket (status, deadline_at);