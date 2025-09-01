package game;

class Torre extends Peca {
    public Torre(String cor) {
        super(cor);
        this.nomePeca = "Torre";
    }

    @Override
    public boolean isMovimentoValido(Tabuleiro tabuleiro, int linhaInicial, int colunaInicial, int linhaFinal, int colunaFinal) {
        Peca pecaDestino = tabuleiro.getPeca(linhaFinal, colunaFinal);

        if (pecaDestino != null && pecaDestino.getCor().equals(this.getCor())) {
            return false;
        }

        if (linhaInicial != linhaFinal && colunaInicial != colunaFinal) {
            return false; 
        }

        // Movimento Vertical
        if (colunaInicial == colunaFinal) {
            int passo = (linhaFinal > linhaInicial) ? 1 : -1;
            for (int i = linhaInicial + passo; i != linhaFinal; i += passo) {
                if (tabuleiro.getPeca(i, colunaInicial) != null) {
                    return false; // Caminho bloqueado.
                }
            }
        }

        else { // linhaInicial == linhaFinal
            int passo = (colunaFinal > colunaInicial) ? 1 : -1;
            for (int i = colunaInicial + passo; i != colunaFinal; i += passo) {
                if (tabuleiro.getPeca(linhaInicial, i) != null) {
                    return false; // Caminho bloqueado.
                }
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return "TO{}";
    }
}