lazy val root = (project in file("."))
  .settings(
    name         := "labyrinth",
    organization := "Matruss",
    scalaVersion := "2.12.4",
    version      := "0.0.1-SNAPSHOT"
  )
libraryDependencies ++= Seq(
  "com.github.scopt" %% "scopt" % "3.7.0",
  "com.typesafe" % "config" % "1.3.1",
  "org.apache.httpcomponents" % "httpclient" % "4.5.3",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.6"
)
