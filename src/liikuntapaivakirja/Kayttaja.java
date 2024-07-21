package liikuntapaivakirja;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * tietää käyttäjän kentät
 * osaa tarkastaa kenttien syntaksin
 * osaa muttaa merkkijonon käyttäjän tiedoiksi
 * osaa antaa merkkijonona tietyn kentän tiedot
 * osaa laittaa merkkijonon tiettyyn kenttään
 * @author kaisa
 * @version  Apr 23, 2021
 *
 */
public class Kayttaja implements Cloneable, Comparable<Kayttaja> {
    
    private int         tunnusNro;
    private String      nimi            = "";
    private String      ika             = "";
    private String      pituus          = "";
    private String      paino           = "";
    private String      tavoitteet      = "";
    
    private static int  seuraavaNro     = 1;
    
     
    /**
     * @return palauttaa käyttäjän tunnusnumeron (varmistetaan että luodaan varmasti eri käyttäjiä)
     */
    public int getTunnusNro() {
        return tunnusNro;
    }
    
    
    /**
     * @return palauttaa käyttäjän nimen
     */
    public String getNimi() {
        return nimi;
    }
    
    
    /**
     * @return palauttaa kayttajan iän
     */
    public String getIka() {
        return ika;
    }


    /**
     * @return palauttaa käyttäjän pituuden
     */
    public String getPituus() {
        return pituus;
    }


    /**
     * @return palauttaa käyttäjän painon
     */
    public String getPaino() {
        return paino;
    }


    /**
     * @return paluttaa käyttäjän tavoitteet
     */
    public String getTavoitteet() {
        return tavoitteet;
    }
    
    
    /**
     * @param s nimi
     * @return virheilmoitus, null jos ok
     */
    public String setNimi(String s) {
        nimi = s;
        return null;
    }
    
    
    /**
     * @param s ika
     * @return virheilmoitus, null jos ok
     */
    public String setIka(String s) {
        if (!s.matches("[0-9]*")) return "Tarkasta ikä!";
        ika = s;
        return null;
    }
    
    
    /**
     * @param s pituus
     * @return virheilmoitus, null jos ok
     */
    public String setPituus(String s) {
        if (!s.matches("[0-9]*")) return "Tarkasta pituus!";
        pituus = s;
        return null;
    }
    
    
    /**
     * @param s paino 
     * @return virheilmoitus, null jos ok
     */
    public String setPaino(String s) {
        if (!s.matches("[0-9]*")) return "Tarkasta paino!";
        paino = s;
        return null;
    }
    
    
    /**
     * @param s tavoitteet
     * @return virheilmoitus, null jos ok
     */
    public String setTavoitteet(String s) {
        tavoitteet = s;
        return null;
    }

    
    /**
     * tulostetaan käyttäjän tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("%03d", tunnusNro) +  " " + nimi + " " + ika);
        out.println(" " + pituus + " " + paino);
        out.println(" " + tavoitteet);
    }
    
    
    /**
     * tulostetaan käyttäjän tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    
    /**
     * antaa jäsenelle rekisterinumeron
     * @return palauttaa uuden kayttäjän tunnusnumeron
     * @example
     * <pre name="test">
     *   Kayttaja maija1 = new Kayttaja();
     *   maija1.getTunnusNro() === 0;
     *   maija1.rekisteroi();
     *   Kayttaja maija2 = new Kayttaja();
     *   maija2.rekisteroi();
     *   int n1 = maija1.getTunnusNro();
     *   int n2 = maija2.getTunnusNro();
     *   n2 === n1+1;
     *   </pre>
     */
    public int rekisteroi() {
        this.tunnusNro = seuraavaNro;
        seuraavaNro++;
        return this.tunnusNro;
    }
    
    /**
     * Antaa k:n kentän sisällön merkkijonona
     * @param k monenenko kentän sisältö palautetaan
     * @return kentän sisältö merkkijonona
     */
    public String anna(int k) {
        switch ( k ) {
        case 0: return "" + nimi;
        case 1: return "" + ika;
        case 2: return "" + pituus;
        case 3: return "" + paino;
        case 4: return "" + tavoitteet;
        default: return "";
        }
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
     * Palajuttaa käyttäjän tiedot merkkijonona joka voidaan tallentaa tiedostoon
     * @return kayttäjän merkkijonona
     * @example
     * * <pre name="test">
     *   Kayttaja kayttaja = new Kayttaja();
     *   kayttaja.parse("   1  |   Maija Mehiläinen   | 25");
     *   kayttaja.toString().startsWith("1|Maija Mehiläinen|25|") === true; 
     * </pre>  
     */
    @Override
    public String toString() {
        return "" +
                getTunnusNro() + "|" +
                nimi + "|" +
                ika + "|" +
                pituus + "|" +
                paino + "|" +
                tavoitteet;
    }
    
    
    /**
     * @param rivi josta tiedot otetaan 
     * @example
     * <pre name="test">
     * Kayttaja kayttaja = new Kayttaja();
     * kayttaja.parse(" 3 | Maija Mehiläinen | 25");
     * kayttaja.getTunnusNro() === 3;
     * kayttaja.toString().startsWith("3|Maija Mehiläinen|25|") === true;
     * 
     * kayttaja.rekisteroi();
     * int n = kayttaja.getTunnusNro();
     * kayttaja.parse(""+(n+20));
     * kayttaja.rekisteroi();
     * kayttaja.getTunnusNro() === n+20+1;
     * </pre>
     * 
     */
    public void parse(String rivi) {
        var sb = new StringBuilder(rivi);
        setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro()));
        nimi = Mjonot.erota(sb, '|', nimi );
        ika = Mjonot.erota(sb, '|', ika );
        pituus = Mjonot.erota(sb, '|', pituus );
        paino = Mjonot.erota(sb, '|', paino );
        tavoitteet = Mjonot.erota(sb, '|', tavoitteet );
    }
    
    
    /**
     * Asetetaan tunnusnumero
     * @param nro asetettava numero
     */
    private void setTunnusNro(int nro) {
        tunnusNro = nro;
        if (tunnusNro >= seuraavaNro) seuraavaNro = tunnusNro + 1;
        
    }


    /**
     * Apumetodi, jolla täytetään testitiedot käyttäjälle
     */
    public void taytaMaijaMehilainenTiedoilla() {
        nimi = "Maija Mehiläinen" + " " + rand(1000, 9999);
        ika = "25";
        pituus = "165";
        paino = "55";
        tavoitteet = "kehittää kuntoa";
    }
    
    
    /**
     * Tehdään klooni käyttäjästä
     * @return Object kloonattu kayttaja
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException 
     *   Kayttaja kayttaja = new Kayttaja();
     *   kayttaja.parse("   3  |  Mehiläinen Maija   | 25");
     *   Kayttaja kopio = kayttaja.clone();
     *   kopio.toString() === kayttaja.toString();
     *   kayttaja.parse("   4  |  Hopo Hessu   | 25");
     *   kopio.toString().equals(kayttaja.toString()) === false;
     * </pre>
     */
    @Override
    public Kayttaja clone() throws CloneNotSupportedException {
        Kayttaja uusi;
        uusi = (Kayttaja) super.clone();
        return uusi;
    }
    
    
    /**
     * Verrataan käyttajiä
     */
    @Override
    public int compareTo(Kayttaja kayttaja) {
        return nimi.compareTo(kayttaja.nimi);
    }
    
    
   
    
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Kayttaja maija = new Kayttaja();
        Kayttaja maija2 = new Kayttaja();
        
        maija.rekisteroi();
        maija2.rekisteroi();
        
        maija.tulosta(System.out);
        maija.taytaMaijaMehilainenTiedoilla();
        maija.tulosta(System.out);
        
        maija2.tulosta(System.out);
        maija2.taytaMaijaMehilainenTiedoilla();
        maija2.tulosta(System.out);

    }




}
