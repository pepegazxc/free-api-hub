-- Migration: V3__create_user_identities_table
-- Author: pepega
-- Description: Create user identities table for user_schema.
-- Here will be stored relation with users table, providers data for OAuth login logic and creation time

CREATE TABLE IF NOT EXIST user_chema.user_identities(
    id UUID PRIMARY KEY uuidv7(),
    user_id UUID NOT NULL
        REFERENCE users_schema.users(id) ON DELETE CASCADE,

    provider_name VARCHAR(25) NOT NULL
        CHECK(provider_name IN ('GOOGlE', 'GITHUB')),
    provider_user_id VARCHAR(255) NOT NULL,

    created_at TIMESTAMPZ NOT NULL DEFAULT now(),

    UNIQUE(provider, provider_user_id),
    UNIQUE (user_id, provider)
);

CREATE INDEX IF NOT EXIST fk_provider_user_id
	ON user_schema.user_idenities(user_id);