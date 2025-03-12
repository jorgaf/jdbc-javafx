package ec.edu.utpl.carreras.computacion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Hello world!
 */
public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ec/edu/utpl/carreras/computacion/MainView.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Administración de Citas - Barbería FX");
        stage.setScene(scene);
        stage.show();
    }
}
