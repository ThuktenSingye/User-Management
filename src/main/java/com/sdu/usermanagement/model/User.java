package com.sdu.usermanagement.model;
import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class User implements UserDetails {
    
    public enum UserStatus {
        REGISTERED, ACCEPTED, ADMIN
    }
    /* Define field */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_Id")
    private int userId;

    @Column(name = "CID_No")
    private long cidNo;

    @Column(name = "Employee_Id")
    private int employeeId;

    @Column(name = "FName")
    private String firstName;

    @Column(name = "MName")
    private String middleName;

    @Column(name = "LName")
    private String lastName;

    @Column(name = "Mobile_No")
    private int mobileNo;

    @Column(name = "Email")
    private String email;

    @Column(name = "Password")
    private String password;

    @Column(name = "DOB")
    private Date dob;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", nullable = false)
    private UserStatus status;
    
    @ManyToOne(
        cascade = {
            CascadeType.DETACH, 
            CascadeType.PERSIST, 
            CascadeType.REFRESH, 
            CascadeType.MERGE
        }
    )
    @JoinColumn(name = "Gender_Id")
    private Gender gender;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Address_Id")
    private Address address;

    @ManyToOne(
        cascade = {
            CascadeType.DETACH, 
            CascadeType.PERSIST, 
            CascadeType.REFRESH, 
            CascadeType.MERGE
        }
    )
    @JoinColumn(name = "Section_Id")
    private Section section;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Image_Id")
    private ProfileImage profileImage;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Token> tokens;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", 
        joinColumns = @JoinColumn(name = "User_Id", referencedColumnName = "User_Id"), 
        inverseJoinColumns = @JoinColumn(name = "Role_Id", referencedColumnName = "Role_id")
    )
    private List<Role> roles;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName().name())).collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    @Override
    public boolean isAccountNonLocked() {
        return true;
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    public boolean isEnabled() {
        return true;
    }


 
}