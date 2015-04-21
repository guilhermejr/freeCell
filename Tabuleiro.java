import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
/**
 * Classe Tabuleiro, contém os métodos de montagem do tabuleiro, embaralhamento das cartas, movimentação das cartas, identifica o fim do jogo, inicia a janela e o run quem inicia a Trhead
 */
public class Tabuleiro extends JFrame implements Runnable, ActionListener {
	// -- Declara a barra de menu ---
	private JMenuBar menuBar = new JMenuBar();

	// --- Declara os itens do menu ---
	private JMenu menuJogo = new JMenu("Jogo");
	private JMenu menuAjuda = new JMenu("Ajuda");

	// --- Declara os subitens do menu ---
	private JMenuItem menuItemNovo = new JMenuItem("Novo Jogo");
	private JMenuItem menuItemMelhores = new JMenuItem("Melhores tempos");
	private JMenuItem menuItemSair = new JMenuItem("Sair");
	private JMenuItem menuItemAjuda = new JMenuItem("Ajuda");
	private JMenuItem menuItemSobre = new JMenuItem("Sobre");

	// --- Declara as cartas ---
	private Carta[] carta = new Carta[52];

	// --- Declara as células ---
	private Carta[] celula = new Carta[8];

	// --- Declara colunas vazias ---
	private Carta[] colunaVazia = new Carta[8];

	// --- Label com o tempo de jogo ---
	private JLabel tempo = new JLabel();

	// --- Label com a quantidade de cartas faltando ---
	private JLabel contador = new JLabel();

	// --- Cria a estrutura de pilha para as cartas ---
	private Pilha cartaPilha1 = new Pilha();
	private Pilha cartaPilha2 = new Pilha();
	private Pilha cartaPilha3 = new Pilha();
	private Pilha cartaPilha4 = new Pilha();
	private Pilha cartaPilha5 = new Pilha();
	private Pilha cartaPilha6 = new Pilha();
	private Pilha cartaPilha7 = new Pilha();
	private Pilha cartaPilha8 = new Pilha();

	// --- Cria a estrutura de pilha para as células ---
	private Pilha celulaPilha1 = new Pilha();
	private Pilha celulaPilha2 = new Pilha();
	private Pilha celulaPilha3 = new Pilha();
	private Pilha celulaPilha4 = new Pilha();
	private Pilha celulaPilha5 = new Pilha();
	private Pilha celulaPilha6 = new Pilha();
	private Pilha celulaPilha7 = new Pilha();
	private Pilha celulaPilha8 = new Pilha();

	// --- Gerador de número aleatório ---
	private Random numeroAleatorio = new Random ();

	// --- Instancia class de mensagens ---
	private Mensagens msg = new Mensagens();

	// --- Altura inicial das colunas ---
	private static final int INICIO = 130;

	// --- Variáveis para checar o fim do jogo ---
	private boolean copas = false;
	private boolean paus = false;
	private boolean ouro = false;
	private boolean espada = false;

	// --- Variáveis para movimentação ---
	private boolean click = false;
	private Pilha primeiraPilha;

	// --- Segundos ---
	private int segundo = 0;

	// --- Contador ---
	private int contadorCartas = 52;

	// --- Dados da conexao ---
    private static final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String url = "jdbc:derby:db";
    Connection conexao = null;

    // --- Reinício do jogo ---
    private boolean reinicio = false;

    // --- Para o contador da Thread seo jogo for reiniciado ---
    private boolean paraThread = false;

	// --- Construtor ---------------------------------------------------------
	public Tabuleiro() {
		// --- Faz a conexao com o banco de dados ---
		try {
	        Class.forName(driver);
	        conexao = DriverManager.getConnection(url);
	    } catch (Exception e) {
	        System.exit(0);
	    }

		// --- Posiciona a label como tempo ---
		add(tempo);
		tempo.setText("FreeCell "+ segundo +" segundos");
		tempo.setBounds(20, 520, 200, 20);

		// --- Posiciona a label com o contador ---
		add(contador);
		contador.setText("Faltam "+ contadorCartas +" cartas");
		contador.setBounds(550, 520, 100, 20);

		// --- Monta menu ---
		// --- Novo ---
		menuJogo.add(menuItemNovo);
		menuItemNovo.addActionListener(this);
		// --- Melhores tempos ---
		menuJogo.add(menuItemMelhores);
		menuItemMelhores.addActionListener(this);
		// --- Sair ---
		menuJogo.add(menuItemSair);
		menuItemSair.addActionListener(this);
		// --- Ajuda ---
		menuAjuda.add(menuItemAjuda);
		menuItemAjuda.addActionListener(this);
		// --- Sobre ---
		menuAjuda.add(menuItemSobre);
		menuItemSobre.addActionListener(this);
		// --- Barra de menu ---
		menuBar.add(menuJogo);
		menuBar.add(menuAjuda);
		setJMenuBar(menuBar);

		// --- Chama método que monta o tabuleiro ---
		montaTabuleiro ();
	}

	/** Monta o tabuleiro */
	public void montaTabuleiro () {
		// --- Variável para percorrer os for ---
		int i;

		// --- Se o jogo for reiniciado esvazia todas as pilhas ---
		if (reinicio) {
			while (!cartaPilha1.isEmpty())
				cartaPilha1.pop();
			while (!cartaPilha2.isEmpty())
				cartaPilha2.pop();
			while (!cartaPilha3.isEmpty())
				cartaPilha3.pop();
			while (!cartaPilha4.isEmpty())
				cartaPilha4.pop();
			while (!cartaPilha5.isEmpty())
				cartaPilha5.pop();
			while (!cartaPilha6.isEmpty())
				cartaPilha6.pop();
			while (!cartaPilha7.isEmpty())
				cartaPilha7.pop();
			while (!cartaPilha8.isEmpty())
				cartaPilha8.pop();
			while (!celulaPilha1.isEmpty())
				celulaPilha1.pop();
			while (!celulaPilha2.isEmpty())
				celulaPilha2.pop();
			while (!celulaPilha3.isEmpty())
				celulaPilha3.pop();
			while (!celulaPilha4.isEmpty())
				celulaPilha4.pop();
			while (!celulaPilha5.isEmpty())
				celulaPilha5.pop();
			while (!celulaPilha6.isEmpty())
				celulaPilha6.pop();
			while (!celulaPilha7.isEmpty())
				celulaPilha7.pop();
			while (!celulaPilha8.isEmpty())
				celulaPilha8.pop();
			while (!primeiraPilha.isEmpty())
				primeiraPilha.pop();

			// --- Remove antigas cartas do tabuleiro ---
			for (i = 0; i < carta.length; i++) {
				remove(carta[i].getImagemCartaCortada());
				remove(carta[i].getImagemCartaInteira());
			}

			// --- Remove antigas células do tabuleiro ---
			for (i = 0; i < celula.length; i++) {
				remove(celula[i].getImagemCartaCortada());
				remove(celula[i].getImagemCartaInteira());
			}

			// --- Remove antigas vazias do tabuleiro ---
			for (i = 0; i < colunaVazia.length; i++) {
				remove(colunaVazia[i].getImagemCartaCortada());
				remove(colunaVazia[i].getImagemCartaInteira());
			}
		}

		// --- Instancia valores de cada carta ---
		carta[0] = new Carta("1", 1, "copas", "vermelho");
		carta[1] = new Carta("2", 1, "espada", "preto");
		carta[2] = new Carta("3", 1, "ouro", "vermelho");
		carta[3] = new Carta("4", 1, "paus", "preto");
		carta[4] = new Carta("5", 2, "copas", "vermelho");
		carta[5] = new Carta("6", 2, "espada", "preto");
		carta[6] = new Carta("7", 2, "ouro", "vermelho");
		carta[7] = new Carta("8", 2, "paus", "preto");
		carta[8] = new Carta("9", 3, "copas", "vermelho");
		carta[9] = new Carta("10", 3, "espada", "preto");
		carta[10] = new Carta("11", 3, "ouro", "vermelho");
		carta[11] = new Carta("12", 3, "paus", "preto");
		carta[12] = new Carta("13", 4, "copas", "vermelho");
		carta[13] = new Carta("14", 4, "espada", "preto");
		carta[14] = new Carta("15", 4, "ouro", "vermelho");
		carta[15] = new Carta("16", 4, "paus", "preto");
		carta[16] = new Carta("17", 5, "copas", "vermelho");
		carta[17] = new Carta("18", 5, "espada", "preto");
		carta[18] = new Carta("19", 5, "ouro", "vermelho");
		carta[19] = new Carta("20", 5, "paus", "preto");
		carta[20] = new Carta("21", 6, "copas", "vermelho");
		carta[21] = new Carta("22", 6, "espada", "preto");
		carta[22] = new Carta("23", 6, "ouro", "vermelho");
		carta[23] = new Carta("24", 6, "paus", "preto");
		carta[24] = new Carta("25", 7, "copas", "vermelho");
		carta[25] = new Carta("26", 7, "espada", "preto");
		carta[26] = new Carta("27", 7, "ouro", "vermelho");
		carta[27] = new Carta("28", 7, "paus", "preto");
		carta[28] = new Carta("29", 8, "copas", "vermelho");
		carta[29] = new Carta("30", 8, "espada", "preto");
		carta[30] = new Carta("31", 8, "ouro", "vermelho");
		carta[31] = new Carta("32", 8, "paus", "preto");
		carta[32] = new Carta("33", 9, "copas", "vermelho");
		carta[33] = new Carta("34", 9, "espada", "preto");
		carta[34] = new Carta("35", 9, "ouro", "vermelho");
		carta[35] = new Carta("36", 9, "paus", "preto");
		carta[36] = new Carta("37", 10, "copas", "vermelho");
		carta[37] = new Carta("38", 10, "espada", "preto");
		carta[38] = new Carta("39", 10, "ouro", "vermelho");
		carta[39] = new Carta("40", 10, "paus", "preto");
		carta[40] = new Carta("41", 11, "copas", "vermelho");
		carta[41] = new Carta("42", 11, "espada", "preto");
		carta[42] = new Carta("43", 11, "ouro", "vermelho");
		carta[43] = new Carta("44", 11, "paus", "preto");
		carta[44] = new Carta("45", 12, "copas", "vermelho");
		carta[45] = new Carta("46", 12, "espada", "preto");
		carta[46] = new Carta("47", 12, "ouro", "vermelho");
		carta[47] = new Carta("48", 12, "paus", "preto");
		carta[48] = new Carta("49", 13, "copas", "vermelho");
		carta[49] = new Carta("50", 13, "espada", "preto");
		carta[50] = new Carta("51", 13, "ouro", "vermelho");
		carta[51] = new Carta("52", 13, "paus", "preto");

		// --- Instancia valores para as células ---
		for (i = 0; i < celula.length; i++)
			celula[i] = new Carta("transparente", 0, "", "");

		// --- Instancia valores para as colunas vazias ---
		for (i = 0; i < colunaVazia.length; i++)
			colunaVazia[i] = new Carta("transparente", 0, "", "");

		// --- Cria as cartas ao tabuleiro ---
		for (i = 0; i < carta.length; i++) {
			add(carta[i].getImagemCartaCortada());
			add(carta[i].getImagemCartaInteira());
		}

		// --- Cria as células ao tabuleiro ---
		for (i = 0; i < celula.length; i++) {
			add(celula[i].getImagemCartaCortada());
			add(celula[i].getImagemCartaInteira());
		}

		// --- Cria as colunas vazias no tabuleiro ---
		for (i = 0; i < colunaVazia.length; i++) {
			add(colunaVazia[i].getImagemCartaCortada());
			add(colunaVazia[i].getImagemCartaInteira());
		}

		// --- Embaralha as cartas ---
		embaralhaCartas ();

		// --- Monta as cartas no tabuleiro ---
		int k = 0;
		for (i = 0; i < 7; i++) {
			// --- Coluna 1 ---
			if (i == 0)
				cartaPilha1.push(colunaVazia[0]);
			carta[k].getImagemCartaCortada().setBounds(20, (INICIO + (i * 30)), 73, 30);
			carta[k].getImagemCartaInteira().setBounds(20, (INICIO + (i * 30)), 0, 0);
			carta[k].setX(20);
			carta[k].setY(INICIO + (i * 30));
			cartaPilha1.push(carta[k]);
			k++;
			// --- Coluna 2 ---
			if (i == 0)
				cartaPilha2.push(colunaVazia[1]);
			carta[k].getImagemCartaCortada().setBounds(100, (INICIO + (i * 30)), 73, 30);
			carta[k].getImagemCartaInteira().setBounds(100, (INICIO + (i * 30)), 0, 0);
			carta[k].setX(100);
			carta[k].setY(INICIO + (i * 30));
			cartaPilha2.push(carta[k]);
			k++;
			// --- Coluna 3 ---
			if (i == 0)
				cartaPilha3.push(colunaVazia[2]);
			carta[k].getImagemCartaCortada().setBounds(180, (INICIO + (i * 30)), 73, 30);
			carta[k].getImagemCartaInteira().setBounds(180, (INICIO + (i * 30)), 0, 0);
			carta[k].setX(180);
			carta[k].setY(INICIO + (i * 30));
			cartaPilha3.push(carta[k]);
			k++;
			// --- Coluna 4 ---
			if (i == 0)
				cartaPilha4.push(colunaVazia[3]);
			carta[k].getImagemCartaCortada().setBounds(260, (INICIO + (i * 30)), 73, 30);
			carta[k].getImagemCartaInteira().setBounds(260, (INICIO + (i * 30)), 0, 0);
			carta[k].setX(260);
			carta[k].setY(INICIO + (i * 30));
			cartaPilha4.push(carta[k]);
			k++;
			// --- Coluna 5 ---
			if (k == 52)
				break;
			if (i == 0)
				cartaPilha5.push(colunaVazia[4]);
			carta[k].getImagemCartaCortada().setBounds(340, (INICIO + (i * 30)), 73, 30);
			carta[k].getImagemCartaInteira().setBounds(340, (INICIO + (i * 30)), 0, 0);
			carta[k].setX(340);
			carta[k].setY(INICIO + (i * 30));
			cartaPilha5.push(carta[k]);
			k++;
			// --- Coluna 6 ---
			if (i == 0)
				cartaPilha6.push(colunaVazia[5]);
			carta[k].getImagemCartaCortada().setBounds(420, (INICIO + (i * 30)), 73, 30);
			carta[k].getImagemCartaInteira().setBounds(420, (INICIO + (i * 30)), 0, 0);
			carta[k].setX(420);
			carta[k].setY(INICIO + (i * 30));
			cartaPilha6.push(carta[k]);
			k++;
			// --- Coluna 7 ---
			if (i == 0)
				cartaPilha7.push(colunaVazia[6]);
			carta[k].getImagemCartaCortada().setBounds(500, (INICIO + (i * 30)), 73, 30);
			carta[k].getImagemCartaInteira().setBounds(500, (INICIO + (i * 30)), 0, 0);
			carta[k].setX(500);
			carta[k].setY(INICIO + (i * 30));
			cartaPilha7.push(carta[k]);
			k++;
			// --- Coluna 8 ---
			if (i == 0)
				cartaPilha8.push(colunaVazia[7]);
			carta[k].getImagemCartaCortada().setBounds(580, (INICIO + (i * 30)), 73, 30);
			carta[k].getImagemCartaInteira().setBounds(580, (INICIO + (i * 30)), 0, 0);
			carta[k].setX(580);
			carta[k].setY(INICIO + (i * 30));
			cartaPilha8.push(carta[k]);
			k++;
		}

		// --- Monta a última fila com as cartas inteiras ---
		carta[44].getImagemCartaInteira().setBounds(340, 280, 73, 97);
		carta[44].getImagemCartaCortada().setBounds(340, 280, 0, 0);
		carta[45].getImagemCartaInteira().setBounds(420, 280, 73, 97);
		carta[45].getImagemCartaCortada().setBounds(420, 280, 0, 0);
		carta[46].getImagemCartaInteira().setBounds(500, 280, 73, 97);
		carta[46].getImagemCartaCortada().setBounds(500, 280, 0, 0);
		carta[47].getImagemCartaInteira().setBounds(580, 280, 73, 97);
		carta[47].getImagemCartaCortada().setBounds(580, 280, 0, 0);
		carta[48].getImagemCartaInteira().setBounds(20, 310, 73, 97);
		carta[48].getImagemCartaCortada().setBounds(20, 310, 0, 0);
		carta[49].getImagemCartaInteira().setBounds(100, 310, 73, 97);
		carta[49].getImagemCartaCortada().setBounds(100, 310, 0, 0);
		carta[50].getImagemCartaInteira().setBounds(180, 310, 73, 97);
		carta[50].getImagemCartaCortada().setBounds(180, 310, 0, 0);
		carta[51].getImagemCartaInteira().setBounds(260, 310, 73, 97);
		carta[51].getImagemCartaCortada().setBounds(260, 310, 0, 0);

		// --- Monta a carta transparente atraz da última posicao da pilha ---
		for (i = 0; i < 8; i++) {
			celula[i].getImagemCartaInteira().setBounds((20 + (i * 80)), 15, 73, 97);
			celula[i].setX((20 + (i * 80)));
			celula[i].setY(15);
		}

		// --- Monta as células no tabuleiro ---
		for (i = 0; i < 8; i++) {
			colunaVazia[i].getImagemCartaInteira().setBounds((20 + (i * 80)), INICIO, 73, 97);
			colunaVazia[i].setX((20 + (i * 80)));
			colunaVazia[i].setY(INICIO);
		}

		// --- joga as células nas respectivas pilhas ---
		celulaPilha1.push(celula[0]);
		celulaPilha2.push(celula[1]);
		celulaPilha3.push(celula[2]);
		celulaPilha4.push(celula[3]);
		celulaPilha5.push(celula[4]);
		celulaPilha6.push(celula[5]);
		celulaPilha7.push(celula[6]);
		celulaPilha8.push(celula[7]);

		// --- Associa click as colunas vazias ---
		for (i = 0; i < colunaVazia.length; i++) {
			final int n = i;
			colunaVazia[i].getImagemCartaInteira().addMouseListener(
				new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						int x = colunaVazia[n].getX();
						int y = colunaVazia[n].getY();

						// --- Click na Coluna 1 ---
						if (y == INICIO && x == 20)
							movimentarCarta (cartaPilha1, "coluna", 20);

						// --- Click na Coluna 2 ---
						if (y == INICIO && x == 100)
							movimentarCarta (cartaPilha2, "coluna", 100);

						// --- Click na Coluna 3 ---
						if (y == INICIO && x == 180)
							movimentarCarta (cartaPilha3, "coluna", 180);

						// --- Click na Coluna 4 ---
						if (y == INICIO && x == 260)
							movimentarCarta (cartaPilha4, "coluna", 260);

						// --- Click na Coluna 5 ---
						if (y == INICIO && x == 340)
							movimentarCarta (cartaPilha5, "coluna", 340);

						// --- Click na Coluna 6 ---
						if (y == INICIO && x == 420)
							movimentarCarta (cartaPilha6, "coluna", 420);

						// --- Click na Coluna 7 ---
						if (y == INICIO && x == 500)
							movimentarCarta (cartaPilha7, "coluna", 500);

						// --- Click na Coluna 8 ---
						if (y == INICIO && x == 580)
							movimentarCarta (cartaPilha8, "coluna", 580);
					}
				}
			);
		}

		// --- Associa click as células ---
		for (i = 0; i < celula.length; i++) {
			final int m = i;
			celula[i].getImagemCartaInteira().addMouseListener(
				new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						int x = celula[m].getX();
						int y = celula[m].getY();

						// --- Click na Célula 1 ---
						if (y == 15 && x == 20)
							movimentarCarta (celulaPilha1, "celula", 20);

						// --- Click na Célula 2 ---
						if (y == 15 && x == 100)
							movimentarCarta (celulaPilha2, "celula", 100);

						// --- Click na Célula 3 ---
						if (y == 15 && x == 180)
							movimentarCarta (celulaPilha3, "celula", 180);

						// --- Click na Célula 4 ---
						if (y == 15 && x == 260)
							movimentarCarta (celulaPilha4, "celula", 260);

						// --- Click na Base 1 ---
						if (y == 15 && x == 340)
							movimentarCarta (celulaPilha5, "base", 340);

						// --- Click na Base 2 ---
						if (y == 15 && x == 420)
							movimentarCarta (celulaPilha6, "base", 420);

						// --- Click na Base 3 ---
						if (y == 15 && x == 500)
							movimentarCarta (celulaPilha7, "base", 500);

						// --- Click na Base 4 ---
						if (y == 15 && x == 580)
							movimentarCarta (celulaPilha8, "base", 580);
					}
				}
			);
		}

		// --- Associa o click do mouse a carta ---
		for (i = 0; i < carta.length; i++) {
			final int j = i;
			carta[i].getImagemCartaInteira().addMouseListener(
				new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						int x = carta[j].getX();
						int y = carta[j].getY();

						// --- Click na Célula 1 ---
						if (y == 15 && x == 20)
							movimentarCarta (celulaPilha1, "celula", 20);

						// --- Click na Célula 2 ---
						if (y == 15 && x == 100)
							movimentarCarta (celulaPilha2, "celula", 100);

						// --- Click na Célula 3 ---
						if (y == 15 && x == 180)
							movimentarCarta (celulaPilha3, "celula", 180);

						// --- Click na Célula 4 ---
						if (y == 15 && x == 260)
							movimentarCarta (celulaPilha4, "celula", 260);

						// --- Click na Base 1 ---
						if (y == 15 && x == 340)
							movimentarCarta (celulaPilha5, "base", 340);

						// --- Click na Base 2 ---
						if (y == 15 && x == 420)
							movimentarCarta (celulaPilha6, "base", 420);

						// --- Click na Base 3 ---
						if (y == 15 && x == 500)
							movimentarCarta (celulaPilha7, "base", 500);

						// --- Click na Base 4 ---
						if (y == 15 && x == 580)
							movimentarCarta (celulaPilha8, "base", 580);

						// --- Click na Coluna 1 ---
						if (x == 20 && y > 15)
							movimentarCarta (cartaPilha1, "coluna", 20);

						// --- Click na Coluna 2 ---
						if (x == 100 && y > 15)
							movimentarCarta (cartaPilha2, "coluna", 100);

						// --- Click na Coluna 3 ---
						if (x == 180 && y > 15)
							movimentarCarta (cartaPilha3, "coluna", 180);

						// --- Click na Coluna 4 ---
						if (x == 260 && y > 15)
							movimentarCarta (cartaPilha4, "coluna", 260);

						// --- Click na Coluna 5 ---
						if (x == 340 && y > 15)
							movimentarCarta (cartaPilha5, "coluna", 340);

						// --- Click na Coluna 6 ---
						if (x == 420 && y > 15)
							movimentarCarta (cartaPilha6, "coluna", 420);

						// --- Click na Coluna 7 ---
						if (x == 500 && y > 15)
							movimentarCarta (cartaPilha7, "coluna", 500);

						// --- Click na Coluna 8 ---
						if (x == 580 && y > 15)
							movimentarCarta (cartaPilha8, "coluna", 580);
					}
				}
			);
		}
	}

	/** Método run para usar a thread */
	public void run () {
		while (true) {
			try {
				// --- Espera um segundo ---
				Thread.sleep((long)1000);
				segundo++;
				tempo.setText("FreeCell "+ segundo +" segundos");
				if (fimDoJogo() || paraThread) {
					paraThread = false;
					break;
				}
			} catch (InterruptedException iex) {
			}
		}
	}

	/** Clicks no menu */
	public void actionPerformed (ActionEvent e) {
		// --- Item Novo Jogo ---
		if (e.getSource() == menuItemNovo) {
			// --- Pergunta ---
			if (msg.pergunta("Deseja realmente iniciar novo jogo?.", "FreeCell") == 0) {
				// --- Reinicia variáveis ---
				copas = false;
				paus = false;
				ouro = false;
				espada = false;
				click = false;
				reinicio = true;
				paraThread = true;

				// --- Reinicia contador de cartas ---
				contadorCartas = 52;
				contador.setText("Faltam "+ contadorCartas +" cartas");

				// --- Reinicia contagem de tempo ---
				segundo = 0;
				Thread t = new Thread(this);
				t.start();

				// --- Reinicia tabuleiro ---
				montaTabuleiro();
			}
		}

		// --- Item Melhores tempos ---
		if (e.getSource() == menuItemMelhores) {
			try {
				Statement stmt = conexao.createStatement();
				ResultSet resultado = stmt.executeQuery("SELECT nome, tempo FROM registro ORDER BY tempo");
				String texto = "";
		        for (int i = 0; i < 3; i++) {
		        	resultado.next();
		        	texto+= resultado.getString("nome") +" - "+ resultado.getInt("tempo") +" segundos\n";
		        }
		        msg.mensagem("Os melhores tempos são:\n\n"+ texto +"\n", "Melhores tempos");

		        resultado.close();
		        stmt.close();
	        } catch (Exception exc) {
	            //System.exit(0);
	        }
		}

		// --- Item Sair ---
		if (e.getSource() == menuItemSair) {
			if (msg.pergunta("Deseja realmente sair.", "FreeCell") == 0)
				System.exit(0);
		}

		// --- Item Ajuda ---
		if (e.getSource() == menuItemAjuda) {
			msg.mensagem("" +
					"O Jogo Freecell  é um jogo solitário, ou seja apenas um jogador, tem como objetivo\n" +
					"mover todas as cartas para as células bases, utilizando as células livres(quatro\n" +
					"espaços a esquerda) como espaço para permutação. Para vencer é necessário criar\n" +
					"quatro pilhas de carta nas células bases (quatro espaços à direita), uma de cada\n" +
					"naipe, empilhada em ordem crescente, ou seja, da carta mias baixa (ás) até a mais\n" +
					"alta (rei) ou quando não houver mais movimentos."
				, "Ajuda");
		}

		// --- Sobre ---
		if (e.getSource() == menuItemSobre) {
			msg.mensagem("" +
						"Jogo FreeCell desenvolvido como parte do Trabalho Interdisciplinar\n das Faculdades Jorge Amando em 2007.1\n\n" +
						"Equipe:\n" +
						"Bruna Sayuri\n" +
						"Camila da Silva Oliveira\n" +
						"Guilherme A. de Oliveira Junior\n" +
						"Renilda Oliveira\n" +
						"Ricardo Cancio"
					, "Sobre");
		}
	}

	/** Faz a movimentação da carta */
	public void movimentarCarta (Pilha pilhaMovimenta, String tipo, int x) {
		// --- Segundo Click ---
		if (click) {
			Carta primeiraCarta = primeiraPilha.ultimaPosicao();
			Carta segundaCarta = pilhaMovimenta.ultimaPosicao();
			// --- Se o click for na célula ---
			if (tipo == "celula") {
				if (segundaCarta.getValor() == 0) {
					// --- Movimenta a carta para a célula ---
					primeiraCarta.getImagemCartaCortada().setBounds(x, 15, 0, 0);
					primeiraCarta.getImagemCartaInteira().setBounds(x, 15, 73, 97);
					primeiraCarta.setX(x);
					primeiraCarta.setY(15);
					pilhaMovimenta.push(primeiraPilha.pop());
					// --- Troca a carta cortada pela carta inteira ---
					if (!primeiraPilha.isEmpty()) {
						primeiraCarta = primeiraPilha.ultimaPosicao();
						primeiraCarta.getImagemCartaCortada().setBounds(primeiraCarta.getX(), primeiraCarta.getY(), 0, 0);
						primeiraCarta.getImagemCartaInteira().setBounds(primeiraCarta.getX(), primeiraCarta.getY(), 73, 97);
					}
				} else {
					msg.erro ("Jogada inválida!");
				}
			}

			// --- Se o click for na coluna ---
			if (tipo == "coluna") {
				// --- Se a coluna estiver vazia ---
				if (segundaCarta.getValor() == 0) {
					primeiraCarta.getImagemCartaInteira().setBounds(x, INICIO, 73, 97);
					primeiraCarta.setX(x);
					primeiraCarta.setY(INICIO);
					pilhaMovimenta.push(primeiraPilha.pop());
					// --- Troca a carta cortada pela carta inteira ---
					if (!primeiraPilha.isEmpty()) {
						primeiraCarta = primeiraPilha.ultimaPosicao();
						primeiraCarta.getImagemCartaCortada().setBounds(primeiraCarta.getX(), primeiraCarta.getY(), 0, 0);
						primeiraCarta.getImagemCartaInteira().setBounds(primeiraCarta.getX(), primeiraCarta.getY(), 73, 97);
					}
				} else {
					// --- Se o movimenta não for válido ---
					if (segundaCarta.getCor() == primeiraCarta.getCor() || segundaCarta.getValor()-1 != primeiraCarta.getValor()) {
						msg.erro ("Jogada inválida!");
					} else {
						// --- Troca a carta inteira pela carta cortada ---
						segundaCarta.getImagemCartaInteira().setBounds(segundaCarta.getX(), segundaCarta.getY(), 0, 0);
						segundaCarta.getImagemCartaCortada().setBounds(segundaCarta.getX(), segundaCarta.getY(), 73, 30);
						// --- Movimenta a carta inteira ---
						primeiraCarta.getImagemCartaInteira().setBounds(segundaCarta.getX(), segundaCarta.getY()+30, 73, 97);
						primeiraCarta.getImagemCartaCortada().setBounds(segundaCarta.getX(), segundaCarta.getY()+30, 0, 0);
						primeiraCarta.setX(x);
						primeiraCarta.setY(segundaCarta.getY()+30);
						// --- Movimenta a carta entre as pilhas ---
						pilhaMovimenta.push(primeiraPilha.pop());
						// --- Troca a carta cortada pela carta inteira ---
						if (!primeiraPilha.isEmpty()) {
							primeiraCarta = primeiraPilha.ultimaPosicao();
							primeiraCarta.getImagemCartaCortada().setBounds(primeiraCarta.getX(), primeiraCarta.getY(), 0, 0);
							primeiraCarta.getImagemCartaInteira().setBounds(primeiraCarta.getX(), primeiraCarta.getY(), 73, 97);
						}
					}
				}
			}

			// --- Se o click for na base ---
			if (tipo == "base") {
				if (segundaCarta.getValor() == 0) {
					if (primeiraCarta.getValor() == 1) {
						primeiraCarta.getImagemCartaInteira().setBounds(x, 15, 73, 97);
						primeiraCarta.getImagemCartaCortada().setBounds(x, 15, 0, 0);
						primeiraCarta.setX(x);
						primeiraCarta.setY(15);
						pilhaMovimenta.push(primeiraPilha.pop());
						// --- Troca a carta cortada pela carta inteira ---
						if (!primeiraPilha.isEmpty()) {
							primeiraCarta = primeiraPilha.ultimaPosicao();
							primeiraCarta.getImagemCartaCortada().setBounds(primeiraCarta.getX(), primeiraCarta.getY(), 0, 0);
							primeiraCarta.getImagemCartaInteira().setBounds(primeiraCarta.getX(), primeiraCarta.getY(), 73, 97);
						}
						contadorCartas--;
						contador.setText("Faltam "+ contadorCartas +" cartas");
					} else {
						msg.erro("Jogada Inválida!");
					}
				} else {
					if (segundaCarta.getNaipe() == primeiraCarta.getNaipe() && segundaCarta.getValor()+1 == primeiraCarta.getValor()) {
						// --- XX ---
						segundaCarta.getImagemCartaCortada().setBounds(segundaCarta.getX(), segundaCarta.getY(), 0, 0);
						segundaCarta.getImagemCartaInteira().setBounds(segundaCarta.getX(), segundaCarta.getY(), 0, 0);
						primeiraCarta.getImagemCartaInteira().setBounds(x, 15, 73, 97);
						primeiraCarta.getImagemCartaCortada().setBounds(x, 15, 0, 0);
						primeiraCarta.setX(x);
						primeiraCarta.setY(15);
						int valor = primeiraCarta.getValor();
						String naipe = primeiraCarta.getNaipe();
						pilhaMovimenta.push(primeiraPilha.pop());
						// --- Troca a carta cortada pela carta inteira ---
						if (!primeiraPilha.isEmpty()) {
							primeiraCarta = primeiraPilha.ultimaPosicao();
							primeiraCarta.getImagemCartaCortada().setBounds(primeiraCarta.getX(), primeiraCarta.getY(), 0, 0);
							primeiraCarta.getImagemCartaInteira().setBounds(primeiraCarta.getX(), primeiraCarta.getY(), 73, 97);
						}
						contadorCartas--;
						contador.setText("Faltam "+ contadorCartas +" cartas");
						// --- Seta se os naipes estão completos ---
						if (valor == 13 && naipe == "copas")
							copas = true;
						if (valor == 13 && naipe == "paus")
							paus = true;
						if (valor == 13 && naipe == "ouro")
							ouro = true;
						if (valor == 13 && naipe == "espada")
							espada = true;
					} else {
						msg.erro("Jogada Inválida!");
					}
				}
			}
			if (fimDoJogo()) {
				boolean mensagemFim = true;
				try {
					Statement stmt = conexao.createStatement();
					ResultSet resultado = stmt.executeQuery("SELECT id, nome, tempo FROM registro ORDER BY tempo");

			        for (int i = 0; i < 3; i++) {
			        	resultado.next();
			        	if (segundo < resultado.getInt("tempo")) {
			        		String nome = JOptionPane.showInputDialog("Parabéns!\nVocê entrou para a lista de melhores tempo.\nPor favor preencha seu nome.", "");
			        		// --- Se preencher o nome grava no banco de dados ---
			        		if (nome != null)
			        			stmt.execute("INSERT INTO registro (nome, tempo) VALUES ('"+ nome +"', "+ segundo +")");
			        		mensagemFim = false;
			        		break;
			        	}
			        }

			        if (mensagemFim)
			        	msg.mensagem("VOCÊ GANHOU!\nFIM DE JOGO!", "FIM DE JOGO");

			        resultado.close();
			        stmt.close();
		        } catch (Exception e) {
		            //System.exit(0);
		        }
			}
			click = false;
		} else { // --- Primeiro Click ---
			// --- Se a pilha estiver vazia não pode movimentar ---
			if (pilhaMovimenta.isEmpty() || tipo == "base") {
				click = false;
				return;
			} else {
				// --- Pega a última posição da pilha ---
				primeiraPilha = pilhaMovimenta;
				Carta primeiraCarta = pilhaMovimenta.ultimaPosicao();
				// --- Se a carta estiver em branco não pode movimentar ---
				if (primeiraCarta.getValor() == 0) {
					click = false;
					return;
				}
				click = true;
			}
		}
	}

	/** Checa fim do jogo */
	public boolean fimDoJogo () {
		if (copas == true && paus == true &&  ouro == true &&  espada == true)
			return true;
		else
			return false;
	}

	/** Embaralha as cartas */
	public void embaralhaCartas () {
		for (int i= 0; i < carta.length; i++) {
			int troca= numeroAleatorio.nextInt (52);

			Carta tempCarta= carta[i];
			carta[i]= carta[troca];
			carta[troca]= tempCarta;
		}
	}

	/** Faz o tratamento da janela e abre a mesma */
	public void display() {
		// --- Tratamento da Janela ---
		setTitle("FreeCell");										// Título da Janela
		setLayout(null);											// Seta layout que será usado
		setSize(680, 600);											// Tamanho da Janela
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);				// Ação quando fecha a janela
		setLocation(200, 50);										// Seta onde a janela vai abrir
		setResizable(false);										// Seta para não permitir redimencionar a janela
		getContentPane().setBackground(new Color(34, 197, 72));		// Seta cor do fundo
		setVisible(true);
	}
}
