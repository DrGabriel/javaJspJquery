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
	 * Analisa html da resposta e monta o objeto da patente com as informações
	 * relevantes para a aplicação
	 */
	private static ProcessoPatente trataResposta(HttpResponse response)
			throws Exception {

		HttpEntity entity = response.getEntity();
		String result = EntityUtils.toString(entity,"UTF-8");
		
		/*
		 * Inicialmente não iria utilizar o jsoup, mas com o javax.xml.parsers, etc
		 * estava ocorrendo um erro de parse, afirmando que havia um <td> que não estava fechado
		 * entretanto como jsoup esse erro não ocorre, tratei todo o encoding e mesmo assim o erro persistiu
		 * então vamos de jsoup mesmo
		 */
		
		/*
		 * Extrai informações do html, caso não exista a informação uma execao é lançada
		 * Teoricamente caso não seja possível encontrar algum dos campos, é pq o elemento não existe
		 * Se o elemento não existe há dois motivos
		 * 1. numero do processo digitado não existe
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
			throw(new NoSuchElementException(idName + " não existe!"));
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
