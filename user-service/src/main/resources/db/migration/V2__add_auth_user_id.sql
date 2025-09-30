ALTER TABLE user_profiles
ADD COLUMN auth_user_id INT NOT NULL,
ADD COLUMN is_default BOOLEAN NOT NULL DEFAULT false;

