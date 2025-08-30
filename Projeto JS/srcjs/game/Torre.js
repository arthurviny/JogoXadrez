import { Peca } from './Peca.js';

export class Torre extends Peca {
    constructor(cor) {
        super(cor);
        this.nomePeca = "Torre";
    }
    
    // O método da instância simplesmente chama a ferramenta estática.
    isMovimentoValido(tabuleiro, linhaInicial, colunaInicial, linhaFinal, colunaFinal) {
        return Torre.checarMovimento(tabuleiro, linhaInicial, colunaInicial, linhaFinal, colunaFinal, this.getCor());
    }

    static checarMovimento(tabuleiro, linhaInicial, colunaInicial, linhaFinal, colunaFinal, corDaPeca) {
        const pecaDestino = tabuleiro.getPeca(linhaFinal, colunaFinal);

        if (pecaDestino !== null && pecaDestino.getCor() === corDaPeca) {
            return false;
        }

        if (linhaInicial !== linhaFinal && colunaInicial !== colunaFinal) {
            return false;
        }

        
        if (colunaInicial === colunaFinal) { // Movimento Vertical
            const passo = (linhaFinal > linhaInicial) ? 1 : -1;
            for (let i = linhaInicial + passo; i !== linhaFinal; i += passo) {
                if (tabuleiro.getPeca(i, colunaInicial) !== null) return false;
            }
        } else { // Movimento Horizontal
            const passo = (colunaFinal > colunaInicial) ? 1 : -1;
            for (let i = colunaInicial + passo; i !== colunaFinal; i += passo) {
                if (tabuleiro.getPeca(linhaInicial, i) !== null) return false;
            }
        }
        
        return true;
    }

    toString() {
        return "T{}";
    }
}