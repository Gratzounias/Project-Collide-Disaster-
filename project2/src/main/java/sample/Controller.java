package sample;

import com.mysql.cj.core.io.ExportControlled;
import com.mysql.cj.core.io.MysqlTextValueDecoder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import mosquito.MainPublisher;
import mosquito.MainSubscriber;
import mysql.DataModel;
import mysql.SearchInDatabase;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.xml.crypto.Data;
import java.awt.*;

public class Controller {      // Class Controller - ta paidia pou perierxontai sto para8iro
    public Button searchid;    //to koumpi search
    public Button resetid;     //to koumpi reset
    public TableView tableid;  //o pinakas twn apotelesmatwn

    public TextField source;  //pedia pou simplirwnontai to to source,accx....collide , gia anazitisi sto D.B.
    public TextField accx;
    public TextField accy;
    public TextField accz;
    public TextField prox;
    public TextField lat;
    public TextField ln;
    public TextField date;
    public TextField colide;

    public TextField erroraccx;  // pi8ani apoklisi la8ous apo tin timi pou exoume simplirwsei st antistoixo apo ta parapanw pedia
    public TextField erroraccy;
    public TextField erroraccz;
    public TextField errorprox;
    public TextField errorlat;
    public TextField errorln;
    public TextField errordate;

    public TextField prox_freq; // sixnotita elegxou

    public TextField prox_thres; // katwflia pou mporoume na ri8misoume
    public TextField accx_thres; // katwflia pou mporoume na ri8misoume
    public TextField accy_thres; // katwflia pou mporoume na ri8misoume
    public TextField accz_thres; // katwflia pou mporoume na ri8misoume

    public TextField ip;         // katwflia pou mporoume na ri8misoume
    public TextField port;       // katwflia pou mporoume na ri8misoume

    public MainSubscriber mainSubscriber = new MainSubscriber(); //orismos Main Subscriber Intelij
    public MainPublisher mainPublisher = new MainPublisher();    //orismos Main Publisher  Intelij


    @FXML
    public void initialize() { //arxikopoiisi - orismos twn pediwn tis selidas tou InteliJ gia ta antistoixa pedia

        TableColumn source = new TableColumn("source"); //orismos stilwn tou pinaka twn apotelesmatwn
        TableColumn accx = new TableColumn("accx");
        TableColumn accy = new TableColumn("accy");
        TableColumn accz = new TableColumn("accz");
        TableColumn prox = new TableColumn("prox");
        TableColumn lat = new TableColumn("lat");
        TableColumn ln = new TableColumn("ln");
        TableColumn date = new TableColumn("date");
        TableColumn collide = new TableColumn("collide");


        tableid.getColumns().clear();
        tableid.getColumns().addAll(source, accx, accy, accz, prox, lat, ln, date, collide); //dimiourgia tou pinaka

        source.setCellValueFactory(new PropertyValueFactory<DataModel, String>("source"));
        accx.setCellValueFactory(new PropertyValueFactory<DataModel, String>("accx"));
        accy.setCellValueFactory(new PropertyValueFactory<DataModel, String>("accy"));
        accz.setCellValueFactory(new PropertyValueFactory<DataModel, String>("accz"));
        prox.setCellValueFactory(new PropertyValueFactory<DataModel, String>("prox"));
        lat.setCellValueFactory(new PropertyValueFactory<DataModel, String>("lat"));
        ln.setCellValueFactory(new PropertyValueFactory<DataModel, String>("ln"));
        date.setCellValueFactory(new PropertyValueFactory<DataModel, String>("date"));
        collide.setCellValueFactory(new PropertyValueFactory<DataModel, String>("collide"));

        mainSubscriber.main(prox_thres, accx_thres, accy_thres, accz_thres); //perasma sto MainSubscriber twn timwn twn katofliwn
        mainPublisher.main(prox_freq ,ip , port);//perasma ston publisher tis sixnotitas elegxou ,tis ip kai tou  epi8imitou port
    }

    private String checkString(TextField t) { // sinartisi pou epistrefei to string pou exei valei o xristis  kai xrisimopoioume to trim g na eleg3oume tixon periptosi pou exei simplirwsei kena
        if (!t.getText().trim().isEmpty()) {//an den exei simplirwsei o xristis tpt epistrefei Null
            return t.getText().trim();
        } else {
            return null;
        }
    }

    private Float checkFloat(TextField t) {          // sinartisi pou epistrefei to string pou exei valei o xristis  kai xrisimopoioume to trim g na eleg3oume tixon periptosi pou exei simplirwsei kena
        //if (!t.getText().trim().isEmpty()) {      //an den exei simplirwsei o xristis tpt epistrefei Null
        if (!t.getText().trim().isEmpty()) {
            try {
                Float f = Float.parseFloat(t.getText());
                return f;
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }


    public void searchidclick(ActionEvent actionEvent) {

        SearchInDatabase search = new SearchInDatabase();//dimiourgia neou antikeimenou Search In Database
        {
            String s = checkString(source); //elegxos tis timis tou source  ,an oxi Null tote st valueOfAccx vale tin timi pou edwse o xristis
            if (s != null) {
                search.valueOfSource = s;
            }
        }
        {
            Float s = checkFloat(accx);//elegxos tis timis tou accx ,an oxi Null tote st valueOfAccx vale tin timi pou edwse o xristis
            if (s != null) {
                search.valueOfAccx = s;
            }
        }
        {
            Float s = checkFloat(accy);//elegxos tis timis tou accy  ,an oxi Null tote st valueOfAccy vale tin timi pou edwse o xristis
            if (s != null) {
                search.valueOfAccy = s;
            }
        }
        {
            Float s = checkFloat(accz);//elegxos tis timis tou Accz  ,an oxi Null tote st valueOfAccz vale tin timi pou edwse o xristis
            if (s != null) {
                search.valueOfAccz = s;
            }
        }
        {
            Float s = checkFloat(prox);//elegxos tis timis tou prox  ,an oxi Null tote st valueOfProx vale tin timi pou edwse o xristis
            if (s != null) {
                search.valueOfProx = s;
            }
        }
        {
            Float s = checkFloat(lat);//elegxos tis timis tou lat  ,an oxi Null tote st valueOfLat vale tin timi pou edwse o xristis
            if (s != null) {
                search.valueOfLat = s;
            }
        }
        {
            Float s = checkFloat(ln);//elegxos tis timis tou lng  ,an oxi Null tote st valueOfLn vale tin timi pou edwse o xristis
            if (s != null) {
                search.valueOfLn = s;
            }
        }
        {
            Float s = checkFloat(date);//elegxos tis timis tou date  ,an oxi Null tote st valueOfDate vale tin timi pou edwse o xristis
            if (s != null) {
                search.valueOfDate = s;
            }
        }
        {
            Float s = checkFloat(colide);//elegxos tis timis tou scolide  ,an oxi Null tote st valueOfColide vale tin timi pou edwse o xristis
            if (s != null) {             // 1->> epibebaiwmeni sigkrousi , 0-> oxi epibebaiwmeni
                search.valueOfCollide = s;
            }
        }


        {
            Float s = checkFloat(erroraccx); //antistoixa me parapanw ,an oxi null pernietai stin timi tis valueofError i timi pou edwse o xristis
            if (s != null) {                  //opou valueOfError i pi8ani apoklisi pou 8eloume
                search.valueOfErrorAccx = s;
            }
        }
        {
            Float s = checkFloat(erroraccy);
            if (s != null) {
                search.valueOfErrorAccy = s;
            }
        }
        {
            Float s = checkFloat(erroraccz);
            if (s != null) {
                search.valueOfErrorAccz = s;
            }
        }
        {
            Float s = checkFloat(errorprox);
            if (s != null) {
                search.valueOfErrorProx = s;
            }
        }
        {
            Float s = checkFloat(errorlat);
            if (s != null) {
                search.valueOfErrorLat = s;
            }
        }
        {
            Float s = checkFloat(errorln);
            if (s != null) {
                search.valueOfErrorLn = s;
            }
        }
        {
            Float s = checkFloat(errordate);
            if (s != null) {
                search.valueOfErrorDate = s;
            }
        }


        ObservableList<DataModel> list = search.fetch(); //ilopoiise tin anazitisi

        tableid.setItems(list); //dimiourgia listas apotelesmatwn
    }


    public void resetidclick(ActionEvent actionEvent) { // Reset Button

        tableid.getItems().clear(); //Clean tin lista twn apotelesmatwn gia nea anazitisi
    }

}

// Sources :
// 1) http://docs.oracle.com/javafx/2/ui_controls/table-view.htm
// 2) http://www.mkyong.com/jdbc/jdbc-preparestatement-example-select-list-of-the-records/
