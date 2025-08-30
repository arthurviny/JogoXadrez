import { Peca } from './Peca.js';

export class Bispo extends Peca {
    constructor(cor) {
        super(cor);
        this.nomePeca = "Bispo";
    }

 
    isMovimentoValido(tabuleiro, linhaInicial, colunaInicial, linhaFinal, colunaFinal) {
        return Bispo.checarMovimento(tabuleiro, linhaInicial, colunaInicial, linhaFinal, colunaFinal, this.getCor());
    }

    // Metodo tem aqui e la na torre pra rainha só juntar os 2
    static checarMovimento(tabuleiro, linhaInicial, colunaInicial, linhaFinal, colunaFinal, corDaPeca) {
        const pecaDestino = tabuleiro.getPeca(linhaFinal, colunaFinal);

        if (pecaDestino !== null && pecaDestino.getCor() === corDaPeca) {
            return false;
        }

        // Movimento deve ser perfeitamente diagonal.
        if (Math.abs(linhaFinal - linhaInicial) !== Math.abs(colunaFinal - colunaInicial)) {
            return false;
        }

        // O caminho precisa estar livre.
        const passoLinha = (linhaFinal > linhaInicial) ? 1 : -1;
        const passoColuna = (colunaFinal > colunaInicial) ? 1 : -1;

        let linhaAtual = linhaInicial + passoLinha;
        let colunaAtual = colunaInicial + passoColuna;

        while (linhaAtual !== linhaFinal) {
            if (tabuleiro.getPeca(linhaAtual, colunaAtual) !== null) {
                return false; // Caminho bloqueado.
            }
            linhaAtual += passoLinha;
            colunaAtual += passoColuna;
        }

        return true;
    }

    toString() {
        return `B{}${this.cor}`;
    }
}