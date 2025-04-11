import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MundoLivre extends JPanel implements Runnable {

    public int jogadorX = 100;
    public int jogadorY = 100;
    public int velocidade = 5;
    public List<Inimigo> inimigos = new ArrayList<>();
    private BatalhaGUI batalhaGUI;
    public boolean emBatalha = false;
    private Image jogadorImage;

    public MundoLivre(BatalhaGUI batalhaGUI) {
        this.batalhaGUI = batalhaGUI;
        setPreferredSize(new Dimension(800, 600));
        setFocusable(true);
        requestFocusInWindow();

        try {
            jogadorImage = new ImageIcon(getClass().getResource("./img/personagem.jpg")).getImage();
        } catch (Exception e) {
            System.err.println("Erro ao carregar a imagem do jogador: " + e.getMessage());
            jogadorImage = new ImageIcon(getClass().getResource("./img/imagem_padrao.jpg")).getImage(); // Use uma imagem padrão
        }

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!emBatalha && !batalhaGUI.isJogoTerminou()) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_W:
                            jogadorY -= velocidade;
                            break;
                        case KeyEvent.VK_S:
                            jogadorY += velocidade;
                            break;
                        case KeyEvent.VK_A:
                            jogadorX -= velocidade;
                            break;
                        case KeyEvent.VK_D:
                            jogadorX += velocidade;
                            break;
                    }
                    verificarColisao();
                    repaint();
                }
            }
        });

        gerarInimigosIniciais();
        new Thread(this).start();
    }

    public void gerarInimigosIniciais() {
        inimigos.clear();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int x = random.nextInt(700);
            int y = random.nextInt(500);
            inimigos.add(new Inimigo(x, y));
        }
    }

    private void verificarColisao() {
        if (!emBatalha && !batalhaGUI.isJogoTerminou() && !inimigos.isEmpty()) {
            Inimigo inimigoProximo = null;
            double menorDistancia = Double.MAX_VALUE;

            for (Inimigo inimigo : inimigos) {
                double distancia = Math.sqrt(Math.pow(jogadorX - inimigo.x, 2) + Math.pow(jogadorY - inimigo.y, 2));
                if (distancia < menorDistancia) {
                    menorDistancia = distancia;
                    inimigoProximo = inimigo;
                }
            }

            if (inimigoProximo != null && menorDistancia < 50) {
                emBatalha = true;
                final Inimigo inimigoParaRemover = inimigoProximo;

                SwingUtilities.invokeLater(() -> {
                    JanelaBatalha janelaBatalha = new JanelaBatalha(batalhaGUI, inimigoParaRemover);
                    batalhaGUI.iniciarBatalha(janelaBatalha, inimigoParaRemover);
                    inimigos.remove(inimigoParaRemover);
                    emBatalha = false;
                });
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(jogadorImage, jogadorX, jogadorY, 50, 50, this);
        for (Inimigo inimigo : inimigos) {
            g.setColor(Color.RED);
            g.fillRect(inimigo.x, inimigo.y, 30, 30);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(1000 / 60);
                repaint();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void processKeyEvent(KeyEvent e) {
        if (!emBatalha && !batalhaGUI.isJogoTerminou()) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    jogadorY -= velocidade;
                    break;
                case KeyEvent.VK_S:
                    jogadorY += velocidade;
                    break;
                case KeyEvent.VK_A:
                    jogadorX -= velocidade;
                    break;
                case KeyEvent.VK_D:
                    jogadorX += velocidade;
                    break;
            }
            verificarColisao();
            repaint();
        }
    }

    public class Inimigo {
        public int x;
        public int y;

        public Inimigo(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}