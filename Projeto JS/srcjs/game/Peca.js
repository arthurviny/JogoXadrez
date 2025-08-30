export class Peca {
    constructor(cor) {
        this.cor = cor;
        this.nomePeca = "";
    }

    getCor() {
        return this.cor;
    }

    isMovimentoValido(tabuleiro, linhaInicial, colunaInicial, linhaFinal, colunaFinal) {
        throw new Error("A subclasse implementa");
    }
}