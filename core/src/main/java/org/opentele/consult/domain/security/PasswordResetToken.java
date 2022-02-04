package org.opentele.consult.domain.security;

import org.opentele.consult.domain.framework.AbstractEntity;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
public class PasswordResetToken extends AbstractEntity {
    private static final int EXPIRATION = 3 * 24;

    @Column(nullable = false)
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date expiryDate;

    protected PasswordResetToken() {
    }

    public PasswordResetToken(String token, User user) {
        this.token = token;
        this.user = user;
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.HOUR, EXPIRATION);
        expiryDate = instance.getTime();
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public enum TokenStatus {
        Expired, NotFound, Valid
    }
}
