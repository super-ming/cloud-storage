package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    @Autowired
    private final CredentialMapper credentialMapper;
    @Autowired
    private final AuthenticationService authenticationService;
    @Autowired
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, AuthenticationService authenticationService,
                             EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.authenticationService = authenticationService;
        this.encryptionService = encryptionService;
    }

    //checks if the credential user name already exists in the database
    public boolean getIsUserNameAvailable(String userName){
        User user = authenticationService.getAuthenticatedUser();
        Boolean userNameFound = credentialMapper.checkUserNameExists(user.getUserId(), userName) != null;
        return !userNameFound;
    }

    //returns the credential for the specified user
    public List<Credential> getCredentials(){
        User user = authenticationService.getAuthenticatedUser();
        return credentialMapper.getCredentials(user.getUserId());
    }

    //adds the credential fields to the database
    public int addCredential(CredentialForm credentialForm) throws SQLException {
        User user = authenticationService.getAuthenticatedUser();
        try {
            SecureRandom random = new SecureRandom();
            byte[] key = new byte[16];
            random.nextBytes(key);
            String encodedKey = Base64.getEncoder().encodeToString(key);
            encryptionService.encryptValue(credentialForm.getCredentialPassword(), encodedKey);
            Integer newCredentialId = credentialMapper.insertCredential(new Credential(null,
                    credentialForm.getCredentialUrl(), credentialForm.getCredentialUsername(), encodedKey,
                    credentialForm.getCredentialPassword(), user.getUserId()));
            if (newCredentialId > 1 ) {
                return newCredentialId;
            } else {
                throw new SQLException("Can't save changes to database");
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }
        return -1;
    }

    //updates the credential fields in the database
    public int editCredential(CredentialForm credentialForm) throws SQLException{
        User user = authenticationService.getAuthenticatedUser();
        try {
            Credential credential = credentialMapper.getCredential(user.getUserId(), credentialForm.getCredentialId());
            Integer updatedStatus = credentialMapper.editCredential(credentialForm.getCredentialId(),
                    credentialForm.getCredentialUrl(), credentialForm.getCredentialUsername(), credential.getKey(),
                    credentialForm.getCredentialPassword(), user.getUserId());
            if (updatedStatus > 0){
                return updatedStatus;
            } else {
                throw new SQLException("Can't save changes to database");
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
        return -1;
    }

    //delete the credential from the database
    public void deleteCredential(Integer credentialId) {
        User user = authenticationService.getAuthenticatedUser();
        credentialMapper.deleteCredential(user.getUserId(), credentialId);
    }
}
