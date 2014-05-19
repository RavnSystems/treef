package com.ravn.treef

import java.io.File
import java.nio.file.{Files, Path}

/**
 * Created by remim on 19/05/14.
 */
object Output {

  def trunc(d : Double) : Double = {
    (math floor d * 100) / 100
  }

  def normalize(result: Map[Feature, Double]) : Map[Feature, Double] = {
    val m = result.values.reduceLeft(_ max _)
    result.map(e => (e._1, trunc(e._2*100/m)))
  }

  def print(result : Map[Feature, Double]) = {
    val printer = (f: Feature, i: Double) => println(f.label + " " + i)
    normalize(result).toList.sortBy(_._2).reverse.foreach(printer.tupled)
  }

  def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) {
    val p = new java.io.PrintWriter(f)
    try { op(p) } finally { p.close() }
  }

  def writeResultSet(destination : Path, results : List[(String, Map[Feature, Double])], features : List[Feature]){
    val delimiter = ","

    // header
    val header = "Title" + delimiter + features.map(_.label).mkString(delimiter)

    // models feature importance
    val lines = results.map({case (title, result) =>
      title + delimiter + features.map(
        f => normalize(result)(f).toString).mkString(delimiter)})

    printToFile(destination.toFile)(p => {
      (header :: lines).foreach(p.println)
    })

  }

}
