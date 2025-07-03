import sbt.{Credentials, Path, _}

val moduleName = "rtp-io-lib"

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

val root = Project(id = moduleName, base = file("."))
  .enablePlugins(GitVersioning)
  .settings(
    name := moduleName,
    organization := "uk.gov.homeoffice",
    scalaVersion := "3.3.5",
    crossScalaVersions := Seq("3.3.5", "2.13.16"),
    libraryDependencies ++= Seq(
        "ch.qos.logback" % "logback-classic" % "1.5.18",
        "commons-codec" % "commons-codec" % "1.18.0",
        "com.typesafe" % "config" % "1.4.3",
        // json-schema-validator depends on guava but isn't upto date,
        // so we include it, to ensure we are on a secure version, even
        // though we don't directly depend on it.
        "com.google.guava" % "guava" % "33.4.8-android",
        "com.github.java-json-tools" % "json-schema-validator" % "2.2.14",
        "uk.gov.homeoffice" %% "rtp-test-lib" % "1.6.37-g813af7a",
        "org.json4s" %% "json4s-native" % "4.1.0-M8",
        "org.json4s" %% "json4s-jackson" % "4.1.0-M8",
        "org.json4s" %% "json4s-ext" % "4.1.0-M8"
      )
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
