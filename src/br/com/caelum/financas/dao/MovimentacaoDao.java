package br.com.caelum.financas.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.caelum.financas.exceptiom.ValorInvalidoException;
import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.modelo.Movimentacao;
import br.com.caelum.financas.modelo.TipoMovimentacao;
import br.com.caelum.financas.modelo.ValorPorMesEAno;

@Stateless
public class MovimentacaoDao {

	@PersistenceContext
	EntityManager manager;

	public void adiciona(Movimentacao movimentacao) {
		this.manager.persist(movimentacao);

		if (movimentacao.getValor().compareTo(BigDecimal.ZERO) < 0) {
			throw new ValorInvalidoException("Movimentação Negativa");
		}
	}

	public Movimentacao busca(Integer id) {
		return this.manager.find(Movimentacao.class, id);
	}

	public List<Movimentacao> lista() {
		return this.manager.createQuery("select m from Movimentacao m", Movimentacao.class).getResultList();
	}

	public void remove(Movimentacao movimentacao) {
		Movimentacao movimentacaoParaRemover = this.manager.find(Movimentacao.class, movimentacao.getId());
		this.manager.remove(movimentacaoParaRemover);
	}

	public List<Movimentacao> listaTodasMovimentacoes(Conta conta) {
		String jpql = "SELECT m FROM Movimentacao m WHERE m.conta = :conta ORDER BY m.valor desc";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("conta", conta);
		return query.getResultList();
	}

	public List<Movimentacao> listaPorValorETipo(BigDecimal valor, TipoMovimentacao tipo) {
		String jpql = "SELECT m FROM Movimentacao m WHERE m.valor <= :valor and m.tipoMovimentacao = :tipo";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("valor", valor);
		query.setParameter("tipo", tipo);
		return query.getResultList();
	}

	public BigDecimal calculaTotalMovimentado(Conta conta, TipoMovimentacao tipo) {
		String jpql = "SELECT sum(m.valor) from Movimentacao m WHERE m.conta = :conta and m.tipoMovimentacao = :tipo";
		TypedQuery<BigDecimal> query = this.manager.createQuery(jpql, BigDecimal.class);
		query.setParameter("conta", conta);
		query.setParameter("tipo", tipo);
		return query.getSingleResult();
	}

	public List<Movimentacao> buscaTodasMovimentacoesDaConta(String titular) {
		String jpql = "SELECT m FROM Movimentacao m WHERE m.conta.titular LIKE :titular";
		TypedQuery<Movimentacao> query = this.manager.createQuery(jpql, Movimentacao.class);
		query.setParameter("titular", "%" + titular + "%");
		return query.getResultList();
	}

	public List<ValorPorMesEAno> listaMesesComMovimentacoes(Conta conta, TipoMovimentacao tipo) {
		String jpql = "SELECT new br.com.caelum.financas.modelo.ValorPorMesEAno (month(m.data), year(m.data), sum(m.valor)) FROM Movimentacao m WHERE m.conta = :conta AND m.tipoMovimentacao = :tipo group by year(m.data) ||month(m.data) order by sum(m.valor) desc";
		TypedQuery<ValorPorMesEAno> query = this.manager.createQuery(jpql, ValorPorMesEAno.class);
		query.setParameter("conta", conta);
		query.setParameter("tipo", tipo);
		return query.getResultList();
	}
}
