package com.ravn.treef

import ciir.umass.edu.learning.RankerFactory
import ciir.umass.edu.learning.tree.LambdaMART
import java.io.FileNotFoundException
import java.nio.file.{Path, Paths, Files}

/**
 * Created by remim on 14/05/14.
 */
object TreefApp extends App {

  val usage = "Usage: java -jar treef.jar -m model-path1 model-path1 -d dataset-path [-f format-description] [-o output]"

  val modelK = 'model
  val datasetK = 'dataset
  val formatK = 'format
  val outputK = 'output

  val opts = Map[String, Symbol](
    "-m" -> modelK,
    "-d" -> datasetK,
    "-o" -> outputK,
    "-f" -> formatK
  )

  def checkPath (path: Path) : Boolean = {
    if (! Files.exists(path))
      throw new FileNotFoundException(path.toString)

    true
  }

  def checkPath(pathS : String, parent : Boolean = false) : Path = {

    val path = Paths.get(pathS)

    if (parent)
      checkPath(path.getParent)
    else
      checkPath(path)

    path
  }

  def checkMandatory = (map : Map[Symbol, Any]) =>
    map.contains(modelK) && map.contains(datasetK)

  def addOption(k : String, value : String) : Map[Symbol, Path] = {
    if (!opts.contains(k))
      throw new RuntimeException("Unrecognized option : " + k)

    if (opts(k) != outputK)
      // we check that the file exists
      Map(opts(k) -> checkPath(value))
    else
      Map(opts(k) -> Paths.get(value).getParent)
  }

  def parseOption(map : Map[Symbol, Path],
                  list: List[String]) : Map[Symbol, Path] = {
    list match {
      case Nil => map
      case k :: value :: tail =>
        parseOption(map ++ addOption(k, value), tail)
      case _ =>
        throw new RuntimeException("Cannot parse command.")
    }
  }

  def intToFeat: (Int) => (Int, Feature) =
    (i: Int) => (i, new Feature(i, "Feature " + i))

  val arglist = args.toList

  val options = parseOption(Map(), arglist)

  if (!checkMandatory(options)) {
    throw new RuntimeException("Missing parameters :\n" + usage)
  }

  val featuresMap = {

    if (options.contains(formatK)) {

      val lines =
        io.Source.fromFile(options(formatK).toString)
          .getLines()

      // parse features
      lines
        .map(l => l.split(":"))
        .map(a => new Feature(a(0).toInt, a(1)))
        .map(f => (f.i, f))
        .toMap
    }

    else {

      // find n features by reading first line
      val nFeatures =
        DataPointReader.getNFeatures(options(datasetK))

      (1 to nFeatures).map(intToFeat).toMap

    }
  }

  val dataPoints =
    DataPointReader.slurp(options(datasetK), featuresMap)

  val ranker = new RankerFactory()
    .loadRanker(options(modelK).toString)
    .asInstanceOf[LambdaMART]

  val ensemble =
    EnsembleBuilder.buildEnsemble(ranker.getEnsemble, featuresMap)

  val gini = new GiniImportance(featuresMap.values)
  val decrease = gini.perform(ensemble, dataPoints)

  Output.print(decrease)

  val modelFile = options(modelK).getFileName.toString

  if (options.contains(outputK))
    Output.writeResultSet(
      options(outputK),
      List((modelFile, decrease)),
      featuresMap.values.toList)

}
