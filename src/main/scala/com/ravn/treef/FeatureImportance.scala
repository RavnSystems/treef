package com.ravn.treef

import ciir.umass.edu.learning.RankerFactory
import ciir.umass.edu.learning.tree.LambdaMART
import com.ravn.{Feature, treef}

/**
 * Created by remim on 11/04/14.
 */
object FeatureImportance extends App {

  val TermRank      = new Feature(1, "Term rank")
  val EntityType    = new Feature(2, "Entity type")
  val TimeOfTheDay  = new Feature(3, "Part of the day")
  val DayOfTheWeek  = new Feature(4, "Day of the week")
  val MatchingWord  = new Feature(5, "Matching word position")

  val featuresList =
    List(TermRank, EntityType, DayOfTheWeek,
      TimeOfTheDay, MatchingWord)
  val featuresMap = featuresList.map(f => (f.i, f)).toMap

//  val example = "0 qid:1933 1:0 2:10 3:1 4:0 # ccded589-a205-49c9-9c5a-1249083dd3eb"
//  val datapoint = DataPointReader.convert(example, features)

  val dataPoints = DataPointReader.slurp("/tmp/rank_valid.txt", featuresMap)

  val ranker = new RankerFactory()
//    .loadRanker("/tmp/model_rank3")
//    .loadRanker("/home/remim/data/xp-sky/model_rank_actor.xml")
//    .loadRanker("/home/remim/data/xp-sky/model_rank_team.xml")
    .loadRanker("/home/remim/data/xp-sky/model_rank_all.xml")
    .asInstanceOf[LambdaMART]

  val ensemble = EnsembleBuilder.buildEnsemble(ranker.getEnsemble, featuresMap)

//  val r = ensemble.evaluate(datapoint)
//  dataPoints.foreach(dp => {if (ensemble.evaluate(dp)._1>0) println(dp.get(EnsembleBuilder.entityType))})

  def countFeatures(features : List[Feature], acc : scala.collection.mutable.Map[Feature, Int]) = {
    features.foreach(feature => {acc(feature) = acc(feature) + 1})
  }

  val acc = scala.collection.mutable.Map() ++ featuresList.map(f => (f, 0)).toMap
  dataPoints.foreach(dp => countFeatures(ensemble.evaluate(dp)._2, acc))

  val n : Double = dataPoints.size
  acc.foreach( e => println(e._1.label + " : " + (e._2 / n )))

}