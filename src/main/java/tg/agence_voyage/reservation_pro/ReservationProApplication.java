package tg.agence_voyage.reservation_pro;

     import org.springframework.boot.SpringApplication;
     import org.springframework.boot.autoconfigure.SpringBootApplication;
     import org.springframework.context.annotation.ComponentScan;
     import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

     @SpringBootApplication
     @ComponentScan(basePackages = {"tg.agence_voyage.reservation_pro", "tg.agence.voyage.reservationpro"})
     @EnableJpaRepositories(basePackages = "tg.agence_voyage.reservation_pro")
     public class ReservationProApplication {
         public static void main(String[] args) {
             SpringApplication.run(ReservationProApplication.class, args);
         }
     }