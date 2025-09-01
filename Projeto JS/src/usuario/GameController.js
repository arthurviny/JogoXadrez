import { Tabuleiro } from '../game/Tabuleiro.js';
import { Peca } from '../game/Peca.js';
import { Peao } from '../game/Peao.js';
import { BoboDaCorte } from '../game/BoboDaCorte.js';
import { Ladrao } from '../game/Ladrao.js';

// Classe principal que controla a interface e o fluxo do jogo
export class GameController {

    constructor(rootElementId) {
        this.rootElement = document.getElementById(rootElementId);
        if (!this.rootElement) {
            throw new Error(`Elemento raiz com id '${rootElementId}' não foi encontrado.`);
        }

        this.jogoDeXadrez = new Tabuleiro();
        this.pecaSelecionada = null;
        this.cellSelecionada = null; 
        this.turnoAtual = "branco";
        this.jogoAcabou = false;

        const todasAsPecasBase = this.jogoDeXadrez.getPecasNoTabuleiro();
        
        // Lista para as opções de promoção do Peão
        this.pecasDePromocao = todasAsPecasBase.filter(p => p !== "Rei" && p !== "Peao");
        
        // Lista base para os modos do Bobo da Corte
        const modosIniciaisDoBobo = todasAsPecasBase.filter(p => p !== "BoboDaCorte");
        
        this.pecasDisponiveisBoboBranco = [...modosIniciaisDoBobo];
        this.pecasDisponiveisBoboPreto = [...modosIniciaisDoBobo];
        this.copiaPecasBobo = [...modosIniciaisDoBobo]; // Cópia de segurança para resetar o ciclo
    }

    
    iniciar() {
        this.desenharTabuleiro()
        this.atualizarTituloDaJanela();
    }

    // interação 

    handleCellClick(event) {
        if (this.jogoAcabou) return;

        const cellElement = event.currentTarget;
        const linha = parseInt(cellElement.dataset.row);
        const coluna = parseInt(cellElement.dataset.col);

        if (event.button === 0) { // Botão Esquerdo
            this.handlePrimaryClick(cellElement, linha, coluna);
        } else if (event.button === 2) { // Botão Direito
            event.preventDefault(); 
            this.handleSecondaryClick(linha, coluna);
        }
    }

   handlePrimaryClick(cell, linha, coluna) {
    if (this.jogoAcabou) return;

    if (this.pecaSelecionada === null) {
        const pecaClicada = this.jogoDeXadrez.getPeca(linha, coluna);
        if (pecaClicada && pecaClicada.getCor() === this.turnoAtual) {
            this.pecaSelecionada = pecaClicada;
            this.cellSelecionada = cell;
            this.desenharTabuleiro();
            this.mostrarMovimentosValidos(linha, coluna);
            const novaCellSelecionada = this.rootElement.querySelector(`[data-row='${linha}'][data-col='${coluna}']`);
            if (novaCellSelecionada) novaCellSelecionada.classList.add('selecionada');
        }
    } 
   
    else {
        const linhaInicial = parseInt(this.cellSelecionada.dataset.row);
        const colunaInicial = parseInt(this.cellSelecionada.dataset.col);
    
        if (this.isMovimentoLegal(linhaInicial, colunaInicial, linha, coluna)) {
            
            const pecaMovida = this.jogoDeXadrez.getPeca(linhaInicial, colunaInicial);
            const pecaNoDestino = this.jogoDeXadrez.getPeca(linha, coluna);
            const foiCaptura = pecaNoDestino !== null;

            this.jogoDeXadrez.moverPeca(linhaInicial, colunaInicial, linha, coluna);
            
         
            if (pecaMovida instanceof BoboDaCorte) {
                    const bobo = pecaMovida;
                const modoUsado = bobo.getModoAtual();
                
                // Se um modo válido foi usado (não "nulo")
                if (modoUsado && modoUsado !== "nulo") {
                    if (bobo.getCor() === "branco") {
                        // Remove o modo da lista de brancas
                        this.pecasDisponiveisBoboBranco = this.pecasDisponiveisBoboBranco.filter(m => m !== modoUsado);
                        // Se a lista ficou vazia, reseta
                        if (this.pecasDisponiveisBoboBranco.length === 0) {
                            console.log("CICLO DO BOBO BRANCO COMPLETO! Resetando modos.");
                            this.pecasDisponiveisBoboBranco = [...this.copiaPecasBobo];
                        }
                    } else { // Se for o bobo preto Remove o modo da lista de pretas
                        this.pecasDisponiveisBoboPreto = this.pecasDisponiveisBoboPreto.filter(m => m !== modoUsado);
                        // Se a lista ficou vazia, reseta
                        if (this.pecasDisponiveisBoboPreto.length === 0) {
                            console.log("CICLO DO BOBO PRETO COMPLETO! Resetando modos.");
                            this.pecasDisponiveisBoboPreto = [...this.copiaPecasBobo];
                        }
                    }
                }
                
                
                bobo.setModo("nulo");              
            }

            
            const ehPeaoPromovendo = pecaMovida instanceof Peao && (linha === 0 || linha === 7);
            const ehLadraoComCaptura = pecaMovida instanceof Ladrao && foiCaptura;

            if (ehPeaoPromovendo) {
                this.modularPromocao(pecaMovida, linha, coluna);
            } else if (ehLadraoComCaptura) {
                this.mostrarDialogoDoLadrao(linhaInicial, colunaInicial, linha, coluna);
            } else {
               
                this.trocarTurno();
            }

        } else {
            
            alert("Movimento Ilegal!");
        }
        
        
        this.deselecionarPeca();
        this.desenharTabuleiro();
    }
}
    handleSecondaryClick(linha, coluna) {
        const pecaClicada = this.jogoDeXadrez.getPeca(linha, coluna);
        if (pecaClicada instanceof BoboDaCorte && pecaClicada.getCor() === this.turnoAtual) {
            this.modularBobo(pecaClicada, linha, coluna);
        }
    }
    
    // ver se pode andar e final do jogo

    isMovimentoLegal(linhaInicial, colunaInicial, linhaFinal, colunaFinal) {
    const peca = this.pecaSelecionada;
    
    const movimentoGeometricoValido = peca.isMovimentoValido(this.jogoDeXadrez, linhaInicial, colunaInicial, linhaFinal, colunaFinal);
    
    if (!movimentoGeometricoValido) {
        return false; 
    }
    
    console.log(`%cChecando Legalidade: ${peca.getNomePeca()} de (${linhaInicial},${colunaInicial}) para (${linhaFinal},${colunaFinal})`, 'color: cyan;');

    // Este movimento deixa o meu Rei em cheque?
    const tabuleiroSimulado = this.jogoDeXadrez.clonar();
    tabuleiroSimulado.moverPeca(linhaInicial, colunaInicial, linhaFinal, colunaFinal);
    
    const posRei = tabuleiroSimulado.encontrarRei(this.turnoAtual);
    if (posRei === null) {
        console.warn("AVISO: Rei não encontrado na simulação. Permitindo movimento por segurança.");
        return true; 
    }

    const corDoOponente = this.turnoAtual === "branco" ? "preto" : "branco";
    const reiFicaEmCheque = tabuleiroSimulado.check(posRei[0], posRei[1], corDoOponente);

    if (reiFicaEmCheque) {
        return false;
    } else {
        
        return true;
    }
}


    temMovimentoLegal(corDoJogador) {
        for (let i = 0; i < 8; i++) {
            for (let j = 0; j < 8; j++) {
                const peca = this.jogoDeXadrez.getPeca(i, j);
                if (peca && peca.getCor() === corDoJogador) {
                    this.pecaSelecionada = peca;
                    if (peca instanceof BoboDaCorte) {
                        const bobo = peca;
                        const modoOriginal = bobo.getModoAtual();
                        const modosDisponiveis = bobo.getCor() === "branco" ? this.pecasDisponiveisBoboBranco : this.pecasDisponiveisBoboPreto;
                        for (const modoDeTeste of modosDisponiveis) {
                            bobo.setModo(modoDeTeste);
                            for (let x = 0; x < 8; x++) {
                                for (let y = 0; y < 8; y++) {
                                    if (this.isMovimentoLegal(i, j, x, y)) {
                                        bobo.setModo(modoOriginal);
                                        this.pecaSelecionada = null;
                                        return true;
                                    }
                                }
                            }
                        }
                        bobo.setModo(modoOriginal);
                    } else {
                        for (let x = 0; x < 8; x++) {
                            for (let y = 0; y < 8; y++) {
                                if (this.isMovimentoLegal(i, j, x, y)) {
                                    this.pecaSelecionada = null;
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        this.pecaSelecionada = null;
        return false;
    }

    verificarFimDeJogo() {
        if (!this.temMovimentoLegal(this.turnoAtual)) {
            this.jogoAcabou = true;
            const posRei = this.jogoDeXadrez.encontrarRei(this.turnoAtual);
            const corDoOponente = this.turnoAtual === "branco" ? "preto" : "branco";

            setTimeout(() => { // Adiciona um pequeno delay para o jogador ver o último movimento
                if (this.jogoDeXadrez.check(posRei[0], posRei[1], corDoOponente)) {
                    alert(`XEQUE-MATE! As peças ${corDoOponente}s venceram!`);
                } else {
                    alert("EMPATE! O jogo terminou por Rei Afogado (Stalemate).");
                }
            }, 100);
        }
    }

    trocarTurno() {
        this.turnoAtual = this.turnoAtual === "branco" ? "preto" : "branco";
        this.atualizarTituloDaJanela();
        
        // Ativa/Desativa Fúria do Herói
        const posRei = this.jogoDeXadrez.encontrarRei(this.turnoAtual);
        if (posRei) {
            const corDoOponente = this.turnoAtual === "branco" ? "preto" : "branco";
            const reiEmCheck = this.jogoDeXadrez.check(posRei[0], posRei[1], corDoOponente);
            const posHeroi = this.jogoDeXadrez.encontrarHeroi(this.turnoAtual);
            if (posHeroi) {
                this.jogoDeXadrez.setFuriaHeroi(posHeroi[0], posHeroi[1], reiEmCheck);
            }
        }
        this.desenharTabuleiro()
        this.verificarFimDeJogo();
    }

    modularPromocao(peao, linha, coluna) {
        
        const popupElement = document.getElementById('popup-promocao');
        const popupContent = popupElement.querySelector('.popup-content');
        if (!popupElement || !popupContent) return;
        
        popupContent.innerHTML = ''; // Limpa opções anteriores

        this.pecasDePromocao.forEach(pecaPromover => {
            const img = document.createElement('img');
            img.src = `./assets/images/${peao.getCor()}/${pecaPromover}.png`;
            img.dataset.peca = pecaPromover;
            img.addEventListener('click', () => {
                const pecaEscolhida = this.jogoDeXadrez.criarPeca(pecaPromover, peao.getCor());
                this.jogoDeXadrez.setPeca(linha, coluna, pecaEscolhida);
                popupElement.classList.add('hidden');
                this.desenharTabuleiro();
                this.trocarTurno(); 
            });
            popupContent.appendChild(img);
        });

        popupElement.classList.remove('hidden'); 
    }

    modularBobo(bobo, linha, coluna) {
        
        const popupElement = document.getElementById('popup-bobo');
        const popupContent = popupElement.querySelector('.popup-content');
        if (!popupElement || !popupContent) return;

        popupContent.innerHTML = '';
        const modosDisponiveis = bobo.getCor() === 'branco' ? this.pecasDisponiveisBoboBranco : this.pecasDisponiveisBoboPreto;

        modosDisponiveis.forEach(modo => {
            const img = document.createElement('img');
            img.src = `./assets/images/${bobo.getCor()}/${modo}.png`;
            img.dataset.modo = modo;
            img.addEventListener('click', () => {
                this.jogoDeXadrez.setModoDoBobo(linha, coluna, modo);
                popupElement.classList.add('hidden');
                this.desenharTabuleiro(); 
                this.mostrarMovimentosValidos(linha, coluna); 
        
                const novaCellSelecionada = this.rootElement.querySelector(`[data-row='${linha}'][data-col='${coluna}']`);
                if (novaCellSelecionada) novaCellSelecionada.classList.add('selecionada');
            });
            popupContent.appendChild(img);
        });
        
        popupElement.classList.remove('hidden');
    }

    mostrarDialogoDoLadrao(linhaInicial, colunaInicial, linha, coluna) {
    const dialogoElement = document.getElementById('dialogo-ladrao');
    const botaoSim = document.getElementById('botao-recuar-sim');
    const botaoNao = document.getElementById('botao-recuar-nao');

    if (!dialogoElement || !botaoSim || !botaoNao) return;

    const fecharDialogo = () => {
        dialogoElement.classList.add('hidden');
        botaoSim.replaceWith(botaoSim.cloneNode(true));
        botaoNao.replaceWith(botaoNao.cloneNode(true));
    };

    // Mostra o diálogo
    dialogoElement.classList.remove('hidden');

    // Sim
    botaoSim.addEventListener('click', () => {
        this.jogoDeXadrez.moverPeca(linha, coluna, linhaInicial, colunaInicial);
        fecharDialogo();
        this.trocarTurno(); // Troca o turno após a ação
        this.desenharTabuleiro(); // Redesenha para mostrar o recuo
    }, { once: true }); // { once: true } garante que o evento só dispare uma vez

    // Nao
    botaoNao.addEventListener('click', () => {
        fecharDialogo();
        this.trocarTurno(); // Troca o turno após a ação
    }, { once: true });
}
    

   desenharTabuleiro() {
    this.rootElement.innerHTML = '';
    this.rootElement.style.display = 'grid';
    this.rootElement.style.gridTemplateColumns = 'repeat(8, 1fr)';

    const tabuleiroArray = this.jogoDeXadrez.getTabuleiro();
    for (let row = 0; row < 8; row++) {
        for (let col = 0; col < 8; col++) {
            const cellElement = document.createElement('div');
            cellElement.classList.add('cell', (row + col) % 2 === 0 ? 'light' : 'dark');
            cellElement.dataset.row = row;
            cellElement.dataset.col = col;

            const peca = tabuleiroArray[row][col];

            if (peca) {
               
                try {
                    const pecaImage = document.createElement('img');
                    
                    pecaImage.src = `./assets/images/${peca.getCor()}/${peca.getNomePeca()}.png`;
                    cellElement.appendChild(pecaImage);
                } catch (error) {
                    
                    console.error(`--- ERRO AO DESENHAR PEÇA ---`);
                    console.error(`Posição do Erro: linha=${row}, coluna=${col}`);
                    console.error(`Conteúdo da célula que causou o erro:`, peca);
                    console.error(`Mensagem de erro original:`, error);
                    
                    cellElement.innerHTML = '<span style="color: red; font-size: 40px; font-weight: bold;">X</span>';
                }
            }
            
            cellElement.addEventListener('mousedown', (event) => this.handleCellClick(event));
            this.rootElement.appendChild(cellElement);
        }
    }
}

    mostrarMovimentosValidos(linhaOrigem, colunaOrigem) {
    console.log(`Buscando movimentos para a peça em (${linhaOrigem}, ${colunaOrigem})`);
    if (!this.pecaSelecionada) {
        console.error("erro : Tentou mostrar movimentos, mas nenhuma peça está selecionada.");
        return;
    }

    document.querySelectorAll('.move-indicator').forEach(el => el.remove());

    const cells = Array.from(this.rootElement.children);
    let movimentosEncontrados = 0; // Um contador para sabermos se algo foi encontrado

    for (let i = 0; i < 8; i++) {
        for (let j = 0; j < 8; j++) {
            // Chama a nossa função de validação completa
            if (this.isMovimentoLegal(linhaOrigem, colunaOrigem, i, j)) {
                movimentosEncontrados++; // Achou
                const index = i * 8 + j;
                if (cells[index]) {
                    const indicador = document.createElement('div');
                    indicador.classList.add('move-indicator');
                    cells[index].appendChild(indicador);
                }
            }
        }
    }
    
}

    atualizarTituloDaJanela() {
        const corCapitalizada = this.turnoAtual === "branco" ? "Brancas" : "Pretas"
        document.title = `XXXadrez! - Vez das ${corCapitalizada}`;
    }

    deselecionarPeca() {
    if (this.cellSelecionada) {
        this.cellSelecionada.classList.remove('selecionada');
    }
    document.querySelectorAll('.move-indicator').forEach(el => el.remove());

    this.pecaSelecionada = null;
    this.cellSelecionada = null;
}

}