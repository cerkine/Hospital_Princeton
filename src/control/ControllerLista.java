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
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import model.Hospital;
import model.Pacient;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ControllerLista implements Initializable {

    private String csvFile = null;
    private List<Pacient> p = new ArrayList<>();
    private ObservableList<Pacient> data;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");

    @FXML TableView<Pacient> tablePacients;
    @FXML Button btnLoadFile;
    @FXML TextField txtDNI, txtNom, txtCognoms, edat1, edat2, peso1, peso2,alçada1,alçada2,edat1D,edat2D,peso1D,peso2D,alçada1D,alçada2D;
    @FXML Text planoEdat, planoPeso,planoAlçada,planoEdatD,planoPesoD,planoAlçadaD;
    @FXML RadioButton rbedat, rbrangedat, rbpeso, rbrangpeso,rbalçada,rbedatD,rbrangedatD,rbpesoD,rbrangpesoD,rbalçadaD,rbedatG,rbalçadaG,rbpesG;
    @FXML PieChart idPieChart;
    @FXML BarChart<String, Number> idBarChart;
    @FXML CategoryAxis xAxis;
    @FXML NumberAxis yAxis;
    @FXML AnchorPane arriba;


    private int edat1Int, edat2Int;
    private float pes1Int, pes2Int;
    private int alçada1Int;
    private int alçada2Int;

    private int edatD1Int, edatD2Int;
    private float pesD1Int, pesD2Int;
    private int alçadaD1Int;
    private int alçadaD2Int;

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

    /**
     * Metodo para hacer la busqueda, los if estan porque el codigo te permite que en un rango de edades o peso, si dejas un campo vacio se entiende que es todos
     * los que sean mas pequeño o mas grande que el campo que has escrito y eso lo hacemos con la cadeana de if asignado el maximo valor o el minimo
     * @param event
     */
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


    /**
     * Estos metodos es para cambiar la visibilidad de algunos campos segun el radiobutton seleccionado
     * @param event
     */
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

    /**
     * Repetimos el mismo funcionamiento que usamos en el buscador pero esta vez lo metemos en una grafica.
     */
    private void setChart() {
        idPieChart.getData().clear();
        List<Pacient> pacients = p.stream().collect(Collectors.toList());
        long totalPacientes = pacients.stream().count();
        if (edat1D.getText().equals("")){
            edatD1Int = 0;
        }
        else {
            edatD1Int = Integer.parseInt(this.edat1D.getText());
        }
        if (edat2D.getText().equals("")){
            edatD2Int = Integer.MAX_VALUE;
        }
        else {
            edatD2Int = Integer.parseInt(this.edat2D.getText());
        }
        if (peso1D.getText().equals("")) {
            pesD1Int = 0.0f;
        } else {
            pesD1Int = Float.parseFloat(this.peso1D.getText());
        }
        if (peso2D.getText().equals("")) {
            pesD2Int = Float.MAX_VALUE;
        } else {
            pesD2Int = Float.parseFloat(this.peso2D.getText());
        }

        if (alçada1D.getText().equals("")){
            alçadaD1Int = 0;
        }
        else {
            alçadaD1Int = Integer.parseInt(this.alçada1D.getText());
        }
        if (alçada2D.getText().equals("")){
            alçadaD2Int = Integer.MAX_VALUE;
        }
        else {
            alçadaD2Int = Integer.parseInt(this.alçada2D.getText());
        }

        if (rbedatD.isSelected()){
            if (!edat1D.getText().equals(""))
                pacients=pacients.stream().filter(pacient -> pacient.getEdat()==(edatD1Int)).collect(Collectors.toList());
        }else {
            if (!(edat1D.getText().equals("") && edat2D.getText().equals(""))){
                pacients=pacients.stream().filter(pacient ->
                        pacient.getEdat()>(edatD1Int) && pacient.getEdat()<(edatD2Int)).collect(Collectors.toList());
            }
        }

        if (rbpesoD.isSelected()){
            if (!peso1D.getText().equals(""))
                pacients=pacients.stream().filter(pacient -> pacient.getPes()==(pesD1Int)).collect(Collectors.toList());
        }else {
            if (!(peso1D.getText().equals("") && peso2D.getText().equals(""))){
                pacients=pacients.stream().filter(pacient ->
                        pacient.getPes()>(pesD1Int) && pacient.getPes()<(pesD2Int)).collect(Collectors.toList());
            }
        }

        if (rbalçadaD.isSelected()){
            if (!alçada1D.getText().equals(""))
                pacients=pacients.stream().filter(pacient -> pacient.getAlçada()==(alçadaD1Int)).collect(Collectors.toList());
        }else {
            if (!(alçada1D.getText().equals("") && alçada2D.getText().equals(""))){
                pacients=pacients.stream().filter(pacient ->
                        pacient.getAlçada()>(alçadaD1Int) && pacient.getAlçada()<(alçadaD2Int)).collect(Collectors.toList());
            }
        }
        long pacients2=pacients.stream().count();
        idPieChart.setTitle("RESULTATS");
        idPieChart.getData().add(new PieChart.Data("COINCIDENTS: " + pacients2,pacients2));
        idPieChart.getData().add(new PieChart.Data("RESTA: " + (totalPacientes-pacients2),totalPacientes-pacients2));
    }

    public void btnGraphic(ActionEvent event) {
        setGraphic();
    }

    public void loadGraphic(Event event) {
        setGraphic();
    }

    public void setGraphic() {
        idBarChart.getData().clear();

        XYChart.Series<String, Number> series1 = new XYChart.Series<String, Number>();

        //Segun el boton que se haya seleccionado, hacemos lo mismo pero cogiendo los datos corrspondientes
        if(rbedatG.isSelected()) {
            xAxis.setLabel("Edat");

            yAxis.setLabel("Number");

            Map<String, Integer> edades = new HashMap<>(); //Creamos un map para almacenar los datos que encontremos

            for (int i = 0; i < p.size(); i++) { //Recorremos la array de los pacientes
                if (edades.containsKey(String.valueOf(p.get(i).getEdat()))) { //Si en el mapa de los datos ya esta este valor
                    edades.put(String.valueOf(p.get(i).getEdat()), edades.get(String.valueOf(p.get(i).getEdat())) + 1); //Sumamos uno al valor de esa clave
                } else {
                    edades.put(String.valueOf(p.get(i).getEdat()), +1); //Sino lo creamos y lo inicializamos a 1

                }
            }
            for (String key : edades.keySet()) {
                series1.getData().add(new XYChart.Data<>(key, edades.get(key))); //Creamos la tabla XY

            }
        }
        //Hacemos lo mismo para los otros botones cogindo su informacion correspondiente
        if (rbpesG.isSelected()){

            xAxis.setLabel("Pes");

            yAxis.setLabel("Number");

            Map<String, Integer> pesos = new HashMap<>();

            for (int i = 0; i < p.size(); i++) {
                if (pesos.containsKey(String.valueOf(p.get(i).getPes()))) {
                    pesos.put(String.valueOf(p.get(i).getPes()), pesos.get(String.valueOf(p.get(i).getPes())) + 1);
                } else {
                    pesos.put(String.valueOf(p.get(i).getPes()), +1);

                }
            }
            for (String key : pesos.keySet()) {
                series1.getData().add(new XYChart.Data<>(key, pesos.get(key)));

            }

        }
        if (rbalçadaG.isSelected()){

            xAxis.setLabel("Alçada");

            yAxis.setLabel("Number");

            Map<String, Integer> alçada = new HashMap<>();

            for (int i = 0; i < p.size(); i++) {
                if (alçada.containsKey(String.valueOf(p.get(i).getAlçada()))) {
                    alçada.put(String.valueOf(p.get(i).getAlçada()), alçada.get(String.valueOf(p.get(i).getAlçada())) + 1);
                } else {
                    alçada.put(String.valueOf(p.get(i).getAlçada()), +1);

                }
            }
            for (String key : alçada.keySet()) {
                series1.getData().add(new XYChart.Data<>(key, alçada.get(key)));

            }

        }
        idBarChart.getData().add(series1); //Y añadimos el mapa a la grafica

    }


    public void clickTable(MouseEvent event) throws Exception {


        //Cal verificar si hi ha alguna selecció feta al fer doble click
        if (event.getClickCount() == 2 && !tablePacients.getSelectionModel().isEmpty()){ //Cuando se hace dobleclick en un paciente
            //Se comprueba si ya esta en la lista de espera
            File file = new File ("./src/data/espera.csv");
            FileReader fr= new FileReader("./src/data/espera.csv");
            BufferedReader br = new BufferedReader(fr);
            String line = tablePacients.getSelectionModel().getSelectedItem().getDNI() + "," + tablePacients.getSelectionModel().getSelectedItem().getNom()+ "," +tablePacients.getSelectionModel().getSelectedItem().getCognoms()+ "," +tablePacients.getSelectionModel().getSelectedItem().getDataNaixament().format(formatter)+ "," +tablePacients.getSelectionModel().getSelectedItem().getGenere()+ "," +tablePacients.getSelectionModel().getSelectedItem().getTelefon()+ ",\"" +tablePacients.getSelectionModel().getSelectedItem().getPes()+"\",\""+tablePacients.getSelectionModel().getSelectedItem().getAlçada() + "\"";
            boolean found = findLine(br, file, line);

            if (found){ //Si esta, lanzamos un aviso de que ya esta en la lista de espera
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Paciente");
                alert.setHeaderText("Datos");
                alert.setContentText("" +
                        "Nombre: " + tablePacients.getSelectionModel().getSelectedItem().getNom() +
                        "\nApellidos: " + tablePacients.getSelectionModel().getSelectedItem().getCognoms() +
                        "\nDNI: " + tablePacients.getSelectionModel().getSelectedItem().getDNI() +
                        "\nEste paciente ya esta en la lista de espera");
                alert.show();
            }
            else { //Sino, preguntamos al usuario si lo quire añadir, mostrando los datos del paciente

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Paciente");
                alert.setHeaderText("Datos");
                alert.setContentText("" +
                        "Nombre: " + tablePacients.getSelectionModel().getSelectedItem().getNom() +
                        "\nApellidos: " + tablePacients.getSelectionModel().getSelectedItem().getCognoms() +
                        "\nDNI: " + tablePacients.getSelectionModel().getSelectedItem().getDNI() +
                        "\nQuieres añadir a este paciente a la lista de espera?");


                Optional<ButtonType> result = alert.showAndWait();
                if (!result.isPresent()) {
                    // alert is exited, no button has been pressed.
                } else if (result.get() == ButtonType.OK) { //En caso afirmativo, lo añadimos al archivo de la lista de espera
                    FileWriter fw = new FileWriter("./src/data/espera.csv", true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.newLine();
                    bw.write(tablePacients.getSelectionModel().getSelectedItem().getDNI() + "," + tablePacients.getSelectionModel().getSelectedItem().getNom() + "," + tablePacients.getSelectionModel().getSelectedItem().getCognoms() + "," + tablePacients.getSelectionModel().getSelectedItem().getDataNaixament().format(formatter) + "," + tablePacients.getSelectionModel().getSelectedItem().getGenere() + "," + tablePacients.getSelectionModel().getSelectedItem().getTelefon() + ",\"" + tablePacients.getSelectionModel().getSelectedItem().getPes() + "\",\"" + tablePacients.getSelectionModel().getSelectedItem().getAlçada() + "\"");
                    bw.close();
                }
                //oke button is pressed
                else if (result.get() == ButtonType.CANCEL) {
                }
                // cancel button is pressed
            }
        }

        }

    public boolean findLine(BufferedReader br , File f, String Line) throws IOException{ //Metodo para comprobar linea por linea si esta el paciente en el archivo

        List<String> lines = Files.readAllLines(f.toPath(),
                StandardCharsets.UTF_8);

        for (int i = 1; i < lines.size(); i++){
            if (lines.get(i).equals(Line)){
                return true;
            }
        }

        return false;

    }
}