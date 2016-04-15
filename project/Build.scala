import sbt.Keys._
import sbt._

object Build extends Build {
  val moduleName = "rtp-io-lib"

  val root = Project(id = moduleName, base = file("."))
    .configs(IntegrationTest)
    .settings(Defaults.itSettings: _*)
    .settings(
      name := moduleName,
      organization := "uk.gov.homeoffice",
      version := "1.7.5-SNAPSHOT",
      scalaVersion := "2.11.8",
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
        "Artifactory Snapshot Realm" at "http://artifactory.registered-traveller.homeoffice.gov.uk/artifactory/libs-snapshot-local/",
        "Artifactory Release Realm" at "http://artifactory.registered-traveller.homeoffice.gov.uk/artifactory/libs-release-local/",
        "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
        "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
        "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
        "Kamon Repository" at "http://repo.kamon.io"
      )
    )
    .settings(libraryDependencies ++= {
      val `json4s-version` = "3.2.11"
      val `logback-version` = "1.1.6"
      val `rtp-test-lib-version` = "1.2.1"

      Seq(
        "com.typesafe" % "config" % "1.3.0" withSources(),
        "org.json4s" %% "json4s-native" % `json4s-version` withSources(),
        "org.json4s" %% "json4s-jackson" % `json4s-version` withSources(),
        "org.json4s" %% "json4s-ext" % `json4s-version` withSources(),
        "org.json4s" %% "json4s-mongo" % `json4s-version` withSources(),
        "com.github.fge" % "json-schema-validator" % "2.2.6" withSources(),
        "org.scala-lang.modules" %% "scala-pickling" % "0.10.1" withSources(),
        "com.lihaoyi" %% "pprint" % "0.3.4",
        "org.clapper" %% "grizzled-slf4j" % "1.0.2",
        "ch.qos.logback" % "logback-core" % `logback-version`,
        "ch.qos.logback" % "logback-classic" % `logback-version`,
        "uk.gov.homeoffice" %% "rtp-test-lib" % `rtp-test-lib-version` withSources()
      ) ++ Seq(
        "uk.gov.homeoffice" %% "rtp-test-lib" % `rtp-test-lib-version` % Test classifier "tests" withSources()
      )
    })
}