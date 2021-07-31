package aroshop;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ForgotPassword {

	Label questLabel;
	Label answerLabel;
	Label usernameLabel;
	Button confirm;
	Button goBack;

	private ComboBox<String> questions;
	private TextField answer;
	private TextField usernameField;

	public ForgotPassword() {
	}

	public ForgotPassword(BorderPane lay) {

		HBox box = new HBox(5);

		VBox boxLeft = new VBox(17);
		VBox boxRight = new VBox(5);

		usernameLabel = new Label("Username:");
		usernameField = new TextField();
		usernameField.setPromptText("Username");
		usernameField.setPrefWidth(300);

		questLabel = new Label("Question:");
		questions = new ComboBox<String>();
		questions.getItems().addAll(
				"",
				"What's your favorite dog?",
				"How many games have you played?",
				"What's the name of your best childhood friend?",
				"What color is your car?",
				"What dish does your wife cook that just tips you off?",
				"What was the brand of your first wristwatch?"
		);
		questions.setEditable(true);
		questions.getSelectionModel().select(0);
		questions.setPrefWidth(300);

		answerLabel = new Label("Answer:");
		answer = new TextField();
		answer.setPrefWidth(300);

		boxLeft.getChildren().addAll(usernameLabel, questLabel, answerLabel);
		boxLeft.setAlignment(Pos.CENTER_RIGHT);

		boxRight.getChildren().addAll(usernameField, questions, answer);
		boxRight.setAlignment(Pos.CENTER);

		box.getChildren().addAll(boxLeft, boxRight);
		box.setMaxWidth(500);
		box.setAlignment(Pos.CENTER);

		confirm = new Button("Confirm");
		confirm.setOnAction(e -> confirmInfo());
		confirm.getStyleClass().add("button-ui");

		VBox centerBox = new VBox(50, box, confirm);
		centerBox.setAlignment(Pos.CENTER);

		Region spacer = new Region();

		goBack = new Button("Cancel");
		goBack.setOnAction(e -> AroShop.viewLogin());
		goBack.getStyleClass().add("button-ui");

		HBox bottom = new HBox(10, spacer, goBack);
		bottom.setHgrow(spacer, Priority.ALWAYS);
		bottom.setPadding(new Insets(10));

		lay.setCenter(centerBox);

		lay.setBottom(bottom);
	}

	private void confirmInfo() {
		String x = getUsernameField().getText();
		String y = getQuestions().getValue();
		String z = getAnswer().getText();
		if (x.isEmpty() && y.isEmpty() && z.isEmpty()) {
			DBManagement.alert("Complete the form!", "Please fill in your recovery information");
		} else if (x.isEmpty()) {
			DBManagement.alert("Empty Username Field!", "Now please fill in your username");
		} else if (y.isEmpty()) {
			DBManagement.alert("Empty Question Field!", "Now please fill in your recovery question");
		} else if (z.isEmpty()) {
			DBManagement.alert("Empty Answer Field!", "Now please fill in your recovery answer");
		} else {
			DBManagement.getPassword();
		}
	}

	protected ComboBox<String> getQuestions() {
		return questions;
	}

	protected TextField getAnswer() {
		return answer;
	}

	protected TextField getUsernameField() {
		return usernameField;
	}

}
