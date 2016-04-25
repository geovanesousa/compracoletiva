package model;

import java.util.Calendar;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity(name = "promocao")
public class Promocao {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(nullable = false, length = 50)
	private String titulo;

	@Column(name = "qtd_compras")
	private Integer qtdCompras;

	@Temporal(TemporalType.DATE)
	@Column(name = "dt_fim", nullable = false)
	private Date dataFim;

	@Column(name = "min_compras", nullable = false)
	private Integer minimoCompras;

	@Column(nullable = false, length = 250)
	private String descricao;

	@Temporal(TemporalType.DATE)
	@Column(name = "dt_inicio", nullable = false)
	private Date dataInicio;
	
	@Transient
	private String tempoRestante;

	@JoinColumn(name = "empresa_id", updatable = false, nullable = false)
	@OneToOne
	Empresa empresa;

	@Column(scale = 2, name = "pc_normal" , nullable = false)
	private Double precoNormal;

	@Column(scale = 2, name = "pc_desconto", nullable = false)
	private Double precoDesconto;

	public Promocao(){
		this.empresa = new Empresa();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Integer getQtdCompras() {
		return qtdCompras;
	}

	public void setQtdCompras(Integer qtdCompras) {
		this.qtdCompras = qtdCompras;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public Integer getMinimoCompras() {
		return minimoCompras;
	}

	public void setMinimoCompras(Integer minimoCompras) {
		this.minimoCompras = minimoCompras;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Double getPrecoNormal() {
		return precoNormal;
	}

	public void setPrecoNormal(Double precoNormal) {
		this.precoNormal = precoNormal;
	}

	public Double getPrecoDesconto() {
		return precoDesconto;
	}

	public void setPrecoDesconto(Double precoDesconto) {
		this.precoDesconto = precoDesconto;
	}
	
	public String getTempoRestante() {
		Calendar dataFinal = Calendar.getInstance();  
		dataFinal.setTime(this.dataFim);
		dataFinal.add(Calendar.DATE, 1);
		Long diferenca = System.currentTimeMillis() - dataFinal.getTimeInMillis();  
		Long diferencaEmSegundo = diferenca / 1000;
		Long diferencaEmMinuto = diferencaEmSegundo / 60;
		Long diferencaEmHoras = diferencaEmMinuto / 60;
		Integer horas = diferencaEmHoras.intValue();
		horas = Math.abs(horas);
		Integer minutos = diferencaEmMinuto.intValue();
		minutos = Math.abs(minutos);
		minutos = minutos-(horas*60);
		
		this.tempoRestante = horas+" horas e "+minutos+" minutos";
		return tempoRestante; 
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Promocao other = (Promocao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
