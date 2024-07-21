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
import liikuntapaivakirja.Suoritus;

/**
 * Käsitellään suoritusdialogin tapahtumat.
 * @author kaisa
 * @version Apr 23, 2021
 *
 */
public class SuoritusViewController implements ModalControllerInterface<Suoritus>, Initializable{

    @FXML private Label labelVirhe;
    @FXML private TextField editLaji;
    @FXML private TextField editKesto;
    @FXML private TextField editPvm;
    
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alusta();
    }

    
    @Override
    public Suoritus getResult() {
        return suoritusKohdalla;
    }

    @Override
    public void handleShown() {
        editLaji.requestFocus();
        
    }

    @Override
    public void setDefault(Suoritus oletus) {
        suoritusKohdalla = oletus;
        naytaSuoritus(suoritusKohdalla);
        
    }
    
    
    @FXML private void handleOK() {
        if (suoritusKohdalla !=null && suoritusKohdalla.getLaji().trim().equals("")) {
            naytaVirhe("Laji ei saa olla tyhjä");
            return;
        }
        ModalController.closeStage(labelVirhe);;
    }
    

    @FXML private void handleCancel() {
        suoritusKohdalla = null;
        ModalController.closeStage(labelVirhe);
    }
    
    
    
    //================================================
    private Suoritus suoritusKohdalla;
    private TextField[] edits;
    
    
    private void alusta() {
        edits = new TextField[]{editLaji, editKesto, editPvm};
        int i = 0;
        for(TextField edit : edits) {
            final int k = ++i;
            edit.setOnKeyReleased(e -> kasitteleMuutosSuoritukseen(k, (TextField)(e.getSource())));
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
    
    
    private void kasitteleMuutosSuoritukseen(int k, TextField edit) {
        if (suoritusKohdalla == null) return;
        String s = edit.getText();
        String virhe = null;
        switch (k) {
        
            case 1 : virhe = suoritusKohdalla.setLaji(s); break;
            case 2 : virhe = suoritusKohdalla.setKesto(s); break;
            case 3 : virhe = suoritusKohdalla.setPvm(s); break;
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
    
    
    private void naytaSuoritus(Suoritus suoritus) {
        naytaSuoritus(edits, suoritus);
    }
    
    
    /**
     * Käyttäjän tiedot TextFieldeihin
     * @param edits taulukko jossa tekstikenttiä
     * @param suoritus näytettävä käyttäjä
     */
    public static void naytaSuoritus(TextField[] edits, Suoritus suoritus) {
        if (suoritus == null) return;
        
        edits[0].setText(suoritus.getLaji());
        edits[1].setText(suoritus.getKesto());
        edits[2].setText(suoritus.getPvm());
    }



    /**
     * @param modalityStage mille ollaan modaalisia, null=sovellukselle
     * @param oletus mitä näytetään oletuksena
     * @return null jos cancel, muuten 
     */
    public static Suoritus kysySuoritus(Stage modalityStage, Suoritus oletus) {
        return ModalController.showModal(LiikuntapaivakirjaGUIController.class.getResource("SuoritusView.fxml"), "Suoritus", modalityStage, oletus);
    }
    

}
