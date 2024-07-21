package fxLiikuntapaivakirja;
	
import javafx.application.Application;
import javafx.stage.Stage;
import liikuntapaivakirja.Liikuntapaivakirja;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

/**
 * Käynnistetään ohjelma.
 * @author Kaisa 
 * @version Apr 23, 2021
 * 
 */
public class LiikuntapaivakirjaMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
		    FXMLLoader ldr = new FXMLLoader(getClass().getResource("LiikuntapaivakirjaGUIView.fxml"));
	        final Pane root = (Pane)ldr.load();
	        final LiikuntapaivakirjaGUIController liikuntapaivakirjaCtrl = (LiikuntapaivakirjaGUIController)ldr.getController();
	        
	        final Scene scene = new Scene(root);
	        scene.getStylesheets().add(getClass().getResource("liikuntapaivakirja.css").toExternalForm()); 
	        primaryStage.setScene(scene);
	        primaryStage.setTitle("Liikuntapaivakirja"); 
	        
	        Liikuntapaivakirja liikuntapaivakirja = new Liikuntapaivakirja();
	        liikuntapaivakirjaCtrl.setLiikuntapaivakirja(liikuntapaivakirja);
	        liikuntapaivakirja.lueTiedostosta();
	        liikuntapaivakirjaCtrl.haeAlussa();
	        
	        primaryStage.setOnCloseRequest((event) -> {
	            // Kutsutaan voikoSulkea-metodia
	            if ( !liikuntapaivakirjaCtrl.voikoSulkea() ) event.consume(); 
	        });
			primaryStage.show();
			
		} catch(Exception e) {
		    e.printStackTrace();
		}
				
		
	}
	
	/**
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
