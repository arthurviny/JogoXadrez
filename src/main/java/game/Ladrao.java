package game;

public class Ladrao extends Peca {
    public Ladrao(String cor) {
        super(cor);
        this.nomePeca = "Ladrao";
    }

    @Override
    public boolean isMovimentoValido(Tabuleiro tabuleiro, int linhaInicial, int colunaInicial, int linhaFinal, int colunaFinal) {
        Peca pecaDestino = tabuleiro.getPeca(linhaFinal, colunaFinal);

        // Checagem do Movimento normal -Diagonal ATÉ 2 casa
        int diffLinha = Math.abs(linhaFinal - linhaInicial);
        int diffColuna = Math.abs(colunaFinal - colunaInicial);

        if (diffLinha != diffColuna) {
            return false;
        }

        if (diffLinha > 0 && diffLinha <= 2) {
            // Se o movimento for de 2 casas, o caminho precisa estar livre.
            if (diffLinha == 2) {
                int passoLinha = (linhaFinal > linhaInicial) ? 1 : -1;
                int passoColuna = (colunaFinal > colunaInicial) ? 1 : -1;

                // Verifica a casa intermediária
                if (tabuleiro.getPeca(linhaInicial + passoLinha, colunaInicial + passoColuna) != null) {
                    return false; // Caminho bloqueado, movimento inválido.
                }
            }

            if (pecaDestino == null || !pecaDestino.getCor().equals(this.getCor())) {
                return true;
            }
        }

        return false; 
    }

    @Override
    public String toString() {
        return "L{}";
    }
}