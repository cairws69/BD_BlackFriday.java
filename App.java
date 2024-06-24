package ex3modelo;

public class App {
	public static void main(String[] args) {
		BlackFridayApp bd = new BlackFridayApp();

		// Inserir um registro
		bd.inserirRegistro(1, "Produto A", 100.0, 20.0);

		// Listar todos os registros
		bd.listarRegistros();

		// Listar registros com valor de desconto superior a 15.0
		bd.listarRegistrosComDescontoSuperior(15.0);

		// Listar registros com percentual de desconto
		bd.listarRegistrosComPercentualDesconto();
	}
}
