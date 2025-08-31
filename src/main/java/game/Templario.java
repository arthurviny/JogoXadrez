package game;

class Templario extends Peca{
    public Templario(String cor) {
        super(cor);
        this.nomePeca = "Templario";
    }

    @Override
    public boolean isMovimentoValido(Tabuleiro tabuleiro, int linhaInicial, int colunaInicial, int linhaFinal, int colunaFinal) {
        Peca pecaDestino = tabuleiro.getPeca(linhaFinal, colunaFinal);

    
        if (pecaDestino != null && pecaDestino.getCor().equals(this.getCor())) {
            return false;
        }

        int diffLinha = Math.abs(linhaInicial - linhaFinal);
        int diffColuna = Math.abs(colunaInicial - colunaFinal);

        // Até 4 casas pra frente
        if (colunaInicial == colunaFinal && (diffLinha > 0 && diffLinha <= 4)) {
            int passo = (linhaFinal > linhaInicial) ? 1 : -1;
            for (int i = linhaInicial + passo; i != linhaFinal; i += passo) {
                if (tabuleiro.getPeca(i, colunaInicial) != null) {
                    return false; // Caminho bloqueado.
                }
            }
            return true;
        }

        // Moivmento em cruz
        if (diffLinha == 3 && diffColuna == 1) {
            // Este movimento NÃO pula peças
            int passoLinha = (linhaFinal > linhaInicial) ? 1 : -1;

            // Verifica a primeira casa no caminho vertical
            if (tabuleiro.getPeca(linhaInicial + passoLinha, colunaInicial) != null) {
                return false; // Caminho bloqueado na primeira casa.
            }

            // Verifica a segunda casa no caminho vertical
            if (tabuleiro.getPeca(linhaInicial + (2 * passoLinha), colunaInicial) != null) {
                return false;
            }
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "TP{}";
    }
}

