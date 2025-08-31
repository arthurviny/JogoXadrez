// Importa a classe Peca para herdar dela
import { Peca } from './Peca.js';
// Importa as classes cujos movimentos estáticos podem ser reutilizados
import { Torre } from './Torre.js';
import { Bispo } from './Bispo.js';
import { Rainha } from './Rainha.js';

export class BoboDaCorte extends Peca {
    constructor(cor) {
        super(cor);
        this.nomePeca = "BoboDaCorte";
        // O modo inicial "nulo" força o jogador a escolher um modo com clique direito
        this.modoAtual = "nulo"; 
        this.listaModosBobo = []; // Equivalente ao new ArrayList<>()
    }

    setModo(novoModo) {
        console.log(`Bobo da Corte (${this.cor}) mudou para o modo: ${novoModo}`);
        this.modoAtual = novoModo;
    }

    setListaModosBobo(listaModosBobo) {
        this.listaModosBobo = listaModosBobo;
    }

    getListaModosBobo() {
        return this.listaModosBobo;
    }

    getModoAtual() {
        return this.modoAtual;
    }

    isMovimentoValido(tabuleiro, linhaInicial, colunaInicial, linhaFinal, colunaFinal) {
        const pecaDestino = tabuleiro.getPeca(linhaFinal, colunaFinal);
        if (pecaDestino !== null && pecaDestino.getCor() === this.getCor()) {
            return false;
        }

        const diffLinha = Math.abs(linhaFinal - linhaInicial);
        const diffColuna = Math.abs(colunaFinal - colunaInicial);

        // A estrutura switch é idêntica em JavaScript
        switch (this.modoAtual) {
            case "Rainha":
                // Reutilizando a lógica estática da Rainha para manter o código limpo
                return Rainha.checarMovimento(tabuleiro, linhaInicial, colunaInicial, linhaFinal, colunaFinal, this.getCor());

            case "Rei":
                return diffLinha <= 1 && diffColuna <= 1;

            case "Cavalo":
                return (diffLinha === 2 && diffColuna === 1) || (diffLinha === 1 && diffColuna === 2);

            case "Ladrao":
                if (diffLinha !== diffColuna) return false;
                if (diffLinha > 0 && diffLinha <= 2) {
                    if (diffLinha === 2) {
                        const passoLinha = (linhaFinal > linhaInicial) ? 1 : -1;
                        const passoColuna = (colunaFinal > colunaInicial) ? 1 : -1;
                        if (tabuleiro.getPeca(linhaInicial + passoLinha, colunaInicial + passoColuna) !== null) return false;
                    }
                    return true;
                }
                return false;
            
            case "Templario":
            case "Principe": // Juntando os dois, já que o movimento do seu Templario era o do Principe/Heroi
                 // REGRA 1: Movimento vertical de até 4 casas
                if (colunaInicial === colunaFinal && (diffLinha > 0 && diffLinha <= 4)) {
                    const passo = (linhaFinal > linhaInicial) ? 1 : -1;
                    for (let i = linhaInicial + passo; i !== linhaFinal; i += passo) {
                        if (tabuleiro.getPeca(i, colunaInicial) !== null) return false;
                    }
                    return true;
                }
                // REGRA 2: Movimento especial (3 vertical, 1 horizontal) SEM PULAR
                if (diffLinha === 3 && diffColuna === 1) {
                    const passoLinha = (linhaFinal > linhaInicial) ? 1 : -1;
                    if (tabuleiro.getPeca(linhaInicial + passoLinha, colunaInicial) !== null) return false;
                    if (tabuleiro.getPeca(linhaInicial + (2 * passoLinha), colunaInicial) !== null) return false;
                    return true;
                }
                return false;

            case "Torre":
                return Torre.checarMovimento(tabuleiro, linhaInicial, colunaInicial, linhaFinal, colunaFinal, this.getCor());

            case "Bispo":
                return Bispo.checarMovimento(tabuleiro, linhaInicial, colunaInicial, linhaFinal, colunaFinal, this.getCor());

            case "Peao":
                const direcao = this.getCor() === "branco" ? -1 : 1;
                if (colunaInicial === colunaFinal && pecaDestino === null) {
                    if (linhaInicial + direcao === linhaFinal) return true;
                }
                if (Math.abs(colunaInicial - colunaFinal) === 1 && (linhaInicial + direcao === linhaFinal)) {
                    if (pecaDestino !== null && pecaDestino.getCor() !== this.getCor()) return true;
                }
                return false;

            default:
                // Retorna falso para o modo "nulo" ou qualquer outro modo desconhecido.
                return false; 
        }
    }

    toString() {
        return `BB{${this.cor}}`;
    }
}