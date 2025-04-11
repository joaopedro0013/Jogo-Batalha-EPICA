import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class BatalhaGUI extends JFrame {

    private JTextPane outputArea;
    private Classe jogador;
    private Classe maquina;
    private Font battleFont;
    private MundoLivre mundoLivre;
    private JanelaBatalha janelaBatalhaAtual;
    private boolean jogoTerminou = false;
    private boolean ultimaBatalhaIniciada = false;

    public BatalhaGUI() {
        setTitle("Batalha Épica");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        battleFont = new Font("Arial", Font.PLAIN, 16);

        outputArea = new JTextPane();
        outputArea.setEditable(false);
        outputArea.setFont(battleFont);
        centerText();
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        mundoLivre = new MundoLivre(this);
        add(mundoLivre, BorderLayout.CENTER);

        // Solicitar foco para MundoLivre após a janela ser visível
        SwingUtilities.invokeLater(() -> mundoLivre.requestFocusInWindow());

        // Adicionar KeyListener para redirecionar eventos para MundoLivre
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Forward the key event to the MundoLivre's KeyListener
                mundoLivre.dispatchEvent(e);
            }
        });
        setFocusTraversalKeysEnabled(false); // Desabilitar o foco padrão
    }

    private void centerText() {
        StyledDocument doc = outputArea.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        StyleConstants.setFontFamily(center, battleFont.getFamily());
        StyleConstants.setFontSize(center, battleFont.getSize());
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
    }

    public void appendText(String text, Color color) {
        try {
            StyledDocument doc = outputArea.getStyledDocument();
            SimpleAttributeSet attributes = new SimpleAttributeSet();
            StyleConstants.setAlignment(attributes, StyleConstants.ALIGN_CENTER);
            StyleConstants.setFontFamily(attributes, battleFont.getFamily());
            StyleConstants.setFontSize(attributes, battleFont.getSize());
            StyleConstants.setForeground(attributes, color);
            doc.insertString(doc.getLength(), text, attributes);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void appendText(String text) {
        appendText(text, Color.BLACK);
    }

    public void iniciarBatalha(JanelaBatalha janelaBatalha, MundoLivre.Inimigo inimigo) {
        this.janelaBatalhaAtual = janelaBatalha;

        jogador = escolherPersonagem();
        maquina = escolherPersonagemMaquina();

        SwingUtilities.invokeLater(() -> {
            janelaBatalha.appendText("A batalha começou!\n");
            atualizarStatus(janelaBatalha);
            iniciarTurnoJogador(janelaBatalha);
        });
    }

    public Classe escolherPersonagem() {
        String[] opcoes = {"BARBARO", "MAGO", "LADINO"};
        int escolha = JOptionPane.showOptionDialog(janelaBatalhaAtual, "Escolha seu personagem", "Escolha",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcoes, opcoes[0]);
        switch (escolha) {
            case 0:
                return new BARBARO();
            case 1:
                return new MAGO();
            case 2:
                return new LADINO();
            default:
                return new BARBARO();
        }
    }

    public Classe escolherPersonagemMaquina() {
        int escolha = (int) (Math.random() * 3) + 1;
        switch (escolha) {
            case 1:
                janelaBatalhaAtual.appendText("A maquina selecionou - BARBARO\n", Color.RED);
                return new BARBARO();
            case 2:
                janelaBatalhaAtual.appendText("A maquina selecionou - MAGO\n", Color.RED);
                return new MAGO();
            case 3:
                janelaBatalhaAtual.appendText("A maquina selecionou - LADINO\n", Color.RED);
                return new LADINO();
            default:
                return new BARBARO();
        }
    }

    public void iniciarTurnoJogador(JanelaBatalha janelaBatalha) {
        janelaBatalha.appendText("===========\n");
        janelaBatalha.appendText("\n");
        janelaBatalha.appendText("Turno do Jogador\n");
        janelaBatalha.appendText("Vida do jogador: ", Color.BLACK);
        janelaBatalha.appendText(jogador.vida + " HP \n", Color.BLUE);
        janelaBatalha.appendText("Energia do jogador: ", Color.BLACK);
        janelaBatalha.appendText(jogador.enegia + " ENG\n", new Color(0, 128, 0)); // Um tom de verde mais suave
        janelaBatalha.appendText("Vida da Maquina: ", Color.BLACK);
        janelaBatalha.appendText(maquina.vida + " HP \n", Color.RED);
        janelaBatalha.appendText("\n");
    }

    public void turnoJogador(int acao) {
        if (acao == 1) {
            Arma arma = escolherArma();
            if (arma != null) {
                atacar(jogador, maquina, arma);
            } else {
                iniciarTurnoJogador(janelaBatalhaAtual);
                return;
            }
        } else if (acao == 2) {
            if (jogador.enegia < 1) {
                janelaBatalhaAtual.appendText("Sem energia suficiente para usar o poder.\n");
                iniciarTurnoJogador(janelaBatalhaAtual);
                return;
            }
            usarPoder(jogador, maquina);
        } else if (acao == 3) {
            int vidaMaxima = 0;
            if (jogador instanceof BARBARO) {
                vidaMaxima = 150;
            } else if (jogador instanceof MAGO) {
                vidaMaxima = 100;
            } else if (jogador instanceof LADINO) {
                vidaMaxima = 120;
            }
            if (jogador.vida == vidaMaxima) {
                janelaBatalhaAtual.appendText("Sua vida está cheia, não é possível curar.\n");
                iniciarTurnoJogador(janelaBatalhaAtual);
                return;
            } else if (jogador.enegia < 1) {
                janelaBatalhaAtual.appendText("Sem energia suficiente para curar. Escolha outra ação.\n");
                iniciarTurnoJogador(janelaBatalhaAtual);
                return;
            }
            curar(jogador);
        }
        if (maquina.vida > 0) {
            turnoMaquina();
        }
        atualizarStatus(janelaBatalhaAtual);
        if (jogador.vida > 0 && maquina.vida > 0) {
            iniciarTurnoJogador(janelaBatalhaAtual);
        }
    }

    private Arma escolherArma() {
        String[] opcoes = new String[1];
        ImageIcon[] imagens = new ImageIcon[1];
        Arma armaJogador = null;

        try {
            switch (jogador.idArma) {
                case 1:
                    opcoes[0] = "Lança";
                    imagens[0] = redimensionarImagem("./img/lanca.jpg");
                    armaJogador = new lanca();
                    break;
                case 2:
                    opcoes[0] = "Espada";
                    imagens[0] = redimensionarImagem("./img/espada.jpg");
                    armaJogador = new Espada();
                    break;
                case 3:
                    opcoes[0] = "Adaga";
                    imagens[0] = redimensionarImagem("./img/adaga.jpg");
                    armaJogador = new Adaga();
                    break;
                default:
                    janelaBatalhaAtual.appendText("Erro: ID de arma inválido.\n");
                    return null;
            }
        } catch (Exception e) {
            janelaBatalhaAtual.appendText("Erro ao carregar a imagem da arma.\n");
            e.printStackTrace();
            return null;
        }

        JPanel panel = new JPanel(new GridLayout(0, 1));
        ButtonGroup group = new ButtonGroup();
        JRadioButton[] buttons = new JRadioButton[1];

        buttons[0] = new JRadioButton(opcoes[0]);
        if (imagens[0] != null) {
            buttons[0].setIcon(imagens[0]);
        } else {
            buttons[0].setText(opcoes[0] + " (Imagem não encontrada)");
        }
        group.add(buttons[0]);
        panel.add(buttons[0]);

        int result = JOptionPane.showConfirmDialog(janelaBatalhaAtual, panel, "Escolha sua arma", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            return armaJogador;
        } else {
            return null;
        }
    }

    private ImageIcon redimensionarImagem(String url) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(url));
            if (icon == null || icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
                janelaBatalhaAtual.appendText("Imagem não encontrada: " + url + "\n");
                return null;
            }
            Image image = icon.getImage();
            Image newimg = image.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
            return new ImageIcon(newimg);
        } catch (Exception e) {
            janelaBatalhaAtual.appendText("Erro ao carregar a imagem: " + url + "\n");
            e.printStackTrace();
            return null;
        }
    }

    private void turnoMaquina() {
        janelaBatalhaAtual.appendText("Turno da Máquina\n");
        Color maquinaColor = Color.RED;
        Color jogadorColor = Color.BLUE;

       
        int escolha = (int) (Math.random() * 3) + 1;
        if (escolha == 1) {
            Arma arma = escolherArmaMaquina();
            atacar(maquina, jogador, arma);
        } else if (escolha == 2) {
            if (maquina.enegia < 1) {
                janelaBatalhaAtual.appendText("A Maquina está sem energia suficiente para usar o poder. Tentando atacar com arma.\n");
                Arma arma = escolherArmaMaquina();
                atacar(maquina, jogador, arma);
            } else {
                usarPoder(maquina, jogador);
            }
        } else if (escolha == 3) {
            int vidaMaxima = 0;
            if (maquina instanceof BARBARO) {
                vidaMaxima = 150;
            } else if (maquina instanceof MAGO) {
                vidaMaxima = 100;
            } else if (maquina instanceof LADINO) {
                vidaMaxima = 120;
            }

            if (maquina.vida == vidaMaxima) {
                janelaBatalhaAtual.appendText("A Maquina está com a vida cheia, não pode curar.\n");
                Arma arma = escolherArmaMaquina();
                atacar(maquina, jogador, arma);
            } else if (maquina.enegia < 1) {
                janelaBatalhaAtual.appendText("A Maquina está sem energia suficiente para curar. Tentando atacar com arma.\n");
                Arma arma = escolherArmaMaquina();
                atacar(maquina, jogador, arma);
            } else {
                curar(maquina);
            }
        }
    }

    private Arma escolherArmaMaquina() {
        int escolha = (int) (Math.random() * 3) + 1;
        switch (escolha) {
            case 1:
                return new lanca();
            case 2:
                return new Espada();
            case 3:
                return new Adaga();
            default:
                return new Adaga();
        }
    }

    private void atacar(Classe atacante, Classe defensor, Arma arma) {
        int dano = Integer.parseInt(arma.dano);
        int defesa = defensor.armadura;
        double reducaoDano = (double) defesa / (100 + defesa);
        double danoFinal = dano * (1 - reducaoDano);
        int resultado = (int) danoFinal;

        // Aplicar dano ao defensor
        defensor.vida -= resultado;

        String nomeAtacante = (atacante == jogador) ? "Você(" + atacante.nameClasse + ")" : "A Maquina(" + atacante.nameClasse + ")";
        String nomeDefensor = (defensor == jogador) ? "Você(" + defensor.nameClasse + ")" : "A Maquina(" + defensor.nameClasse + ")";
        Color atacanteColor = (atacante == jogador) ? Color.BLUE : Color.RED;
        Color defensorColor = (defensor == jogador) ? Color.BLUE : Color.RED;
        janelaBatalhaAtual.appendText(nomeAtacante , atacanteColor);
        janelaBatalhaAtual.appendText(" atacou com " + arma.tipoArma + " causando ", Color.BLACK);
        janelaBatalhaAtual.appendText(resultado + " de dano.\n", atacanteColor);
        janelaBatalhaAtual.appendText(nomeDefensor , defensorColor);
        janelaBatalhaAtual.appendText(nomeDefensor + " recebeu ", Color.BLACK);
        janelaBatalhaAtual.appendText(resultado + " de dano de arma e está com ", Color.BLACK);
        janelaBatalhaAtual.appendText(defensor.vida + " de vida.\n", defensorColor);
        janelaBatalhaAtual.appendText("===========\n");
    }

    private void usarPoder(Classe atacante, Classe defensor) {
        if (atacante.enegia < 1) {
            janelaBatalhaAtual.appendText("Sem energia suficiente para usar o poder.\n");
            return;
        }
        int danoAp = atacante.Poder;
        int defesaAp = defensor.resistenciaMagica;
        double reducaoDanoAp = (double) defesaAp / (100 + defesaAp);
        double danoFinalAp = danoAp * (1 - reducaoDanoAp);
        int resultadoAp = (int) danoFinalAp;

        String nomeAtacante = (atacante == jogador) ? "Você(" + atacante.nameClasse + ")" : "A Maquina(" + atacante.nameClasse + ")";
        String nomeDefensor = (defensor == jogador) ? "Você(" + defensor.nameClasse + ")" : "A Maquina(" + defensor.nameClasse + ")";
        Color atacanteColor = (atacante == jogador) ? Color.BLUE : Color.RED;
        Color defensorColor = (defensor == jogador) ? Color.BLUE : Color.RED;

        // Aplicar dano ao defensor
        defensor.vida -= resultadoAp;
        atacante.enegia -= 1;

        janelaBatalhaAtual.appendText(nomeAtacante + " usou seu poder causando ", Color.BLACK);
        janelaBatalhaAtual.appendText(resultadoAp + " de dano.\n", atacanteColor);
        janelaBatalhaAtual.appendText(nomeDefensor + " recebeu " + resultadoAp + " de dano do poder Especial e está com ", Color.BLACK);
        janelaBatalhaAtual.appendText(defensor.vida + " de vida.\n", defensorColor);
        atacante.enegia -= 1;
    }

    private void curar(Classe curador) {
        Random random = new Random();
        int cura = random.nextInt(16) + 15;
        int vidaMaxima = 0;

        if (curador instanceof BARBARO) {
            vidaMaxima = 150;
        } else if (curador instanceof MAGO) {
            vidaMaxima = 100;
        } else if (curador instanceof LADINO) {
            vidaMaxima = 120;
        }

        // Aplicar cura ao curador
        curador.vida += cura;

        if (curador.vida > vidaMaxima) {
            curador.vida = vidaMaxima;
        }

        String nomeCurador = (curador == jogador) ? "Você(" + curador.nameClasse + ")" : "A Maquina(" + curador.nameClasse + ")";
        Color curadorColor = (curador == jogador) ? Color.GREEN : Color.GREEN;

        curador.enegia -= 1;
        janelaBatalhaAtual.appendText(nomeCurador + " curou " + cura + " de vida.\n", curadorColor);
        janelaBatalhaAtual.appendText(nomeCurador + " está com " + curador.vida + " de vida e " + curador.enegia + " de energia.\n", curadorColor);
        janelaBatalhaAtual.appendText("===========\n");
    }

    private void atualizarStatus(JanelaBatalha janelaBatalha) {
        if (jogador.vida <= 0) {
            janelaBatalha.appendText("A máquina venceu!\n", Color.RED);
            fecharJanelaBatalha(janelaBatalha);
        } else if (maquina.vida <= 0) {
            janelaBatalha.appendText("Você venceu!\n", Color.BLUE);
            fecharJanelaBatalha(janelaBatalha);
        }
    }

    private void fecharJanelaBatalha(JanelaBatalha janelaBatalha) {
        janelaBatalha.dispose();
        SwingUtilities.invokeLater(() -> mundoLivre.requestFocusInWindow());
        verificarFimDeJogoAposBatalha(); // Adiciona esta linha
    }

    public void verificarFimDeJogo() {
        if (mundoLivre.inimigos.isEmpty() && !jogoTerminou && !ultimaBatalhaIniciada) {
            ultimaBatalhaIniciada = true;
            // Iniciar a última batalha aqui, se necessário
            // Após a batalha, exibir a mensagem de parabéns
        }
    }

    private void verificarFimDeJogoAposBatalha() {
        if (mundoLivre.inimigos.isEmpty() && !jogoTerminou && ultimaBatalhaIniciada) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, "Parabéns! Você limpou o mundo livre!", "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);
                int escolha = JOptionPane.showOptionDialog(this, "Deseja jogar novamente?", "Fim de Jogo",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                        new Object[]{"Sim", "Não"}, "Sim");

                if (escolha == JOptionPane.YES_OPTION) {
                    reiniciarJogo();
                } else {
                    System.exit(0);
                }
            });
            jogoTerminou = true;
        }
    }

    private void reiniciarJogo() {
        mundoLivre.jogadorX = 100;
        mundoLivre.jogadorY = 100;
        mundoLivre.inimigos.clear();
        mundoLivre.gerarInimigosIniciais();
        mundoLivre.emBatalha = false;
        jogoTerminou = false;
        ultimaBatalhaIniciada = false;
        SwingUtilities.invokeLater(() -> mundoLivre.requestFocusInWindow());
    }

    public MundoLivre getMundoLivre() {
        return mundoLivre;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BatalhaGUI().setVisible(true));
    }

    public boolean isJogoTerminou() {
        return jogoTerminou;
    }

    public void setJogoTerminou(boolean jogoTerminou) {
        this.jogoTerminou = jogoTerminou;
    }

    public boolean isUltimaBatalhaIniciada() {
        return ultimaBatalhaIniciada;
    }

    public void setUltimaBatalhaIniciada(boolean ultimaBatalhaIniciada) {
        this.ultimaBatalhaIniciada = ultimaBatalhaIniciada;
    }
}