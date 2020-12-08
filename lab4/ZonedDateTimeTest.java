import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Instant;

/**
 * ZonedDateTime tests
 */
public class ZonedDateTimeTest {
    public static void main(String[] args) {
        System.out.println(zonedDateTimeOf());
        System.out.println(zonedDateTimeParse());
        System.out.println(zonedDateTimeFormat());
        System.out.println(toPST());
        System.out.println(sameInstantAs());
        System.out.println(sameLocalAs());
    }

    static ZonedDateTime zonedDateTimeOf() {
        /**
         * Create a {@link ZonedDateTime} with time of 2015-07-10 2:14:25.000 as Japan Standard Time
         * by using {@link ZonedDateTime#of} and {@link ZoneId#of}
         */
       ZoneId id = ZoneId.of("Asia/Tokyo");
       ZonedDateTime zdt = ZonedDateTime.of(2015, 07, 10, 2, 14, 25, 000000000, id);
       return zdt;
    }

    static ZonedDateTime zonedDateTimeParse() {
        /**
         * Create a {@link ZonedDateTime} with time of 2015-06-18 23:07:25.000 as Japan Standard Time
         * by using {@link ZonedDateTime#parse}
         */
       
      
        ZonedDateTime zdt = ZonedDateTime.parse("2015-06-18T23:07:25+09:00[Asia/Tokyo]");
        return zdt;
    }

    static String zonedDateTimeFormat() {
        ZonedDateTime zdt = DateAndTimes.ZDT_20150618_23073050;

        /**
         * Format {@link zdt} to a {@link String} as "2015_06_18_23_07_30_JST"
         * by using {@link ZonedDateTime#format}
         */
         DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss_z");
        String formatedDateTime = zdt.format(format);
         return formatedDateTime;

           
    }

    static ZonedDateTime toPST() {
        LocalDateTime ldt = DateAndTimes.LDT_20150618_23073050;
        /**
         * Create a {@link ZonedDateTime} from {@link ldt} with Pacific Standard Time
         */
        ZoneId zoneId = ZoneId.of( "America/Los_Angeles" );
        ZonedDateTime zdt = ldt.atZone( zoneId );
        return zdt;
    }

    static ZonedDateTime sameInstantAs() {
        ZonedDateTime zdt = DateAndTimes.ZDT_20150618_23073050;
        /**
         * Create a {@link ZonedDateTime} same instant as {@link zdt} with Pacific Standard Time
         * by using {@link ZonedDateTime#withZoneSameInstant}
         */

         ZonedDateTime zdt2 = zdt.withZoneSameInstant(ZoneId.of("America/Los_Angeles"));

        return zdt2;
    }

    static ZonedDateTime sameLocalAs() {
        ZonedDateTime zdt = DateAndTimes.ZDT_20150618_23073050;
        /**
         * Create a {@link ZonedDateTime} same local time as {@link zdt} with Pacific Standard Time
         * by using {@link ZonedDateTime#withZoneSameLocal}
         */
        ZonedDateTime zdt2 = zdt.withZoneSameLocal(ZoneId.of("America/Los_Angeles"));

        return zdt2;
    }

    static class DateAndTimes {

        public static final LocalDateTime LDT_20150618_23073050 = LocalDateTime.of(2015, 6, 18, 23, 7, 30, 500000000);
        public static final ZonedDateTime
                ZDT_20150618_23073050 = ZonedDateTime.of(LDT_20150618_23073050, ZoneId.of("Asia/Tokyo"));
    }
}
