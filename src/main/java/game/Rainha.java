package game;

public class Rainha extends Peca {
    public Rainha(String cor) {
        super(cor);
        this.nomePeca = "Rainha";
    }

    @Override
    public boolean isMovimentoValido(Tabuleiro tabuleiro, int linhaInicial, int colunaInicial, int linhaFinal, int colunaFinal) {
        Peca pecaDestino = tabuleiro.getPeca(linhaFinal, colunaFinal);

        // Regra 1: Não pode capturar peça da mesma cor.
        if (pecaDestino != null && pecaDestino.getCor().equals(this.getCor())) {
            return false;
        }

        // Verifica se o movimento é reto (como uma Torre)
        boolean ehMovimentoReto = (linhaInicial == linhaFinal || colunaInicial == colunaFinal);

        // Verifica se o movimento é diagonal (como um Bispo)
        boolean ehMovimentoDiagonal = (Math.abs(linhaFinal - linhaInicial) == Math.abs(colunaFinal - colunaInicial));

        if (!ehMovimentoReto && !ehMovimentoDiagonal) {
            return false; // Se não for reto NEM diagonal, é inválido.
        }

        if (ehMovimentoReto) {
            // Movimento Vertical
            if (colunaInicial == colunaFinal) {
                int passo = (linhaFinal > linhaInicial) ? 1 : -1;
                for (int i = linhaInicial + passo; i != linhaFinal; i += passo) {
                    if (tabuleiro.getPeca(i, colunaInicial) != null) return false;
                }
            }
           
            else {
                int passo = (colunaFinal > colunaInicial) ? 1 : -1;
                for (int i = colunaInicial + passo; i != colunaFinal; i += passo) {
                    if (tabuleiro.getPeca(linhaInicial, i) != null) return false;
                }
            }
        }

        // Movimento Diagonal
        if (ehMovimentoDiagonal) {
            int passoLinha = (linhaFinal > linhaInicial) ? 1 : -1;
            int passoColuna = (colunaFinal > colunaInicial) ? 1 : -1;
            int linhaAtual = linhaInicial + passoLinha;
            int colunaAtual = colunaInicial + passoColuna;
            while (linhaAtual != linhaFinal) {
                if (tabuleiro.getPeca(linhaAtual, colunaAtual) != null) return false;
                linhaAtual += passoLinha;
                colunaAtual += passoColuna;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return "RA{}";
    }
}
