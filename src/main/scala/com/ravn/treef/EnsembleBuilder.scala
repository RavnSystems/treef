package com.ravn.treef

import java.lang.reflect.Field
import ciir.umass.edu.learning.tree.{Ensemble => LibEnsemble, Split => LibSplit, RegressionTree}
import com.ravn.Feature

/**
 * Created by remim on 11/04/14.
 */
object EnsembleBuilder {

  val rootField : Field = classOf[RegressionTree].getDeclaredField("root")
  rootField.setAccessible(true)

  val thresholdField : Field = classOf[LibSplit].getDeclaredField("threshold")
  thresholdField.setAccessible(true)

  val featureField : Field = classOf[LibSplit].getDeclaredField("featureID")
  featureField.setAccessible(true)

  val avgLabelField : Field = classOf[LibSplit].getDeclaredField("avgLabel")
  avgLabelField.setAccessible(true)

  def buildTreeNode(split : LibSplit, features : Map[Int, Feature]) : TreeNode = {

    val featID = featureField.get(split).asInstanceOf[Int]

    if (featID >= 0) {
      val feature = features(featID)
      val threshold = thresholdField.get(split).asInstanceOf[Float]
      val left = buildTreeNode(split.getLeft, features)
      val right = buildTreeNode(split.getRight, features)
      new Split(left, right, threshold, feature)
    }
    else
      new Leaf(avgLabelField.get(split).asInstanceOf[Double])

  }

  def buildTree(t : RegressionTree, weight : Double, features : Map[Int, Feature]) : Tree = {
    val rootSplit = rootField.get(t).asInstanceOf[LibSplit]
    val root = buildTreeNode(rootSplit, features)
    new Tree(weight, root)
  }

  def buildEnsemble(ensemble : LibEnsemble, features : Map[Int, Feature]) : Ensemble = {

    val trees = (0 until ensemble.treeCount())
      .map(i => buildTree(ensemble.getTree(i), ensemble.getWeight(i), features))
    new Ensemble(trees)
  }
}
