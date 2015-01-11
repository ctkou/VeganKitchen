package app.vegankitchen.exception;

/**
 * Created by Eden on 2014-12-26.
 */
public class NoSeasonalDishException extends Exception {
    public NoSeasonalDishException() {
        super("No seasonal dish was found from the database.");
    }
}
