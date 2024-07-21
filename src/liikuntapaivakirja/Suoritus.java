package liikuntapaivakirja;

import java.io.OutputStream;
import java.io.PrintStream;
import fi.jyu.mit.ohj2.Mjonot;

/**
 * Tietää suorituksen kentät (laji, pvm yms.)
 * osaa muuttaa suorituskenttien syntaksin
 * merkkijonon muuttaminen suoritustiedoiksi
 * osaa antaa merkkijonona tietyn kentän tiedot
 * osaa laittaa merkkijonon tiettyyn kenttään
 * @author kaisa
 * @version Apr 23, 2021
 *
 */
public class Suoritus {
    private int tunnusNro;
    private int kayttajaNro;
    private String laji;
    private String kesto;
    private String pvm;
    private String taso;
    private String lisatiedot;
    
    private static int seuraavaNro = 1;
    
    
    /**
     * alustetaan tietyn käyttäjän suoritus
     * @param kayttajaNro käyttäjän viitenro
     */
    public Suoritus(int kayttajaNro) {
        this.kayttajaNro = kayttajaNro;
    }
    
    
    /**
     * alustus
     */
    public Suoritus() {
        // 
    }


    /**
     * arvotaan satunnainen kokonaisluku välille [ala,yla]
     * @param ala arvonnan alaraja
     * @param yla arvonnan yläraja
     * @return satunnainen luku väliltä [ala,yla] 
     */
    public static int rand(int ala, int yla) {
        double n = (yla-ala)*Math.random() + ala;
        return (int)Math.round(n);
    }
    
    
    /**
     * testiarvot suoritukselle
     * 
     * @param nro viite käyttäjään, jonka suoritus
     */
    public void taytaHiihto(int nro) {
        kayttajaNro = nro;
        laji = "Maastohiihto";
        kesto = "60";
        pvm = "1.1.2021";
        taso = "keskitaso";
        lisatiedot = "Käsiin sattui";
    }
    
    
    /**
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(laji + " " + kesto + " min " + pvm +  " " + taso + " " + lisatiedot);
    }
    
    
    /**
     * Tulostetaan tiedot
     * @param os tietovirta johon tulostetaan 
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    
    /**
     * Suoritukselle seuraava rekisterinumero
     * @return suorituksen uusi tunnusNro
     * @example
     * <pre name="test">
     * Suoritus suor1 = new Suoritus();
     * suor1.getTunnusNro() === 0;
     * suor1.rekisteroi();
     * Suoritus suor2 = new Suoritus();
     * suor2.rekisteroi();
     * int n1 = suor1.getTunnusNro();
     * int n2 = suor2.getTunnusNro();
     * n2 === n1 +1;
     * </pre>
     */
    public int rekisteroi() {
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }
    
    
    /**
     * Palautetaan suorituksen id
     * @return suorituksen id
     */
    public int getTunnusNro() {
        return tunnusNro;
    }
    
    
    /**
     * Palautetaan mille käyttäjälle suoritus kuuluu
     * @return käyttäjän id
     */
    public int getKayttajaNro() {
        return kayttajaNro;
    }
    
    
    /**
     * Asettaa tunnusnumeron ja samalla varmistaa että
     * seuraava numero on aina suurempi kuin tähän mennessä suurin.
     * @param nr asetettava tunnusnumero
     */
    private void setTunnusNro(int nr) {
        tunnusNro = nr;
        if ( tunnusNro >= seuraavaNro ) seuraavaNro = tunnusNro + 1;
    }


    /**
     * Palauttaa suorituksen tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return suoritus tolppaeroteltuna merkkijonona 
     * @example
     * <pre name="test">
     *   Suoritus suoritus = new Suoritus();
     *   suoritus.parse("   2   |  10  |   Maastohiihto  | 120 | 1.1.2021 | keskitaso | käsiin sattui");
     *   suoritus.toString()    === "2|10|Maastohiihto|120|1.1.2021|keskitaso|käsiin sattui";
     * </pre>
     */
    @Override
    public String toString() {
        return "" + getTunnusNro() + "|" + kayttajaNro + "|" + laji + "|" + kesto + "|" + pvm + "|" + taso + "|" + lisatiedot ;
    }


    /**
     * Selvitää suorituksen tiedot | erotellusta merkkijonosta.
     * Pitää huolen että seuraavaNro on suurempi kuin tuleva tunnusnro.
     * @param rivi josta suorituksen tiedot otetaan
     * @example
     * <pre name="test">
     *   Suoritus suoritus = new Suoritus();
     *   suoritus.parse("   2   |  10  |   Maastohiihto  | 225 | 1.1.2021 | keskitaso | käsiin sattui ");
     *   suoritus.getKayttajaNro() === 10;
     *   suoritus.toString()    === "2|10|Maastohiihto|225|1.1.2021|keskitaso|käsiin sattui";
     *   
     *   suoritus.rekisteroi();
     *   int n = suoritus.getTunnusNro();
     *   suoritus.parse(""+(n+20));
     *   suoritus.rekisteroi();
     *   suoritus.getTunnusNro() === n+20+1;
     *   suoritus.toString()     === "" + (n+20+1) + "|10|Maastohiihto|225|1.1.2021|keskitaso|käsiin sattui";
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro()));
        kayttajaNro = Mjonot.erota(sb, '|', kayttajaNro);
        laji = Mjonot.erota(sb, '|', laji);
        kesto = Mjonot.erota(sb, '|', kesto);
        pvm = Mjonot.erota(sb, '|', pvm);
        taso = Mjonot.erota(sb, '|', taso);
        lisatiedot = Mjonot.erota(sb, '|', lisatiedot);   
    }
    
    
    /**
     * Palautetaan suorituksen kesto
     * @return kesto
     */
    public String getKesto() {
        return kesto;
    }


    /**
     * Palautetaan laji
     * @return laji
     */
    public String getLaji() {
        return laji;
    }


    /**
     * Palautetaan suoritus päivämäärä
     * @return pvm
     */
    public String getPvm() {
        return pvm;
    }
    
    
    /**
     * Asetetaan laji
     * @param s laji
     * @return null
     */
    public String setLaji(String s) {
        laji = s;
        return null;
    }


    /**
     * Asetetaan kesto
     * @param s kesto
     * @return null
     */
    public String setKesto(String s) {
       kesto = s;
       return null;
    }


    /**
     * Asetetaan päivämäärä
     * @param s päivämäärä
     * @return null
     */
    public String setPvm(String s) {
        if (!s.matches("[0-9]*")) return "Tarkasta oikeellisuus!";
        pvm = s;
        return null;
    }

    
    /**
     * Testiohjelma
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Suoritus suor = new Suoritus();
        suor.taytaHiihto(2);
        suor.tulosta(System.out);
    }
   
}
