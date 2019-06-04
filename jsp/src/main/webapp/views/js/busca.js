const urlServico = "http://localhost:8080/jsp/Busca";
const msgNotFound = "Nenhum processo foi encontrado com o dado informado!";
$("#mensagem").hide();
var erro = false;
$("#btnBuscarNumero").click(
		function() {
			$("#corpoTabelaProcessos").empty();
			let numeroProcesso = $('[name="numeroProcesso"]').val().replace(/\//g,'');
			$.get(urlServico + "?opcaoBusca=buscaNumero&numeroProcesso=" + numeroProcesso,
					function(data, status, xhr) {
						if(data.length == 0){
							$("#mensagem").text(msgNotFound);
							erro = true;
						}else{
							
							data.forEach(function(processo,indice) {
								insereProcessoView(processo,indice);
							});
							erro = false;
						}

					}).fail(function(data, status, xhr) {
						erro = true;
				$("#mensagem").text(data.responseJSON.erro);
			});
		});

$("#btnBuscarRequerente").click(
		function() {
			$("#corpoTabelaProcessos").empty();
			let requerente = $('[name="requerente"]').val();
			$.get(urlServico + "?opcaoBusca=buscaRequerente&requerente=" + requerente,
					function(data, status, xhr) {
						if(data.length == 0){
							$("#mensagem").text(msgNotFound);
							erro = true;
						}else{
							
							data.forEach(function(processo,indice){
								insereProcessoView(processo,indice);
							});
							erro = false;
						}
					}).fail(function(data, status, xhr) {
						erro = true;
				$("#mensagem").text(data.responseJSON.erro);
			});
		});

function insereProcessoView(processo,indice){
	let titulo = '';
	let requerentes = processo.requerentes.split(';');
	let requerentesSeparados = '';
	requerentes.forEach(requerente=>{
		requerentesSeparados +='<div>[' + requerente +']</div>';
	});
	processo.titulo.forEach(tituloSiglaIdioma =>{
		titulo += 
			'<div><strong>' +
			tituloSiglaIdioma.idioma.sigla + '</strong> ' + tituloSiglaIdioma.titulo + '</div>\n';
	})
		
	
	$("#corpoTabelaProcessos").append(
		$('<tr>'+
				'<th scope="row">'+(indice+1)+'</th>'+
				'<td>'+processo.numeroPublicacao+'</td>'+
				'<td>'+processo.numeroPedidoInternacional+'</td>'+
				'<td>'+processo.dataPublicacao+'</td>'+
				'<td>'+requerentesSeparados+'</td>'+
				'<td>'+titulo+'</td>'+
				
		  '</tr'));
}

$(document).ready(function(){
	  $(document).ajaxStart(function(){
		  $("#mensagem").show();
	    $("#wait").css("display", "block");
	  });
	  $(document).ajaxComplete(function(){
		  if(!erro){
			  $("#mensagem").hide();
		  }
	    $("#wait").css("display", "none");
	   
	  });
	});
