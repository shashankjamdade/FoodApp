-- User Profiles Table
CREATE TABLE user_profiles (
    user_id UUID PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL UNIQUE,
    user_role VARCHAR(50) NOT NULL DEFAULT 'CUSTOMER',
    profile_picture VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Email Verifications Table
CREATE TABLE email_verifications (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    otp VARCHAR(6) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    verified BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL
);

-- Addresses Table
CREATE TABLE addresses (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    address_line1 VARCHAR(255) NOT NULL,
    address_line2 VARCHAR(255),
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    country VARCHAR(100) NOT NULL,
    landmark VARCHAR(255),
    address_type VARCHAR(50) NOT NULL,
    is_default BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user_profiles(user_id)
);

-- Favorites Table
CREATE TABLE favorites (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    restaurant_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user_profiles(user_id),
    UNIQUE (user_id, restaurant_id)
);

-- Wallets Table
CREATE TABLE wallets (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE,
    balance DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user_profiles(user_id)
);

-- Wallet Transactions Table
CREATE TABLE wallet_transactions (
    id UUID PRIMARY KEY,
    wallet_id UUID NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    type VARCHAR(20) NOT NULL,
    description VARCHAR(255) NOT NULL,
    reference_id VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (wallet_id) REFERENCES wallets(id)
);
