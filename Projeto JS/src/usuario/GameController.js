import { Tabuleiro } from '../game/Tabuleiro.js';
import { Peca } from '../game/Peca.js';
import { Peao } from '../game/Peao.js';
import { BoboDaCorte } from '../game/BoboDaCorte.js';
import { Ladrao } from '../game/Ladrao.js';

// Classe principal que controla a interface e o fluxo do jogo
export class GameController {

    constructor(rootElementId) {
        console.log("GameController.constructor() foi chamado.");
        this.rootElement = document.getElementById(rootElementId);
        if (!this.rootElement) {
            throw new Error(`Elemento raiz com id '${rootElementId}' não foi encontrado.`);
        }

        this.jogoDeXadrez = new Tabuleiro();
        this.pecaSelecionada = null;
        this.cellSelecionada = null; 
        this.turnoAtual = "branco";
        this.jogoAcabou = false;

        // --- LÓGICA DAS LISTAS PARA PEÇAS ESPECIAIS (TRADUZIDA DO JAVA) ---
        const todasAsPecasBase = this.jogoDeXadrez.getPecasNoTabuleiro();
        
        // Lista para as opções de promoção do Peão
        this.pecasDePromocao = todasAsPecasBase.filter(p => p !== "Rei" && p !== "Peao");
        
        // Lista base para os modos do Bobo da Corte
        const modosIniciaisDoBobo = todasAsPecasBase.filter(p => p !== "BoboDaCorte");
        
        // Cria uma cópia para cada cor
        this.pecasDisponiveisBoboBranco = [...modosIniciaisDoBobo];
        this.pecasDisponiveisBoboPreto = [...modosIniciaisDoBobo];
        this.copiaPecasBobo = [...modosIniciaisDoBobo]; // Cópia de segurança para resetar o ciclo
    }

    // Inicia o jogo, desenhando o tabuleiro e configurando o título
    iniciar() {
        console.log("GameController.iniciar() foi chamado. Prestes a desenhar o tabuleiro.");
        this.desenharTabuleiro();
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
            event.preventDefault(); // Impede o menu de contexto do navegador
            this.handleSecondaryClick(linha, coluna);
        }
    }

    handlePrimaryClick(cell, linha, coluna) {
    
    if (this.pecaSelecionada === null) {
        const pecaClicada = this.jogoDeXadrez.getPeca(linha, coluna);
        if (pecaClicada && pecaClicada.getCor() === this.turnoAtual) {
            this.pecaSelecionada = pecaClicada;
            this.cellSelecionada = cell;

            // NÃO redesenhamos mais o tabuleiro aqui.
            // Apenas adicionamos os destaques visuais.
            cell.classList.add('selecionada');
            this.mostrarMovimentosValidos(linha, coluna);
        }
    } 
   
    else {
        const linhaInicial = parseInt(this.cellSelecionada.dataset.row);
        const colunaInicial = parseInt(this.cellSelecionada.dataset.col);

       
        if (linhaInicial === linha && colunaInicial === coluna) {
            this.deselecionarPeca();
            this.desenharTabuleiro(); 
            return;
        }

        if (this.isMovimentoLegal(linhaInicial, colunaInicial, linha, coluna)) {
            const pecaMovida = this.jogoDeXadrez.getPeca(linhaInicial, colunaInicial);
            const pecaNoDestino = this.jogoDeXadrez.getPeca(linha, coluna);
            const foiCaptura = pecaNoDestino !== null;

            this.jogoDeXadrez.moverPeca(linhaInicial, colunaInicial, linha, coluna);
            
            // Lógica de troca de turno (agora usando if/else if/else)
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
        if (!this.pecaSelecionada.isMovimentoValido(this.jogoDeXadrez, linhaInicial, colunaInicial, linhaFinal, colunaFinal)) {
            return false;
        }
        const tabuleiroSimulado = this.jogoDeXadrez.clonar();
        tabuleiroSimulado.moverPeca(linhaInicial, colunaInicial, linhaFinal, colunaFinal);
        const posRei = tabuleiroSimulado.encontrarRei(this.turnoAtual);
        if (posRei === null) return true;
        const corDoOponente = this.turnoAtual === "branco" ? "preto" : "branco";
        return !tabuleiroSimulado.check(posRei[0], posRei[1], corDoOponente);
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

        this.verificarFimDeJogo();
    }
    
    // --- MÉTODOS DE UI (DESENHO E POPUPS) ---

    modularPromocao(peao, linha, coluna) {
        // No HTML, você precisaria ter um <div id="popup-promocao" class="popup hidden"></div>
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
                popupElement.classList.add('hidden'); // Esconde o popup
                this.desenharTabuleiro();
                this.trocarTurno(); // Troca o turno após a promoção
            });
            popupContent.appendChild(img);
        });

        popupElement.classList.remove('hidden'); // Mostra o popup
    }

    modularBobo(bobo, linha, coluna) {
        // Similar à promoção, usaria um <div id="popup-bobo" class="popup hidden"></div>
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
                this.desenharTabuleiro(); // Redesenha para limpar o destaque
                this.mostrarMovimentosValidos(linha, coluna); // Mostra os novos movimentos
        
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

    // Função para fechar e limpar os eventos
    const fecharDialogo = () => {
        dialogoElement.classList.add('hidden');
        // Remove os listeners antigos para evitar múltiplos cliques no futuro
        botaoSim.replaceWith(botaoSim.cloneNode(true));
        botaoNao.replaceWith(botaoNao.cloneNode(true));
    };

    // Mostra o diálogo
    dialogoElement.classList.remove('hidden');

    // Ação do botão SIM
    botaoSim.addEventListener('click', () => {
        this.jogoDeXadrez.moverPeca(linha, coluna, linhaInicial, colunaInicial);
        fecharDialogo();
        this.trocarTurno(); // Troca o turno após a ação
        this.desenharTabuleiro(); // Redesenha para mostrar o recuo
    }, { once: true }); // { once: true } garante que o evento só dispare uma vez

    // Ação do botão NÃO
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

            // Se existe algo na casa, tentamos desenhar
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
    // Garante que uma peça está selecionada antes de tentar mostrar os movimentos.
    if (!this.pecaSelecionada) return;

    const cells = Array.from(this.rootElement.children);
    for (let i = 0; i < 8; i++) {
        for (let j = 0; j < 8; j++) {
            // AQUI ESTÁ A MUDANÇA: Usamos isMovimentoLegal, que já usa this.pecaSelecionada
            if (this.isMovimentoLegal(linhaOrigem, colunaOrigem, i, j)) {
                const index = i * 8 + j;
                if(cells[index]) {
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

}