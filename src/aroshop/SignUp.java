package aroshop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

public class SignUp {

	VBox box = new VBox();

	Label usernameLabel;
	Label age;
	Label sex;
	Label questLabel;
	Label answerLabel;
	Label setPswd;
	Label conPswd;
	Label info;
	Label recovery;

	Button createButton;

	private final Logger LOG = Logger.getLogger(SignUp.class.getName());

	private TextField usernameField;

	private TextField answer;

	private TextField ageField;

	private PasswordField password;

	private PasswordField conPassword;

	private ComboBox<String> sexComboBox;

	private ComboBox<String> questions;

	private TextField question;
	Button goBack;

//	public SignUp(String name, BorderPane lay) {
//
//		info = new Label("Admin Info");
//		info.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 20));
//		info.getStyleClass().add("theme-label");
//
//		usernameLabel = new Label("Username:");
//		usernameLabel.setAlignment(Pos.CENTER_LEFT);
//		usernameField = new TextField();
//
//		setPswd = new Label("Set Password:");
//		setPswd.setAlignment(Pos.CENTER_LEFT);
//		password = new PasswordField();
//		password.setPromptText("Enter Password");
//
//		conPswd = new Label("Confirm Password:");
//
//		conPswd.setAlignment(Pos.CENTER_LEFT);
//		conPassword = new PasswordField();
//		conPassword.setPromptText("Re-enter Your Password");
//
//		VBox adminBox = new VBox(5, usernameLabel, usernameField, setPswd, password, conPswd, conPassword);
//
//		recovery = new Label("Password Recovery Settings");
//		recovery.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 20));
//		recovery.getStyleClass().add("theme-label");
//		recovery.setAlignment(Pos.CENTER);
//
//		questLabel = new Label("Choose a Recovery Question:");
//		questLabel.setAlignment(Pos.CENTER_LEFT);
//		question = new TextField();
//		question.setPromptText("Question");
//		question.setPrefWidth(450);
//
//		answerLabel = new Label("Answer:");
//		answerLabel.setAlignment(Pos.CENTER_LEFT);
//		answer = new TextField();
//		answer.setPromptText("Please choose wisely");
//
//		VBox recoBox = new VBox(5, questLabel, question, answerLabel, answer);
//
//		VBox base = new VBox(10, info, adminBox, recovery, recoBox);
//		base.setMaxSize(450, 450);
////		base.setPadding(new Insets(20,0,0,0));
//		base.setAlignment(Pos.CENTER);
//
//		lay.setCenter(base);
//
//		createButton = new Button("Complete Admin Setup");
//		createButton.setOnAction(e -> {
//			String w = usernameField.getText();
//			String x = password.getText();
//			String t = conPassword.getText();
//			String y = question.getText();
//			String z = answer.getText();
//			if ("".equals(w) || "".equals(x) || "".equals(y) || "".equals(z) || "".equals(t)) {
//				DBManagement.alert("Empty Field(s)!", "A field or more have not been filled");
//			} else if (!x.equals(t)) {
//				DBManagement.alert("Invalid Password!", "Your password entries don't match");
//			} else if (x.length() < 8) {
//				DBManagement.alert("Weak Password!", "The password is too short", "It must be at least 8 characters long");
//			} else {
//				try {
//					ResultSet result;
//
//					String jdbcUrl = "jdbc:sqlite:inventory - Copy.db";
//					Connection connect = DriverManager.getConnection(jdbcUrl);
//					String sql = "Select * from userinfo where username = ?;";
//					PreparedStatement statement = connect.prepareStatement(sql);
//
//					statement.setString(1, w);
//
//					result = statement.executeQuery();
//
//					if (result.next()) {
//						DBManagement.alert("Username Unavaliable!", "This username is taken, choose something else");
//					} else {
//						DBManagement.createAdmin();
//					}
//
//					connect.close();
//					statement.close();
//					result.close();
//				} catch (SQLException em) {
//					DBManagement.alert(DBManagement.error, DBManagement.errorMessage);
//				}
//			}
//
//		});
//		createButton.getStyleClass().add("button-ui");
//
//		HBox box = new HBox(createButton);
//		box.setPadding(new Insets(10));
//		box.setAlignment(Pos.CENTER);
//
//		lay.setBottom(box);
//
//	}

	public SignUp(BorderPane lay) {

		VBox box = new VBox(5);

		VBox boxLeft = new VBox(17);
		VBox boxRight = new VBox(5);

		//User Info
		info = new Label("User Info");
		info.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 20));
		info.getStyleClass().add("theme-label");

		usernameLabel = new Label("Username:");
		usernameField = new TextField();
		usernameField.setPromptText("Enter Username");

		age = new Label("Age:");
		ageField = new TextField();
		ageField.setPromptText("Enter your age");

		sex = new Label("Sex:");
		sexComboBox = new ComboBox<>();
		sexComboBox.getItems().addAll("Male", "Female");
		sexComboBox.setEditable(false);
		sexComboBox.setPromptText("Male or Female?");

		setPswd = new Label("Password:");
		password = new PasswordField();
		password.setPromptText("Enter Password");

		conPswd = new Label("Confirm Password:");
		conPswd.setAlignment(Pos.CENTER_LEFT);
		conPassword = new PasswordField();
		conPassword.setPromptText("Re-enter Your Password");

		boxLeft.getChildren().addAll(usernameLabel, age, sex, setPswd, conPswd);
		boxLeft.setAlignment(Pos.CENTER_RIGHT);
		usernameLabel.setPadding(new Insets(3, 0, 0, 0));

		boxRight.getChildren().addAll(usernameField, ageField, sexComboBox, password, conPassword);
		usernameField.setPrefWidth(300);
		ageField.setPrefWidth(300);
		sexComboBox.setPrefWidth(300);
		password.setPrefWidth(300);
		conPassword.setPrefWidth(300);

		HBox userInfo = new HBox(10, boxLeft, boxRight);
		userInfo.setMaxWidth(450);
		userInfo.setAlignment(Pos.CENTER);

		// Recovery Controls
		recovery = new Label("Password Recovery Settings");
		recovery.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 20));
		recovery.getStyleClass().add("theme-label");

		questLabel = new Label("Choose a Recovery Question:");
		questions = new ComboBox<>();
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
//		questions.setPromptText("Question:");
		questions.setPrefWidth(450);
		questions.getSelectionModel().select(0);

		VBox questBox = new VBox(5, questLabel, questions);

		answerLabel = new Label("Answer:");
		answer = new TextField();
		answer.setPromptText("Please choose wisely");

		VBox answerBox = new VBox(5, answerLabel, answer);
		answerBox.setVgrow(answer, Priority.ALWAYS);

		VBox recoveryBox = new VBox(20, questBox, answerBox);
		recoveryBox.setMaxWidth(450);
		recoveryBox.setAlignment(Pos.CENTER_LEFT);

		createButton = new Button("Create Account");
		createButton.setOnAction(e -> {
			String u = usernameField.getText();
			String w = password.getText();
			String t = conPassword.getText();
			int v = 0;
			String v2 = ageField.getText();
			String x = sexComboBox.getValue();
			String y = questions.getValue();
			String z = answer.getText();

			if ("".equals(u) || "".equals(w) || "".equals(v2) || "".equals(x) || "".equals(y) || "".equals(z) || "".equals(t)) {
				DBManagement.alert("Empty Field(s)!", "A field or more have not been filled");
			} else if (!w.equals(t)) {
				DBManagement.alert("Invalid Password!", "Your password entries don't match");
			} else if (w.length() < 8) {
				DBManagement.alert("Weak Password!", "The password is too short", "It must be at least 8 characters long");
			} else {
				try {
					v += Integer.parseInt(ageField.getText());
					try {
						ResultSet result;

						String jdbcUrl = "jdbc:sqlite:src/Database/AroShop.db";
						Connection connect = DriverManager.getConnection(jdbcUrl);
						String sql = "Select * from userinfo where username = ?;";
						PreparedStatement statement = connect.prepareStatement(sql);

						statement.setString(1, u);

						result = statement.executeQuery();

						if (result.next()) {
							DBManagement.alert("Username Unavaliable", "This username is taken, choose something else");
						} else {
							DBManagement.signUp();
						}
						connect.close();
						statement.close();
						result.close();
					} catch (SQLException em) {
						DBManagement.alert(DBManagement.error, DBManagement.errorMessage);
					}
				} catch (Exception f) {
					DBManagement.alert("Error Accepting Your Age", "Your age must be in numerical digits");
				}

			}

		});
		createButton.getStyleClass().add("button-ui");

		goBack = new Button("Cancel");
		goBack.setOnAction(e
				-> AroShop.viewLogin());
		goBack.getStyleClass().add("button-ui");

		Region spacer = new Region();

		HBox buttomBox = new HBox(10, spacer, createButton, goBack);

		buttomBox.setHgrow(spacer, Priority.ALWAYS);

		buttomBox.setPadding(
				new Insets(10));

		box.getChildren()
				.addAll(info, userInfo, recovery, recoveryBox);
		box.setMargin(recovery,
				new Insets(30, 0, 0, 0));
		box.setMaxWidth(
				500);
		box.setAlignment(Pos.CENTER);

		lay.setCenter(box);

		lay.setBottom(buttomBox);

	}

	public TextField getQuestion() {
		return question;
	}

	public TextField getConpassword() {
		return conPassword;
	}

	public void setConPassword(PasswordField aConpassword) {
		conPassword = aConpassword;
	}

	protected TextField getUsernameField() {
		return usernameField;
	}

	protected TextField getAgeField() {
		return ageField;
	}

	protected ComboBox<String> getSexComboBox() {
		return sexComboBox;
	}

	protected ComboBox<String> getQuestions() {
		return questions;
	}

	protected TextField getAnswer() {
		return answer;
	}

	protected TextField getPassword() {
		return password;
	}

}
