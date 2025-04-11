import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JanelaBatalha extends JFrame {

    private BatalhaGUI batalhaGUI;
    private MundoLivre.Inimigo inimigo;
    private JTextPane outputArea;
    private JButton atacarButton;
    private JButton usarPoderButton;
    private JButton curarButton;
    private Font battleFont;

    public JanelaBatalha(BatalhaGUI batalhaGUI, MundoLivre.Inimigo inimigo) {
        this.batalhaGUI = batalhaGUI;
        this.inimigo = inimigo;

        setTitle("Batalha Épica");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        battleFont = new Font("Arial", Font.PLAIN, 16);

        outputArea = new JTextPane();
        outputArea.setEditable(false);
        outputArea.setFont(battleFont);
        centerText();
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        atacarButton = new JButton("Atacar com arma");
        usarPoderButton = new JButton("Usar poder");
        curarButton = new JButton("Curar");
        buttonPanel.add(atacarButton);
        buttonPanel.add(usarPoderButton);
        buttonPanel.add(curarButton);
        add(buttonPanel, BorderLayout.SOUTH);

        atacarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                batalhaGUI.turnoJogador(1);
            }
        });

        usarPoderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                batalhaGUI.turnoJogador(2);
            }
        });

        curarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                batalhaGUI.turnoJogador(3);
            }
        });

        setVisible(true);

        // Adicionar mensagem de teste
        appendText("Teste: Chat da batalha aparecendo!\n");
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
        repaint();
    }

    public void appendText(String text) {
        appendText(text, Color.BLACK);
    }
}