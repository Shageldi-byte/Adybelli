package com.adybelli.android.Object;

public class CreateOrderResponse {
    private boolean error;
    private Message message;
    private CreateOrderResponseBody body;

    public CreateOrderResponse(boolean error, Message message, CreateOrderResponseBody body) {
        this.error = error;
        this.message = message;
        this.body = body;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public CreateOrderResponseBody getBody() {
        return body;
    }

    public void setBody(CreateOrderResponseBody body) {
        this.body = body;
    }
}
