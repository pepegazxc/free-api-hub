-- Migration: V3__create_user_identities_table
-- Author: pepega
-- Description: Create user identities table for user_schema.
-- Here will be stored relation with users table, providers data for OAuth login logic and creation time

CREATE TABLE IF NOT EXISTS user_schema.user_identities(
    id UUID PRIMARY KEY DEFAULT uuidv7(),
    user_id UUID NOT NULL
        REFERENCES users_schema.users(id) ON DELETE CASCADE,

    provider_name VARCHAR(25) NOT NULL
        CHECK(provider_name IN ('GOOGLE', 'GITHUB')),
    provider_user_id VARCHAR(255) NOT NULL,

    created_at timestamptz NOT NULL DEFAULT now(),

    UNIQUE(provider_name, provider_user_id),
);

CREATE INDEX IF NOT EXISTS fk_provider_user_id
	ON user_schema.user_identities(user_id);