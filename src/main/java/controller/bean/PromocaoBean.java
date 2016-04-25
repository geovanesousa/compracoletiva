package controller.bean;

import controller.dao.GenericoDao;
import controller.dao.PromocaoDao;
import model.Cliente;
import model.Compra;
import model.Cupom;
import model.Empresa;
import model.PagamentoCartao;
import model.Promocao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.UploadedFile;

@ManagedBean
@ViewScoped
public class PromocaoBean {

	private Promocao promocao = new Promocao();
	private PagamentoCartao pgCartao = new PagamentoCartao();
	private Compra compra = new Compra();
	private List<Promocao> promocoes = new ArrayList<Promocao>();
	private Map<String, String> cidades = null;
	private Cupom cupom = new Cupom();
	private String codigo = "", titulo = "", cnpj = "", razaoSocial = "";
	private UploadedFile arquivo;

	private EntityManager getEntityManager() {
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) ec.getRequest();
		EntityManager manager = (EntityManager) request.getAttribute("EntityManager");
		return manager;
	}

	public String inserirPromocao() {
		EntityManager manager = this.getEntityManager();
		GenericoDao genericoDao = new GenericoDao(manager);
		try {
			this.promocao.setEmpresa(
					(Empresa) genericoDao.retornaTodosAtributos(this.cnpj, "empresa", "cnpj", Empresa.class));
		} catch (NoResultException e) {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Não existe empresa com esse CNPJ!");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
			return null;
		}
		Integer empresasPorCidade = genericoDao.qtdDeRegistros("empresa", "endereco.cidade",
				this.promocao.getEmpresa().getEndereco().getCidade());
		if (empresasPorCidade > 1) {
			Calendar dataLimite = Calendar.getInstance();
			dataLimite.setTime(this.promocao.getDataInicio());
			this.promocao.setDataFim(dataLimite.getTime());
		} else {
			Calendar dataLimite = Calendar.getInstance();
			dataLimite.setTime(this.promocao.getDataInicio());
			dataLimite.add(Calendar.DATE, 1);
			this.promocao.setDataFim(dataLimite.getTime());
		}
		genericoDao.inserir(this.promocao);
		FacesContext fc = FacesContext.getCurrentInstance();
		FacesMessage fm = new FacesMessage("Inserido com sucesso!");
		fm.setSeverity(FacesMessage.SEVERITY_INFO);
		fc.addMessage(null, fm);
		return null;
	}

	public String cidadePorUf() {
		try {
			EntityManager manager = this.getEntityManager();
			PromocaoDao promocaoDao = new PromocaoDao(manager);
			this.cidades = promocaoDao.listaCidadesPorUf(this.promocao.getEmpresa().getEndereco().getUf());
		} catch (NoResultException e) {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Não existem cidades para essa UF!");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
		}
		return null;
	}

	public String buscarPromocoesPorCnpj() {
		if (!this.cnpj.equals("")) {
			try {
				EntityManager manager = this.getEntityManager();
				PromocaoDao promocaoDao = new PromocaoDao(manager);
				this.promocoes = promocaoDao.listarIgual(this.cnpj, "empresa.cnpj");
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Promocões encontradas!");
				fm.setSeverity(FacesMessage.SEVERITY_INFO);
				fc.addMessage(null, fm);
			} catch (NoResultException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Não existem promoções para esse CNPJ!");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			}
		} else {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Para pesquisar promoções informe CNPJ.");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
		}
		return null;
	}

	public String buscarPromocoesPorRzSocial() {
		if (!this.razaoSocial.equals("")) {
			try {
				EntityManager manager = this.getEntityManager();
				PromocaoDao promocaoDao = new PromocaoDao(manager);
				this.promocoes = promocaoDao.listarIgual(this.razaoSocial, "empresa.razaoSocial");
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Promocões encontradas!");
				fm.setSeverity(FacesMessage.SEVERITY_INFO);
				fc.addMessage(null, fm);
			} catch (NoResultException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Não existem promoções para essa RAZÃO SOCIAL!");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			} catch (NonUniqueResultException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Mais de uma promoção com esse título! Pesquise pelo CÓDIGO.");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			}
		} else {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Para pesquisar promoções informe RAZÃO SOCIAL.");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
		}
		return null;
	}

	public String promocaoDoDia() {
		if (this.promocao.getEmpresa().getEndereco().getCidade() != null
				|| !this.promocao.getEmpresa().getEndereco().getCidade().equals("")) {
			try {
				EntityManager manager = this.getEntityManager();
				PromocaoDao pDao = new PromocaoDao(manager);
				this.promocao = pDao.promocaoDiaCidade(this.promocao.getEmpresa().getEndereco().getCidade());
			} catch (NoResultException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Nenhuma promoção para hoje nessa cidade!");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			} catch (NonUniqueResultException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage(
						"Violação de regra de negócio: " + "Mais de uma promoção para hoje nessa cidade!");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			}

		} else {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Selecione uma cidade!");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
		}
		return null;
	}

	public String buscarPromocaoPorCod() {
		if (!this.codigo.equals("")) {
			try {
				EntityManager manager = this.getEntityManager();
				GenericoDao genericoDao = new GenericoDao(manager);
				this.promocao = (Promocao) genericoDao.retornaTodosAtributos(this.codigo, "promocao", "id",
						Promocao.class);
				this.codigo = String.valueOf(this.promocao.getId());
				this.titulo = this.promocao.getTitulo();
				this.cnpj = this.promocao.getEmpresa().getCnpj();
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Promoção encontrada!");
				fm.setSeverity(FacesMessage.SEVERITY_INFO);
				fc.addMessage(null, fm);
			} catch (NoResultException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Não existe promoção com esse CÓDIGO!");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			}
		} else {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Para pesquisar uma promoção informe CÓDIGO ou TÍTULO.");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
		}
		return null;
	}

	public String buscarPromocaoPorTitulo() {
		if (!this.titulo.equals("")) {
			try {
				EntityManager manager = this.getEntityManager();
				GenericoDao genericoDao = new GenericoDao(manager);
				this.promocao = (Promocao) genericoDao.retornaTodosAtributos(this.titulo, "promocao", "titulo",
						Promocao.class);
				this.codigo = String.valueOf(this.promocao.getId());
				this.titulo = this.promocao.getTitulo();
				this.cnpj = this.promocao.getEmpresa().getCnpj();
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Promoção encontrada!");
				fm.setSeverity(FacesMessage.SEVERITY_INFO);
				fc.addMessage(null, fm);
			} catch (NoResultException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Não existe promoção com esse título!");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			} catch (NonUniqueResultException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Mais de uma promoção com esse título! Pesquise pelo CÓDIGO.");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			}
		} else {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Para pesquisar uma promoção informe CÓDIGO ou TÍTULO.");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
		}
		return null;
	}

	public String atualizarPromocao() {
		EntityManager manager = this.getEntityManager();
		GenericoDao genericoDao = new GenericoDao(manager);
		this.promocao.setTitulo(this.titulo);
		Integer empresasPorCidade = genericoDao.qtdDeRegistros("empresa", "endereco.cidade",
				this.promocao.getEmpresa().getEndereco().getCidade());
		if (empresasPorCidade > 1) {
			Calendar dataLimite = Calendar.getInstance();
			dataLimite.setTime(this.promocao.getDataInicio());
			this.promocao.setDataFim(dataLimite.getTime());
		} else {
			Calendar dataLimite = Calendar.getInstance();
			dataLimite.setTime(this.promocao.getDataInicio());
			dataLimite.add(Calendar.DATE, 1);
			this.promocao.setDataFim(dataLimite.getTime());
		}
		genericoDao.atualizar(this.promocao);
		FacesContext fc = FacesContext.getCurrentInstance();
		FacesMessage fm = new FacesMessage("Atualizada com sucesso!");
		fm.setSeverity(FacesMessage.SEVERITY_INFO);
		fc.addMessage(null, fm);
		return null;
	}

	public String excluirPromocao() {
		if (!this.codigo.equals("")) {
			try {
				EntityManager manager = this.getEntityManager();
				GenericoDao genericoDao = new GenericoDao(manager);
				this.promocao = (Promocao) genericoDao.retornaTodosAtributos(this.codigo, "promocao", "id",
						Promocao.class);
				genericoDao.excluir(this.promocao);
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Excluida com sucesso!");
				fm.setSeverity(FacesMessage.SEVERITY_INFO);
				fc.addMessage(null, fm);
				this.promocao = new Promocao();
				this.codigo = "";
				this.titulo = "";
			} catch (NoResultException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Não existe promoção com esse código!");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			}
		} else if (!this.titulo.equals("")) {
			try {
				EntityManager manager = this.getEntityManager();
				GenericoDao genericoDao = new GenericoDao(manager);
				this.promocao = (Promocao) genericoDao.retornaTodosAtributos(this.titulo, "promocao", "titulo",
						Promocao.class);
				genericoDao.excluir(this.promocao);
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Excluida com sucesso!");
				fm.setSeverity(FacesMessage.SEVERITY_INFO);
				fc.addMessage(null, fm);
				this.promocao = new Promocao();
				this.codigo = "";
				this.titulo = "";
			} catch (NoResultException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Não existe promoção com esse TÍTULO!");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			} catch (NonUniqueResultException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Mais de uma promoção com esse título! Informe o CÓDIGO.");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			}
		} else {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Para excluir uma promoção informe CÓDIGO ou TÍTULO.");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
		}
		return null;
	}

	public String comprar() {
		EntityManager manager = this.getEntityManager();
		FacesContext context = FacesContext.getCurrentInstance();
		Cliente cliente = (Cliente) context.getExternalContext().getSessionMap().get("usuarioLogado");
		this.compra.setCliente(cliente);
		this.compra.setData(Calendar.getInstance().getTime());
		this.compra.setHora(Calendar.getInstance().getTime());
		this.buscarPromocaoPorCod();
		this.compra.setPromocao(this.promocao);
		GenericoDao gDao = new GenericoDao(manager);
		gDao.inserir(this.compra);
		this.pgCartao.setCompra(this.compra);
		this.pgCartao.setData(Calendar.getInstance().getTime());
		this.pgCartao.setHora(Calendar.getInstance().getTime());
		gDao.inserir(this.pgCartao);
		this.cupom.setCompra(this.compra);
		Calendar dataFim = Calendar.getInstance();
		dataFim.add(Calendar.DATE, 9);
		this.cupom.setDataFim(dataFim.getTime());
		Calendar dataInicio = Calendar.getInstance();
		dataFim.add(Calendar.DATE, 2);
		this.cupom.setDataInicio(dataInicio.getTime());
		gDao.inserir(this.cupom);
		return "/restrito/promocao/cupom";
	}

	public List<String> listaPromocoes(String titulo) {
		this.titulo = titulo;
		EntityManager manager = this.getEntityManager();
		GenericoDao genericoDao = new GenericoDao(manager);
		List<String> nomes = genericoDao.listarPorInicio(".titulo", "promocao", ".titulo", titulo);
		return nomes;
	}

	public List<String> listaEmpresas(String cnpj) {
		this.cnpj = cnpj;
		EntityManager manager = this.getEntityManager();
		GenericoDao genericoDao = new GenericoDao(manager);
		List<String> nomes = genericoDao.listarPorInicio(".cnpj", "empresa", ".cnpj", this.cnpj);
		return nomes;
	}

	public String limparCampos() {
		this.promocao = new Promocao();
		this.codigo = "";
		this.titulo = "";
		this.cnpj = "";
		this.cidades = null;
		this.pgCartao = new PagamentoCartao();
		this.cupom = new Cupom();
		this.compra = new Compra();
		return null;
	}

	public String voltar() {
		this.promocao = new Promocao();
		this.codigo = "";
		this.titulo = "";
		this.cnpj = "";
		this.cidades = null;
		this.pgCartao = new PagamentoCartao();
		this.cupom = new Cupom();
		this.compra = new Compra();
		return "/restrito/cliente/bv_cli";
	}

	public Compra getCompra() {
		return compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

	public Cupom getCupom() {
		return cupom;
	}

	public void setCupom(Cupom cupom) {
		this.cupom = cupom;
	}

	public PagamentoCartao getPgCartao() {
		return pgCartao;
	}

	public void setPgCartao(PagamentoCartao pgCartao) {
		this.pgCartao = pgCartao;
	}

	public Map<String, String> getCidades() {
		return cidades;
	}

	public void setCidades(Map<String, String> cidades) {
		this.cidades = cidades;
	}

	public Promocao getPromocao() {
		return promocao;
	}

	public void setPromocao(Promocao promocao) {
		this.promocao = promocao;
	}

	public List<Promocao> getPromocoes() {
		return promocoes;
	}

	public void setPromocoes(List<Promocao> promocoes) {
		this.promocoes = promocoes;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public UploadedFile getArquivo() {
		return arquivo;
	}

	public void setArquivo(UploadedFile arquivo) {
		this.arquivo = arquivo;
	}

}
