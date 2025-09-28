create extension if not exists pgcrypto;
create schema if not exists core;
create table if not exists core.user_profile(
  user_id uuid primary key,
  full_name text,
  phone text,
  wallet_balance numeric(12,2) default 0
);
