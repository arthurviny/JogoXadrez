package game;

class Torre extends Peca {
    public Torre(String cor) {
        super(cor);
        this.nomePeca = "Torre";
    }

    @Override
    public boolean isMovimentoValido(Tabuleiro tabuleiro, int linhaInicial, int colunaInicial, int linhaFinal, int colunaFinal) {
        Peca pecaDestino = tabuleiro.getPeca(linhaFinal, colunaFinal);

        // Regra 1: Não pode capturar peça da mesma cor.
        if (pecaDestino != null && pecaDestino.getCor().equals(this.getCor())) {
            return false;
        }

        // Regra 2: Movimento deve ser reto (na mesma linha ou mesma coluna).
        if (linhaInicial != linhaFinal && colunaInicial != colunaFinal) {
            return false; // Se não for reto, é inválido.
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
        // Movimento Horizontal
        else { // linhaInicial == linhaFinal
            int passo = (colunaFinal > colunaInicial) ? 1 : -1;
            for (int i = colunaInicial + passo; i != colunaFinal; i += passo) {
                if (tabuleiro.getPeca(linhaInicial, i) != null) {
                    return false; // Caminho bloqueado.
                }
            }
        }

        return true; // Se passou por todas as regras, o movimento é válido.
    }

    @Override
    public String toString() {
        return "TO{}";
    }
}