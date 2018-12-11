enablePlugins(DockerComposePlugin)

name := "MicroserviceCodingChallenge"

version := "1.0"

lazy val `microservicecodingchallenge` =
  (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

scalaVersion := "2.12.2"
val circeVersion = "0.9.3"

libraryDependencies ++= Seq(
  jdbc,
  ehcache,
  ws,
  specs2 % Test,
  guice,
  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "com.typesafe.akka" %% "akka-testkit" % "2.5.13" % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
)

unmanagedResourceDirectories in Test <+= baseDirectory(
  _ / "target/web/public/test")

//dockerImageCreationTask := (publishLocal in Docker).value
