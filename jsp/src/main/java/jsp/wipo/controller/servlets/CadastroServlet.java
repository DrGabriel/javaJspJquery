package jsp.wipo.controller.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.NoSuchElementException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import jsp.wipo.controller.service.WipoService;
import jsp.wipo.dao.PersistenciaProcessoPatente;
import jsp.wipo.model.ProcessoPatente;

/**
 * Servlet implementation class CadastroServlet
 */
public class CadastroServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CadastroServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		String numeroProcesso = request.getParameter("numeroProcesso");

		if(numeroProcesso != null) {
			numeroProcesso = numeroProcesso.replaceAll("/", "");
			executaBuscaProcessoPatente(request,response,numeroProcesso);
		}else {
			
			request.getRequestDispatcher("views/cadastro.jsp").forward(request, response);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String data = extraiInformacaoPostBody(request);
		ProcessoPatente processoPatente = new Gson().fromJson(data, ProcessoPatente.class);
		try{	
			salvarProcessoPatente(processoPatente);
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(200);
			out.print("{\"sucesso\": \"O processo de patente foi cadastrado no banco de dados!\" }");
			out.flush();
		}catch(IllegalArgumentException e) {
			e.printStackTrace();
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(500);
			out.print("{\"erro\": \"O processo de patente já está cadastrado no banco de dados!\" }");
			out.flush();
		}

	}

	private String extraiInformacaoPostBody(HttpServletRequest request) throws IOException {
		StringBuilder buffer = new StringBuilder();
		    BufferedReader reader = request.getReader();
		    String line;
		    while ((line = reader.readLine()) != null) {
		        buffer.append(line);
		    }
		    String data = buffer.toString();
		return data;
	}

	private void executaBuscaProcessoPatente(HttpServletRequest request, HttpServletResponse response,String numeroProcesso)
			throws ServletException, IOException {
		
		if (numeroProcesso == null) {
			/*
			 * redireciona pra pagina informando que houve um erro/ numero do processo deve
			 * ser preenchido
			 */
		} else {
			try {
				String processoJSON = WipoService.getJsonProcessoPatente(numeroProcesso);
				// request.getRequestDispatcher("views/cadastro.jsp").forward(request,
				// response);
				PrintWriter out = response.getWriter();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				out.print(processoJSON);
				out.flush();

			} catch (NoSuchElementException e1) {
				e1.printStackTrace();

				PrintWriter out = response.getWriter();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.setStatus(404);
				out.print("{\"erro\": \"Houve um erro, não foi possível encontrar o número de processo informado!\" }");
				out.flush();

			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	}

	private void salvarProcessoPatente(ProcessoPatente processoPatente) {
		processoPatente.setNumeroPublicacao(
		processoPatente.getNumeroPublicacao().replace("/", ""));
		PersistenciaProcessoPatente.criar(processoPatente);
	}

}
