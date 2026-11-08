-- Migration: V2__create_users_table
-- Author: pepega
-- Description: Create users table for user_schema.
-- Here will be stored username, email and email hash, date when email will be verified,
-- password and date of creating and updating accounts with status

CREATE TABLE IF NOT EXISTS user_schema.users(
    id UUID PRIMARY KEY DEFAULT uuidv7(),
    username VARCHAR(50) NOT NULL,
    cipher_email BYTEA NOT NULL,
    hash_email BYTEA NOT NULL UNIQUE,
    email_verified_at timestamptz ,
    hash_password TEXT,

    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),

    status VARCHAR(25) NOT NULl DEFAULT 'ACTIVE'
    CHECK( status in ('ACTIVE' , 'BLOCKED'))

);

CREATE UNIQUE INDEX IF NOT EXISTS idx_user_username_lower
    ON user_schema.users (lower(username));