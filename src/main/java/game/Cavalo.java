package game;

public class Cavalo extends Peca {
    public Cavalo(String cor) {
        super(cor);
        this.nomePeca = "Cavalo";
    }

    @Override
    public boolean isMovimentoValido(Tabuleiro tabuleiro, int linhaInicial, int colunaInicial, int linhaFinal, int colunaFinal) {
        Peca pecaDestino = tabuleiro.getPeca(linhaFinal, colunaFinal);

        if (pecaDestino != null && pecaDestino.getCor().equals(this.getCor())) {
            return false;
        }

        int diffLinha = Math.abs(linhaFinal - linhaInicial);
        int diffColuna = Math.abs(colunaFinal - colunaInicial);

        boolean ehMovimentoL = (diffLinha == 2 && diffColuna == 1) || (diffLinha == 1 && diffColuna == 2);

        // Como o cavalo pula, não precisamos checar o caminho.
        return ehMovimentoL;
    }

    @Override
    public String toString() {
        return "C{}";
    }
}