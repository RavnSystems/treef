package com.ravn.treef

import com.ravn.tree.DataPoint
import com.ravn.Feature

/**
 * Created by remim on 11/04/14.
 */

sealed trait TreeNode {
  def evaluate(features: DataPoint): (Double, List[Feature])
}

class Ensemble(trees: Seq[Tree]) extends TreeNode {
  override def evaluate(features: DataPoint): (Double, List[Feature]) = {
    val results = trees.map(tree => tree.evaluate(features))
    val score = results.map(result => result._1).sum / trees.length
    val usedFeatures = results.map(result => result._2).flatten

    (score, usedFeatures.toList)
  }
}

class Tree(weight: Double, root : TreeNode) extends TreeNode {
  override def evaluate(features: DataPoint): (Double, List[Feature]) = {
    val result = root.evaluate(features)
    (result._1 * weight, result._2)
  }
}

class Leaf(output: Double) extends TreeNode {
  override def evaluate(features: DataPoint): (Double, List[Feature]) = (output, Nil)
}

class Split(left: TreeNode,
            right: TreeNode,
            threshold: Double,
            feature: Feature) extends TreeNode {

  override def evaluate(features: DataPoint): (Double, List[Feature]) = {

    val result =
      if (features.get(feature).get <= threshold)
        left.evaluate(features)
      else
        right.evaluate(features)

    (result._1, feature :: result._2)
  }
}

