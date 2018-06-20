import sbt._
import sbtrelease.ReleasePlugin

val moduleName = "rtp-io-lib"

val root = Project(id = moduleName, base = file("."))
  .enablePlugins(ReleasePlugin)
  .configs(IntegrationTest)
  .settings(Defaults.itSettings: _*)
  .settings(
    name := moduleName,
    organization := "uk.gov.homeoffice",
    scalaVersion := "2.12.6",
    crossScalaVersions := Seq("2.11.8", "2.12.6")
  )

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

resolvers ++= Seq(
  "Artifactory Snapshot Realm" at "http://artifactory.registered-traveller.homeoffice.gov.uk/artifactory/libs-snapshot-local/",
  "Artifactory Release Realm" at "http://artifactory.registered-traveller.homeoffice.gov.uk/artifactory/libs-release-local/",
  "Artifactory External Release Local Realm" at "http://artifactory.registered-traveller.homeoffice.gov.uk/artifactory/ext-release-local/"
)

val json4sVersion = "3.5.4"

libraryDependencies ++= Seq(
  "commons-codec" % "commons-codec" % "1.10",
  "com.typesafe" % "config" % "1.3.1",
  "org.json4s" %% "json4s-native" % json4sVersion,
  "org.json4s" %% "json4s-jackson" % json4sVersion,
  "org.json4s" %% "json4s-ext" % json4sVersion,
  "org.json4s" %% "json4s-mongo" % json4sVersion,
  "com.github.fge" % "json-schema-validator" % "2.2.6",
  "com.lihaoyi" %% "pprint" % "0.4.4",
  "com.github.nscala-time" %% "nscala-time" % "2.16.0",
  "org.scalactic" %% "scalactic" % "3.0.5",
  "uk.gov.homeoffice" %% "rtp-test-lib" % "1.4.4-SNAPSHOT"
)

publishTo := {
  val artifactory = "http://artifactory.registered-traveller.homeoffice.gov.uk/"

  if (isSnapshot.value)
    Some("snapshot" at artifactory + "artifactory/libs-snapshot-local")
  else
    Some("release"  at artifactory + "artifactory/libs-release-local")
}

// Enable publishing the jar produced by `test:package`
publishArtifact in (Test, packageBin) := true

// Enable publishing the test API jar
publishArtifact in (Test, packageDoc) := true

// Enable publishing the test sources jar
publishArtifact in (Test, packageSrc) := true

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case PathList(ps @ _*) if ps.last endsWith ".java" => MergeStrategy.discard
  case _ => MergeStrategy.first
}
