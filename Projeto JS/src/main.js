
import { MenuController } from './usuario/MenuController.js';

document.addEventListener('DOMContentLoaded', () => {
    console.log("HTML carregado. Iniciando o jogo...");
    const gameContainerId = 'game-container';
    
    const controller = new MenuController(gameContainerId);
    
    controller.carregar();

});