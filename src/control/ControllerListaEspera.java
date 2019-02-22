package control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.Hospital;
import model.Pacient;

import java.io.*;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ControllerListaEspera implements Initializable {
    private String csvFile = "./src/data/espera.csv";

    private ObservableList<Pacient> data;
    @FXML
    TableView<Pacient> tablePacients;
    private List<Pacient> p = new ArrayList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        data = FXCollections.observableArrayList();

        TableColumn DNI = new TableColumn("DNI");
        TableColumn Nom = new TableColumn("Nom");
        TableColumn Cognoms = new TableColumn("Cognoms");
        TableColumn DataNaix = new TableColumn("Data de Naixament");
        TableColumn Genre = new TableColumn("Gènere");
        TableColumn Telefon = new TableColumn("Telèfon");
        TableColumn pes = new TableColumn("Pes");
        TableColumn Alçada = new TableColumn("Alçada");

        DNI.setCellValueFactory(new PropertyValueFactory<Pacient, String>("DNI"));
        Nom.setCellValueFactory(new PropertyValueFactory<Pacient, String>("Nom"));
        Cognoms.setCellValueFactory(new PropertyValueFactory<Pacient, String>("Cognoms"));
        DataNaix.setCellValueFactory(new PropertyValueFactory<Pacient, String>("DataNaixament"));
        Genre.setCellValueFactory(new PropertyValueFactory<Pacient, String>("genere"));
        Telefon.setCellValueFactory(new PropertyValueFactory<Pacient, String>("Telefon"));
        pes.setCellValueFactory(new PropertyValueFactory<Pacient, Float>("Pes"));
        Alçada.setCellValueFactory(new PropertyValueFactory<Pacient, Integer>("Alçada"));

        tablePacients.getColumns().addAll(DNI, Nom, Cognoms, DataNaix, Genre, Telefon, pes, Alçada);

        loadData();
        data.addAll(p);
        tablePacients.setItems(data);
    }

    private void loadData() {
        Hospital hospital = new Hospital();
        p.addAll(hospital.loadPacients(csvFile));
    }

    public void clickTable(MouseEvent event) throws Exception {


        //Cal verificar si hi ha alguna selecció feta al fer doble click
        if (event.getClickCount() == 2 && !tablePacients.getSelectionModel().isEmpty()){

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Paciente");
            alert.setHeaderText("Datos");
            alert.setContentText("" +
                    "Nombre: " + tablePacients.getSelectionModel().getSelectedItem().getNom() +
                    "\nApellidos: " + tablePacients.getSelectionModel().getSelectedItem().getCognoms() +
                    "\nDNI: " + tablePacients.getSelectionModel().getSelectedItem().getDNI() +
                    "\nQuieres eliminar a este paciente de la lista de espera?");


            Optional<ButtonType> result = alert.showAndWait();
            if(!result.isPresent()) {
                // alert is exited, no button has been pressed.
            }
            else if(result.get() == ButtonType.OK){

                File file = new File ("./src/data/espera.csv");
                FileReader fw= new FileReader("./src/data/espera.csv");
                BufferedReader bw = new BufferedReader(fw);
                String line = tablePacients.getSelectionModel().getSelectedItem().getDNI() + "," + tablePacients.getSelectionModel().getSelectedItem().getNom()+ "," +tablePacients.getSelectionModel().getSelectedItem().getCognoms()+ "," +tablePacients.getSelectionModel().getSelectedItem().getDataNaixament().format(formatter)+ "," +tablePacients.getSelectionModel().getSelectedItem().getGenere()+ "," +tablePacients.getSelectionModel().getSelectedItem().getTelefon()+ ",\"" +tablePacients.getSelectionModel().getSelectedItem().getPes()+"\",\""+tablePacients.getSelectionModel().getSelectedItem().getAlçada() + "\"";
                removeLine(bw, file, line);
                bw.close();
                fw.close();

                data.clear();
                data = FXCollections.observableArrayList();
                p = new ArrayList<>();
                loadData();
                data.addAll(p);
                tablePacients.setItems(data);

            }
            //oke button is pressed
            else if(result.get() == ButtonType.CANCEL){
            }
            // cancel button is pressed
        }

    }

    public static void removeLine(BufferedReader br , File f, String Line) throws IOException{
        File temp = new File("temp.csv");
        BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
        String removeID = Line;
        String currentLine;
        while((currentLine = br.readLine()) != null){
            String trimmedLine = currentLine.trim();
            if(trimmedLine.equals(removeID)){
                currentLine = "";
            }
            else {
                bw.write(currentLine + System.getProperty("line.separator"));
            }


        }
        bw.close();
        br.close();
        f.delete();
        temp.renameTo(f);
    }



}