package com.ravn.treef

import scala.collection.immutable.Map

/**
 * Created by remim on 11/04/14.
 */

//package object treef {
  class Feature(val i : Int, val label : String)
  class DataPoint(val c : Int, val qid : Int,
                  val values : Map[Feature, Double],
                  val comment : String)
//  type DataPoint = Map[Feature, Double]
//}
