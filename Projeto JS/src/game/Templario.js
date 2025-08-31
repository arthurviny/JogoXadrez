import { Peca } from './Peca.js';

export class Templario extends Peca {
    constructor(cor) {
        super(cor);
        this.nomePeca = "Templario";
    }

    isMovimentoValido(tabuleiro, linhaInicial, colunaInicial, linhaFinal, colunaFinal) {
        const pecaDestino = tabuleiro.getPeca(linhaFinal, colunaFinal);

        if (pecaDestino !== null && pecaDestino.getCor() === this.getCor()) {
            return false;
        }

        const diffLinha = Math.abs(linhaInicial - linhaFinal);
        const diffColuna = Math.abs(colunaInicial - colunaFinal);

        // --- REGRA 1: Movimento vertical de até 4 casas ---
        if (colunaInicial === colunaFinal && (diffLinha > 0 && diffLinha <= 4)) {
            const passo = (linhaFinal > linhaInicial) ? 1 : -1;
            for (let i = linhaInicial + passo; i !== linhaFinal; i += passo) {
                if (tabuleiro.getPeca(i, colunaInicial) !== null) {
                    return false; // Caminho bloqueado.
                }
            }
            return true;
        }

        
        if (diffLinha === 3 && diffColuna === 1) {
            const passoLinha = (linhaFinal > linhaInicial) ? 1 : -1;

            // Verifica as duas casas no caminho vertical.
            if (tabuleiro.getPeca(linhaInicial + passoLinha, colunaInicial) !== null) {
                return false;
            }
            if (tabuleiro.getPeca(linhaInicial + (2 * passoLinha), colunaInicial) !== null) {
                return false;
            }

            return true; // Caminho livre, movimento válido.
        }

        return false;
    }

    toString() {
        return "TP{}";
    }
}