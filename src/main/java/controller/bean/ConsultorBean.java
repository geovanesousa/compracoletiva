package controller.bean;

import controller.dao.GenericoDao;
import model.Consultor;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@ViewScoped
public class ConsultorBean {

	private Consultor consultor = new Consultor();
	private String cpf = "", nome = "", senha2 = "";

	private EntityManager getEntityManager() {
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) ec.getRequest();
		EntityManager manager = (EntityManager) request.getAttribute("EntityManager");
		return manager;
	}

	public String inserirConsultor() {
		if (this.consultor.getSenha().equals(this.senha2)) {
			EntityManager manager = this.getEntityManager();
			GenericoDao genericoDao = new GenericoDao(manager);
			try {
				genericoDao.inserir(this.consultor.getPessoaFisica());
				genericoDao.inserir(this.consultor);
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Inserido com sucesso!");
				fm.setSeverity(FacesMessage.SEVERITY_INFO);
				fc.addMessage(null, fm);
			} catch (PersistenceException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("CPF já cadastrado!");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
				return null;
			}
		} else {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("As senhas não correspondem!");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
		}
		return null;
	}

	public String buscarConsultorPorNome() {
		if (!this.nome.equals("")) {
			try {
				EntityManager manager = this.getEntityManager();
				GenericoDao genericoDao = new GenericoDao(manager);
				this.consultor = (Consultor) genericoDao.retornaTodosAtributos(this.nome, "consultor",
						"pessoaFisica.nome", Consultor.class);
				this.cpf = this.consultor.getPessoaFisica().getCpf();
				this.nome = this.consultor.getPessoaFisica().getNome();
				this.senha2 = this.consultor.getSenha();
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Consultor encontrado!");
				fm.setSeverity(FacesMessage.SEVERITY_INFO);
				fc.addMessage(null, fm);
			} catch (NoResultException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Não existe consultor com esse nome!");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			} catch (NonUniqueResultException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Mais de um consultor com esse nome! Pesquise pelo CPF.");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			}
		} else {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Para pesquisar um consultor informe CPF ou NOME.");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
		}
		return null;
	}

	public String buscarConsultorPorCpf() {
		if (!this.cpf.equals("")) {
			try {
				EntityManager manager = this.getEntityManager();
				GenericoDao genericoDao = new GenericoDao(manager);
				this.consultor = (Consultor) genericoDao.retornaTodosAtributos(this.cpf, "consultor",
						"pessoaFisica.cpf", Consultor.class);
				this.cpf = this.consultor.getPessoaFisica().getCpf();
				this.nome = this.consultor.getPessoaFisica().getNome();
				this.senha2 = this.consultor.getSenha();
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Consultor encontrado!");
				fm.setSeverity(FacesMessage.SEVERITY_INFO);
				fc.addMessage(null, fm);
			} catch (NoResultException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Não existe consultor com esse CPF!");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			}
		} else {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Para pesquisar um consultor informe CPF ou NOME.");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
		}
		return null;
	}

	public String atualizarConsultor() {
		EntityManager manager = this.getEntityManager();
		GenericoDao genericoDao = new GenericoDao(manager);
		this.consultor.getPessoaFisica().setCpf(this.cpf);
		this.consultor.getPessoaFisica().setNome(this.nome);
		if(this.consultor.getSenha().equals(this.senha2)){
		try {
			genericoDao.atualizar(this.consultor.getPessoaFisica());
			genericoDao.atualizar(this.consultor);
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Atualizado com sucesso!");
			fm.setSeverity(FacesMessage.SEVERITY_INFO);
			fc.addMessage(null, fm);
		} catch (PersistenceException e) {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("CPF já cadastrado!");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
		}
		}else{
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("As senhas não correspondem!");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
		}
		return null;
	}

	public String excluirConsultor() {
		if (!this.cpf.equals("")) {
			try {
				EntityManager manager = this.getEntityManager();
				GenericoDao genericoDao = new GenericoDao(manager);
				this.consultor = (Consultor) genericoDao.retornaTodosAtributos(this.cpf, "consultor",
						"pessoaFisica.cpf", Consultor.class);
				genericoDao.excluir(this.consultor);
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Excluido com sucesso!");
				fm.setSeverity(FacesMessage.SEVERITY_INFO);
				fc.addMessage(null, fm);
				this.consultor = new Consultor();
				this.cpf = "";
				this.nome = "";
				this.senha2 = "";
			} catch (NoResultException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Não existe consultor com esse CPF!");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			}
		} else if (!this.nome.equals("")) {
			try {
				EntityManager manager = this.getEntityManager();
				GenericoDao genericoDao = new GenericoDao(manager);
				this.consultor = (Consultor) genericoDao.retornaTodosAtributos(this.nome, "consultor",
						"pessoaFisica.nome", Consultor.class);
				genericoDao.excluir(this.consultor);
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Excluido com sucesso!");
				fm.setSeverity(FacesMessage.SEVERITY_INFO);
				fc.addMessage(null, fm);
				this.consultor = new Consultor();
				this.cpf = "";
				this.nome = "";
				this.senha2 = "";
			} catch (NoResultException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Não existe consultor com esse nome!");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			} catch (NonUniqueResultException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Mais de um consultor com esse nome! Informe o CPF.");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			}
		} else {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Para excluir um consultor informe CPF ou NOME.");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
		}
		return null;
	}

	public List<String> listaConsultores(String nome) {
		this.nome = nome;
		EntityManager manager = this.getEntityManager();
		GenericoDao genericoDao = new GenericoDao(manager);
		List<String> nomes = genericoDao.listarPorInicio(".pessoaFisica.nome", "consultor", ".pessoaFisica.nome", nome);
		return nomes;
	}

	public String limparCampos() {
		this.consultor = new Consultor();
		this.cpf = "";
		this.nome = "";
		this.senha2 = "";
		return null;
	}

	public Consultor getConsultor() {
		return consultor;
	}

	public void setConsultor(Consultor consultor) {
		this.consultor = consultor;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha2() {
		return senha2;
	}

	public void setSenha2(String senha2) {
		this.senha2 = senha2;
	}

}
