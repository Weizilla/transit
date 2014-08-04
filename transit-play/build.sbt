name := """transit-play"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "com.weizilla.transit" % "lib-transit" % "1.0.0-SNAPSHOT",
  "com.weizilla.transit" % "favorites-sqlite" % "1.0.0-SNAPSHOT"
)

resolvers += (
  "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository"
)

