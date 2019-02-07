package model;



import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;


public class Hospital {
    private Map<String, Pacient> map_pacients = new HashMap<>();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Collection<Pacient> loadPacients(String file) {
        CSVReader csvreader = null;
        String[] line;
        try {
            csvreader = new CSVReader(new FileReader(file));
            csvreader.readNext(); //saltem primera línia de titols
            while ((line = csvreader.readNext()) != null) {
                System.out.println(line[0] + ":" + line[4]);
                map_pacients.putIfAbsent(line[0],
                        new Pacient(line[0],
                                line[1],
                                line[2],
                                LocalDate.parse(line[3],formatter),
                                Persona.Genere.valueOf(line[4]),
                                line[5],
                                Float.valueOf(line[6]),
                                Integer.valueOf(line[7]))
                );
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

        //Retornem la llista de valors
        return map_pacients.values();
    }

}