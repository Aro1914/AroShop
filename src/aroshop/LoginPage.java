package aroshop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class LoginPage {

	BorderPane lay = new BorderPane();

	Label welcome;

	Button loginBtn;
	Button signUpBtn;
	Button fgtPassBtn;

	protected TextField usernameField;

	protected PasswordField passField;

	protected String previousUser;

	public LoginPage(BorderPane lay) {

		welcome = new Label("AroShop");
		welcome.getStyleClass().add("welcome-label");

		this.lay = lay;

		usernameField = new TextField();
		usernameField.setPromptText("Enter Username");
		usernameField.setMaxWidth(300);

		HBox userBox = new HBox(usernameField);
		userBox.setHgrow(usernameField, Priority.ALWAYS);
		userBox.setAlignment(Pos.CENTER);

		passField = new PasswordField();
		passField.setPromptText("Enter Password");
		passField.setMaxWidth(300);

		HBox passBox = new HBox(passField);
		passBox.setHgrow(passField, Priority.ALWAYS);
		passBox.setAlignment(Pos.CENTER);

		fgtPassBtn = new Button("Forgot Password?");
		fgtPassBtn.setOnAction(e -> AroShop.viewForPass());

		HBox fgtBox = new HBox(fgtPassBtn);
		fgtBox.setMaxWidth(300);
		fgtBox.setMargin(fgtPassBtn, new Insets(0, 0, 20, 0));
		fgtPassBtn.setAlignment(Pos.CENTER_RIGHT);
		fgtPassBtn.getStyleClass().add("forgot-password");

		loginBtn = new Button("Log In");
		loginBtn.setOnAction(((ActionEvent event) -> {
			if (getUsernameField().getText().isEmpty() && getPassField().getText().isEmpty()) {
				DBManagement.alert("Complete the form!", "Please fill in your username and password");
			} else if (getUsernameField().getText().isEmpty()) {
				DBManagement.alert("Empty Username Field!", "Now please fill in your username");
			} else if (getPassField().getText().isEmpty()) {
				DBManagement.alert("Empty Password Field!", "Now please fill in your password");
			} else {
				String jdbcUrl = "jdbc:sqlite:src/Database/AroShop.db";
				PreparedStatement statement2;
				ResultSet result2;
				try ( Connection connect = DriverManager.getConnection(jdbcUrl)) {

					String sql2 = "SELECT password FROM userinfo WHERE username = ?;";
					statement2 = connect.prepareStatement(sql2);
					statement2.setString(1, getUsernameField().getText());

					result2 = statement2.executeQuery();

					if (result2.next()) {
						previousUser = AroShop.userName;
						AroShop.setUsername();
						DBManagement.valUser();
					} else {
						DBManagement.alert("Account Not Found!", "The username you provided could not be found", "Use the 'Sign Up' botton if you are new");
					}
				} catch (SQLException ex) {
					DBManagement.alert(DBManagement.error, DBManagement.errorMessage);
				}

			}
		}));
		loginBtn.setDefaultButton(true);
		loginBtn.getStyleClass().add("log-in");

		signUpBtn = new Button("Sign Up");
		signUpBtn.setOnAction(e -> AroShop.viewSign());
		signUpBtn.getStyleClass().add("sign-up");

		HBox logBox = new HBox(loginBtn);
		loginBtn.setAlignment(Pos.CENTER);

		VBox logInBox = new VBox(5, welcome, userBox, passBox, fgtBox, logBox, signUpBtn);
		fgtBox.setAlignment(Pos.CENTER_RIGHT);
		logBox.setAlignment(Pos.CENTER);
		logInBox.setMargin(welcome, new Insets(0, 0, 50, 0));
		logInBox.setMargin(signUpBtn, new Insets(50, 0, 0, 0));

		lay.setCenter(logInBox);

		logInBox.setAlignment(Pos.CENTER);
		logInBox.setMaxWidth(500);

	}

	public TextField getUsernameField() {
		return usernameField;
	}

	public void setUsernameField(TextField aUsernameField) {
		usernameField = aUsernameField;
	}

	public TextField getPassField() {
		return passField;
	}

	public void setPassField(PasswordField aPassField) {
		passField = aPassField;
	}

}
