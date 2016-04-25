package controller.bean;

import controller.dao.GenericoDao;
import model.Cliente;
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
public class ClienteBean {

	private Cliente cliente = new Cliente();
	private String cpf = "", nome = "", senha2 = "";

	private EntityManager getEntityManager() {
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) ec.getRequest();
		EntityManager manager = (EntityManager) request.getAttribute("EntityManager");
		return manager;
	}

	public String inserirCliente() {
		if (this.cliente.getSenha().equals(this.senha2)) {
			EntityManager manager = this.getEntityManager();
			GenericoDao genericoDao = new GenericoDao(manager);
			try {
				genericoDao.inserir(this.cliente.getPessoaFisica());
				genericoDao.inserir(this.cliente);
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Inserido com sucesso!");
				fm.setSeverity(FacesMessage.SEVERITY_INFO);
				fc.addMessage(null, fm);
				return "/index.xhtml";
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

	public String buscarClientePorNome() {
		if (!this.nome.equals("")) {
			try {
				EntityManager manager = this.getEntityManager();
				GenericoDao genericoDao = new GenericoDao(manager);
				this.cliente = (Cliente) genericoDao.retornaTodosAtributos(this.nome, "cliente", "pessoaFisica.nome",
						Cliente.class);
				this.cpf = this.cliente.getPessoaFisica().getCpf();
				this.nome = this.cliente.getPessoaFisica().getNome();
				this.senha2 = this.cliente.getSenha();
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Cliente encontrado!");
				fm.setSeverity(FacesMessage.SEVERITY_INFO);
				fc.addMessage(null, fm);
			} catch (NoResultException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Não existe cliente com esse nome!");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			} catch (NonUniqueResultException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Mais de um cliente com esse nome! Pesquise pelo CPF.");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			}
		} else {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Para pesquisar um cliente informe CPF ou NOME.");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
		}
		return null;
	}

	public String buscarClientePorCpf() {
		if (!this.cpf.equals("")) {
			try {
				EntityManager manager = this.getEntityManager();
				GenericoDao genericoDao = new GenericoDao(manager);
				this.cliente = (Cliente) genericoDao.retornaTodosAtributos(this.cpf, "cliente", "pessoaFisica.cpf",
						Cliente.class);
				this.cpf = this.cliente.getPessoaFisica().getCpf();
				this.nome = this.cliente.getPessoaFisica().getNome();
				this.senha2 = this.cliente.getSenha();
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Cliente encontrado!");
				fm.setSeverity(FacesMessage.SEVERITY_INFO);
				fc.addMessage(null, fm);
			} catch (NoResultException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Não existe cliente com esse CPF!");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			}
		} else {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Para pesquisar um cliente informe CPF ou NOME.");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
		}
		return null;
	}

	public String atualizarCliente() {
		EntityManager manager = this.getEntityManager();
		GenericoDao genericoDao = new GenericoDao(manager);
		this.cliente.getPessoaFisica().setCpf(this.cpf);
		this.cliente.getPessoaFisica().setNome(this.nome);
		try {
			genericoDao.atualizar(this.cliente.getPessoaFisica());
			genericoDao.atualizar(this.cliente);
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
		return null;
	}

	public String excluirCliente() {
		if (!this.cpf.equals("")) {
			try {
				EntityManager manager = this.getEntityManager();
				GenericoDao genericoDao = new GenericoDao(manager);
				this.cliente = (Cliente) genericoDao.retornaTodosAtributos(this.cpf, "cliente", "pessoaFisica.cpf",
						Cliente.class);
				genericoDao.excluir(this.cliente);
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Excluido com sucesso!");
				fm.setSeverity(FacesMessage.SEVERITY_INFO);
				fc.addMessage(null, fm);
				this.cliente = new Cliente();
				this.cpf = "";
				this.nome = "";
				this.senha2 = "";
			} catch (NoResultException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Não existe cliente com esse CPF!");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			}
		} else if (!this.nome.equals("")) {
			try {
				EntityManager manager = this.getEntityManager();
				GenericoDao genericoDao = new GenericoDao(manager);
				this.cliente = (Cliente) genericoDao.retornaTodosAtributos(this.nome, "cliente", "pessoaFisica.nome",
						Cliente.class);
				genericoDao.excluir(this.cliente);
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Excluido com sucesso!");
				fm.setSeverity(FacesMessage.SEVERITY_INFO);
				fc.addMessage(null, fm);
				this.cliente = new Cliente();
				this.cpf = "";
				this.nome = "";
				this.senha2 = "";
			} catch (NoResultException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Não existe cliente com esse nome!");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			} catch (NonUniqueResultException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Mais de um cliente com esse nome! Informe o CPF.");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			}
		} else {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Para excluir um cliente informe CPF ou NOME.");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
		}
		return null;
	}

	public List<String> listaClientes(String nome) {
		this.nome = nome;
		EntityManager manager = this.getEntityManager();
		GenericoDao genericoDao = new GenericoDao(manager);
		List<String> nomes = genericoDao.listarPorInicio(".pessoaFisica.nome", "cliente", ".pessoaFisica.nome", nome);
		return nomes;
	}

	public String limparCampos() {
		this.cliente = new Cliente();
		this.cpf = "";
		this.nome = "";
		this.senha2 = "";
		return null;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
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
