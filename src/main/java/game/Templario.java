package game;

class Templario extends Peca{
    public Templario(String cor) {
        super(cor);
        this.nomePeca = "Templario";
    }

    @Override
    public boolean isMovimentoValido(Tabuleiro tabuleiro, int linhaInicial, int colunaInicial, int linhaFinal, int colunaFinal) {
        Peca pecaDestino = tabuleiro.getPeca(linhaFinal, colunaFinal);

        // Regra básica: não pode capturar uma peça da mesma cor.
        if (pecaDestino != null && pecaDestino.getCor().equals(this.getCor())) {
            return false;
        }

        int diffLinha = Math.abs(linhaInicial - linhaFinal);
        int diffColuna = Math.abs(colunaInicial - colunaFinal);

        // --- REGRA 1: Movimento vertical de até 4 casas ---
        if (colunaInicial == colunaFinal && (diffLinha > 0 && diffLinha <= 4)) {
            int passo = (linhaFinal > linhaInicial) ? 1 : -1;
            for (int i = linhaInicial + passo; i != linhaFinal; i += passo) {
                if (tabuleiro.getPeca(i, colunaInicial) != null) {
                    return false; // Caminho bloqueado.
                }
            }
            return true;
        }

        // --- REGRA 2: Movimento especial (3 vertical, 1 horizontal) SEM PULAR ---
        if (diffLinha == 3 && diffColuna == 1) {
            // Este movimento NÃO pula peças. Verificamos as duas casas no caminho vertical.
            int passoLinha = (linhaFinal > linhaInicial) ? 1 : -1;

            // Verifica a primeira casa no caminho vertical
            if (tabuleiro.getPeca(linhaInicial + passoLinha, colunaInicial) != null) {
                return false; // Caminho bloqueado na primeira casa.
            }

            // Verifica a segunda casa no caminho vertical
            if (tabuleiro.getPeca(linhaInicial + (2 * passoLinha), colunaInicial) != null) {
                return false; // Caminho bloqueado na segunda casa.
            }

            // Se o caminho vertical estiver livre, o movimento é válido.
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "TP{}";
    }
}

