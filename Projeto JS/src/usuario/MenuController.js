import { GameController } from './GameController.js';

export class MenuController {
    /**
     * @param {string} rootElementId
     */
    constructor(rootElementId) {
        this.rootElement = document.getElementById(rootElementId);
        if (!this.rootElement) {
            throw new Error(`Elemento raiz com id '${rootElementId}' não foi encontrado.`);
        }
    }

   
    carregar() {
       
        this.rootElement.innerHTML = '';

        
        this.rootElement.classList.add('menu-container');

        
        const titulo = document.createElement('h1');
        titulo.classList.add('menu-titulo');
        titulo.textContent = 'Xadrez 2';

        const playButton = document.createElement('button');
        playButton.classList.add('menu-botao-jogar');
        playButton.textContent = '▶ JOGAR';

        playButton.addEventListener('click', () => this.iniciarJogo());

        this.rootElement.appendChild(titulo);
        this.rootElement.appendChild(playButton);
    }

    iniciarJogo() {
        
        this.rootElement.innerHTML = '';
        this.rootElement.classList.remove('menu-container');


        const gameController = new GameController(this.rootElement.id);
        gameController.iniciar();
    }
}