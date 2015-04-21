/**
 * Classe principal onde o jogo e a Thread são iniciados.
 */
public class FreeCell {
	// --- Método main --------------------------------------------------------
	public static void main(String[] args) {

		// --- Instancia o Jogo ---
		Tabuleiro fc = new Tabuleiro ();

		// --- instancia a thread ---
		Thread t = new Thread(fc);

		// --- Inicia Thread ---
		t.start();

		// --- Inicia o jogo ---
		fc.display();
	}
}