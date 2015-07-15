package gh.com.zenithbank.ibank.Classes;

import java.util.List;

/**
 * Created by Robby on 7/8/2015.
 */
public class CardRequests_Class
{
    public static class ProcessVariable
    {
        public String name ;
        public String type ;
        public Object stringValue ;
        public String dateValue ;
    }

    public static class ProcessVariables
    {
        public List<ProcessVariable> processVariable ;
    }

    public static class ProcessStartRequest
    {
        public long key ;
        public String processName ;
        public ProcessVariables processVariables ;
        public boolean startingTasks ;
    }

    public static class RootObject
    {
        public ProcessStartRequest processStartRequest ;
    }

}
