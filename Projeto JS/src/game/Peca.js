export class Peca {
    constructor(cor) {
        this.cor = cor;
        this.nomePeca = "";
    }

    getCor() {
        return this.cor;
    }

    // CORREÇÃO: Garanta que o nome do método está em camelCase correto.
    getNomePeca() {
        return this.nomePeca;
    }

    isMovimentoValido(tabuleiro, linhaInicial, colunaInicial, linhaFinal, colunaFinal) {
        throw new Error("O método 'isMovimentoValido' precisa ser implementado pela subclasse.");
    }
}