import java.util.Scanner;

public class Batalha {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        Classe jogador = escolherPersonagem(scanner);
        Classe maquina = escolherPersonagemMaquina();

        System.out.println("=====================================");

        System.out.println("A batalha começou!");

        while (jogador.vida > 0 && maquina.vida > 0) {
            turnoJogador(scanner, jogador, maquina);
            if (maquina.vida > 0) {
                turnoMaquina(maquina, jogador);
            }
        }

        if (jogador.vida > 0) {
            System.out.println("Você venceu!");
        } else {
            System.out.println("A máquina venceu!");
        }

        scanner.close();
    }

    private static Classe escolherPersonagem(Scanner scanner) {
        System.out.println("Escolha seu personagem");
        System.out.println("1 - BARBARO");
        System.out.println("2 - MAGO");
        System.out.println("3 - LADINO");

        int escolha = scanner.nextInt();
        switch (escolha) {
            case 1:
                return new BARBARO();
            case 2:
                return new MAGO();
            case 3:
                return new LADINO();
            default:
                System.out.println("Escolha inválida, BARBARO selecionado por padrão.");
                return new BARBARO();
        }
    }

    private static Classe escolherPersonagemMaquina() {
        int escolha = (int) (Math.random() * 3) + 1;
        switch (escolha) {
            case 1:
                System.out.println("A maquina selecionou - BARBARO");
                return new BARBARO();
            case 2:
                System.out.println("A maquina selecionou - MAGO");
                return new MAGO();
            case 3:
            System.out.println("A maquina selecionou - LADINO");
                return new LADINO();
            default:
                return new BARBARO();
        }
    }

    private static void turnoJogador(Scanner scanner, Classe jogador, Classe maquina) {
        System.out.println("Sua vida: " + jogador.vida);
        System.out.println("Vida da máquina: " + maquina.vida);
        System.out.println("Escolha sua ação:");
        System.out.println("1 - Atacar com arma");
        System.out.println("2 - Usar poder (dano: " + jogador.Poder+ ")");

        int escolha = scanner.nextInt();
        switch (escolha) {
            case 1:
                Arma arma = escolherArma(scanner);
                atacar(jogador, maquina, arma);
                break;
            case 2:
                usarPoder(jogador, maquina);
                break;
            default:
                System.out.println("Escolha inválida, turno perdido.");
        }
    }

    private static Arma escolherArma(Scanner scanner) {
        System.out.println("Escolha sua arma:");
        System.out.println("1 - Lança (Dano:" + new lanca().dano +" )" + " - " + new lanca().imagem);
        System.out.println("2 - Espada (Dano:" + new Espada().dano + " )" + " - " + new Espada().imagem);
        System.out.println("3 - Adaga (Dano:" + new Adaga().dano+ " )" + " - " + new Adaga().imagem);
        

        int escolha = scanner.nextInt();
        switch (escolha) {
            case 1:
                return new lanca();
            case 2:
                return new Espada();
            case 3:
                return new Adaga();
            default:
                System.out.println("Escolha inválida, Adaga selecionada por padrão.");
                return new Adaga();
        }
    }

    private static void turnoMaquina(Classe maquina, Classe jogador) {
        int escolha = (int) (Math.random() * 2) + 1;
        System.out.println("Turno da Máquina");
        switch (escolha) {  
            case 1:
                Arma arma = escolherArmaMaquina();
                atacar(maquina, jogador, arma);
                break;
            case 2:
                usarPoder(maquina, jogador);
                break;
        }
    }

    private static Arma escolherArmaMaquina() {
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

    private static void atacar(Classe atacante, Classe defensor, Arma arma) {
        int dano = Integer.parseInt(arma.dano);
        defensor.vida -= dano;
        System.out.println(atacante.nameClasse + " atacou com " + arma.tipoArma + " causando " + dano + " de dano.");
        System.out.println(defensor.nameClasse + " Recebeu " + dano + " de dano de arma e está com " + defensor.vida + " de vida.");
        System.out.println("===========");
        }

    private static void usarPoder(Classe atacante, Classe defensor) {
        if (atacante.enegia < 1) {
            System.out.println("Sem energia suficiente para usar o poder. Escolha outra ação.");
            return;
        }
        defensor.vida -= atacante.Poder;
        atacante.enegia -= 1;
        System.out.println(atacante.nameClasse + " usou seu poder causando " + atacante.Poder + " de dano.");
        System.out.println(defensor.nameClasse + " Recebeu " + atacante.Poder + " de dano do poder e está com " + defensor.vida + " de vida.");
        System.out.println("===========");
    }

    private static void usarPoder(Scanner scanner, Classe atacante, Classe defensor) {
        if (atacante.enegia < 1) {
            System.out.println("Sem energia suficiente para usar o poder. Escolha outra ação.");
            turnoJogador(scanner, atacante, defensor);
            return;
        }
        defensor.vida -= atacante.Poder;
        atacante.enegia -= 1;
        System.out.println(atacante.nameClasse + " usou seu poder causando " + atacante.Poder + " de dano.");
        System.out.println(defensor.nameClasse + " Recebeu " + atacante.Poder + " de dano do poder e está com " + defensor.vida + " de vida.");
        System.out.println("===========");
        
    }
}