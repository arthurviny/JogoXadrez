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
        // Verificação padrão: não pode capturar peça da mesma cor.
        Peca pecaDestino = tabuleiro.getPeca(linhaFinal, colunaFinal);
        if (pecaDestino != null && pecaDestino.getCor().equals(this.getCor())) {
            return false;
        }

        int diffLinha = Math.abs(linhaFinal - linhaInicial);
        int diffColuna = Math.abs(colunaFinal - colunaInicial);

        // --- MODO FÚRIA ---
        if (this.reiEstaEmCheck) {
            // Regra da Fúria: Pode mover 1 ou 2 casas, para frente ou para trás, reto ou na diagonal.

            // 1. Verifica se a distância está correta (1 ou 2 casas na vertical).
            if (diffLinha > 0 && diffLinha <= 2) {

                // 2. Verifica se o movimento lateral está correto (reto ou 1 casa para o lado).
                if (diffColuna <= 1) {

                    // 3. Se for um movimento de 2 casas em linha RETA, o caminho precisa estar livre.
                    if (diffLinha == 2 && diffColuna == 0) {
                        int passoLinha = (linhaFinal > linhaInicial) ? 1 : -1; // Direção do movimento
                        // Verifica a casa intermediária
                        if (tabuleiro.getPeca(linhaInicial + passoLinha, colunaInicial) != null) {
                            return false; // Caminho bloqueado.
                        }
                    }

                    // Se passou por todas as checagens, o movimento de fúria é válido.
                    return true;
                }
            }
        }

        else {

            int direcao = this.getCor().equals("branco") ? -1 : 1;


            if (linhaFinal == linhaInicial + direcao) {

                // 2. Verifica se o movimento lateral está correto (reto ou 1 casa para o lado).
                if (diffColuna <= 1) {
                    return true;
                }
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