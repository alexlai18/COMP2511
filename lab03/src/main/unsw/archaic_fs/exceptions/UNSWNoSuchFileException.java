package unsw.archaic_fs.exceptions;

public class UNSWNoSuchFileException extends java.nio.file.NoSuchFileException {
    public UNSWNoSuchFileException(String msg) {
        super(msg);
    }
}