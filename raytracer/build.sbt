ThisBuild / version := "0.1.1"
ThisBuild / scalaVersion := "3.0.2"

val zioVersion = "2.0.0-M2"

lazy val root = (project in file("."))
  .settings(
    name := "raytracer-zio",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % zioVersion,
      "dev.zio" %% "zio-test" % zioVersion % Test
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
