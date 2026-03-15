# Database Migration Workflow

This project uses Flyway Community, which is forward-only by default.

## Folders
- Forward migrations (auto-run by Flyway): `src/main/resources/db/migration`
- Manual rollback companions (never auto-run): `src/main/resources/db/rollback`

## Naming Convention
- Forward: `V{version}__{description}.sql`
- Rollback companion: `U{version}__{description}.sql`

Example:
- `V2__add_users_status.sql`
- `U2__remove_users_status.sql`

## How To Add a Change
1. Add a new forward migration in `db/migration`.
2. Add a matching rollback companion in `db/rollback`.
3. Start the app (or run Flyway) to apply the new `V` migration.

## How To Roll Back (Manual)
Flyway will not auto-run `U` files. Execute them manually with `psql`.

Example:
```bash
psql -h localhost -U "$DB_USERNAME" -d employee_weather -f src/main/resources/db/rollback/U1__drop_users_table.sql
```

## Rules
- Never edit an already applied `V` migration.
- Always create a new `V` migration for schema changes.
- Keep `U` files for operational rollback playbooks only.
