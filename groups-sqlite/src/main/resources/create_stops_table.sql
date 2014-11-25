CREATE TABLE IF NOT EXISTS stops (
  _id INTEGER PRIMARY KEY AUTOINCREMENT,
  group_id int,
  stop_id int,
  stop_name VARCHAR,
  FOREIGN KEY(group_id) REFERENCES groups(_id),
  UNIQUE (group_id, stop_id) ON CONFLICT REPLACE
)