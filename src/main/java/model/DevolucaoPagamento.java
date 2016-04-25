package model;

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

@Entity(name = "dev_pgto")
public class DevolucaoPagamento {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@JoinColumn(name = "pg_cartao_id", updatable = false, nullable = false)
	@OneToOne
	PagamentoCartao pagamentoCartao;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date data;

	public DevolucaoPagamento(){
		this.pagamentoCartao = new PagamentoCartao();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public PagamentoCartao getPagamentoCartao() {
		return pagamentoCartao;
	}

	public void setPagamentoCartao(PagamentoCartao pagamentoCartao) {
		this.pagamentoCartao = pagamentoCartao;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
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
		DevolucaoPagamento other = (DevolucaoPagamento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
