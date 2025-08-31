import { Peca } from './Peca.js';


export class Ladrao extends Peca {
    constructor(cor) {
        super(cor);
        this.nomePeca = "Ladrao";
    }

    isMovimentoValido(tabuleiro, linhaInicial, colunaInicial, linhaFinal, colunaFinal) {
        const pecaDestino = tabuleiro.getPeca(linhaFinal, colunaFinal);

   
        const diffLinha = Math.abs(linhaFinal - linhaInicial);
        const diffColuna = Math.abs(colunaFinal - colunaInicial);


        if (diffLinha !== diffColuna) {
            return false;
        }

    
        if (diffLinha > 0 && diffLinha <= 2) {

            if (diffLinha === 2) {
                const passoLinha = (linhaFinal > linhaInicial) ? 1 : -1;
                const passoColuna = (colunaFinal > colunaInicial) ? 1 : -1;

               
                if (tabuleiro.getPeca(linhaInicial + passoLinha, colunaInicial + passoColuna) !== null) {
                    return false; 
                }
            }

           
            if (pecaDestino === null || pecaDestino.getCor() !== this.getCor()) {
                return true;
            }
        }

       
        return false;
    }

    toString() {
        return "L{}";
    }
}