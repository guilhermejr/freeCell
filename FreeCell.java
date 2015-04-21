/**
 * Classe principal onde o jogo e a Thread s�o iniciados.
 */
public class FreeCell {
	// --- M�todo main --------------------------------------------------------
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