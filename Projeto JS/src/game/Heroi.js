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
    
        let pecaDestino = tabuleiro.getPeca(linhaFinal, colunaFinal);
        if (pecaDestino !== null && pecaDestino.getCor() === this.getCor()) {
            return false;
        }

        let diffLinha = Math.abs(linhaFinal - linhaInicial);
        let diffColuna = Math.abs(colunaFinal - colunaInicial);

        if (pecaDestino !== null && pecaDestino.getCor() === this.getCor()) {
            return false;
        }

        // Só pode andar exatamente 1 linha na direção "pra frente"
        if (diffLinha <= 1 && diffLinha !== 0 && diffColuna <= 1) {
            return true;
        }

        if (diffLinha === 2 && diffColuna === 0) {
            let passoLinha = (linhaFinal > linhaInicial) ? 1 : -1;
            let passoColuna = (colunaFinal > colunaInicial) ? 1 : -1;

            // Verifica a casa intermediária
            if (tabuleiro.getPeca(linhaInicial + passoLinha, colunaInicial) !== null) {
                return false; // Caminho bloqueado, movimento inválido.
            }
            return true;
        }

        // --- MODO FÚRIA ---
        if (this.reiEstaEmCheck) {
            // Regra da Fúria: Pode mover 1 ou 2 casas, para frente ou para trás, reto ou na diagonal.

            // 1. Verifica se a distância está correta (1 ou 2 casas na vertical).
            if (pecaDestino !== null && pecaDestino.getCor() === this.getCor()) {
                return false;
            }

            // Só pode andar exatamente 1 linha na direção "pra frente"
            if (diffLinha <= 1 && diffLinha !== 0 && diffColuna <= 1) {
                return true;
            }

            if (diffLinha === 2 || diffLinha === 3 && diffColuna === 0) {
                let passoLinha = (linhaFinal > linhaInicial) ? 1 : -1;
                let linhaAtual = linhaInicial + passoLinha;

                // Verifica a casa intermediária
                while (linhaAtual < linhaFinal) {
                    if (tabuleiro.getPeca(linhaAtual, colunaInicial) !== null) {
                        return false; // Caminho bloqueado
                    }
                    linhaAtual += passoLinha;
                }
                return true;
            }
        }
      
        return false;
    }

    toString() {
        return "PR{}";
    }
}