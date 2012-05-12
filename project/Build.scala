import sbt._
import Keys._

object PluginBuild extends Build {

  lazy val buildVersion  =  "1.0-SNAPSHOT"

  lazy val moduleName    = "sbt-doma-plugin"

  lazy val releases      = "C:/project/maven-repo/release"
  lazy val snapshot      = "C:/project/maven-repo/snapshots"
  lazy val repo          = if (buildVersion.endsWith("SNAPSHOT")) snapshot else releases

  lazy val root = Project(id = moduleName, base = file("."), settings = Project.defaultSettings).settings(
    sbtPlugin := true,
    version := buildVersion,
    publishTo := Some(Resolver.file("maven-repo", file(repo))),
    javacOptions ++= Seq("-encoding", "utf8", "-target", "1.6"),
    libraryDependencies += "commons-io" % "commons-io" % "2.3"
  )
}
