package gh.com.zenithbank.ibank.Classes;

import java.util.List;

/**
 * Created by Robby on 7/13/2015.
 */
public class Country_Class
{
    public static class Translations
    {
        public String de ;
        public String es ;
        public String fr ;
        public String ja ;
        public String it ;
    }

    public static class RootObject
    {
        public String name ;
        public String capital ;
        public List<String> altSpellings ;
        public String relevance ;
        public String region ;
        public String subregion ;
        public Translations translations ;
        public int population ;
        public List<Integer> latlng ;
        public String demonym ;
        public int area ;
        public double gini ;
        public List<String> timezones ;
        public List<String> borders ;
        public String nativeName ;
        public List<String> callingCodes ;
        public List<String> topLevelDomain ;
        public String alpha2Code ;
        public String alpha3Code ;
        public List<String> currencies ;
        public List<String> languages ;
    }
}
