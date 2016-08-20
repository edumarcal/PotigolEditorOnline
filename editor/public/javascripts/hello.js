function updatePress(e, tag) {
    var cursor = tag.selectionStart;
    var code = (e.keyCode ? e.keyCode : e.which);
    
   $.post("/mensagem", {
       caracter: e.key,
       position: cursor,
       action: "insert"
    });    
}
        
function updateDown(e, tag){
    var cursor = tag.selectionStart;
    var code = (e.keyCode ? e.keyCode : e.which);
    if(cursor === 0 && code == 9) {
        e.preventDefault(); //cancelar a função do tab de mudar o foco
        tag.value += "\t";
        $.post("/mensagem", {
            caracter: "\t",
            position: cursor,
            action: "inserir"
        });  
    } else if(cursor > 0) {
        switch (code) {
            case 8: //tecla backspace
                $.post("/mensagem", {
                    caracter: e.key,
                    position: cursor,
                    action: "backspace"
                });  
                //console.warn(cursor);
                break;
            case 9: //tecla tab
                e.preventDefault(); //cancelar a função do tab de mudar o foco
                tag.value += "\t";
                $.post("/mensagem", {
                    caracter: "\t",
                    position: cursor,
                    action: "inserir"
                });    
            //console.warn(cursor);
                break;
              case 13: //tecla enter
                $.post("/mensagem", {
                    caracter: "\n",
                    position: cursor,
                    action: "insert"
                });    
            //console.warn(cursor);
                break;
            case 46: //tecla delete
                $.post("/mensagem", {
                    caracter: e.key,
                    position: cursor,
                    action: "delete"
                });   
            //console.warn(cursor);
            break;
        }
    }
//            console.warn(cursor);
//            console.warn(e);
}
        
function runCode() {
   $.post("/compilar");
   
   var result = document.getElementById("resultado");
   result.setAttribute("style", "font-size:30px;color:red");
   result.innerHTML = "aguarde o processo de compilação";
   
   window.setInterval(function() {
       $.get("/compilar", function(data) {
           if(data) {
               result.setAttribute("style", "font-size:30px;color:blue");
               result.innerHTML = data;   
           }
        });
    }, 1000);
}

function updateMessage(codigo){
    window.setInterval(function() {
        document.getElementById("texto").value = codigo;
        console.info("run");
    }, 1000);
}