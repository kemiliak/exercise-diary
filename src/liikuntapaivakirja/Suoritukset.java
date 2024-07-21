package liikuntapaivakirja;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Suoritusten lisääminen ja poisto
 * suoritusten lukeminen ja kirjoittaminen tiedostoon
 * suoritusten etsiminen ja lajittelu
 * @author kaisa
 * @version Apr 27, 2021
 *
 */
public class Suoritukset implements Iterable<Suoritus> {
    
    private boolean muutettu = false;
    private String tiedostonPerusNimi = "";
    
    private Collection<Suoritus> alkiot = new ArrayList<Suoritus>();
    
    
    /**
     * alustus
     */
    public Suoritukset() {
        //alustus on tehty jo
    }
     
    
    /**
     * Lisätään uusi suoritus tietorakenteeseen
     * @param suor lisättävä suoritus
     */
    public void lisaa(Suoritus suor) {
        alkiot.add(suor);
        muutettu = true;
    }


    /**
     * Etsitään käyttäjän suoritukset
     * @param tunnusnro ketä etsitään
     * @return lista löytyneistä suorituksista
     * @example
     * <pre name="test">
     * #import java.util.*;
     * 
     *  Suoritukset suoritukset = new Suoritukset();
     *  Suoritus suor1 = new Suoritus(2); suoritukset.lisaa(suor1);
     *  Suoritus suor2 = new Suoritus(1); suoritukset.lisaa(suor2);
     *  Suoritus suor3 = new Suoritus(2); suoritukset.lisaa(suor3);
     *  Suoritus suor4 = new Suoritus(1); suoritukset.lisaa(suor4);
     *  Suoritus suor5 = new Suoritus(2); suoritukset.lisaa(suor5);
     *  Suoritus suor6 = new Suoritus(5); suoritukset.lisaa(suor6);
     *
     *  List<Suoritus> loytyneet;
     *  loytyneet = suoritukset.annaSuoritukset(3);
     *  loytyneet.size() === 0;
     *  loytyneet = suoritukset.annaSuoritukset(1);
     *  loytyneet.size() === 2;
     *  loytyneet.get(0) == suor2 === true;
     *  loytyneet.get(1) == suor4 === true;
     *  loytyneet = suoritukset.annaSuoritukset(5);
     *  loytyneet.size() === 1;
     *  loytyneet.get(0) == suor6 === true;
     *  </pre>
     */
    public List<Suoritus> annaSuoritukset(int tunnusnro) {
        List<Suoritus> loydetyt = new ArrayList<Suoritus>();
        for(Suoritus suor : alkiot) 
            if(suor.getKayttajaNro() == tunnusnro) loydetyt.add(suor);
        return loydetyt;
    }
    
    
    
    /**
     * Lukee Suoritukset tiedostosta.
     * @param tied tiedoston nimen alkuosa
     * @throws SailoException jos lukeminen epäonnistuu
     * * @example
     * <pre name="test">
     * #THROWS SailoException
     * #THROWS IOException 
     * #import java.io.File;
     * #import java.io.IOException;
     *  Suoritukset suoritukset = new Suoritukset();
     *  Suoritus suor1 = new Suoritus(); suor1.taytaHiihto(2);
     *  Suoritus suor2 = new Suoritus(); suor2.taytaHiihto(1);
     *  Suoritus suor3 = new Suoritus(); suor3.taytaHiihto(2); 
     *  Suoritus suor4 = new Suoritus(); suor4.taytaHiihto(1); 
     *  Suoritus suor5 = new Suoritus(); suor5.taytaHiihto(2); 
     *  String hakemisto = "testit";
     *  String tiedNimi = hakemisto+"/suorituksia.dat";
     *  File dir = new File(hakemisto);
     *  dir.mkdir();
     *  File ftied = new File(tiedNimi);
     *  try {
     *      ftied.createNewFile(); 
     *  } catch (IOException e) {
     *      e.printStackTrace();
     *  }
     *  suoritukset.lisaa(suor1);
     *  suoritukset.lisaa(suor2);
     *  suoritukset.lisaa(suor3);
     *  suoritukset.lisaa(suor4);
     *  suoritukset.lisaa(suor5);
     *  suoritukset.tallenna();
     *  suoritukset = new Suoritukset();
     *  suoritukset.lueTiedostosta();
     *  Iterator<Suoritus> i = suoritukset.iterator();
     *  i.next().toString() === suor1.toString();
     *  i.next().toString() === suor2.toString();
     *  i.next().toString() === suor3.toString();
     *  i.next().toString() === suor4.toString();
     *  i.next().toString() === suor5.toString();
     *  i.hasNext() === false;
     *  suoritukset.lisaa(suor5);
     *  suoritukset.tallenna();
     *  ftied.delete() === true;
     *  dir.delete() === true;
     * </pre> 
     */
    public void lueTiedostosta(String tied) throws SailoException {

        try ( BufferedReader fi = new BufferedReader(new FileReader(tied)) ) {

            String rivi;
            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Suoritus suor = new Suoritus();
                suor.parse(rivi); 
                lisaa(suor);
            }
            muutettu = false;

        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + tied + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    }

    
    /**
     * PAlautetaan tiedoston nimi
     * @return tiedoston nimi
     */
    public String getTiedostonNimi() {
        return tiedostonPerusNimi + ".dat";
    }


    /**
     * Asetetaan tiedoston perusnimi
     * @param tied tiedosto
     */
    public void setTiedostonPerusNimi(String tied) {
        tiedostonPerusNimi = tied;
    }


    /**
     * Luetaan aikaisemmin annetun nimisestä tiedostosta
     * @throws SailoException jos tulee poikkeus
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonNimi());
    }
    
    
    /**
     * Palautetaan tiedostonperusnimi
     * @return tiedostonpreusnimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }
    
    /**
     * Lasketaan suoritusten lkm
     * @param tunnusNro tunnusnumero
     * @return suoritusten lukumäärä
     */
    public int getKayttajaSuorituksetLKM(int tunnusNro) {
        int n = 0;
        for (Iterator<Suoritus> it = alkiot.iterator(); it.hasNext();) {
            Suoritus suor = it.next();
            if ( suor.getKayttajaNro() == tunnusNro ) {
                n++;
            }  
        }
        return n;
    }
    
   
    /**
    * Lasketaan lajien lkm
    * @param tunnusNro tunnusnumero
    * @return lajien lukumäärä
    */
   public int getKayttajaLajitLKM(int tunnusNro) {
       List<String> lajit = new ArrayList<>();
       for (Iterator<Suoritus> it = alkiot.iterator(); it.hasNext();) {
           Suoritus suor = it.next();
           if ( suor.getKayttajaNro() == tunnusNro ) {
              lajit.add(suor.getLaji());
           }
       
       }
       HashSet<String> uniikitLajit = new HashSet<String>(lajit);
       return uniikitLajit.size();
   }
   
   
   /**
    * Lasketaan liikuntaan käytetty aika
    * @param tunnusNro käyttäjän tunnusnro
    * @return liikunta yhteensä minuuteissa
    */
   public int getKayttajaLiikuntaAika(int tunnusNro) {
       int aika = 0;
       for (Iterator<Suoritus> it = alkiot.iterator(); it.hasNext();) {
           Suoritus suor = it.next();
           if ( suor.getKayttajaNro() == tunnusNro ) {
               aika = aika + Integer.parseInt(suor.getPvm());
           }
       }
       return aika;
   }
   

    /**
     * Tallentaa suoritukset tiedostoon.
     * @throws SailoException jos talletus epäonnistuu
     */
    public void tallenna() throws SailoException {
        if ( !muutettu ) return;

        
        File ftied = new File(getTiedostonNimi());
        
        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            for (Suoritus suor : this) {
                fo.println(suor.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }

        muutettu = false;
    }
    

    /**
     * @param suoritus poistettava
     * @return true jos poistettava löytyi
     */
    public boolean poista(Suoritus suoritus) {
        boolean r = alkiot.remove(suoritus);
        if (r) muutettu = true;
        return r;
    }
    
    
    /**
     * @param tunnusNro viite, 
     * @return montako poistettiin
     */
    public int poistaKayttajanSuoritukset(int tunnusNro) {
        int n = 0;
        for (Iterator<Suoritus> it = alkiot.iterator(); it.hasNext();) {
            Suoritus suor = it.next();
            if ( suor.getKayttajaNro() == tunnusNro ) {
                it.remove();
                n++;
            }
        }
        if (n > 0) muutettu = true;
        return n;
    }

    /**
     * Iteraattori kaikkien suoritusten läpikäymiseen
     * @return suoritusiteraattori
     * 
     * @example
     * <pre name="test">
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     *  Suoritukset suoritukset = new Suoritukset();
     *  Suoritus suor1 = new Suoritus(2); suoritukset.lisaa(suor1);
     *  Suoritus suor2 = new Suoritus(1); suoritukset.lisaa(suor2);
     *  Suoritus suor3 = new Suoritus(2); suoritukset.lisaa(suor3);
     *  Suoritus suor4 = new Suoritus(1); suoritukset.lisaa(suor4);
     *  Suoritus suor5 = new Suoritus(2); suoritukset.lisaa(suor5);
     * 
     *  Iterator<Suoritus> i2=suoritukset.iterator();
     *  i2.next() === suor1;
     *  i2.next() === suor2;
     *  i2.next() === suor3;
     *  i2.next() === suor4;
     *  i2.next() === suor5;
     *  i2.next() === suor4;  #THROWS NoSuchElementException  
     *  
     *  int n = 0;
     *  int jnrot[] = {2,1,2,1,2};
     *  
     *  for ( Suoritus suor:suoritukset ) { 
     *    suor.getKayttajaNro() === jnrot[n]; n++;  
     *  }
     *  
     *  n === 5;
     *  
     * </pre>
     */
    @Override
    public Iterator<Suoritus> iterator() {
        return alkiot.iterator();
    }
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Suoritukset suoritukset = new Suoritukset();
        Suoritus suor1 = new Suoritus();
        suor1.taytaHiihto(2);
        Suoritus suor2 = new Suoritus();
        suor2.taytaHiihto(1);
        Suoritus suor3 = new Suoritus();
        suor3.taytaHiihto(2);
        Suoritus suor4 = new Suoritus();
        suor4.taytaHiihto(2);
        
        suoritukset.lisaa(suor1);
        suoritukset.lisaa(suor2);
        suoritukset.lisaa(suor3);
        suoritukset.lisaa(suor2);
        suoritukset.lisaa(suor4);
        
        System.out.println("=================== SUORITUKSET TESTI ====================");
        
        List<Suoritus> suoritukset2 = suoritukset.annaSuoritukset(2); 
        for (Suoritus suor:suoritukset2) 
            suor.tulosta(System.out); 
        
    }

    
}
