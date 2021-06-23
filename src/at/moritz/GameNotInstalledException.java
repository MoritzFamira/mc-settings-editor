package at.moritz;

public class GameNotInstalledException extends Exception {
    public GameNotInstalledException(String message) {
        super(message);
    }
}
