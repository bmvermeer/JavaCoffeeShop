package nl.brianvermeer.workshop.coffee.service;

import nl.brianvermeer.workshop.coffee.domain.Person;
import nl.brianvermeer.workshop.coffee.domain.Role;
import nl.brianvermeer.workshop.coffee.exception.EmailTakenException;
import nl.brianvermeer.workshop.coffee.exception.UsernameTakenException;
import nl.brianvermeer.workshop.coffee.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.security.GeneralSecurityException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EncryptionServiceDes encryptionService;


    public List<Person> getAllPersons() {
        return personRepository.findAll().stream().map(this::decryptPerson).collect(Collectors.toList());
    }

    public Person savePerson(Person person) {
        if (person.getPassword() != null && !person.getPassword().isEmpty()) {
            person.setEncryptedPassword(passwordEncoder.encode(person.getPassword()));
        }
        try {
            var address = person.getAddress();
            if (address != null) {
                person.setAddress(encryptionService.encrypt(person.getAddress()));
            }
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        return personRepository.save(person);
    }

    public Person findByUsername(String username) {
        return personRepository.findByUsername(username);
    }

    public List<Person> findByEmail(String email) {
        return personRepository.findByEmail(email);
    }

    public Person findById(Long id) {
        return personRepository.findById(id).get();
    }

    public void removePerson(Person person) {
        personRepository.delete(person);
    }

    public void removePerson(Long id) {
        personRepository.deleteById(id);
    }

    public Person registerNewPerson(Person person) throws EmailTakenException, UsernameTakenException {
        String username = person.getUsername();
        String email = person.getEmail();

        if (findByUsername(username) != null) {
            throw new UsernameTakenException("Username is already taken: " + username);
        }

        if (!findByEmail(email).isEmpty()) {
            throw new EmailTakenException("Email is already exists: " + email);
        }

        person.setRoles(Role.ROLE_CUSTOMER);

        return savePerson(person);
    }

    private Person decryptPerson(Person p) {
        try {
            var address = encryptionService.decrypt(p.getAddress());
            p.setAddress(address);
            return p;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
