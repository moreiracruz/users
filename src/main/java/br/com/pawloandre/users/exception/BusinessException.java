package br.com.pawloandre.users.exception;

public class BusinessException extends RuntimeException {

    private final String code;
    private final String messagePt;
    private final String messageEn;
    private final String messageEs;

    public BusinessException(String code, String messagePt, String messageEn, String messageEs) {
        super(messageEn);
        this.code = code;
        this.messagePt = messagePt;
        this.messageEn = messageEn;
        this.messageEs = messageEs;
    }

    public String getCode() {
        return code;
    }

    public String getMessagePt() {
        return messagePt;
    }

    public String getMessageEn() {
        return messageEn;
    }

    public String getMessageEs() {
        return messageEs;
    }
}