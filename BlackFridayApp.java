package ex3modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BlackFridayApp {
	public Connection con = null;
	public PreparedStatement st = null;
	public ResultSet rs = null;

	private final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private final String DATABASENAME = "prova";
	private final String URL = "jdbc:sqlserver://localhost:1433;databasename=" + DATABASENAME;
	private final String LOGIN = "usuario";
	private final String SENHA = "fatec";

	public boolean getConnection() {
		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, LOGIN, SENHA);
			System.out.println("Conectou!");
			return true;
		} catch (SQLException erro) {
			System.out.println("Falha na conexão! " + erro);
			return false;
		} catch (ClassNotFoundException erro) {
			System.out.println("Driver não encontrado!");
			return false;
		}
	}

	public void close() {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
		}

		try {
			if (st != null)
				st.close();
		} catch (SQLException e) {
		}
		try {
			if (con != null) {
				con.close();
				System.out.println("Desconectou!");
			}
		} catch (SQLException e) {
		}
	}

	public void inserirRegistro(int id, String produto, double precoOriginal, double valorDesconto) {
		String sql = "INSERT INTO blackfriday (id, produto, preco_original, valor_desconto) VALUES (?, ?, ?, ?)";
		try {
			if (getConnection()) {
				st = con.prepareStatement(sql);
				st.setInt(1, id);
				st.setString(2, produto);
				st.setDouble(3, precoOriginal);
				st.setDouble(4, valorDesconto);
				int affectedRows = st.executeUpdate();
				if (affectedRows > 0) {
					System.out.println("Registro inserido com sucesso.");
				} else {
					System.out.println("Falha ao inserir o registro.");
				}
			}
		} catch (SQLException e) {
			System.out.println("Erro ao inserir registro: " + e.getMessage());
		} finally {
			close();
		}
	}

	public void listarRegistros() {
		String sql = "SELECT *, (preco_original - valor_desconto) AS preco_final FROM blackfriday";
		try {
			if (getConnection()) {
				st = con.prepareStatement(sql);
				rs = st.executeQuery();
				while (rs.next()) {
					System.out.println("Produto: " + rs.getString("produto"));
					System.out.println("Preço Original: " + rs.getDouble("preco_original"));
					System.out.println("Valor Desconto: " + rs.getDouble("valor_desconto"));
					System.out.println("Preço Final: " + rs.getDouble("preco_final"));
					System.out.println();
				}
			}
		} catch (SQLException e) {
			System.out.println("Erro ao listar registros: " + e.getMessage());
		} finally {
			close();
		}
	}

	public void listarRegistrosComDescontoSuperior(double valorDesconto) {
		String sql = "SELECT * FROM blackfriday WHERE valor_desconto > ?";
		try {
			if (getConnection()) {
				st = con.prepareStatement(sql);
				st.setDouble(1, valorDesconto);
				rs = st.executeQuery();
				while (rs.next()) {
					System.out.println("Produto: " + rs.getString("produto"));
					System.out.println("Preço Original: " + rs.getDouble("preco_original"));
					System.out.println("Valor Desconto: " + rs.getDouble("valor_desconto"));
					System.out.println(
							"Preço Final: " + (rs.getDouble("preco_original") - rs.getDouble("valor_desconto")));
					System.out.println();
				}
			}
		} catch (SQLException e) {
			System.out.println("Erro ao listar registros: " + e.getMessage());
		} finally {
			close();
		}
	}

	public void listarRegistrosComPercentualDesconto() {
		String sql = "SELECT *, (valor_desconto / preco_original) * 100 AS percentual_desconto FROM blackfriday";
		try {
			if (getConnection()) {
				st = con.prepareStatement(sql);
				rs = st.executeQuery();
				while (rs.next()) {
					System.out.println("Produto: " + rs.getString("produto"));
					System.out.println("Preço Original: " + rs.getDouble("preco_original"));
					System.out.println("Valor Desconto: " + rs.getDouble("valor_desconto"));
					System.out.println("Percentual Desconto: " + rs.getDouble("percentual_desconto") + "%");
					System.out.println();
				}
			}
		} catch (SQLException e) {
			System.out.println("Erro ao listar registros: " + e.getMessage());
		} finally {
			close();
		}
	}

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
