package liikuntapaivakirja;

/**
 * @author kaisa
 * @version Apr 23, 2021
 *
 */
public class SailoException extends Exception {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * muodostaja, jolle tuodaan poikkeuksen viesti
     * @param viesti poikkeuksen viesti
     */
    public SailoException(String viesti) {
        super(viesti);
    }
    
}
