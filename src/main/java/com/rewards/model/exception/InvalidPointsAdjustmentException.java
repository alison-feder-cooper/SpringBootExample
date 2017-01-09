package com.rewards.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid points adjustment amount request")
public class InvalidPointsAdjustmentException extends RuntimeException {

    private static final long serialVersionUID = -6791497609517443019L;

    public InvalidPointsAdjustmentException(String message) {
        super(message);
    }

    public static InvalidPointsAdjustmentException zeroPoints() {
        return new InvalidPointsAdjustmentException("Cannot adjust points by 0.");
    }

    public static InvalidPointsAdjustmentException withdrawalExceedsUserPoints(long userPoints,
                                                                               long deltaPoints) {
        return new InvalidPointsAdjustmentException("Cannot create a withdrawal greater than a " +
                "user's points. [userPoints=" + userPoints + ",transferPoints=" + deltaPoints);
    }
}
