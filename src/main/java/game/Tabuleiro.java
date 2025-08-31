package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tabuleiro {
    private Peca[][] tabuleiro = new Peca[8][8];
    List<String> pecasNoTabuleiro = new ArrayList<>();

    public List<String> getPecasNoTabuleiro() {
        return pecasNoTabuleiro;
    }

    public Tabuleiro() {

        // Usando a bibilioteca Collections, criamos um arrayList com o nome de
        // todas as peças, que podem ser usadas durante o jogo
        List<String> nomePecas = new ArrayList<>();
        nomePecas.add("Torre");
        nomePecas.add("Cavalo");
        nomePecas.add("BoboDaCorte");
        nomePecas.add("Principe");
        nomePecas.add("Ladrao");
        nomePecas.add("Bispo");
        nomePecas.add("Templario");

        // embaralha as peças (variação no setup inicial)
        Collections.shuffle(nomePecas);

        List<String> FiladeTras = new ArrayList<>();


        for (int i = 0; i < 6; i++) {
            FiladeTras.add(nomePecas.get(i));
        }
        FiladeTras.add("Rei");
        FiladeTras.add("Rainha");

        pecasNoTabuleiro = new ArrayList<>(FiladeTras);
        pecasNoTabuleiro.add("Peao");

        Collections.shuffle(FiladeTras);

        // Colocar peças pretas
        for (int j = 0; j < 8; j++) {
            Peca peca = criarPeca(FiladeTras.get(j), "preto");
            tabuleiro[0][j] = peca;
            Peca peao = criarPeca("Peao", "preto");
            tabuleiro[1][j] = peao;
        }

        // Colocar peças branca
        for (int j = 0; j < 8; j++) {
            Peca peca = criarPeca(FiladeTras.get(j), "branco");
            tabuleiro[7][j] = peca;
            Peca peao = criarPeca("Peao", "branco");
            tabuleiro[6][j] = peao;
        }
    }

    public boolean check(int linha, int coluna, String corAtacante, boolean checandoMovimentoDoRei) {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                Peca peca = tabuleiro[i][j];

                if(peca != null && peca.getCor().equals(corAtacante)) {
                    if (peca instanceof Peao) {
                        int direcao = peca.getCor().equals("branco") ? -1 : 1;


                        if (linha == i + direcao && Math.abs(coluna - j) == 1) {
                            return true; // É um ataque de peão válido.
                        }
                    } else if (peca instanceof BoboDaCorte) {
                        BoboDaCorte bobo = (BoboDaCorte) peca;
                        String modoOriginal = bobo.getModoAtual();
                        if (checandoMovimentoDoRei) {
                            List<String> listaDeModosBoboAtual = bobo.getListaModosBobo();

                            for (String modo : listaDeModosBoboAtual) {
                                bobo.setModo(modo); // Altera o modo temporariamente
                                // Verifica se o Bobo, neste modo, pode atacar o Rei
                                if (bobo.isMovimentoValido(this, i, j, linha, coluna)) {
                                    bobo.setModo(modoOriginal); // Restaura o modo original
                                    return true; // Rei está em cheque
                                }
                            }
                        } else {
                            if (bobo.isMovimentoValido(this, i, j, linha, coluna)) {
                                return true;
                            }
                        }
                    } else {

                        peca.isMovimentoValido(this, i, j, linha, coluna);
                        if (peca.isMovimentoValido(this, i, j, linha, coluna)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public int[] encontrarRei(String corDoRei) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Peca peca = getPeca(i, j);
                if (peca != null && peca.getNomePeca().equals("Rei") && peca.getCor().equals(corDoRei)) {
                    return new int[]{i, j};
                }
            }
        }
        return null; // Não deveria acontecer em um jogo normal.
    }

    public int[] encontrarHeroi(String corDoHeroi) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Peca peca = getPeca(i, j);
                if (peca != null && peca.getNomePeca().equals("Principe") && peca.getCor().equals(corDoHeroi)) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    // Metodo para criar uma cópia exata do tabuleiro atual.
    public Tabuleiro clonar() {
        Tabuleiro clone = new Tabuleiro();
        Peca[][] novoTabuleiroArray = new Peca[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // não modificoa  as peças em si, apenas suas posicoes.
                novoTabuleiroArray[i][j] = this.tabuleiro[i][j];
            }
        }
        clone.setTabuleiro(novoTabuleiroArray);
        return clone;
    }

    public Peca[][] getTabuleiro() {
        return tabuleiro;
    }

    public void setTabuleiro(Peca[][] tabuleiro) {
        this.tabuleiro = tabuleiro;
    }

    public Peca getPeca(int linha, int coluna) {
        return tabuleiro[linha][coluna];
    }

    public void setPeca(int linha, int coluna, Peca peca) {
        tabuleiro[linha][coluna] = peca;
    }

    public void setListaModosBobo(int linha, int coluna, List<String> listaAtualModos) {
        Peca peca = getPeca(linha, coluna);

        if (peca instanceof BoboDaCorte) {
            ((BoboDaCorte) peca).setListaModosBobo(listaAtualModos);
        }
    }

    public void setModoDoBobo(int linha, int coluna, String novoModo) {
        Peca peca = getPeca(linha, coluna);

        if (peca instanceof BoboDaCorte) {
            ((BoboDaCorte) peca).setModo(novoModo);
        }
    }

    public void setFuriaHeroi(int linha, int coluna, boolean estaEmCheck) {
        Peca peca = getPeca(linha, coluna);

        if (peca instanceof Heroi) {
            ((Heroi) peca).setReiEstaEmCheck(estaEmCheck);
        }
    }

    public Peca criarPeca(String nome, String cor) {
        switch(nome) {
            case "Rainha":
                return new Rainha(cor);
            case "Rei":
                return new Rei(cor);
            case "BoboDaCorte":
                return new BoboDaCorte(cor);
            case "Ladrao":
                return new Ladrao(cor);
            case "Bispo":
                return new Bispo(cor);
            case "Principe":
                return new Heroi(cor); // talvez você queira renomear Heroi -> Principe
            case "Cavalo":
                return new Cavalo(cor);
            case "Torre":
                return new Torre(cor);
            case "Templario":
                return new Templario(cor);
            case "Peao":
            default:
                return new Peao(cor);
        }
    }

    /**
     * Move uma peça de (linhaInicial, colunaInicial) para (linhaFinal, colunaFinal).
     */
    public void moverPeca(int linhaInicial, int colunaInicial, int linhaFinal, int colunaFinal) {
        Peca peca = getPeca(linhaInicial, colunaInicial);

        if (peca == null) {
            System.out.println("Nenhuma peça encontrada na posição inicial.");
            return;
        }

        setPeca(linhaFinal, colunaFinal, peca);   // coloca peça no destino
        setPeca(linhaInicial, colunaInicial, null); // limpa posição inicial

      /*  System.out.println("✅ " + peca.getNomePeca() + " (" + peca.getCor() + ") movida de " +
                "(" + linhaInicial + "," + colunaInicial + ") para (" + linhaFinal + "," + colunaFinal + ")"); */
    }
}
