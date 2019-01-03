package com.project.EhrRoute.Entities.Auth;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;


@Entity
@Table(name="verification_tokens")
public class VerificationToken
{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity=User.class, fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    private static final int expiration = 60*24; // Token Expiration time = 24 Hours

    private Date expiryDate;


    private Date calculateExpiryDate(int expiryTimeInMinutes)
    {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Timestamp(calendar.getTime().getTime())); // Set the calender time to current time
        calendar.add(Calendar.MINUTE, expiryTimeInMinutes); // Add the expiration time

        return new Date(calendar.getTime().getTime()); // Return the date
    }


    public VerificationToken() { }
    public VerificationToken(String token) {
        this.token = token;
        this.expiryDate = calculateExpiryDate(expiration);
    }
    public VerificationToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expiryDate = calculateExpiryDate(expiration);
    }


    public Long getId() {
        return id;
    }
    public User getUser() {
        return user;
    }
    public String getToken() {
        return token;
    }
    public Date getExpiryDate() {
        return expiryDate;
    }
    public static int getExpiration() {
        return expiration;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
