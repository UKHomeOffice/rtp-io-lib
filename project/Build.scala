import sbt.Keys._
import sbt._
import sbtrelease.ReleasePlugin

object Build extends Build {
  val moduleName = "rtp-io-lib"

  val root = Project(id = moduleName, base = file(".")).enablePlugins(ReleasePlugin)
    .configs(IntegrationTest)
    .settings(Defaults.itSettings: _*)
    .settings(
      name := moduleName,
      organization := "uk.gov.homeoffice",
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
      ivyScala := ivyScala.value map {
        _.copy(overrideScalaVersion = true)
      },
      resolvers ++= Seq(
        "Artifactory Snapshot Realm" at "http://artifactory.registered-traveller.homeoffice.gov.uk/artifactory/libs-snapshot-local/",
        "Artifactory Release Realm" at "http://artifactory.registered-traveller.homeoffice.gov.uk/artifactory/libs-release-local/",
        "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
        "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
        "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/",
        "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
        "Kamon Repository" at "http://repo.kamon.io"
      )
    )
    .settings(libraryDependencies ++= {
      val `json4s-version` = "3.5.0"
      val `scalactic-version` = "3.0.1"
      val `rtp-test-lib-version` = "1.4.1"

      Seq(
        "commons-codec" % "commons-codec" % "1.10",
        "com.typesafe" % "config" % "1.3.1" withSources(),
        "org.json4s" %% "json4s-native" % `json4s-version` withSources(),
        "org.json4s" %% "json4s-jackson" % `json4s-version` withSources(),
        "org.json4s" %% "json4s-ext" % `json4s-version` withSources(),
        "org.json4s" %% "json4s-mongo" % `json4s-version` withSources(),
        "com.github.fge" % "json-schema-validator" % "2.2.6" withSources(),
        "org.scala-lang.modules" %% "scala-pickling" % "0.10.1" withSources(),
        "com.lihaoyi" %% "pprint" % "0.4.4",
        "com.github.nscala-time" %% "nscala-time" % "2.16.0" withSources(),
        "org.scalactic" %% "scalactic" % `scalactic-version` withSources(),
        "uk.gov.homeoffice" %% "rtp-test-lib" % `rtp-test-lib-version` withSources()
      ) ++ Seq(
        "uk.gov.homeoffice" %% "rtp-test-lib" % `rtp-test-lib-version` % Test classifier "tests" withSources()
      )
    })
}