package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import scala.collection.mutable.Queue;
import java.io._
import scala.sys.process.{ stringSeqToProcess, ProcessBuilder }

@Singleton
class Servidor @Inject() extends Controller {
    var pedidos = new Queue[Char] //Codigo
    var requisicao = new Queue[AnyContent] //Empilha todos os pedidos
   
    def resultMessage() = {
       while(!requisicao.isEmpty) {
            val element = requisicao.dequeue;
            val caracter = element.asFormUrlEncoded.map { x => x("caracter")(0)}.getOrElse("") 
            val position = element.asFormUrlEncoded.map { x => x("position")(0).toInt}.getOrElse(-1)
            val action = element.asFormUrlEncoded.map { x => x("action")(0)}.getOrElse("") 
    
            if(position != -1 && position >= pedidos.size)
                pedidos+=(caracter(0))
        }
        println("Thread--------------------------------------------------------")
        println(pedidos)
        println("--------------------------------------------------------------")
    }
    
    def index = Action {
        resultMessage; Ok(views.html.teste_editor(pedidos))
    }

    def getMessage =  Action {
        //thread.start; Ok(views.html.teste(pedidos))
        resultMessage; Ok(views.html.teste_mensagem(pedidos))
    }

    def postMessage = Action { implicit request =>
        requisicao+=(request.body); Redirect(routes.Servidor.index)
    }
    
    def getCompilar =  Action {
        Redirect(routes.Servidor.index)
    }

    def postCompilar = Action {
        // PrintWriter
        val pw = new PrintWriter(new File("arquivo.poti" ))
        pw.write(pedidos.toString)
        pw.close
        
        Redirect(routes.Servidor.index)
        // FileWriter
        //val file = new File(canonicalFilename)
        //val bw = new BufferedWriter(new FileWriter(file))
        //bw.write(text)
        //bw.close()
    }
}
