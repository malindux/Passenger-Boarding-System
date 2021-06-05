import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Scanner;
import java.util.*;

public class TrainStation extends Application {
    static Scanner sc = new Scanner(System.in);

    static PassengerQueue trainQueue = new PassengerQueue();

    static Passenger passenger = new Passenger();

    private static List<Passenger> waitingRoomColomboToBadulla = new ArrayList<>(); //waiting room passenger list.
    private static List<Passenger> waitingRoomBadullaToColombo = new ArrayList<>();

    private static List<String> nicNumbersColomboToBadulla = new ArrayList<>();
    private static List<String> nicNumbersBadullaToColombo = new ArrayList<>();

    private static List<String> nicNumbersQ1ColomboToBadulla = new ArrayList<>();
    private static List<String> nicNumbersQ1BadullaToColombo = new ArrayList<>();
    private static List<String> nicNumbersQ2ColomboToBadulla = new ArrayList<>();
    private static List<String> nicNumbersQ2BadullaToColombo = new ArrayList<>();

    private static String[] seatNumberColomboToBadulla = new String[42];
    private static String[] seatNumberBadullaToColombo = new String[42];

    private static String[] seatNumberArrivedCB = new String[42];//this array will hold the seat numbers of boarded passengers.
    private static String[] seatNumberArrivedBC = new String[42];

    private static List<Integer> seatNumberTempCB = new ArrayList<>(); //this will contain passengers' seat numbers in the waiting room passengers temporary before boarding to the train.
    private static List<Integer> seatNumberTempBC = new ArrayList<>();

    private static List<Integer> seatNumberTempQ1CB = new ArrayList<>();//this will contain passengers' seat numbers in both queues' passengers temporary before boarding to the train.
    private static List<Integer> seatNumberTempQ1BC = new ArrayList<>();
    private static List<Integer> seatNumberTempQ2CB = new ArrayList<>();
    private static List<Integer> seatNumberTempQ2BC = new ArrayList<>();

    private static List<Integer> timeInTheQue1ColomboToBadulla = new ArrayList<>();//this list will contain time statistics which calculates in the boarding process for passengers.
    private static List<Integer> timeInTheQue1BadullaToColombo = new ArrayList<>();
    private static List<Integer> timeInTheQue2ColomboToBadulla = new ArrayList<>();
    private static List<Integer> timeInTheQue2BadullaToColombo = new ArrayList<>();

    //colombo to badulla queue statistics.
    static double maxWaitingTimeQ1ColomboToBadulla;
    static double minWaitingTimeQ1ColomboToBadulla;
    static double avgTimeQ1ColomboToBadulla;
    static int maxQueLengthQ1ColomboToBadulla;
    static double maxWaitingTimeQ2ColomboToBadulla;
    static double minWaitingTimeQ2ColomboToBadulla;
    static double avgTimeQ2ColomboToBadulla;
    static int maxQueLengthQ2ColomboToBadulla;

    //badulla to colombo queue statistics.
    static double maxWaitingTimeQ1BadullaToColombo;
    static double minWaitingTimeQ1BadullaToColombo;
    static double avgTimeQ1BadullaToColombo;
    static int maxQueLengthQ1BadullaToColombo;
    static double maxWaitingTimeQ2BadullaToColombo;
    static double minWaitingTimeQ2BadullaToColombo;
    static double avgTimeQ2BadullaToColombo;
    static int maxQueLengthQ2BadullaToColombo;

    static File fileColomboToBadulla = new File("src/dataColomboToBadulla.txt");
    static File fileBadullaToColombo = new File("src/dataBadullaToColombo.txt");

    static File queFileColomboToBadulla = new File("src/queDataColomboToBadulla.txt");
    static File queFileBadullaToColombo = new File("src/queDataBadullaToColombo.txt");

    static File reportDatafileCB = new File("src/reportDataCB.txt"); // files to store report data.
    static File reportDatafileBC = new File("src/reportDataBC.txt");

    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static LocalDate today = LocalDate.now();

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    static LocalDateTime dateAndTime = LocalDateTime.now();

    static Random randomNum = new Random();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        for (int i = 0; i < 42; i++) {
            seatNumberColomboToBadulla[i] = Integer.toString(i + 1);
        }

        for (int i = 0; i < 42; i++) {
            seatNumberBadullaToColombo[i] = Integer.toString(i + 1);
        }

        /*-----------------------------------GUI elements---------------------------------------*/
        Stage stage = new Stage();
        stage.setTitle("Select your destination");
        File background = new File("src/Background.jpg");
        Image img1 = new Image(background.toURI().toString());
        BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
        BorderPane bp = new BorderPane();
        bp.setBackground(new Background(new BackgroundImage(img1, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bSize)));
        Label lb1 = new Label("Please select your destination.");
        Button colomboToBadulla = new Button("Colombo - Badulla"); //Trip selection buttons.
        Button badullaToColombo = new Button("Badulla - Colombo");
        Button close = new Button("   Close   ");
        Button goToMenu = new Button("   Menu   ");
        Pane p2a = new Pane();
        Pane p3a = new Pane();
        lb1.setStyle("-fx-background-color: yellow");

        bp.setCenter(p3a);
        bp.setBottom(p2a);
        p3a.getChildren().add(lb1);
        p3a.getChildren().add(colomboToBadulla);
        p3a.getChildren().add(badullaToColombo);

        p2a.getChildren().add(close);
        p2a.getChildren().add(goToMenu);
        p2a.setPadding(new Insets(20));
        goToMenu.setLayoutX(205);
        close.setLayoutX(115);

        Scene scene = new Scene(bp, 400, 600);
        /*----------------------------------------------------------------------------------------*/

        main:
        while (true) {
            menu:
            while (true) {
                System.out.println("--------------------- W E L C O M E ----------------------");
                System.out.println("----------------------------------------------------------");
                System.out.println("========== Enter \"C\" to Check-In a passenger. ============");
                System.out.println();
                System.out.println("Enter \"A\" to add a passengers to the Train Queue.");
                System.out.println("Enter \"V\" to view Train Queue.");
                System.out.println("Enter \"R\" to Run the simulation and produce the report.");
                System.out.println("Enter \"D\" to to delete a passenger from train Queue.");
                System.out.println("Enter \"S\" to store program data.");
                System.out.println("Enter \"L\" to load program data.");
                System.out.println("Enter \"Q\" to quit.");
                System.out.println("----------------------------------------------------------");
                System.out.print("Please select a option from above : ");
                String input = sc.next();
                System.out.println("==========================================================");
                switch (input) {
                    case "C":
                    case "c":
                        Label name1 = new Label("Name : ");
                        name1.setStyle("-fx-background-color: yellow");
                        TextField name1tf = new TextField();
                        Label nic = new Label("NIC     : ");
                        nic.setStyle("-fx-background-color: yellow");
                        TextField nictf = new TextField();
                        Label seatNumber = new Label("Seat Number : ");
                        seatNumber.setStyle("-fx-background-color: yellow");
                        TextField seatNumbertf = new TextField();

                        stage.setTitle("Check-in");
                        Pane p3c = new Pane();
                        lb1.setStyle("-fx-background-color: yellow");

                        bp.setTop(p3c);

                        p3c.getChildren().add(name1);
                        p3c.getChildren().add(name1tf);
                        p3c.getChildren().add(nic);
                        p3c.getChildren().add(nictf);
                        p3c.getChildren().add(seatNumber);
                        p3c.getChildren().add(seatNumbertf);

                        lb1.setLayoutX(95);
                        lb1.setLayoutY(110);

                        name1.setLayoutY(40);
                        name1.setLayoutX(40);

                        name1tf.setLayoutX(150);
                        name1tf.setLayoutY(35);

                        nic.setLayoutY(100);
                        nic.setLayoutX(40);

                        nictf.setLayoutX(150);
                        nictf.setLayoutY(95);

                        seatNumber.setLayoutY(160);
                        seatNumber.setLayoutX(40);

                        seatNumbertf.setLayoutX(150);
                        seatNumbertf.setLayoutY(155);

                        colomboToBadulla.setLayoutX(127);
                        colomboToBadulla.setLayoutY(170);
                        badullaToColombo.setLayoutX(127);
                        badullaToColombo.setLayoutY(220);

                        colomboToBadulla.setOnAction(e -> {
                            if (name1tf.getText().equals("") || nictf.getText().equals("") || seatNumbertf.getText().equals("")) {
                                nictf.setStyle("-fx-border-color: red;" + "-fx-border-width: 2");
                                name1tf.setStyle("-fx-border-color: red;" + "-fx-border-width: 2");
                                seatNumbertf.setStyle("-fx-border-color: red;" + "-fx-border-width: 2");
                            } else {
                                nictf.setStyle("-fx-border-color: null;" + "-fx-border-width: 0");
                                name1tf.setStyle("-fx-border-color: null;" + "-fx-border-width: 0");
                                seatNumbertf.setStyle("-fx-border-color: null;" + "-fx-border-width: 0");

                                /*Validating entered seat number
                                 * Checking if it's not a integer or out of seating capacity*/
                                if (!seatNumbertf.getText().matches("[0-9]+") || Integer.parseInt(seatNumbertf.getText()) > 42) {
                                    Alert a2 = new Alert(Alert.AlertType.ERROR);
                                    a2.setContentText("Please enter a valid seat number.");
                                    seatNumbertf.setText("");
                                    a2.show();
                                }
                                /*Checking for reservations according to the giver customer details*/
                                else if (nictf.getText().length() != 10 && nictf.getText().length() != 12) {
                                    nictf.setText("");
                                    Alert a2 = new Alert(Alert.AlertType.ERROR);
                                    a2.setContentText("Please enter a valid NIC number.");
                                    a2.show();
                                } else {
                                    checkIn(name1tf.getText(), nictf.getText(), seatNumbertf.getText(), waitingRoomColomboToBadulla, nicNumbersColomboToBadulla, seatNumberArrivedCB, seatNumberTempCB, seatNumberTempQ1CB, seatNumberTempQ2CB, fileColomboToBadulla);
                                }
                            }
                        });
                        badullaToColombo.setOnAction(e -> {
                            if (name1tf.getText().equals("") || nictf.getText().equals("") || seatNumbertf.getText().equals("")) {
                                nictf.setStyle("-fx-border-color: red;" + "-fx-border-width: 2");
                                name1tf.setStyle("-fx-border-color: red;" + "-fx-border-width: 2");
                                seatNumbertf.setStyle("-fx-border-color: red;" + "-fx-border-width: 2");
                            } else {
                                nictf.setStyle("-fx-border-color: null;" + "-fx-border-width: 0");
                                name1tf.setStyle("-fx-border-color: null;" + "-fx-border-width: 0");
                                seatNumbertf.setStyle("-fx-border-color: null;" + "-fx-border-width: 0");

                                /*Validating entered seat number
                                 * Checking if it's not a integer or out of seating capacity*/
                                if (!seatNumbertf.getText().matches("[0-9]+") || Integer.parseInt(seatNumbertf.getText()) > 42) {
                                    Alert a2 = new Alert(Alert.AlertType.ERROR);
                                    a2.setContentText("Please enter a valid seat number.");
                                    seatNumbertf.setText("");
                                    a2.show();

                                    /*Checking for reservations according to the giver customer details*/
                                } else if (nictf.getText().length() != 10 && nictf.getText().length() != 12) {
                                    nictf.setText("");
                                    Alert a2 = new Alert(Alert.AlertType.ERROR);
                                    a2.setContentText("Please enter a valid NIC number.");
                                    a2.show();
                                } else {
                                    checkIn(name1tf.getText(), nictf.getText(), seatNumbertf.getText(), waitingRoomBadullaToColombo, nicNumbersBadullaToColombo, seatNumberArrivedBC, seatNumberTempBC, seatNumberTempQ1BC, seatNumberTempQ2BC, fileBadullaToColombo);
                                }
                            }
                        });

                        close.setOnAction(e -> {
                            stage.close();
                        });
                        goToMenu.setOnAction(e -> {
                            stage.close();
                        });
                        stage.setScene(scene);
                        stage.showAndWait();
                        break;
                    case "A":
                    case "a":
                        stage.setTitle("Select your destination");
                        bp.setTop(null);
                        lb1.setLayoutX(95);
                        lb1.setLayoutY(250);
                        colomboToBadulla.setLayoutX(127);
                        colomboToBadulla.setLayoutY(300);
                        badullaToColombo.setLayoutX(127);
                        badullaToColombo.setLayoutY(350);

                        colomboToBadulla.setOnAction(e -> {
                            Stage stage1 = new Stage();
                            stage1.setTitle("Waiting Room");
                            Label label1 = new Label("WAITING ROOM");
                            Button closeBtn = new Button("   Close   ");
                            Button goBack = new Button("    Back    ");
                            Button addCust = new Button("Add Passenger");

                            VBox fp0 = new VBox();
                            VBox fp1 = new VBox();
                            Pane p2 = new Pane();
                            fp0.setPrefHeight(550);
                            fp1.setPrefHeight(550);

                            GridPane gp = new GridPane();
                            ColumnConstraints col1 = new ColumnConstraints();
                            col1.setPercentWidth(50);
                            ColumnConstraints col2 = new ColumnConstraints();
                            col2.setPercentWidth(50);
                            gp.getColumnConstraints().addAll(col1, col2);

                            gp.add(label1,0,0);
                            GridPane.setColumnSpan(label1, 2);
                            gp.add(fp0, 0, 1);
                            gp.add(fp1, 1, 1);
                            gp.add(p2, 1, 2);

                            GridPane.setColumnSpan(p2, 2);

                            fp1.setPadding(new Insets(20, 0, 30, 35));
                            fp0.setPadding(new Insets(20, 0, 30, 35));

                            p2.setPadding(new Insets(10));

                            if (!waitingRoomColomboToBadulla.isEmpty()) {
                                if (waitingRoomColomboToBadulla.size() > 21) {
                                    for (int i = 0; i < 21; i++) {
                                        Label seat = new Label(String.format("%02d", seatNumberTempCB.get(i)) + " - " + waitingRoomColomboToBadulla.get(i).getName());
                                        fp0.getChildren().add(seat);
                                        seat.setStyle("-fx-font-weight: bold;" + "-fx-font-size: 15");
                                    }
                                    for (int j = 21; j < waitingRoomColomboToBadulla.size(); j++) {
                                        Label seat = new Label(String.format("%02d", seatNumberTempCB.get(j)) + " - " + waitingRoomColomboToBadulla.get(j).getName());
                                        fp1.getChildren().add(seat);
                                        seat.setStyle("-fx-font-weight: bold;" + "-fx-font-size: 15");
                                    }
                                } else {
                                    for (int i = 0; i < waitingRoomColomboToBadulla.size(); i++) {
                                        Label seat = new Label(String.format("%02d", seatNumberTempCB.get(i)) + " - " + waitingRoomColomboToBadulla.get(i).getName());
                                        fp0.getChildren().add(seat);
                                        seat.setStyle("-fx-font-weight: bold;" + "-fx-font-size: 15");
                                    }
                                }
                            }

                            p2.getChildren().add(closeBtn);
                            p2.getChildren().add(addCust);
                            p2.getChildren().add(goBack);
                            closeBtn.setLayoutX(-120);
                            goBack.setLayoutX(20);
                            closeBtn.setLayoutY(0);
                            goBack.setLayoutY(0);
                            addCust.setLayoutY(55);
                            addCust.setLayoutX(-73);

                            label1.setPadding(new Insets(20, 0, 20, 210));
                            label1.setStyle("-fx-font-size: 17;" + "-fx-font-weight: bold;");

                            closeBtn.setStyle("-fx-background-color: red;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
                            goBack.setStyle("-fx-background-color: limegreen;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
                            addCust.setStyle("-fx-background-color: orange;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");

                            closeBtn.addEventHandler(MouseEvent.MOUSE_EXITED, e1 -> {
                                closeBtn.setStyle("-fx-background-color: red;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
                            });
                            closeBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, e1 -> {
                                closeBtn.setStyle("-fx-background-color: tomato;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
                            });
                            goBack.addEventHandler(MouseEvent.MOUSE_EXITED, e1 -> {
                                goBack.setStyle("-fx-background-color: limegreen;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
                            });
                            goBack.addEventHandler(MouseEvent.MOUSE_ENTERED, e1 -> {
                                goBack.setStyle("-fx-background-color: lightgreen;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
                            });
                            addCust.addEventHandler(MouseEvent.MOUSE_EXITED, e1 -> {
                                addCust.setStyle("-fx-background-color: orange;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
                            });
                            addCust.addEventHandler(MouseEvent.MOUSE_ENTERED, e1 -> {
                                addCust.setStyle("-fx-background-color: #FF7F50;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
                            });

                            closeBtn.setOnAction(e1 -> {
                                stage1.close();
                            });

                            goBack.setOnAction(e1 -> {
                                stage1.close();
                            });

                            addCust.setOnAction(e1 -> {
                                stage1.close();
                                addPassenger(waitingRoomColomboToBadulla, trainQueue.passengerQue1ColomboToBadulla, trainQueue.passengerQue2ColomboToBadulla, nicNumbersColomboToBadulla, nicNumbersQ1ColomboToBadulla, nicNumbersQ2ColomboToBadulla, seatNumberTempCB, seatNumberTempQ1CB, seatNumberTempQ2CB);
                            });

                            Scene scene1 = new Scene(gp, 560, 750);
                            stage1.setScene(scene1);
                            stage1.showAndWait();
                        });
                        badullaToColombo.setOnAction(e -> {
                            Stage stage1 = new Stage();
                            stage1.setTitle("Waiting Room");
                            Label label1 = new Label("WAITING ROOM");
                            Button closeBtn = new Button("   Close   ");
                            Button goBack = new Button("    Back    ");
                            Button addCust = new Button("Add Passenger");

                            VBox fp0 = new VBox();
                            VBox fp1 = new VBox();
                            Pane p2 = new Pane();
                            fp0.setPrefHeight(550);
                            fp1.setPrefHeight(550);

                            GridPane gp = new GridPane();
                            ColumnConstraints col1 = new ColumnConstraints();
                            col1.setPercentWidth(50);
                            ColumnConstraints col2 = new ColumnConstraints();
                            col2.setPercentWidth(50);
                            gp.getColumnConstraints().addAll(col1, col2);

                            gp.add(label1,0,0);
                            GridPane.setColumnSpan(label1, 2);
                            gp.add(fp0, 0, 1);
                            gp.add(fp1, 1, 1);
                            gp.add(p2, 1, 2);
                            gp.setStyle("-fx-grid-lines-visible: true");
                            GridPane.setColumnSpan(p2, 2);

                            fp1.setPadding(new Insets(20, 0, 30, 35));
                            fp0.setPadding(new Insets(20, 0, 30, 35));

                            p2.setPadding(new Insets(10));

                            if (!waitingRoomBadullaToColombo.isEmpty()) {
                                if (waitingRoomBadullaToColombo.size() > 21) {
                                    for (int i = 0; i < 21; i++) {
                                        Label seat = new Label(String.format("%02d", seatNumberTempBC.get(i)) + " - " + waitingRoomBadullaToColombo.get(i).getName());
                                        fp0.getChildren().add(seat);
                                        seat.setStyle("-fx-font-weight: bold;" + "-fx-font-size: 15");
                                    }
                                    for (int j = 21; j < waitingRoomBadullaToColombo.size(); j++) {
                                        Label seat = new Label(String.format("%02d", seatNumberTempBC.get(j)) + " - " + waitingRoomBadullaToColombo.get(j).getName());
                                        fp1.getChildren().add(seat);
                                        seat.setStyle("-fx-font-weight: bold;" + "-fx-font-size: 15");
                                    }
                                } else {
                                    for (int i = 0; i < waitingRoomBadullaToColombo.size(); i++) {
                                        Label seat = new Label(String.format("%02d", seatNumberTempBC.get(i)) + " - " + waitingRoomBadullaToColombo.get(i).getName());
                                        fp0.getChildren().add(seat);
                                        seat.setStyle("-fx-font-weight: bold;" + "-fx-font-size: 15");
                                    }
                                }
                            }

                            p2.getChildren().add(closeBtn);
                            p2.getChildren().add(addCust);
                            p2.getChildren().add(goBack);
                            closeBtn.setLayoutX(-120);
                            goBack.setLayoutX(20);
                            closeBtn.setLayoutY(0);
                            goBack.setLayoutY(0);
                            addCust.setLayoutY(55);
                            addCust.setLayoutX(-73);

                            label1.setPadding(new Insets(20, 0, 20, 210));
                            label1.setStyle("-fx-font-size: 17;" + "-fx-font-weight: bold;");

                            closeBtn.setStyle("-fx-background-color: red;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
                            goBack.setStyle("-fx-background-color: limegreen;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
                            addCust.setStyle("-fx-background-color: orange;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");

                            closeBtn.addEventHandler(MouseEvent.MOUSE_EXITED, e1 -> {
                                closeBtn.setStyle("-fx-background-color: red;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
                            });
                            closeBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, e1 -> {
                                closeBtn.setStyle("-fx-background-color: tomato;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
                            });
                            goBack.addEventHandler(MouseEvent.MOUSE_EXITED, e1 -> {
                                goBack.setStyle("-fx-background-color: limegreen;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
                            });
                            goBack.addEventHandler(MouseEvent.MOUSE_ENTERED, e1 -> {
                                goBack.setStyle("-fx-background-color: lightgreen;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
                            });
                            addCust.addEventHandler(MouseEvent.MOUSE_EXITED, e1 -> {
                                addCust.setStyle("-fx-background-color: orange;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
                            });
                            addCust.addEventHandler(MouseEvent.MOUSE_ENTERED, e1 -> {
                                addCust.setStyle("-fx-background-color: #FF7F50;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
                            });

                            closeBtn.setOnAction(e1 -> {
                                stage1.close();
                            });

                            goBack.setOnAction(e1 -> {
                                stage1.close();
                            });

                            addCust.setOnAction(e1 -> {
                                stage1.close();
                                addPassenger(waitingRoomBadullaToColombo, trainQueue.passengerQue1BadullaToColombo, trainQueue.passengerQue2BadullaToColombo, nicNumbersBadullaToColombo, nicNumbersQ1BadullaToColombo, nicNumbersQ2BadullaToColombo, seatNumberTempBC, seatNumberTempQ1BC, seatNumberTempQ2BC);
                            });

                            Scene scene1 = new Scene(gp, 560, 750);
                            stage1.setScene(scene1);
                            stage1.showAndWait();

                        });

                        close.setOnAction(e -> {
                            stage.close();
                        });
                        goToMenu.setOnAction(e -> {
                            stage.close();
                        });

                        stage.setScene(scene);
                        stage.showAndWait();
                        break;
                    case "V":
                    case "v":
                        stage.setTitle("Select your destination");
                        bp.setTop(null);
                        lb1.setLayoutX(95);
                        lb1.setLayoutY(250);
                        colomboToBadulla.setLayoutX(127);
                        colomboToBadulla.setLayoutY(300);
                        badullaToColombo.setLayoutX(127);
                        badullaToColombo.setLayoutY(350);

                        colomboToBadulla.setOnAction(e -> {
                            viewTrainQue(waitingRoomColomboToBadulla, trainQueue.passengerQue1ColomboToBadulla, trainQueue.passengerQue2ColomboToBadulla, seatNumberTempCB, seatNumberTempQ1CB, seatNumberTempQ2CB, seatNumberArrivedCB, seatNumberColomboToBadulla);
                        });
                        badullaToColombo.setOnAction(e -> {
                            viewTrainQue(waitingRoomBadullaToColombo, trainQueue.passengerQue1BadullaToColombo, trainQueue.passengerQue2BadullaToColombo, seatNumberTempBC, seatNumberTempQ1BC, seatNumberTempQ2BC, seatNumberArrivedBC, seatNumberBadullaToColombo);
                        });

                        close.setOnAction(e -> {
                            stage.close();
                        });
                        goToMenu.setOnAction(e -> {
                            stage.close();
                        });

                        stage.setScene(scene);
                        stage.showAndWait();
                        break;
                    case "R":
                    case "r":
                        runSimulation();
                        break;
                    case "D":
                    case "d":
                        System.out.println("Please Select Your Destination.");
                        System.out.println("===============================");
                        System.out.println("1. Colombo To Badulla");
                        System.out.println("2. Badulla To Colombo");
                        while (!sc.hasNextInt()) {
                            System.out.println("Invalid Input. Please Select Your Destination (1/2) : ");
                            sc.next();
                        }
                        int input1 = sc.nextInt();
                        System.out.println("===============================");
                        if (input1 == 1) {
                            deletePassenger(trainQueue.passengerQue1ColomboToBadulla, trainQueue.passengerQue2ColomboToBadulla, nicNumbersQ1ColomboToBadulla, nicNumbersQ2ColomboToBadulla, seatNumberTempQ1CB, seatNumberTempQ2CB);
                        }
                        if (input1 == 2) {
                            deletePassenger(trainQueue.passengerQue1BadullaToColombo, trainQueue.passengerQue2BadullaToColombo, nicNumbersQ1BadullaToColombo, nicNumbersQ2BadullaToColombo, seatNumberTempQ1BC, seatNumberTempQ2BC);
                        }
                        break;
                    case "S":
                    case "s":
                        saveData(queFileColomboToBadulla, queFileBadullaToColombo);
                        break;
                    case "L":
                    case "l":
                        loadData(queFileColomboToBadulla, queFileBadullaToColombo);
                        break;
                    case "Q":
                    case "q":
                        System.out.println(" ");
                        while (true) {
                            System.out.println("Are you sure you want to exit ? (y/n)");
                            String userInput = sc.next();
                            /*Validating user input*/
                            if (userInput.contains("n")) {
                                break menu;
                            } else if (userInput.contains("y")) {
                                System.out.println("Thank You!");
                                break main;
                            } else System.out.println("Invalid Input!");
                        }
                    default:
                        System.out.println("Invalid Input !");
                }
            }
        }
    }

    private static void checkIn(String name, String nic, String seatNumber, List<Passenger> waitingRoom, List<String> nicNumbers, String[] seatNumberArrived, List<Integer> seatNumberTemp, List<Integer> seatNumberTempQ1, List<Integer> seatNumberTempQ2, File file) {
        BufferedReader reader;
        List<String[]> data = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String lineReader = reader.readLine();
            while (lineReader != null) { //Checking line by line if it is not null
                data.add(lineReader.split(":")); //Splitting a line which is not null by ':' and adding divided items to a temporary created array list
                // read next line
                lineReader = reader.readLine();
            }

            //boolean value to check if records contains entered data.
            boolean checkRecords = true;
            //checking if train queue or train board already contains the values that checks in check-in menu.
            if (seatNumberArrived[Integer.parseInt(seatNumber) - 1] == null && !seatNumberTempQ1.contains(Integer.parseInt(seatNumber)) && !seatNumberTempQ2.contains(Integer.parseInt(seatNumber))) {
                for (String[] j : data) {
                    //validating the data with records.
                    if (j[3].equals(dtf.format(today)) && j[1].equals(nic) && j[2].equals(seatNumber)) {
                        checkRecords = false;
                        //gathering the data for current date.
                        if (j[3].equals(dtf.format(today)) && !seatNumberTemp.contains(Integer.parseInt(j[2]))) {
                            Passenger passenger = new Passenger();
                            passenger.setName(name);
                            waitingRoom.add(passenger);
                            nicNumbers.add(j[1]);
                            seatNumberTemp.add(Integer.parseInt(j[2]));
                            Alert a2 = new Alert(Alert.AlertType.INFORMATION);
                            a2.setContentText("Passenger was successfully added.");
                            a2.show();
                        } else if (j[3].equals(dtf.format(today)) && seatNumberTemp.contains(Integer.parseInt(seatNumber))) {
                            Alert a2 = new Alert(Alert.AlertType.ERROR);
                            a2.setContentText("You have been already added to the waiting room.");
                            a2.show();
                        }
                    }
                }

                if (checkRecords == true) {
                    Alert a2 = new Alert(Alert.AlertType.ERROR);
                    a2.setContentText("Entered details cannot be found in system data records. Please check your details again.");
                    a2.show();
                }
            } else {
                Alert a2 = new Alert(Alert.AlertType.ERROR);
                a2.setContentText("Entered seat number is already occupied by a passenger. Please check your seat number.");
                a2.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*bubble sorting,
         * waiting room
         * temporary seat number list
         * nic number list*/
        int temp = 0;
        Passenger tempName;
        String tempNic;
        for (int i = 0; i < seatNumberTemp.size(); i++) {
            for (int j = i + 1; j < seatNumberTemp.size(); j++) {
                if (seatNumberTemp.get(j) < seatNumberTemp.get(i)) {
                    temp = seatNumberTemp.get(j);
                    seatNumberTemp.set(j, seatNumberTemp.get(i));
                    seatNumberTemp.set(i, temp);
                    tempName = waitingRoom.get(j);
                    waitingRoom.set(j, waitingRoom.get(i));
                    waitingRoom.set(i, tempName);
                    tempNic = nicNumbers.get(j);
                    nicNumbers.set(j, nicNumbers.get(i));
                    nicNumbers.set(i, tempNic);
                }
            }
        }
    }

    private static void addPassenger(List<Passenger> waitingRoom, List<Passenger> trainQue1, List<Passenger> trainQue2, List<String> nicNumbers, List<String> nicNumbersQ1, List<String> nicNumbersQ2, List<Integer> seatNumberTemp, List<Integer> seatNumberTempQ1, List<Integer> seatNumberTempQ2) {
        Stage stage = new Stage();
        stage.setTitle("Train Queue");

        Label label2 = new Label("TRAIN QUEUE 01");
        Label label3 = new Label("TRAIN QUEUE 02");
        Button close = new Button("   Close   ");
        Button goBack = new Button("    Back    ");

        BorderPane bp = new BorderPane();
        VBox fp = new VBox();
        VBox fp1 = new VBox();
        Pane p1 = new Pane();
        Pane p2 = new Pane();

        final Separator sepVert1 = new Separator();
        sepVert1.setOrientation(Orientation.VERTICAL);
        sepVert1.setValignment(VPos.CENTER);
        sepVert1.setPrefHeight(80);

        bp.setLeft(fp);
        bp.setCenter(sepVert1);
        bp.setRight(fp1);
        bp.setTop(p1);
        bp.setBottom(p2);

        fp.setPadding(new Insets(10, 10, 30, 150));
        fp.setSpacing(3);
        fp.setPrefWidth(450);
        fp1.setPadding(new Insets(10, 0, 30, 150));
        fp1.setSpacing(3);
        fp1.setPrefWidth(450);

        p1.setPadding(new Insets(30, 10, 0, 20));
        p2.setPadding(new Insets(20));

        //checking if both queues do not contains the maximum number of passengers - 42
        if (!trainQueue.isFull(trainQue1.size() + trainQue2.size())) {
            int roll = randomNum.nextInt(6) + 1;
            //checking the waiting room is not empty.
            if (!waitingRoom.isEmpty()) {
                /*if the generated random number is less than or equal to the size of waiting room, that amount of passengers will be taken from the waiting room,
                 * and each of those passengers will be added to the shortest queue.*/
                if (roll <= waitingRoom.size()) {
                    for (int i = 0; i < roll; i++) {
                        if (trainQue1.size() <= trainQue2.size()) {
                            trainQueue.add(trainQue1, waitingRoom.get(i));
                            nicNumbersQ1.add(nicNumbers.get(i));
                            seatNumberTempQ1.add(seatNumberTemp.get(i));
                        } else {
                            trainQueue.add(trainQue2, waitingRoom.get(i));
                            nicNumbersQ2.add(nicNumbers.get(i));
                            seatNumberTempQ2.add(seatNumberTemp.get(i));
                        }
                    }

                    for (int i = 0; i < roll; i++) {
                        nicNumbers.remove(0);
                        seatNumberTemp.remove(0);
                        waitingRoom.remove(0);
                    }

                    // else all the passengers in the waiting room will be taken and, each of those passengers will be added to the shortest queue.
                } else {
                    for (int i = 0; i < waitingRoom.size(); i++) {
                        if (trainQue1.size() <= trainQue2.size()) {
                            trainQueue.add(trainQue1, waitingRoom.get(i));
                            nicNumbersQ1.add(nicNumbers.get(i));
                            seatNumberTempQ1.add(seatNumberTemp.get(i));
                        } else {
                            trainQueue.add(trainQue2, waitingRoom.get(i));
                            nicNumbersQ2.add(nicNumbers.get(i));
                            seatNumberTempQ2.add(seatNumberTemp.get(i));
                        }
                    }
                    for (int i = 0; i < waitingRoom.size(); i++) {
                        nicNumbers.remove(0);
                        seatNumberTemp.remove(0);
                    }

                    waitingRoom.clear();
                }

                //displaying both queues.
                for (int i = 0; i < trainQue1.size(); i++) {
                    Label seat = new Label(String.format("%02d", seatNumberTempQ1.get(i)) + " - " + trainQue1.get(i).getName());
                    fp.getChildren().add(seat);
                    seat.setStyle("-fx-font-weight: bold;" + "-fx-font-size: 15");

                }
                for (int j = 1; j <= 21 - trainQue1.size(); j++) {
                    Label seat = new Label("Empty");
                    fp.getChildren().add(seat);
                    seat.setStyle("-fx-font-weight: bold;" + "-fx-font-size: 15");
                }
                for (int i = 0; i < trainQue2.size(); i++) {
                    Label seat = new Label(String.format("%02d", seatNumberTempQ2.get(i)) + " - " + trainQue2.get(i).getName());
                    fp1.getChildren().add(seat);
                    seat.setStyle("-fx-font-weight: bold;" + "-fx-font-size: 15");
                }
                for (int j = 1; j <= 21 - trainQue2.size(); j++) {
                    Label seat = new Label("Empty");
                    fp1.getChildren().add(seat);
                    seat.setStyle("-fx-font-weight: bold;" + "-fx-font-size: 15");
                }

                p1.getChildren().add(label2);
                p1.getChildren().add(label3);
                label2.setPadding(new Insets(40, 100, 20, 140));
                label2.setStyle("-fx-font-size: 17;" + "-fx-font-weight: bold;");
                label3.setPadding(new Insets(40, 100, 20, 595));
                label3.setStyle("-fx-font-size: 17;" + "-fx-font-weight: bold;");
                p2.getChildren().add(close);
                close.setLayoutX(340);
                goBack.setLayoutX(464);

                p2.getChildren().add(goBack);

                close.setStyle("-fx-background-color: red;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
                goBack.setStyle("-fx-background-color: limegreen;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");

                close.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
                    close.setStyle("-fx-background-color: red;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
                });
                close.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
                    close.setStyle("-fx-background-color: tomato;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
                });
                goBack.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
                    goBack.setStyle("-fx-background-color: limegreen;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
                });
                goBack.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
                    goBack.setStyle("-fx-background-color: lightgreen;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
                });


                close.setOnAction(e -> {
                    stage.close();
                });

                goBack.setOnAction(e -> {
                    stage.close();
                });

                Scene scene = new Scene(bp, 900, 865);
                stage.setScene(scene);
                stage.showAndWait();

            } else {
                Alert a2 = new Alert(Alert.AlertType.ERROR);
                a2.setContentText("Waiting Room is currently empty.");
                a2.show();
            }

        } else {
            Alert a2 = new Alert(Alert.AlertType.ERROR);
            a2.setContentText("Train Queue is full.");
            a2.show();
        }
    }

    private static void viewTrainQue(List<Passenger> waitingRoom, List<Passenger> trainQue1, List<Passenger> trainQue2, List<Integer> seatNumberTemp, List<Integer> seatNumberTempQ1, List<Integer> seatNumberTempQ2, String[] seatNumberArrived, String[] seatNumbers) {
        Stage stage = new Stage();
        stage.setTitle("Waiting Room - Train Queue - Boarded Passengers");

        Label label1 = new Label("WAITING ROOM");
        Label label2 = new Label("TRAIN QUEUE 01");
        Label label3 = new Label("TRAIN QUEUE 02");
        Label label4 = new Label("TRAIN BOARD");
        Button close = new Button("   Close   ");
        Button goBack = new Button("    Back    ");

        GridPane gp = new GridPane();
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(20);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(20);
        ColumnConstraints col4 = new ColumnConstraints();
        col4.setPercentWidth(30);
        gp.getColumnConstraints().addAll(col1, col2, col3, col4);
        gp.setStyle("-fx-grid-lines-visible: true");

        GridPane fp = new GridPane();
        VBox fp0 = new VBox();
        VBox fp1 = new VBox();
        VBox fp2 = new VBox();
        VBox fp3 = new VBox();
        FlowPane fp4 = new FlowPane();

        fp.add(fp0, 0, 0);
        fp.add(fp1, 1, 0);

        Pane p2 = new Pane();
        gp.add(label1, 0, 0);
        gp.add(label2, 1, 0);
        gp.add(label3, 2, 0);
        gp.add(label4, 3, 0);
        gp.add(fp, 0, 1);
        gp.add(fp2, 1, 1);
        gp.add(fp3, 2, 1);
        gp.add(fp4, 3, 1);
        gp.add(p2, 1, 2);


        fp1.setPadding(new Insets(20, 30, 30, 30));
        fp0.setPadding(new Insets(20, 30, 30, 30));

        fp2.setPadding(new Insets(20, 30, 30, 50));

        fp3.setPadding(new Insets(20, 30, 30, 50));

        fp4.setVgap(15);
        fp4.setHgap(15);
        fp4.setPadding(new Insets(20, 30, 30, 82));

        p2.setPadding(new Insets(20));

        if (!waitingRoom.isEmpty()) {
            /*Each queue can contain 21 passengers.*/
            if (waitingRoom.size() > 21) {
                for (int i = 0; i < 21; i++) {
                    Label seat = new Label(String.format("%02d", seatNumberTemp.get(i)) + " - " + waitingRoom.get(i).getName());
                    fp0.getChildren().add(seat);
                    seat.setStyle("-fx-font-weight: bold;" + "-fx-font-size: 15");
                }
                for (int j = 21; j < waitingRoom.size(); j++) {
                    Label seat = new Label(String.format("%02d", seatNumberTemp.get(j)) + " - " + waitingRoom.get(j).getName());
                    fp1.getChildren().add(seat);
                    seat.setStyle("-fx-font-weight: bold;" + "-fx-font-size: 15");
                }
            } else {
                for (int i = 0; i < waitingRoom.size(); i++) {
                    Label seat = new Label(String.format("%02d", seatNumberTemp.get(i)) + " - " + waitingRoom.get(i).getName());
                    fp0.getChildren().add(seat);
                    seat.setStyle("-fx-font-weight: bold;" + "-fx-font-size: 15");
                }
            }
        }
        for (int i = 0; i < trainQue1.size(); i++) {
            Label seat = new Label(String.format("%02d", seatNumberTempQ1.get(i)) + " - " + trainQue1.get(i).getName());
            fp2.getChildren().add(seat);
            seat.setStyle("-fx-font-weight: bold;" + "-fx-font-size: 15");

        }
        for (int j = 1; j <= 21 - trainQue1.size(); j++) {
            Label seat = new Label("Empty");
            fp2.getChildren().add(seat);
            seat.setStyle("-fx-font-weight: bold;" + "-fx-font-size: 15");
        }
        for (int i = 0; i < trainQue2.size(); i++) {
            Label seat = new Label(String.format("%02d", seatNumberTempQ2.get(i)) + " - " + trainQue2.get(i).getName());
            fp3.getChildren().add(seat);
            seat.setStyle("-fx-font-weight: bold;" + "-fx-font-size: 15");
        }
        for (int j = 1; j <= 21 - trainQue2.size(); j++) {
            Label seat = new Label("Empty");
            fp3.getChildren().add(seat);
            seat.setStyle("-fx-font-weight: bold;" + "-fx-font-size: 15");
        }
        for (int j = 1; j <= seatNumbers.length; j++) {
            if (seatNumbers[j - 1] != null && seatNumbers[j - 1].equals(seatNumberArrived[j - 1])) {
                Button seat = new Button(String.format("%02d", Integer.parseInt(seatNumbers[j - 1])) + " - Arrived");
                fp4.getChildren().add(seat);
                seat.setStyle("-fx-background-color: \t#98FB98;" + "-fx-pref-width:120;" + "-fx-pref-height: 10");
            } else if (seatNumbers[j - 1] != null && seatNumberArrived[j - 1] == null) {
                Button seat = new Button(String.format("%02d", Integer.parseInt(seatNumbers[j - 1])) + " - Empty");
                fp4.getChildren().add(seat);
                seat.setStyle("-fx-background-color: AQUAMARINE;" + "-fx-pref-width:120;" + "-fx-pref-height: 10");
            }
        }

        label1.setPadding(new Insets(20, 0, 20, 210));
        label1.setStyle("-fx-font-size: 17;" + "-fx-font-weight: bold;");
        label2.setPadding(new Insets(20, 0, 20, 120));
        label2.setStyle("-fx-font-size: 17;" + "-fx-font-weight: bold;");
        label3.setPadding(new Insets(20, 0, 20, 115));
        label3.setStyle("-fx-font-size: 17;" + "-fx-font-weight: bold;");
        label4.setPadding(new Insets(20, 0, 20, 215));
        label4.setStyle("-fx-font-size: 17;" + "-fx-font-weight: bold;");
        p2.getChildren().add(close);
        close.setLayoutX(248.5);
        goBack.setLayoutX(390);
        close.setLayoutY(50);
        goBack.setLayoutY(50);
        p2.getChildren().add(goBack);

        close.setStyle("-fx-background-color: red;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
        goBack.setStyle("-fx-background-color: limegreen;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");

        close.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            close.setStyle("-fx-background-color: red;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
        });
        close.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            close.setStyle("-fx-background-color: tomato;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
        });
        goBack.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            goBack.setStyle("-fx-background-color: limegreen;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
        });
        goBack.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            goBack.setStyle("-fx-background-color: lightgreen;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
        });

        close.setOnAction(e -> {
            stage.close();
        });

        goBack.setOnAction(e -> {
            stage.close();
        });

        Scene scene = new Scene(gp, 1843, 865);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private static void runSimulation() throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Select your destination");
        File background = new File("src/Background.jpg");
        Image img1 = new Image(background.toURI().toString());
        BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
        BorderPane bp = new BorderPane();
        bp.setBackground(new Background(new BackgroundImage(img1, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bSize)));
        Label lb1 = new Label("Please select your destination and date.");
        Button colomboToBadulla = new Button("Colombo - Badulla"); //Trip selection buttons.
        Button badullaToColombo = new Button("Badulla - Colombo");
        Button close = new Button("   Close   ");
        Button goToMenu = new Button("   Menu   ");
        Pane p2a = new Pane();
        Pane p3a = new Pane();
        lb1.setStyle("-fx-background-color: yellow");
        bp.setCenter(p3a);
        bp.setBottom(p2a);
        p3a.getChildren().add(lb1);
        p3a.getChildren().add(colomboToBadulla);
        p3a.getChildren().add(badullaToColombo);
        lb1.setLayoutX(65);
        lb1.setLayoutY(250);
        colomboToBadulla.setLayoutX(121);
        colomboToBadulla.setLayoutY(350);
        badullaToColombo.setLayoutX(121);
        badullaToColombo.setLayoutY(400);
        p2a.getChildren().add(close);
        p2a.getChildren().add(goToMenu);
        p2a.setPadding(new Insets(20));
        goToMenu.setLayoutX(200);
        close.setLayoutX(100);


        colomboToBadulla.setOnAction(e -> {
            //calculating the count of boarded passengers to check if the train is full.
            int count = 0;
            for (int i = 0; i < 42; i++) {
                if (seatNumberArrivedCB[i] != null) {
                    count = count + 1;
                }
            }
            if (count != 42) {
                //checking if the train queues are not empty.
                if (!trainQueue.isEmpty(trainQueue.passengerQue1ColomboToBadulla) || !trainQueue.isEmpty(trainQueue.passengerQue2ColomboToBadulla)) {
                    if (!trainQueue.isEmpty(trainQueue.passengerQue1ColomboToBadulla)) {

                        //this method will calculating queue statistics.
                        queStatistics(trainQueue.passengerQue1ColomboToBadulla, timeInTheQue1ColomboToBadulla, seatNumberTempQ1CB, seatNumberArrivedCB);

                        trainQueue.setMaxStayInQue(timeInTheQue1ColomboToBadulla.get(timeInTheQue1ColomboToBadulla.size() - 1));
                        maxWaitingTimeQ1ColomboToBadulla = trainQueue.getMaxStay();

                        minWaitingTimeQ1ColomboToBadulla = (double) timeInTheQue1ColomboToBadulla.get(0);
                        avgTimeQ1ColomboToBadulla = Math.round((maxWaitingTimeQ1ColomboToBadulla) / ((double) timeInTheQue1ColomboToBadulla.size()) * 100.0) / 100.0;

                        trainQueue.setMaxLength(timeInTheQue1ColomboToBadulla.size());
                        maxQueLengthQ1ColomboToBadulla = trainQueue.getMaxLength();
                    }
                    if (!trainQueue.isEmpty(trainQueue.passengerQue2ColomboToBadulla)) {

                        //this method will calculating queue statistics.
                        queStatistics(trainQueue.passengerQue2ColomboToBadulla, timeInTheQue2ColomboToBadulla, seatNumberTempQ2CB, seatNumberArrivedCB);

                        trainQueue.setMaxStayInQue(timeInTheQue2ColomboToBadulla.get(timeInTheQue2ColomboToBadulla.size() - 1));
                        maxWaitingTimeQ2ColomboToBadulla = trainQueue.getMaxStay();

                        minWaitingTimeQ2ColomboToBadulla = (double) timeInTheQue2ColomboToBadulla.get(0);
                        avgTimeQ2ColomboToBadulla = Math.round((maxWaitingTimeQ2ColomboToBadulla) / ((double) timeInTheQue2ColomboToBadulla.size()) * 100.0) / 100.0;

                        trainQueue.setMaxLength(timeInTheQue2ColomboToBadulla.size());
                        maxQueLengthQ2ColomboToBadulla = trainQueue.getMaxLength();
                    }

                    // displaying report in a gui.
                    displayReport(reportDatafileCB, trainQueue.passengerQue1ColomboToBadulla, trainQueue.passengerQue2ColomboToBadulla, nicNumbersQ1ColomboToBadulla, nicNumbersQ2ColomboToBadulla, timeInTheQue1ColomboToBadulla, timeInTheQue2ColomboToBadulla, seatNumberTempQ1CB,seatNumberTempQ2CB, maxWaitingTimeQ1ColomboToBadulla, minWaitingTimeQ1ColomboToBadulla, avgTimeQ1ColomboToBadulla, maxQueLengthQ1ColomboToBadulla, maxWaitingTimeQ2ColomboToBadulla, minWaitingTimeQ2ColomboToBadulla, avgTimeQ2ColomboToBadulla, maxQueLengthQ2ColomboToBadulla);

                } else {
                    Alert a2 = new Alert(Alert.AlertType.ERROR);
                    a2.setContentText("Train Queue is currently empty.");
                    a2.show();
                }

                if (!seatNumberTempQ1CB.isEmpty()) {
                    for (int g = 0; g < timeInTheQue1ColomboToBadulla.size(); g++) {
                        trainQueue.remove(trainQueue.passengerQue1ColomboToBadulla, 0);
                        nicNumbersQ1ColomboToBadulla.remove(0);
                        seatNumberTempQ1CB.remove(0);
                    }
                }
                if (!seatNumberTempQ2CB.isEmpty()) {
                    for (int g = 0; g < timeInTheQue2ColomboToBadulla.size(); g++) {
                        trainQueue.remove(trainQueue.passengerQue2ColomboToBadulla, 0);
                        nicNumbersQ2ColomboToBadulla.remove(0);
                        seatNumberTempQ2CB.remove(0);
                    }
                }

            } else {
                Alert a2 = new Alert(Alert.AlertType.INFORMATION);
                a2.setContentText("Train is full. Please wait until next turn.");
                a2.show();
            }
        });
        badullaToColombo.setOnAction(e -> {
            //calculating the count of boarded passengers to check if the train is full.
            int count = 0;
            for (int i = 0; i < 42; i++) {
                if (seatNumberArrivedBC[i] != null) {
                    count = count + 1;
                }
            }
            if (count != 42) {
                if (!trainQueue.isEmpty(trainQueue.passengerQue1BadullaToColombo) || !trainQueue.isEmpty(trainQueue.passengerQue2BadullaToColombo)) {
                    if (!trainQueue.isEmpty(trainQueue.passengerQue1BadullaToColombo)) {

                        //this method will calculating queue statistics.
                        queStatistics(trainQueue.passengerQue1BadullaToColombo, timeInTheQue1BadullaToColombo, seatNumberTempQ1BC, seatNumberArrivedBC);

                        trainQueue.setMaxStayInQue(timeInTheQue1BadullaToColombo.get(timeInTheQue1BadullaToColombo.size() - 1));
                        maxWaitingTimeQ1BadullaToColombo = trainQueue.getMaxStay();

                        maxWaitingTimeQ1BadullaToColombo = (double) timeInTheQue1BadullaToColombo.get(0);
                        avgTimeQ1BadullaToColombo = Math.round((maxWaitingTimeQ1BadullaToColombo) / ((double) timeInTheQue1BadullaToColombo.size()) * 100.0) / 100.0;

                        trainQueue.setMaxLength(timeInTheQue1BadullaToColombo.size());
                        maxQueLengthQ1BadullaToColombo = trainQueue.getMaxLength();
                    }
                    if (!trainQueue.isEmpty(trainQueue.passengerQue2BadullaToColombo)) {

                        //this method will calculating queue statistics.
                        queStatistics(trainQueue.passengerQue2BadullaToColombo, timeInTheQue2BadullaToColombo, seatNumberTempQ2BC, seatNumberArrivedBC);

                        trainQueue.setMaxStayInQue(timeInTheQue2BadullaToColombo.get(timeInTheQue2BadullaToColombo.size() - 1));
                        maxWaitingTimeQ2BadullaToColombo = trainQueue.getMaxStay();

                        minWaitingTimeQ2BadullaToColombo = (double) timeInTheQue2BadullaToColombo.get(0);
                        avgTimeQ2BadullaToColombo = Math.round((maxWaitingTimeQ2BadullaToColombo) / ((double) timeInTheQue2BadullaToColombo.size()) * 100.0) / 100.0;

                        trainQueue.setMaxLength(timeInTheQue2BadullaToColombo.size());
                        maxQueLengthQ2BadullaToColombo = trainQueue.getMaxLength();
                    }

                    // displaying report in a gui.
                    displayReport(reportDatafileBC, trainQueue.passengerQue1BadullaToColombo, trainQueue.passengerQue2BadullaToColombo, nicNumbersQ1BadullaToColombo, nicNumbersQ2BadullaToColombo, timeInTheQue1BadullaToColombo, timeInTheQue2BadullaToColombo,seatNumberTempQ1BC,seatNumberTempQ2BC, maxWaitingTimeQ1BadullaToColombo, minWaitingTimeQ1BadullaToColombo, avgTimeQ1BadullaToColombo, maxQueLengthQ1BadullaToColombo, maxWaitingTimeQ2BadullaToColombo, minWaitingTimeQ2BadullaToColombo, avgTimeQ2BadullaToColombo, maxQueLengthQ2BadullaToColombo);

                } else {
                    Alert a2 = new Alert(Alert.AlertType.ERROR);
                    a2.setContentText("Train Queue is currently empty.");
                    a2.show();
                }

                //removing passengers from train queues after adding passengers to the train.
                if (!seatNumberTempQ1BC.isEmpty()) {
                    for (int g = 0; g < timeInTheQue1BadullaToColombo.size(); g++) {
                        trainQueue.remove(trainQueue.passengerQue1BadullaToColombo, 0);
                        nicNumbersQ1BadullaToColombo.remove(0);
                        seatNumberTempQ1BC.remove(0);
                    }
                }
                if (!seatNumberTempQ2BC.isEmpty()) {
                    for (int g = 0; g < timeInTheQue2BadullaToColombo.size(); g++) {
                        trainQueue.remove(trainQueue.passengerQue2BadullaToColombo, 0);
                        nicNumbersQ2BadullaToColombo.remove(0);
                        seatNumberTempQ2BC.remove(0);
                    }
                }

            } else {
                Alert a2 = new Alert(Alert.AlertType.INFORMATION);
                a2.setContentText("Train is full. Please wait until next turn.");
                a2.show();
            }
        });

        close.setOnAction(e -> {
            stage.close();
        });
        goToMenu.setOnAction(e -> {
            stage.close();
        });
        Scene scene = new Scene(bp, 400, 600);
        stage.setScene(scene);
        stage.showAndWait();

    }

    //method to display the report.
    private static void displayReport(File file1, List<Passenger> trainQue1, List<Passenger> trainQue2, List<String> nicNumbersQ1, List<String> nicNumbersQ2, List<Integer> timeInTheQue1, List<Integer> timeInTheQue2, List<Integer> seatNumberTempQ1,List<Integer> seatNumberTempQ2, double maxStayQ1, double minStayQ1, double avgTimeQ1, int maxLengthQ1, double maxStayQ2, double minStayQ2, double avgTimeQ2, int maxLengthQ2) {
        FileWriter fr1 = null;
        BufferedWriter br1 = null;

        try {
            fr1 = new FileWriter(file1, true);
            br1 = new BufferedWriter(fr1);

            Stage stage = new Stage();
            stage.setTitle("Report");

            Label label = new Label("R E P O R T");
            Label lb1 = new Label("Queue 01");
            Label lb2 = new Label("Queue 02");
            Label lb3 = new Label("Passenger Details");
            Label lb4 = new Label("Queue Statistics");
            Label lb5 = new Label("Passenger Details");
            Label lb6 = new Label("Queue Statistics");
            Button close = new Button("   Close   ");
            Button goBack = new Button("    Back    ");

            GridPane gp = new GridPane();

            VBox vb3 = new VBox();
            VBox vb4 = new VBox();

            Pane p1 = new Pane();
            Pane p2 = new Pane();

            VBox vb1 = new VBox();
            VBox vb2 = new VBox();

            ColumnConstraints col1 = new ColumnConstraints();
            col1.setPercentWidth(25);
            ColumnConstraints col2 = new ColumnConstraints();
            col2.setPercentWidth(25);
            ColumnConstraints col3 = new ColumnConstraints();
            col3.setPercentWidth(25);
            ColumnConstraints col4 = new ColumnConstraints();
            col3.setPercentWidth(25);
            gp.getColumnConstraints().addAll(col1, col2, col3, col4);

            gp.add(vb1, 0, 3);
            gp.add(vb3, 1, 3);
            gp.add(vb2, 2, 3);
            gp.add(vb4, 3, 3);

            gp.add(lb1, 0, 1);
            GridPane.setColumnSpan(lb1, 3);
            gp.add(lb2, 1, 1);
            GridPane.setColumnSpan(lb2, 3);

            gp.add(lb3, 0, 2);
            gp.add(lb4, 1, 2);
            gp.add(lb5, 2, 2);
            gp.add(lb6, 3, 2);

            gp.add(p2, 1, 4);
            GridPane.setColumnSpan(p2, 5);

            gp.add(p1, 0, 0);
            GridPane.setColumnSpan(p1, 5);

            vb1.setPadding(new Insets(30, 0, 30, 55));
            vb2.setPadding(new Insets(30, 0, 30, 55));
            vb3.setPadding(new Insets(30, 0, 30, 80));
            vb4.setPadding(new Insets(30, 0, 30, 80));

            p1.setPadding(new Insets(30, 10, 0, 20));
            p2.setPadding(new Insets(20));

            //writes data to the report file.
            p1.getChildren().add(label);

            br1.write("-----------"+formatter.format(dateAndTime)+"-----------");

            br1.newLine();
            br1.write("---- TRAIN QUEUE 01 ----");
            br1.newLine();
            br1.write("  - Passenger Details -");
            br1.newLine();
            for (int i = 0; i < timeInTheQue1.size(); i++) {
                Label passengerDetails = new Label(seatNumberTempQ1.get(i)+" - "+trainQue1.get(i).getName() + " - " + nicNumbersQ1.get(i));
                vb1.getChildren().add(passengerDetails);
                br1.write("Name : " + trainQue1.get(i).getName() + "  |  NIC Number : " + nicNumbersQ1.get(i)+ "  |  Seat Number : " + seatNumberTempQ1.get(i));
                br1.newLine();
            }
            Label queStatisticsQ1 = new Label("Maximum Waiting Time - " + maxStayQ1 + "s \nMinimum Waiting Time - " + minStayQ1 + "s \nAverage Waiting Time - " + avgTimeQ1 + "s \nMaximum Queue Length - " + maxLengthQ1);
            vb3.getChildren().add(queStatisticsQ1);
            br1.newLine();
            br1.write(" - Queue Statistics - ");
            br1.newLine();
            br1.write("Maximum Waiting Time - " + maxStayQ1);
            br1.newLine();
            br1.write("Minimum Waiting Time - " + minStayQ1);
            br1.newLine();
            br1.write("Average Waiting Time - " + avgTimeQ1);
            br1.newLine();
            br1.write("Maximum Queue Length - " + maxLengthQ1);
            br1.newLine();
            br1.write("--------------------------------------");
            br1.newLine();

            br1.write("---- TRAIN QUEUE 02 ----");
            br1.newLine();
            br1.write("  - Passenger Details -");
            br1.newLine();
            for (int i = 0; i < timeInTheQue2.size(); i++) {
                Label passengerDetails = new Label(seatNumberTempQ2.get(i)+" - "+trainQue2.get(i).getName() + " - " + nicNumbersQ2.get(i));
                vb2.getChildren().add(passengerDetails);
                br1.write("Name : " + trainQue2.get(i).getName() + "  |  NIC Number : " + nicNumbersQ2.get(i) + "  |  Seat Number : " + seatNumberTempQ2.get(i));
                br1.newLine();
            }
            Label queStatisticsQ2 = new Label("Maximum Waiting Time - " + maxStayQ2 + "s \nMinimum Waiting Time - " + minStayQ2 + "s \nAverage Waiting Time - " + avgTimeQ2 + "s \nMaximum Queue Length - " + maxLengthQ2);
            vb4.getChildren().add(queStatisticsQ2);
            br1.newLine();
            br1.write(" - Queue Statistics - ");
            br1.newLine();
            br1.write("Maximum Waiting Time - " + maxStayQ2);
            br1.newLine();
            br1.write("Minimum Waiting Time - " + minStayQ2);
            br1.newLine();
            br1.write("Average Waiting Time - " + avgTimeQ2);
            br1.newLine();
            br1.write("Maximum Queue Length - " + maxLengthQ2);
            br1.newLine();
            br1.write("--------------------------------------");
            br1.newLine();
            br1.newLine();

            label.setPadding(new Insets(30, 100, 10, 646));
            label.setStyle("-fx-font-size: 23;" + "-fx-font-weight: bold;");
            lb1.setPadding(new Insets(20, 0, 20, 280));
            lb1.setStyle("-fx-font-size: 18;" + "-fx-font-weight: bold;");
            lb2.setPadding(new Insets(20, 0, 20, 630));
            lb2.setStyle("-fx-font-size: 18;" + "-fx-font-weight: bold;");

            lb3.setPadding(new Insets(20, 0, 20, 55));
            lb3.setStyle("-fx-font-size: 16;" + "-fx-font-weight: bold;");
            lb4.setPadding(new Insets(20, 0, 20, 80));
            lb4.setStyle("-fx-font-size: 16;" + "-fx-font-weight: bold;");
            lb5.setPadding(new Insets(20, 0, 20, 55));
            lb5.setStyle("-fx-font-size: 16;" + "-fx-font-weight: bold;");
            lb6.setPadding(new Insets(20, 0, 20, 80));
            lb6.setStyle("-fx-font-size: 16;" + "-fx-font-weight: bold;");

            p2.getChildren().add(close);
            p2.getChildren().add(goBack);
            close.setLayoutX(220);
            goBack.setLayoutX(367);
            close.setLayoutY(70);
            goBack.setLayoutY(70);


            close.setStyle("-fx-background-color: red;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
            goBack.setStyle("-fx-background-color: limegreen;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");

            close.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
                close.setStyle("-fx-background-color: red;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
            });
            close.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
                close.setStyle("-fx-background-color: tomato;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
            });
            goBack.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
                goBack.setStyle("-fx-background-color: limegreen;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
            });
            goBack.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
                goBack.setStyle("-fx-background-color: lightgreen;"+"-fx-text-fill: white;"+"-fx-font-weight: bold;"+"-fx-font-size: 18;");
            });

            close.setOnAction(e -> {
                stage.close();
            });
            goBack.setOnAction(e -> {
                stage.close();
            });

            Scene scene = new Scene(gp, 1450, 900);
            stage.setScene(scene);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br1.close();
                fr1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to calculate the queue statistics.
    private static void queStatistics(List<Passenger> trainQue, List<Integer> timeInTheQue, List<Integer> seatNumberTemp, String[] seatNumberArrived) {
        int time = 0;
        for (int i = 0; i < trainQue.size(); i++) {
            int roll1 = randomNum.nextInt(6) + 1;
            int roll2 = randomNum.nextInt(6) + 1;
            int roll3 = randomNum.nextInt(6) + 1;
            int sumOfDices = roll1 + roll2 + roll3;
            time = time + sumOfDices;
            passenger.setSecondsInQueue(time);
            timeInTheQue.add(passenger.getSeconds());
            seatNumberArrived[seatNumberTemp.get(i) - 1] = Integer.toString(seatNumberTemp.get(i));
        }
    }

    private static void deletePassenger(List<Passenger> trainQue1, List<Passenger> trainQue2, List<String> nicNumbersQ1, List<String> nicNumbersQ2, List<Integer> seatNumberTempQ1, List<Integer> seatNumberTempQ2) {
        System.out.println(nicNumbersQ2);
        System.out.println(seatNumberTempQ2);
        System.out.println(trainQue2.get(0).getName());
        main:
        while (true) {
            if (!trainQue1.isEmpty() || !trainQue2.isEmpty()) {
                System.out.println("=========================================================================================");
                System.out.print("NIC Number : ");
                String nicNumber = sc.next();
                System.out.print("Seat Number : ");
                while (!sc.hasNextInt()) {
                    System.out.print("Please enter a valid seat number : ");
                    sc.next();
                }
                int seatNumber = sc.nextInt();
                sc.nextLine();
                System.out.print("Please Enter Your Name (Please enter exact same name which you used to book the seat.) : ");
                String passengerName = sc.nextLine();
                System.out.println("=========================================================================================");

                if (nicNumbersQ1.contains(nicNumber) && seatNumberTempQ1.contains(seatNumber)) {
                    if (trainQue1.get(seatNumberTempQ1.indexOf(seatNumber)).getName().equals(passengerName)) {
                        System.out.println("Passenger removed successfully.");
                        System.out.println("-------------------------------");
                        System.out.println("Passenger Name : " + passengerName);
                        System.out.println("NIC Number : " + nicNumber);
                        System.out.println("Seat Number : " + seatNumber);
                        System.out.println();
                        trainQue1.remove(seatNumberTempQ1.indexOf(seatNumber));
                        nicNumbersQ1.remove(nicNumber);
                        seatNumberTempQ1.remove(Integer.valueOf(seatNumber));
                    }else {
                        System.out.println("* Provided details cannot be found in the train queues.\n* Please re-enter your details correctly.");
                        System.out.println();}
                } else if (nicNumbersQ2.contains(nicNumber) && seatNumberTempQ2.contains(seatNumber)) {
                    if (trainQue2.get(seatNumberTempQ2.indexOf(seatNumber)).getName().equals(passengerName)) {
                        System.out.println("Passenger removed successfully.");
                        System.out.println("-------------------------------");
                        System.out.println("Passenger Name : " + passengerName);
                        System.out.println("NIC Number : " + nicNumber);
                        System.out.println("Seat Number : " + seatNumber);
                        System.out.println();
                        trainQue2.remove(seatNumberTempQ2.indexOf(seatNumber));
                        nicNumbersQ2.remove(nicNumber);
                        seatNumberTempQ2.remove(Integer.valueOf(seatNumber));
                    }else {
                        System.out.println("* Provided details cannot be found in the train queues.\n* Please re-enter your details correctly.");
                        System.out.println();}
                } else {
                    System.out.println("* Provided details cannot be found in the train queues.\n* Please re-enter your details correctly.");
                    System.out.println();
                }
                while (true) {
                    System.out.println("Do you want to remove more passengers (y/n) ?");
                    String input = sc.next();
                    if (input.contains("y")) {
                        continue main;
                    } else if (input.contains("n")) {
                        System.out.println("Thank You!");
                        break main;
                    } else {
                        System.out.println("Invaid Input!");
                        continue;
                    }
                }
            } else System.out.println("Train Queue is currently empty.");
            break;
        }
    }

    private void saveData(File file1, File file2) {
        FileWriter fr1 = null;
        BufferedWriter br1 = null;// Use to write data to separate lines
        FileWriter fr2 = null;
        BufferedWriter br2 = null;// Use to write data to separate lines
        try {
            fr1 = new FileWriter(file1, true);
            br1 = new BufferedWriter(fr1);
            fr2 = new FileWriter(file2, true);
            br2 = new BufferedWriter(fr2);

            for (int i = 0; i < trainQueue.passengerQue1ColomboToBadulla.size(); i++) {
                br1.write(trainQueue.passengerQue1ColomboToBadulla.get(i).getName() + ":" + nicNumbersQ1ColomboToBadulla.get(i) + ":" + seatNumberTempQ1CB.get(i) + ":" + "1" + ":" + "1" + ":" + dtf.format(today));
                br1.newLine(); //Use to write data to separate lines
            }
            for (int i = 0; i < trainQueue.passengerQue2ColomboToBadulla.size(); i++) {
                br1.write(trainQueue.passengerQue2ColomboToBadulla.get(i).getName() + ":" + nicNumbersQ2ColomboToBadulla.get(i) + ":" + seatNumberTempQ2CB.get(i) + ":" + "1" + ":" + "2" + ":" + dtf.format(today));
                br1.newLine(); //Use to write data to separate lines
            }
            for (int i = 0; i < trainQueue.passengerQue1BadullaToColombo.size(); i++) {
                br2.write(trainQueue.passengerQue1BadullaToColombo.get(i).getName() + ":" + nicNumbersQ1BadullaToColombo.get(i) + ":" + seatNumberTempQ1BC.get(i) + ":" + "2" + ":" + "1" + ":" + dtf.format(today));
                br2.newLine(); //Use to write data to separate lines
            }
            for (int i = 0; i < trainQueue.passengerQue2BadullaToColombo.size(); i++) {
                br2.write(trainQueue.passengerQue2BadullaToColombo.get(i).getName() + ":" + nicNumbersQ2BadullaToColombo.get(i) + ":" + seatNumberTempQ2BC.get(i) + ":" + "2" + ":" + "2" + ":" + dtf.format(today));
                br2.newLine(); //Use to write data to separate lines
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br1.close();
                br2.close();
                fr1.close();
                fr2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("All data have been successfully saved.");
        }
    }

    private void loadData(File file1, File file2){
        /*Readers for read both data files
         * file1 - Colombo to Badulla queue data file
         * file2 - Badulla to Colombo queue data file*/
        BufferedReader reader1;
        BufferedReader reader2;
        /*Array lists for store items after splitting line by line
         * datas1 - for Colombo to Badulla data
         * datas2 - for Badulla to Colombo data*/
        List<String[]> datas1 = new ArrayList<>();
        List<String[]> datas2 = new ArrayList<>();
        try {
            reader1 = new BufferedReader(new FileReader(file1));
            reader2 = new BufferedReader(new FileReader(file2));
            String lineReader1 = reader1.readLine();
            String lineReader2 = reader2.readLine();

            while (lineReader1 != null) { //Checking line by line if it is not null
                datas1.add(lineReader1.split(":")); //Splitting a line which is not null by ':' and adding divided items to a temporary created array list
                // read next line
                lineReader1 = reader1.readLine();
            }
            while (lineReader2 != null) { //Checking line by line if it is not null
                datas2.add(lineReader2.split(":")); //Splitting a line which is not null by ':' and adding divided items to a temporary created array list
                // read next line
                lineReader2 = reader2.readLine();
            }
            for (String[] j : datas1) {
                if (j[5].equals(dtf.format(today))) {
                    //checking if loading data already contains in the train queues and train board.
                    if (j[3].equals("1") && j[4].equals("1") && !seatNumberTempQ1CB.contains(Integer.parseInt(j[2])) && seatNumberArrivedCB[Integer.parseInt(j[2]) - 1] == null) {
                        Passenger passenger = new Passenger();
                        passenger.setName(j[0]);
                        trainQueue.passengerQue1ColomboToBadulla.add(passenger);
                        nicNumbersQ1ColomboToBadulla.add(j[1]);
                        seatNumberTempQ1CB.add(Integer.parseInt(j[2]));
                    } else if (j[3].equals("1") && j[4].equals("2") && !seatNumberTempQ2CB.contains(Integer.parseInt(j[2])) && seatNumberArrivedCB[Integer.parseInt(j[2]) - 1] == null) {
                        Passenger passenger = new Passenger();
                        passenger.setName(j[0]);
                        trainQueue.passengerQue2ColomboToBadulla.add(passenger);
                        nicNumbersQ2ColomboToBadulla.add(j[1]);
                        seatNumberTempQ2CB.add(Integer.parseInt(j[2]));
                    }
                }
            }

            for (String[] j : datas2) {
                if (j[5].equals(dtf.format(today))) {
                    //checking if loading data already contains in the train queues and train board.
                    if (j[3].equals("2") && j[4].equals("1") && !seatNumberTempQ1BC.contains(Integer.parseInt(j[2])) && seatNumberArrivedBC[Integer.parseInt(j[2]) - 1] == null) {
                        Passenger passenger = new Passenger();
                        passenger.setName(j[0]);
                        trainQueue.passengerQue1BadullaToColombo.add(passenger);
                        nicNumbersQ1BadullaToColombo.add(j[1]);
                        seatNumberTempQ1BC.add(Integer.parseInt(j[2]));
                    } else if (j[3].equals("2") && j[4].equals("2") && !seatNumberTempQ2BC.contains(Integer.parseInt(j[2])) && seatNumberArrivedBC[Integer.parseInt(j[2]) - 1] == null) {
                        Passenger passenger = new Passenger();
                        passenger.setName(j[0]);
                        trainQueue.passengerQue2BadullaToColombo.add(passenger);
                        nicNumbersQ2BadullaToColombo.add(j[1]);
                        seatNumberTempQ2BC.add(Integer.parseInt(j[2]));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("All data have been loaded to the system.");
    }
}
