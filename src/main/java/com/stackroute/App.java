package com.stackroute;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);
        DataSource dataSource = context.getBean("dataSource", DataSource.class);
        //            Connection connection=dataSource.getConnection();
//            Statement statement=connection.createStatement();
//            statement.execute("Create table if not exists Customer" +
//                            "(id int,firstName varchar(20),lastname varchar(20))");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("Create table if not exists Customer" +
                "(id int,firstName varchar(20),lastname varchar(20))");
        jdbcTemplate.update("insert into Customer values(?,?,?)", 1, "Lionel", "Messi");
        String s = "2,Cristiano,Ronaldo 3,Neymar,Jr. 4,Kylien,Mpbape";
        List<Object[]> list = Arrays.asList(s.split(" ")).stream()
                .map(name -> name.split(","))
                .collect(Collectors.toList());
        jdbcTemplate.batchUpdate("insert into Customer values(?,?,?)", list);
        //jdbcTemplate.update("Delete from Customer where id in (1,2,3,4)");

        jdbcTemplate.update("Delete from Customer where id=?", 1);
       List<Customer> customerList=jdbcTemplate.query
                ("Select * from Customer",new customerMapper());
        for (Customer c:
             customerList) {
            System.out.println(c);
        }
    }
}
final class customerMapper implements RowMapper<Customer>
{

    @Override
    public Customer mapRow(ResultSet resultSet, int i) throws SQLException {
        Customer customer=new Customer(resultSet.getInt(1),
                resultSet.getString(2),resultSet.getString(3));
        return customer;
    }
}


