package game;

class Heroi extends Peca {
    private boolean reiEstaEmCheck = false;
    public int forca;

    public void setReiEstaEmCheck(boolean reiEstaEmCheck) {
        this.reiEstaEmCheck = reiEstaEmCheck;
    }

    public Heroi(String cor) {
        super(cor);
        this.nomePeca = "Principe";
        forca = 1;
    }

    @Override
    public boolean isMovimentoValido(Tabuleiro tabuleiro, int linhaInicial, int colunaInicial, int linhaFinal, int colunaFinal) {
        // Pega a peça de destino
        if (this.reiEstaEmCheck) {
            forca = 2;
        }

        Peca pecaDestino = tabuleiro.getPeca(linhaFinal, colunaFinal);

        // A direção do movimento depende da cor da peça
        int direcao = this.getCor().equals("branco") ? -forca : forca;

        // A linha final deve ser igual a linha inicial + direção, garantindo assim, o deslocamento de apenas uma linha
        if (linhaFinal == linhaInicial + direcao) {

            // Pegamos a diferença de coluna
            int diffColuna = Math.abs(colunaFinal - colunaInicial);
            if (diffColuna <= 1) {

                // Se a casa de destino estiver vazia ou se a casa de destino tiver uma peça de outra cor, é um movimento válido
                return pecaDestino == null || !pecaDestino.getCor().equals(this.getCor());
            }
        }

        // Para qualquer outro movimento, a condição é inválida.
        return false;
    }

    @Override
    public String toString() {
        return "PR{}";
    }
}