package liikuntapaivakirja;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import fi.jyu.mit.ohj2.WildChars;

/**
 * käyttäjien lisääminen ja poisto
 * käyttäjien lukeminen ja kirjoittaminen tiedostoon
 * käyttäjien etsiminen ja lajittelu
 * @author kaisa
 * @version Apr 27, 2021
 *
 */
public class Kayttajat implements Iterable<Kayttaja>{
    
    private static final int MAX_KAYTTAJIA = 5;
    private boolean muutettu = false;
    private int lkm = 0;
    private Kayttaja[] alkiot;
    private String tiedostonPerusNimi = "";
    
    
    /**
     * Luodaan alustava taulukko
     */
    public Kayttajat() {
        alkiot = new Kayttaja[MAX_KAYTTAJIA]; 
    }
    
    
    /**
     * Lisää uuden käyttäjän
     * @param kayttaja käyttäjän viite
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Kayttajat kayttajat = new Kayttajat();
     * Kayttaja maija1 = new Kayttaja(), maija2 = new Kayttaja();
     * kayttajat.getLkm() === 0;
     * kayttajat.lisaa(maija1); kayttajat.getLkm() === 1;
     * kayttajat.lisaa(maija2); kayttajat.getLkm() === 2;
     * kayttajat.lisaa(maija1); kayttajat.getLkm() === 3;
     * kayttajat.anna(0) === maija1;
     * kayttajat.anna(1) === maija2;
     * kayttajat.anna(2) === maija1;
     * kayttajat.anna(1) == maija1 === false;
     * kayttajat.anna(1) == maija2 === true;
     * kayttajat.anna(3) === maija1; #THROWS IndexOutOfBoundsException
     * kayttajat.lisaa(maija1); kayttajat.getLkm() === 4;
     * kayttajat.lisaa(maija1); kayttajat.getLkm() === 5;
     * kayttajat.lisaa(maija1);
     * </pre>
     */
    public void lisaa(Kayttaja kayttaja) {
        if (lkm >= alkiot.length) {
            alkiot = Arrays.copyOf(alkiot, alkiot.length+ 10);
        }
        alkiot[lkm] = kayttaja;
        lkm++;
        muutettu = true;
    }
    
    
    /**
     * Korvaa kayttajan tietorakenteessa
     * Etsitään samalla tunnusnumerolla oleva kayttaja.  Jos ei löydy, niin lisätään uusi käyttäjä.
     * @param kayttaja lisätäävän kayttajan viite
     * @throws SailoException jos tietorakenne on jo täynnä
     * <pre name="test">
     * #THROWS SailoException,CloneNotSupportedException
     * #PACKAGEIMPORT
     * Kayttajat kayttajat = new Kayttajat();
     * Kayttaja maija1 = new Kayttaja(), maija2 = new Kayttaja();
     * maija1.rekisteroi(); maija2.rekisteroi();
     * kayttajat.getLkm() === 0;
     * kayttajat.korvaaTaiLisaa(maija1); kayttajat.getLkm() === 1;
     * kayttajat.korvaaTaiLisaa(maija2); kayttajat.getLkm() === 2;
     * Kayttaja maija3 = maija1.clone();
     * maija3.setPituus("170");
     * Iterator<Kayttaja> it = kayttajat.iterator();
     * it.next() == maija1 === true;
     * kayttajat.korvaaTaiLisaa(maija3); kayttajat.getLkm() === 2;
     * it = kayttajat.iterator();
     * Kayttaja k0 = it.next();
     * k0 === maija3;
     * k0 == maija3 === true;
     * k0 == maija1 === false;
     * </pre>
     */
    public void korvaaTaiLisaa(Kayttaja kayttaja) throws SailoException {
        int id = kayttaja.getTunnusNro();
        for (int i = 0; i < lkm; i++) {
            if (alkiot[i].getTunnusNro() == id) {
                alkiot[i] = kayttaja;
                muutettu = true;
                return;
            }
        }
        lisaa(kayttaja);
    }
    
    
    /**
     * palauttaa käyttäjien lukumäärän
     * @return käyttäjien lkm
     */
    public int getLkm() {
        return lkm;
    }
    
    
    /**
     * Palauttaa viitteen i jäseneen
     * @param i monennenko käyttäjän viite
     * @return viite käyttäjään indeksillä i
     * @throws IndexOutOfBoundsException  jos i ei ole sallitulla alueella
     */
    public Kayttaja anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || this.lkm <= i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }
  
    
    /**
     * Lukee käyttäjät tiedostosta
     * @param tiedosto tiedoston nimi
     * @throws SailoException jos lukeminen epäonnistuuu
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File; 
     *  Kayttajat kayttajat = new Kayttajat();
     *  Kayttaja maija1 = new Kayttaja(), maija2 = new Kayttaja();
     *  maija1.taytaMaijaMehilainenTiedoilla();
     *  maija2.taytaMaijaMehilainenTiedoilla();
     *  String tiedNimi = "kayttajia.dat";
     *  File ftied = new File(tiedNimi);
     *  ftied.delete();
     *  kayttajat.lisaa(maija1);
     *  kayttajat.lisaa(maija2);
     *  kayttajat.tallenna(tiedNimi);
     *  kayttajat = new Kayttajat();            
     *  kayttajat.lueTiedostosta(tiedNimi);  
     *  Iterator<Kayttaja> i = kayttajat.iterator();
     *  i.next().getTunnusNro() === maija1.getTunnusNro();
     *  i.next().getTunnusNro() === maija2.getTunnusNro();
     *  i.hasNext() === false;
     *  kayttajat.lisaa(maija2);
     *  kayttajat.tallenna(tiedNimi);     
     *  ftied.delete() === true;
     * </pre>
     */
    public void lueTiedostosta(String tiedosto) throws SailoException {
        
        try (Scanner fi = new Scanner(new FileInputStream(tiedosto))) {
            while (fi.hasNext()) {
                String s = "";
                s = fi.nextLine();
                
                Kayttaja kayttaja = new Kayttaja();
                kayttaja.parse(s);
                
                lisaa(kayttaja);
            }
            fi.close();
        } catch (FileNotFoundException e) {
            throw new SailoException("Ei saa luettua tiedostoa " + tiedosto);
//        } catch (IOException e) {
//            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
       
    }
    
    
    /**
     * @throws SailoException jos tulee poikkeus
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonNimi());
    }
    
    
    /**
     * Tallentaa käyttävjät tiedostoon
     * @param tiednimi tiedostonnimmi
     * @throws SailoException jos tallennus epäonnistuu
     */
    public void tallenna(String tiednimi) throws SailoException {
        if (!muutettu) return;
        File ftied = new File(tiednimi);
        try(PrintStream fo = new PrintStream(new FileOutputStream(ftied, false))){
            for (int i = 0; i < getLkm(); i++) {
                Kayttaja kayttaja = anna(i);
                fo.println(kayttaja.toString());
            }
        } catch (FileNotFoundException ex) {
            throw new SailoException("Tiedosto" + ftied.getAbsolutePath() + " ei aukea");
        }
        muutettu = false;
    }
    
    
    /**
     * Poistetaan tietoja
     * @param id poistettavan tunnusnro 
     * @return 1 jos poistettiin, 0 jos ei 
     */
    public int poista(int id) {
        int ind = etsiId(id); 
        if (ind < 0) return 0; 
        lkm--; 
        for (int i = ind; i < lkm; i++) 
            alkiot[i] = alkiot[i + 1]; 
        alkiot[lkm] = null; 
        muutettu = true; 
        return 1; 
    }
    
    
    /**
     * Etsitään id 
     * @param id tunnusnro
     * @return lötyneen indeksi tai -1
     */
    public int etsiId(int id) {
        for (int i = 0; i < lkm; i++) 
            if (id == alkiot[i].getTunnusNro()) return i; 
        return -1; 
    }


    /**
     * @return tiedoston tallennukseen käytettävä nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }
    
    
    /**
     * @param nimi tallennustiedoston nimi
     */
    public void setTiedostonPerusNimi(String nimi) {
        tiedostonPerusNimi = nimi;
    }
    
    
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return getTiedostonPerusNimi() + ".dat";
    }


    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }
    
    
    /**
     * Luokka kayttajien iteroimiseksi.
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     * Kayttajat kayttajat = new Kayttajat();
     * Kayttaja maija1 = new Kayttaja(), maija2 = new Kayttaja();
     * maija1.rekisteroi(); maija2.rekisteroi();
     *
     * kayttajat.lisaa(maija1); 
     * kayttajat.lisaa(maija2); 
     * kayttajat.lisaa(maija1); 
     * 
     * StringBuffer sb = new StringBuffer(30);
     * for (Kayttaja kayttaja : kayttajat)  
     *   sb.append(" "+kayttaja.getTunnusNro());           
     * 
     * String tulos = " " + maija1.getTunnusNro() + " " + maija2.getTunnusNro() + " " + maija1.getTunnusNro();
     * 
     * sb.toString() === tulos; 
     * 
     * sb = new StringBuffer(30);
     * for (Iterator<Kayttaja>  i=kayttajat.iterator(); i.hasNext(); ) { 
     *   Kayttaja kayttaja = i.next();
     *   sb.append(" "+kayttaja.getTunnusNro());           
     * }
     * 
     * sb.toString() === tulos;
     * 
     * Iterator<Kayttaja>  i=kayttajat.iterator();
     * i.next() == maija1  === true;
     * i.next() == maija2  === true;
     * i.next() == maija1  === true;
     * 
     * i.next();  #THROWS NoSuchElementException
     *  
     * </pre>
     */
    public class KayttajatIterator implements Iterator<Kayttaja> {
        private int kohdalla = 0;

        /**
         * Onko olemassa vielä seuraavaa kayttajaa
         * @see java.util.Iterator#hasNext()
         * @return true jos on vielä kayttäjiä
         */
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }


        /**
         * Annetaan seuraava kayttaja
         * @return seuraava kayttaja
         * @throws NoSuchElementException jos seuraava alkiota ei enää ole
         * @see java.util.Iterator#next()
         */
        @Override
        public Kayttaja next() throws NoSuchElementException {
            if ( !hasNext() ) throw new NoSuchElementException("Ei oo");
            return anna(kohdalla++);
        }


        /**
         * Tuhoamista ei ole toteutettu
         * @throws UnsupportedOperationException aina
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Me ei poisteta");
        }
    }
    
    
    /**
     * Palautetaan iteraattori käyttäjistä
     * @return käyttäjä iteraattori 
     */
    @Override
    public Iterator<Kayttaja> iterator() {
        return new KayttajatIterator();
    }
    
    
    /**
     * @param hakuehto hakuehto 
     * @param k etsittävän kentän indeksi
     * @return tietorakenne löytyneistä käyttäjistä
     * @example 
     * <pre name="test"> 
     * #THROWS SailoException  
     *   Kayttajat kayttajat = new Kayttajat(); 
     *   Kayttaja kayttaja1 = new Kayttaja(); kayttaja1.parse("1|Mehilainen Maija|25|170|"); 
     *   Kayttaja kayttaja2 = new Kayttaja(); kayttaja2.parse("2|Batman|40|185|"); 
     *   Kayttaja kayttaja3 = new Kayttaja(); kayttaja3.parse("3|Supermies|30|180|"); 
     *   Kayttaja kayttaja4 = new Kayttaja(); kayttaja4.parse("4|Hämähäkkimies|25|190|"); 
     *   Kayttaja kayttaja5 = new Kayttaja(); kayttaja5.parse("5|Maajussi|60|170|"); 
     *   kayttajat.lisaa(kayttaja1); kayttajat.lisaa(kayttaja2); kayttajat.lisaa(kayttaja3); kayttajat.lisaa(kayttaja4); kayttajat.lisaa(kayttaja5);
     * </pre> 
     */ 
    
    public Collection<Kayttaja> etsi(String hakuehto, int k) {
        String ehto = "*"; 
        if ( hakuehto != null && hakuehto.length() > 0 ) ehto = hakuehto; 
        int hk = k; 
        if (hk < 0) hk = 1;
        List<Kayttaja> loytyneet = new ArrayList<Kayttaja>();
        for (Kayttaja kayttaja : this) {
            if (WildChars.onkoSamat(kayttaja.anna(hk), ehto))loytyneet.add(kayttaja);
        }
        
        return loytyneet;
    }
    
    
    /**
     * Testiohjelma
     * @param args ei käytössä
     */    
    public static void main(String[] args) {
        Kayttajat kayttajat = new Kayttajat();
        
        try {
            kayttajat.lueTiedostosta("kayttajat.dat");
        } catch (SailoException ex) {
            System.err.println("Ei voi lukea " + ex.getMessage());
        }
        
        
        Kayttaja maija = new Kayttaja();
        Kayttaja maija2 = new Kayttaja();
        
        maija.rekisteroi();
        maija.taytaMaijaMehilainenTiedoilla();
        maija2.rekisteroi();
        maija2.taytaMaijaMehilainenTiedoilla();
           
        kayttajat.lisaa(maija);
        kayttajat.lisaa(maija2);
      
      
        System.out.println("==================Käyttäjät testi=====================");
      
        for (int i = 0; i < kayttajat.getLkm(); i++) {
        Kayttaja kayttaja = kayttajat.anna(i);
        System.out.println("Käyttäjä indeksi: " + i);
        kayttaja.tulosta(System.out);
      }
        
        try {
            kayttajat.tallenna("kayttajat.dat");
        } catch (SailoException e) {     
            e.printStackTrace();
        }
        
    }
  
}


