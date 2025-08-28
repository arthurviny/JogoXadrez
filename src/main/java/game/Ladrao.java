package game;

public class Ladrao extends Peca {
    public Ladrao(String cor) {
        super(cor);
        this.nomePeca = "Ladrao";
    }

    @Override
    public boolean isMovimentoValido(Tabuleiro tabuleiro, int linhaInicial, int colunaInicial, int linhaFinal, int colunaFinal) {
        Peca pecaDestino = tabuleiro.getPeca(linhaFinal, colunaFinal);

        // Regra 1: Não pode mover para uma casa com uma peça da mesma cor.
        // (Esta regra continua a mesma, mas a movi para o final para evitar repetição de código)

        // --- Checagem do Movimento Normal (Diagonal ATÉ 2 casas) ---
        int diffLinha = Math.abs(linhaFinal - linhaInicial);
        int diffColuna = Math.abs(colunaFinal - colunaInicial);

        // Regra 2: Deve ser um movimento estritamente diagonal.
        if (diffLinha != diffColuna) {
            return false;
        }

        // Regra 3: A distância na diagonal deve ser de 1 ou 2 casas.
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

            // Se chegou até aqui, o movimento (de 1 ou 2 casas com caminho livre) é geometricamente válido.
            // Agora, checamos a peça de destino.
            if (pecaDestino == null || !pecaDestino.getCor().equals(this.getCor())) {
                return true;
            }
        }

        return false; // Se nenhuma das condições acima for atendida, o movimento é inválido.
    }

    @Override
    public String toString() {
        return "L{}";
    }
}