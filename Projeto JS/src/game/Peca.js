export class Peca {
    constructor(cor) {
        this.cor = cor;
        this.nomePeca = "";
    }

    getCor() {
        return this.cor;
    }

    getNomePeca() {
        return this.nomePeca;
    }

    isMovimentoValido(tabuleiro, linhaInicial, colunaInicial, linhaFinal, colunaFinal) {
        throw new Error("O método 'isMovimentoValido' precisa ser implementado pela subclasse.");
    }
}