package controller.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import controller.utilitarios.CalculoDatas;
import model.Promocao;

public class PromocaoDao {

	private EntityManager manager;

	public PromocaoDao(EntityManager manager) {
		this.manager = manager;
	}

	public List<Promocao> listarIgual(String valor, String nomeDaColuna) {
		String jpql = "SELECT a FROM " + "promocao"+ " a WHERE a." + nomeDaColuna + " = '" + valor + "'";
		List<Promocao> lista = this.manager.createQuery(jpql, Promocao.class).getResultList();
		return lista;
	}
	
	public Promocao promocaoDiaCidade(String cidade) {
		CalculoDatas cdtas = new CalculoDatas();
		String dataHoje = cdtas.dataAtual();
		String jpql = "SELECT p FROM promocao p WHERE "
				+ "p.dataInicio <= '"+dataHoje+"' AND p.empresa.endereco.cidade = '"+cidade+"' "
				+ "AND p.dataFim >= '"+dataHoje+"'"; 
		Promocao promocao = this.manager.createQuery(jpql, Promocao.class).getSingleResult();
		return promocao;
	}  
	
	public Map<String,String> listaCidadesPorUf(String uf) {
		String jpql = "select DISTINCT(e.cidade) from Endereco e WHERE e.uf = '"+uf+"'";
		List<String> lista = this.manager.createQuery(jpql, String.class).getResultList();
		Map<String, String> cidades = new HashMap<String,String>();
		for(String a:lista){
			cidades.put(a, a);
		}
		return cidades;
	}

}