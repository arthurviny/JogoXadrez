console.log("Arquivo main.js foi carregado e executado.")
import { MenuController } from './usuario/MenuController.js';

document.addEventListener('DOMContentLoaded', () => {
    console.log("HTML carregado. Iniciando o jogo...");
    // O ID do elemento principal que definimos no index.html
    const gameContainerId = 'game-container';
    
    // Cria uma nova instância do controlador do MENU
    const controller = new MenuController(gameContainerId);
    
    // Manda o MENU carregar!
    controller.carregar();

});