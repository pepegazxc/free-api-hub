-- Migration: V1__create_users_table
-- Author: pepega
-- Description: Create user table to store the required user information (username, email, password)

CREATE TABLE users(
    id BIGSERIAL PRIMARY KEY,
    username Varchar(50) UNIQUE NOT NULL,
    cipher_email VARCHAR(255) UNIQUE NOT NULL,
    hash_password TEXT NOT NULL
)
