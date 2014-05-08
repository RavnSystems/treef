package com.ravn.treef

/**
 * Created by remim on 11/04/14.
 */

sealed trait TreeNode {
  def evaluate(features: DataPoint): List[TreeNode]
}

sealed trait Scorer {
  def compute(features: DataPoint): Double
}

class Ensemble(val trees: Seq[Tree]) extends Scorer {

  override def compute(features: DataPoint): Double = {
    trees.map(tree => tree.compute(features)).sum
  }
}

class Tree(val weight: Double, val root : TreeNode) extends Scorer {
  override def compute(features: DataPoint): Double =
    weight * root.evaluate(features).last.asInstanceOf[Leaf].output
}

class Leaf(val output: Double) extends TreeNode {
  override def evaluate(features: DataPoint): List[TreeNode] = this :: Nil
}

class Split(val left: TreeNode, val right: TreeNode, threshold: Double,
            val feature: Feature) extends TreeNode {

  override def evaluate(dp: DataPoint): List[TreeNode] = {

    val result =
      if (dp.values(feature) <= threshold)
        left.evaluate(dp)
      else
        right.evaluate(dp)

    this :: result
  }
}

