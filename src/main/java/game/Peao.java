package game;

public class Peao extends Peca {
    public Peao(String cor) {
        super(cor);
        this.nomePeca = "Peao";
    }

    @Override
    public boolean isMovimentoValido(Tabuleiro tabuleiro, int linhaInicial, int colunaInicial, int linhaFinal, int colunaFinal) {
        Peca pecaDestino = tabuleiro.getPeca(linhaFinal, colunaFinal);
        int direcao = this.getCor().equals("branco") ? -1 : 1; // Peão branco anda "para cima" (aumenta a linha), preto "para baixo" (diminui a linha)

        // Verifica se a coluna é a mesma (movimento reto) e o destino está vazio
        if (colunaInicial == colunaFinal && pecaDestino == null) {
            // Movimento de 1 casa para frente
            if (linhaInicial + direcao == linhaFinal) {
                return true;
            }
            // Movimento de 2 casas no primeiro lance
            boolean ehPrimeiroMovimento = (this.getCor().equals("branco") && linhaInicial == 6) || (this.getCor().equals("preto") && linhaInicial == 1);
            if (ehPrimeiroMovimento && (linhaInicial + 2 * direcao == linhaFinal)) {
                // Verifica se o caminho está livre
                Peca pecaNoMeio = tabuleiro.getPeca(linhaInicial + direcao, colunaInicial);
                if (pecaNoMeio == null) {
                    return true;
                }
            }
        }

        // Verifica se andou na diagonal (1 casa de lado e 1 para frente) e se há uma peça inimiga no destino
        if (Math.abs(colunaInicial - colunaFinal) == 1 && (linhaInicial + direcao == linhaFinal)) {
            if (pecaDestino != null && !pecaDestino.getCor().equals(this.getCor())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "P{}";
    }
}