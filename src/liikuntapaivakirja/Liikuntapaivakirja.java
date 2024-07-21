package liikuntapaivakirja;

import java.util.Collection;
import java.util.List;

/**
 * @author kaisa
 * @version Apr 9, 2021
 *
 */
/**
 * huolehtii luokkien välisestä yhteistyöstä ja niiden tietojen välittämisesetä
 * vastaa tilastoista
 * @author kaisa
 * @version  Apr 23, 2021
 *
 */
public class Liikuntapaivakirja {
    
    private Kayttajat kayttajat = new Kayttajat();
    private Suoritukset suoritukset = new Suoritukset();
    private Kayttaja kayttajaKohdalla = new Kayttaja();
    private final String tiedosto = "kayttajat.dat";
    
    
    /**
     * lisätään uusi käyttäjä
     * @param kayttaja uusi käyttäjä
     * @example
     * <pre name="test">
     *  Liikuntapaivakirja paivakirja = new Liikuntapaivakirja();
     *  Kayttaja maija1 = new Kayttaja(), maija2 = new Kayttaja();
     *  maija1.rekisteroi(); maija2.rekisteroi();
     *  paivakirja.getKayttajia() === 0;
     *  paivakirja.lisaa(maija1); paivakirja.getKayttajia() === 1;
     *  paivakirja.lisaa(maija2); paivakirja.getKayttajia() === 2;
     *  paivakirja.lisaa(maija1); paivakirja.getKayttajia() === 3;
     *  paivakirja.getKayttajia() === 3;
     *  paivakirja.annaKayttaja(0) === maija1;
     *  paivakirja.annaKayttaja(1) === maija2;
     *  paivakirja.annaKayttaja(2) === maija1;
     *  paivakirja.annaKayttaja(3) === maija1; #THROWS IndexOutOfBoundsException
     *  paivakirja.lisaa(maija1); paivakirja.getKayttajia() === 4;
     *  paivakirja.lisaa(maija1); paivakirja.getKayttajia() === 5;
     *  paivakirja.lisaa(maija1); 
     *  </pre>
     */
    public void lisaa(Kayttaja kayttaja)  {
        kayttajat.lisaa(kayttaja);
        
    }
    
    
    /**
     * Korvaa käyttäjän tietorakenteessa
     * @param kayttaja liättävän käyttäjän viite
     * @throws SailoException jos
     */
    public void korvaaTaiLisaa(Kayttaja kayttaja) throws SailoException {
        kayttajat.korvaaTaiLisaa(kayttaja);
    }
    
     
    /**
     * lisätään uusi suoritus
     * @param suor lisättävä suoritus
     * @throws SailoException jos ongelmia
     */
    public void lisaa(Suoritus suor) throws SailoException {
        suoritukset.lisaa(suor); 
    }
    
    
    /**
     * Palauttaa käyttäjien viitteet "taulukkoon", hakuehdolla 
     * @param hakuehto hakuehto
     * @param k etsittävän kentän indeksi
     * @return tietorakenne löytyneistä käyttäjistä
     * @throws SailoException jos jotakine menee pieleen
     */
    public Collection<Kayttaja> etsi(String hakuehto, int k) throws SailoException {
        return kayttajat.etsi(hakuehto, k);
    }
    
    
    /**
     * Palauttaa käyttäjien määrän
     * @return kayttäjien lukumäärä
     */
    public int getKayttajia() {
        
        return this.kayttajat.getLkm();
    }
    
    /**
     * Palauttaa käyttäjän kohdalla
     * @return palauttaa kayttajan kohdalla
     */
    public Kayttaja getKayttajaKohdalla() {
        return kayttajaKohdalla;
    }
    
    
    /**
     * Asettaa käyttäjän kohtaan
     * @param kayttaja asetetaan käyttäjä
     */
    public void setKayttajaKohdalla(Kayttaja kayttaja) {
        this.kayttajaKohdalla = kayttaja;
    }
    
    
    /**
     * Liikuntapäiväkirjan i:n käyttäjän 
     * @param i monesko jäsen
     * @return viite i käyttäjään
     * @throws IndexOutOfBoundsException jos i väärin
     */
    public Kayttaja annaKayttaja(int i) throws IndexOutOfBoundsException {
        return kayttajat.anna(i);
    }
    
    
    /**
     * @param kayttaja jonka suoritukset haetaan
     * @return tietorakenteet jossa viitteet löydettyihin suorituksiin
     * @throws SailoException jos tulee ongelmia
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.util.*;
     * 
     *  Liikuntapaivakirja liikuntapaivakirja = new Liikuntapaivakirja();
     *  Kayttaja maija1 = new Kayttaja(), maija2 = new Kayttaja(), maija3 = new Kayttaja();
     *  maija1.rekisteroi(); maija2.rekisteroi(); maija3.rekisteroi();
     *  int id1 = maija1.getTunnusNro();
     *  int id2 = maija2.getTunnusNro();
     *  Suoritus suor1 = new Suoritus(id1); liikuntapaivakirja.lisaa(suor1);
     *  Suoritus suor2 = new Suoritus(id1); liikuntapaivakirja.lisaa(suor2);
     *  Suoritus suor3 = new Suoritus(id2); liikuntapaivakirja.lisaa(suor3);
     *  Suoritus suor4 = new Suoritus(id2); liikuntapaivakirja.lisaa(suor4);
     *  Suoritus suor5 = new Suoritus(id2); liikuntapaivakirja.lisaa(suor5);
     *  
     *  List<Suoritus> loytyneet;
     *  loytyneet = liikuntapaivakirja.annaSuoritukset(maija3);
     *  loytyneet.size() === 0; 
     *  loytyneet = liikuntapaivakirja.annaSuoritukset(maija1);
     *  loytyneet.size() === 2; 
     *  loytyneet.get(0) == suor1 === true;
     *  loytyneet.get(1) == suor2 === true;
     *  loytyneet = liikuntapaivakirja.annaSuoritukset(maija2);
     *  loytyneet.size() === 3; 
     *  loytyneet.get(0) == suor3 === true;
     * </pre> 
     */
    public List<Suoritus> annaSuoritukset(Kayttaja kayttaja) throws SailoException {
        return suoritukset.annaSuoritukset(kayttaja.getTunnusNro());
    }

    
    
    /**
     * asetetaan tiedostojen perusnimet
     */
    public void setTiedosto() {
        kayttajat.setTiedostonPerusNimi("kayttajat");
        suoritukset.setTiedostonPerusNimi("suoritukset");
    }
    

    /**
     * lukee tiedostosta
     * @throws SailoException jos lukeminen epäonnistuu
     * * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.*;
     * #import java.util.*;
     * 
     *  Liikuntapaivakirja liikuntapaivakirja = new Liikuntapaivakirja();
     *  
     *  Kayttaja maija1 = new Kayttaja(); maija1.taytaMaijaMehilainenTiedoilla(); maija1.rekisteroi();
     *  Kayttaja maija2 = new Kayttaja(); maija2.taytaMaijaMehilainenTiedoilla(); maija2.rekisteroi();
     *  Suoritus suor1 = new Suoritus(); suor1.taytaHiihto(maija2.getTunnusNro());
     *  Suoritus suor2 = new Suoritus(); suor2.taytaHiihto(maija1.getTunnusNro());
     *  Suoritus suor3 = new Suoritus(); suor3.taytaHiihto(maija2.getTunnusNro()); 
     *  Suoritus suor4 = new Suoritus(); suor4.taytaHiihto(maija1.getTunnusNro()); 
     *  Suoritus suor5 = new Suoritus(); suor5.taytaHiihto(maija2.getTunnusNro());
     *   
     *  String hakemisto = "testikirja";
     *  File dir = new File(hakemisto);
     *  File ftied  = new File(hakemisto+"/kayttajat.dat");
     *  File fhtied = new File(hakemisto+"/suoritukset.dat");
     *  dir.mkdir();  
     *  ftied.delete();
     *  fhtied.delete();
     *  liikuntapaivakirja.lueTiedostosta(); 
     *  liikuntapaivakirja.lisaa(maija1);
     *  liikuntapaivakirja.lisaa(maija2);
     *  liikuntapaivakirja.lisaa(suor1);
     *  liikuntapaivakirja.lisaa(suor2);
     *  liikuntapaivakirja.lisaa(suor3);
     *  liikuntapaivakirja.lisaa(suor4);
     *  liikuntapaivakirja.lisaa(suor5);
     *  liikuntapaivakirja.tallenna();
     *  liikuntapaivakirja = new Liikuntapaivakirja();
     *  liikuntapaivakirja.lueTiedostosta();
     *  Collection<Kayttaja> kaikki = liikuntapaivakirja.etsi("",-1); 
     *  Iterator<Kayttaja> it = kaikki.iterator();
     *  it.next() === maija1;
     *  it.next() === maija2;
     *  it.hasNext() === false;
     *  List<Suoritus> loytyneet = liikuntapaivakirja.annaSuoritukset(maija1);
     *  Iterator<Suoritus> ih = loytyneet.iterator();
     *  ih.next() === suor2;
     *  ih.next() === suor4;
     *  ih.hasNext() === false;
     *  loytyneet = liikuntapaivakirja.annaSuoritukset(maija2);
     *  ih = loytyneet.iterator();
     *  ih.next() === suor1;
     *  ih.next() === suor3;
     *  ih.next() === suor5;
     *  ih.hasNext() === false;
     *  liikuntapaivakirja.lisaa(maija2);
     *  liikuntapaivakirja.lisaa(suor5);
     *  liikuntapaivakirja.tallenna();
     *  ftied.delete()  === true;
     *  fhtied.delete() === true;
     * </pre>
     */
    public void lueTiedostosta() throws SailoException {
        kayttajat = new Kayttajat();
        suoritukset = new Suoritukset();
        
        setTiedosto();
        kayttajat.lueTiedostosta();
        suoritukset.lueTiedostosta();
        
    }
    
    
    /**
     * Tallennetaan tiedostoon
     * @throws SailoException jos tallentamisessa ongelmia
     */
    public void tallenna() throws SailoException {
        tallenna(tiedosto);
    }
    
    
    /**
     * Tallenttaa liikuntapaivakirjan tiedot tiedostoon.  
     * Vaikka kayttajien tallettamien epäonistuisi, niin yritetään silti tallettaa
     * suorituksia ennen poikkeuksen heittämistä.
     * @param tied tiedoston nimi
     * @throws SailoException jos tallettamisessa ongelmia
     */
    public void tallenna(String tied) throws SailoException {
        String virhe = "";
        try {
            kayttajat.tallenna(tied);
        } catch ( SailoException ex ) {
            virhe = ex.getMessage();
        }

        try {
            suoritukset.tallenna();
        } catch ( SailoException ex ) {
            virhe += ex.getMessage();
        }
        if ( !"".equals(virhe) ) throw new SailoException(virhe);
    }

    
    /**
     * @param suoritus poistettava
     */
    public void poistaSuoritus(Suoritus suoritus) {
        suoritukset.poista(suoritus);
        
    }


    /**
     * @param kayttaja poistettava
     * @return montako kayttajaa poistettiin
     */
    public int poista(Kayttaja kayttaja) {
        if (kayttaja == null) return 0;
        int r = kayttajat.poista(kayttaja.getTunnusNro());
        suoritukset.poistaKayttajanSuoritukset(kayttaja.getTunnusNro());
        return r;
    }
    
    /**
     * @param kayttaja k
     * @return liikuntasuoritusten lkm
     */
    public int liikuntasuorituksia(Kayttaja kayttaja) {
        return suoritukset.getKayttajaSuorituksetLKM(kayttaja.getTunnusNro());
    }
    
    
    /**
     * @param kayttaja kayttaja
     * @return lajeja lkm int
     */
    public int eriLajeja(Kayttaja kayttaja) {
        return suoritukset.getKayttajaLajitLKM(kayttaja.getTunnusNro());
    }
    
    
    /**
     * @param kayttaja kayttaja
     * @return aika yhteensä minuuteissa
     */
    public int liikuntaAika(Kayttaja kayttaja) {
        return suoritukset.getKayttajaLiikuntaAika(kayttaja.getTunnusNro());
    }
    
    
    /**
     * @param args ei käytössä
     */
    public static void main (String[] args) {
        
        Liikuntapaivakirja paivakirja = new Liikuntapaivakirja();
        
        Kayttaja maija = new Kayttaja();
        Kayttaja maija2 = new Kayttaja();
        
        maija.rekisteroi();
        maija.taytaMaijaMehilainenTiedoilla();
        maija2.rekisteroi();
        maija2.taytaMaijaMehilainenTiedoilla();
        
        paivakirja.lisaa(maija);
        paivakirja.lisaa(maija2);
         
        for (int i = 0; i< paivakirja.getKayttajia(); i++) {
            Kayttaja kayttaja = paivakirja.annaKayttaja(i);
            kayttaja.tulosta(System.out);
        }
    
    }


}
