import javax.swing.JOptionPane;
/**
 * Classe Mensagens, contém métodos para facilitar o trabalho com as caixas de diálogo.
 */
public class Mensagens {

	/** Monta mensagem de erro */
	public void erro(String texto) {
		JOptionPane.showMessageDialog(null, texto, "Erro", JOptionPane.WARNING_MESSAGE);
	}

	/** Monta mensagem */
	public void mensagem(String texto, String titulo) {
		JOptionPane.showMessageDialog(null, texto, titulo, JOptionPane.PLAIN_MESSAGE);
	}

	/** Monta caixa de diálogo com pergunta */
	public int pergunta(String texto, String titulo) {
		Object[] options = { "Sim", "Não" };
		return JOptionPane.showOptionDialog(null, texto, titulo, JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
	}
}
