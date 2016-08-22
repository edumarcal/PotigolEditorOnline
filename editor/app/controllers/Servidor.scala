package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import scala.collection.mutable._;
import java.io._
import scala.sys.process._

@Singleton
class Servidor @Inject() extends Controller {
    
    var pedidos = new StringBuilder //Codigo
    var requisicao = new Queue[AnyContent] //Empilha todos os pedidos

    def resultMessage() = {
       while(!requisicao.isEmpty) {
            val element = requisicao.dequeue;
            val caracter = element.asFormUrlEncoded.map { x => x("caracter")(0)}.getOrElse("") 
            val position = element.asFormUrlEncoded.map { x => x("position")(0).toInt}.getOrElse(-1)
            val action = element.asFormUrlEncoded.map { x => x("action")(0)}.getOrElse("") 
    
            if(position != -1) { // && position >= pedidos.size
                if(action == "insert") {
                    if(caracter == "Enter")
                        pedidos+=13 //caracter \n
                    else 
                        pedidos+=(caracter(0))
                } else if(action == "backspace") {
                    pedidos.delete(position-1, position) //margem do caracter BACKSPACE
                } else {
                    pedidos.delete(position, position+1) //margem do caracter DELETE
                }
            }
        }
    }
    
    def index = Action { 
        resultMessage; Ok(views.html.editor_principal(pedidos))
    }

    def getMessage =  Action {
        resultMessage; Ok(views.html.editor_secundario(pedidos))
    }

    def postMessage = Action { implicit request =>
        requisicao+=(request.body); Redirect(routes.Servidor.index)
    }
    
    def getCode = Action {
        Ok(views.html.code(pedidos))
    }
    
    def getCompilar =  Action {
        Ok.sendFile(new File("saida.txt"))
    }

    def postCompilar = Action {
        val pwArquivo = new PrintWriter(new File("arquivo.poti"))
        pwArquivo.write(pedidos.toString)
        pwArquivo.close

        val command = Seq("java","-jar","/home/papejajunior/app/potigol097/potigol.jar", "arquivo.poti") #> new File("saida.txt")!
        
        Redirect(routes.Servidor.index)
    }
}
