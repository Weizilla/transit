CREATE TABLE IF NOT EXISTS cache_routes (
  _id integer PRIMARY KEY AUTOINCREMENT,
  id varchar UNIQUE ON CONFLICT REPLACE,
  name varchar
)