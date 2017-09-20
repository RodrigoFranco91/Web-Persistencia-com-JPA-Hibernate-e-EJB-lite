package br.com.caelum.financas.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

//@Stateless
@Singleton
@Startup
public class Agendador {

	private static int totalCriado;
	
	@Resource
	private TimerService timeService;

	public void executa() {
		System.out.printf("%d instancias criadas %n", totalCriado);

		// simulando demora de 4s na execucao
		try {
			System.out.printf("Executando %s %n", this);
			Thread.sleep(4000);
		} catch (InterruptedException e) {
		}
	}
	
	@PostConstruct
	void posConstruct(){
		System.out.println("Criando agendador");
		totalCriado++;
	}
	
	@PreDestroy
	void preDestruicao(){
		System.out.println("Destruindo agendador");
	}
	
	public void agenda(String expressaoMinutos, String expressaoSegundos){
		ScheduleExpression expression = new ScheduleExpression();
		expression.hour("*");
		expression.minute(expressaoMinutos);
		expression.second(expressaoSegundos);
		
		TimerConfig config = new TimerConfig();
		config.setInfo(expression.toString());
		config.setPersistent(false);
		
		this.timeService.createCalendarTimer(expression, config);
		
		System.out.println("Agendamento: " + expression);
	}
	
	@Timeout
	public void verificacaoPeriodicaSeHaNovasContas(Timer timer){
		System.out.println(timer.getInfo());
		//aqui poderiamos acessar o BD com JPA para verificar as contas
	}
	
	//Usar anotacao no lugar de criar um EXPRESSION
	@Schedule(hour="*", minute="*/1", second="0", dayOfWeek="Mon", persistent=false)
	public void enviaEmailCadaMinutoComInformacoesDasUltimasMovimentacoes(){
		System.out.println("Envia e-mail a cada minuto");
	}

}
