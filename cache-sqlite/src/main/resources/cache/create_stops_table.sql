CREATE TABLE IF NOT EXISTS cache_stops (
  _id integer PRIMARY KEY AUTOINCREMENT,
  id integer UNIQUE ON CONFLICT REPLACE,
  name varchar
)