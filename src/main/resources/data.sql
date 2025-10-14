-- Create users table (if not using JPA auto ddl)
CREATE TABLE IF NOT EXISTS users (
                                     id SERIAL PRIMARY KEY,
                                     username VARCHAR(100) UNIQUE NOT NULL,
                                     email VARCHAR(200) UNIQUE NOT NULL,
                                     password VARCHAR(255) NOT NULL,
                                     role VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS shift_requests (
                                              id SERIAL PRIMARY KEY,
                                              employee_id INT REFERENCES users(id),
                                              shift VARCHAR(50),
                                              status VARCHAR(50) DEFAULT 'PENDING',
                                              admin_comment TEXT
);

-- Insert roles and users
-- Password is bcrypt for 'password123' (example hash)
INSERT INTO users (username, email, password, role)
VALUES
    ('admin','admin@example.com','$2a$12$lqzX7gksfrlsvXHTPJ3FRejUICmCYFRSOfFnG5VgF3Dl0/5grxpPO','ROLE_ADMIN')
ON CONFLICT (username) DO NOTHING;

INSERT INTO users (username, email, password, role)
VALUES
    ('employee','employee@example.com','$2a$12$lqzX7gksfrlsvXHTPJ3FRejUICmCYFRSOfFnG5VgF3Dl0/5grxpPO','ROLE_EMPLOYEE')
ON CONFLICT (username) DO NOTHING;


