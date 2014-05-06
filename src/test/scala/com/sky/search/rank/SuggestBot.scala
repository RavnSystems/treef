package com.sky.search.rank

import dispatch._, Defaults._
import java.util.UUID
//import play.api.libs.json.Json

/**
 * Created by remim on 11/04/14.
 */
object SuggestBot extends App {

  /*def readEntitiesFile(filePath : String){
    val wholeFile = io.Source.fromFile(filePath).toString()

    val typ : Type = new TypeToken<List<Entity>>() { }.getType();
    List<Entity> rawEntities = new Gson().fromJson(entitiesJson, type);

    val json = Json.parse(wholeFile)


    json(0)

  }*/

//  readEntitiesFile("/home/remim/data/entities.json")

  /*val svc = url("http://localhost:4000/entities/suggest")
  svc.addQueryParameter("cid", UUID.randomUUID().toString)
  val country = Http(svc OK as.String)

  svc.addQueryParameter("term", )
*/

}
