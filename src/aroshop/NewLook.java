/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aroshop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Hp
 */
public class NewLook {

//	ComboBoxes
	ComboBox<String> categoryBox;
	ComboBox<String> quantityComboBox;

//	Category list
	ObservableList<Node> catObject = FXCollections.observableArrayList();
	ObservableList<String> categoryList = FXCollections.observableArrayList("All Items");

//	Spacer
	Region spacer;

//	Buttons
	Button logOut;
	Button cart;
	Button goBack;
	Button order;

//	Layouts
	BorderPane lay;
	ScrollPane scrl;
	VBox tile;
	TilePane catTile1;
	HBox boxTop;
	VBox cartBox = new VBox(20);
	VBox orderBox = new VBox(20);
	ScrollPane scrl2 = new ScrollPane(cartBox);
	ScrollPane scrl3 = new ScrollPane(orderBox);
	VBox bottomBox;

//	Database path
	String jdbcUrl = "jdbc:sqlite:src/Database/AroShop.db";

	public NewLook() {
	}

	public NewLook(BorderPane lay) {
		this.lay = lay;

		cartBox.setPadding(new Insets(20));
		orderBox.setPadding(new Insets(20));

		catTile1 = new TilePane(Orientation.HORIZONTAL, 0, 10);
		catTile1.setAlignment(Pos.CENTER);
		catTile1.setPrefColumns(3);
		loadShop();
		catObject.add(0, catTile1);

		Button confirm = new Button("Confirm Purchase");
		confirm.setOnAction(e -> confirmation());
		confirm.getStyleClass().add("button-ui");
		confirm.setAlignment(Pos.CENTER);
		bottomBox = new VBox(confirm);
		bottomBox.setAlignment(Pos.CENTER);

		goBack = new Button("Back to Shop");
		goBack.getStyleClass().add("button-ui");
		goBack.setOnAction(e -> {
			boxTop.getChildren().remove(0);
			boxTop.getChildren().add(0, categoryBox);
			lay.setCenter(scrl);
			lay.setBottom(null);

			if (cart.isDisabled() && !cartBox.getChildren().isEmpty()) {
				cart.setDisable(false);
			}

			if (order.isDisabled() && !orderBox.getChildren().isEmpty()) {
				order.setDisable(false);
			}
		});

		scrl2.setFitToHeight(true);
		scrl2.setFitToWidth(true);
		scrl2.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

		ResultSet result;
		PreparedStatement statement;
		try ( Connection connect = DriverManager.getConnection(jdbcUrl)) {
			String sql = "SELECT category FROM category;";
			statement = connect.prepareStatement(sql);
			result = statement.executeQuery();
			if (result.next()) {
				do {
					categoryList.add(result.getString("category"));
					result.next();
				} while (!result.isAfterLast());
			}
		} catch (Exception e) {
			System.err.println("Exception: " + e);
		}
		categoryBox = new ComboBox<>(categoryList);
		categoryBox.setEditable(false);
		categoryBox.getSelectionModel().select(0);
		categoryBox.setOnAction(e -> {
			sortItems(categoryBox.getSelectionModel().getSelectedIndex());
		});

		spacer = new Region();

		cart = new Button("View Cart");
		cart.setOnAction(e -> viewCart());
		cart.getStyleClass().add("button-ui");

		order = new Button("Purchase History");
		order.setOnAction(e -> viewOrder());
		order.getStyleClass().add("button-ui");

		logOut = new Button("Log Out");
		logOut.setOnAction(e -> {
			AroShop.logOut(true);
		});
		logOut.getStyleClass().add("log-out");

		boxTop = new HBox(10, categoryBox, spacer, cart, order, logOut);
		boxTop.setHgrow(spacer, Priority.ALWAYS);
		boxTop.setPadding(new Insets(10));
		lay.setTop(boxTop);

		tile = new VBox(10);
		tile.setPadding(new Insets(10, 0, 10, 0));
		tile.getChildren().add(catObject.get(0));
		tile.setAlignment(Pos.CENTER);
		scrl = new ScrollPane(tile);
		scrl.setFitToHeight(true);
		scrl.setFitToWidth(true);
		scrl.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		lay.setCenter(scrl);
	}

	private int getUserID() {
		int userID = 0;
		ResultSet result1;
		PreparedStatement statement1;
		try ( Connection connect1 = DriverManager.getConnection(jdbcUrl);) {
			String sql
					= "SELECT userID FROM userinfo "
					+ "WHERE username = ?;";
			statement1 = connect1.prepareStatement(sql);
			statement1.setString(1, AroShop.user_name);
			result1 = statement1.executeQuery();

			if (result1.next()) {
				userID = result1.getInt("userID");
			}
		} catch (Exception f) {
			System.err.println("Exception 7: " + f);
		}
		return userID;
	}

	private void loadShop() {
		ResultSet result1;
		PreparedStatement statement1;
		try ( Connection connect1 = DriverManager.getConnection(jdbcUrl)) {

			String sql = "SELECT category FROM category;";
			statement1 = connect1.prepareStatement(sql);
			result1 = statement1.executeQuery();
			if (result1.next()) {
				do {
					TilePane catTile = new TilePane(Orientation.HORIZONTAL, 0, 10);
					catTile.setPrefColumns(3);

					ObservableList<Node> catItems = FXCollections.observableArrayList();

					ResultSet result;
					PreparedStatement statement;
					String category = result1.getString("category");
					try ( Connection connect = DriverManager.getConnection(jdbcUrl)) {
						String sql1
								= "SELECT itemID, item FROM items "
								+ "INNER JOIN category "
								+ "ON items.categoryID = category.categoryID "
								+ "WHERE category = ? "
								+ "ORDER BY item ASC;";
						statement = connect.prepareStatement(sql1);
						statement.setString(1, category);
						result = statement.executeQuery();
						if (result.next()) {
							int itemID;
							String item;
							String image = "";
							do {
								PreparedStatement statement2;
								ResultSet result2;
								try {
									itemID = result.getInt("itemID");
									item = result.getString("item");

									try ( Connection connect2 = DriverManager.getConnection(jdbcUrl)) {
										String sql2
												= "SELECT image FROM images "
												+ "WHERE itemID = ? "
												+ "LIMIT 1;";
										statement2 = connect2.prepareStatement(sql2);
										statement2.setInt(1, itemID);

										result2 = statement2.executeQuery();

										image = result2.getString("image");
									}

									String tempPath = image;

									Image img = new Image(
											"/Images/" + image);
									ImageView imgView = new ImageView(img);
									imgView.setFitWidth(140);
									imgView.setFitWidth(140);
									imgView.setPreserveRatio(true);

									Button view = new Button(item);
									view.setOnAction(e -> {
										viewItem(view.getText(), tempPath);
									});
									view.getStyleClass().add("item-Button");

									VBox box = new VBox(10, imgView, view);
									box.setAlignment(Pos.CENTER);
									catItems.add(box);
//
									Image img1 = new Image(
											"/Images/" + image);
									ImageView imgView1 = new ImageView(img1);
									imgView1.setFitWidth(140);
									imgView1.setFitWidth(140);
									imgView1.setPreserveRatio(true);

									Button view1 = new Button(item);
									view1.setOnAction(e -> {
										viewItem(view1.getText(), tempPath);
									});
									view1.getStyleClass().add("item-Button");

									VBox box1 = new VBox(10, imgView1, view1);
									box1.setAlignment(Pos.CENTER);
									catTile1.getChildren().add(0, box1);
								} catch (Exception e) {
									System.out.println("Exception 2: " + e);
								}
								result.next();
							} while (!result.isAfterLast());
						}
					} catch (SQLException ex) {
						AroShop.viewLogin();
						DBManagement.alert("Error!", "Sorry, an error ocurred", "while attempting to run the Customer UI");
					}

					catTile.getChildren().addAll(catItems);
					catTile.setAlignment(Pos.CENTER);
					catObject.add(catTile);
					result1.next();
				} while (!result1.isAfterLast());

			}
		} catch (Exception e) {
			System.err.println("Exception: " + e);
		}
	}

	protected void sortItems(int x) {
		tile.getChildren().clear();
		tile.getChildren().add(catObject.get(x));
	}

	protected void loadCart() {
		ResultSet result;
		PreparedStatement statement;
		cartBox.getChildren().removeAll(cartBox.getChildren());
		try ( Connection connect = DriverManager.getConnection(jdbcUrl)) {
			String sql
					= "SELECT cartID, cart.itemID, item, quantity, cost FROM items "
					+ "INNER JOIN cart "
					+ "ON items.itemID = cart.itemID "
					+ "WHERE cart.userID = ? "
					+ "ORDER BY cartID DESC;";
			statement = connect.prepareStatement(sql);
			statement.setInt(1, getUserID());
			result = statement.executeQuery();

			ResultSet result2;
			PreparedStatement statement2;
			if (result.next()) {
				do {
					int itemID = result.getInt("itemID");
					HBox pendingItem = new HBox(55);
					try ( Connection connect2 = DriverManager.getConnection(jdbcUrl)) {
						String sql2
								= "SELECT image FROM images "
								+ "WHERE itemID = ? "
								+ "LIMIT 1";
						statement2 = connect2.prepareStatement(sql2);
						statement2.setInt(1, itemID);
						result2 = statement2.executeQuery();

						if (result2.next()) {

							Image img = new Image(
									"/Images/" + result2.getString("image"));
							ImageView imgView = new ImageView(img);
							imgView.setFitWidth(80);
							imgView.setFitWidth(80);
							imgView.setPreserveRatio(true);

							pendingItem.getChildren().add(imgView);

							VBox itemBox = new VBox(10);
							Label item = new Label(result.getString(("item")));

							HBox detailBox = new HBox(30);
							Label quantity = new Label("Quantity: " + Integer.toString(result.getInt("quantity")));
							Label cost = new Label("Cost: ₦" + Integer.toString(result.getInt("cost")));
							detailBox.getChildren().addAll(quantity, cost);
							itemBox.getChildren().addAll(item, detailBox);
							itemBox.setAlignment(Pos.CENTER);

							int cartID = result.getInt("cartID");

							Button delete = new Button("Drop Item");
							delete.setOnAction(e -> {
								deleteFromCart(cartID);
								cartBox.getChildren().remove(pendingItem);
								if (cartBox.getChildren().isEmpty()) {
									boxTop.getChildren().remove(0);
									boxTop.getChildren().add(0, categoryBox);
									lay.setCenter(scrl);
									lay.setBottom(null);
									cart.setDisable(true);
								}
							});
							delete.setVisible(false);
							delete.getStyleClass().add("delete");

							VBox delBox = new VBox(delete);
							delBox.setAlignment(Pos.CENTER);

							Region spacer = new Region();

//							This is a HBox, add the delete button to this layout pane
							pendingItem.getChildren().addAll(itemBox, spacer, delBox);
							pendingItem.setHgrow(spacer, Priority.ALWAYS);
							pendingItem.hoverProperty().addListener((observable, oldValue, newValue) -> {
								if (newValue) {
									delete.setVisible(true);
								} else {
									delete.setVisible(false);
								}
							});
						}
					} catch (Exception f) {
						System.err.println("loadCart Exception 1: " + f);
					}
					cartBox.getChildren().add(pendingItem);
					result.next();
				} while (!result.isAfterLast());
			}
		} catch (SQLException ex) {
			System.err.println("loadCart Exception 2: " + ex);
		}
	}

	private void viewCart() {
		lay.setCenter(scrl2);
		lay.setBottom(bottomBox);
		boxTop.getChildren().remove(0);
		boxTop.getChildren().add(0, goBack);
		cart.setDisable(true);

		if (order.isDisabled() && !orderBox.getChildren().isEmpty()) {
			order.setDisable(false);
		}
	}

	private void viewOrder() {
		lay.setBottom(null);
		lay.setCenter(scrl3);
		boxTop.getChildren().remove(0);
		boxTop.getChildren().add(0, goBack);
		order.setDisable(true);

		if (cart.isDisabled() && cartBox.getChildren().size() != 0) {
			cart.setDisable(false);
		}
	}

	private void saveToCart(int userID, int itemID, int quantity, double cost) {
		PreparedStatement statement;
		try ( Connection connect = DriverManager.getConnection(jdbcUrl)) {
			String sql
					= "INSERT INTO cart("
					+ "userID, "
					+ "itemID, "
					+ "quantity, "
					+ "cost"
					+ ") VALUES("
					+ "?, ?, ?, ?);";
			statement = connect.prepareStatement(sql);
			statement.setInt(1, userID);
			statement.setInt(2, itemID);
			statement.setInt(3, quantity);
			statement.setDouble(4, cost);
			statement.executeUpdate();

			DBManagement.alert("Success!", "Your choice has been saved");
			if (cart.isDisabled()) {
				cart.setDisable(false);
			}
		} catch (SQLException e) {
			System.err.println("SQLException 1: " + e);
		} catch (Exception e) {
			System.err.println("Exception 9: " + e);
		}

	}

	private void updateCartView() {
		ResultSet result;
		PreparedStatement statement;

		try ( Connection connect = DriverManager.getConnection(jdbcUrl)) {

			String sql
					= "SELECT cartID, cart.itemID, item, quantity, cost FROM items "
					+ "INNER JOIN cart "
					+ "ON items.itemID = cart.itemID "
					+ "WHERE cart.userID = ? "
					+ "ORDER BY cartID ASC "
					+ "LIMIT 1 OFFSET ?;";

			statement = connect.prepareStatement(sql);
			statement.setInt(1, getUserID());
			statement.setInt(2, cartBox.getChildren().size());

			result = statement.executeQuery();

			if (result.next()) {
				ResultSet result2;
				PreparedStatement statement2;

				int itemID = result.getInt("itemID");
				HBox orderBox = new HBox(55);

				try ( Connection connect2 = DriverManager.getConnection(jdbcUrl)) {
					String sql2
							= "SELECT image FROM images "
							+ "WHERE itemID = ? "
							+ "LIMIT 1";
					statement2 = connect2.prepareStatement(sql2);
					statement2.setInt(1, itemID);
					result2 = statement2.executeQuery();

					if (result2.next()) {
						Image img = new Image(
								"/Images/" + result2.getString("image"));
						ImageView imgView = new ImageView(img);
						imgView.setFitWidth(80);
						imgView.setFitWidth(80);
						imgView.setPreserveRatio(true);

						orderBox.getChildren().add(imgView);

						VBox itemBox = new VBox(10);
						Label item = new Label(result.getString(("item")));

						HBox detailBox = new HBox(30);
						Label quantity = new Label("Quantity: " + Integer.toString(result.getInt("quantity")));
						Label cost = new Label("Cost: ₦" + Integer.toString(result.getInt("cost")));
						detailBox.getChildren().addAll(quantity, cost);
						itemBox.getChildren().addAll(item, detailBox);
						itemBox.setAlignment(Pos.CENTER);

						int cartID = result.getInt("cartID");

						Button delete = new Button("Drop Item");
						delete.setOnAction(e -> {
							deleteFromCart(cartID);
							cartBox.getChildren().remove(orderBox);
							if (cartBox.getChildren().isEmpty()) {
								boxTop.getChildren().remove(0);
								boxTop.getChildren().add(0, categoryBox);
								lay.setCenter(scrl);
								lay.setBottom(null);
								cart.setDisable(true);
							}
						});
						delete.setVisible(false);
						delete.getStyleClass().add("delete");

						VBox delBox = new VBox(delete);
						delBox.setAlignment(Pos.CENTER);

						Region spacer = new Region();

//						This is a HBox, add the delete button to this layout pane
						orderBox.getChildren().addAll(itemBox, spacer, delBox);
						orderBox.setHgrow(spacer, Priority.ALWAYS);
						orderBox.hoverProperty().addListener((observable, oldValue, newValue) -> {
							if (newValue) {
								delete.setVisible(true);
							} else {
								delete.setVisible(false);
							}
						});
					}
				} catch (Exception f) {
					System.err.println("updateCartView Exception 1: " + f);
				}
				cartBox.getChildren().add(0, orderBox);
			}
		} catch (SQLException e) {
			System.err.println("updateCartView Exception 2: " + e);
		} catch (Exception e) {
			System.err.println("updateCartView Exception 3: " + e);
		}
	}

	private void deleteFromCart(int cartID) {
		PreparedStatement statement;
		try ( Connection connect = DriverManager.getConnection(jdbcUrl)) {
			String sql
					= "DELETE FROM cart WHERE cartID = ?";
			statement = connect.prepareStatement(sql);
			statement.setInt(1, cartID);
			statement.executeUpdate();
			DBManagement.alert("Success!", "Item has been dropped");
		} catch (SQLException e) {
			System.err.println("SQLException in deleteFromCart 1: " + e);
		} catch (Exception e) {
			System.err.println("Exception in deleteFromCart 2: " + e);
		}
	}

	protected void viewItem(String x, String i) {
		Button goBack;
		goBack = new Button("Back");
		goBack.getStyleClass().add("button-ui");
		goBack.setOnAction((ActionEvent e) -> {
			boxTop.getChildren().remove(0);
			boxTop.getChildren().add(0, categoryBox);
			lay.setCenter(scrl);
		});
		goBack.setCancelButton(true);
		boxTop.getChildren().remove(0);
		boxTop.getChildren().add(0, goBack);

		HBox view = new HBox(20);
		VBox boxLeft = new VBox(20);
		VBox boxRight = new VBox(30);
		TilePane picPane = new TilePane(Orientation.HORIZONTAL, 10, 10);
		Image img = new Image(
				"/Images/" + i);
		ImageView imgView = new ImageView(img);
		imgView.setFitWidth(300);
		imgView.setFitWidth(300);
		imgView.setPreserveRatio(true);
		boxLeft.getChildren().add(imgView);

		ObservableList<String> quantityList;

		int itemID = 0;

		ResultSet result;
		PreparedStatement statement;
		try ( Connection connect = DriverManager.getConnection(jdbcUrl)) {

			String sql
					= "SELECT items.itemID, image FROM items "
					+ "INNER JOIN images "
					+ "ON items.itemID = images.itemID "
					+ "WHERE item = ? "
					+ "ORDER BY imageID ASC;";
			statement = connect.prepareStatement(sql);
			statement.setString(1, x);
			result = statement.executeQuery();

			itemID = result.getInt("itemID");
			if (result.next()) {
				String image;
				do {
					image = result.getString("image");
					Image imgs = new Image(
							"/Images/" + image);
					ImageView imgsView = new ImageView(imgs);
					imgsView.setFitWidth(50);
					imgsView.setFitWidth(50);
					imgsView.setPreserveRatio(true);
					imgsView.setOnMouseClicked(e -> {
						imgView.setImage(imgs);
						imgView.setFitWidth(300);
						imgView.setFitWidth(300);
						imgView.setPreserveRatio(true);
					});

					picPane.getChildren().add(imgsView);

					result.next();
				} while (!result.isAfterLast());
			}
		} catch (SQLException e) {
			System.err.println("SQLException: " + e);
		} catch (Exception e) {
			System.err.println("Exception 3: " + e);
		}

		picPane.setAlignment(Pos.CENTER);
		if (!(picPane.getChildren().size() <= 1)) {
			boxLeft.getChildren().add(picPane);
		}
		boxLeft.setAlignment(Pos.CENTER);
		view.getChildren().add(boxLeft);

		String price = "";

		try ( Connection connect = DriverManager.getConnection(jdbcUrl)) {
			String sql
					= "SELECT price FROM items "
					+ "WHERE item = ?;";
			statement = connect.prepareStatement(sql);
			statement.setString(1, x);
			result = statement.executeQuery();

			if (result.next()) {
				price = Integer.toString(result.getInt("price"));
			}
		} catch (Exception e) {
			System.err.println("Exception 5: " + e);
		}

		Label itemLabel = new Label(x);
		itemLabel.getStyleClass().add("item-Label");

		Label priceLabel = new Label("₦" + price);
		priceLabel.getStyleClass().add("price-Label");

		quantityList = FXCollections.observableArrayList(
				"1",
				"2",
				"3",
				"4",
				"5",
				"6",
				"7",
				"8",
				"9",
				"10",
				"11",
				"12");

		TextField costField = new TextField();
		costField.setAlignment(Pos.CENTER);
		costField.setMaxWidth(150);
		costField.setEditable(false);
		costField.setFocusTraversable(false);
		costField.setPromptText("Total");

		quantityComboBox = new ComboBox<>(quantityList);
		quantityComboBox.setEditable(false);
		quantityComboBox.setPromptText("Quantity");
		quantityComboBox.setPrefSize(190, 35);
		quantityComboBox.setOnAction(e -> costField.setText(calCost(Integer.parseInt(priceLabel.getText().substring(1, priceLabel.getText().length())))));
		quantityComboBox.setVisibleRowCount(5);

		int it = itemID;

		Button add = new Button("Add to Cart");
		add.setOnAction(e -> {
			String cost = costField.getText();
			if ("".equals(cost)) {
				DBManagement.alert("Quantity Not Specified!", "Please specify the amount you are taking");
			} else {
				saveToCart(
						getUserID(),
						it,
						Integer.parseInt(quantityComboBox.getValue()),
						Double.parseDouble(costField.getText().substring(1, costField.getText().length())));
				updateCartView();
			}
		});
		add.setDefaultButton(true);
		add.getStyleClass().add("button-ui");

		boxRight.getChildren().addAll(itemLabel, priceLabel, quantityComboBox, costField, add);
		boxRight.setAlignment(Pos.CENTER);

		view.getChildren().add(boxRight);
		view.setAlignment(Pos.CENTER);
		lay.setCenter(view);
	}

	private String calCost(int itemPrice) {
		double itemCost;
		itemCost = itemPrice * Double.parseDouble(quantityComboBox.getValue());
		return "₦" + itemCost;

	}

	protected void loadPurchases() {
		ResultSet result;
		PreparedStatement statement;
		orderBox.getChildren().removeAll(orderBox.getChildren());
		try ( Connection connect = DriverManager.getConnection(jdbcUrl)) {
			String sql
					= "SELECT orderID, orders.itemID, item, quantity, cost, date FROM items "
					+ "INNER JOIN orders "
					+ "ON items.itemID = orders.itemID "
					+ "WHERE orders.userID = ? "
					+ "ORDER BY orderID DESC;";
			statement = connect.prepareStatement(sql);
			statement.setInt(1, getUserID());
			result = statement.executeQuery();

			ResultSet result2;
			PreparedStatement statement2;
			if (result.next()) {
				do {
					int itemID = result.getInt("itemID");
					HBox purchasedItem = new HBox(55);
					try ( Connection connect2 = DriverManager.getConnection(jdbcUrl)) {
						String sql2
								= "SELECT image FROM images "
								+ "WHERE itemID = ? "
								+ "LIMIT 1";
						statement2 = connect2.prepareStatement(sql2);
						statement2.setInt(1, itemID);
						result2 = statement2.executeQuery();

						if (result2.next()) {

							Image img = new Image(
									"/Images/" + result2.getString("image"));
							ImageView imgView = new ImageView(img);
							imgView.setFitWidth(80);
							imgView.setFitWidth(80);
							imgView.setPreserveRatio(true);

							purchasedItem.getChildren().add(imgView);

							VBox itemBox = new VBox(10);
							Label item = new Label(result.getString(("item")));

							HBox detailBox = new HBox(30);
							Label quantity = new Label("Quantity: " + Integer.toString(result.getInt("quantity")));
							Label cost = new Label("Cost: ₦" + Integer.toString(result.getInt("cost")));
							detailBox.getChildren().addAll(quantity, cost);
							itemBox.getChildren().addAll(item, detailBox);
							itemBox.setAlignment(Pos.CENTER);

							Label dateLabel = new Label("Date of Purchase:");
							Label dateID = new Label(result.getString("date"));

							VBox dateBox = new VBox(dateLabel, dateID);
							dateBox.setAlignment(Pos.CENTER);

							Region spacer = new Region();

							purchasedItem.getChildren().addAll(itemBox, spacer, dateBox);
							purchasedItem.setHgrow(spacer, Priority.ALWAYS);
							purchasedItem.setAlignment(Pos.CENTER);
						}
					} catch (Exception f) {
						System.err.println("loadPurchases Exception 1: " + f);
					}
					orderBox.getChildren().add(purchasedItem);
					result.next();
				} while (!result.isAfterLast());
			}
		} catch (SQLException ex) {
			System.err.println("loadPurchases Exception 2: " + ex);
		}
	}

	private void confirmPurchase() {
		ResultSet result;
		PreparedStatement statement;
		try ( Connection connect = DriverManager.getConnection(jdbcUrl)) {
			String sql
					= "SELECT * FROM cart "
					+ "WHERE userID = ?;";
			statement = connect.prepareStatement(sql);
			statement.setInt(1, getUserID());
			result = statement.executeQuery();

			if (result.next()) {
				PreparedStatement statement2;
				String sql2
						= "INSERT INTO orders("
						+ "userID, "
						+ "itemID, "
						+ "quantity, "
						+ "cost,"
						+ "date"
						+ ") VALUES("
						+ "?, "
						+ "?, "
						+ "?, "
						+ "?, "
						+ "datetime(strftime('%s','now'), 'unixepoch', 'localtime'));";

				statement2 = connect.prepareStatement(sql2);
				do {
					statement2.setInt(1, result.getInt("userID"));
					statement2.setInt(2, result.getInt("itemID"));
					statement2.setInt(3, result.getInt("quantity"));
					statement2.setDouble(4, result.getInt("cost"));
					statement2.executeUpdate();
					result.next();
				} while (!result.isAfterLast());

			}

			String sql2
					= "DELETE FROM cart WHERE userID = ?";
			statement = connect.prepareStatement(sql2);
			statement.setInt(1, getUserID());
			statement.executeUpdate();

			String sql3
					= "SELECT orderID, orders.itemID, item, quantity, cost, date FROM items "
					+ "INNER JOIN orders "
					+ "ON items.itemID = orders.itemID "
					+ "WHERE orders.userID = ? "
					+ "LIMIT ? OFFSET ?;";
			statement = connect.prepareStatement(sql3);
			statement.setInt(1, getUserID());
			statement.setInt(2, cartBox.getChildren().size());
			statement.setInt(3, orderBox.getChildren().size());

			cartBox.getChildren().clear();

			result = statement.executeQuery();

			ResultSet result2;
//			PreparedStatement statement2;
			if (result.next()) {
//				orderBox.getChildren().clear();
				do {
					int itemID = result.getInt("itemID");
					HBox purchasedItem = new HBox(55);

					String sql12
							= "SELECT image FROM images "
							+ "WHERE itemID = ? "
							+ "LIMIT 1";
					statement = connect.prepareStatement(sql12);
					statement.setInt(1, itemID);
					result2 = statement.executeQuery();

					if (result2.next()) {

						Image img = new Image(
								"/Images/" + result2.getString("image"));
						ImageView imgView = new ImageView(img);
						imgView.setFitWidth(80);
						imgView.setFitWidth(80);
						imgView.setPreserveRatio(true);

						purchasedItem.getChildren().add(imgView);

						VBox itemBox = new VBox(10);
						Label item = new Label(result.getString(("item")));

						HBox detailBox = new HBox(30);
						Label quantity = new Label("Quantity: " + Integer.toString(result.getInt("quantity")));
						Label cost = new Label("Cost: ₦" + Integer.toString(result.getInt("cost")));
						detailBox.getChildren().addAll(quantity, cost);
						itemBox.getChildren().addAll(item, detailBox);
						itemBox.setAlignment(Pos.CENTER);

						Label dateLabel = new Label("Date of Purchase:");
						Label dateID = new Label(result.getString("date"));

						VBox dateBox = new VBox(dateLabel, dateID);
						dateBox.setAlignment(Pos.CENTER);

						Region spacer = new Region();

						purchasedItem.getChildren().addAll(itemBox, spacer, dateBox);
						purchasedItem.setHgrow(spacer, Priority.ALWAYS);
						purchasedItem.setAlignment(Pos.CENTER);
					}
					orderBox.getChildren().add(0, purchasedItem);
					result.next();
				} while (!result.isAfterLast());
				DBManagement.alert("Success!", "Your purchase was successful");
				if (order.isDisabled()) {
					order.setDisable(false);
				}
			}
		} catch (Exception d) {
			System.err.println("confirmPurcahses Exception: " + d);
		}
	}

	protected void confirmation() {
		Stage stage = new Stage();

		BorderPane pane = new BorderPane();

		Label msg = new Label("Please confirm purchase");
		msg.setAlignment(Pos.CENTER);

		Button ok = new Button("Confirm");
		ok.setDefaultButton(true);
		ok.setOnAction(e -> {
			stage.close();
			confirmPurchase();
			viewOrder();
		});
		ok.setAlignment(Pos.CENTER);
		ok.getStyleClass().add("pos-Btn");

		Button no = new Button("Cancel");
		no.setDefaultButton(true);
		no.setOnAction(e -> stage.close());
		no.setAlignment(Pos.CENTER);
		no.getStyleClass().add("neg-Btn");

		HBox box = new HBox(20, ok, no);
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
		stage.setTitle("Confirmation!");
		stage.showAndWait();
	}
}
