package jsp.wipo.controller.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import jsp.wipo.dao.PersistenciaProcessoPatente;
import jsp.wipo.model.ProcessoPatente;

/**
 * Servlet implementation class BuscaServlet
 */
public class BuscaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BuscaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String opcaoBusca = request.getParameter("opcaoBusca");
		if(opcaoBusca != null) {
			switch(opcaoBusca) {
				case "buscaNumero":
					String numeroProcesso = request.getParameter("numeroProcesso");
					if(numeroProcesso != null) {
						numeroProcesso = numeroProcesso.replaceAll("/", "");
						List<ProcessoPatente> processo = PersistenciaProcessoPatente.buscarProcessoNumero(numeroProcesso);
						String respostaJson = new Gson().toJson(processo);
						geraResposta(response, respostaJson);
					}else {
						geraErro(response,"Numero do processo deve ser preenchido!");
					}
				break;
				case "buscaRequerente":
					String requerente = request.getParameter("requerente");
					if(requerente != null) {
						List<ProcessoPatente> processo = PersistenciaProcessoPatente.buscarProcessoRequerente(requerente);
						String respostaJson = new Gson().toJson(processo);
						geraResposta(response, respostaJson);
					}else {
						geraErro(response,"O campo do requerente deve ser preenchido!");
					}
				break;
				
				default:
				geraErro(response,"Parametro inválido!");
				break;
			}
		}else {
			
			request.getRequestDispatcher("views/busca.jsp").forward(request, response);
		}
		
	}

	private void geraResposta(HttpServletResponse response, String respostaJson) throws IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(200);
		out.print(respostaJson);
		out.flush();
	}
	
	private void geraErro(HttpServletResponse response, String msgErro) throws IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(500);
		out.print("{\"erro\": \" "+ msgErro+  "\" }");
		out.flush();
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	

}
