
     package tg.agence_voyage.reservation_pro.client;

     import com.fasterxml.jackson.annotation.JsonIgnore;
     import jakarta.persistence.*;
     import lombok.Getter;
     import lombok.Setter;
     import org.springframework.security.core.GrantedAuthority;
     import org.springframework.security.core.authority.SimpleGrantedAuthority;
     import org.springframework.security.core.userdetails.UserDetails;
     import tg.agence_voyage.reservation_pro.reservation.Reservation;

     import java.sql.Date;
     import java.util.Collection;
     import java.util.Collections;
     import java.util.List;

     @Entity
     @Getter
     @Setter
     @Table(name = "client")
     public class Client implements UserDetails {

         @Id
         @GeneratedValue(strategy = GenerationType.IDENTITY)
         @Column(name = "id_client", nullable = false)
         private int idClient;

         @Column(name = "nom_client", nullable = false, length = 50)
         private String nomClient;

         @Column(name = "prenom_client", nullable = false, length = 75)
         private String prenomClient;

         @Column(name = "sexe_client", nullable = false, length = 10)
         private String sexeClient;

         @Column(name = "nationalite_client", nullable = true, length = 50)
         private String nationaliteClient;

         @Column(name = "date_naiss_client", nullable = true)
         private Date dateNaissClient;

         @Column(name = "mail_client", nullable = true, length = 100)
         private String mailClient;

         @Column(name = "tel_client", nullable = false, length = 20)
         private String telClient;

         @Column(name = "login_client", nullable = false, length = 100)
         private String loginClient;

         @Column(name = "mot_passe_client", nullable = false, length = 255)
         private String motPasseClient;

         @Column(name = "role", nullable = false, length = 20)
         private String role;

         @JsonIgnore
         @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
         private List<Reservation> reservations;

         @Override
         public Collection<? extends GrantedAuthority> getAuthorities() {
             return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
         }

         @Override
         public String getPassword() {
             return motPasseClient;
         }

         @Override
         public String getUsername() {
             return mailClient;
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

         @Override
         public int hashCode() {
             final int prime = 31;
             int result = 1;
             result = prime * result + idClient;
             return result;
         }

         @Override
         public boolean equals(Object obj) {
             if (this == obj)
                 return true;
             if (obj == null)
                 return false;
             if (getClass() != obj.getClass())
                 return false;
             Client other = (Client) obj;
             if (idClient != other.idClient)
                 return false;
             return true;
         }

         @Override
         public String toString() {
             return "Client [idClient=" + idClient + ", nomClient=" + nomClient + ", prenomClient=" + prenomClient
                     + ", sexeClient=" + sexeClient + ", nationaliteClient=" + nationaliteClient + ", dateNaissClient="
                     + dateNaissClient + ", mailClient=" + mailClient + ", telClient=" + telClient + ", loginClient="
                     + loginClient + ", motPasseClient=" + motPasseClient + ", role=" + role + "]";
         }
     }
     