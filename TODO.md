# Features
- filter by buses on stops
- filter by buses for stops in groups
- handle delayed predictions
- display alerts
- link to cta bus tracker mobile site
- remove stops from groups

# Bugs
- use clock offset from cta data source for more accurate predictions

# Enhancements
- not use temp db in play. set in config
- rename groups in ui
- better ui for predictions from multiple stops
- better api for controller with less logic in scala
- better navigation for play web ui

# Tech Debt
- find common logging framework
- break data objs between ones from xml and ones used in app
 - route is just id + name. stop is id and name. prediction contains a route and a stop obj
