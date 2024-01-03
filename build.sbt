import sbt.{Credentials, Path, _}

val moduleName = "rtp-io-lib"

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

val root = Project(id = moduleName, base = file("."))
  .enablePlugins(GitVersioning)
  .settings(
    name := moduleName,
    organization := "uk.gov.homeoffice",
    scalaVersion := "2.13.12",
    crossScalaVersions := Seq("2.12.16", "2.13.12"),
    libraryDependencies ++= { Seq(
        "ch.qos.logback" % "logback-classic" % "1.4.14",
        "commons-codec" % "commons-codec" % "1.16.0",
        "com.typesafe" % "config" % "1.4.3",
        "org.json4s" %% "json4s-native" % "3.6.12",
        "org.json4s" %% "json4s-jackson" % "3.6.12",
        "org.json4s" %% "json4s-ext" % "3.6.12",
        "org.json4s" %% "json4s-mongo" % "3.6.12",
        "com.github.fge" % "json-schema-validator" % "2.2.6",
        "com.lihaoyi" %% "pprint" % "0.8.1",
        "com.github.nscala-time" %% "nscala-time" % "2.32.0",
        "uk.gov.homeoffice" %% "rtp-test-lib" % "1.6.22-gacd233d"
      ) ++ {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 12)) => Seq(
          "org.json4s" %% "json4s-native" % "3.6.12",
          "org.json4s" %% "json4s-jackson" % "3.6.12",
          "org.json4s" %% "json4s-ext" % "3.6.12",
          "org.json4s" %% "json4s-mongo" % "3.6.12",
        )
        case _ => Seq(
          "org.json4s" %% "json4s-native" % "4.0.7",
          "org.json4s" %% "json4s-jackson" % "4.0.7",
          "org.json4s" %% "json4s-ext" % "4.0.7",
          "org.json4s" %% "json4s-mongo" % "4.0.7",
        )
      }}
    }
  )

resolvers ++= Seq(
  "Artifactory Snapshot Realm" at "https://artifactory.digital.homeoffice.gov.uk/artifactory/libs-snapshot-local/",
  "Artifactory Release Realm" at "https://artifactory.digital.homeoffice.gov.uk/artifactory/libs-release-local/",
  "Typesafe Repository" at "https://repo.typesafe.com/typesafe/releases/",
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/",
  "Kamon Repository" at "https://repo.kamon.io"
)

publishTo := {
  val artifactory = sys.env.get("ARTIFACTORY_SERVER").getOrElse("https://artifactory.digital.homeoffice.gov.uk/")
  Some("release" at artifactory + "artifactory/libs-release-local")
}

publishArtifact in(Test, packageBin) := true
publishArtifact in(Test, packageDoc) := true
publishArtifact in(Test, packageSrc) := true

git.useGitDescribe := true
git.gitDescribePatterns := Seq("v*.*")
git.gitTagToVersionNumber := { tag :String =>

val branchTag = if (git.gitCurrentBranch.value == "master") "" else "-" + git.gitCurrentBranch.value
val uncommit = if (git.gitUncommittedChanges.value) "-U" else ""

tag match {
  case v if v.matches("v\\d+.\\d+") => Some(s"$v.0${uncommit}".drop(1))
  case v if v.matches("v\\d+.\\d+-.*") => Some(s"${v.replaceFirst("-",".")}${branchTag}${uncommit}".drop(1))
  case _ => None
}}
