package control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import model.Pacient;
import model.Hospital;
import model.Persona;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ControllerLista implements Initializable {

    private String csvFile = null;
    private List<Pacient> p = new ArrayList<>();
    private ObservableList<Pacient> data;

    @FXML TableView<Pacient> tablePacients;
    @FXML Button btnLoadFile;
    @FXML TextField txtDNI, txtNom, txtCognoms, edat1, edat2, peso1, peso2,alçada1,alçada2,edat1D,edat2D,peso1D,peso2D,alçada1D,alçada2D;
    @FXML Text planoEdat, planoPeso,planoAlçada,planoEdatD,planoPesoD,planoAlçadaD;
    @FXML RadioButton rbedat, rbrangedat, rbpeso, rbrangpeso,rbalçada,rbedatD,rbrangedatD,rbpesoD,rbrangpesoD,rbalçadaD;
    @FXML PieChart idPieChart;
    @FXML BarChart<String, Number> idBarChart;
    @FXML CategoryAxis xAxis;
    @FXML NumberAxis yAxis;

    private int edat1Int, edat2Int;
    private float pes1Int, pes2Int;
    private int alçada1Int;
    private int alçada2Int;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        data = FXCollections.observableArrayList();
        if(csvFile == null) {
            btnLoadFile.setText("Click per carregar CSV");
        }else {
            setTableView();
        }
    }

    private void setTableView() {
        TableColumn DNI = new TableColumn("DNI");
        TableColumn Nom = new TableColumn("Nom");
        TableColumn Cognoms = new TableColumn("Cognoms");
        TableColumn DataNaix = new TableColumn("Data de Naixament");
        TableColumn Genre = new TableColumn("Gènere");
        TableColumn Telefon = new TableColumn("Telèfon");
        TableColumn pes = new TableColumn("Pes");
        TableColumn Alçada = new TableColumn("Alçada");

        // COMPTE!!!! les propietats han de tenir getters i setters
        DNI.setCellValueFactory(new PropertyValueFactory<Pacient, String>("DNI"));
        Nom.setCellValueFactory(new PropertyValueFactory<Pacient, String>("Nom"));
        Cognoms.setCellValueFactory(new PropertyValueFactory<Pacient, String>("Cognoms"));
        DataNaix.setCellValueFactory(new PropertyValueFactory<Pacient, String>("DataNaixament"));
        Genre.setCellValueFactory(new PropertyValueFactory<Pacient, String>("genere"));
        Telefon.setCellValueFactory(new PropertyValueFactory<Pacient, String>("Telefon"));
        pes.setCellValueFactory(new PropertyValueFactory<Pacient, Float>("Pes"));
        Alçada.setCellValueFactory(new PropertyValueFactory<Pacient, Integer>("Alçada"));

        tablePacients.getColumns().addAll(DNI, Nom, Cognoms, DataNaix, Genre, Telefon, pes, Alçada);

        //data.add(new Pacient("111", "n", "co", LocalDate.of(2000, 12, 12), Persona.Genere.HOME, "55555", 5.4f, 100));
        loadData();
        data.addAll(p);
        tablePacients.setItems(data);

    }

    private void loadData() {
        Hospital hospital = new Hospital();
        p.addAll(hospital.loadPacients(csvFile));
    }


    public void clickLoadFile(ActionEvent event) {
        if(csvFile == null) {
            FileChooser fc = new FileChooser();
            fc.setTitle("Select csv file");
            File file = fc.showOpenDialog(null);
            csvFile = file.getAbsolutePath();
            setTableView();
            btnLoadFile.setText("Loaded");
        }else {
            btnLoadFile.setText("File is loaded");
        }
    }

    public void btnCerca(ActionEvent event) {

        List<Pacient> pacients = p.stream().collect(Collectors.toList());
        if (edat1.getText().equals("")){
            edat1Int = 0;
        }
        else {
            edat1Int = Integer.parseInt(this.edat1.getText());
        }
        if (edat2.getText().equals("")){
            edat2Int = Integer.MAX_VALUE;
        }
        else {
            edat2Int = Integer.parseInt(this.edat2.getText());
        }
        if (peso1.getText().equals("")) {
            pes1Int = 0.0f;
        } else {
            pes1Int = Float.parseFloat(this.peso1.getText());
        }
        if (peso2.getText().equals("")) {
            pes2Int = Float.MAX_VALUE;
        } else {
            pes2Int = Float.parseFloat(this.peso2.getText());
        }

        if (alçada1.getText().equals("")){
            alçada1Int = 0;
        }
        else {
            alçada1Int = Integer.parseInt(this.alçada1.getText());
        }
        if (alçada2.getText().equals("")){
            alçada2Int = Integer.MAX_VALUE;
        }
        else {
            alçada2Int = Integer.parseInt(this.alçada2.getText());
        }


        pacients=pacients.stream().filter(pacient -> pacient.getDNI().contains(txtDNI.getText())).collect(Collectors.toList());

        pacients=pacients.stream().filter(pacient -> pacient.getNom().contains(txtNom.getText())).collect(Collectors.toList());

        pacients=pacients.stream().filter(pacient -> pacient.getCognoms().contains(txtCognoms.getText())).collect(Collectors.toList());
        if (rbedat.isSelected()){
            if (!edat1.getText().equals(""))
            pacients=pacients.stream().filter(pacient -> pacient.getEdat()==(edat1Int)).collect(Collectors.toList());
        }else {
            if (!(edat1.getText().equals("") && edat2.getText().equals(""))){
                pacients=pacients.stream().filter(pacient ->
                        pacient.getEdat()>(edat1Int) && pacient.getEdat()<(edat2Int)).collect(Collectors.toList());
            }
        }

        if (rbpeso.isSelected()){
            if (!peso1.getText().equals(""))
                pacients=pacients.stream().filter(pacient -> pacient.getPes()==(pes1Int)).collect(Collectors.toList());
        }else {
            if (!(peso1.getText().equals("") && peso2.getText().equals(""))){
                pacients=pacients.stream().filter(pacient ->
                        pacient.getPes()>(pes1Int) && pacient.getPes()<(pes2Int)).collect(Collectors.toList());
            }
        }

        if (rbalçada.isSelected()){
            if (!alçada1.getText().equals(""))
                pacients=pacients.stream().filter(pacient -> pacient.getAlçada()==(alçada1Int)).collect(Collectors.toList());
        }else {
            if (!(alçada1.getText().equals("") && alçada2.getText().equals(""))){
                pacients=pacients.stream().filter(pacient ->
                        pacient.getAlçada()>(alçada1Int) && pacient.getAlçada()<(alçada2Int)).collect(Collectors.toList());
            }
        }

        if (txtDNI.getText().equals("") && txtNom.getText().equals("") && txtCognoms.getText().equals("") && this.edat1.getText().equals("") && this.edat2.getText().equals("")&&
        this.peso1.getText().equals("") && this.peso2.getText().equals("") && this.alçada1.getText().equals("") && this.alçada2.getText().equals("")) {
            updateTable(p);
        } else updateTable(pacients);
    }



    public void makeVisEdat(ActionEvent event){
        if (rbedat.isSelected()){
            planoEdat.setVisible(false);
            edat2.setVisible(false);
        }
        else if (rbrangedatD.isSelected()){
            planoEdatD.setVisible(true);
            edat2D.setVisible(true);
        }
    }

    public void makeVisEdat2(ActionEvent event){
        if (rbedatD.isSelected()){
            planoEdatD.setVisible(false);
            edat2D.setVisible(false);
        }
        else if (rbrangedatD.isSelected()){
            planoEdatD.setVisible(true);
            edat2D.setVisible(true);
        }
    }


    public void makeVisPeso(ActionEvent event){
        if (rbpeso.isSelected()){
            planoPeso.setVisible(false);
            peso2.setVisible(false);
        }
        else if (rbrangpeso.isSelected()){
            planoPeso.setVisible(true);
            peso2.setVisible(true);
        }
    }
    public void makeVisPeso2(ActionEvent event){
        if (rbpesoD.isSelected()){
            planoPesoD.setVisible(false);
            peso2D.setVisible(false);
        }
        else if (rbrangpesoD.isSelected()){
            planoPesoD.setVisible(true);
            peso2D.setVisible(true);
        }
    }

    private void updateTable(List<Pacient> pacients) {
        data.clear();
        data.addAll(pacients);
        tablePacients.setItems(data);
    }

    public void makeVisAlçada(ActionEvent event){
        if (rbalçada.isSelected()){
            planoAlçada.setVisible(false);
            alçada2.setVisible(false);
        }
        else{
            planoAlçada.setVisible(true);
            alçada2.setVisible(true);
        }
    }
    public void makeVisAlçada2(ActionEvent event){
        if (rbalçadaD.isSelected()){
            planoAlçadaD.setVisible(false);
            alçada2D.setVisible(false);
        }
        else{
            planoAlçadaD.setVisible(true);
            alçada2D.setVisible(true);
        }
    }



    public void btnChart(ActionEvent event) {
        setChart();
    }

    public void loadChart(Event event) {
        setChart();
    }

    private void setChart() {
        idPieChart.getData().clear();
        long dones = p.stream()
                .filter(pacient -> pacient.getGenere()== Persona.Genere.DONA)
                .count();
        long homes = p.stream()
                .filter(pacient -> pacient.getGenere()== Persona.Genere.HOME)
                .count();
        idPieChart.setTitle("Gènere");
        idPieChart.getData().add(new PieChart.Data(Persona.Genere.DONA.toString(),dones));
        idPieChart.getData().add(new PieChart.Data(Persona.Genere.HOME.toString(),homes));

    }

    public void btnGraphic(ActionEvent event) {
        setGraphic();
    }

    public void loadGraphic(Event event) {
        setGraphic();
    }

    private void setGraphic() {
        idBarChart.getData().clear();
        xAxis = new CategoryAxis();
        xAxis.setLabel("category");

        yAxis = new NumberAxis();
        yAxis.setLabel("score");

        idBarChart = new BarChart<>(xAxis, yAxis);
        idBarChart.setTitle("Comparison between various cars");

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Fiat");
        series1.getData().add(new XYChart.Data<>("Speed", 1.0));
        series1.getData().add(new XYChart.Data<>("User rating", 3.0));
        series1.getData().add(new XYChart.Data<>("Milage", 5.0));
        series1.getData().add(new XYChart.Data<>("Safety", 5.0));

        idBarChart.getData().add(series1);

    }

    public void changeText(KeyEvent keyEvent) {
        data.clear();
        List<Pacient> pacients = p.stream()
                .filter(pacient -> pacient.getNom().contains(txtNom.getText()))
                .filter((pacient -> pacient.getCognoms().contains(txtCognoms.getText())))
                .collect(Collectors.toList());
        data.addAll(pacients);
        tablePacients.setItems(data);
    }

    public void clickTable(MouseEvent event) {
        //Cal verificar si hi ha alguna selecció feta al fer doble click
        if (event.getClickCount() == 2 && !tablePacients.getSelectionModel().isEmpty()){
            System.out.println(tablePacients.getSelectionModel().getSelectedItem().getNom());
        }
    }
}