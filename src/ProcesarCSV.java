/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import util.Row;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @version 1.0
 * @author esteban
 */
public class ProcesarCSV {

    /**
     * @param args the command line arguments
     */
    private StringBuilder sql;
    private Row line = Row.getInstance();

    public ProcesarCSV() throws IOException {
        sql = new StringBuilder();
        crearTabla();
        leerArchivoXLS();
        eliminarFilasRepetidas();
        generarSQL();
    }

    //FAC,DEP,IDE,MAT,GR,NOMBRE DE LA MATERIA,CUPO,MATRI,AULA,HORARIO,CEDULA,DOCENTE
    public void crearTabla() {
        sql.append("CREATE TABLE IF NOT EXISTS `CURSOS` (\n");
        sql.append("`id` MEDIUMINT NOT NULL AUTO_INCREMENT,\n");
        sql.append("`FACULTAD` varchar(2) NOT NULL,\n");
        sql.append("`DEPARTAMENTO` varchar(2) NOT NULL,\n");
        sql.append("`MATERIA` varchar(3) NOT NULL,\n");
        sql.append("`GRUPO` int(2) NOT NULL,\n");
        sql.append("`NOMBRE` varchar(50) NOT NULL,\n"); // En el archivo se manejan 27 caracteres
        sql.append("`AULA` varchar(10) NOT NULL,\n");
        sql.append("`HORARIO` varchar(15) NOT NULL,\n");
        sql.append("`PROFESOR` varchar(50) NOT NULL,\n");
        sql.append("PRIMARY KEY (`id`)\n");
        sql.append(");\n\n");
    }

    public void leerArchivoXLS() throws FileNotFoundException, IOException {
        String linea;
        FileReader f = new FileReader("/home/esteban/Descargas/PROGRAMACION.csv");
        BufferedReader b = new BufferedReader(f);

        sql.append("INSERT INTO `CURSOS` (`ID`,`FACULTAD`, `DEPARTAMENTO`, `MATERIA`, `GRUPO`, `NOMBRE`, `AULA`, `HORARIO`, `PROFESOR`) VALUES\n");
        b.readLine();
        while ((linea = b.readLine()) != null) {
            sql.append(fragmentarLinea(linea));
        }
        b.close();
        sql.delete(sql.length() - 2, sql.length());
        sql.append(";");
    }

    public String fragmentarLinea(String linea) {

        String[] token = linea.split(",", 12);

        line.setAux(token[0]);
        line.setFacultad((!line.getAux().isEmpty()) ? line.getAux() : line.getFacultad());
        line.setFacultad((line.getFacultad().length() == 1) ? "0" + line.getFacultad() : line.getFacultad());

        line.setAux(token[1]);
        line.setDepartamento((!line.getAux().isEmpty()) ? line.getAux() : line.getDepartamento());
        line.setDepartamento((line.getDepartamento().length() == 1) ? "0" + line.getDepartamento() : line.getDepartamento());

        line.setAux(token[3]);
        line.setMateria((!line.getAux().isEmpty()) ? line.getAux() : line.getMateria());
        if (line.getMateria().length() == 1) {
            line.setMateria("00" + line.getMateria());
        } else if (line.getMateria().length() == 2) {
            line.setMateria("0" + line.getMateria());
        }

        line.setAux(token[4]);
        line.setGrupo((!line.getAux().isEmpty()) ? line.getAux() : line.getGrupo());

        line.setAux(token[5]);
        line.setNombre((!line.getAux().isEmpty()) ? line.getAux() : line.getNombre());

        line.setAux(token[8]);
        line.setAula((!line.getAux().isEmpty()) ? line.getAux() : line.getAula());
//        line.setAux(line.getAula().substring(line.getAula().length() - 3));
//        
//        line.setAula(line.getAula().replace(line.getAux(), "-" + line.getAux()));
//        System.out.println(line.getAula());

        line.setAux(token[9]);
        line.setHorario((!line.getAux().isEmpty()) ? line.getAux() : line.getHorario());

        line.setAux(token[11]);
        line.setProfesor((!line.getAux().isEmpty()) ? line.getAux() : line.getProfesor());

        return ("('" + line.getFacultad() + "', "
                + "'" + line.getDepartamento() + "', "
                + "'" + line.getMateria() + "', "
                + "'" + line.getGrupo() + "', "
                + "'" + line.getNombre() + "', "
                + "'" + line.getAula() + "', "
                + "'" + line.getHorario() + "', "
                + "'" + line.getProfesor() + "'),\n");

    }

    public void generarSQL() {
        String directorio = System.getProperty("user.dir") + "/PROGRAMACION.sql";
        File file = new File(directorio);
        try {
            FileWriter w = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(w);
            PrintWriter wr = new PrintWriter(bw);
            wr.write(sql.toString());//Escribir en el archivo
            wr.close();
            bw.close();
        } catch (IOException e) {

        };

    }

    private void eliminarFilasRepetidas() {

    }

    public static void main(String[] args) throws IOException {
        ProcesarCSV run = new ProcesarCSV();
    }

}
