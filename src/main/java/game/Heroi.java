package game;

public class Heroi extends Peca {
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
        // Verificação padrão: não pode capturar peça da mesma cor.
        Peca pecaDestino = tabuleiro.getPeca(linhaFinal, colunaFinal);
        if (pecaDestino != null && pecaDestino.getCor().equals(this.getCor())) {
            return false;
        }

        int diffLinha = Math.abs(linhaFinal - linhaInicial);
        int diffColuna = Math.abs(colunaFinal - colunaInicial);

        if (pecaDestino != null && pecaDestino.getCor().equals(this.getCor())) {
            return false;
        }

        // Só pode andar exatamente 1 linha na direção "pra frente"
        if (diffLinha <= 1 && diffLinha != 0 && diffColuna <= 1) {
            return true;
        }

        if (diffLinha == 2 && diffColuna == 0) {
            int passoLinha = (linhaFinal > linhaInicial) ? 1 : -1;
            int passoColuna = (colunaFinal > colunaInicial) ? 1 : -1;

            // Verifica a casa intermediária
            if (tabuleiro.getPeca(linhaInicial + passoLinha, colunaInicial) != null) {
                return false; // Caminho bloqueado, movimento inválido.
            }
            return true;
        }

        // --- MODO FÚRIA ---
        if (this.reiEstaEmCheck) {
            // Regra da Fúria: Pode mover 1 ou 2 casas, para frente ou para trás, reto ou na diagonal.

            // 1. Verifica se a distância está correta (1 ou 2 casas na vertical).
            if (pecaDestino != null && pecaDestino.getCor().equals(this.getCor())) {
                return false;
            }

            // Só pode andar exatamente 1 linha na direção "pra frente"
            if (diffLinha <= 1 && diffLinha != 0 && diffColuna <= 1) {
                return true;
            }

            if (diffLinha == 2 || diffLinha == 3 && diffColuna == 0) {
                int passoLinha = (linhaFinal > linhaInicial) ? 1 : -1;
                int linhaAtual = linhaInicial + passoLinha;

                // Verifica a casa intermediária
                while (linhaAtual < linhaFinal) {
                    if (tabuleiro.getPeca(linhaAtual, colunaInicial) != null) {
                        return false; // Caminho bloqueado, movimento inválido.
                    }
                    linhaAtual += passoLinha;
                }
                return true;
            }
        }
        // Se não se encaixar em nenhuma das regras, o movimento é inválido.
        return false;
    }


    @Override
    public String toString() {
        return "PR{}";
    }
}