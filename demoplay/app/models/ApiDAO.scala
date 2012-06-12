package models
import play.api.libs.json.JsObject
import play.api.libs.json.Json
import play.api.libs.json.JsArray
import play.api.libs.json.JsString
import java.util.Date

class ApiDAO {
  def save(a: Api) {
    val fileUtil = new FileProcessUtils()
    var jsObj: JsObject = Json.parse(fileUtil.readDB).asInstanceOf[JsObject]
    var jsArr: JsArray = (jsObj \ "apis").asInstanceOf[JsArray]
    var newJsObj = JsObject(Seq(
      "id" -> JsString(a.id),
      "url" -> JsString(a.url),
      "des" -> JsString(a.des),
      "update_date" -> JsString((new Date).toLocaleString()),
      "id_category" -> JsString(a.idCategory)))

    var newJsArr = JsArray()

    if (findById(a.id) == null) {
      newJsArr = jsArr :+ newJsObj
    } else {
      jsArr.as[List[JsObject]].foreach(jObj => {
        if ((jObj \ "id").as[String] == a.id) {
          newJsArr :+= newJsObj
        } else {
          newJsArr :+= jObj
        }
      })
    }

    jsObj ++= JsObject(Seq(
      "apis" -> newJsArr))
    fileUtil.writeDB(jsObj.toString());
  }

  def findAll: List[Api] = {
    val fileUtil = new FileProcessUtils()
    var jsObj: JsObject = Json.parse(fileUtil.readDB).asInstanceOf[JsObject]
    var jsArr: JsArray = (jsObj \ "apis").asInstanceOf[JsArray]
    var res = List[Api]()
    jsArr.as[List[JsObject]].foreach(jObj => {
      var api = new Api()
      api.id = (jObj \ "id").as[String]
      api.url = (jObj \ "url").as[String]
      api.des = (jObj \ "des").as[String]
      api.updateDate = (jObj \ "update_date").as[String]
      api.idCategory = (jObj \ "id_category").as[String]
      res :+= api
    })
    return res
  }

  def findAllByIdCategory(id: String): List[Api] = {
    val fileUtil = new FileProcessUtils()
    var jsObj: JsObject = Json.parse(fileUtil.readDB).asInstanceOf[JsObject]
    var jsArr: JsArray = (jsObj \ "apis").asInstanceOf[JsArray]
    var res = List[Api]()
    jsArr.as[List[JsObject]].foreach(jObj => {
      if ((jObj \ "id_category").as[String] == id) {
        var api = new Api()
        api.id = (jObj \ "id").as[String]
        api.url = (jObj \ "url").as[String]
        api.des = (jObj \ "des").as[String]
        api.updateDate = (jObj \ "update_date").as[String]
        api.idCategory = (jObj \ "id_category").as[String]
        res :+= api
      }
    })
    return res
  }

  def findById(id: String): Api = {
    val fileUtil = new FileProcessUtils()
    var jsObj: JsObject = Json.parse(fileUtil.readDB).asInstanceOf[JsObject]
    var jsArr: JsArray = (jsObj \ "apis").asInstanceOf[JsArray]
    jsArr.as[List[JsObject]].foreach(jObj => {
      if ((jObj \ "id").as[String] == id) {
        var api = new Api()
        api.id = (jObj \ "id").as[String]
        api.url = (jObj \ "url").as[String]
        api.des = (jObj \ "des").as[String]
        api.updateDate = (jObj \ "update_date").as[String]
        api.idCategory = (jObj \ "id_category").as[String]
        return api
      }
    })
    return null
  }

  def delete(a: Api) {
    val fileUtil = new FileProcessUtils()
    var jsObj: JsObject = Json.parse(fileUtil.readDB).asInstanceOf[JsObject]
    var jsArr: JsArray = (jsObj \ "apis").asInstanceOf[JsArray]

    var newJsArr = JsArray()

    jsArr.as[List[JsObject]].foreach(jObj => {
      if ((jObj \ "id").as[String] != a.id) {
        newJsArr :+= jObj
      }
    })

    jsObj ++= JsObject(Seq(
      "apis" -> newJsArr))
    fileUtil.writeDB(jsObj.toString());
  }
}