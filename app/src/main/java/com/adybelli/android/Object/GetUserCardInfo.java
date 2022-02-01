package com.adybelli.android.Object;

public class GetUserCardInfo {
    private Boolean error;
    private Message message;
    private GetUserCardInfoBody body;

    public GetUserCardInfo(Boolean error, Message message, GetUserCardInfoBody body) {
        this.error = error;
        this.message = message;
        this.body = body;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public GetUserCardInfoBody getBody() {
        return body;
    }

    public void setBody(GetUserCardInfoBody body) {
        this.body = body;
    }
}
