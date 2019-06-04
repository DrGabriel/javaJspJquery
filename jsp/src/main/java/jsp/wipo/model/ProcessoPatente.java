package jsp.wipo.model;

import java.util.List;


import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ProcessoPatente")
public class ProcessoPatente {
	
	@Id
	@Column(name="numeroPublicacao",nullable=false)
	private String numeroPublicacao;
	@Column(name="numeroPedidoInternacional",nullable=false)
	private String numeroPedidoInternacional;
	@Column(name="dataPublicacao",nullable=false)
	private String dataPublicacao;
	@Column(name="requerentes",nullable=false)
	private String requerentes;
	
	//@OneToMany(mappedBy="processoPatente", cascade = {CascadeType.ALL, CascadeType.PERSIST})
	@ElementCollection(fetch = FetchType.EAGER)
	private List<TituloIdioma> titulo;

	
	public String getNumeroPublicacao() {
		return numeroPublicacao;
	}

	public void setNumeroPublicacao(String numeroPublicacao) {
		this.numeroPublicacao = numeroPublicacao;
	}

	public String getNumeroPedidoInternacional() {
		return numeroPedidoInternacional;
	}

	public void setNumeroPedidoInternacional(String numeroPedidoInternacional) {
		this.numeroPedidoInternacional = numeroPedidoInternacional;
	}

	public String getDataPublicacao() {
		return dataPublicacao;
	}

	public void setDataPublicacao(String dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}

	public String getRequerentes() {
		return requerentes;
	}

	public void setRequerentes(String requerentes) {
		this.requerentes = requerentes;
	}

	public List<TituloIdioma> getTitulo() {
		return titulo;
	}

	public void setTitulo(List<TituloIdioma>  titulo) {
		this.titulo = titulo;
	}
	
	
	
}
