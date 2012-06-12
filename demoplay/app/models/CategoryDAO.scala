package models
import play.api.libs.json.JsObject
import play.api.libs.json.JsString
import play.api.libs.json.Json
import play.api.libs.json.JsArray
import java.util.Date

class CategoryDAO {
  def save(c: Category) {
    val fileUtil = new FileProcessUtils()
    var jsObj: JsObject = Json.parse(fileUtil.readDB).asInstanceOf[JsObject]
    var jsArr: JsArray = (jsObj \ "categories").asInstanceOf[JsArray]
    var newJsObj = JsObject(Seq(
      "id" -> JsString(c.id),
      "name" -> JsString(c.name),
      "update_date" -> JsString((new Date).toLocaleString())))

    var newJsArr = JsArray()

    if (findById(c.id) == null) {
      newJsArr = jsArr :+ newJsObj
    } else {
      jsArr.as[List[JsObject]].foreach(jObj => {
        if ((jObj \ "id").as[String] == c.id) {
          newJsArr :+= newJsObj
        } else {
          newJsArr :+= jObj
        }
      })
    }

    jsObj ++= JsObject(Seq(
      "categories" -> newJsArr))
    fileUtil.writeDB(jsObj.toString());
  }

  def findAll: List[Category] = {
    val fileUtil = new FileProcessUtils()
    var jsObj: JsObject = Json.parse(fileUtil.readDB).asInstanceOf[JsObject]
    var jsArr: JsArray = (jsObj \ "categories").asInstanceOf[JsArray]
    var res = List[Category]()
    var category = new Category()
    jsArr.as[List[JsObject]].foreach(jObj => {
      var category = new Category()
      category.id = (jObj \ "id").as[String]
      category.name = (jObj \ "name").as[String]
      category.updateDate = (jObj \ "update_date").as[String]
      res :+= category
    })
    return res
  }

  def findById(id: String): Category = {
    val fileUtil = new FileProcessUtils()
    var jsObj: JsObject = Json.parse(fileUtil.readDB).asInstanceOf[JsObject]
    var jsArr: JsArray = (jsObj \ "categories").asInstanceOf[JsArray]
    jsArr.as[List[JsObject]].foreach(jObj => {
      if ((jObj \ "id").as[String] == id) {
        var category = new Category()
        category.id = (jObj \ "id").as[String]
        category.name = (jObj \ "name").as[String]
        category.updateDate = (jObj \ "update_date").as[String]
        return category
      }
    })
    return null
  }

  def delete(c: Category) {
    val fileUtil = new FileProcessUtils()
    var jsObj: JsObject = Json.parse(fileUtil.readDB).asInstanceOf[JsObject]
    var jsArr: JsArray = (jsObj \ "categories").asInstanceOf[JsArray]

    var newJsArr = JsArray()
    jsArr.as[List[JsObject]].foreach(jObj => {
      if ((jObj \ "id").as[String] != c.id) {
        newJsArr :+= jObj
      }
    })

    jsObj ++= JsObject(Seq(
      "categories" -> newJsArr))
    fileUtil.writeDB(jsObj.toString());
  }
}