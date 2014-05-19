package com.ravn.treef.test

import java.io.FileNotFoundException
import java.nio.file.Paths
import com.ravn.treef._
import ciir.umass.edu.learning.{DenseDataPoint, RankerFactory}
import ciir.umass.edu.learning.tree.LambdaMART
import org.scalatest._

/**
 * Created by remim on 06/05/14.
 */
class TreefTest extends UnitSpec {

  val features5 = (1 to 5).map(TreefApp.intToFeat).toMap
  val features46 = (1 to 46).map(TreefApp.intToFeat).toMap
  
  val datasetPath = getClass.getResource("/dataset").getPath
  val modelPath = getClass.getResource("/test-model.xml").getPath
  val headerPath = getClass.getResource("/header.properties").getPath

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
    val ranker = new RankerFactory()
      .loadRanker(modelPath)
      .asInstanceOf[LambdaMART]

    EnsembleBuilder.buildEnsemble(ranker.getEnsemble, features46)
  }

  "An existing model" should "be readable" in {
    loadModel
  }

  def loadDataset = {
    DataPointReader.slurp(Paths.get(datasetPath), features46)
  }

  "A dataset" should "be readable" in {
    loadDataset
  }

  "A dataset" should "be interpreted through a model" in {
    val model = loadModel
    val dataset = loadDataset

    val gi = new GiniImportance(features46.values.toList)
    gi.perform(model, dataset)
  }

  "some datapoints" should "have the same score with the two libraries" in {
    val dataset = loadDataset
    val datasetS = io.Source
      .fromFile(datasetPath)
      .getLines().map(new DenseDataPoint(_)).toList

    val ranker = new RankerFactory()
      .loadRanker(modelPath)
      .asInstanceOf[LambdaMART]

    val ensemble = EnsembleBuilder.buildEnsemble(ranker.getEnsemble, features46)

    val ranklib00 = ranker.getEnsemble.getTree(0).eval(datasetS(0)) * ranker.getEnsemble.getWeight(0)
    val treef00 = ensemble.trees(0).compute(dataset(0))
    assert(ranklib00 == treef00)

    val ranklib01 = ranker.getEnsemble.getTree(0).eval(datasetS(1)) * ranker.getEnsemble.getWeight(1)
    val treef01 = ensemble.trees(0).compute(dataset(1))
    assert(ranklib01 == treef01)

    // not strictly equal because of different precision
    assert((ranker.eval(datasetS(0)) - ensemble.compute(dataset(0))).abs < 0.0001)
  }

  "the app" should "throw RuntimeException when passing wrong argument" in {
    a [RuntimeException] should be thrownBy {
      TreefApp.main("blabla".split(" "))
    }
    a [FileNotFoundException] should be thrownBy {
      TreefApp.main("-m model -d dataset".split(" "))
    }
    a [RuntimeException] should be thrownBy {
      TreefApp.main(Array("-m", modelPath, "-d", datasetPath, "-o"))
    }
  }

  "the app" should "run properly on test files" in {
    println("-m " + modelPath + " -d " + "/home/remim/data/MQ2008/Fold1/train.txt")
    TreefApp.main(Array("-m", modelPath, "-d", "/home/remim/data/MQ2008/Fold1/train.txt"))
  }

}
