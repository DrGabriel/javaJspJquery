<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%@include file="cabecalho.jsp"%>
	<h2>Busca de processos de patente</h2>
	<%@include file="waitDiv.jsp"%>
	<div id="mensagem" class="alert alert-info"></div>
	<div>
		<h3>Busca por número do processo</h3>
		<form name="buscaNumero" id="buscaNumero" class="form-group">
			<label for="numeroProcesso">Número do Processo</label> <input
				type="text" name="numeroProcesso" />
			<button type="button" class="btn btn-primary" id="btnBuscarNumero">buscar por número</button>
		</form>
		<h3>Busca por requerente</h3>
		<form name="buscaRequerente" id="buscaRequerente" class="form-group">
			<label for="requerente">Requerente</label> <input type="text"
				name="requerente" />
			<button type="button" class="btn btn-primary" id="btnBuscarRequerente">buscar por
				requerente</button>
		</form>
	</div>
	<div id="resultado">
		<table class="table" id="tabelaProcessos">
			<thead>
				<tr>
					<th scope="col">#</th>
					<th scope="col">Número de Publicação</th>
					<th scope="col">Número do Pedido Internacional</th>
					<th scope="col">Data de Publicação</th>
					<th scope="col">Requerentes</th>
					<th scope="col">Título</th>
					
				</tr>
			</thead>
			<tbody id="corpoTabelaProcessos">

			</tbody>
		</table>
	</div>
	<script src="./views/js/busca.js"></script>
</body>
</html>