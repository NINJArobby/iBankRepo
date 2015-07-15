package gh.com.zenithbank.ibank.Classes;

/**
 * Created by Robby on 6/29/2015.
 */
public class Bill_Schedule_Class
{
    public static class RootObject
    {
        public BillPayment billPayment ;
    }

    public static class BillPayment
    {
        public int id ;
        public BillInformation billInformation ;
        public long pmtAcctNo ;
        public int amount ;
        public int status;
        public int Schedule_id;
        public String productName;
        public String schedule_dt;
    }

    public static class BillInformation
    {
        public String id ;
    }
}
