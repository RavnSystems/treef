name := "Treef"

version := "1.0"

scalaVersion := "2.11.0"

resolvers +=
  "Typesafe" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += 
  Resolver.url("sbt-repo", url("http://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns)

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.1.5" % "test"

//libraryDependencies +=
//  "net.databinder.dispatch" %% "dispatch-core" % "0.11.0"

mainClass := Some("com.ravn.treef.TreefApp")
