package Repositorio;

import Entidades.Batalha;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import BancoDeDados.DatabaseConnection;

// Classe responsável por acessar e manipular os dados da entidade Raca no banco de dados
public class RepositorioBatalha {

    // Método para salvar uma instância de Raca no banco de dados
    public void salvarBatalha(Batalha batalha) {
        // Comando SQL para inserir uma nova raça com os valores especificados
        String sql = "INSERT INTO batalhas (lutador1_id, lutador2_id, vencedor_id) VALUES (?, ?, ?)";

        // Tenta conectar ao banco de dados e preparar a execução do comando SQL
        try (Connection conexao = DatabaseConnection.conectar();
             // Prepara a instrução SQL e especifica que as chaves geradas serão retornadas
             PreparedStatement stmt = conexao.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Define os valores dos parâmetros na instrução SQL
            stmt.setInt(1, batalha.getLutador1().getId());
            stmt.setInt(2, batalha.getLutador2().getId());
            stmt.setInt(3, batalha.getVencedor().getId());
            // Executa a inserção no banco de dados
            stmt.executeUpdate();

            // Obtém as chaves geradas pelo banco (neste caso, o ID da raça inserida)
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                // Atribui o ID gerado à instância de Raca
            	batalha.setId(generatedKeys.getInt(1));
            }

            // Mensagem de confirmação de que a raça foi salva com sucesso
            System.out.println("Lutador 1" + batalha.getLutador1() + "Lutador 2 " + batalha.getLutador2() + " salva com ID " + batalha.getId());

        } catch (SQLException e) {
            // Em caso de erro, imprime o stack trace para facilitar a identificação do problema
            e.printStackTrace();
        }
    }

    // Método para buscar todas as raças no banco de dados
    public List<Batalha> buscarTodasBatalhas() {
        // Lista para armazenar as raças encontradas
        List<Batalha> batalha = new ArrayList<>();
        // Comando SQL para selecionar todas as raças
        String sql = "SELECT * FROM batalhas";

        // Tenta conectar ao banco de dados e executar o comando SQL
        try (Connection conexao = DatabaseConnection.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Itera sobre os resultados e cria objetos Raca para cada linha
            while (rs.next()) {
            	Batalha batalhas = new Batalha();
            	batalhas.setId(rs.getInt("id_batalha"));
            	batalhas.getLutador1().setId(rs.getInt("lutador1_id"));
            	batalhas.getLutador2().setId(rs.getInt("lutador2_id"));
            	batalhas.getVencedor().setId(rs.getInt("vencedor_id"));
                
                // Adiciona a raça à lista
            	batalha.add(batalhas);
            }

        } catch (SQLException e) {
            // Em caso de erro, imprime o stack trace para facilitar a identificação do problema
            e.printStackTrace();
        }

        // Retorna a lista de raças encontradas
        return batalha;
    }

    // Método para buscar uma raça específica pelo ID
    public Batalha buscarBatalhaPorId(int id) {
        // Comando SQL para selecionar a raça pelo ID
        String sql = "SELECT * FROM batalhas WHERE id_batalha = ?";
    	Batalha batalha = null;

        // Tenta conectar ao banco de dados e executar o comando SQL
        try (Connection conexao = DatabaseConnection.conectar();
        	PreparedStatement stmt = conexao.prepareStatement(sql)) {

            // Define o valor do parâmetro na instrução SQL
            stmt.setInt(1, id);
            // Executa a consulta e obtém os resultados
            try (ResultSet rs = stmt.executeQuery()) {
                // Verifica se a raça foi encontrada
                if (rs.next()) {
                	batalha = new Batalha();
                	batalha.setId(rs.getInt("id_batalha"));
                	batalha.getLutador1().setId(rs.getInt("lutador1_id"));
                	batalha.getLutador2().setId(rs.getInt("lutador2_id"));
                	batalha.getVencedor().setId(rs.getInt("vencedor_id"));
                }
            }

        } catch (SQLException e) {
            // Em caso de erro, imprime o stack trace para facilitar a identificação do problema
            e.printStackTrace();
        }

        // Retorna a raça encontrada ou null se não existir
        return batalha;
    }
}