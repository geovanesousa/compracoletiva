package controller.bean;

import controller.dao.GenericoDao;
import model.Empresa;

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
public class EmpresaBean {

	private Empresa empresa = new Empresa();
	private String cnpj = "", rzsocial = "", senha2 = "";

	private EntityManager getEntityManager() {
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) ec.getRequest();
		EntityManager manager = (EntityManager) request.getAttribute("EntityManager");
		return manager;
	}

	public String inserirEmpresa() {
		if (this.empresa.getSenha().equals(this.senha2)) {
			EntityManager manager = this.getEntityManager();
			GenericoDao genericoDao = new GenericoDao(manager);
			try {
				genericoDao.retornaTodosAtributos(this.empresa.getCnpj(), 
						"empresa", "cnpj", Empresa.class);
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("CNPJ já cadastrado!");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			} catch (NoResultException e) {
				genericoDao.inserir(this.empresa.getEndereco());
				genericoDao.inserir(this.empresa);
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Inserida com sucesso!");
				fm.setSeverity(FacesMessage.SEVERITY_INFO);
				fc.addMessage(null, fm);
			}

		} else {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("As senhas não correspondem!");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
		}
		return null;
	}

	public String buscarEmpresaPorRazaoSocial() {
		if (!this.rzsocial.equals("")) {
			try {
				EntityManager manager = this.getEntityManager();
				GenericoDao genericoDao = new GenericoDao(manager);
				this.empresa = (Empresa) genericoDao.retornaTodosAtributos(this.rzsocial, "empresa", 
						"razaoSocial", Empresa.class);
				this.cnpj = this.empresa.getCnpj();
				this.rzsocial = this.empresa.getRazaoSocial();
				this.senha2 = this.empresa.getSenha();
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Empresa encontrada!");
				fm.setSeverity(FacesMessage.SEVERITY_INFO);
				fc.addMessage(null, fm);
			} catch (NoResultException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Não existe empresa com essa razão social!");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			} catch (NonUniqueResultException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Mais de uma empresa com essa razão social! Pesquise pelo CNPJ.");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			}
		} else {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Para pesquisar uma empresa informe CNPJ ou RAZÃO SOCIAL.");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
		}
		return null;
	}

	public String buscarEmpresaPorCnpj() {
		if (!this.cnpj.equals("")) {
			try {
				EntityManager manager = this.getEntityManager();
				GenericoDao genericoDao = new GenericoDao(manager);
				this.empresa = (Empresa) genericoDao.retornaTodosAtributos(this.cnpj, "empresa", "cnpj", 
						Empresa.class);	
				this.cnpj = this.empresa.getCnpj();
				this.rzsocial = this.empresa.getRazaoSocial();
				this.senha2 = this.empresa.getSenha();
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Empresa encontrada!");
				fm.setSeverity(FacesMessage.SEVERITY_INFO);
				fc.addMessage(null, fm);
			} catch (NoResultException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Não existe empresa com esse CNPJ!");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			}
		} else {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Para pesquisar uma empresa informe CNPJ ou RAZÃO SOCIAL.");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
		}
		return null;
	}

	public String atualizarEmpresa() {
		EntityManager manager = this.getEntityManager();
		GenericoDao genericoDao = new GenericoDao(manager);
		this.empresa.setCnpj(this.cnpj);
		this.empresa.setRazaoSocial(this.rzsocial);
		try {
			genericoDao.atualizar(this.empresa.getEndereco());
			genericoDao.atualizar(this.empresa);
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

	public String excluirEmpresa() {
		if (!this.cnpj.equals("")) {
			try {
				EntityManager manager = this.getEntityManager();
				GenericoDao genericoDao = new GenericoDao(manager);
				this.empresa = (Empresa) genericoDao.retornaTodosAtributos(this.cnpj, "empresa", "cnpj",
						Empresa.class);
				genericoDao.excluir(this.empresa);
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Excluida com sucesso!");
				fm.setSeverity(FacesMessage.SEVERITY_INFO);
				fc.addMessage(null, fm);
				this.empresa = new Empresa();
				this.cnpj = "";
				this.rzsocial = "";
				this.senha2 = "";
			} catch (NoResultException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Não existe empresa com esse CNPJ!");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			}
		} else if (!this.rzsocial.equals("")) {
			try {
				EntityManager manager = this.getEntityManager();
				GenericoDao genericoDao = new GenericoDao(manager);
				this.empresa = (Empresa) genericoDao.retornaTodosAtributos(this.rzsocial, "empresa", "razaoSocial",
						Empresa.class);
				genericoDao.excluir(this.empresa);
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Excluida com sucesso!");
				fm.setSeverity(FacesMessage.SEVERITY_INFO);
				fc.addMessage(null, fm);
				this.empresa = new Empresa();
				this.cnpj = "";
				this.rzsocial = "";
				this.senha2 = "";
			} catch (NoResultException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Não existe empresa com essa razão social!");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			} catch (NonUniqueResultException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Mais de uma empresa com essa razão social! Informe o CNPJ.");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			}
		} else {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Para excluir uma empresa informe CNPJ ou RAZÃO SOCIAL.");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
		}
		return null;
	}

	public List<String> listaEmpresas(String rzsocial) {
		this.rzsocial = rzsocial;
		EntityManager manager = this.getEntityManager();
		GenericoDao genericoDao = new GenericoDao(manager);
		List<String> nomes = genericoDao.listarPorInicio(".razaoSocial", "empresa", ".razaoSocial", rzsocial);
		return nomes;
	}

	public String limparCampos() {
		this.empresa = new Empresa();
		this.cnpj = "";
		this.rzsocial = "";
		this.senha2 = "";
		return null;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getRzsocial() {
		return rzsocial;
	}

	public void setRzsocial(String rzsocial) {
		this.rzsocial = rzsocial;
	}

	public String getSenha2() {
		return senha2;
	}

	public void setSenha2(String senha2) {
		this.senha2 = senha2;
	}

}
