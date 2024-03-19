package First.second;

public class ClientInfo {

    private String emailAddress;
    private String fileName;
    private String ClientName;

    public ClientInfo(String emailAddress, String fileName, String clientName) {
        this.emailAddress = emailAddress;
        this.fileName = fileName;
        ClientName = clientName;
    }

    public ClientInfo(String emailAddress, String fileName) {
        this.emailAddress = emailAddress;
        this.fileName = fileName;
    }

    public ClientInfo() {
    }

    public String getEmailAddress() {
        return emailAddress.trim();
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress.trim();
    }

    public String getFileName() {
        return fileName.trim();
    }

    public void setFileName(String fileName) {
        this.fileName = fileName.trim();
    }

    public String getClientName() {
        return ClientName.trim();
    }

    public void setClientName(String clientName) {
        ClientName = clientName.trim();
    }

    @Override
    public String toString() {
        return "ClientInfo { " +
                "emailAddress='" + emailAddress + '\'' +
                ", fileName='" + fileName + '\'' +
                ", ClientName='" + ClientName + '\'' +
                " }";
    }
}

