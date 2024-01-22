package com.sdu.usermanagement.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "token")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Token {
    public enum TokenType {
        BEARER
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Token_Id")
    private int id;

    @Column(name = "Token")
    private String token;

    @Column(name = "Token_Type")
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @Column(name = "Expired")
    private boolean expired;

    @Column(name = "Revoked")
    private boolean revoked;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
