import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.*;

import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class TrainTicketBooking extends Application {
    static final int SEATING_CAPACITY = 42;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scanner sc = new Scanner(System.in);

        List<String> customerNameListBadullaToColombo = new ArrayList<>(); //These lists will store customer name for each trip
        List<String> customerNameListColomboToBadulla = new ArrayList<>();

        List<String> names = new ArrayList<>(); //To get all names to a one list for sorting purpose

        File fileColomboToBadulla = new File("src/dataColomboToBadulla.txt"); //Two different files for save data according to the trip
        File fileBadullaToColombo = new File("src/dataBadullaToColombo.txt");

        Button colomboToBadulla = new Button("Colombo - Badulla"); //Trip selection buttons.
        Button badullaToColombo = new Button("Badulla - Colombo");

        /* Default datepicker for all start up windows
         * Program only allows user to select 30 days from today*/
        DatePicker datePicker = new DatePicker();

        String[][][][] array = new String[2][30][SEATING_CAPACITY][2]; //This 4D Array will store customer data according to the date,seat number and trip

        String[] day = new String[1]; //This array will temporary hold the day of the selected date.

        List<String> days = new ArrayList<>();//A list of selected days

        List<String> dates = new ArrayList<>();// A list of selected dates

        LocalDate today = LocalDate.now();//Present date

        /*---------------------GUI elements for start up windows-----------------------*/
        Stage stage = new Stage();
        stage.setTitle("Select your destination");
        File background = new File("src/Background.jpg");
        Image img1 = new Image(background.toURI().toString());
        BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
        BorderPane bp = new BorderPane();
        bp.setBackground(new Background(new BackgroundImage(img1, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bSize)));
        Label lb1 = new Label("Please select your destination and date.");

        Button close = new Button("   Close   ");
        Button goToMenu = new Button("   Menu   ");

        Label name1 = new Label("Name : ");
        name1.setStyle("-fx-background-color: yellow");
        TextField name1tf = new TextField();
        Label nic = new Label("NIC     : ");
        nic.setStyle("-fx-background-color: yellow");
        TextField nictf = new TextField();
        Label seatNumber = new Label("Seat Number : ");
        seatNumber.setStyle("-fx-background-color: yellow");
        TextField seatNumbertf = new TextField();

        Scene scene = new Scene(bp, 400, 600);
        /*---------------------------------------------------------------------------*/

        int indexColomboToBadulla = 0; //Default array index for trip Colombo to Badulla
        int indexBadullaToColombo = 1;//Default array index for trip Badulla to Colombo

        main:
        while (true) {
            menu:
            while (true) {
                System.out.println("---------------------------------------------------------");
                System.out.println("Enter \"A\" to add a customer to a seat.");
                System.out.println("Enter \"V\" to view all seats.");
                System.out.println("Enter \"F\" to find the seat for a given customers name .");
                System.out.println("Enter \"E\" to view empty seats.");
                System.out.println("Enter \"O\" to view seats ordered alphabetically by name.");
                System.out.println("Enter \"D\" to to delete a customer from a seat.");
                System.out.println("Enter \"S\" to store program data in to file.");
                System.out.println("Enter \"L\" to load program data from file .");
                System.out.println("Enter \"Q\" to quit.");
                System.out.println("---------------------------------------------------------");
                System.out.print("Please select a option from above : ");
                String input = sc.next();

                switch (input) {
                    case "A":
                    case "a":
                        /*Welcome window
                         * addCustomer method is executing inside this method*/
                        welcomeUI(customerNameListColomboToBadulla, customerNameListBadullaToColombo, names, array, indexColomboToBadulla, indexBadullaToColombo, day, days, dates, datePicker, today);
                        break;
                    case "V":
                    case "v":
                        /*Start up window for view all seats
                         * Customer can select specific date to check*/
                        Pane p2a = new Pane();
                        Pane p3a = new Pane();
                        lb1.setStyle("-fx-background-color: yellow");
                        bp.setCenter(p3a);
                        bp.setBottom(p2a);
                        p3a.getChildren().add(datePicker);
                        datePicker.setLayoutX(85);
                        datePicker.setLayoutY(300);
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
                        close.setLayoutX(110);

                        colomboToBadulla.setOnAction(e -> {
                            //Checking if the value of datepicker is null,before today or after 30 days from today
                            if (datePicker.getValue() == null || datePicker.getValue().isBefore(today.plusDays(1)) || datePicker.getValue().isAfter(today.plusDays(30))) {
                                datePicker.setStyle("-fx-border-color: red");
                            } else {
                                datePicker.setStyle("-fx-border-color: null");
                                day[0] = (String.valueOf(datePicker.getValue()).substring(8, 10));
                                System.out.println(day[0]);
                                viewAllSeats(array, indexColomboToBadulla, day);
                            }
                        });
                        badullaToColombo.setOnAction(e -> {
                            //Checking if the value of datepicker is null,before today or after 30 days from today
                            if (datePicker.getValue() == null || datePicker.getValue().isBefore(today.plusDays(1)) || datePicker.getValue().isAfter(today.plusDays(30))) {
                                datePicker.setStyle("-fx-border-color: red");
                            } else {
                                datePicker.setStyle("-fx-border-color: null");
                                day[0] = (String.valueOf(datePicker.getValue()).substring(8, 10));
                                System.out.println(day[0]);
                                viewAllSeats(array, indexBadullaToColombo, day);
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
                    case "F":
                    case "f":
                        while (true) {
                            System.out.println("Enter your NIC here : ");
                            String nicNumber = sc.next();
                            sc.nextLine();
                            System.out.println("Enter your name here (Please enter the exact same name as you registered.) : ");
                            String name = sc.nextLine();
                            //Checking NIC number if it is valid
                            if ((nicNumber.length() != 10 && nicNumber.length() != 12)) { // Validating NIC number
                                System.out.println("please enter a valid NIC number.");
                                System.out.println();
                                //Checking Name if it is valid
                            } else {
                                findSeatByName(customerNameListColomboToBadulla, customerNameListBadullaToColombo, array, days, dates, name, nicNumber);
                                break;
                            }
                        }
                        break;
                    case "E":
                    case "e":
                        /*Start up window for view empty seats
                         * Customer can select specific date to check*/
                        Pane p2b = new Pane();
                        Pane p3b = new Pane();
                        lb1.setStyle("-fx-background-color: yellow");
                        bp.setCenter(p3b);
                        bp.setBottom(p2b);
                        p3b.getChildren().add(datePicker);
                        datePicker.setLayoutX(85);
                        datePicker.setLayoutY(300);
                        p3b.getChildren().add(lb1);
                        p3b.getChildren().add(colomboToBadulla);
                        p3b.getChildren().add(badullaToColombo);
                        lb1.setLayoutX(65);
                        lb1.setLayoutY(250);
                        colomboToBadulla.setLayoutX(121);
                        colomboToBadulla.setLayoutY(350);
                        badullaToColombo.setLayoutX(121);
                        badullaToColombo.setLayoutY(400);
                        p2b.getChildren().add(close);
                        p2b.getChildren().add(goToMenu);
                        p2b.setPadding(new Insets(20));
                        goToMenu.setLayoutX(200);
                        close.setLayoutX(110);

                        colomboToBadulla.setOnAction(e -> {
                            //Checking if the value of datepicker is null,before today or after 30 days from today
                            if (datePicker.getValue() == null || datePicker.getValue().isBefore(today.plusDays(1)) || datePicker.getValue().isAfter(today.plusDays(30))) {
                                datePicker.setStyle("-fx-border-color: red");
                            } else {
                                datePicker.setStyle("-fx-border-color: null");
                                day[0] = (String.valueOf(datePicker.getValue()).substring(8, 10));
                                System.out.println(day[0]);
                                viewEmptySeats(array, indexColomboToBadulla, day);
                            }
                        });
                        badullaToColombo.setOnAction(e -> {
                            //Checking if the value of datepicker is null,before today or after 30 days from today
                            if (datePicker.getValue() == null || datePicker.getValue().isBefore(today.plusDays(1)) || datePicker.getValue().isAfter(today.plusDays(30))) {
                                datePicker.setStyle("-fx-border-color: red");
                            } else {
                                datePicker.setStyle("-fx-border-color: null");
                                day[0] = (String.valueOf(datePicker.getValue()).substring(8, 10));
                                System.out.println(day[0]);
                                viewEmptySeats(array, indexBadullaToColombo, day);
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
                    case "O":
                    case "o":
                        viewSeatsAlphabetically(names, array, days, dates);
                        break;
                    case "D":
                    case "d":
                        /*Start up window for delete a reservation
                         * Customer can select specific date to check*/
                        stage.setTitle("Remove your reservation");
                        Pane p2c = new Pane();
                        Pane p3c = new Pane();
                        lb1.setStyle("-fx-background-color: yellow");
                        bp.setCenter(p3c);
                        bp.setBottom(p2c);
                        p3c.getChildren().add(datePicker);
                        datePicker.setLayoutX(85);
                        datePicker.setLayoutY(300);
                        p3c.getChildren().add(lb1);
                        p3c.getChildren().add(name1);
                        p3c.getChildren().add(name1tf);
                        p3c.getChildren().add(nic);
                        p3c.getChildren().add(nictf);
                        p3c.getChildren().add(seatNumber);
                        p3c.getChildren().add(seatNumbertf);
                        p3c.getChildren().add(colomboToBadulla);
                        p3c.getChildren().add(badullaToColombo);

                        lb1.setLayoutX(65);
                        lb1.setLayoutY(240);

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

                        colomboToBadulla.setLayoutX(121);
                        colomboToBadulla.setLayoutY(350);
                        badullaToColombo.setLayoutX(121);
                        badullaToColombo.setLayoutY(400);
                        p2c.getChildren().add(close);
                        p2c.getChildren().add(goToMenu);
                        p2c.setPadding(new Insets(20));
                        goToMenu.setLayoutX(200);
                        close.setLayoutX(110);

                        colomboToBadulla.setOnAction(e -> {
                            /*Checking if all text fields are filled
                             * Unless border color of the text field will turn red*/
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

                                    //Checking if the value of datepicker is null,before today or after 30 days from today
                                } else if (datePicker.getValue() == null || datePicker.getValue().isBefore(today.plusDays(1)) || datePicker.getValue().isAfter(today.plusDays(30))) {
                                    datePicker.setStyle("-fx-border-color: red");
                                } else {
                                    day[0] = (String.valueOf(datePicker.getValue()).substring(8, 10)); // Getting day from the date and adding it to the array
                                    datePicker.setStyle("-fx-border-color: null");
                                    if (!Objects.equals(array[indexColomboToBadulla][Integer.parseInt(day[0]) - 1][Integer.parseInt(seatNumbertf.getText()) - 1][0], (name1tf.getText())) && !Objects.equals(array[indexBadullaToColombo][Integer.parseInt(day[0]) - 1][Integer.parseInt(seatNumbertf.getText()) - 1][1], (nictf.getText()))) {
                                        Alert a2 = new Alert(Alert.AlertType.ERROR);
                                        a2.setContentText("That seat is not reserved under your name.");
                                        seatNumbertf.setText("");
                                        a2.show();
                                    } else {
                                        Stage stage3 = new Stage();
                                        FlowPane fp = new FlowPane();
                                        Pane p = new Pane();
                                        Label label1 = new Label("Are you sure you want to delete your reservation ?");
                                        Button yes = new Button("Yes");
                                        Button no = new Button("No");
                                        fp.getChildren().add(label1);
                                        fp.getChildren().add(p);
                                        p.getChildren().add(yes);
                                        p.getChildren().add(no);
                                        yes.setLayoutX(120);
                                        no.setLayoutX(180);
                                        fp.setHgap(10);
                                        fp.setVgap(10);
                                        fp.setPadding(new Insets(20));

                                        yes.setOnAction(e1 -> {
                                            deleteSeat(array, indexColomboToBadulla, day, name1tf.getText(), nictf.getText(), seatNumbertf.getText());
                                            Alert a1 = new Alert(Alert.AlertType.INFORMATION);
                                            a1.setContentText("Your reservation was successfully removed.");
                                            a1.show();
                                            System.out.println("Removed Reservations : " + "Name - " + name1tf.getText() + " | NIC Number - " + nictf.getText() + " | Seat Number - " + seatNumbertf.getText() + " | Trip : Colombo To Badulla" + " | Date - " + (datePicker.getValue()));
                                            stage3.close();
                                        });

                                        no.setOnAction(e2 -> {
                                            stage3.close();
                                        });

                                        Scene scene1 = new Scene(fp, 380, 100);
                                        stage3.setScene(scene1);
                                        stage3.showAndWait();
                                    }
                                }
                            }
                        });

                        badullaToColombo.setOnAction(e -> {
                            /*Checking if all text fields are filled
                             * Unless border color of the text field will turn red*/
                            if (name1tf.getText().equals("") || nictf.getText().equals("")) {
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
                                    a2.setAlertType(Alert.AlertType.ERROR);
                                    a2.show();

                                    /*Validating the Name*/
                                } else if (!name1tf.getText().matches("^[a-zA-Z]*$")) {
                                    Alert a2 = new Alert(Alert.AlertType.ERROR);
                                    a2.setContentText("Please enter a valid name.");
                                    name1tf.setText("");
                                    a2.show();

                                    /*Validating entered NIC number*/
                                } else if (nictf.getText().length() != 10 && nictf.getText().length() != 12) {
                                    nictf.setText("");
                                    Alert a2 = new Alert(Alert.AlertType.ERROR);
                                    a2.setContentText("Please enter a valid NIC number.");
                                    a2.show();

                                    //Checking if the value of datepicker is null,before today or after 30 days from today
                                } else if (datePicker.getValue() == null || datePicker.getValue().isBefore(today.plusDays(1)) || datePicker.getValue().isAfter(today.plusDays(30))) {
                                    datePicker.setStyle("-fx-border-color: red");
                                } else {
                                    day[0] = (String.valueOf(datePicker.getValue()).substring(8, 10)); // Getting day from the date and adding it to the array
                                    if (!Objects.equals(array[indexBadullaToColombo][Integer.parseInt(day[0]) - 1][Integer.parseInt(seatNumbertf.getText()) - 1][0], (name1tf.getText())) && !Objects.equals(array[indexBadullaToColombo][Integer.parseInt(day[0]) - 1][Integer.parseInt(seatNumbertf.getText()) - 1][1], (nictf.getText()))) {
                                        Alert a2 = new Alert(Alert.AlertType.ERROR);
                                        a2.setContentText("That seat is not reserved under your name.");
                                        seatNumbertf.setText("");
                                        a2.show();
                                    } else {
                                        datePicker.setStyle("-fx-border-color: null");
                                        Stage stage3 = new Stage();
                                        FlowPane fp = new FlowPane();
                                        Pane p = new Pane();
                                        Label label1 = new Label("Are you sure you want to delete your reservation ?");
                                        Button yes = new Button("Yes");
                                        Button no = new Button("No");
                                        fp.getChildren().add(label1);
                                        fp.getChildren().add(p);
                                        p.getChildren().add(yes);
                                        p.getChildren().add(no);
                                        yes.setLayoutX(120);
                                        no.setLayoutX(180);
                                        fp.setHgap(10);
                                        fp.setVgap(10);
                                        fp.setPadding(new Insets(20));

                                        yes.setOnAction(e1 -> {
                                            deleteSeat(array, indexBadullaToColombo, day, name1tf.getText(), nictf.getText(), seatNumbertf.getText());
                                            Alert a1 = new Alert(Alert.AlertType.INFORMATION);
                                            a1.setContentText("Your reservation was successfully removed.");
                                            a1.show();
                                            System.out.println("Removed Reservations : " + "Name - " + name1tf.getText() + " | NIC Number - " + nictf.getText() + " | Seat Number - " + seatNumbertf.getText() + " | Trip : Badulla To Colombo" + " | Date - " + (datePicker.getValue()));
                                            stage3.close();
                                        });

                                        no.setOnAction(e2 -> {
                                            stage3.close();
                                        });

                                        Scene scene1 = new Scene(fp, 380, 100);
                                        stage3.setScene(scene1);
                                        stage3.showAndWait();
                                    }
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
                    case "S":
                    case "s":
                        storeProgramData(fileColomboToBadulla, fileBadullaToColombo, array, days, dates);
                        break;
                    case "L":
                    case "l":
                        loadProgramData(fileColomboToBadulla, fileBadullaToColombo, array, customerNameListColomboToBadulla, customerNameListBadullaToColombo, names, days, dates);
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

    private static void findSeatByName(List<String> customerNameList1, List<String> customerNameList2, String[][][][] array, List<String> days, List<String> dates, String custName, String nicNumber) {
        if ((customerNameList1.contains(custName) || customerNameList2.contains(custName))) {
            for (int i = 1; i <= SEATING_CAPACITY; i++) {
                for (String j : days) {
                    for (String k : dates) {
                        //Checking the array with names according to the trip Colombo to Badulla
                        if (Objects.equals(array[0][Integer.parseInt(j) - 1][i - 1][0], (custName)) && Objects.equals(array[0][Integer.parseInt(j) - 1][i - 1][1], (nicNumber)) && k.substring(8, 10).equals(j)) {
                            System.out.println("Name - " + custName + " | NIC Number - " + nicNumber + " | Seat Number - " + i + " | Trip - Colombo to Badulla" + " | Date - " + k);
                        } else System.out.print("");

                        //Checking the array with names according to the trip Badulla to Colombo
                        if (Objects.equals(array[1][Integer.parseInt(j) - 1][i - 1][0], (custName)) && Objects.equals(array[1][Integer.parseInt(j) - 1][i - 1][1], (nicNumber)) && k.substring(8, 10).equals(j)) {
                            System.out.println("Name - " + custName + " | NIC Number - " + nicNumber + " | Seat Number - " + i + " | Trip - Badulla to Colombo" + " | Date - " + k);
                        } else System.out.print("");
                    }
                }
            }
        } else {
            System.out.println("No Reservations under your name and NIC.");
        }
    }

    private static void loadProgramData(File file1, File file2, String[][][][] array, List<String> customerNameList1, List<String> customerNameList2, List<String> names, List<String> days, List<String> dates) throws IOException {
        /*Readers for read both data files
         * file1 - Colombo to Badulla data file
         * file2 - Badulla to Colombo data file*/
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

            //Reading file1 adding data to the array
            while (lineReader1 != null) { //Checking line by line if it is not null
                datas1.add(lineReader1.split(":")); //Splitting a line which is not null by ':' and adding divided items to a temporary created array list
                // read next line
                lineReader1 = reader1.readLine();
            }

            for (String[] j : datas1) {
                if (!days.contains(j[3].substring(8, 10)) && !dates.contains(j[3])) { /*checking if days array contains loading days
                 *if it does not, days will be added to the list*/
                    if (!customerNameList1.contains(j[0])) { /*checking customer name lists if they already contain loading names
                     *if it does not, names will be added to the list*/
                        //First index of the array is '0' because the trip we are considering is Colombo To badulla
                        array[0][Integer.parseInt(j[3].substring(8, 10)) - 1][Integer.parseInt(j[2]) - 1][0] = j[0]; // adding data to the array
                        array[0][Integer.parseInt(j[3].substring(8, 10)) - 1][Integer.parseInt(j[2]) - 1][1] = j[1];
                        days.add(j[3].substring(8, 10));
                        dates.add(j[3]);
                        customerNameList1.add(j[0]);
                    } else {
                        array[0][Integer.parseInt(j[3].substring(8, 10)) - 1][Integer.parseInt(j[2]) - 1][0] = j[0];// adding data to the array
                        array[0][Integer.parseInt(j[3].substring(8, 10)) - 1][Integer.parseInt(j[2]) - 1][1] = j[1];
                    }
                } else {
                    if (!customerNameList1.contains(j[0])) {
                        array[0][Integer.parseInt(j[3].substring(8, 10)) - 1][Integer.parseInt(j[2]) - 1][0] = j[0];// adding data to the array
                        array[0][Integer.parseInt(j[3].substring(8, 10)) - 1][Integer.parseInt(j[2]) - 1][1] = j[1];
                        customerNameList1.add(j[0]);
                    } else {
                        array[0][Integer.parseInt(j[3].substring(8, 10)) - 1][Integer.parseInt(j[2]) - 1][0] = j[0];// adding data to the array
                        array[0][Integer.parseInt(j[3].substring(8, 10)) - 1][Integer.parseInt(j[2]) - 1][1] = j[1];
                    }
                }
                if (!names.contains(j[0])) {
                    names.add(j[0]);
                }
            }
            reader1.close();

            //Reading file2 adding data to the array
            while (lineReader2 != null) { //Checking line by line if it is not null
                datas2.add(lineReader2.split(":"));//Splitting a line which is not null by ':' and adding divided items to a temporary created array list
                // read next line
                lineReader2 = reader2.readLine();
            }
            for (String[] j : datas2) {
                if (!days.contains(j[3].substring(8, 10)) && !dates.contains(j[3])) { /*checking if days array contains loading days
                 *if it does not, days will be added to the list*/
                    if (!customerNameList2.contains(j[0])) { /*checking customer name lists if they already contain loading names
                     *if it does not, names will be added to the list*/
                        //First index of the array is '1' because the trip we are considering is Badulla to Colombo
                        array[1][Integer.parseInt(j[3].substring(8, 10)) - 1][Integer.parseInt(j[2]) - 1][0] = j[0];// adding data to the array
                        array[1][Integer.parseInt(j[3].substring(8, 10)) - 1][Integer.parseInt(j[2]) - 1][1] = j[1];
                        days.add(j[3].substring(8, 10));
                        dates.add(j[3]);
                        customerNameList2.add(j[0]);
                    } else {
                        array[1][Integer.parseInt(j[3].substring(8, 10)) - 1][Integer.parseInt(j[2]) - 1][0] = j[0];// adding data to the array
                        array[1][Integer.parseInt(j[3].substring(8, 10)) - 1][Integer.parseInt(j[2]) - 1][1] = j[1];
                    }
                } else {
                    if (!customerNameList2.contains(j[0])) {
                        array[1][Integer.parseInt(j[3].substring(8, 10)) - 1][Integer.parseInt(j[2]) - 1][0] = j[0];// adding data to the array
                        array[1][Integer.parseInt(j[3].substring(8, 10)) - 1][Integer.parseInt(j[2]) - 1][1] = j[1];
                        customerNameList2.add(j[0]);
                    } else {
                        array[1][Integer.parseInt(j[3].substring(8, 10)) - 1][Integer.parseInt(j[2]) - 1][0] = j[0];// adding data to the array
                        array[1][Integer.parseInt(j[3].substring(8, 10)) - 1][Integer.parseInt(j[2]) - 1][1] = j[1];
                    }
                }
                if (!names.contains(j[0])) { /*checking names list if its contain loading names
                if names does not contains, it will be added to the list.*/
                    names.add(j[0]);
                }
            }
            reader2.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("All the data have been loaded.");
    }

    private static void storeProgramData(File file1, File file2, String[][][][] array, List<String> days, List<String> dates) {
        FileWriter fr1 = null;
        BufferedWriter br1 = null;// Use to write data to separate lines
        FileWriter fr2 = null;
        BufferedWriter br2 = null;// Use to write data to separate lines
        try {
            fr1 = new FileWriter(file1, true);
            br1 = new BufferedWriter(fr1);
            fr2 = new FileWriter(file2, true);
            br2 = new BufferedWriter(fr2);
            for (int i = 1; i <= SEATING_CAPACITY; i++) { // for third index - seat number
                for (String j : days) { //for second index - day
                    for (String k : dates) { // dates list is checking to write the date
                        if (array[0][Integer.parseInt(j) - 1][i - 1][0] != null && array[0][Integer.parseInt(j) - 1][i - 1][1] != null && k.substring(8, 10).equals(j)) {
                            br1.write(array[0][Integer.parseInt(j) - 1][i - 1][0] + ":" + array[0][Integer.parseInt(j) - 1][i - 1][1] + ":" + i + ":" + k);
                            br1.newLine(); //Use to write data to separate lines
                        }
                    }
                }
            }
            for (int i = 1; i <= SEATING_CAPACITY; i++) { // for third index - seat number
                for (String j : days) { //for second index - day
                    for (String k : dates) { // dates list is checking to write the date
                        if (array[1][Integer.parseInt(j) - 1][i - 1][0] != null && array[1][Integer.parseInt(j) - 1][i - 1][1] != null && k.substring(8, 10).equals(j)) {
                            br2.write(array[1][Integer.parseInt(j) - 1][i - 1][0] + ":" + array[1][Integer.parseInt(j) - 1][i - 1][1] + ":" + i + ":" + k);
                            br2.newLine(); //Use to write data to separate lines
                        }
                    }
                }
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
            System.out.println("All the data have been successfully added to the files.");
        }
    }

    private static void deleteSeat(String[][][][] array, int index, String[] day, String custName, String nicNumber, String seatNumber) {
        /*Checking if the given customer details is in the array
         * If they exist in the array both name value and nic value will be set as null in the array*/
        if (Objects.equals(array[index][Integer.parseInt(day[0]) - 1][Integer.parseInt(seatNumber) - 1][0], (custName)) && Objects.equals(array[index][Integer.parseInt(day[0]) - 1][Integer.parseInt(seatNumber) - 1][1], (nicNumber))) {
            array[index][Integer.parseInt(day[0]) - 1][Integer.parseInt(seatNumber) - 1][0] = null;
            array[index][Integer.parseInt(day[0]) - 1][Integer.parseInt(seatNumber) - 1][1] = null;
        }
    }

    private static void viewSeatsAlphabetically(List<String> names, String[][][][] array, List<String> days, List<String> dates) {
        String temp;
        int i;
        /*Using 'names' list to sort the names alphabetically
         * first it will check first and second items of names list
         * if they are not in alphabetical order they will be swapped.
         * if they are in alphabetical order, now program will check second and third items in the name list
         * if they are not in alphabetical order they will be swapped.
         * otherwise program will check third and fourth items and do the same thing
         * likewise this sorting algorithm will be checked with all the elements in the name list and set the items in alphabetical order*/
        for (i = 0; i < names.size(); i++) {
            for (int j = i + 1; j < names.size(); j++) {
                if (names.get(j).compareTo(names.get(i)) < 0) {
                    temp = names.get(i);
                    names.set(i, names.get(j));
                    names.set(j, temp);
                }
            }
        }

        /*Then the sorted names will be checked with array items.*/
        for (String custName : names) {
            for (int k = 1; k <= SEATING_CAPACITY; k++) {
                for (String j : days) {
                    for (String l : dates) {
                        //Checking trip Colombo to Badulla
                        if (Objects.equals(array[0][Integer.parseInt(j) - 1][k - 1][0], custName) && l.substring(8, 10).equals(j)) {
                            System.out.println("Name - " + custName + " | NIC Number - " + array[0][Integer.parseInt(j) - 1][k - 1][1] + " | Seat Number - " + k + " | Trip - Colombo to Badulla" + " | Date - " + l);
                        }
                        //Checking trip Badulla to Colombo
                        if (Objects.equals(array[1][Integer.parseInt(j) - 1][k - 1][0], custName) && l.substring(8, 10).equals(j)) {
                            System.out.println("Name - " + custName + " | NIC Number - " + array[1][Integer.parseInt(j) - 1][k - 1][1] + " | Seat Number - " + k + " | Trip - Badulla to Colombo" + " | Date - " + l);
                        }
                    }
                }
            }
        }
    }

    private static void viewEmptySeats(String[][][][] array, int index, String[] day) {
        Stage stage = new Stage();
        stage.setTitle("Empty Seats.");
        System.out.println("Redirecting to view empty seats.");

        Label label = new Label("    Available Seats    ");
        Button close = new Button("   Close   ");
        Button goToMenu = new Button("   Menu   ");

        BorderPane bp = new BorderPane();
        FlowPane fp = new FlowPane();
        Pane p1 = new Pane();
        Pane p2 = new Pane();
        Button b1 = new Button(" ");
        Button b2 = new Button();
        Label lb1 = new Label("Available Seats");
        Label lb2 = new Label("Reserved Seats");

        bp.setCenter(fp);
        bp.setTop(p1);
        bp.setBottom(p2);

        fp.setVgap(10);
        fp.setHgap(10);
        fp.setPadding(new Insets(10, 10, 30, 10));

        p1.setPadding(new Insets(30, 10, 0, 20));
        p2.setPadding(new Insets(70, 20, 20, 20));

        //For create buttons and check array items
        for (int i = 1; i <= SEATING_CAPACITY; i++) {
            Button seat = new Button(" seat " + String.format("%02d", i));
            seat.setId(Integer.toString(i));
            fp.getChildren().add(seat);
            seat.setStyle("-fx-background-color: AQUAMARINE");

            /*Checking if array items are null or not according to the trip and change the color of seats which are not null*/
            if (array[index][Integer.parseInt(day[0]) - 1][i - 1][0] != null) {
                seat.setStyle("-fx-background-color: DARKSALMON");
                seat.setText("Booked");
            }
        }

        p1.getChildren().add(label);
        label.setPadding(new Insets(30, 100, 0, 155));
        label.setStyle("-fx-font-size: 20");
        p2.getChildren().add(close);
        close.setLayoutX(250);
        close.setLayoutY(30);
        goToMenu.setLayoutX(350);
        goToMenu.setLayoutY(30);
        p2.getChildren().add(goToMenu);
        p2.getChildren().add(b1);
        p2.getChildren().add(b2);
        p2.getChildren().add(lb1);
        p2.getChildren().add(lb2);
        b2.setPadding(new Insets(13, 23, 2, 13));
        b1.setPadding(new Insets(13, 18, 2, 13));
        b2.setStyle("-fx-background-color: AQUAMARINE");
        b1.setStyle("-fx-background-color: DARKSALMON");

        b2.setLayoutX(30);
        b2.setLayoutY(5);
        b1.setLayoutX(30);
        b1.setLayoutY(50);

        lb1.setLayoutX(72);
        lb1.setLayoutY(14);
        lb2.setLayoutX(72);
        lb2.setLayoutY(59);

        close.setOnAction(e -> {
            stage.close();
        });

        goToMenu.setOnAction(e -> {
            stage.close();
        });

        Scene scene = new Scene(bp, 503, 520);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private static void viewAllSeats(String[][][][] array, int index, String[] day) {
        Stage stage = new Stage();
        stage.setTitle("View All Seats.");
        System.out.println("Redirecting to view all seats.");

        Label label = new Label("W E L C O M E");
        Button close = new Button("   Close   ");
        Button goToMenu = new Button("   Menu   ");

        BorderPane bp = new BorderPane();
        FlowPane fp = new FlowPane();
        Pane p1 = new Pane();
        Pane p2 = new Pane();

        bp.setCenter(fp);
        bp.setTop(p1);
        bp.setBottom(p2);

        fp.setVgap(10);
        fp.setHgap(10);
        fp.setPadding(new Insets(10, 10, 30, 10));

        p1.setPadding(new Insets(30, 10, 0, 20));
        p2.setPadding(new Insets(20));

        //For create buttons and check array items
        for (int i = 1; i <= SEATING_CAPACITY; i++) {
            Button seat = new Button(" seat " + String.format("%02d", i));
            seat.setId(Integer.toString(i));
            fp.getChildren().add(seat);
            seat.setStyle("-fx-background-color: AQUAMARINE");

            /*Checking if array items are null or not, according to the trip and change the color of seats which are not null*/
            if (array[index][Integer.parseInt(day[0]) - 1][i - 1][0] != null) {
                seat.setStyle("-fx-background-color: DARKSALMON");
                seat.setText("Booked");
            }
        }

        p1.getChildren().add(label);
        label.setPadding(new Insets(30, 100, 10, 205));
        label.setStyle("-fx-font-size: 15");
        p2.getChildren().add(close);
        close.setLayoutX(160);
        goToMenu.setLayoutX(260);
        p2.getChildren().add(goToMenu);

        close.setOnAction(e -> {
            stage.close();
        });

        goToMenu.setOnAction(e -> {
            stage.close();
        });

        Scene scene = new Scene(bp, 503, 490);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private static void addCustomer(List<String> customerNameList, List<String> names, String[][][][] array, int index, String[] day) {
        List<String> tempSeats = new ArrayList<>();//Temporary list to add and remove seats when user select and dis-select seat buttons.
        Stage stage1 = new Stage();
        stage1.setTitle("Book your seat here");
        System.out.println("Redirecting to booking page.");

        Button back = new Button("   Back   ");
        Button book = new Button("   Book   ");
        Button close = new Button("   Close   ");

        Label name1 = new Label("Full Name (First Name and Surname) : ");
        name1.setStyle("-fx-background-color: yellow");
        TextField name1tf = new TextField(); //TextField to get customer name
        Label nic = new Label("National Identity Number                   : ");
        nic.setStyle("-fx-background-color: yellow");
        TextField nictf = new TextField(); //TextField to get NIC number

        BorderPane bp = new BorderPane();
        FlowPane fp1 = new FlowPane();
        Pane p3 = new Pane();
        Pane p2 = new Pane();

        /*Setting a background image*/
        File file = new File("src/Train.png");
        Image image = new Image(file.toURI().toString());
        BackgroundImage bgImg = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, null);

        bp.setBottom(p2);
        bp.setTop(p3);
        bp.setCenter(fp1);

        p3.getChildren().add(name1);
        p3.getChildren().add(name1tf);
        p3.getChildren().add(nic);
        p3.getChildren().add(nictf);
        p3.setBackground(new Background(bgImg));

        name1.setLayoutX(25);
        name1.setLayoutY(35);
        name1tf.setLayoutX(300);
        name1tf.setLayoutY(30);
        nic.setLayoutX(25);
        nic.setLayoutY(85);
        nictf.setLayoutX(300);
        nictf.setLayoutY(80);

        p2.getChildren().add(back);
        back.setLayoutY(0);
        back.setLayoutX(167);
        back.setStyle("-fx-border-color: yellow;" + "-fx-border-width: 2;" + "-fx-border-radius: 3;" + "-fx-focus-color: transparent;");

        p2.getChildren().add(close);
        close.setLayoutY(0);
        close.setLayoutX(265);
        close.setStyle("-fx-border-color: red;" + "-fx-border-width: 2;" + "-fx-border-radius: 3");

        /* Button 'book' will be disabled until both name and nic fields are filled*/
        book.disableProperty().bind(Bindings.or(
                name1tf.textProperty().isEmpty(), nictf.textProperty().isEmpty()
        ));

        p2.getChildren().add(book);
        book.setLayoutY(0);
        book.setLayoutX(367);
        book.setStyle("-fx-border-color: green;" + "-fx-border-width: 2;" + "-fx-border-radius: 3");

        fp1.setVgap(10);
        fp1.setHgap(10);
        fp1.setPadding(new Insets(30, 10, 47, 10));

        p3.setPadding(new Insets(0, 0, 30, 0));
        p2.setPadding(new Insets(60, 10, 0, 20));

        /*Creating Radio Buttons as seats to select*/
        for (int i = 1; i <= SEATING_CAPACITY; i++) {
            RadioButton seat = new RadioButton(" seat " + String.format("%02d", i) + " ");
            seat.getStyleClass().remove("radio-button");
            seat.getStyleClass().add("toggle-button");
            seat.setId(Integer.toString(i)); // setting an id to seats
            seat.setStyle("-fx-background-color:AQUAMARINE");
            fp1.getChildren().add(seat);

            /*Mouse hover effect on seat buttons*/
            seat.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
                if (seat.isSelected()) {
                    seat.setStyle("-fx-background-color: DARKSALMON");
                } else
                    seat.setStyle("-fx-background-color: AQUAMARINE");
            });
            seat.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
                if (seat.isSelected()) {
                    seat.setStyle("-fx-background-color: DARKSALMON");
                } else
                    seat.setStyle("-fx-background-color: yellow");
            });
            /*----------------------------------*/

            seat.setOnAction(e -> {
                if (seat.isSelected()) {
                    seat.setStyle("-fx-background-color: DARKSALMON");
                    tempSeats.add(seat.getId());// adding the seat when user selects
                } else {
                    seat.setStyle("-fx-background-color: AQUAMARINE");
                    tempSeats.remove(seat.getId());// remove the seat if user dis-select the seat
                }
            });

            /*when starting the gui, program will check if a particular seat(s) have already selected,
             * depending on that color of the seats will be changed*/
            if (array[index][Integer.parseInt(day[0]) - 1][i - 1][0] != null && array[index][Integer.parseInt(day[0]) - 1][i - 1][1] != null) {
                seat.setStyle("-fx-background-color: DARKSALMON");
                seat.setText(" Booked");
                seat.setDisable(true);
            }
        }

        book.setOnAction(event -> {
            /*if no seats have selected by the user, program will give an error massage to the user*/
            if (tempSeats.isEmpty()) {
                Alert a1 = new Alert(Alert.AlertType.NONE);
                a1.setContentText("Pleae select a seat to book");
                a1.setAlertType(Alert.AlertType.ERROR);
                a1.show();

                //validating the NIC number
            } else if (nictf.getText().length() != 10 && nictf.getText().length() != 12) {
                Alert a2 = new Alert(Alert.AlertType.NONE);
                a2.setContentText("Please enter a valid NIC number.");
                nictf.setText("");
                a2.setAlertType(Alert.AlertType.ERROR);
                a2.show();
            } else {
                /*checking customer name lists if they already contain loading names
                 *if it does not, names will be added to the list*/
                if (customerNameList.contains(name1tf.getText())) {
                    for (String tempSeat : tempSeats) {
                        array[index][Integer.parseInt(day[0]) - 1][Integer.parseInt(tempSeat) - 1][0] = name1tf.getText();
                        array[index][Integer.parseInt(day[0]) - 1][Integer.parseInt(tempSeat) - 1][1] = nictf.getText();
                    }
                } else {
                    customerNameList.add(name1tf.getText());
                    for (String tempSeat : tempSeats) {
                        array[index][Integer.parseInt(day[0]) - 1][Integer.parseInt(tempSeat) - 1][0] = name1tf.getText();
                        array[index][Integer.parseInt(day[0]) - 1][Integer.parseInt(tempSeat) - 1][1] = nictf.getText();
                    }
                }
                /*checking names list if its contain loading names
                if names does not contains, it will be added to the list.*/
                if (!names.contains(name1tf.getText())) {
                    names.add(name1tf.getText());
                }
                stage1.close();
                Alert a2 = new Alert(Alert.AlertType.INFORMATION);
                a2.setContentText("Your seats are successfully reserved. Thank You !");
                a2.show();
            }
        });

        back.setOnAction(e -> {
            stage1.close();
        });

        close.setOnAction(e -> {
            Stage stage3 = new Stage();
            FlowPane fp = new FlowPane();
            Pane p = new Pane();

            //getting confirmation to close the program
            Label label1 = new Label("Are you sure you want to exit ?");
            Button yes = new Button("Yes");
            Button no = new Button("No");

            fp.getChildren().add(label1);
            fp.getChildren().add(p);
            p.getChildren().add(yes);
            p.getChildren().add(no);
            yes.setLayoutX(60);
            no.setLayoutX(110);
            fp.setHgap(10);
            fp.setVgap(10);
            fp.setPadding(new Insets(20));

            yes.setOnAction(e1 -> {
                stage1.close();
                stage3.close();
            });

            no.setOnAction(e2 -> {
                stage3.close();
            });

            Scene scene = new Scene(fp, 250, 100);
            stage3.setScene(scene);
            stage3.showAndWait();
        });

        Scene scene = new Scene(bp, 612, 535);
        stage1.setScene(scene);
        stage1.showAndWait();
    }

    private static void welcomeUI(List<String> customerNameList1, List<String> customerNameList2, List<String> names, String[][][][] array, int index1, int index2, String[] day, List<String> days, List<String> dates, DatePicker datePicker, LocalDate today) {
        Stage stage = new Stage();
        stage.setTitle("Select your destination");

        File file = new File("src/Background.jpg");
        Image image = new Image(file.toURI().toString());
        BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
        BorderPane bp = new BorderPane();
        bp.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bSize)));

        Button colomboToBadulla = new Button("Colombo To Badulla");
        Button badullaToColombo = new Button("Badulla To Colombo");

        Label lb1 = new Label("Please select your destination and date.");
        Pane p2 = new Pane();
        Pane p3 = new Pane();
        Button close = new Button("   Close   ");
        Button goToMenu = new Button("   Menu   ");

        lb1.setStyle("-fx-background-color: yellow");

        bp.setCenter(p3);
        bp.setBottom(p2);

        p3.getChildren().add(datePicker);
        datePicker.setLayoutX(85);
        datePicker.setLayoutY(300);

        p3.getChildren().add(lb1);
        p3.getChildren().add(colomboToBadulla);
        p3.getChildren().add(badullaToColombo);

        lb1.setLayoutX(60);
        lb1.setLayoutY(250);
        lb1.setStyle("-fx-background-color: yellow");

        colomboToBadulla.setLayoutX(121);
        colomboToBadulla.setLayoutY(350);

        badullaToColombo.setLayoutX(121);
        badullaToColombo.setLayoutY(400);

        p2.getChildren().add(close);
        p2.getChildren().add(goToMenu);
        p2.setPadding(new Insets(20));

        goToMenu.setLayoutX(200);

        close.setLayoutX(110);

        colomboToBadulla.setOnAction(e -> {
            //Checking if the value of datepicker is null,before today or after 30 days from today
            if (datePicker.getValue() == null || datePicker.getValue().isBefore(today.plusDays(1)) || datePicker.getValue().isAfter(today.plusDays(30))) {
                datePicker.setStyle("-fx-border-color: red");
            } else {
                datePicker.setStyle("-fx-border-color: null");
                day[0] = (String.valueOf(datePicker.getValue()).substring(8, 10));
                if (days.contains(day[0]) && dates.contains(String.valueOf(datePicker.getValue()))) {
                    addCustomer(customerNameList1, names, array, index1, day);
                } else {
                    days.add(day[0]);
                    dates.add(String.valueOf(datePicker.getValue()));
                    addCustomer(customerNameList1, names, array, index1, day);
                }
            }
        });
        badullaToColombo.setOnAction(e -> {
            //Checking if the value of datepicker is null,before today or after 30 days from today
            if (datePicker.getValue() == null || datePicker.getValue().isBefore(today.plusDays(1)) || datePicker.getValue().isAfter(today.plusDays(30))) {
                datePicker.setStyle("-fx-border-color: red");
            } else {
                datePicker.setStyle("-fx-border-color: null");
                day[0] = (String.valueOf(datePicker.getValue()).substring(8, 10));
                if (days.contains(day[0]) && dates.contains(String.valueOf(datePicker.getValue()))) {
                    addCustomer(customerNameList2, names, array, index2, day);
                } else {
                    days.add(day[0]);
                    dates.add(String.valueOf(datePicker.getValue()));
                    addCustomer(customerNameList2, names, array, index2, day);
                }
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
}
