import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class BatalhaGUI extends JFrame {
    private JTextArea outputArea;
    private JButton atacarButton;
    private JButton usarPoderButton;
    private Classe jogador;
    private Classe maquina;

    public BatalhaGUI() {
        setTitle("Batalha Épica");
        setSize(800, 600); // Aumentar o tamanho da janela
        setLocationRelativeTo(null); // Centralizar a janela na tela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        atacarButton = new JButton("Atacar com arma");
        usarPoderButton = new JButton("Usar poder");
        buttonPanel.add(atacarButton);
        buttonPanel.add(usarPoderButton);
        add(buttonPanel, BorderLayout.SOUTH);

        atacarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                turnoJogador(1);
            }
        });

        usarPoderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                turnoJogador(2);
            }
        });

        iniciarBatalha();
    }

    private void iniciarBatalha() {
        jogador = escolherPersonagem();
        maquina = escolherPersonagemMaquina();
        outputArea.append("A batalha começou!\n");
        atualizarStatus();
        iniciarTurnoJogador(); // Iniciar o primeiro turno do jogador
    }

    private Classe escolherPersonagem() {
        String[] opcoes = {"BARBARO", "MAGO", "LADINO"};
        int escolha = JOptionPane.showOptionDialog(this, "Escolha seu personagem", "Escolha",
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

    private Classe escolherPersonagemMaquina() {
        int escolha = (int) (Math.random() * 3) + 1;
        switch (escolha) {
            case 1:
                outputArea.append("A maquina selecionou - BARBARO\n");
                return new BARBARO();
            case 2:
                outputArea.append("A maquina selecionou - MAGO\n");
                return new MAGO();
            case 3:
                outputArea.append("A maquina selecionou - LADINO\n");
                return new LADINO();
            default:
                return new BARBARO();
        }
    }

    private void iniciarTurnoJogador() {
        outputArea.append("===========\n");
        outputArea.append("Turno do Jogador\n");
        outputArea.append("Vida do jogador: " + jogador.vida + "\n");
        outputArea.append("Vida da Maquina: " + maquina.vida + "\n");
        atacarButton.setEnabled(true);
        usarPoderButton.setEnabled(true);

    }

    private void turnoJogador(int acao) {
        atacarButton.setEnabled(false);
        usarPoderButton.setEnabled(false);
        if (acao == 1) {
            Arma arma = escolherArma();
            if (arma != null) {
                atacar(jogador, maquina, arma);
            } else {
                iniciarTurnoJogador(); // Voltar para a janela de seleção entre usar o poder ou usar uma arma
                return;
            }
        } else if (acao == 2) {
            usarPoder(jogador, maquina);
        }
        if (maquina.vida > 0) {
            turnoMaquina();
        }
        atualizarStatus();
        if (jogador.vida > 0 && maquina.vida > 0) {
            iniciarTurnoJogador(); // Iniciar o próximo turno do jogador
        }
    }

    private Arma escolherArma() {
        String[] opcoes = {"Lança", "Espada", "Adaga"};
        ImageIcon[] imagens = {
            redimensionarImagem("https://static.wikia.nocookie.net/rpg-rise-of-the-titans/images/f/f7/Spearr.jpg/revision/latest?cb=20220702041006&path-prefix=pt-br"),
            redimensionarImagem("https://acdn.mitiendanube.com/stores/003/350/223/products/uc1299-688e734f73911cc19b16941316375886-640-0.png"),
            redimensionarImagem("https://www.htimportech.oruc.com.br/arquivos/PRODUTOS/3891722000427662486/1_G_Adaga-Dark-com-bainha-adaga-medieval-para-co_43.jpg")
        };

        JPanel panel = new JPanel(new GridLayout(0, 1));
        ButtonGroup group = new ButtonGroup();
        JRadioButton[] buttons = new JRadioButton[opcoes.length];

        for (int i = 0; i < opcoes.length; i++) {
            buttons[i] = new JRadioButton(opcoes[i]);
            buttons[i].setIcon(imagens[i]);
            group.add(buttons[i]);
            panel.add(buttons[i]);
        }

        int result = JOptionPane.showConfirmDialog(this, panel, "Escolha sua arma", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            for (int i = 0; i < buttons.length; i++) {
                if (buttons[i].isSelected()) {
                    switch (i) {
                        case 0:
                            return new lanca();
                        case 1:
                            return new Espada();
                        case 2:
                            return new Adaga();
                    }
                }
            }
        }
        return null; // Retornar null se o jogador clicar em "Cancelar" ou fechar a janela
    }

    private ImageIcon redimensionarImagem(String url) {
        try {
            ImageIcon icon = new ImageIcon(new URL(url));
            Image image = icon.getImage();
            Image newimg = image.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH); // Redimensionar a imagem
            return new ImageIcon(newimg);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void turnoMaquina() {
        int escolha = (int) (Math.random() * 2) + 1;
        outputArea.append("Turno da Máquina\n");
        if (escolha == 1) {
            Arma arma = escolherArmaMaquina();
            atacar(maquina, jogador, arma);
        } else if (escolha == 2) {
            usarPoder(maquina, jogador);
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
        defensor.vida -= dano;
        outputArea.append(atacante.nameClasse + " atacou com " + arma.tipoArma + " causando " + dano + " de dano.\n");
        outputArea.append(defensor.nameClasse + " recebeu " + dano + " de dano de arma e está com " + defensor.vida + " de vida.\n");
        outputArea.append("===========\n");
    }

    private void usarPoder(Classe atacante, Classe defensor) {
        if (atacante.enegia < 1) {
            outputArea.append("Sem energia suficiente para usar o poder. Escolha outra ação.\n");
            return;
        }
        defensor.vida -= atacante.Poder;
        atacante.enegia -= 1;
        outputArea.append(atacante.nameClasse + " usou seu poder causando " + atacante.Poder + " de dano.\n");
        outputArea.append(defensor.nameClasse + " recebeu " + atacante.Poder + " de dano do poder e está com " + defensor.vida + " de vida.\n");
        outputArea.append("===========\n");
    }

    private void atualizarStatus() {
        if (jogador.vida <= 0) {
            outputArea.append("A máquina venceu!\n");
            desabilitarBotoes();
        } else if (maquina.vida <= 0) {
            outputArea.append("Você venceu!\n");
            desabilitarBotoes();
        }
    }

    private void desabilitarBotoes() {
        atacarButton.setEnabled(false);
        usarPoderButton.setEnabled(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BatalhaGUI().setVisible(true);
            }
        });
    }
}