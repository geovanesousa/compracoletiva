package controller.dao;

import java.util.List;
import javax.persistence.EntityManager;

public class GenericoDao {

    private EntityManager manager;

    public GenericoDao(EntityManager manager) {
        this.manager = manager;
    }

    public void inserir(Object objeto) {
        this.manager.persist(objeto);
    }

    public void atualizar(Object objeto) {
        this.manager.merge(objeto);
    }

    public void excluir(Object objeto) {
        this.manager.remove(objeto);
    }

        public Object retornaAtributo(String valor, String nomeDaClasse, 
            String nomeDoAtributo, Class classe, String dadoDeRetorno) {
        
        String jpql = "SELECT a."+dadoDeRetorno+" FROM "+nomeDaClasse+""
                + " a WHERE a"+nomeDoAtributo+" = '"+valor+"'";
        
        Object atributo = this.manager.createQuery(jpql, classe).
                getSingleResult();
        return atributo;
    }
        
        public Object retornaTodosAtributos(String valor, String nomeDaClasse, 
                String nomeDaColuna, Class classe) {
            
            String jpql = "SELECT a FROM "+nomeDaClasse+""
                    + " a WHERE a."+nomeDaColuna+" = '"+valor+"'";
            
            Object atributo = this.manager.createQuery(jpql, classe).
                    getSingleResult();
            return atributo;
        }
    
    public List<String> listarPorInicio(String dadoDeRetorno, String nomeDaClasse, 
            String nomeDoAtributo, String valor) {
        
        String jpql = "SELECT a"+dadoDeRetorno+" FROM "+nomeDaClasse+""
                + " a WHERE a"+nomeDoAtributo+" LIKE '"+valor+"%'";
        
        List<String> lista = this.manager.createQuery(jpql, String.class).
                getResultList();
        return lista;
    }
    
    public List<Object> listarIgual(String valor, String nomeDaClasse, 
            String nomeDaColuna, Class classe) {
        
        String jpql = "SELECT a FROM "+nomeDaClasse+""
                + " a WHERE a."+nomeDaColuna+" = '"+valor+"'";
        
        List<Object> lista = this.manager.createQuery(jpql, classe).
                getResultList();
        return lista;
    }
    
public Integer qtdDeRegistros(String entidade, String coluna, String valor) {
        
        String jpql = "SELECT COUNT(a) FROM "+entidade+" a"
        +" WHERE a."+coluna+" = '"+valor+"'";
        
        Long qtdL = this.manager.createQuery(
                jpql, Long.class).getSingleResult();
        Integer qtd = Integer.parseInt(String.valueOf(qtdL));
        return qtd;
    }

}