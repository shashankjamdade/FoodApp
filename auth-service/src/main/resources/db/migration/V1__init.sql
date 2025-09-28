create extension if not exists pgcrypto;
create schema if not exists auth;
create table if not exists auth.users(
  id uuid primary key default gen_random_uuid(),
  email text unique not null,
  password_hash text not null,
  role text not null check (role in ('CUSTOMER','RESTAURANT','DELIVERY')),
  created_at timestamptz default now()
);
