package controller.bean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import controller.dao.GenericoDao;
import model.Cliente;
import model.Consultor;
import model.Empresa;

@ManagedBean
@ViewScoped
public class LoginBean {

	private String login = "", senha = "";
	private EntityManager getEntityManager() {
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) ec.getRequest();
		EntityManager manager = (EntityManager) request.getAttribute("EntityManager");
		return manager;
	}

	public String autenticar() {
		try {
			EntityManager manager = this.getEntityManager();
			GenericoDao genericoDao = new GenericoDao(manager);
			Consultor consultor = (Consultor) genericoDao.retornaTodosAtributos("11111111111", "consultor",
					"pessoaFisica.cpf", Consultor.class);
		} catch (NoResultException e) {
			EntityManager manager = this.getEntityManager();
			GenericoDao genericoDao = new GenericoDao(manager);
			Consultor c = new Consultor();
			c.getPessoaFisica().setCpf("11111111111");
			c.getPessoaFisica().setNome("Consultor");
			c.setSenha("12345");
			genericoDao.inserir(c.getPessoaFisica());
			genericoDao.inserir(c);
		}
		EntityManager manager = this.getEntityManager();
		try {
			Cliente cliente = (Cliente) new GenericoDao(manager).retornaTodosAtributos(this.login, "cliente",
					"pessoaFisica.cpf", Cliente.class);
			FacesContext fc = FacesContext.getCurrentInstance();
			if (cliente.getSenha().equals(this.senha)) {
				ExternalContext ec = fc.getExternalContext();
				HttpSession session = (HttpSession) ec.getSession(false);
				session.setAttribute("usuarioLogado", cliente);
				return "/restrito/cliente/bv_cli";
			} else {
				FacesMessage fm = new FacesMessage("senha inválida");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
				return null;
			}
		} catch (NoResultException e) {
			try {
				Empresa empresa = (Empresa) new GenericoDao(manager).retornaTodosAtributos(this.login, "empresa",
						"cnpj", Empresa.class);
				FacesContext fc = FacesContext.getCurrentInstance();
				if (empresa.getSenha().equals(this.senha)) {
					ExternalContext ec = fc.getExternalContext();
					HttpSession session = (HttpSession) ec.getSession(false);
					session.setAttribute("usuarioLogado", empresa);
					return "/restrito/empresa/bv_emp";
				} else {
					FacesMessage fm = new FacesMessage("senha inválida");
					fm.setSeverity(FacesMessage.SEVERITY_ERROR);
					fc.addMessage(null, fm);
					return null;
				}
			} catch (NoResultException erro) {
				try {
					Consultor consultor = (Consultor) new GenericoDao(manager).retornaTodosAtributos(this.login,
							"consultor", "pessoaFisica.cpf", Consultor.class);
					FacesContext fc = FacesContext.getCurrentInstance();
					if (consultor.getSenha().equals(this.senha)) {
						ExternalContext ec = fc.getExternalContext();
						HttpSession session = (HttpSession) ec.getSession(false);
						session.setAttribute("usuarioLogado", consultor);
						return "/restrito/consultor/bv_con";
					} else {
						FacesMessage fm = new FacesMessage("senha inválida");
						fm.setSeverity(FacesMessage.SEVERITY_ERROR);
						fc.addMessage(null, fm);
						return null;
					}
				} catch (NoResultException erro3) {
					FacesContext fc = FacesContext.getCurrentInstance();
					FacesMessage fm = new FacesMessage("nenhuma resultado encontrado");
					fm.setSeverity(FacesMessage.SEVERITY_ERROR);
					fc.addMessage(null, fm);
				}
			}
		}
		return null;
	}
	

	public String registraSaida() {
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		HttpSession session = (HttpSession) ec.getSession(false);
		session.removeAttribute("usuarioLogado");
		session.invalidate();  
		return "/index.xhtml";
	}
	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
