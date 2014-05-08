package com.ravn.treef.test

import com.ravn.treef.{EnsembleBuilder, DataPointReader, Feature, DataPoint}
import ciir.umass.edu.learning.DenseDataPoint
import ciir.umass.edu.learning.RankerFactory
import ciir.umass.edu.learning.tree.LambdaMART
import java.util.Date
import java.util.concurrent.TimeUnit

/**
 * Created by remim on 08/05/14.
 */
object TreefPerf extends App {

  val test = new TreefTest()

  val model = getClass.getResource("/test-model.xml").getPath
  val ranker = new RankerFactory().loadRanker(model).asInstanceOf[LambdaMART]

  val features = test.features46
  val ensemble = EnsembleBuilder.buildEnsemble(ranker.getEnsemble, features)

  val path = "/home/remim/data/MQ2008/Fold"

  val files = (1 to 5).map(path + _).map(_ + "/train.txt")

  val dataset = files.map(DataPointReader.slurp(_, features)).flatten.toList
  val dataset2 = files.map(io.Source.fromFile(_).getLines().toList).flatten.map(new DenseDataPoint(_)).toList

  def elapsed(start : Date) : Long = {
    TimeUnit.MILLISECONDS.convert(new Date().getTime - start.getTime, TimeUnit.MILLISECONDS)
  }

//  println(dataset.size)
  val start = new Date()
  val result = dataset.map(ensemble.compute)//.sum
  println(elapsed(start))
//  println(result)

//  println(dataset2.size)
  val start2 = new Date()
  val result2 = dataset2.map(ranker.eval)//.sum
  println(elapsed(start2))
//  println(result2)
}
