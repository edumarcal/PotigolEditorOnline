package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import scala.collection.mutable.Queue;

@Singleton
class Servidor @Inject() extends Controller {
  var arquivo = new Queue[Char]
  var pedidos = new Queue[Char]
  var rq = new Queue[AnyContent]
  //pedidos+=('a','\n', 'b','\\','c','\t','d')
  
  def getMessage =  Action {
    //Ok(views.html.codigo(pedidos))
    //Ok(views.html.teste(pedidos))
    Ok(views.html.teste(rq))
  }

  def postMessage = Action { implicit request =>
    rq+=(request.body); Redirect(routes.HomeController.index)
  }
/*  
    def postMessage = Action { implicit request =>
  //AnyContentAsFormUrlEncoded(Map(caracter -> ArrayBuffer(83), positon -> ArrayBuffer(5), action -> ArrayBuffer(insert)))
    val caracter = request.body.asFormUrlEncoded.map { x => x("caracter")(0).toInt}.getOrElse(-1) 
  	val position = request.body.asFormUrlEncoded.map { x => x("position")(0).toInt}.getOrElse(-1)
  	val action = request.body.asFormUrlEncoded.map { x => x("action")(0)}.getOrElse("") 
  	
  	if(position != -1 && position > pedidos.size)
  	    pedidos+=(caracter.toChar)
  	    
  	println(pedidos(position))
  	
    println(request.body); Redirect(routes.HomeController.index)
  }
  */
}
