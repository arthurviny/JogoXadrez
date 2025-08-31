package game;

public class Rei extends Peca {
    public Rei(String cor) {
        super(cor);
        this.nomePeca = "Rei";
    }

    @Override
    public boolean isMovimentoValido(Tabuleiro tabuleiro, int linhaInicial, int colunaInicial, int linhaFinal, int colunaFinal) {
        
        int diffLinha = Math.abs(linhaInicial - linhaFinal);
        int diffColuna = Math.abs(colunaInicial - colunaFinal);
        boolean podeMover = diffLinha <= 1 && diffColuna <= 1;

        if (podeMover) {
            Peca pecaDestino = tabuleiro.getPeca(linhaFinal, colunaFinal);
            // Pode mover se a casa de destino estiver vazia OU tiver uma peça de cor diferente
            if (pecaDestino == null || !pecaDestino.getCor().equals(this.getCor())) {
                return true;
            }
        }

        return false; // Movimento inválido
    }

    @Override
    public String toString() {
        return "RE{}";
    }
}
