package com.rewards.web.request;

import java.io.Serializable;

public class CreateTransferRequest implements Serializable {

    private static final long serialVersionUID = 6676226847474967342L;

    private long userId, delta;

    //TODO clean up default constructor; in for json serialization
    public CreateTransferRequest() {

    }

    public CreateTransferRequest(long userId, long delta) {
        this.userId = userId;
        this.delta = delta;
    }

    public long getUserId() {
        return userId;
    }

    public long getDelta() {
        return delta;
    }
}
