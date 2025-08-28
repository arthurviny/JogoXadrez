package game;

public abstract class Peca {
    public String nomePeca;

    public Peca(String cor) {
        this.cor = cor;
    }

    protected String cor;

    abstract public boolean isMovimentoValido(Tabuleiro tabuleiro, int linhaInicial, int colunaInicial, int linhaFinal, int colunaFinal);

    public String getNomePeca() {
        return nomePeca;
    }

    public String getCor() {
        return cor;
    }


}