import sbt.Keys._
import sbt._

object Build extends Build {
  val moduleName = "rtp-io-lib"

  lazy val io = Project(id = moduleName, base = file("."))
    .configs(IntegrationTest)
    .settings(Defaults.itSettings: _*)
    .settings(
      name := moduleName,
      organization := "uk.gov.homeoffice",
      version := "1.2.0-SNAPSHOT",
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
        "Artifactory Snapshot Realm" at "http://artifactory.registered-traveller.homeoffice.gov.uk/artifactory/libs-snapshot-local/",
        "Artifactory Release Realm" at "http://artifactory.registered-traveller.homeoffice.gov.uk/artifactory/libs-release-local/",
        "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
        "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
        "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
        "Kamon Repository" at "http://repo.kamon.io"),
      libraryDependencies ++= Seq(
        "com.typesafe" % "config" % "1.3.0" withSources(),
        "org.json4s" %% "json4s-native" % "3.2.11" withSources(),
        "org.json4s" %% "json4s-jackson" % "3.2.11" withSources(),
        "org.json4s" %% "json4s-ext" % "3.2.11" withSources(),
        "org.json4s" %% "json4s-mongo" % "3.2.11" withSources(),
        "com.github.fge" % "json-schema-validator" % "2.2.6" withSources(),
        "org.scala-lang.modules" %% "scala-pickling" % "0.10.1" withSources(),
        "com.lihaoyi" %% "pprint" % "0.3.4",
        "org.clapper" %% "grizzled-slf4j" % "1.0.2",
        "ch.qos.logback" % "logback-core" % "1.1.3",
        "ch.qos.logback" % "logback-classic" % "1.1.3"),
      libraryDependencies ++= Seq()
    )

  val testPath = "../rtp-test-lib"

  def existsLocallyAndNotOnJenkins(filePath: String) = file(filePath).exists && !file(filePath + "/nextBuildNumber").exists()

  val root = if ((existsLocallyAndNotOnJenkins(testPath) || existsLocallyAndNotOnJenkins("../" + testPath)) && sys.props.get("jenkins").isEmpty) {
    println("================")
    println("Build locally io")
    println("================")

    val testLib = ProjectRef(file(testPath), "rtp-test-lib")
    io.dependsOn(testLib % "test->test;compile->compile")

  } else {
    println("===================")
    println("Build on Jenkins io")
    println("===================")

    io.settings(
      libraryDependencies ++= Seq(
        "uk.gov.homeoffice" %% "rtp-test-lib" % "1.0" withSources(),
        "uk.gov.homeoffice" %% "rtp-test-lib" % "1.0" % Test classifier "tests" withSources()
      ))
  }
}