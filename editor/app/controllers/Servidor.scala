package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import scala.collection.mutable.Queue;
import java.io._
import scala.sys.process._

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
//        println("Thread--------------------------------------------------------")
//        println(pedidos)
//        println("--------------------------------------------------------------")
    }
    
    def index = Action {
        resultMessage; Ok(views.html.teste_editor(pedidos))
    }

    def getMessage =  Action {
        resultMessage; Ok(views.html.teste_mensagem(pedidos))
    }

    def postMessage = Action { implicit request =>
        requisicao+=(request.body); Redirect(routes.Servidor.index)
    }
    
    def getCompilar =  Action {
        Ok.sendFile(new File("saida.txt"))
    }

    def postCompilar = Action {
        val pwArquivo = new PrintWriter(new File("arquivo.poti" ))
        val p = pedidos.clone
        var x = ""
        while(!p.isEmpty) { x+= p.dequeue; }
        pwArquivo.write(x)
        pwArquivo.close

        val command = Seq("java","-jar","/home/papejajunior/app/potigol097/potigol.jar", "arquivo.poti") #> new File("saida.txt")!

        Redirect(routes.Servidor.index)
    }
}
