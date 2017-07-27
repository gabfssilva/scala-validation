name := "core"
version := "0.0.5"
organization := "org.scala.validation"
scalaVersion := "2.12.2"

resolvers += Resolver.jcenterRepo

bintrayOrganization := Some("scala-validation")
bintrayRepository := "releases"

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"