package de.celineevelyn.kugelbahn;
	
import java.net.URL;

import de.celineevelyn.kugelbahn.controller.MainScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application 
{
	
	@Override
	public void start(Stage primaryStage) 
	{
		try 
		{
			
			URL rootfxml = getClass().getResource("/de/celineevelyn/kugelbahn/MainScreen.fxml");
			FXMLLoader loader = new FXMLLoader(rootfxml);
			Parent root = (Parent)loader.load();
			MainScreenController controller = (MainScreenController)loader.getController();
			Scene scene = new Scene(root,1500,780);
			scene.getStylesheets().add(getClass().getResource("/de/celineevelyn/kugelbahn/css/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
			
			controller.postInit();
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) 
	{
		launch(args);
	}
}
