name := """intake24-short-urls"""
organization := "uk.ac.ncl.openlab.intake24"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.12.7"

maintainer := "Ivan Poliakov <ivan.poliakov@ncl.ac.uk>"

libraryDependencies ++= Seq(
  jdbc,
  guice,
  cacheApi,
  ehcache,
  ws,
  "commons-net" % "commons-net" % "3.6",
  "com.opencsv" % "opencsv" % "4.2",
  "com.amazonaws" % "aws-java-sdk-s3" % "1.11.383",
  "com.typesafe.play" %% "play-mailer" % "6.0.1",
  "com.typesafe.play" %% "play-mailer-guice" % "6.0.1",
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
)
