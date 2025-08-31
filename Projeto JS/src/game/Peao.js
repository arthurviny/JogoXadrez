import { Peca } from './Peca.js';

export class Peao extends Peca {
    constructor(cor) {
        super(cor);
        this.nomePeca = "Peao";
    }

    isMovimentoValido(tabuleiro, linhaInicial, colunaInicial, linhaFinal, colunaFinal) {
        const pecaDestino = tabuleiro.getPeca(linhaFinal, colunaFinal);
        const direcao = this.getCor() === "branco" ? -1 : 1;


        if (colunaInicial === colunaFinal && pecaDestino === null) {
            
            if (linhaInicial + direcao === linhaFinal) {
                return true;
            }

            const ehPrimeiroMovimento = (this.getCor() === "branco" && linhaInicial === 6) || (this.getCor() === "preto" && linhaInicial === 1);
            if (ehPrimeiroMovimento && (linhaInicial + 2 * direcao === linhaFinal)) {
                
                const pecaNoMeio = tabuleiro.getPeca(linhaInicial + direcao, colunaInicial);
                if (pecaNoMeio === null) {
                    return true;
                }
            }
        }

        if (Math.abs(colunaInicial - colunaFinal) === 1 && (linhaInicial + direcao === linhaFinal)) {
            
            // A casa de destino DEVE ter uma peça inimiga.
            if (pecaDestino !== null && pecaDestino.getCor() !== this.getCor()) {
                return true;
            }
        }

        return false;
    }

    toString() {
        return "P{}";
    }
}