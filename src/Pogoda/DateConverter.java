package Pogoda;

import javafx.util.StringConverter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Calendar;

public class DateConverter {

    public static StringConverter<Number> dateIntToString = new StringConverter<Number>() {
        @Override
        public java.lang.String toString(Number object) {
            Double time = (double)object;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time.longValue());
            Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
            return hour.toString();
        }

        @Override
        public Number fromString(java.lang.String string) {
            return null;
        }
    };

    public static long getDateAndTimeInLong (SingleWeatherData weatherData) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(weatherData.getYear(), weatherData.getMonth() - 1, weatherData.getDay(), weatherData.getHour(), 0);
        return calendar.getTimeInMillis();
    }

    public static long getDateInLong (SingleWeatherData weatherData) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(weatherData.getYear(), weatherData.getMonth() - 1, weatherData.getDay(), 0, 0);
        return calendar.getTimeInMillis();
    }

    public static LocalDateTime ConvertDate(String dateInStr) {
        StringBuilder stringBuilder = new StringBuilder(dateInStr);
        stringBuilder.setCharAt(10,'T');
        dateInStr = stringBuilder.toString();
        LocalDateTime dateAndTime = LocalDateTime.parse(dateInStr);
        return dateAndTime;
    }

    public static String ConvertDay(DayOfWeek dayWeekIndex) {
        switch (dayWeekIndex) {
            case MONDAY:
                return "Poniedziałek";
            case TUESDAY:
                return "Wtorek";
            case WEDNESDAY:
                return "Środa";
            case THURSDAY:
                return "Czwartek";
            case FRIDAY:
                return "Piątek";
            case SATURDAY:
                return "Sobota";
            case SUNDAY:
                return "Niedziela";
            default:
                throw new IllegalStateException("Unexpected value: " + dayWeekIndex);
        }
    }

}
