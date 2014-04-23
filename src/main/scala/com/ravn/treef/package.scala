package com.ravn

import scala.collection.immutable.Map

/**
 * Created by remim on 11/04/14.
 */

class Feature(val i : Int, val label : String){}

package object tree {
  type DataPoint = Map[Feature, Double]
}
