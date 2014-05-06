package com.ravn.treef

/**
 * Created by remim on 11/04/14.
 */
object DataPointReader {

  val splitChar = " "
  val valChar = ":"
  val comChar = "#"

  def slurp(filePath : String, features : Map[Int, Feature]) : List[DataPoint] = {
    io.Source.fromFile(filePath).getLines().toList
      .map(line => convert(line, features))
  }

  def convert(dataPointS : String, features : Map[Int, Feature]) : DataPoint = try {

    // todo throw exception properly

    val parts = dataPointS.split(splitChar)

    val c = parts(0).toInt

    val qid = parts(1).split(valChar)(1).toInt

    // TODO replace that explicit slice
    val values = dataPointS.split(splitChar)
      .slice(2, 2 + features.size)
      .map(s => s.split(valChar))
      .map(a => (features(a(0).toInt), a(1).toDouble))
      .toMap

    if (values.size != features.size) throw new IllegalArgumentException()

    val i = dataPointS.indexOf(comChar)

    val commented = dataPointS.length > (i + 1)

    if (!commented)
      new DataPoint(c, qid, values, "")
    else
      new DataPoint(c, qid, values, dataPointS.substring(i + 1))

  } catch {
    case _ => throw new IllegalArgumentException ("Wrong format for datapoint " + dataPointS)
  }

}