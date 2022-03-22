package cz.upce.fei.testovani;

import cz.upce.fei.testovani.shared.GenericResponse;
import cz.upce.fei.testovani.user.User;
import cz.upce.fei.testovani.user.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // aktivujeme kontext
class TestovaniApplicationTests {

    public static final String API_1_0_USERS = "/api/1.0/users";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    UserRepository userRepository;

    @Before
    public void cleanup(){
        userRepository.deleteAll();
    }

    @Test
    void postUser_whenUserIsValid_recieveOk() {
        User user = createUser();

        ResponseEntity<Object> response = testRestTemplate.postForEntity(API_1_0_USERS, user, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void postUser_whenUserIsValid_userSavedToDatabase() {
        User user = createUser();
        ResponseEntity<Object> response = testRestTemplate.postForEntity(API_1_0_USERS, user, Object.class);

        assertThat(userRepository.count()).isEqualTo(1);
    }

    @Test
    void postUser_whenUserIsValid_receiveSuccessMessage() {
        User user = createUser();
        ResponseEntity<GenericResponse> response = testRestTemplate.postForEntity(API_1_0_USERS, user, GenericResponse.class);

        assertThat(response.getBody().getMessage()).isNotNull();
    }

    @Test
    void postUser_whenUserIsValid_passwordIsHashedInDatabase() {
        User user = createUser();
        ResponseEntity<GenericResponse> response = testRestTemplate.postForEntity(API_1_0_USERS, user, GenericResponse.class);

        List<User> users = userRepository.findAll();
        User inDB = users.get(0);
        assertThat(inDB.getPassword()).isNotEqualToIgnoringCase(user.getPassword());
    }

    @Test
    void postUser_whenUserHasNullUserName_receivedBadRequest() {
        User user = createUser();
        user.setUsername(null);

        ResponseEntity<GenericResponse> response = testRestTemplate.postForEntity(API_1_0_USERS, user, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void postUser_whenUserHasNullDisplayName_receivedBadRequest() {
        User user = createUser();
        user.setDisplayname(null);

        ResponseEntity<GenericResponse> response = testRestTemplate.postForEntity(API_1_0_USERS, user, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void postUser_whenUserHasNullPassword_receivedBadRequest() {
        User user = createUser();
        user.setPassword(null);

        ResponseEntity<GenericResponse> response = testRestTemplate.postForEntity(API_1_0_USERS, user, GenericResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // TODO Pomoci dalsich constraint z odkazu https://beanvalidation.org/2.0/spec/ osetrete dalsi
    // vhodne hodnoty - napr. min. a max. delku uzivatelskeho jmena, slozeni hesla apod.
    // Co se tyka uvazovanych delek jmena i hesla, tak uvazujte i s limitem v DB

    private User createUser() {
        User user = new User();
        user.setUsername("test-user");
        user.setDisplayname("test-user");
        user.setPassword("P4ssword");
        return user;
    }


}
