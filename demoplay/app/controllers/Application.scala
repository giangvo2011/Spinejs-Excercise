package controllers

import play.api._
import play.api.mvc._
import models.FileProcessUtils
import models.Category
import models.CategoryDAO
import models.ApiDAO
import java.util.Date
import models.Api

object Application extends Controller {

  val categoryDAO = new CategoryDAO()
  val apiDAO = new ApiDAO()
  
  def index = Action {
    val categoryDAO = new CategoryDAO()
    Ok(views.html.index(categoryDAO.findAll))
  }
  
  def new_category = Action {
    Ok(views.html.new_category())
  }
  
  def new_category_handle = Action(parse.urlFormEncoded) {request =>
    val id = String.valueOf((new Date()).getTime())
    val name = request.body.get("name").first.first
    val categoryDAO = new CategoryDAO()
    var category = new Category()
    category.id = id
    category.name = name
    categoryDAO.save(category)
    Redirect(routes.Application.index())
  }

  def category(id:String) = Action {request =>
    val categoryDAO = new CategoryDAO()
    val apiDAO = new ApiDAO()
    Ok(views.html.category(categoryDAO.findById(id), apiDAO.findAllByIdCategory(id)))
  }
  
  def edit_category(id:String) = Action {request =>
    val categoryDAO = new CategoryDAO()
    val category = categoryDAO.findById(id)
    Ok(views.html.edit_category(category))
  }
  
  def edit_category_handle = Action(parse.urlFormEncoded) {request =>
    val id = request.body.get("id").first.first
    val name = request.body.get("name").first.first
    val categoryDAO = new CategoryDAO()
    var category = categoryDAO.findById(id)
    category.name = name
    categoryDAO.save(category)
    Redirect(routes.Application.index())    
  }
  
  def delete_category(id:String) = Action {request =>
    val category = categoryDAO.findById(id)
    categoryDAO.delete(category)
    Redirect(routes.Application.index())
  }
  
  def new_api(idCategory:String) = Action {
    Ok(views.html.new_api(idCategory, categoryDAO.findAll))
  }
  
  def new_api_handle = Action(parse.urlFormEncoded) {request =>
    val id = String.valueOf((new Date()).getTime())
    val url = request.body.get("url").first.first
    val des = request.body.get("des").first.first
    val idCategory = request.body.get("idCategory").first.first
    var api = new Api()
    api.id = id
    api.url = url
    api.des = des
    api.idCategory = idCategory
    apiDAO.save(api)
    Redirect(routes.Application.category(idCategory))
  }
  
  def api(id:String) = Action {request =>
    val api = apiDAO.findById(id)
    Ok(views.html.api(api, categoryDAO.findById(api.idCategory)))
  }
  
  def edit_api(id:String) = Action {request =>
    Ok(views.html.edit_api(apiDAO.findById(id), categoryDAO.findAll))
  }
  
  def edit_api_handle = Action(parse.urlFormEncoded) {request =>
    val id = request.body.get("id").first.first
    val url = request.body.get("url").first.first
    val des = request.body.get("des").first.first
    val idCategory = request.body.get("idCategory").first.first
    var api = apiDAO.findById(id)
    api.url = url
    api.des = des
    api.idCategory = idCategory
    apiDAO.save(api)
    Redirect(routes.Application.category(idCategory))
  }
  
  def delete_api(id:String) = Action {request =>
    val api = apiDAO.findById(id)
    apiDAO.delete(api)
    Redirect(routes.Application.index())
  }
}