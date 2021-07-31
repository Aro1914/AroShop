package aroshop;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class OnClose implements Runnable {

	static Stage stage;

	static boolean yesBtnClicked;

	protected static boolean confirmationBox() {

		yesBtnClicked = false;

		stage = new Stage();

		stage.initModality(Modality.APPLICATION_MODAL);

		stage.setTitle("Close AroShop?");
		stage.setMinWidth(400);
		stage.setResizable(false);
		stage.setMinHeight(150);

		Label lbl = new Label();
		lbl.setText("Do you really want to close the application?");

		Button yes = new Button("Yes");

		yes.setOnAction(e -> yes_clicked());
		yes.setDefaultButton(true);
		yes.getStyleClass().add("pos-Btn");

		Button no = new Button("No");
		no.setOnAction(e -> no_clicked());
		no.setCancelButton(true);
		no.getStyleClass().add("neg-Btn");

		HBox btnBox = new HBox(10, yes, no);
		btnBox.setAlignment(Pos.CENTER);

		BorderPane right = new BorderPane();
		right.setRight(btnBox);

		BorderPane borderPane = new BorderPane();

		borderPane.setBottom(right);
		borderPane.setCenter(lbl);

		borderPane.setPadding(new Insets(10));

		Scene scene = new Scene(borderPane);
		scene.getStylesheets().add("/Styling/OnClose.css");

		Image aro = new Image("/Icon/Aro.png");

		stage.getIcons().add(aro);
		stage.setScene(scene);
		stage.initOwner(AroShop.stage);
		stage.showAndWait();

		return yesBtnClicked;

	}

	protected static void yes_clicked() {
		stage.close();
		yesBtnClicked = true;
	}

	protected static void no_clicked() {
		stage.close();
		yesBtnClicked = false;
	}

	public void run() {
		boolean reallyQuit = false;

		reallyQuit = confirmationBox();

		if (reallyQuit) {
			AroShop.stage.close();
		}
	}

}
