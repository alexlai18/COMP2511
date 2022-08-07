package unsw.blackout;

public class File {
    private String fileName;
    private String content;
    private int fileLength;
    private boolean transferStatus;

    File(String fileName, String content, int fileLength, boolean transferStatus) {
        this.fileName = fileName;
        this.content = content;
        this.fileLength = fileLength;
        this.transferStatus = transferStatus;
    }

    public String getName() {
        return this.fileName;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String newContent) {
        this.content = newContent;
    }

    public void appendContent(String add) {
        this.content += add;
    }

    public void setFileLength() {
        this.fileLength = getContent().length();
    }

    public int getFileLength() {
        return this.fileLength;
    }

    public boolean getTransferStatus() {
        return this.transferStatus;
    }

    public void setTransferStatus(boolean b) {
        this.transferStatus = b;
    }
}
