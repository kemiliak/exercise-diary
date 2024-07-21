package fxLiikuntapaivakirja;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import liikuntapaivakirja.Kayttaja;

/**
 * Käsitellään käyttäjän lisäämiseen/muokkaamiseen vaadittavat.
 * @author kaisa
 * @version Apr 23, 2021
 *
 */
public class ProfiiliViewController implements ModalControllerInterface<Kayttaja>, Initializable {

    @FXML private Label labelVirhe;
    @FXML private TextField editNimi;
    @FXML private TextField editIka;
    @FXML private TextField editPituus;
    @FXML private TextField editPaino;
    @FXML private TextField editTavoitteet;
    
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alusta();
    }

    @Override
    public Kayttaja getResult() {
        return kayttajaKohdalla;
    }

    @Override
    public void handleShown() {
        editNimi.requestFocus();
    }

    @Override
    public void setDefault(Kayttaja oletus) {
        kayttajaKohdalla = oletus;
        naytaKayttaja(kayttajaKohdalla);
    }
    
    
    @FXML private void handleOK() {
        if (kayttajaKohdalla !=null && kayttajaKohdalla.getNimi().trim().equals("")) {
            naytaVirhe("Nimi ei saa olla tyhjä");
            return;
        }
        ModalController.closeStage(labelVirhe);;
    }
    
    
    @FXML private void handleCancel() {
        kayttajaKohdalla = null;
        ModalController.closeStage(labelVirhe);
    }

    
    //====================================================================
    
    private Kayttaja kayttajaKohdalla;
    private TextField[] edits;
    
    
    /**
     * Alustetaan tarvittavat
     */
    private void alusta() {
        edits = new TextField[]{editNimi, editIka, editPituus, editPaino, editTavoitteet};
        int i = 0;
        for(TextField edit : edits) {
            final int k = ++i;
            edit.setOnKeyReleased(e -> kasitteleMuutosKayttajaan(k, (TextField)(e.getSource())));
        }
    }
    
    
    /**
     * Näyttää virheen kun jokin menee pieleen
     */
    private void naytaVirhe(String virhe) {
        if (virhe == null || virhe.isEmpty()) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }
    
    
    /**
     * Käsitellään muutos 
     * @param k k
     * @param edit textfield muuttunut
     */
    private void kasitteleMuutosKayttajaan(int k, TextField edit) {
        if (kayttajaKohdalla == null) return;
        String s = edit.getText();
        String virhe = null;
        switch (k) {
            case 1 : virhe = kayttajaKohdalla.setNimi(s); break;
            case 2 : virhe = kayttajaKohdalla.setIka(s); break;
            case 3 : virhe = kayttajaKohdalla.setPituus(s); break;
            case 4 : virhe = kayttajaKohdalla.setPaino(s); break;
            case 5 : virhe = kayttajaKohdalla.setTavoitteet(s); break;
            default:
        }
        if (virhe == null) {
            Dialogs.setToolTipText(edit, "");
            edit.getStyleClass().removeAll("virhe");
            naytaVirhe(virhe);
        } else {
            Dialogs.setToolTipText(edit, virhe);
            edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        }
    }
    
    
    /**
     * Näytetään käyttäjän tiedot
     * @param kayttaja käyttääjä
     */
    private void naytaKayttaja(Kayttaja kayttaja) {
        naytaKayttaja(edits, kayttaja);
    }
    
    
    /**
     * Käyttäjän tiedot TextFieldeihin
     * @param edits taulukko jossa tekstikenttiä
     * @param kayttaja näytettävä käyttäjä
     */
    public static void naytaKayttaja(TextField[] edits, Kayttaja kayttaja) {
        if (kayttaja == null) return;
        edits[0].setText(kayttaja.getNimi());
        edits[1].setText(kayttaja.getIka());
        edits[2].setText(kayttaja.getPituus());
        edits[3].setText(kayttaja.getPaino());
        edits[4].setText(kayttaja.getTavoitteet());
    }

    
    /**
     * Käyttäjän kysymisdialogi 
     * @param modalityStage mille ollaan modaalisia, null=sovellukselle
     * @param oletus mitä näytetään oletuksena
     * @return null jos cancel, muuten 
     */
    public static Kayttaja kysyKayttaja(Stage modalityStage, Kayttaja oletus) {
        return ModalController.showModal(LiikuntapaivakirjaGUIController.class.getResource("ProfiiliView.fxml"), "Kayttaja", modalityStage, oletus);
        
    }
    

}    
