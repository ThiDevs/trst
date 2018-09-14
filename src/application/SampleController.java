package application;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONObject;

public class SampleController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField Pesquisa;

    @FXML
    private ListView list,list2;

    JSONObject data;

    @FXML
    void Pesquisar(ActionEvent event) {
        try {
            list.getItems().clear();
            HttpResponse<String> response = Unirest.get("https://api.vagalume.com.br/search.artmus?apikey=deef00ef0eb28795b026393460b7fd4c&q="+Pesquisa.getText().replaceAll(" ","%20")+"&limit=5")
                    .header("Cache-Control", "no-cache")
                    .header("Postman-Token", "e86097ef-9f2f-4b67-ba58-18954fda16ec")
                    .asString();

            data = new JSONObject(response.getBody());
            System.out.println();
            System.out.println();
            for (int i = 0; i!= data.getJSONObject("response").getJSONArray("docs").length(); i++){

                try {
                    list.getItems().add(new Label("Musica " + (i+1) + ": " + data.getJSONObject("response").getJSONArray("docs").getJSONObject(i).get("title") + " - " + data.getJSONObject("response").getJSONArray("docs").getJSONObject(i).get("band")));

                }catch (Exception e){
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() throws InterruptedException {
        list.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                int i =  list.getSelectionModel().getSelectedIndex();

                String title = "Instrumental" + data.getJSONObject("response").getJSONArray("docs").getJSONObject(i).get("title") + " - " + data.getJSONObject("response").getJSONArray("docs").getJSONObject(i).get("band");
                Search pesquisa = new Search(title);
                Thread t1 = new Thread(pesquisa);
                t1.start();
                try{t1.join();}catch (Exception e){}
                List<String> lista =  pesquisa.getLista();
                for (String lst : lista){
                    list2.getItems().add(new Label(lst));
                }




            }
        });

    }


}
