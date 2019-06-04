package jsp.wipo.controller.service;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

import jsp.wipo.model.SiglaIdioma;
import jsp.wipo.model.ProcessoPatente;
import jsp.wipo.model.TituloIdioma;

public class WipoService {
	private static final String wipoUrl = "https://patentscope.wipo.int/search/pt/detail.jsf?docId=%s&redirectedID=true";

	// jogo a responsabilidade de tratar erro pro controlador
	public static String getJsonProcessoPatente(String numeroProcesso)
			throws Exception {
		String urlParametrizada = String.format(wipoUrl, numeroProcesso);

		HttpClient client = clientBuilder().build();
		//client.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
		HttpGet request = new HttpGet(urlParametrizada);
		request.addHeader("Content-Type", "text/html; charset=UTF-8");

		HttpResponse response = client.execute(request);

		return new Gson().toJson(trataResposta(response));
	}

	/*
	 * Analisa html da resposta e monta o objeto da patente com as informa��es
	 * relevantes para a aplica��o
	 */
	private static ProcessoPatente trataResposta(HttpResponse response)
			throws Exception {

		HttpEntity entity = response.getEntity();
		String result = EntityUtils.toString(entity,"UTF-8");
		
		/*
		 * Inicialmente n�o iria utilizar o jsoup, mas com o javax.xml.parsers, etc
		 * estava ocorrendo um erro de parse, afirmando que havia um <td> que n�o estava fechado
		 * entretanto como jsoup esse erro n�o ocorre, tratei todo o encoding e mesmo assim o erro persistiu
		 * ent�o vamos de jsoup mesmo
		 */
		
		/*
		 * Extrai informa��es do html, caso n�o exista a informa��o uma execao � lan�ada
		 * Teoricamente caso n�o seja poss�vel encontrar algum dos campos, � pq o elemento n�o existe
		 * Se o elemento n�o existe h� dois motivos
		 * 1. numero do processo digitado n�o existe
		 * 2. Id/classe dos elementos mudou de nome
		 */
		org.jsoup.nodes.Document doc = Jsoup.parse(result, "", Parser.xmlParser());
		
		String tdNumeroDePublicacao = findByID(doc, "detailPCTtableWO");
		String tdNumeroDePedidoInternacional = findByID(doc, "detailPCTtableAN");
		String tdDataDePublicacao = findByID(doc, "detailPCTtablePubDate");
		String tdRequerentes = findByID(doc, "PCTapplicants");
		List<TituloIdioma> tdTitulo = trataListaTitulosIdioma(doc, "PCTtitle");
		
		ProcessoPatente processo = new ProcessoPatente();
		processo.setNumeroPublicacao(tdNumeroDePublicacao);
		processo.setNumeroPedidoInternacional(tdNumeroDePedidoInternacional);
		processo.setDataPublicacao(tdDataDePublicacao);
		processo.setRequerentes(tdRequerentes);
		processo.setTitulo(tdTitulo);
		
		return processo;

	}

	private static List<TituloIdioma> trataListaTitulosIdioma(org.jsoup.nodes.Document doc, String className){
		Elements element = doc.getElementsByClass(className);
		org.jsoup.nodes.Document titulosIdiomas = Jsoup.parse(element.html(), "", Parser.xmlParser());
		Elements idiomas = titulosIdiomas.getElementsByClass("notranslate");
		Elements titulos = titulosIdiomas.getElementsByClass("trans-section NPtitle");
		List<TituloIdioma> listaTitulosIdioma = new ArrayList<TituloIdioma>();
		for(int indice = 0; indice < idiomas.size(); indice ++) {
			TituloIdioma tituloIdioma = new TituloIdioma();
			SiglaIdioma siglaIdioma = new SiglaIdioma(); 
			
			siglaIdioma.setSigla(idiomas.get(indice).ownText());
			tituloIdioma.setTitulo(titulos.get(indice).ownText());
			tituloIdioma.setIdioma(siglaIdioma);
			listaTitulosIdioma.add(tituloIdioma);
		}
		return listaTitulosIdioma;
	}
	private static String findByID(org.jsoup.nodes.Document doc, String idName) throws NoSuchElementException {
		org.jsoup.nodes.Element element = doc.getElementById(idName);
		
		if (element == null) {
			throw(new NoSuchElementException(idName + " n�o existe!"));
		} 
		
		return element.text();
	}

	private static HttpClientBuilder clientBuilder()
			throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		SSLContextBuilder builder = new SSLContextBuilder();
		builder.loadTrustMaterial(null, (chain, authType) -> true);
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(),
				NoopHostnameVerifier.INSTANCE);
		return HttpClients.custom().setSSLSocketFactory(sslsf);
	}

}
