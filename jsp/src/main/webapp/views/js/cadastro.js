const urlServico = "http://localhost:8080/jsp/Cadastro";
var processoPatente = undefined;
$("#formProcesso").hide();
$("#mensagemErro").hide();
$("#mensagemSalvar").hide();

$("#btnBuscar").click(
		function() {
			let numeroProcesso = $('[name="numeroProcesso"]').val().replace(/\//g,'');
			console.log(numeroProcesso);
			$.get(urlServico + "?numeroProcesso=" + numeroProcesso,
					function(data, status, xhr) {

						processoPatente = data;
						populaValoresFormProcesso(data)
						$("#formBusca").hide();
						$("#mensagemErro").hide();
						$("#formProcesso").show();
						$("#mensagemSalvar").text("Dados encontrados com sucesso no wipo");

					}).fail(function(data, status, xhr) {
				$("#mensagemSalvar").text("Houve um erro, não foi possível recuperar o processo de patente!");
			});
		});

function populaValoresFormProcesso(jsonResposta) {
	console.log(jsonResposta);
	$.each($('#processoPatente').serializeArray(), function(i, field) {
		$('[name="' + field.name + '"]').val(jsonResposta[field.name]);
	});
	
	jsonResposta["titulo"].forEach(element =>{
		$('#titulos').append($('<div class="row">'+
									'<div class="col">'+
										'<label> Titulo '+ element.idioma.sigla + '</label>'+
									'</div>'+
									'<div class="col">'+
										'<input value="'+ element.titulo + '"/>'+
									'</div>'+
								'</div>'));
	});
	$("#processoPatente input").prop("disabled", true);
	$("#processoPatente textarea").prop("disabled", true);
}

function geraJsonProcessoPatente() {

	let jsonProcessoPatente = "{";
	$.each($('#processoPatente').serializeArray(), function(i, field) {
		jsonProcessoPatente += "\"" + field.name + "\"" + ":" + "\""
				+ field.value + "\"" + ",\n";
	});
	jsonProcessoPatente = jsonProcessoPatente.substring(0,
			jsonProcessoPatente.length - 2)
	jsonProcessoPatente += "}";
	return jsonProcessoPatente;
}

$("#btnSalvar").click(function() {
	$("#mensagemSalvar").text("salvando...");
	$.post(urlServico, JSON.stringify(processoPatente), function(data, status,xhr) {
		$("#mensagemSalvar").text(data.sucesso);
	}).fail(function(data, status, xhr) {
		console.log(data);
		console.log(status);
		console.log(xhr);
		
		$("#mensagemSalvar").text(data.responseJSON.erro);
	});
});

$(document).ready(function(){
	  $(document).ajaxStart(function(){
		  $("#mensagemSalvar").show();
	    $("#wait").css("display", "block");
	  });
	  $(document).ajaxComplete(function(){
	    $("#wait").css("display", "none");
	   
	  });
	});
