/*
This file is part of Intake24.

Copyright 2015, 2016 Newcastle University.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.7")

addSbtPlugin("com.lihaoyi" % "scalatex-sbt-plugin" % "0.3.11")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.9.0")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.6")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.2")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.21")

libraryDependencies ++= Seq(
  "org.vafer" % "jdeb" % "1.5" artifacts (Artifact("jdeb", "jar", "jar")),
  "org.scala-js" %% "scalajs-env-selenium" % "0.2.0"
)
