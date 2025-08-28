package game;

class Bispo extends Peca {
    public Bispo(String cor) {
        super(cor);
        this.nomePeca = "Bispo";
    }
    @Override
    public boolean isMovimentoValido(Tabuleiro tabuleiro, int linhaInicial, int colunaInicial, int linhaFinal, int colunaFinal) {
        Peca pecaDestino = tabuleiro.getPeca(linhaFinal, colunaFinal);

        if (pecaDestino != null && pecaDestino.getCor().equals(this.getCor())) {
            return false;
        }

        if (Math.abs(linhaFinal - linhaInicial) != Math.abs(colunaFinal - colunaInicial)) {
            return false;
        }

        int passoLinha = (linhaFinal > linhaInicial) ? 1 : -1;
        int passoColuna = (colunaFinal > colunaInicial) ? 1 : -1;

        int linhaAtual = linhaInicial + passoLinha;
        int colunaAtual = colunaInicial + passoColuna;

        while (linhaAtual != linhaFinal) {
            if (tabuleiro.getPeca(linhaAtual, colunaAtual) != null) {
                return false;
            }
            linhaAtual += passoLinha;
            colunaAtual += passoColuna;
        }

        return true;
    }

    @Override
    public String toString() {
        return "B{}" + cor;
    }
}