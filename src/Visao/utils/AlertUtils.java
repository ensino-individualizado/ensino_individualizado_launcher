package Visao.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Gustavo Freitas on 02/06/2016.
 */
public class AlertUtils {

    private static AlertUtils instance = new AlertUtils();

    public void showException(String title, String header, Exception exception){

        Alert alert = new Alert(Alert.AlertType.ERROR);
        TextArea mensagem;

        alert.setTitle(title);
        alert.setHeaderText(header);

        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));
        String exceptionAsString = sw.toString();

        mensagem = new TextArea();
        mensagem.setEditable(false);
        mensagem.setWrapText(true);
        mensagem.setText("Descrição: " + exception.getMessage() + "\nRastro da Pilha:\n" + sw);

        mensagem.setMaxWidth(Double.MAX_VALUE);
        mensagem.setMaxHeight(Double.MAX_VALUE);
        mensagem.setMinWidth(1000.0);
        mensagem.setMinHeight(500.0);

        GridPane.setVgrow(mensagem, Priority.ALWAYS);
        GridPane.setHgrow(mensagem, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(mensagem, 0, 0);

        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }

    public static AlertUtils getInstance() {
        return instance;
    }
}
