package game;

public class Rei extends Peca {
    public Rei(String cor) {
        super(cor);
        this.nomePeca = "Rei";
    }

    @Override
    public boolean isMovimentoValido(Tabuleiro tabuleiro, int linhaInicial, int colunaInicial, int linhaFinal, int colunaFinal) {
        // Calcula a distância do movimento nas linhas e colunas
        int diffLinha = Math.abs(linhaInicial - linhaFinal);
        int diffColuna = Math.abs(colunaInicial - colunaFinal);

        // O Rei só pode se mover 1 casa em qualquer direção.
        // Então, a diferença tanto na linha quanto na coluna deve ser no máximo 1.
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
