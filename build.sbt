name := """dropbox-scala-play"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.4"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += "bintray" at "http://dl.bintray.com/shinsuke-abe/maven"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "com.github.Shinsuke-Abe" %% "dropbox4s" % "0.2.0",
  "org.scalaj" %% "scalaj-http" % "1.0.1"
)

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

