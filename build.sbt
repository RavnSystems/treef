name := "Treef"

version := "1.0"

scalaVersion := "2.11.0"


//resolvers += "Local Maven Repository" at
//  "file://"+Path.userHome.absolutePath+"/.m2/repository"


resolvers +=
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

resolvers +=
  "Typesafe" at "http://repo.typesafe.com/typesafe/releases/"

//libraryDependencies += "ciir.umass.edu" % "ranklib" % "2.3"

//libraryDependencies += "org.scala-lang" % "scala-library" % "2.10.1"

// libraryDependencies += "com.typesafe.play" % "play-json" % "2.2.2"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.1.5" /*% "test"*/

//libraryDependencies += "com.google.code.gson" % "gson" % "2.2.4"

//libraryDependencies +=
//  "net.databinder.dispatch" %% "dispatch-core" % "0.11.0"

mainClass := Some("com.ravn.treef.Hi")
