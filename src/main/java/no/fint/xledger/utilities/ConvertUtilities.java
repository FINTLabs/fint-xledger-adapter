package no.fint.xledger.utilities;

public class ConvertUtilities {

    public static Long stringPriceToLongOre(String price) {
        return ((Double)(Double.parseDouble(price) * 100)).longValue();
    }
}
