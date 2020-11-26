name := "tapir-multiple-errors-same-code"

version := "0.1"

scalaVersion := "2.13.3"

val akkaVersion = "2.6.13"
val akkaHttpVersion = "10.2.4"
val tapirVersion = "0.17.14"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-akka-http-server" % tapirVersion exclude ("com.typesafe.akka", "akka-stream_2.13"),
  "com.softwaremill.sttp.tapir" %% "tapir-json-play" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-akka-http" % tapirVersion excludeAll "com.typesafe.akka"
)
