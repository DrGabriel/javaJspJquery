<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Cadastro informações WIPO</title>

</head>
<body class="container">
	<%@include file="cabecalho.jsp"%>
	<h2>Cadastro de informações do proceso - WIPO</h2>
	<%@include file="waitDiv.jsp"%>
	<div id="mensagemSalvar" class="alert alert-info"></div>
	<div id="formBusca">
		<h3>Busca de processo</h3>
		<form name="busca" id="busca">
			<label for="numeroProcesso">Número do Processo</label> <input
				type="text" name="numeroProcesso" />
			<button type="button" class="btn btn-primary" id="btnBuscar">buscar</button>
		</form>
	</div>


	<div id="formProcesso">
		<h3>Formulário do Processo</h3>
		<span id="mensagemSalvar"></span>
		<form id="processoPatente" class="form-group container">
			<div class="row">
				<div class="col">
					<label class="label label-default" for="numeroPublicacao">Número
						de publicacao</label>
				</div>
				<div class="col">
					<input type="text" name="numeroPublicacao" />
				</div>
			</div>
			<div class="row">
				<div class="col">
					<label class="label label-default" for="numeroPedidoInternacional">Número
						do pedido internacional</label>
				</div>
				<div class="col">
					<input type="text" name="numeroPedidoInternacional" />
				</div>
			</div>
			<div class="row">
				<div class="col">
					<label class="label label-default" for="dataPublicacao">Data
						de publicação</label>
				</div>
				<div class="col">
					<input type="text" name="dataPublicacao" />
				</div>
			</div>
			<div class="row">
				<div class="col">
					<label class="label label-default" for="requerentes">Requerentes</label>
				</div>
				<div class="col">
					<textarea name="requerentes" rows="10" cols="50" wrap="soft"> </textarea>
				</div>
			</div>
			<div id="titulos"></div>
			<div>
				<button type="button" class="btn btn-primary" id="btnSalvar">salvar</button>
			</div>
		</form>
	</div>
	<script src="./views/js/cadastro.js"></script>
</body>
</html>