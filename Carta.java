import javax.swing.ImageIcon;
import javax.swing.JLabel;
/**
 * Classe Carta, nela tem todas as informações da carta: valor, naipe, cor, coordenada x, coordena y, imagem da carta inteira e imagem da carta cortada.
 */
public class Carta {
	/** Valor da carta */
	private int valor;
	/** Naipe da carta */
	private String naipe;
	/** Cor da carta */
	private String cor;
	/** Coordenada x da carta */
	private int x;
	/** Coordenada y da carta */
	private int y;
	/** JLabel que recebe a imagem da carta cortada */
	private JLabel imagemCartaCortada = new JLabel();
	/** JLabelk que recebe a imagem da carta inteira */
	private JLabel imagemCartaInteira = new JLabel();

	// --- Método construtor --------------------------------------------------
	public Carta (String nomeImagem, int valor, String naipe, String cor) {
		this.imagemCartaCortada = new JLabel(new ImageIcon("cartas/"+ nomeImagem +".gif"));
		this.imagemCartaInteira = new JLabel(new ImageIcon("cartas/"+ nomeImagem +"_grande.gif"));
		this.valor = valor;
		this.naipe = naipe;
		this.cor = cor;
		this.x = 0;
		this.y = 0;
	}

	/** Recupera a cor da carta */
	public String getCor() {
		return cor;
	}

	/** Recupera a JLabel com a carta cortada */
	public JLabel getImagemCartaCortada() {
		return imagemCartaCortada;
	}

	/** Recupera a JLabel com a carta inteira */
	public JLabel getImagemCartaInteira() {
		return imagemCartaInteira;
	}

	/** Recupera o naipe da carta */
	public String getNaipe() {
		return naipe;
	}

	/** Recupera o valor da carta */
	public int getValor() {
		return valor;
	}

	/** Recupera a coordenada x */
	public int getX() {
		return x;
	}

	/** Seta uma nova coordenada x */
	public void setX(int x) {
		this.x = x;
	}

	/** Recupera a coordenada y */
	public int getY() {
		return y;
	}

	/** Seta uma nova coordenada y */
	public void setY(int y) {
		this.y = y;
	}
}
