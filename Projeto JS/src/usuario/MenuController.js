import { GameController } from './GameController.js';

export class MenuController {
    /**
     * @param {string} rootElementId O ID do elemento principal onde o menu e o jogo serão desenhados.
     */
    constructor(rootElementId) {
        this.rootElement = document.getElementById(rootElementId);
        if (!this.rootElement) {
            throw new Error(`Elemento raiz com id '${rootElementId}' não foi encontrado.`);
        }
    }

    // Método que desenha o menu na tela
    carregar() {
        // 1. Limpa qualquer conteúdo anterior
        this.rootElement.innerHTML = '';

        // 2. Adiciona uma classe de estilo ao contêiner principal para o menu
        this.rootElement.classList.add('menu-container');

        // 3. Cria o título do jogo
        const titulo = document.createElement('h1');
        titulo.classList.add('menu-titulo');
        titulo.textContent = 'Xadrez 2';

        // 4. Cria o botão "Jogar"
        const playButton = document.createElement('button');
        playButton.classList.add('menu-botao-jogar');
        playButton.textContent = '▶ JOGAR';

        // 5. Define a AÇÃO do botão: limpar o menu e iniciar o GameController
        playButton.addEventListener('click', () => this.iniciarJogo());

        // 6. Adiciona o título e o botão ao elemento principal
        this.rootElement.appendChild(titulo);
        this.rootElement.appendChild(playButton);
    }

    iniciarJogo() {
        // Remove os estilos e o conteúdo do menu
        this.rootElement.innerHTML = '';
        this.rootElement.classList.remove('menu-container');

        // Cria e inicia o controlador do jogo principal
        // Passamos o mesmo ID do elemento raiz para ele
        const gameController = new GameController(this.rootElement.id);
        gameController.iniciar();
    }
}