import { Peca } from './Peca.js';

export class Cavalo extends Peca {
    constructor(cor) {

        super(cor); 
        this.nomePeca = "Cavalo";
    }

   
    isMovimentoValido(tabuleiro, linhaInicial, colunaInicial, linhaFinal, colunaFinal) {
       
        const pecaDestino = tabuleiro.getPeca(linhaFinal, colunaFinal);

        if (pecaDestino !== null && pecaDestino.getCor() === this.getCor()) {
            return false;
        }

        const diffLinha = Math.abs(linhaFinal - linhaInicial);
        const diffColuna = Math.abs(colunaFinal - colunaInicial);

        const ehMovimentoL = (diffLinha === 2 && diffColuna === 1) || (diffLinha === 1 && diffColuna === 2);

        return ehMovimentoL;
    }

    toString() {
        return "C{}";
    }
}