import sbt._
import Keys._

import org.apache.commons.io.FileUtils

object SbtDomaPlugin extends Plugin {
  val aptGeneratedDirectory = ".apt_generated"
  val domaResourceDirectory = SettingKey[File]("doma-resource-directory")
  val domaOutputDirectory = SettingKey[File]("doma-output-directory")
  val domaResourceCopy = TaskKey[Unit]("doma-resource-copy", "Copy Doma Resource")
  val domaResourceClean = TaskKey[Unit]("doma-resource-clean", "Clean Doma Resource")

  override val settings = Seq(
    domaResourceDirectory <<= (resourceDirectory in Compile) / "META-INF",
    domaOutputDirectory <<= crossTarget / "classes",
    domaResourceCopy <<= domaResourceCopyTask,
    domaResourceClean <<= domaResourceCleanTask,
    javacOptions ++= Seq("-s", aptGeneratedDirectory),
    sourceDirectories in Compile += file(aptGeneratedDirectory),
    unmanagedSourceDirectories in Compile += file(aptGeneratedDirectory),
    resolvers += "The Seasar Foundation Maven2 Repository" at "http://maven.seasar.org/maven2",
    compile <<= (compile in Compile) dependsOn domaResourceCopy,
    clean <<= clean dependsOn domaResourceClean
  )

  def domaResourceCopyTask = (domaResourceDirectory, domaOutputDirectory, streams) map {
    (from, to, s) => {
      IO.createDirectory(file(aptGeneratedDirectory))

      s.log.debug("Creating " + aptGeneratedDirectory + " directory ... done.")

      FileUtils copyDirectoryToDirectory(from, to)

      s.log.debug("Copying sql files ... done.")
    }
  }

  def domaResourceCleanTask = (domaOutputDirectory, streams) map {
    (to, s) => {
      FileUtils deleteDirectory file(aptGeneratedDirectory)

      s.log.debug("Deleting " + aptGeneratedDirectory + " directory ... done.")

      FileUtils deleteDirectory (to / "META-INF")

      s.log.debug("Deleting sql files ... done.")
    }
  }
}