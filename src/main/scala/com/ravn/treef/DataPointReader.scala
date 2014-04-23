package com.ravn.treef

import com.ravn.tree.DataPoint
import com.ravn.Feature
;

/**
 * Created by remim on 11/04/14.
 */
object DataPointReader {

  def slurp(filePath : String, features : Map[Int, Feature]) : List[DataPoint] = {
    io.Source.fromFile(filePath).getLines().toList
      .map(line => convert(line, features))
  }

  def convert(dataPointS : String, features : Map[Int, Feature]) : DataPoint = {

    val rawFeatures : Array[Array[String]] =
      dataPointS.split(" ").slice(2,7).map(s => s.split(":"))

    val featArray : Array[(Feature, Double)] =
      rawFeatures.map(a => (features(a(0).toInt), a(1).toDouble))

    featArray.toMap
  }

}