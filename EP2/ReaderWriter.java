public class ReaderWriter {
	typedef int semaforo;
	semaforo acessReader = 1; //controla o num de acesso de leitores na base
	semaforo baseControl = 1; //controle o acesso a base de dados
	int numReaders = 0; //numero de processos lendo ou querendo ler

	void reader(void) {
		while(TRUE) {
			lockBase(&acessReader); /*obtem acesso exclusivo a ’numReaders’*/
			numReaders++; /*um leitor a mais agora*/
			if (numReaders == 1) lockBase(&baseControl); /*trava se for o primeiro leitor*/
			freeBase(&acessReader); /*libera acesso a ’numReaders’*/
			readBase(); /*acessa os dados*/
			lockBase(&acessReader); /*obtem acesso exclusivo a ’numReaders’*/
			numReaders--; /*um leitor a menos agora*/
			if (numReaders == 0) freeBase(&baseControl); /*o ultimo destrava a base*/
			freeBase(&acessReader); /*libera acesso a ’numReaders’*/
			useData() /*regiao nao crıtica*/
		}
	}
	void writer(void) {
		while (TRUE) {
			createData(); /*regiao nao crıtica*/
			lockBase(&baseControl); 
			writeData(); /*atualiza a base*/
			freeBase(&baseControl); 
		}	
	}
}
/* quando um leitor chegar e
um escritor estiver esperando, o leitor e suspenso
tambem, em vez de ser admitido imediatamente
Escritores precisam apenas esperar que leitores ativos
completem
Nao precisam esperar por leitores que chegam depois dele
 */
