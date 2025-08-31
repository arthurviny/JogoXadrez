import { Peca } from './Peca.js';
import { Rei } from './Rei.js';
import { Rainha } from './Rainha.js';
import { Torre } from './Torre.js';
import { Bispo } from './Bispo.js';
import { Cavalo } from './Cavalo.js';
import { Peao } from './Peao.js';
import { Heroi } from './Heroi.js';
import { BoboDaCorte } from './BoboDaCorte.js';
import { Ladrao } from './Ladrao.js';
import { Templario } from './Templario.js';

export class Tabuleiro {
   
    constructor(popularTabuleiro = true) {
     
        this.tabuleiro = Array(8).fill(null).map(() => Array(8).fill(null));
        this.pecasNoTabuleiro = [];

        if (popularTabuleiro) {
            this.iniciarPecas();
        }
    }

    iniciarPecas() {
        const nomePecas = ["Torre", "Cavalo", "BoboDaCorte", "Principe", "Ladrao", "Bispo", "Templario"];
        
        // Embaralha as peças (equivalente a Collections.shuffle)
        for (let i = nomePecas.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [nomePecas[i], nomePecas[j]] = [nomePecas[j], nomePecas[i]];
        }

        const filadeTras = [];
        for (let i = 0; i < 6; i++) {
            filadeTras.push(nomePecas[i]);
        }
        filadeTras.push("Rei");
        filadeTras.push("Rainha");
        
        this.pecasNoTabuleiro = [...filadeTras, "Peao"];

      
        for (let i = filadeTras.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [filadeTras[i], filadeTras[j]] = [filadeTras[j], filadeTras[i]];
        }
        
        // Colocar peças pretas 
        for (let j = 0; j < 8; j++) {
            this.tabuleiro[0][j] = this.criarPeca(filadeTras[j], "preto");
            this.tabuleiro[1][j] = this.criarPeca("Peao", "preto");
        }
        
        // Colocar peças brancas 
        for (let j = 0; j < 8; j++) {
            this.tabuleiro[7][j] = this.criarPeca(filadeTras[j], "branco");
            this.tabuleiro[6][j] = this.criarPeca("Peao", "branco");
        }
    }

    getPecasNoTabuleiro() {
        return this.pecasNoTabuleiro;
    }

    getTabuleiro() {
        return this.tabuleiro;
    }

    getPeca(linha, coluna) {
        return this.tabuleiro[linha][coluna];
    }
    
    setPeca(linha, coluna, peca) {
        this.tabuleiro[linha][coluna] = peca;
    }

    moverPeca(linhaInicial, colunaInicial, linhaFinal, colunaFinal) {
        const peca = this.getPeca(linhaInicial, colunaInicial);
        if (peca === null) {
            console.log("Nenhuma peça encontrada na posição inicial.");
            return;
        }
        this.setPeca(linhaFinal, colunaFinal, peca);
        this.setPeca(linhaInicial, colunaInicial, null);
    }

    check(linha, coluna, corAtacante) {
        for(let i = 0; i < 8; i++) {
            for(let j = 0; j < 8; j++) {
                const peca = this.tabuleiro[i][j];
                if(peca !== null && peca.getCor() === corAtacante) {
                    if (peca instanceof Peao) {
                        const direcao = peca.getCor() === "branco" ? -1 : 1;
                        if (linha === i + direcao && Math.abs(coluna - j) === 1) {
                            return true;
                        }
                    } else {
                        if (peca.isMovimentoValido(this, i, j, linha, coluna)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    encontrarRei(corDoRei) {
        for (let i = 0; i < 8; i++) {
            for (let j = 0; j < 8; j++) {
                const peca = this.getPeca(i, j);
                if (peca !== null && peca.getNomePeca() === "Rei" && peca.getCor() === corDoRei) {
                    return [i, j];
                }
            }
        }
        return null;
    }

    clonar() {
        const clone = new Tabuleiro(false);
        for (let i = 0; i < 8; i++) {
            for (let j = 0; j < 8; j++) {
                clone.setPeca(i, j, this.getPeca(i, j));
            }
        }
        return clone;
    }

    criarPeca(nome, cor) {
        console.log(`Tentando criar a peça: '${nome}' da cor '${cor}'`);
        switch (nome) {
            case "Rainha": return new Rainha(cor);
            case "Rei": return new Rei(cor);
            case "Torre": return new Torre(cor);
            case "Bispo": return new Bispo(cor);
            case "Cavalo": return new Cavalo(cor);
            case "Peao": return new Peao(cor);
            case "Principe": return new Heroi(cor);
            case "BoboDaCorte": return new BoboDaCorte(cor);
            case "Ladrao": return new Ladrao(cor);
            case "Templario": return new Templario(cor);
            default: 
            console.error(`--- ERRO FATAL: PEÇA NÃO ENCONTRADA ---`);
            console.error(`O nome '${nome}' não corresponde a nenhuma peça conhecida.`);
            return null;
        }
    }

    encontrarHeroi(corDoHeroi) {
    for (let i = 0; i < 8; i++) {
        for (let j = 0; j < 8; j++) {
            const peca = this.getPeca(i, j);
        
            if (peca !== null && peca.getNomePeca() === "Principe" && peca.getCor() === corDoHeroi) {
                return [i, j];
            }
        }
    }
    return null; 
}

setFuriaHeroi(linha, coluna, estaEmCheck) {
    const peca = this.getPeca(linha, coluna);
  
    if (peca instanceof Heroi) {
        peca.setReiEstaEmCheck(estaEmCheck);
    }
}

setModoDoBobo(linha, coluna, novoModo) {
    const peca = this.getPeca(linha, coluna);
   
    if (peca instanceof BoboDaCorte) {
        
        peca.setModo(novoModo);
    }
}
}