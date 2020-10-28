import db.tables.records.CityRecord;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.*;

import static db.tables.City.CITY;

public final class Main {

  public static void main(String[] args) {
    final Flyway flyway = Flyway.configure()
        .dataSource("jdbc:postgresql://127.0.0.1:5438/l06", "postgres", "PgSQL12")
        .locations("db")
        .load();
    flyway.clean();
    flyway.migrate();

    try (Connection connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5438/l06", "postgres", "PgSQL12"))
    {
      System.out.println("Connection Ok.");
      try (Statement stmt = connection.createStatement()) {
        int id = 1;
        String name = "Moscow";
        stmt.executeUpdate("INSERT INTO city(id,name) VALUES(" + id + ",'" + name + "')");
        id = 2;
        name = "Voronezh";
        stmt.executeUpdate("INSERT INTO city(id,name) VALUES(" + id + ",'" + name + "')");
      }  catch (SQLException e) {
        System.out.println(e.getMessage());
      }

      DSLContext dslContext = DSL.using(connection, SQLDialect.POSTGRES);

      Result<CityRecord> result = dslContext.selectFrom(CITY)
          .where(CITY.ID.eq(1))
          .fetch();

      for (CityRecord r : result) {
        Integer id = r.getId();
        String firstName = r.getName();
        System.out.println("ID: " + id + " first name: " + firstName);
      }
      
    }
    catch (SQLException e) {
      System.out.println("Connection failure.");
      e.printStackTrace();
    }


    System.out.println("Hello world.");

  }
}
