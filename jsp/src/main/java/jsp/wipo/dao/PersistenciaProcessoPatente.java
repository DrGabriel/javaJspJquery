package jsp.wipo.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.query.Query;

import jsp.wipo.dao.hibernate.HibernateUtil;
import jsp.wipo.model.ProcessoPatente;
import jsp.wipo.model.SiglaIdioma;
import jsp.wipo.model.TituloIdioma;

public class PersistenciaProcessoPatente {
	
	public static List<ProcessoPatente> buscarProcessoNumero(String numeroProcesso) {
		org.hibernate.Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List<ProcessoPatente> processo = new ArrayList<ProcessoPatente>();
		session.beginTransaction();
		Query queryProcesso = session.createQuery("select p from ProcessoPatente p where p.numeroPublicacao = :num");
		queryProcesso.setParameter("num", numeroProcesso);
		processo = queryProcesso.getResultList();
		session.getTransaction().commit();
		session.close();
		return processo;
	}
	public static void criar(ProcessoPatente processo) throws IllegalArgumentException{
		org.hibernate.Session session = HibernateUtil.getSessionFactory().getCurrentSession();

		session.beginTransaction();
		Query queryProcesso = session.createQuery("select p from ProcessoPatente p where p.numeroPublicacao = :num");
		queryProcesso.setParameter("num", processo.getNumeroPublicacao());
		if (queryProcesso.uniqueResult() == null) {

			for (TituloIdioma tituloIdioma : processo.getTitulo()) {

				Query query = session
						.createQuery("select idioma from SiglaIdioma idioma where idioma.sigla = :nome");
				query.setParameter("nome", tituloIdioma.getIdioma().getSigla());

				if (query.uniqueResult() != null) {
					SiglaIdioma idioma = (SiglaIdioma) query.uniqueResult();
					tituloIdioma.setIdioma(idioma);
				} else {
					session.save(tituloIdioma.getIdioma());
					session.flush();
					session.clear();
				}

				session.save(tituloIdioma);
				session.flush();
				session.clear();

			}
			session.save(processo);
		}else {
			throw new IllegalArgumentException();
		}
		session.getTransaction().commit();
		session.close();
	}
	public static List<ProcessoPatente> buscarProcessoRequerente(String requerente) {
		org.hibernate.Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List<ProcessoPatente> processo = new ArrayList<ProcessoPatente>();
		session.beginTransaction();
		Query queryProcesso = session.createQuery("select p from ProcessoPatente p where p.requerentes like :requerentes");
		queryProcesso.setParameter("requerentes","%"+requerente+"%");
		processo = queryProcesso.getResultList();
		session.getTransaction().commit();
		session.close();
		return processo;
	}
}
