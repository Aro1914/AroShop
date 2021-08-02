package aroshop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class DBManagement {

	static String password;

	static String error = "Error!";

	static String errorMessage = "Some kind of error occurred mate";

	static String jdbcUrl = "jdbc:sqlite:src/Database/AroShop.db";

	// Working methods
	public static void valUser() {
		try ( Connection connect = DriverManager.getConnection(jdbcUrl)) {

			String sql = "SELECT * FROM userinfo WHERE username = ? AND password = ?;";
			PreparedStatement statement = connect.prepareStatement(sql);

			statement.setString(1, AroShop.loginPage.getUsernameField().getText());
			statement.setString(2, AroShop.loginPage.getPassField().getText());

			ResultSet result = statement.executeQuery();

			if (result.next()) {
				alert("Login Success!", "Welcome " + AroShop.user_name);
				if (!AroShop.user_name.equals(AroShop.loginPage.previousUser)) {
					AroShop.newLook.loadCart();
					AroShop.newLook.loadPurchases();
				}
				if (AroShop.newLook.orderBox.getChildren().isEmpty()) {
					AroShop.newLook.order.setDisable(true);
				}
				if (AroShop.newLook.cartBox.getChildren().isEmpty()) {
					AroShop.newLook.cart.setDisable(true);
				}

				AroShop.viewNew();

				FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), AroShop.newLook.lay);
				fadeIn.setFromValue(0);
				fadeIn.setToValue(1);
				fadeIn.setCycleCount(1);
				fadeIn.play();

			} else {
				AroShop.loginPage.passField.setText("");
				alert("Wrong Password!", "Forgot password?", "Click the 'Forgot Password?' button");
			}
		} catch (SQLException ex) {
			alert(error, errorMessage);
		}
	}

	public static void getPassword() {
		try ( Connection connect = DriverManager.getConnection(jdbcUrl)) {
			String sql2 = "SELECT * FROM userinfo WHERE username = ?;";
			PreparedStatement statement2 = connect.prepareStatement(sql2);

			statement2.setString(1, AroShop.forgotPassword.getUsernameField().getText());

			ResultSet result2 = statement2.executeQuery();

			if (result2.next()) {
				PreparedStatement statement;
				try ( Connection connect1 = DriverManager.getConnection(jdbcUrl)) {

					String sql = "SELECT password FROM userinfo WHERE question = ? AND answer = ?;";
					statement = connect1.prepareStatement(sql);

					statement.setString(1, AroShop.forgotPassword.getQuestions().getValue());
					statement.setString(2, AroShop.forgotPassword.getAnswer().getText());

					ResultSet result = statement.executeQuery();

					if (result.next()) {
						password = result2.getString("password");
						alert("Retrieval Successful!", "Your password is '" + password + "'", "Please store it in a safe but accessible location");
						AroShop.viewLogin();
						AroShop.loginPage.usernameField.setText(AroShop.forgotPassword.getUsernameField().getText());
					} else {
						alert("Account Not Found!", "Sorry the information provided don't match");
					}
				} catch (Exception e) {
				}
			} else {
				alert("Account Not Found!", "The username you provided could not be found", "Please make sure your username is correct");
			}
		} catch (SQLException ex) {
			alert(error, errorMessage);
		}
	}

	protected static void signUp() {
		PreparedStatement pstmt;
		try ( Connection connection = DriverManager.getConnection(jdbcUrl)) {
			String sql = "INSERT INTO userinfo (username, password, age, sex, question, answer) VALUES(?,?,?,?,?,?);";
			pstmt = connection.prepareStatement(sql);

			pstmt.setString(1, AroShop.signUp.getUsernameField().getText());
			pstmt.setString(2, AroShop.signUp.getPassword().getText());
			pstmt.setInt(3, Integer.parseInt(AroShop.signUp.getAgeField().getText()));
			pstmt.setString(4, AroShop.signUp.getSexComboBox().getValue());
			pstmt.setString(5, AroShop.signUp.getQuestions().getValue());
			pstmt.setString(6, AroShop.signUp.getAnswer().getText());

			pstmt.executeUpdate();

			AroShop.viewLogin();
			AroShop.loginPage.usernameField.setText(AroShop.signUp.getUsernameField().getText());
			AroShop.loginPage.passField.setText(AroShop.signUp.getPassword().getText());
			alert("Sign Up Successful!", "You can now login to make purchases");
		} catch (SQLException e) {
			alert(error, errorMessage);
		}
	}
	// End of working methods

	// Alerts 
	protected static void alert(String title, String labelText) {
		Stage stage = new Stage();

		BorderPane pane = new BorderPane();

		Label msg = new Label(labelText + ".");
		msg.setAlignment(Pos.CENTER);

		Button ok = new Button("Ok");
		ok.setDefaultButton(true);
		ok.setOnAction(e -> stage.close());
		ok.setAlignment(Pos.CENTER);
		ok.getStyleClass().add("alert-button");

		HBox box = new HBox(ok);
		box.setPadding(new Insets(10));
		box.setAlignment(Pos.CENTER);

		pane.setCenter(msg);
		pane.setMargin(msg, new Insets(30, 0, 20, 0));
		pane.setBottom(box);

		Scene scene = new Scene(pane);
		scene.getStylesheets().add("/Styling/Alerts.css");

		Image aro = new Image("/Icon/Aro.png");

		stage.getIcons().add(aro);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setWidth(400);
		stage.setResizable(false);
		stage.setScene(scene);
		stage.initOwner(AroShop.stage);
		stage.centerOnScreen();
		stage.setTitle(title);
		stage.showAndWait();
	}

	protected static void alert(String title, String text, String text2) {
		Stage stage = new Stage();

		BorderPane pane = new BorderPane();

		Label msg = new Label(text + ".");
		msg.setAlignment(Pos.CENTER);

		Label msg2 = new Label(text2 + ".");
		msg2.setAlignment(Pos.CENTER);

		VBox labels = new VBox(5, msg, msg2);
		labels.setAlignment(Pos.CENTER);

		Button ok = new Button("Ok");
		ok.setDefaultButton(true);
		ok.setOnAction(e -> stage.close());
		ok.setAlignment(Pos.CENTER);
		ok.getStyleClass().add("alert-button");

		HBox box = new HBox(ok);
		box.setPadding(new Insets(10));
		box.setAlignment(Pos.CENTER);

		pane.setCenter(labels);
		pane.setMargin(labels, new Insets(30, 0, 20, 0));
		pane.setBottom(box);

		Scene scene = new Scene(pane);
		scene.getStylesheets().add("/Styling/Alerts.css");

		Image aro = new Image("/Icon/Aro.png");

		stage.getIcons().add(aro);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setWidth(400);
		stage.setResizable(false);
		stage.setScene(scene);
		stage.initOwner(AroShop.stage);
		stage.centerOnScreen();
		stage.setTitle(title);
		stage.showAndWait();
	}
	// End of Alerts
}
