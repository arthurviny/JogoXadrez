import { Peca } from './Peca.js';

export class Rainha extends Peca {
    constructor(cor) {
        super(cor);
        this.nomePeca = "Rainha";
    }

    isMovimentoValido(tabuleiro, linhaInicial, colunaInicial, linhaFinal, colunaFinal) {
    
        const podeMoverComoTorre = Torre.checarMovimento(tabuleiro, linhaInicial, colunaInicial, linhaFinal, colunaFinal, this.getCor());
        const podeMoverComoBispo = Bispo.checarMovimento(tabuleiro, linhaInicial, colunaInicial, linhaFinal, colunaFinal, this.getCor());

        return podeMoverComoTorre || podeMoverComoBispo;
    }

    toString() {
        return "Q{}"; // Q de Queen em inglês para não confundir com R de Rei
    }
}