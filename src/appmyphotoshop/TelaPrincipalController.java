
package appmyphotoshop;

import ij.ImagePlus;
import ij.process.ImageProcessor;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class TelaPrincipalController implements Initializable {
    
    @FXML
    private ScrollPane pnscrool;
    @FXML
    private ImageView imageview;
    private int flag = 0;
    private Image imagem;
    private BufferedImage bimag;
    private File file=null;
    @FXML
    private Menu mntransf;
    @FXML
    private Menu idImageJ;
    @FXML
    private MenuItem idSalvarComo;
    @FXML
    private MenuItem IdFechar;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mntransf.setDisable(true); 
        idImageJ.setDisable(true);
        idSalvarComo.setDisable(true);
    }    

    private void carregarImagem() {
        FileChooser fchooser=new FileChooser();
        file =fchooser.showOpenDialog(null);
        if(file!=null) //um arquivo de imagem foi selecionado
        {
            imagem=new Image(file.toURI().toString());
            imageview.setFitWidth(imagem.getWidth());
            imageview.setFitHeight(imagem.getHeight());
            imageview.setImage(imagem);
            bimag=SwingFXUtils.fromFXImage(imagem, null);
            mntransf.setDisable(false);
            idSalvarComo.setDisable(false);
            idImageJ.setDisable(false);
        }
    }
    
    private void TomCinza()
    {
        
        int aux,pixel[]={0,0,0,0};
        WritableRaster raster=bimag.getRaster();
        
        for(int y=0;y<imagem.getHeight();y++)   //cada linha da imagem
            for(int x=0;x<imagem.getWidth();x++)  //cada coluna da imagem
            {    raster.getPixel(x, y, pixel);
                 aux=(int)(pixel[0]*0.3+pixel[1]*0.59+pixel[2]*0.11);
                 pixel[0]=pixel[1]=pixel[2]=aux;
                 raster.setPixel(x, y, pixel);
            }
        imagem = SwingFXUtils.toFXImage(bimag, null);
        imageview.setImage(imagem);
        flag = 1;
    }
    private void TrocarCores(){
        BufferedImage bimag = SwingFXUtils.fromFXImage(imagem, null);
        int aux, pixel[] = {0,0,0,0};
        WritableRaster raster = bimag.getRaster();
        
        for(int y = 0; y < imagem.getHeight();y++) // cada Linha da Imagem
            for(int x = 0; x < imagem.getWidth(); x++){
                raster.getPixel(x,y,pixel);
                aux = pixel[0];
                pixel[0] = pixel[2];
                pixel[2] = aux;
                raster.setPixel(x,y, pixel);
            }
        imagem = SwingFXUtils.toFXImage(bimag, null);
        imageview.setImage(imagem);     
    }
    
    @FXML
    private void evtDetecBorda(ActionEvent event) {
        ImagePlus imgplus = new ImagePlus();
        imgplus.setImage(bimag);
        ImageProcessor ipr=imgplus.getProcessor();
        ipr.findEdges();
        imageview.setImage(SwingFXUtils.toFXImage(imgplus.getBufferedImage(), null));
        bimag=imgplus.getBufferedImage();
        flag = 1;
    }
    @FXML
    private void evtAbout(ActionEvent event){
        String str1 = "Developers: Christian Ferreira e Mateus Mendonça Esta Aplicação tem com objetivo realizar tranformações em Imagens JPEG, JPG, GIF, PNG";
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(str1);
        alert.setTitle("About Application");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
           getClass().getResource("DialogTheme.css").toExternalForm());
        dialogPane.getStyleClass().add("DialogTheme");
        
        alert.showAndWait();
    }
    
    private void SalvarComo(){
        FileChooser fchooser=new FileChooser();
        String extensao;
        fchooser.getExtensionFilters().addAll(
                new ExtensionFilter("PNG (.png)",".png"),
                new ExtensionFilter("JPG (.jpg)",".jpg")
        );


        fchooser.setInitialFileName(file.getName());
        file = fchooser.showSaveDialog(null);
        if(file != null) //um arquivo de imagem foi selecionado
        {
            try{
               extensao = file.getName().substring(1 + file.getName().lastIndexOf(".")).toLowerCase();
               ImageIO.write(bimag, extensao , file);
            }catch(Exception e)
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erro: "+e.getMessage());
                alert.showAndWait();
            }
        }
        
    }
    @FXML
    private void evtSalvarComo(ActionEvent event) {
        SalvarComo();
    }
    private void evtVerifyClose(ActionEvent event) {
        if(flag == 1){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Fechar Aplicativo ?");
            alert.setContentText("Foram realizadas alterações na imagem, deseja salvar?");
            Optional<ButtonType> Result = alert.showAndWait();
            
            if(Result.get() == ButtonType.OK)
                evtSalvarComo(event);
        }
        ((Stage)(imageview.getScene().getWindow())).close();
    }
    private void evtVerifyOpen(ActionEvent event) {
        if(flag == 1){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Abrir nova Imagem ?");
            alert.setContentText("Foram realizadas alterações na imagem, deseja salvar?");
            Optional<ButtonType> Result = alert.showAndWait();
            
            if(Result.get() == ButtonType.OK)
                evtSalvarComo(event);
        }
        ((Stage)(imageview.getScene().getWindow())).close();
    }
    private void PretoBranco(){
        int aux, pixel[] = {0,0,0,0};
        WritableRaster raster = bimag.getRaster();
        
        for(int y = 0; y < imagem.getHeight(); y++) //Cada linha da img
            for(int x = 0; x < imagem.getWidth(); x++){ //Cada Coluna da img
                raster.getPixel(x, y, pixel);
                aux = (int) (pixel[0]*0.3 + pixel[1]*0.59 + pixel[2] * 0.11);
                if(aux >= 144)
                    pixel[0] = pixel[1] = pixel[2] = 255;
                else
                    pixel[0] = pixel[1] = pixel[2] = 0;
                raster.setPixel(x, y, pixel);
            }
        imagem = SwingFXUtils.toFXImage(bimag,null);
        imageview.setImage(imagem);
        idSalvarComo.setDisable(false);
        flag = 1;
    }
    private void Negative(){
        int aux, pixel[] = {0,0,0,0};
        WritableRaster raster = bimag.getRaster();
        
        for(int y = 0; y < imagem.getHeight(); y++) //Cada linha da img
            for(int x = 0; x < imagem.getWidth(); x++){ //Cada Coluna da img
                raster.getPixel(x, y, pixel);
                aux = (int) (pixel[0]*0.3 + pixel[1]*0.59 + pixel[2] * 0.11);
                if(aux >= 0){
                    pixel[0] = 255 - pixel[0];
                    pixel[1] = 255 - pixel[1];
                    pixel[2] = 255 - pixel[2];
                }
                    
                raster.setPixel(x, y, pixel);
            }
        imagem = SwingFXUtils.toFXImage(bimag,null);
        imageview.setImage(imagem);
        idSalvarComo.setDisable(false);
        flag = 1;
    }    
    
    private void MirrorV(){
        int pixel[] = {0,0,0,0}, pixel2[] = {0,0,0,0},  x, y, z;
        WritableRaster raster  = bimag.getRaster(), rasterDestiny;
        
        BufferedImage bImgDestiny;
        
        bImgDestiny = new BufferedImage(bimag.getWidth(), bimag.getHeight(),bimag.getType());
        /*rasterDestiny = bImgDestiny.getRaster();
        
        for(int y = 0; y < imagem.getHeight() / 2; y++)
            for(int x = 0; x < imagem.getWidth(); x++){
                raster.getPixel(x, y, pixel);
                rasterDestiny.getPixel(x, (int)imagem.getHeight()-1-y, pixel);
            }
        bimag = bImgDestiny;
        imagem = SwingFXUtils.toFXImage(bImgDestiny, null);
        imageview.setImage(imagem);
        idSalvarComo.setDisable(false);
        flag = 1;*/
        
        for(y = 0, z = (int)imagem.getHeight() - 1;y<imagem.getHeight() / 2;y++, z--)  //cada coluna da imagem
        {    
            for(x = 0; x<imagem.getWidth(); x++){   //cada linha da imagem
                raster.getPixel(x, y, pixel);
                raster.getPixel(x, z, pixel2);
                raster.setPixel(x, y, pixel2);
                raster.setPixel(x, z, pixel);
            }
        }
        imagem = SwingFXUtils.toFXImage(bimag, null);
        imageview.setImage(imagem);
        flag = 1;
        idSalvarComo.setDisable(false);
    }
    private void MirrorH(){
        int pixel[] = {0,0,0,0},  pixel2[] = {0,0,0,0};
        int x, z;
        WritableRaster raster  = bimag.getRaster(), rasterDestiny;
        
        BufferedImage bImgDestiny;
        
        bImgDestiny = new BufferedImage(bimag.getWidth(), bimag.getHeight(),bimag.getType());
        /*rasterDestiny = bImgDestiny.getRaster();
        
        for(int y = 0; y < imagem.getHeight(); y++)
            for(int x = 0; x < imagem.getWidth() / 2; x++){
                raster.getPixel(x, y, pixel);
                rasterDestiny.getPixel((int)imagem.getWidth()-1-x, y, pixel);
            }
        bimag = bImgDestiny;
        imagem = SwingFXUtils.toFXImage(bImgDestiny, null);
        imageview.setImage(imagem);
        idSalvarComo.setDisable(false);*/
        
        for(int y=0;y<imagem.getHeight();y++)  //cada coluna da imagem
        {    
            for(x = 0, z = (int)imagem.getWidth() - 1; x<imagem.getWidth() / 2; x++, z--){   //cada linha da imagem
                raster.getPixel(x, y, pixel);
                raster.getPixel(z, y, pixel2);
                raster.setPixel(x, y, pixel2);
                raster.setPixel(z, y, pixel);
            }
        }
        imagem = SwingFXUtils.toFXImage(bimag, null);
        imageview.setImage(imagem);
        flag = 1;
        idSalvarComo.setDisable(false);
    }
    @FXML
    private void evtNegative(ActionEvent event) {
        Negative();
    }
    
    private void evtTrocaCores(MouseEvent event) {
        TrocarCores();
    }

    @FXML
    private void evtAbrir(ActionEvent event) {
        carregarImagem();
    }

    @FXML
    private void evtTomCinza(ActionEvent event) {
        TomCinza();
    }

    @FXML
    private void evtFechar(ActionEvent event) {
        evtVerifyClose(event);
    }
    @FXML
    private void evtPretoBranco(ActionEvent event) {
        PretoBranco();
    }

    @FXML
    private void evtMirrorH(ActionEvent event) {
        MirrorH();
    }

    @FXML
    private void evtMirrorV(ActionEvent event) {
        MirrorV();
    }

    @FXML
    private void evtSmooth(ActionEvent event) {
        ImagePlus imgplus = new ImagePlus();
        imgplus.setImage(bimag);
        ImageProcessor ipr = imgplus.getProcessor();
        ipr.smooth();
        imageview.setImage(SwingFXUtils.toFXImage(imgplus.getBufferedImage(), null));
        bimag=imgplus.getBufferedImage();
        flag = 1;
    }

    @FXML
    private void evtSharpen(ActionEvent event) {
        ImagePlus imgplus = new ImagePlus();
        imgplus.setImage(bimag);
        ImageProcessor ipr = imgplus.getProcessor();
        ipr.sharpen();
        imageview.setImage(SwingFXUtils.toFXImage(imgplus.getBufferedImage(), null));
        bimag=imgplus.getBufferedImage();
        flag = 1;
    }

    @FXML
    private void evtRotate(ActionEvent event) {
        List<String> opcoes = new ArrayList<>();
        opcoes.add("45");
        opcoes.add("90");
        opcoes.add("180");
        ImagePlus imgplus = new ImagePlus();
        imgplus.setImage(bimag);
        ImageProcessor ipr = imgplus.getProcessor();
        

        ChoiceDialog<String> input = new ChoiceDialog<>("0", opcoes);
        input.setTitle("Rotação");
        input.setHeaderText("Rotação de imagem");
        input.setContentText("Escolha o ângulo a ser rotacionado:");
        
        Optional<String> resultado = input.showAndWait();
        int graus = Integer.parseInt(resultado.get());
        ipr.rotate(graus);
        
        imageview.setImage(SwingFXUtils.toFXImage(imgplus.getBufferedImage(), null));
        bimag=imgplus.getBufferedImage();
        flag = 1;
    }


}
