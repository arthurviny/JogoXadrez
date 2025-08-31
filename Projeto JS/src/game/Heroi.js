// Importa a classe Peca para herdar dela
import { Peca } from './Peca.js';

export class Heroi extends Peca {
    constructor(cor) {
        super(cor);
        this.nomePeca = "Principe";
        this.reiEstaEmCheck = false; 
    }

    setReiEstaEmCheck(estaEmCheck) {
        this.reiEstaEmCheck = estaEmCheck;
    }

    isMovimentoValido(tabuleiro, linhaInicial, colunaInicial, linhaFinal, colunaFinal) {
        
        const pecaDestino = tabuleiro.getPeca(linhaFinal, colunaFinal);
        if (pecaDestino !== null && pecaDestino.getCor() === this.getCor()) {
            return false;
        }

        const diffLinha = Math.abs(linhaFinal - linhaInicial);
        const diffColuna = Math.abs(colunaFinal - colunaFinal);

        if (diffLinha <= 1 && diffColuna <= 1 && (diffLinha + diffColuna > 0)) {
            return true;
        }

        
        if (diffLinha === 2 && diffColuna === 0) {
            const passoLinha = (linhaFinal > linhaInicial) ? 1 : -1;
            // Verifica a casa intermediária
            if (tabuleiro.getPeca(linhaInicial + passoLinha, colunaInicial) === null) {
                return true; // Caminho livre
            }
        }
        
        // Furia

        if (this.reiEstaEmCheck) {
            
            if (diffLinha === 2 && diffColuna > 0) { 
                 return true;
            }

            
            if (diffLinha === 3 && diffColuna === 0) {
                const passoLinha = (linhaFinal > linhaInicial) ? 1 : -1;
                // Verifica as 2 casas intermediárias
                const casa1 = tabuleiro.getPeca(linhaInicial + passoLinha, colunaInicial);
                const casa2 = tabuleiro.getPeca(linhaInicial + (2 * passoLinha), colunaInicial);
                if (casa1 === null && casa2 === null) {
                    return true; // Caminho livre
                }
            }
        }

        // Se nenhuma das condições acima for atendida, o movimento é inválido.
        return false;
    }

    toString() {
        return "PR{}";
    }
}