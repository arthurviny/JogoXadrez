import { Peca } from './Peca.js';

export class Rei extends Peca {
    constructor(cor) {
        super(cor);
        this.nomePeca = "Rei";
    }

    isMovimentoValido(tabuleiro, linhaInicial, colunaInicial, linhaFinal, colunaFinal) {
        const pecaDestino = tabuleiro.getPeca(linhaFinal, colunaFinal);

        if (pecaDestino !== null && pecaDestino.getCor() === this.getCor()) {
            return false;
        }

        const diffLinha = Math.abs(linhaFinal - linhaInicial);
        const diffColuna = Math.abs(colunaFinal - colunaInicial);

        return diffLinha <= 1 && diffColuna <= 1;
    }

    toString() {
        return "R{}";
    }
}