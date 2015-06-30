import sbt.Keys._
import sbt._

object Build extends Build {
  val moduleName = "rtp-io-lib"

  lazy val root = Project(id = moduleName, base = file("."))
    .configs(IntegrationTest)
    .settings(Defaults.itSettings: _*)
    .settings(
      name := moduleName,
      organization := "uk.gov.homeoffice",
      version := "1.0-SNAPSHOT",
      scalaVersion := "2.11.7",
      scalacOptions ++= Seq(
        "-feature",
        "-language:implicitConversions",
        "-language:higherKinds",
        "-language:existentials",
        "-language:reflectiveCalls",
        "-language:postfixOps",
        "-Yrangepos",
        "-Yrepl-sync"),
      resolvers ++= Seq(
        "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
        "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
        "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
        "Kamon Repository" at "http://repo.kamon.io"),
      libraryDependencies ++= Seq(
        "org.slf4j" % "slf4j-api" % "1.7.12" withSources(),
        "com.typesafe" % "config" % "1.3.0" withSources(),
        "org.json4s" %% "json4s-native" % "3.2.11" withSources(),
        "org.json4s" %% "json4s-ext" % "3.2.11" withSources(),
        "com.github.fge" % "json-schema-validator" % "2.2.6" withSources(),
        "org.scalactic" %% "scalactic" % "2.2.4" withSources()),
      libraryDependencies ++= Seq(
        "org.specs2" %% "specs2-core" % "3.6.2" % Test withSources(),
        "org.specs2" %% "specs2-mock" % "3.6.2" % Test withSources(),
        "org.specs2" %% "specs2-matcher-extra" % "3.6.2" % Test withSources()))
}