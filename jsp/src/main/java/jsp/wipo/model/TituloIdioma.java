package jsp.wipo.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="TitutloIdioma")
public class TituloIdioma {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id",nullable=false)
	private int id;
	@Column(name="titulo", nullable=false)
	private String titulo;
	
	/*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_numeroProcesso")
	private ProcessoPatente processoPatente;*/
	
	@ManyToOne(cascade=CascadeType.PERSIST, fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name="id_siglaIdioma")
	private SiglaIdioma idioma;
	
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public SiglaIdioma getIdioma() {
		return idioma;
	}
	public void setIdioma(SiglaIdioma idioma) {
		this.idioma = idioma;
	}
	/*public ProcessoPatente getProcessoPatente() {
		return processoPatente;
	}
	public void setProcessoPatente(ProcessoPatente processoPatente) {
		this.processoPatente = processoPatente;
	}*/
	
	
}
