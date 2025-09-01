package org.example.vamo_ver;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import game.BoboDaCorte;
import game.Ladrao;
import game.Peao;
import game.Peca;
import game.Rei;
import game.Tabuleiro;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class HelloController implements Initializable {

    @FXML
    private GridPane tabuleiroGrid;
    private Tabuleiro jogoDeXadrez;
    private Peca pecaSelecionada = null;
    private StackPane cellSelecionada = null;
    private String turnoAtual = "branco";
    private boolean jogoAcabou = false;
    private boolean boboConsegueSalvar = false;
    List<String> pecasTransformarPretas = new ArrayList<>();
    List<String> pecasTransformarBrancas = new ArrayList<>();

    // Uso de conjuntos para evitar duplicatas
    Set<String> pecasBoboImpedirCheckPretas = new HashSet<>();
    Set<String> pecasBoboImpedirCheckBrancas = new HashSet<>();

    List<String> todasPecasMenosReiPeao = new ArrayList<>();
    List<String> copiaPecasBobo = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        jogoDeXadrez = new Tabuleiro();

        pecasTransformarBrancas = jogoDeXadrez.getPecasNoTabuleiro();
        pecasTransformarPretas.addAll(pecasTransformarBrancas);
        pecasTransformarPretas.remove("BoboDaCorte");
        pecasTransformarBrancas.remove("BoboDaCorte");
        copiaPecasBobo.addAll(pecasTransformarBrancas);
        todasPecasMenosReiPeao = new ArrayList<>(pecasTransformarBrancas);
        todasPecasMenosReiPeao.remove("Peao");
        todasPecasMenosReiPeao.remove("Rei");

        desenharTabuleiro();

        // Listener para atualizar o título da janela
        tabuleiroGrid.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obs2, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        atualizarTituloDaJanela();
                    }
                });
            }
        });
    }

    private void modularPromocao(Peca peao, int linha, int coluna) {
        final Popup popup = new Popup();
        GridPane menuDePecas = new GridPane();
        menuDePecas.setStyle("-fx-background-color: #333333; -fx-padding: 5; -fx-border-color: #666666; -fx-border-width: 2;");
        menuDePecas.setHgap(5);
        menuDePecas.setVgap(5);

        for (int i = 0; i < 4; i++) {
            menuDePecas.getColumnConstraints().add(new javafx.scene.layout.ColumnConstraints(30, 30, 30, Priority.SOMETIMES, HPos.CENTER, true));
        }

        for (int i = 0; i < 2; i++) {
            menuDePecas.getRowConstraints().add(new javafx.scene.layout.RowConstraints(30, 30, 30, Priority.SOMETIMES, VPos.CENTER, true));
        }

        int contador = 0;
        if (peao.getCor().equals("branco")) {
            for (String pecaPromover : pecasTransformarBrancas) {
                Image image = new Image(
                        getClass().getResourceAsStream(
                                "/images/" + peao.getCor() + "/" + pecaPromover + ".png"
                        )
                );

                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(30);
                imageView.setFitHeight(30);
                imageView.setOnMouseClicked(event -> {

                    Peca pecaEscolhidaPromocao = jogoDeXadrez.criarPeca(pecaPromover, peao.getCor());
                    jogoDeXadrez.setPeca(linha, coluna, pecaEscolhidaPromocao);
                    atualizarTabuleiro();
                    System.out.println("Peao promovido a: " + pecaPromover);

                    popup.hide();

                });

                int menuCol = contador % 4; // Colunas: 0, 1, 2, 3
                int menuRow = contador / 4; // Linhas: 0, 0, 0, 0, 1, 1, 1, 1

                menuDePecas.add(imageView, menuCol, menuRow);
                contador++;
            }

            popup.getContent().add(menuDePecas);
            Node node = getNodeByRowColumnIndex(linha, coluna, tabuleiroGrid);
            if (node != null) {

                double screenX = node.localToScreen(0, 0).getX();
                double screenY = node.localToScreen(0, 0).getY();

                popup.show(tabuleiroGrid, screenX + 50, screenY);
            }
        } else {
            for (String pecaPromover : todasPecasMenosReiPeao) {
                Image image = new Image(
                        getClass().getResourceAsStream(
                                "/images/" + peao.getCor() + "/" + pecaPromover + ".png"
                        )
                );

                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(30);
                imageView.setFitHeight(30);
                imageView.setOnMouseClicked(event -> {

                    // LOGICA DE PROMOCAO AQUI
                    Peca pecaEscolhidaPromocao = jogoDeXadrez.criarPeca(pecaPromover, peao.getCor());
                    jogoDeXadrez.setPeca(linha, coluna, pecaEscolhidaPromocao);
                    atualizarTabuleiro();
                    System.out.println("Peao promovido a: " + pecaPromover);
                    popup.hide();

                });

                int menuCol = contador % 4; // Colunas: 0, 1, 2, 3
                int menuRow = contador / 4; // Linhas: 0, 0, 0, 0, 1, 1, 1, 1

                menuDePecas.add(imageView, menuCol, menuRow);
                contador++;
            }

            popup.getContent().add(menuDePecas);
            Node node = getNodeByRowColumnIndex(linha, coluna, tabuleiroGrid);
            if (node != null) {

                double screenX = node.localToScreen(0, 0).getX();
                double screenY = node.localToScreen(0, 0).getY();

                popup.show(tabuleiroGrid, screenX + 50, screenY);
            }
        }
    }

    private void modularBobo(Peca bobo, int linha, int coluna) {
        final Popup popup = new Popup();
        GridPane menuDeModos = new GridPane();
        menuDeModos.setStyle("-fx-background-color: #333333; -fx-padding: 5; -fx-border-color: #666666; -fx-border-width: 2;");
        menuDeModos.setHgap(5);
        menuDeModos.setVgap(5);

        for (int i = 0; i < 4; i++) {
            menuDeModos.getColumnConstraints().add(new javafx.scene.layout.ColumnConstraints(30, 30, 30, Priority.SOMETIMES, HPos.CENTER, true));
        }

        for (int i = 0; i < 2; i++) {
            menuDeModos.getRowConstraints().add(new javafx.scene.layout.RowConstraints(30, 30, 30, Priority.SOMETIMES, VPos.CENTER, true));
        }

        int contador = 0;
        if (bobo.getCor().equals("branco")) {
            for (String modo : boboConsegueSalvar ? pecasBoboImpedirCheckBrancas : pecasTransformarBrancas) {
                Image image = new Image(
                        getClass().getResourceAsStream(
                                "/images/" + bobo.getCor() + "/" + modo + ".png"
                        )
                );

                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(30);
                imageView.setFitHeight(30);
                imageView.setOnMouseClicked(event -> {

                    // Lógica para mudar o modo do bobo
                    jogoDeXadrez.setModoDoBobo(linha, coluna, modo);
                    // Mostrar os movimentos válidos logo após escolher o modo do bobo
                    limparMovimentosValidos();
                    mostrarMovimentosValidos(linha, coluna);
                    System.out.println("Modo do Bobo da Corte alterado para: " + modo);

                    popup.hide();

                });

                int menuCol = contador % 4; // Colunas: 0, 1, 2, 3
                int menuRow = contador / 4; // Linhas: 0, 0, 0, 0, 1, 1, 1, 1

                menuDeModos.add(imageView, menuCol, menuRow);
                contador++;
            }

            popup.getContent().add(menuDeModos);
            Node node = getNodeByRowColumnIndex(linha, coluna, tabuleiroGrid);
            if (node != null) {

                double screenX = node.localToScreen(0, 0).getX();
                double screenY = node.localToScreen(0, 0).getY();

                popup.show(tabuleiroGrid, screenX + 50, screenY);
            }
        } else {
            for (String modo : boboConsegueSalvar ? pecasBoboImpedirCheckPretas : pecasTransformarPretas) {
                Image image = new Image(
                        getClass().getResourceAsStream(
                                "/images/" + bobo.getCor() + "/" + modo + ".png"
                        )
                );

                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(30);
                imageView.setFitHeight(30);
                imageView.setOnMouseClicked(event -> {

                    // Lógica para mudar o modo do Bobo da Corte
                    jogoDeXadrez.setModoDoBobo(linha, coluna, modo);
                    // Antes de mostrar os movimentos válidos da nova peça, limpa os movimentos válidos antigos
                    limparMovimentosValidos();
                    mostrarMovimentosValidos(linha, coluna);
                    System.out.println("Modo do Bobo da Corte alterado para: " + modo);

                    popup.hide();

                });

                int menuCol = contador % 4; // Colunas: 0, 1, 2, 3
                int menuRow = contador / 4; // Linhas: 0, 0, 0, 0, 1, 1, 1, 1

                menuDeModos.add(imageView, menuCol, menuRow);
                contador++;
            }

            popup.getContent().add(menuDeModos);
            Node node = getNodeByRowColumnIndex(linha, coluna, tabuleiroGrid);
            if (node != null) {

                double screenX = node.localToScreen(0, 0).getX();
                double screenY = node.localToScreen(0, 0).getY();

                popup.show(tabuleiroGrid, screenX + 50, screenY);
            }
        }
    }


    public void handlePrimaryClick(StackPane cell, int linha, int coluna) {
        if (jogoAcabou) return; // Se o jogo acabou, não faz nada

        // Lógica de seleção de peça
        if (pecaSelecionada == null) {
            Peca pecaClicada = jogoDeXadrez.getPeca(linha, coluna);
            if (pecaClicada != null && pecaClicada.getCor().equals(turnoAtual)) {
                pecaSelecionada = pecaClicada;
                cellSelecionada = cell;
                cell.setStyle(cell.getStyle() + "; -fx-border-color: yellow; -fx-border-width: 2;");
                mostrarMovimentosValidos(linha, coluna);
            }
        }
        // Lógica de tentativa de movimento
        else {
            int linhaInicial = GridPane.getRowIndex(cellSelecionada);
            int colunaInicial = GridPane.getColumnIndex(cellSelecionada);

            // A validação completa agora está centralizada no isMovimentoLegal
            if (isMovimentoLegal(linhaInicial, colunaInicial, linha, coluna)) {

                String corOponente = turnoAtual == "branco" ? "preto" : "branco";
                Peca pecaNoDestino = jogoDeXadrez.getPeca(linha, coluna);
                boolean foiCaptura = pecaNoDestino != null;

                jogoDeXadrez.moverPeca(linhaInicial, colunaInicial, linha, coluna);

                Peca peca = jogoDeXadrez.getPeca(linha, coluna);

                if (peca instanceof Peao) {
                    if (peca.getCor().equals("branco") && linha == 0) {
                        modularPromocao(peca, linha, coluna);
                    } else if (peca.getCor().equals("preto") && linha == 7) {
                        modularPromocao(peca, linha, coluna);
                    }
                }

                if (peca instanceof BoboDaCorte) {
                    BoboDaCorte bobo = (BoboDaCorte) peca;
                    String modoAtualBobo = bobo.getModoAtual();
                    if (peca.getCor().equals("branco")) {
                        pecasTransformarBrancas.remove(modoAtualBobo);
                        jogoDeXadrez.setListaModosBobo(linha, coluna, pecasTransformarBrancas);
                        jogoDeXadrez.setModoDoBobo(linha, coluna, "nulo");
                    } else {
                        pecasTransformarPretas.remove(modoAtualBobo);
                        jogoDeXadrez.setListaModosBobo(linha, coluna, pecasTransformarPretas);
                        jogoDeXadrez.setModoDoBobo(linha, coluna, "nulo");
                    }

                    if (pecasTransformarBrancas.isEmpty()) {
                        pecasTransformarBrancas.addAll(copiaPecasBobo);
                    } else if (pecasTransformarPretas.isEmpty()){
                        pecasTransformarPretas.addAll(copiaPecasBobo);
                    }
                }

                if (pecaSelecionada instanceof Ladrao && foiCaptura) {
                    mostrarDialogoDoLadrao(linhaInicial, colunaInicial, linha, coluna);
                    trocarTurno();
                } else {
                    trocarTurno();
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Movimento ILEGAL!");
            }

            // Limpa a seleção e redesenha o tabuleiro para qualquer tentativa de movimento
            atualizarTabuleiro();
            deselecionarPeca();
        }
    }

    public void handleSecondaryClick(int linha, int coluna) {
        if (pecaSelecionada instanceof BoboDaCorte) {
            modularBobo(pecaSelecionada, linha, coluna);
        }
    }

    // Parte Logica do Xadrez
    private boolean isMovimentoLegal(int linhaInicial, int colunaInicial, int linhaFinal, int colunaFinal) {
        if (pecaSelecionada == null) {
            return false;
        }

        // Olha se a peça pode fazer o movimento
        if (!pecaSelecionada.isMovimentoValido(jogoDeXadrez, linhaInicial, colunaInicial, linhaFinal, colunaFinal)) {
            return false;
        }

        // Logica de Simulaçao
        Tabuleiro tabuleiroSimulado = jogoDeXadrez.clonar();
        tabuleiroSimulado.moverPeca(linhaInicial, colunaInicial, linhaFinal, colunaFinal);

        int[] posRei = tabuleiroSimulado.encontrarRei(turnoAtual);
        if (posRei == null) return true; // Segurança

        String corDoOponente = turnoAtual.equals("branco") ? "preto" : "branco";

        if (pecaSelecionada instanceof Rei) {
            return !tabuleiroSimulado.check(posRei[0], posRei[1], corDoOponente, true);
        }
        // Passo 3: O movimento só é legal se o Rei NÃO estiver sob ataque na simulação.
        return !tabuleiroSimulado.check(posRei[0], posRei[1], corDoOponente, false);
    }

    private boolean temMovimentoLegal(String corDoJogador) {
        // Percorremos todas as peças do tabuleiro
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Peca peca = jogoDeXadrez.getPeca(i, j);
                // Só importa pra gente a peça da cor do jogador que estamos verificando
                if (peca != null && peca.getCor().equals(corDoJogador)) {
                    // Precisamos de uma verificação especial pro bobo da corte
                    // Percorrendo todas as peças do bobo pra ver se alguma pode impedir o check
                    this.pecaSelecionada = peca; // Seleciona temporariamente
                    if (pecaSelecionada instanceof BoboDaCorte) {
                        BoboDaCorte bobo = (BoboDaCorte) pecaSelecionada;
                        String modoOriginalBobo = bobo.getModoAtual();
                        if (corDoJogador.equals("branco")) {
                            for (String pecaTexto: pecasTransformarBrancas) {
                                jogoDeXadrez.setModoDoBobo(i, j, pecaTexto);
                                for (int x = 0; x < 8; x++) {
                                    for (int y = 0; y < 8; y++) {
                                        // Verificamos, pra cada peça, se ainda existe um movimento
                                        // legal, se não existir mais movimentos legais, o jogo acaba
                                        if (isMovimentoLegal(i, j, x, y)) {
                                            pecasBoboImpedirCheckBrancas.add(pecaTexto);
                                            boboConsegueSalvar = true;
                                        }
                                    }
                                    for(String pecaT : pecasBoboImpedirCheckBrancas) {
                                        System.out.println(pecaT);
                                    }
                                }
                            }
                        } else {
                            for (String pecaTexto: pecasTransformarPretas) {
                                jogoDeXadrez.setModoDoBobo(i, j, pecaTexto);
                                for (int x = 0; x < 8; x++) {
                                    for (int y = 0; y < 8; y++) {
                                        // Verificamos, pra cada peça, se ainda existe um movimento
                                        // legal, se não existir mais movimentos legais, o jogo acaba
                                        if (isMovimentoLegal(i, j, x, y)) {
                                            pecasBoboImpedirCheckPretas.add(pecaTexto);
                                            boboConsegueSalvar = true;
                                        }
                                    }
                                }
                            }
                        }
                        bobo.setModo(modoOriginalBobo); // Voltamos o modo do bobo pro que ele era antes, já que
                        this.pecaSelecionada = null;    // trocamos ele apenas pra verificar se alguma variação podia
                                                        // podia impedir o check
                    } else {
                        for (int x = 0; x < 8; x++) {
                            for (int y = 0; y < 8; y++) {
                                // Verificamos, pra cada peça, se ainda existe um movimento
                                // legal, se não existir mais movimentos legais, o jogo acaba
                                if (isMovimentoLegal(i, j, x, y)) {
                                    this.pecaSelecionada = null; // Limpa a seleção temporária
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (boboConsegueSalvar) return true;
        this.pecaSelecionada = null; // Limpa a seleção temporária
        return false;
    }

    private void verificarFimDeJogo() {
        String corDoOponente = turnoAtual.equals("branco") ? "preto" : "branco";
        if (jogoDeXadrez.encontrarRei(turnoAtual) == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fim de Jogo!");
            alert.setHeaderText("REI CAPTURADO!");
            String vencedor = corDoOponente.substring(0, 1).toUpperCase() + corDoOponente.substring(1);
            alert.setContentText(vencedor + "s venceram!");
            alert.showAndWait();
        }

        if (!temMovimentoLegal(turnoAtual)) {
            this.jogoAcabou = true; // Trava o jogo
            int[] posRei = jogoDeXadrez.encontrarRei(turnoAtual);

            if (jogoDeXadrez.check(posRei[0], posRei[1], corDoOponente, false)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Fim de Jogo!");
                alert.setHeaderText("XEQUE-MATE!");
                String vencedor = corDoOponente.substring(0, 1).toUpperCase() + corDoOponente.substring(1);
                alert.setContentText(vencedor + "s venceram!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Fim de Jogo!");
                alert.setHeaderText("EMPATE!");
                alert.setContentText("O jogo terminou em empate por Rei Afogado (Stalemate).");
                alert.showAndWait();
            }
        }
    }

    private void verificarCheck() {
        int[] posRei = jogoDeXadrez.encontrarRei(turnoAtual);
        if (posRei == null) return;

        String corOponente = turnoAtual.equals("branco") ? "preto" : "branco";

        int[] posHeroi = jogoDeXadrez.encontrarHeroi(turnoAtual);
        if (posHeroi == null) return;

        // Usa a função check que já considera bloqueios
        boolean reiEmCheck = jogoDeXadrez.check(posRei[0], posRei[1], corOponente, false);

        jogoDeXadrez.setFuriaHeroi(posHeroi[0], posHeroi[1], reiEmCheck);
    }


    private void trocarTurno() {
        turnoAtual = turnoAtual.equals("branco") ? "preto" : "branco";
        verificarCheck();
        boboConsegueSalvar = false; // Como é por turnos, só precisamos de uma variável se o bobo consegue salvar, que vai ser usada pelos dois lados
        pecasBoboImpedirCheckBrancas.clear(); // Limpar as peças que podem impedir o check do turno anterior
        pecasBoboImpedirCheckPretas.clear();
        atualizarTituloDaJanela();
        verificarFimDeJogo();
    }

    // Parte Visual

    public void desenharTabuleiro() {
        tabuleiroGrid.getChildren().clear();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                StackPane cell = new StackPane();
                if ((row + col) % 2 == 0) {
                    cell.setStyle("-fx-background-color: #eeeed2;");
                } else {
                    cell.setStyle("-fx-background-color: #673166;");
                }

                final int linha = row;
                final int coluna = col;

                Peca peca = jogoDeXadrez.getPeca(row, col);
                cell.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        handlePrimaryClick(cell, linha, coluna);
                    } else if (event.getButton() == MouseButton.SECONDARY) {
                        handleSecondaryClick(linha, coluna);
                    }
                });

                if (peca != null) {
                    Image image = new Image(
                            getClass().getResourceAsStream(
                                    "/images/" + peca.getCor() + "/" + peca.getNomePeca() + ".png"
                            )
                    );

                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(50);
                    imageView.setFitHeight(50);
                    cell.getChildren().add(imageView);
                }
                tabuleiroGrid.add(cell, col, row);
            }
        }
    }

    public void atualizarTabuleiro() {
        desenharTabuleiro();
    }

    private Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == column && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    public void mostrarMovimentosValidos(int linhaOrigem, int colunaOrigem) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // CORREÇÃO: Usar isMovimentoLegal para só mostrar movimentos 100% válidos
                if (isMovimentoLegal(linhaOrigem, colunaOrigem, i, j)) {
                    javafx.scene.shape.Circle circuloIndicador = new javafx.scene.shape.Circle(12);
                    circuloIndicador.setFill(javafx.scene.paint.Color.rgb(0, 0, 0, 0.3));
                    circuloIndicador.setMouseTransparent(true);
                    GridPane.setHalignment(circuloIndicador, HPos.CENTER);
                    GridPane.setValignment(circuloIndicador, VPos.CENTER);
                    tabuleiroGrid.add(circuloIndicador, j, i);
                }
            }
        }
    }

    private void limparMovimentosValidos() {
        // Criamos uma lista do tipoNode que vai armazenar todos os circulos que queremos remover
        List<Node> circulosParaRemover = new ArrayList<>();
        // Vamos percorrer cada node na filhos da grid do tabuleiro
        for (Node node : tabuleiroGrid.getChildren()) {
            // Sempre que esse esse node for uma instância de circulo, adicionamos nesse array de circulos pra remover
            if (node instanceof javafx.scene.shape.Circle) {
                circulosParaRemover.add(node);
            }
        }

        // Vamos na nossa grid do tabuleiro, pegamos todos os filhos dele, e removemos todo o array de circulos pra remover
        tabuleiroGrid.getChildren().removeAll(circulosParaRemover);
    }

    private void mostrarDialogoDoLadrao(int linhaInicial, int colunaInicial, int linha, int coluna) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Habilidade do Ladrão!");
        alert.setHeaderText("O Ladrão capturou uma peça.");
        alert.setContentText("Deseja usar a habilidade para voltar à casa original?");

        try {
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/ladrao-dialog.css").toExternalForm()
            );
        } catch (Exception e) {
            System.out.println("Arquivo CSS do Ladrão não encontrado, usando estilo padrão.");
        }

        ButtonType botaoSim = new ButtonType("Sim, recuar!");
        ButtonType botaoNao = new ButtonType("Não, ficar.");
        alert.getButtonTypes().setAll(botaoSim, botaoNao);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == botaoSim) {
            // Se o jogador escolheu SIM, move a peça de volta
            jogoDeXadrez.moverPeca(linha, coluna, linhaInicial, colunaInicial);
            atualizarTabuleiro(); // Atualiza o visual de novo para mostrar o recuo
        }
    }

    private void atualizarTituloDaJanela() {
        if (tabuleiroGrid.getScene() == null || tabuleiroGrid.getScene().getWindow() == null) return;
        Stage stage = (Stage) tabuleiroGrid.getScene().getWindow();
        String corCapitalizada = turnoAtual.equals("branco") ? "Brancas " : "Pretas ";
        stage.setTitle("XXXadrez! - Vez das " + corCapitalizada);
    }

    public void deselecionarPeca() {
        pecaSelecionada = null;
        cellSelecionada = null;
    }
}