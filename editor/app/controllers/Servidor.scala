package controllers

import javax.inject._
import play.api._
import play.api.mvc._
//import scala.collection.mutable.Queue;
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
println("#########################################################################")
println(element)
println("#########################################################################")
            val caracter = element.asFormUrlEncoded.map { x => x("caracter")(0)}.getOrElse("") 
            val position = element.asFormUrlEncoded.map { x => x("position")(0).toInt}.getOrElse(-1)
            val action = element.asFormUrlEncoded.map { x => x("action")(0)}.getOrElse("") 
    
            if(position != -1 && position >= pedidos.size) {
                if(action == "insert") {
                    pedidos+=(caracter(0))
println("--------------------------------------------------------")
print(action)
print("\t")
println(pedidos)
println("--------------------------------------------------------")
                } else if(action == "backspace") {
                    pedidos.delete(position-1, position) //margem do caracter
println("--------------------------------------------------------")
print(action)
print("\t")
println(pedidos)
println("--------------------------------------------------------")
                } else {
                    pedidos.delete(position, position+1) //margem do caracter
println("--------------------------------------------------------")
print(action)
print("\t")
println(pedidos)
println("--------------------------------------------------------")
                }
            }
            //println("--------------------------------------------------------")
            //println(element)
            //println("--------------------------------------------------------")
        }
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
        pwArquivo.write(pedidos.toString)
        pwArquivo.close

        println(pedidos)
        
        val command = Seq("java","-jar","/home/papejajunior/app/potigol097/potigol.jar", "arquivo.poti") #> new File("saida.txt")!

        Redirect(routes.Servidor.index)
    }
}
