package fxLiikuntapaivakirja;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import liikuntapaivakirja.Kayttaja;
import liikuntapaivakirja.Liikuntapaivakirja;

/**
 * @author kaisa
 * @version Apr 23, 2021
 *
 */
public class TilastotViewController implements ModalControllerInterface<Liikuntapaivakirja> {
    
    @FXML private Label labelVirhe;
    @FXML private TextField liikuntasuorituksia;
    @FXML private TextField eriLajeja;
    @FXML private TextField liikuntaaYhteensa;
    
    
    @Override
    public Liikuntapaivakirja getResult() {
        return liikuntapaivakirja;
    }


    @Override
    public void handleShown() {
        setKayttaja(liikuntapaivakirja.getKayttajaKohdalla());
        
        laitaTilasto(liikuntasuorituksia, liikuntapaivakirja.liikuntasuorituksia(kayttajaKohdalla));
        laitaTilasto(eriLajeja, liikuntapaivakirja.eriLajeja(kayttajaKohdalla));
        laitaTilasto(liikuntaaYhteensa, liikuntapaivakirja.liikuntaAika(kayttajaKohdalla));        
    }


    @Override
    public void setDefault(Liikuntapaivakirja oletus) {
        liikuntapaivakirja = oletus;
    }
    
    
   @FXML private void handleCancel() {
       kayttajaKohdalla = null;
       ModalController.closeStage(labelVirhe);
   }
   
   
   //======================================================

   
   private Liikuntapaivakirja liikuntapaivakirja;
   private Kayttaja kayttajaKohdalla;
    
   /**
    * Asetetaan tulos 
    * @param text textField
    * @param luku luku
    */
    public void laitaTilasto(TextField text, int luku) {
       if (text == null) return; 
       String tulos = String.format("%s", luku);
       text.setText(tulos);
       
   }
     
   /**
    * Asetetaan käyttäjä
    * @param kayttaja asetettava käyttäjä
    */
    private void setKayttaja(Kayttaja kayttaja) {
        kayttajaKohdalla = kayttaja;
       }
    
   
  /**
   * Tilastojen laskeminen
   * @param stage stage
   * @param oletus olettus
   * @param kayttaja kayttaja
   * @return palauttaa 
   */
   public Liikuntapaivakirja laskeTilastot(Stage stage, Liikuntapaivakirja oletus, Kayttaja kayttaja) {
      return ModalController.<Liikuntapaivakirja, TilastotViewController>showModal(TilastotViewController.class.getResource("TilastotView.fxml"), 
              "Tilastot", stage, oletus, ctrl -> ctrl.setKayttaja(kayttaja));

   }

   

}

