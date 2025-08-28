package game;

public class BoboDaCorte extends Peca {
    private String modoAtual = "gklashgikasg";

    public BoboDaCorte(String cor) {
        super(cor);
        this.nomePeca = "BoboDaCorte";
    }

    public void setModo(String novoModo) {
        this.modoAtual = novoModo;
    }

    public String getModoAtual() {
        return modoAtual;
    }

    @Override
    public boolean isMovimentoValido(Tabuleiro tabuleiro, int linhaInicial, int colunaInicial, int linhaFinal, int colunaFinal) {

        // 1. Verificação de Peca Destino (pode ser feita uma vez)
        Peca pecaDestino = tabuleiro.getPeca(linhaFinal, colunaFinal);
        if (pecaDestino != null && pecaDestino.getCor().equals(this.getCor())) {
            return false;
        }

        int diffLinha = Math.abs(linhaFinal - linhaInicial);
        int diffColuna = Math.abs(colunaFinal - colunaInicial);

        // 2. Lógica de movimento baseada no modo
        switch(this.modoAtual) {
            case "Rainha": {
                // A Rainha pode se mover como uma Torre OU como um Bispo
                boolean ehMovimentoReto = (linhaInicial == linhaFinal || colunaInicial == colunaFinal);
                boolean ehMovimentoDiagonal = (diffLinha == diffColuna);

                // Se o movimento não for nem reto nem diagonal, já é inválido.
                if (!ehMovimentoReto && !ehMovimentoDiagonal) {
                    return false;
                }

                // Agora, verificamos se o caminho está livre
                // Movimento Reto
                if (ehMovimentoReto) {
                    // Lógica para checar o caminho reto (Vertical ou Horizontal)
                    int passo = (linhaFinal != linhaInicial) ? ((linhaFinal > linhaInicial) ? 1 : -1) : ((colunaFinal > colunaInicial) ? 1 : -1);
                    int i = (linhaFinal != linhaInicial) ? linhaInicial + passo : colunaInicial + passo;
                    while ((linhaFinal != linhaInicial && i != linhaFinal) || (colunaFinal != colunaInicial && i != colunaFinal)) {
                        Peca pecaNoCaminho = (linhaFinal != linhaInicial) ? tabuleiro.getPeca(i, colunaInicial) : tabuleiro.getPeca(linhaInicial, i);
                        if (pecaNoCaminho != null) return false;
                        i += passo;
                    }
                }
                // Movimento Diagonal
                if (ehMovimentoDiagonal) {
                    // Lógica para checar o caminho diagonal
                    int passoLinha = (linhaFinal > linhaInicial) ? 1 : -1;
                    int passoColuna = (colunaFinal > colunaInicial) ? 1 : -1;
                    int linhaAtual = linhaInicial + passoLinha;
                    int colunaAtual = colunaInicial + passoColuna;
                    while (linhaAtual != linhaFinal) {
                        if (tabuleiro.getPeca(linhaAtual, colunaAtual) != null) return false;
                        linhaAtual += passoLinha;
                        colunaAtual += passoColuna;
                    }
                }
                return true;
            }
            case "Rei": {
                return diffLinha <= 1 && diffColuna <= 1;
            }
            case "Cavalo": {
                return (diffLinha == 2 && diffColuna == 1) || (diffLinha == 1 && diffColuna == 2);
            }
            case "Ladrao": {

                if (diffLinha != diffColuna) {
                    return false;
                }

                if (diffLinha > 0 && diffLinha <= 2) {
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
            case "Templario": {
                if(diffLinha <= 7 || diffColuna <= 7) {
                    if (pecaDestino == null || !pecaDestino.getCor().equals(this.cor)) {
                        return true;
                    }
                    return false;
                }
                return false;
            }
            case "Principe": {

            }
            case "Torre": {
                if (pecaDestino != null && pecaDestino.getCor().equals(this.getCor())) {
                    return false;
                }

                // Regra 2: Movimento deve ser reto (na mesma linha ou mesma coluna).
                if (linhaInicial != linhaFinal && colunaInicial != colunaFinal) {
                    return false; // Se não for reto, é inválido.
                }

                // Movimento Vertical
                if (colunaInicial == colunaFinal) {
                    int passo = (linhaFinal > linhaInicial) ? 1 : -1;
                    for (int i = linhaInicial + passo; i != linhaFinal; i += passo) {
                        if (tabuleiro.getPeca(i, colunaInicial) != null) {
                            return false; // Caminho bloqueado.
                        }
                    }
                }
                // Movimento Horizontal
                else { // linhaInicial == linhaFinal
                    int passo = (colunaFinal > colunaInicial) ? 1 : -1;
                    for (int i = colunaInicial + passo; i != colunaFinal; i += passo) {
                        if (tabuleiro.getPeca(linhaInicial, i) != null) {
                            return false; // Caminho bloqueado.
                        }
                    }
                }

                return true; // Se passou por todas as regras, o movimento é válido.
            }
            case "Bispo": {
                if (pecaDestino != null && pecaDestino.getCor().equals(this.getCor())) {
                    return false;
                }

                if (Math.abs(linhaFinal - linhaInicial) != Math.abs(colunaFinal - colunaInicial)) {
                    return false;
                }

                int passoLinha = (linhaFinal > linhaInicial) ? 1 : -1;
                int passoColuna = (colunaFinal > colunaInicial) ? 1 : -1;

                int linhaAtual = linhaInicial + passoLinha;
                int colunaAtual = colunaInicial + passoColuna;

                while (linhaAtual != linhaFinal) {
                    if (tabuleiro.getPeca(linhaAtual, colunaAtual) != null) {
                        return false;
                    }
                    linhaAtual += passoLinha;
                    colunaAtual += passoColuna;
                }

                return true;
            }
            case "Peao": {
                int direcao = this.getCor().equals("branco") ? -1 : 1;
                if (colunaInicial == colunaFinal && pecaDestino == null) {
                    // Movimento de 1 casa para frente
                    if (linhaInicial + direcao == linhaFinal) {
                        return true;
                    }
                }

                // --- Movimento de Captura na Diagonal ---
                // Verifica se andou na diagonal (1 casa de lado e 1 para frente) e se há uma peça inimiga no destino
                if (Math.abs(colunaInicial - colunaFinal) == 1 && (linhaInicial + direcao == linhaFinal)) {
                    if (pecaDestino != null && !pecaDestino.getCor().equals(this.getCor())) {
                        return true;
                    }
                }

                return false; // Para todos os outros casos, o movimento é inválido
            }
            default:
                return false; // Retorna falso para qualquer modo desconhecido
        }
    }

    @Override
    public String toString() {
        return "BB{}";
    }
}