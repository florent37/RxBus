package florent37.github.com.rxsharedpreferences.model;

/**
 * Created by florentchampigny on 09/05/2017.
 */

public class User {
    private String pseudo;
    private int age;

    public User() {
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "pseudo='" + pseudo + '\'' +
                ", age=" + age +
                '}';
    }
}
