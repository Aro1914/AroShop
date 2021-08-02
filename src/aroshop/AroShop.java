package aroshop;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AroShop extends Application {

	Label aroShop;
	Label motto;

	protected static String user_name;

	protected static void setUsername() {
		user_name = loginPage.usernameField.getText();
	}

	static Stage stage = new Stage();

	public static void main(String[] args) {
		launch(args);
	}

//	NewLook View
	static BorderPane newLookPane = new BorderPane();
	static NewLook newLook = new NewLook(newLookPane);
	static Scene newLookScene = new Scene(newLookPane, AroShop.stage.getWidth(), AroShop.stage.getHeight());

	protected static void viewNew() {
		newLookScene.getStylesheets().add("/Styling/AroShop.css");
		AroShop.stage.setScene(newLookScene);
		AroShop.stage.setTitle("AroShop - Now place that order " + user_name +"!");
	}
//	End of NewLook View

//	ForgotPassword View
	static BorderPane forgotPasswordPane = new BorderPane();
	static ForgotPassword forgotPassword;
	static Scene forgotPasswordScene = new Scene(forgotPasswordPane, AroShop.stage.getWidth(), AroShop.stage.getHeight());

	protected static void viewForPass() {
		forgotPassword = new ForgotPassword(forgotPasswordPane);
		forgotPasswordScene.getStylesheets().add("/Styling/AroShop.css");
		AroShop.stage.setScene(forgotPasswordScene);
		AroShop.stage.setTitle("AroShop - Password Retrieval");
	}
//	End of ForgotPassword View

//	Login VIew
	static BorderPane loginPagePane = new BorderPane();
	static LoginPage loginPage;
	static Scene loginPageScene = new Scene(loginPagePane, AroShop.stage.getWidth(), AroShop.stage.getHeight());

	protected static void viewLogin() {
		loginPage = new LoginPage(loginPagePane);
		loginPageScene.getStylesheets().add("/Styling/AroShop.css");
		AroShop.stage.setScene(loginPageScene);
		AroShop.stage.setTitle("AroShop - Log In");
	}
//	End of Login View

//	Create Admin View from SignUp
	static BorderPane createAdminPane = new BorderPane();
	static SignUp createAdmin;
	static Scene createAdminScene = new Scene(createAdminPane, AroShop.stage.getWidth(), AroShop.stage.getHeight());

//	SignUp View
	static BorderPane signUpPane = new BorderPane();
	static SignUp signUp;
	static Scene signUpScene = new Scene(signUpPane, AroShop.stage.getWidth(), AroShop.stage.getHeight());

	protected static void viewSign() {
		signUp = new SignUp(signUpPane);
		signUpScene.getStylesheets().add("/Styling/AroShop.css");
		AroShop.stage.setScene(signUpScene);
		AroShop.stage.setTitle("AroShop - Sign Up");
	}
//	End of SignUp View

//	Log out confirmation alert
	protected static void logOut(boolean x) {
		Stage stage = new Stage();

		BorderPane pane = new BorderPane();

		Label msg = new Label("Do you really want to log out?");
		msg.setAlignment(Pos.CENTER);

		VBox vBox = new VBox(5, msg);
		vBox.setAlignment(Pos.CENTER);

		Button yes = new Button("Yes");
		yes.setOnAction(e -> {
			stage.close();
			viewLogin();
			if (x) {
				newLook.scrl.setVvalue(0);
				newLook.boxTop.getChildren().remove(0);
				newLook.boxTop.getChildren().add(0, newLook.categoryBox);
				newLook.categoryBox.getSelectionModel().select(0);
				newLook.cart.setDisable(false);
				newLook.order.setDisable(false);
				newLook.lay.setBottom(null);
				newLook.lay.setCenter(newLook.scrl);
			}
		});
		yes.setDefaultButton(true);
		yes.getStyleClass().add("pos-Btn");

		Button no = new Button("No");
		no.setOnAction(e -> {
			stage.close();
		});
		no.setCancelButton(true);
		no.getStyleClass().add("neg-Btn");

		Region spacer = new Region();

		HBox box = new HBox(10, spacer, yes, no);
		box.setHgrow(spacer, Priority.ALWAYS);
		box.setPadding(new Insets(10));
		box.setAlignment(Pos.CENTER);

		pane.setCenter(vBox);
		pane.setMargin(vBox, new Insets(10, 0, 0, 0));
		pane.setBottom(box);

		Scene scene = new Scene(pane);
		scene.getStylesheets().add("/Styling/OnClose.css");
		Image aro = new Image("/Icon/Aro.png");

		stage.getIcons().add(aro);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setWidth(400);
		stage.setResizable(false);
		stage.initOwner(AroShop.stage);
		stage.setScene(scene);
		stage.setTitle("Log out?");
		stage.showAndWait();
	}

	BorderPane root = new BorderPane();

	@Override
	public void start(Stage primaryStage) {

		this.stage = primaryStage;

		OnClose onClose = new OnClose();

		Scene scene = new Scene(root);
		scene.getStylesheets().add("/Styling/Welcome.css");

		Image aro = new Image("/Icon/Aro.png");

		primaryStage.getIcons().add(aro);

		primaryStage.setTitle("AroShop");
		primaryStage.setScene(scene);

		primaryStage.setWidth(650);
		primaryStage.setHeight(550);
		primaryStage.centerOnScreen();
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(e -> {
			e.consume();
			onClose.run();
		});
		primaryStage.show();
		loadSplashScreen();
	}

	private void loadSplashScreen() {
		aroShop = new Label("AroShop");
		aroShop.getStyleClass().add("aroShop");

		motto = new Label("...we give smiles to your pockets.");
		motto.getStyleClass().add("motto");

		VBox box = new VBox(20, aroShop, motto);
		box.setAlignment(Pos.CENTER);

		root.setCenter(box);

		FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), box);
		fadeIn.setFromValue(0);
		fadeIn.setToValue(1);
		fadeIn.setCycleCount(1);
		
		FadeTransition stay = new FadeTransition(Duration.seconds(2), box);
		stay.setFromValue(1);
		stay.setToValue(1);
		stay.setCycleCount(1);

		FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), box);
		fadeOut.setFromValue(1);
		fadeOut.setToValue(0);
		fadeOut.setCycleCount(1);

		fadeIn.play();		
		
		fadeIn.setOnFinished(e -> {
			stay.play();
		});
		
		stay.setOnFinished(e -> {
			fadeOut.play();
		});

		fadeOut.setOnFinished(e -> {
			viewLogin();
		});
	}

}
