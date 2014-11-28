import play.PlayScala

name := """transit-play"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "com.weizilla.transit" % "lib-transit" % "1.0.0-SNAPSHOT",
  "com.weizilla.transit" % "cache-sqlite" % "1.0.0-SNAPSHOT",
  "com.weizilla.transit" % "favorites-sqlite" % "1.0.0-SNAPSHOT",
  "com.weizilla.transit" % "groups-sqlite" % "1.0.0-SNAPSHOT"
)

resolvers += (
  "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository"
)

