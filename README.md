# FoodApp — Kotlin + Java 23 (Gateway, Auth, User, Common Libs)

## Run
```bash
mvn -q -DskipTests package
docker compose up -d --build

# Signup
curl -s -XPOST http://localhost:8081/auth/signup -H 'Content-Type: application/json'   -d '{"email":"u1@test.com","password":"pass","role":"CUSTOMER"}'

# Login (get token)
TOKEN=$(curl -s -XPOST http://localhost:8081/auth/login -H 'Content-Type: application/json'   -d '{"email":"u1@test.com","password":"pass"}' | jq -r .accessToken)

# Me via gateway
curl -s -H "Authorization: Bearer $TOKEN" http://localhost:8080/users/me

# Upsert profile
curl -s -XPOST -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json"   -d '{"fullName":"Shashank","phone":"9999999999","walletBalance":100.0}'   http://localhost:8080/users/profile
```

## Supabase
Replace Postgres envs on services:
- `PG_JDBC=jdbc:postgresql://<supabase-host>:5432/<db>`
- `PG_USER=<user>`
- `PG_PASSWORD=<password>`
