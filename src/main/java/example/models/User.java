package example.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {

    @DynamoDBAttribute
    public Integer id;

    @DynamoDBAttribute
    public String username;
    @DynamoDBAttribute
    public String email;
    @DynamoDBAttribute
    public Integer age;

    @JsonIgnore
    @DynamoDBIgnore
    public String token;

    public static User build( String username, String email, Integer age) {
        User user = new User();
        user.username = username;
        user.email = email;
        user.age = age;
        return user;
    }
}