package fxLiikuntapaivakirja;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import liikuntapaivakirja.Kayttaja;
import liikuntapaivakirja.Liikuntapaivakirja;
import liikuntapaivakirja.SailoException;
import liikuntapaivakirja.Suoritus;

import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.*;

/**
 * @author Kaisa 
 * @version Apr 23, 2021
 *
 */
public class LiikuntapaivakirjaGUIController implements Initializable {
    
    @FXML private TextField hakuehto;
    @FXML private ComboBoxChooser<String> cbKentat;
    @FXML private Label labelVirhe;
    @FXML private ListChooser<Kayttaja> chooserKayttajat;
    @FXML private ScrollPane panelKayttaja;
    @FXML private StringGrid<Suoritus> tableSuoritukset;
    
    @FXML private TextField editNimi;
    @FXML private TextField editIka;
    @FXML private TextField editPituus;
    @FXML private TextField editPaino;
    @FXML private TextField editTavoitteet;
    
    
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();
    }

    
    /**
     * hakuehto, jolla haetaan
     */
    @FXML private void handleHakuehto() {
        hae(0);
    }
    
    
    /**
     * Käsitellään uuden profiilin lisääminen
     */
    @FXML private void handleUusiProfiili() {
        uusiKayttaja();
   
    }
    
    
    /**
     * Käsitellään uuden suorituksen lisääminen
     */
    @FXML private void handleUusiSuoritus() {
        uusiSuoritus();
    }
    
    
    /**
     * Käsitellään suorituksen muokkaaminen
     */
    @FXML private void handleMuokkaaSuoritus() {
        muokkaa();
    }
    
    
    /**
     * Käsitellään profiilin muokkaaminen
     */
    @FXML private void handleMuokkaaProfiili() {
        muokkaa();
    }
    
    
    /**
     * Käsitellään tallennuskäsky
     */
    @FXML private void handleTallenna() {
        tallenna();
    }
    
    
    /**
     * Käsitellään poistokäsky
     */
    @FXML private void handlePoistaKayttaja() {
        poistaKayttaja();
    }
    
    
    /**
     * Käsitellään poistokäsky
     */
    @FXML private void handlePoistaSuoritus() {
        poistaSuoritus();
    }
    
    
    /**
     * Käsitellään apua, joka avaa ohjeen selaimeen
     */
    @FXML private void handleApua() {
        apua();
    }
    
    
    /**
     * Käsitellään lopetuskäsky
     */
    @FXML private void handleLopeta() {
        tallenna();
        Platform.exit();
    }
    
    
    /**
     * Lasketaan käyttäjälle tilastot suorituksista
     */
    @FXML private void handleTilastot() {
        laskeTilastot();
    }
    
    
    /**
     * Näyttää ohjelman tiedot
     */
    @FXML private void handleTietoja() {
        ModalController.showModal(LiikuntapaivakirjaGUIController.class.getResource("TietojaView.fxml"), "Liikuntapäiväkirja", null, "");
    }

    
    //========================================================================================================
    
    private Liikuntapaivakirja liikuntapaivakirja;
    private Kayttaja kayttajaKohdalla;
    private TextField[] edits;
    
    
    private void alusta() {
        panelKayttaja.setFitToHeight(true);
        chooserKayttajat.clear();
        chooserKayttajat.addSelectionListener(e -> naytaKayttaja());      
        
        edits = new TextField[]{editNimi, editIka, editPituus, editPaino, editTavoitteet};
    }
    
    
    /**
     * Näyttää virheen kun jotain ei osata tehdä
     */
    @SuppressWarnings("unused")
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
     * Tietojen tallennus
     */
    private String tallenna() {
        try {
            liikuntapaivakirja.tallenna();
            return null;
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Tallennuksessa ongelmia!" + ex.getMessage());
            return ex.getMessage();
        }  
    }
    
    
    /**
     * Tarkistetaan onko tallennus tehty
     * @return true jos saa sulkea sovelluksen, false jos ei
     */
    public boolean voikoSulkea() {
        tallenna();
        return true;
    }
    
    
    /**
     * näytetään käyttäjän tiedot
     */
    private void naytaKayttaja() {
        kayttajaKohdalla = chooserKayttajat.getSelectedObject();
        if (kayttajaKohdalla == null) return;
        ProfiiliViewController.naytaKayttaja(edits, kayttajaKohdalla);
        naytaSuoritukset(kayttajaKohdalla);
    }
    
    
    /**
     * Tietojen muokkaaminen
     */
    private void muokkaa() {
        if (kayttajaKohdalla == null) return; 
        try {
            Kayttaja kayttaja;
            kayttaja = ProfiiliViewController.kysyKayttaja(null, kayttajaKohdalla.clone());
            if (kayttaja == null) return;
            liikuntapaivakirja.korvaaTaiLisaa(kayttaja);
            hae(kayttaja.getTunnusNro());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        } catch (SailoException e) {
            e.printStackTrace();
        }   
    }
    
    
    /**
     * Suoritusten näyttäminen
     * @param kayttaja käyttäjä jonka suoritukset
     */
    private void naytaSuoritukset(Kayttaja kayttaja) {
        tableSuoritukset.clear();
        if (kayttaja == null) return;
        try {
            List<Suoritus> suoritukset = liikuntapaivakirja.annaSuoritukset(kayttaja);
            if (suoritukset.size() == 0) return;
            for (Suoritus suor : suoritukset) 
                naytaSuoritus(suor);
        } catch (SailoException e) {
            // naytaVirhe(e.getMessage());
        }   
    }
    
    
    /**
     * Suoritukset, toteutettu huonosti
     * @param suor suoritus
     */
    private void naytaSuoritus(Suoritus suor) {
        String[] rivi = suor.toString().split("\\|");
        tableSuoritukset.add(suor, rivi[2], rivi[4], rivi[3]);
    }
    
    
    /**
     * Hakee käyttäjien tiedot alustaan
     * @param knro käyttäjän numero, aktivoidaan haun jälkeen  
     */
    private void hae(int knro) {
        int k = cbKentat.getSelectionModel().getSelectedIndex();
       
        String ehto = hakuehto.getText();
        
        
        chooserKayttajat.clear();
        
        int indeksi = 0;
        Collection<Kayttaja> kayttajat;
        try {
            kayttajat = liikuntapaivakirja.etsi(ehto, k);
            int i = 0;
            for (Kayttaja kayttaja : kayttajat) {
                if (kayttaja.getTunnusNro() == knro) indeksi = i;
                chooserKayttajat.add(kayttaja.getNimi(), kayttaja);
                i++;
            }
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Käyttäjän hakemisessa ongelmia! " + ex.getMessage());
        }
        chooserKayttajat.setSelectedIndex(indeksi); 
    }
    
    
    /**
     * hakee aikaisemmin tallenetut tiedot
     */
    public void haeAlussa() {
        chooserKayttajat.clear();
        for (int i = 0; i < liikuntapaivakirja.getKayttajia(); i++) {
            Kayttaja kayttaja = liikuntapaivakirja.annaKayttaja(i);
            chooserKayttajat.add(kayttaja.getNimi(), kayttaja) ;
        }
    }
    
    
    /**
     * Lisätään uusi käyttäjä
     */
    private void uusiKayttaja() {
        Kayttaja uusi = new Kayttaja();
        uusi = ProfiiliViewController.kysyKayttaja(null, uusi);  
        if ( uusi == null ) return;
        uusi.rekisteroi();
        liikuntapaivakirja.lisaa(uusi);
        hae(uusi.getTunnusNro());
    }
    
    
    /**
     * Lisätään uusi tyhjä suoritus
     */
    public void uusiSuoritus() {
        if (kayttajaKohdalla == null) return;
        try {
            Suoritus uusi = new Suoritus(kayttajaKohdalla.getTunnusNro());
            uusi = SuoritusViewController.kysySuoritus(null, uusi); 
            if ( uusi == null ) return;
            uusi.rekisteroi();
            liikuntapaivakirja.lisaa(uusi);
            naytaSuoritukset(kayttajaKohdalla); 
            haeAlussa();
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Lisääminen epäonnistui: " + e.getMessage());
        }
        
    }
    
    
    /**
     * Asetetaan liikuntapäiväkirja
     * @param liikuntapaivakirja jota käytetään
     */
    public void setLiikuntapaivakirja(Liikuntapaivakirja liikuntapaivakirja) {
        this.liikuntapaivakirja = liikuntapaivakirja;
        naytaKayttaja();
    }
    
    
    /**
     * Avataan ohjelman käyttöohje selaimessa, eli harjoitustyön suunnitelma kurssisivulla
     */
    private void apua() {
        Desktop desktop = Desktop.getDesktop();
        try {
            URI uri = new URI("https://tim.jyu.fi/view/kurssit/tie/ohj2/2021k/ht/korhkakr");
            desktop.browse(uri);
        } catch (URISyntaxException e) {
            return;
        } catch (IOException e) {
            return;
        }
    }
    
    
    /**
     * @param os tietovirta johon tulostetaan 
     * @param kayttaja tulostettava käyttäjä
     * @throws SailoException e
     */
    public void tulosta(PrintStream os, final Kayttaja kayttaja) throws SailoException {
        os.println("-------------------------------------------");
        kayttaja.tulosta(os);
        List<Suoritus> suoritukset = liikuntapaivakirja.annaSuoritukset(kayttaja);
        for (Suoritus suor : suoritukset)
            suor.tulosta(os);
        os.println("-------------------------------------------");
        
    }
    
    
    /**
     * Käyttäjän tietojen poisto
     */
    private void poistaKayttaja() {
        Kayttaja kayttaja = kayttajaKohdalla;
        if ( kayttaja == null ) return;
        if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko kayttaja: " + kayttaja.getNimi(), "Kyllä", "Ei") )
            return;
        liikuntapaivakirja.poista(kayttaja);
        int index = chooserKayttajat.getSelectedIndex();
        haeAlussa();
        chooserKayttajat.setSelectedIndex(index);
    }
    
    
    /**
     * Poistetaan valittu suoritus. 
     */
    private void poistaSuoritus() {
        int rivi = tableSuoritukset.getRowNr();
        if ( rivi < 0 ) return;
        Suoritus suoritus = tableSuoritukset.getObject();
        if ( suoritus == null ) return;
        liikuntapaivakirja.poistaSuoritus(suoritus);
        naytaSuoritukset(kayttajaKohdalla);
        int harrastuksia = tableSuoritukset.getItems().size(); 
        if ( rivi >= harrastuksia ) rivi = harrastuksia -1;
        tableSuoritukset.getFocusModel().focus(rivi);
        tableSuoritukset.getSelectionModel().select(rivi);
    }
    

    /**
     * Tilastojen laskeminen
     */
    private void laskeTilastot() {
        liikuntapaivakirja.setKayttajaKohdalla(kayttajaKohdalla);
        ModalController.showModal(LiikuntapaivakirjaGUIController.class.getResource("TilastotView.fxml"), "Tilastot", null, liikuntapaivakirja);
    }
 
}

