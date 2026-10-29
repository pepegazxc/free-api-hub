-- Migration: V2__add_new_column_to_the_users_table
-- Author: pepega
-- Description: Add hash_email column to store unique email hash for searching user email

ALTER TABLE users
ADD COLUMN hash_email VARCHAR(255) UNIQUE NOT NULL