package com.ravn.treef.test

import scala.io.Source
import com.ravn.treef._
import ciir.umass.edu.learning.RankerFactory
import ciir.umass.edu.learning.tree.LambdaMART

/**
 * Created by remim on 06/05/14.
 */
class TreefTest extends UnitSpec {
  def intToFeat : (Int) => (Int, Feature) = (i:Int) => (i, new Feature(i, "Feature " + i))
  val features5 = (1 to 5).map(intToFeat).toMap
  val features46 = (1 to 46).map(intToFeat).toMap

  "A simple DataPoint" should "be read properly" in {
    val rawdp = "0 qid:1933 1:1000 2:10 3:1 4:1 5:0 # test"
    val dp = DataPointReader.convert(rawdp, features5)
    assert(dp.c == 0)
    assert(dp.values.size == 5)
    assert(dp.values(features5(1)) == 1000)
    assert(dp.qid == 1933)
    assert(dp.comment == " test")
  }

  "A DataPoint without all features" should "fail" in {
    intercept[IllegalArgumentException] {
      val rawdp = "0 qid:1933 1:1000 2:10 3:1 4:1 # test"
      DataPointReader.convert(rawdp, features5)
    }
  }

  "A DataPoint without comment" should "be read properly" in {
    val rawdp = "0 qid:1933 1:1000 2:10 3:1 4:1 5:0 6:42 #"
    DataPointReader.convert(rawdp, features5)
  }

  def loadModel = {
    val model = getClass.getResource("/test-model.xml").getPath
    val ranker = new RankerFactory()
      .loadRanker(model)
      .asInstanceOf[LambdaMART]

    EnsembleBuilder.buildEnsemble(ranker.getEnsemble, features46)
  }

  "An existing model" should "be readable" in {
    loadModel
  }

  def loadDataset = {
    val dataset = getClass.getResource("/dataset").getPath
    DataPointReader.slurp(dataset, features46)
  }

  "A dataset" should "be readable" in {
    loadDataset
  }

  "A dataset" should "be interpreted through a model" in {
    val model = loadModel
    val dataset = loadDataset

    val gi = new GiniImportance(features46.values.toList)
    val results = gi.perform(model, dataset)
    gi.print(results)
  }

}
